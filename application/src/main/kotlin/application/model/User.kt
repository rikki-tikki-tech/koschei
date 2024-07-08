package application.model

import application.dependency.PasswordEncoder
import domain.entity.user.Email
import domain.entity.user.NewPassword
import domain.entity.user.Password
import domain.entity.user.PasswordHash
import domain.entity.user.User
import java.time.Instant
import java.time.LocalDate

data class UserModel(
    val id: String,
    val email: Email,
    val firstName: String? = null,
    val birthdayDate: LocalDate? = null,
) {
    constructor(user: User) : this(user.id, user.email, user.firstName, user.birthdayDate)
}

data class CreateUserModel(
    val email: Email,
    val firstName: String? = null,
    val password: NewPassword,
    val birthdayDate: LocalDate? = null,
) {
    fun toUser(
        id: String,
        createTime: Instant,
        updateTime: Instant,
        encoder: PasswordEncoder,
    ) = User(
        id = id,
        email = email,
        password = encoder.encode(password),
        firstName = firstName,
        birthdayDate = birthdayDate,
        createTime = createTime,
        updateTime = updateTime,
    )
}

data class UpdateUserModel(
    val id: String,
    val email: Email,
    val firstName: String? = null,
    val birthdayDate: LocalDate? = null,
) {
    fun toUser(
        passwordHash: PasswordHash,
        createTime: Instant,
        updateTime: Instant,
    ) = User(
        id = id,
        email = email,
        password = passwordHash,
        firstName = firstName,
        birthdayDate = birthdayDate,
        createTime = createTime,
        updateTime = updateTime,
    )
}

data class SignInUserModel(
    val email: Email,
    val password: Password,
)
