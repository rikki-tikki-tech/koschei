@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package adapters.config

import application.usecases.UsecaseType
import domain.repository.Repository
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Kind
import org.koin.core.definition.KoinDefinition
import org.koin.core.instance.SingleInstanceFactory
import org.koin.core.module.Module
import org.koin.core.qualifier._q
import org.koin.dsl.bind
import org.reflections.Reflections
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

private const val USECASE_PACKAGE = "application.usecases"
private const val REPOSITORY_PACKAGE = "repositories"
private val usecases get() = Reflections(USECASE_PACKAGE).getSubTypesOf(UsecaseType::class.java)
private val repositories get() = Reflections(REPOSITORY_PACKAGE).getSubTypesOf(Repository::class.java)

fun Module.usecasesAndRepositories(domain: String) {
    repositories(domain)
    usecases(domain)
}

fun Module.usecases(domain: String) {
    usecases.map { it.kotlin }
        .filter { it.qualifiedName!!.startsWith("$USECASE_PACKAGE.$domain") }
        .forEach { uc ->
            usecase(uc)
        }
}

fun Module.repositories(domain: String) {
    repositories.map { it.kotlin }
        .filter { it.qualifiedName!!.startsWith("$REPOSITORY_PACKAGE.$domain") }
        .forEach { uc ->
            println(uc)
            repository(uc)
        }
}

fun Module.usecase(usecase: KClass<out UsecaseType<*>>): KoinDefinition<out UsecaseType<*>> {
    val factory = createFactory(usecase)
    indexPrimaryType(factory)
    return KoinDefinition(this, factory)
}

fun Module.repository(repository: KClass<out Repository<*, *>>): KoinDefinition<out Repository<*, *>> {
    val factory = createFactory(repository)
    indexPrimaryType(factory)
    val domain: KClass<in Repository<*, *>> =
        repository.supertypes.find { it.isSubtypeOf(typeOf<Repository<*, *>>()) }!!.jvmErasure as KClass<in Repository<*, *>>
    val koinDef = KoinDefinition(this, factory)
    koinDef.bind(domain)
    return koinDef
}

private fun <T : Any> Module.createFactory(clazz: KClass<T>): SingleInstanceFactory<T> {
    val def =
        BeanDefinition(
            _q("_root_"),
            clazz,
            null,
            {
                val constructor = clazz.constructors.first()
                val params =
                    constructor.parameters.map {
                        this.getOrNull<Any>(it.type.jvmErasure, null)
                            ?: throw IllegalArgumentException("Dependency not found for ${it.type}")
                    }

                constructor.call(*params.toTypedArray())
            },
            Kind.Singleton,
            secondaryTypes = emptyList(),
        )
    val factory = SingleInstanceFactory(def)
    indexPrimaryType(factory)
    return factory
}
