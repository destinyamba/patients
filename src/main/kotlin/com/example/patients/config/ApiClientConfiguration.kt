//package com.example.patients.config
//
//import com.docusign.esign.client.ApiClient
//import com.docusign.esign.client.auth.OAuth
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.core.env.Environment
//import org.springframework.stereotype.Component
//import java.nio.file.Files
//import java.nio.file.Path
//import java.nio.file.Paths
//
//
//@Component
//class ApiClientConfiguration {
//
//    @Value("\${docusign.accountId}")
//    private lateinit var accountId: String
//
//    @Value("\${docusign.userId}")
//    private lateinit var userId: String
//
//    @Value("\${docusign.basePath}")
//    private lateinit var basePath: String
//
//    @Value("\${docusign.integration-key}")
//    private lateinit var integrationKey: String
//
//    @Autowired
//    private val environment: Environment? = null
//    fun configureApiClient(): ApiClient? {
//        try {
//            val clientId = integrationKey
//            val host = basePath
//            val userId = userId
//            val rsaKeyFile = "core/main/resources/DocusignPrivateKey"
//            val path: Path = Paths.get(rsaKeyFile)
//            var rsaPrivateKey: ByteArray? = null
//            rsaPrivateKey = Files.readAllBytes(path)
//            val expiresIn = 1
//
//            val scopes = ArrayList<String>()
//            scopes.add("signature")
//            scopes.add("impersonation")
//
//
//            val apiClient = ApiClient(host)
//            val token: OAuth.OAuthToken =
//                apiClient.requestJWTUserToken(clientId, userId, scopes, rsaPrivateKey, expiresIn.toLong())
//            apiClient.setAccessToken(token.getAccessToken(), 3600L)
//
//
//            //For production get accounts and set the baseUri
//            val userInfo: OAuth.UserInfo = apiClient.getUserInfo(token.getAccessToken())
//
//            apiClient.setBasePath("https://na2.docusign.net/restapi")
//            apiClient.setBasePath(userInfo.getAccounts().get(0).getBaseUri() + "/restapi")
//            apiClient.addDefaultHeader("Authorization", "Bearer " + token.getAccessToken())
//
//            return apiClient
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return null
//        }
//    }
//}