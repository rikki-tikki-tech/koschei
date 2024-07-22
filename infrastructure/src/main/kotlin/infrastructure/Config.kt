package infrastructure

import adapters.config.Config
import adapters.config.JDBC
import adapters.config.JWTConfig
import com.typesafe.config.ConfigFactory
import io.github.cdimascio.dotenv.DotenvException
import io.github.cdimascio.dotenv.dotenv

fun loadConfig(): Config {
    try {
        val dotenv = dotenv()
        dotenv.entries().forEach { entry ->
            System.setProperty(entry.key, entry.value)
        }
    } catch (e: DotenvException) {
        println("Warning: .env file is missing or cannot be read.")
    }

    val config = ConfigFactory.load()

    return Config(
        development = config.getBoolean("development"),
        jdbc =
            JDBC(
                url = config.getString("jdbc.url"),
                schema = config.getString("jdbc.schema"),
                username = config.getString("jdbc.username"),
                password = config.getString("jdbc.password"),
                maximumPoolSize = config.getInt("jdbc.maximumPoolSize"),
            ),
        jwt =
            JWTConfig(
                domain = config.getString("jwt.domain"),
                secret = config.getString("jwt.secret"),
            ),
    )
}
