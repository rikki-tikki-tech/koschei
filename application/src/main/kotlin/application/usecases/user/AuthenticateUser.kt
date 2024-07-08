package application.usecases.user

import application.dependency.Authenticator
import application.dependency.Logger
import application.usecases.Usecase
import domain.entity.user.Token
import kotlin.reflect.typeOf

class AuthenticateUser(
    logger: Logger,
    private val authenticator: Authenticator,
) : Usecase<Token, String>(typeOf<Token>(), typeOf<String>(), logger) {
    override suspend fun executor(token: Token): String {
        return authenticator.verify(token)
    }
}
