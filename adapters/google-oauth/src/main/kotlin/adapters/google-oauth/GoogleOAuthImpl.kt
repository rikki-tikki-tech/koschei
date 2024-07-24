package adapters.google.oauth

import application.dependency.GoogleOAuth
import application.model.OAuthUserInfoModel
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.oauth2.Oauth2
import domain.entity.user.Email
import domain.exception.GoogleOAuthException

class GoogleOAuthImpl(
    private val clientId: String,
    private val clientSecret: String,
    private val redirectUri: String,
) : GoogleOAuth {
    override fun getUserInfo(codeToken: String): OAuthUserInfoModel {
        // TODO понять как лучше
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val jsonFactory = GsonFactory.getDefaultInstance()

        return try {
            val googleAuthorization =
                GoogleAuthorizationCodeTokenRequest(
                    httpTransport,
                    jsonFactory,
                    clientId,
                    clientSecret,
                    codeToken,
                    redirectUri,
                ).execute()

            val credential = GoogleCredential().setAccessToken(googleAuthorization.accessToken)
            val oauth2 =
                Oauth2.Builder(httpTransport, jsonFactory, credential)
                    .build()

            val userinfo = oauth2.userinfo().get().execute()

            OAuthUserInfoModel(
                email = Email(userinfo.email),
                firstName = userinfo.givenName,
            )
        } catch (e: Exception) {
            throw GoogleOAuthException(e.message)
        }
    }
}
