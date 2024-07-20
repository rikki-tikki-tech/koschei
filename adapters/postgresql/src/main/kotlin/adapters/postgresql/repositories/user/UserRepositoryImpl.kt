package repositories.user

import adapters.postgresql.generated.tables.daos.UserProfileDao
import adapters.postgresql.generated.tables.pojos.UserProfile
import adapters.postgresql.generated.tables.records.UserProfileRecord
import adapters.postgresql.repositories.AbstractRepository
import domain.entity.user.Email
import domain.entity.user.PasswordHash
import domain.entity.user.User
import domain.repository.user.UserRepository
import org.jooq.DSLContext
import java.time.ZoneOffset

class UserRepositoryImpl(dslContext: DSLContext) :
    UserRepository,
    AbstractRepository<User, String, UserProfile, UserProfileRecord, UserProfileDao>(UserProfileDao(dslContext.configuration())) {
    override suspend fun findByEmail(email: Email): User? {
        return dao.fetchOneByEmail(email.value)?.toDomain()
    }

    override fun UserProfile.toDomain() =
        User(
            id = this.id,
            email = Email(this.email),
            password = PasswordHash(this.password),
            firstName = this.firstName,
            createTime = this.createTime.toInstant(),
            updateTime = this.updateTime.toInstant(),
            birthdayDate = this.birthdayDate,
        )

    override fun User.toPersistenceModel(): UserProfile {
        val offset = ZoneOffset.UTC

        return UserProfile(
            id = this.id,
            email = this.email.value,
            password = this.password.value,
            firstName = this.firstName,
            createTime = this.createTime.atOffset(offset),
            updateTime = this.updateTime.atOffset(offset),
            birthdayDate = this.birthdayDate,
        )
    }
}
