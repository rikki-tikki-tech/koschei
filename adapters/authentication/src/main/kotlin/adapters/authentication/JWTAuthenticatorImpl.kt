package adapters.authentication

import application.dependency.Authenticator
import application.model.UserModel
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import domain.entity.user.Token
import domain.exception.AuthenticationException

class JWTAuthenticatorImpl(
    private val issuer: String,
    secret: String,
) : Authenticator {
    private val algorithm: Algorithm = Algorithm.HMAC256(secret)

    private val verifier =
        JWT
            .require(algorithm)
            .withIssuer(issuer)
            .build()

    override fun generate(user: UserModel) =
        JWT
            .create()
            .withIssuer(issuer)
            .withSubject(user.id)
            .withClaim("email", user.email.value)
            .sign(algorithm)

    override fun verify(token: Token): String {
        return try {
            val decodedJWT = verifier.verify(token.value)
            decodedJWT.subject
        } catch (exception: JWTVerificationException) {
            throw AuthenticationException()
        }
    }
}
