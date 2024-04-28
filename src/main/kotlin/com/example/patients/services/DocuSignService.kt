package com.example.patients.services

import com.example.patients.dto.request.EnvelopeRequestBody
import org.springframework.stereotype.Service
import com.docusign.esign.api.EnvelopesApi
import com.docusign.esign.client.ApiClient
import com.docusign.esign.model.*
import org.springframework.beans.factory.annotation.Value
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
                    recipientId = generateRandomUUID().toString()
                    clientUserId = generateRandomUUID().toString()
                }
                recipients = Recipients().apply {
                    signers = listOf(recipient)
                }
            }

            envelopesApi.createEnvelope(accountId, envelopeDefinition)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Failed to create envelope: ${e.message}")
        }
    }

    private fun generateRandomUUID(): UUID {
        return UUID.randomUUID()
    }
}