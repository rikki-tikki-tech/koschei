package infrastructure.modules

import adapters.authentication.JWTAuthenticatorImpl
import adapters.authentication.PasswordEncoderImpl
import adapters.config.Config
import adapters.config.JDBC
import adapters.config.JWTConfig
import adapters.logging.LoggerImpl
import adapters.postgresql.DatabaseUtils
import adapters.postgresql.repository.user.UserRepositoryImpl
import application.dependency.Authenticator
import application.dependency.Logger
import application.dependency.PasswordEncoder
import application.usecases.user.AuthenticateUser
import application.usecases.user.CreateUser
import application.usecases.user.GetUser
import application.usecases.user.SignInUser
import application.usecases.user.UpdateUser
import application.usecases.user.UserExists
import domain.repository.user.UserRepository
import org.jooq.DSLContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ports.grpc.services.user.UserService
import javax.sql.DataSource

private fun databaseModule(jdbc: JDBC) =
    module {
        single {
            DatabaseUtils.JDBC(
                url = jdbc.url,
                username = jdbc.username,
                password = jdbc.password,
                maximumPoolSize = jdbc.maximumPoolSize,
            )
        }

        singleOf(DatabaseUtils::createDataSource) {
            bind<DataSource>()
            createdAtStart()
        }

        singleOf(DatabaseUtils::createDSLContext) {
            bind<DSLContext>()
            createdAtStart()
        }
    }

private fun userModule(jwtConfig: JWTConfig) =
    module {
        singleOf(::UserRepositoryImpl) {
            bind<UserRepository>()
            createdAtStart()
        }

        single<Authenticator> {
            JWTAuthenticatorImpl(
                issuer = jwtConfig.domain,
                secret = jwtConfig.secret,
            )
        }

        singleOf(::PasswordEncoderImpl) {
            bind<PasswordEncoder>()
        }
    }

private fun loggerModule() =
    module {
        singleOf(::LoggerImpl) {
            bind<Logger>()
        }
    }

private fun usecasesModule() =
    module {
        factoryOf(::GetUser)
        factoryOf(::CreateUser)
        factoryOf(::UserExists)
        factoryOf(::UpdateUser)
        factoryOf(::SignInUser)
        factoryOf(::AuthenticateUser)
    }

private fun servicesModule() =
    module {
        factoryOf(::UserService)
    }

fun modules(config: Config): List<Module> {
    return listOf(
        databaseModule(config.jdbc),
        loggerModule(),
        userModule(config.jwt),
        usecasesModule(),
        servicesModule(),
    )
}
