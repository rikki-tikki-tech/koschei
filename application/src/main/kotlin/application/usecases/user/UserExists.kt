package application.usecases.user

import application.dependency.Logger
import application.usecases.Usecase
import domain.entity.user.Email
import domain.repository.user.UserRepository
import kotlin.reflect.typeOf

class UserExists(
    logger: Logger,
    private val repository: UserRepository,
) : Usecase<Email, Boolean>(typeOf<Email>(), typeOf<Boolean>(), logger) {
    override suspend fun executor(email: Email): Boolean {
        return repository.findByEmail(email) != null
    }
}
