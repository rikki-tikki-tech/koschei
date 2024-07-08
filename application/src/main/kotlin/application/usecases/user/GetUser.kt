package application.usecases.user

import application.dependency.Logger
import application.model.UserModel
import application.usecases.Usecase
import domain.exception.UserNotFoundException
import domain.repository.user.UserRepository
import kotlin.reflect.typeOf

// TODO Добавить ауентификацию
class GetUser(
    logger: Logger,
    private val repository: UserRepository,
) : Usecase<String, UserModel>(typeOf<String>(), typeOf<UserModel>(), logger) {
    override suspend fun executor(id: String): UserModel {
        return repository.findById(id)?.let { UserModel(it) } ?: throw UserNotFoundException()
    }
}
