package application.usecases.user

import application.dependency.Authenticator
import application.dependency.GoogleOAuth
import application.dependency.Logger
import application.model.CreateUserModel
import application.model.UserModel
import application.usecases.Usecase
import domain.entity.user.NewPassword
import domain.repository.user.UserRepository
import java.util.UUID
import kotlin.reflect.typeOf

class GoogleSignInOrSignUpUser(
    logger: Logger,
    private val repository: UserRepository,
    private val googleOAuth: GoogleOAuth,
    private val authenticator: Authenticator,
    private val createUser: CreateUser,
) : Usecase<String, String>(typeOf<String>(), typeOf<String>(), logger) {
    override suspend fun executor(codeToken: String): String {
        val userInfo = googleOAuth.getUserInfo(codeToken)

        val user = repository.findByEmail(userInfo.email)
        if (user != null) {
            logger.info("Google OAuth sign in ${userInfo.email}")
            return authenticator.generate(UserModel(user))
        }

        return authenticator.generate(
            createUser(
                CreateUserModel(
                    email = userInfo.email,
                    password = NewPassword(UUID.randomUUID().toString() + "aA1."),
                    firstName = userInfo.firstName,
                ),
            ),
        )
    }
}
