package infrastructure.interceptors

import application.dependency.Logger
import domain.exception.AlreadyExistsException
import domain.exception.AuthenticationException
import domain.exception.EmailAlreadyExistsException
import domain.exception.EmailInvalidException
import domain.exception.GoogleOAuthException
import domain.exception.InvalidPropertyException
import domain.exception.NotFoundException
import domain.exception.PasswordInvalidException
import domain.exception.SignInException
import domain.exception.UserNotFoundException
import io.grpc.ForwardingServerCall
import io.grpc.ForwardingServerCallListener
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import io.grpc.Status

class LoggingAndExceptionTranslationServerInterceptor(private val logger: Logger) : ServerInterceptor {
    private inner class ExceptionTranslatingServerCall<ReqT, RespT>(
        delegate: ServerCall<ReqT, RespT>,
    ) : ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(delegate) {
        override fun close(
            status: Status,
            trailers: Metadata,
        ) {
            val newStatus =
                if (!status.isOk) {
                    val cause = status.cause
                    logger.error("Closing due to error. Cause: ${cause?.javaClass?.simpleName}: ${cause?.message}")

                    if (status.code == Status.Code.UNKNOWN) {
                        when (cause) {
                            is InvalidPropertyException -> Status.INVALID_ARGUMENT.withDescription(cause.message)
                            is EmailInvalidException -> Status.INVALID_ARGUMENT.withDescription(cause.message)
                            is PasswordInvalidException -> Status.INVALID_ARGUMENT.withDescription(cause.message)
                            is EmailInvalidException -> Status.INVALID_ARGUMENT.withDescription(cause.message)
                            is GoogleOAuthException -> Status.INVALID_ARGUMENT.withDescription(cause.message)

                            is SignInException -> Status.UNAUTHENTICATED.withDescription(cause.message)
                            is AuthenticationException -> Status.UNAUTHENTICATED.withDescription(cause.message)

                            is UserNotFoundException -> Status.NOT_FOUND.withDescription(cause.message)
                            is NotFoundException -> Status.NOT_FOUND.withDescription(cause.message)

                            is AlreadyExistsException -> Status.ALREADY_EXISTS.withDescription(cause.message)
                            is EmailAlreadyExistsException -> Status.ALREADY_EXISTS.withDescription(cause.message)

                            else -> Status.UNKNOWN
                        }
                    } else {
                        status
                    }
                } else {
                    status
                }

            super.close(newStatus, trailers)
        }
    }

    private inner class LoggingServerCallListener<ReqT>(
        delegate: ServerCall.Listener<ReqT>,
    ) : ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(delegate) {
        override fun onMessage(message: ReqT) {
            try {
                super.onMessage(message)
            } catch (t: Throwable) {
                logger.error("Error handling message. $t")
            }
        }

        override fun onHalfClose() {
            try {
                super.onHalfClose()
            } catch (t: Throwable) {
                logger.error("Error handling half-close. $t")
                throw t
            }
        }

        override fun onCancel() {
            try {
                super.onCancel()
            } catch (t: Throwable) {
                logger.error("Error handling cancel. $t")
                throw t
            }
        }

        override fun onComplete() {
            try {
                super.onComplete()
            } catch (t: Throwable) {
                logger.error("Error handling complete. $t")
                throw t
            }
        }

        override fun onReady() {
            try {
                super.onReady()
            } catch (t: Throwable) {
                logger.error("Error handling ready. $t")
                throw t
            }
        }
    }

    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        metadata: Metadata,
        next: ServerCallHandler<ReqT, RespT>,
    ): ServerCall.Listener<ReqT> = LoggingServerCallListener(next.startCall(ExceptionTranslatingServerCall(call), metadata))
}
