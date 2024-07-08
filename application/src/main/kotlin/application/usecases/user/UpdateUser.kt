package application.usecases.user

import application.dependency.Logger
import application.model.UpdateUserModel
import application.model.UserModel
import application.usecases.Usecase
import application.utils.updateFields
import domain.exception.UserNotFoundException
import domain.repository.user.UserRepository
import java.time.Instant
import kotlin.reflect.typeOf

class UpdateUser(
    logger: Logger,
    private val repository: UserRepository,
) : Usecase<UpdateUserModel, UserModel>(typeOf<UpdateUserModel>(), typeOf<UserModel>(), logger) {
    private val updatableFields =
        setOf(
            "firstName",
            "birthdayDate",
            "updateTime",
        )

    override suspend fun executor(user: UpdateUserModel): UserModel {
        val old = repository.findById(user.id) ?: throw UserNotFoundException()

        return UserModel(
            repository.update(
                updateFields(
                    old,
                    user.toUser(old.password, old.createTime, Instant.now()),
                    updatableFields,
                ),
            ),
        )
    }
}
