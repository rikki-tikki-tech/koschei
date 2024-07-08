package domain.entity.user

import domain.entity.Entity
import domain.entity.ValueClass
import domain.exception.EmailInvalidException
import domain.exception.PasswordInvalidException
import java.time.Instant
import java.time.LocalDate

data class User(
    override val id: String,
    val email: Email,
    val password: PasswordHash,
    val firstName: String? = null,
    val birthdayDate: LocalDate? = null,
    val createTime: Instant,
    val updateTime: Instant,
) : Entity<String>

data class Email(override val value: String) : ValueClass<String> {
    init {
        if (!value.contains('@')) throw EmailInvalidException()
    }
}

class NewPassword(value: String) : Password(value) {
    init {
        // Минимум одна заглавная буква (A-Z).
        // Минимум одна строчная буква (a-z).
        // Минимум одна цифра (0-9).
        // Минимум один специальный символ из набора #?!@$%^&*-.
        // Минимальная длина строки — 8 символов.
        if (!value.matches(Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-.]).{8,}\$"))) {
            throw PasswordInvalidException()
        }
    }

    override fun toString(): String {
        return "NewPassword()"
    }
}

open class Password(override val value: String) : ValueClass<String> {
    override fun toString(): String {
        return "Password()"
    }
}

data class PasswordHash(override val value: String) : ValueClass<String> {
    override fun toString(): String {
        return "PasswordHash()"
    }
}

data class Token(override val value: String) : ValueClass<String> {
    override fun toString(): String {
        return "Token()"
    }
}
