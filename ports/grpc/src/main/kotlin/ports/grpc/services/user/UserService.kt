package ports.grpc.services.user

import application.model.CreateUserModel
import application.model.SignInUserModel
import application.model.UpdateUserModel
import application.model.UserModel
import application.usecases.user.AuthenticateUser
import application.usecases.user.CreateUser
import application.usecases.user.GetUser
import application.usecases.user.SignInUser
import application.usecases.user.UpdateUser
import com.google.protobuf.util.FieldMaskUtil
import domain.entity.user.Email
import domain.entity.user.NewPassword
import domain.entity.user.Password
import domain.entity.user.Token
import koschei.ports.grpc.proto.v1.AuthenticateUserRequest
import koschei.ports.grpc.proto.v1.AuthenticateUserResponse
import koschei.ports.grpc.proto.v1.GetUserRequest
import koschei.ports.grpc.proto.v1.SignInUserRequest
import koschei.ports.grpc.proto.v1.SignInUserResponse
import koschei.ports.grpc.proto.v1.SignUpUserRequest
import koschei.ports.grpc.proto.v1.UpdateUserRequest
import koschei.ports.grpc.proto.v1.User
import koschei.ports.grpc.proto.v1.UsersGrpcKt
import ports.grpc.utils.convertGoogleDateToLocalDate
import ports.grpc.utils.convertLocalDateToGoogleDate
import java.time.LocalDate

class UserService(
    private val getUser: GetUser,
    private val createUser: CreateUser,
    private val updateUser: UpdateUser,
    private val signInUser: SignInUser,
    private val authenticateUser: AuthenticateUser,
) : UsersGrpcKt.UsersCoroutineImplBase() {
    override suspend fun getUser(request: GetUserRequest): User {
        val userModel = getUser(request.name)
        return userModel.toGrpcUser()
    }

    override suspend fun signInUser(request: SignInUserRequest): SignInUserResponse {
        val token = signInUser(SignInUserModel(Email(request.email), Password(request.password)))

        return SignInUserResponse
            .newBuilder()
            .setToken(token)
            .build()
    }

    override suspend fun authenticateUser(request: AuthenticateUserRequest): AuthenticateUserResponse {
        val user = authenticateUser(Token(request.token))

        return AuthenticateUserResponse
            .newBuilder()
            .setName(user)
            .build()
    }

    override suspend fun signUpUser(request: SignUpUserRequest): User {
        val birthdayDate: LocalDate? =
            request.user.birthdayDate.takeIf {
                request.user.hasBirthdayDate()
            }?.let {
                convertGoogleDateToLocalDate(it)
            }

        val userModel =
            createUser(
                CreateUserModel(
                    email = Email(request.user.email),
                    password = NewPassword(request.user.password),
                    firstName = request.user.firstName,
                    birthdayDate = birthdayDate,
                ),
            )

        return userModel.toGrpcUser()
    }

    override suspend fun updateUser(request: UpdateUserRequest): User {
        val existingUser = getUser(request.user.name)

        val builder = existingUser.toGrpcUser().toBuilder()
        FieldMaskUtil.merge(request.updateMask, request.user, builder)

        val updatedUserProto = builder.build()

        val birthdayDate: LocalDate? =
            updatedUserProto.birthdayDate.takeIf {
                request.user.hasBirthdayDate()
            }?.let {
                convertGoogleDateToLocalDate(it)
            }

        val userModel =
            updateUser(
                UpdateUserModel(
                    id = updatedUserProto.name,
                    email = Email(updatedUserProto.email),
                    firstName = updatedUserProto.firstName,
                    birthdayDate = birthdayDate,
                ),
            )

        return userModel.toGrpcUser()
    }

    private fun UserModel.toGrpcUser(): User {
        val grpcUserBuilder =
            User.newBuilder()
                .setName(this.id)
                .setEmail(this.email.value)
                .setFirstName(this.firstName)

        this.birthdayDate?.let {
            grpcUserBuilder.setBirthdayDate(convertLocalDateToGoogleDate(it))
        }

        return grpcUserBuilder.build()
    }
}
