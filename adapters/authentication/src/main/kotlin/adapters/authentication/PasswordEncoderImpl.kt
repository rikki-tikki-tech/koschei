package adapters.authentication

import application.dependency.PasswordEncoder
import at.favre.lib.crypto.bcrypt.BCrypt
import domain.entity.user.Password
import domain.entity.user.PasswordHash

class PasswordEncoderImpl : PasswordEncoder {
    override fun encode(password: Password) = PasswordHash(BCrypt.withDefaults().hashToString(12, password.value.toCharArray()))

    override fun matches(
        password: Password,
        hash: PasswordHash,
    ) = BCrypt.verifyer().verify(password.value.toCharArray(), hash.value).verified
}
