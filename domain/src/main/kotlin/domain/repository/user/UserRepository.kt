package domain.repository.user

import domain.entity.user.Email
import domain.entity.user.User
import domain.repository.Repository

interface UserRepository : Repository<User, String> {
    suspend fun findByEmail(email: Email): User?
}
