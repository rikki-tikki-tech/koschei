package infrastructure

import adapters.config.modules
import application.dependency.Logger
import infrastructure.interceptors.LoggingAndExceptionTranslationServerInterceptor
import io.grpc.Server
import io.grpc.ServerBuilder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.logger.slf4jLogger
import ports.grpc.services.user.UserService

class Application(private val port: Int) : KoinComponent {
    private val logger: Logger by inject()
    private val userService: UserService by inject()

    private val server: Server =
        ServerBuilder
            .forPort(port)
            .intercept(LoggingAndExceptionTranslationServerInterceptor(logger))
            .addService(userService)
            .build()

    fun start() {
        server.start()

        logger.info("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                logger.info("*** shutting down gRPC server since JVM is shutting down")
                this@Application.stop()
                logger.info("*** server shut down")
            },
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }
}

fun main() {
    val config = loadConfig()

    startKoin {
        slf4jLogger()
        modules(modules(config))
    }

    val port = System.getenv("PORT")?.toInt() ?: 50051
    val server = Application(port)
    server.start()
    server.blockUntilShutdown()
}
