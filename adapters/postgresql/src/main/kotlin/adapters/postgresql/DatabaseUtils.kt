package adapters.postgresql

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.MappedSchema
import org.jooq.conf.RenderMapping
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import javax.sql.DataSource

object DatabaseUtils {
    data class JDBC(
        val url: String,
        val username: String,
        val password: String,
        val maximumPoolSize: Int,
    )

    fun createDataSource(jdbc: JDBC): HikariDataSource {
        val hikariConfig =
            HikariConfig().apply {
                username = jdbc.username
                password = jdbc.password
                jdbcUrl = jdbc.url
                maximumPoolSize = jdbc.maximumPoolSize
            }

        return HikariDataSource(hikariConfig)
    }

    fun createDSLContext(dataSource: DataSource): DSLContext {
        val settings =
            Settings().withRenderMapping(
                RenderMapping().withSchemata(MappedSchema().withInput("public")),
            )

        return DSL.using(dataSource, SQLDialect.POSTGRES, settings)
    }
}
