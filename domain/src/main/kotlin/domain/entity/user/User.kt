package domain.entity.user

import domain.entity.Entity
import domain.entity.ValueClass
import domain.exception.EmailInvalidException
import domain.exception.InvalidPropertyException
import domain.exception.PasswordInvalidException
import io.konform.validation.Invalid
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern
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
) : Entity<String> {
    companion object {
        private val validateUser = Validation<User> {
            User::firstName ifPresent {
                minLength(2)
                maxLength(64)
            }
        }
    }

    init {
        val validationResult = validateUser(this)
        if (validationResult is Invalid) {
            throw InvalidPropertyException(validationResult.errors.toString())
        }
    }
}

data class Email(override val value: String) : ValueClass<String> {
    init {
        val validateEmail = Validation {
            Email::value {
                pattern(".+@.+\\..+")
                maxLength(256)
            }
        }

        val validationResult = validateEmail(this)
        if (validationResult is Invalid) {
            throw EmailInvalidException()
        }
    }
}

class NewPassword(value: String) : Password(value) {
    init {
        // Минимум одна заглавная буква (A-Z).
        // Минимум одна строчная буква (a-z).
        // Минимум одна цифра (0-9).
        // Минимум один специальный символ из набора #?!@$%^&*-.
        // Минимальная длина строки — 8 символов.
        // Максимальная длина строки — 256 символов.
        if (!value.matches(Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-.]).{8,256}\$"))) {
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
