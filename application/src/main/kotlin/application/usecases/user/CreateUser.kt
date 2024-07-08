package application.usecases.user

import application.dependency.Logger
import application.dependency.PasswordEncoder
import application.model.CreateUserModel
import application.model.UserModel
import application.usecases.Usecase
import domain.exception.EmailAlreadyExistsException
import domain.repository.user.UserRepository
import java.time.Instant
import kotlin.random.Random
import kotlin.reflect.typeOf

private fun generateRandomId(): String {
    val min = 100_000_000
    val max = 999_999_999
    val randomNumber = Random.nextInt(min, max + 1)
    return randomNumber.toString()
}

class CreateUser(
    logger: Logger,
    private val repository: UserRepository,
    private val userExists: UserExists,
    private val passwordEncoder: PasswordEncoder,
) : Usecase<CreateUserModel, UserModel>(typeOf<CreateUserModel>(), typeOf<UserModel>(), logger) {
    public override suspend fun executor(user: CreateUserModel): UserModel {
        if (userExists(user.email)) throw EmailAlreadyExistsException()

        val currentTime = Instant.now()
        return UserModel(
            repository.create(
                user.toUser(
                    generateRandomId(),
                    createTime = currentTime,
                    updateTime = currentTime,
                    passwordEncoder,
                ),
            ),
        )
    }
}
