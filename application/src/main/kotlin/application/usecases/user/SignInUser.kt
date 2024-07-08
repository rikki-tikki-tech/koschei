package application.usecases.user

import application.dependency.Authenticator
import application.dependency.Logger
import application.dependency.PasswordEncoder
import application.model.SignInUserModel
import application.model.UserModel
import application.usecases.Usecase
import domain.exception.SignInException
import domain.repository.user.UserRepository
import kotlin.reflect.typeOf

class SignInUser(
    logger: Logger,
    private val repository: UserRepository,
    private val authenticator: Authenticator,
    private val passwordEncoder: PasswordEncoder,
) : Usecase<SignInUserModel, String>(typeOf<SignInUserModel>(), typeOf<String>(), logger) {
    override suspend fun executor(signInUserModel: SignInUserModel): String {
        val user = repository.findByEmail(signInUserModel.email)
        if (user == null || !passwordEncoder.matches(signInUserModel.password, user.password)) {
            throw SignInException()
        }

        return authenticator.generate(UserModel(user))
    }
}
