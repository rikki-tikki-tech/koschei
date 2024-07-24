package adapters.config

data class Config(
    val development: Boolean,
    val jdbc: JDBC,
    val jwt: JWTConfig,
    val googleOAuth: GoogleOAuthConfig,
)

data class JDBC(
    val url: String,
    val schema: String,
    val username: String,
    val password: String,
    val maximumPoolSize: Int,
)

data class JWTConfig(
    val domain: String,
    val secret: String,
)

data class GoogleOAuthConfig(
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String,
)
