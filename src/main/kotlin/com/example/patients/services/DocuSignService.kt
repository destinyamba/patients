package com.example.patients.services

import com.docusign.esign.api.EnvelopesApi
import com.docusign.esign.client.ApiClient
import com.docusign.esign.model.*
import com.example.patients.dto.request.EnvelopeRequestBody
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class DocuSignService(
    private val apiClient: ApiClient,
    private val envelopesApi: EnvelopesApi
) {

    @Value("\${docusign.accountId}")
    private lateinit var accountId: String

    @Value("\${docusign.accessToken}")
    private lateinit var accessToken: String

    @Value("\${docusign.basePath}")
    private lateinit var basePath: String

    private val logger: Logger = LoggerFactory.getLogger(PatientService::class.java)

    fun createEnvelope(request: EnvelopeRequestBody) {
        apiClient.setBasePath("$basePath")
        apiClient.addDefaultHeader("Authorization", "Bearer $accessToken")

        try {
            val envelopeDefinition = EnvelopeDefinition().apply {
                emailSubject = "Patient Contract - Please Sign Document"
                status = "sent"

                val document = Document().apply {
                    documentBase64 = request.documentBase64
                    name = "PatientContract.pdf"
                    documentId = "1"
                }
                documents = listOf(document)

                val recipient = Signer().apply {
                    email = request.recipientEmail
                    name = request.recipientName
                    recipientId = request.recipientId
                    clientUserId = request.recipientId
                }
                recipients = Recipients().apply {
                    signers = listOf(recipient)
                }
            }

            envelopesApi.createEnvelope(accountId, envelopeDefinition)
            val envelopeSummary: EnvelopeSummary = envelopesApi.createEnvelope(accountId, envelopeDefinition)
            val envelopeId = envelopeSummary.envelopeId
            val recipientViewUrl = generateRecipientViewUrl(envelopeId, request)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Failed to create envelope: ${e.message}")
        }
    }

    private fun generateRecipientViewUrl(
        envelopeId: String,
        request: EnvelopeRequestBody
    ): String {
        val recipientViewRequest = RecipientViewRequest().apply {
            authenticationMethod = "email"
            email = request.recipientEmail
            clientUserId = request.recipientId
            userName = request.recipientName
            returnUrl = "https://www.example.com/callback"
        }

        val viewUrl = envelopesApi.createRecipientView(accountId, envelopeId, recipientViewRequest)
        logger.info("Embedded url: ${viewUrl.url}")
        return viewUrl.url
    }


    private fun generateRandomUUID(): UUID {
        return UUID.randomUUID()
    }
}
