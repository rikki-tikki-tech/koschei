package application.dependency

import domain.entity.user.Password
import domain.entity.user.PasswordHash

interface PasswordEncoder {
    fun encode(password: Password): PasswordHash

    fun matches(
        password: Password,
        hash: PasswordHash,
    ): Boolean
}
