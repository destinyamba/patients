package com.example.patients.config

import com.docusign.esign.api.EnvelopesApi
import com.docusign.esign.client.ApiClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DocuSignConfig {
    @Value("\${docusign.basePath}")
    private lateinit var basePath: String

    @Value("\${docusign.accessToken}")
    private lateinit var accessToken: String

    @Bean
    fun apiClient(): ApiClient {
        val apiClient = ApiClient()
        apiClient.basePath = basePath
        apiClient.addDefaultHeader("Authorization", "Bearer $accessToken")
        return apiClient
    }

    @Bean
    fun envelopesApi(apiClient: ApiClient): EnvelopesApi {
        return EnvelopesApi(apiClient)
    }
}