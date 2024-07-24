package application.dependency

import application.model.OAuthUserInfoModel

interface GoogleOAuth {
    fun getUserInfo(codeToken: String): OAuthUserInfoModel
}
