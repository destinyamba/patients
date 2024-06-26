package com.example.patients.services

import com.docusign.esign.api.EnvelopesApi
import com.docusign.esign.model.Document
import com.docusign.esign.model.EnvelopeDefinition
import com.docusign.esign.model.EnvelopeSummary
import com.docusign.esign.model.RecipientViewRequest
import com.docusign.esign.model.Recipients
import com.docusign.esign.model.Signer
import com.example.patients.dto.request.EnvelopeRequestBody
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class DocuSignService @Autowired constructor(
    private val envelopesApi: EnvelopesApi,
    private val oAuthService: OAuthService
) {

    @Value("\${docusign.accountId}")
    private lateinit var accountId: String

    private val logger: Logger = LoggerFactory.getLogger(PatientService::class.java)

    fun createEnvelope(request: EnvelopeRequestBody): String? {
        val accessToken = oAuthService.getAccessToken()
        val apiClient = envelopesApi.apiClient
        apiClient.addDefaultHeader("Authorization", "Bearer $accessToken")

        return try {
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
                    recipientId = "1"
                    if (request.isEmbeddedSigning) {
                        clientUserId = request.recipientId
                    }
                }
                recipients = Recipients().apply {
                    signers = listOf(recipient)
                }
            }

            val envelopeSummary: EnvelopeSummary = envelopesApi.createEnvelope(accountId, envelopeDefinition)
            val envelopeId = envelopeSummary.envelopeId

            if (request.isEmbeddedSigning) {
                return generateRecipientViewUrl(envelopeId, request)
            } else {
                return envelopeId
            }
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
}
