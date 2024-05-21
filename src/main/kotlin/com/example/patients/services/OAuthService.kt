package com.example.patients.services

import com.docusign.esign.client.ApiClient
import com.docusign.esign.client.auth.OAuth
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class OAuthService(
    @Value("\${docusign.integrationKey}") private val integrationKey: String,
    @Value("\${docusign.userId}") private val userId: String,
    @Value("\${docusign.basePath}") private val basePath: String
) {

    private var oAuthToken: OAuth.OAuthToken? = null
    private val apiClient = ApiClient(basePath).setOAuthBasePath("account-d.docusign.com")

    fun getAccessToken(): String {
        if (oAuthToken == null || isTokenExpired()) {
            refreshAccessToken()
        }

        return oAuthToken!!.accessToken
    }

    private fun isTokenExpired(): Boolean {
        return oAuthToken!!.expiresIn * 1000L <= System.currentTimeMillis() + 60000
    }

    private fun refreshAccessToken() {
        try {
            val scopes = ArrayList<String>()
            scopes.add(OAuth.Scope_SIGNATURE)
            scopes.add(OAuth.Scope_IMPERSONATION)
            val privateKeyBytes = Files.readAllBytes(Paths.get("src/main/resources/rsaKey.pem"))

            oAuthToken = apiClient.requestJWTUserToken(
                integrationKey,
                userId,
                scopes,
                privateKeyBytes,
                3600L,
            )
        } catch (e: Exception) {
            throw RuntimeException("Error refreshing access token", e)
        }
    }
}
