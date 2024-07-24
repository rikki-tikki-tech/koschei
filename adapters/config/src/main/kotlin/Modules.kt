package adapters.config

import adapters.authentication.JWTAuthenticatorImpl
import adapters.authentication.PasswordEncoderImpl
import adapters.google.oauth.GoogleOAuthImpl
import adapters.logging.LoggerImpl
import adapters.postgresql.DatabaseUtils
import application.dependency.Authenticator
import application.dependency.GoogleOAuth
import application.dependency.Logger
import application.dependency.PasswordEncoder
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

private fun commonModule() =
    module {
        singleOf(::LoggerImpl) {
            bind<Logger>()
        }
    }

private fun userModule(config: Config) =
    module {
        usecasesAndRepositories("user")

        factoryOf(::UserService)

        single<Authenticator> {
            JWTAuthenticatorImpl(
                issuer = config.jwt.domain,
                secret = config.jwt.secret,
            )
        }

        single<GoogleOAuth> {
            GoogleOAuthImpl(
                clientId = config.googleOAuth.clientId,
                clientSecret = config.googleOAuth.clientSecret,
                redirectUri = config.googleOAuth.redirectUri,
            )
        }

        singleOf(::PasswordEncoderImpl) {
            bind<PasswordEncoder>()
        }
    }

fun modules(config: Config): List<Module> {
    return listOf(
        databaseModule(config.jdbc),
        commonModule(),
        userModule(config),
    )
}
