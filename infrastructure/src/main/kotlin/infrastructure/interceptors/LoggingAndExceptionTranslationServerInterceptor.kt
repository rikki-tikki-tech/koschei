package infrastructure.interceptors

import application.dependency.Logger
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
                    logger.error("Closing due to error. $cause")

                    if (status.code == Status.Code.UNKNOWN) {
                        val newStatus =
                            when (cause) {
                                is IllegalArgumentException -> Status.INVALID_ARGUMENT
                                is IllegalStateException -> Status.FAILED_PRECONDITION
                                else -> Status.UNKNOWN
                            }
                        newStatus.withDescription(cause?.message).withCause(cause)
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
