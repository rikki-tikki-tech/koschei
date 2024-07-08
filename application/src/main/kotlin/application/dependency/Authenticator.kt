package application.dependency

import application.model.UserModel
import domain.entity.user.Token

interface Authenticator {
    fun generate(user: UserModel): String

    fun verify(token: Token): String
}
