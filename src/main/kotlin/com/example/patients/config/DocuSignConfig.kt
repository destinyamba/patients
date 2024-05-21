package com.example.patients.config

import com.docusign.esign.api.EnvelopesApi
import com.docusign.esign.client.ApiClient
import com.example.patients.services.OAuthService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DocuSignConfig(val oAuthService: OAuthService) {
    @Value("\${docusign.basePath}")
    private lateinit var basePath: String

    @Bean
    fun apiClient(): ApiClient {
        val apiClient = ApiClient()
        apiClient.basePath = basePath
        val accessToken = oAuthService.getAccessToken()
        apiClient.addDefaultHeader("Authorization", "Bearer $accessToken")
        return apiClient
    }

    @Bean
    fun envelopesApi(apiClient: ApiClient): EnvelopesApi {
        return EnvelopesApi(apiClient)
    }
}