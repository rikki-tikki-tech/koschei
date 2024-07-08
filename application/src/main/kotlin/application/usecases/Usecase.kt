package application.usecases

import application.dependency.Logger
import kotlin.reflect.KType

sealed class UsecaseType<R : Any>(
    val result: KType,
    private val logger: Logger,
) {
    abstract val args: List<KType>
    open val logging = true

    protected fun before(vararg args: Any) {
        if (logging) {
            logger.info(
                "${this::class.simpleName!!}: ${
                    args.joinToString(" | ")
                }",
            )
        }
    }
}

abstract class Usecase<A : Any, R : Any>(
    private val a: KType,
    result: KType,
    logger: Logger,
) : UsecaseType<R>(result, logger), suspend (A) -> R {
    final override val args get() = listOf(a)

    protected abstract suspend fun executor(a: A): R

    override suspend fun invoke(a: A): R {
        before(a)
        return executor(a)
    }
}
