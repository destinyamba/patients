package com.example.patients.services

import com.example.patients.dto.request.EnvelopeRequestBody
import com.example.patients.dto.request.UpdatePatientRequest
import com.example.patients.dto.response.PagedResponse
import com.example.patients.dto.response.PatientResponse
import com.example.patients.exceptions.PatientNotFoundException
import com.example.patients.exceptions.PatientPhoneNotFoundException
import com.example.patients.models.Patient
import com.example.patients.repositories.PatientRepository
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PatientService(val patientRepository: PatientRepository, val docuSignService: DocuSignService) {
    private val logger: Logger = LoggerFactory.getLogger(PatientService::class.java)

    /**
     * This is to get a list of all the patients from the database.
     */
    fun getAllPatients(pageNum: Int, pageSize: Int): PagedResponse<PatientResponse> {
        val allPatients = patientRepository.findAll()
        val totalPatients = allPatients.size
        val totalPages = (totalPatients + pageSize - 1) / pageSize

        val startIndex = (pageNum - 1) * pageSize
        val endIndex = (startIndex + pageSize).coerceAtMost(totalPatients)
        val paginatedPatients = if (startIndex < totalPatients) {
            allPatients.subList(startIndex, endIndex)
        } else {
            emptyList()
        }

        val patientResponses = paginatedPatients.map { PatientResponse(it) }

        return PagedResponse(
            patients = patientResponses,
            page = pageNum,
            pageSize = pageSize,
            totalItems = totalPatients,
            totalPages = totalPages
        )
    }


    /**
     * This is to get a patient by id from the database.
     */
    fun getPatientById(id: String): Patient? {
        if (!ObjectId.isValid(id)) {
            throw IllegalArgumentException("Invalid ObjectId format")
        }

        return patientRepository.findById(ObjectId(id))
            .orElseThrow { PatientNotFoundException(id) }
    }

    /**
     * This is to get a patient by id from the database.
     */
    fun getPatientByPatientNum(patientNum: String): Patient? {
        return patientRepository.findByPatientNum(patientNum) ?: throw PatientNotFoundException(
            "Patient not found with patientNum: $patientNum"
        )
    }

    /**
     * This is to create a patient and add to the database.
     */
    fun createPatient(patient: Patient): Patient {
        val patientNum = generatePatientNum()
        if (patientRepository.findByPhone(patient.phone) != null) {
            throw PatientNotFoundException("Patient with phone number ${patient.phone} already exists")
        }
        patient.patientNum = patientNum
        patient.lastVisit = LocalDateTime.now().toString()

        docuSignService.createEnvelope(
            EnvelopeRequestBody(
                recipientEmail = patient.email,
                recipientName = patient.name,
                isEmbeddedSigning = false
            )
        )
        logger.info("Creating envelope for ${patient.name} to ${patient.email} ")

        return patientRepository.save(patient)
    }

    private fun generatePatientNum(): String {
        var newPatientNum: String
        var isUnique: Boolean

        do {
            val randomDigits = (100..999).random() // Generate random 3-digit number
            newPatientNum = "P$randomDigits"

            // Check if the generated patient number already exists in the database
            val existingPatient = patientRepository.findByPatientNum(newPatientNum)
            isUnique = existingPatient == null
        } while (!isUnique)

        return newPatientNum
    }

    /**
     * This is to update a patient that exists in the database.
     */
    fun updatePatient(patientId: String, updateRequest: UpdatePatientRequest): Patient {
        val patient = patientRepository.findById(ObjectId(patientId))
            .orElseThrow { PatientNotFoundException("Patient with ID $patientId not found") }

        updateRequest.name?.let { patient?.name = it }
        updateRequest.age?.let { patient?.age = it }
        updateRequest.gender?.let { patient?.gender = it }
        updateRequest.address?.let { patient?.address = it }
        updateRequest.phone?.let { newPhoneNumber ->
            if (patientRepository.findByPhone(newPhoneNumber) != null && patient.phone != newPhoneNumber) {
                throw PatientPhoneNotFoundException("Phone number $newPhoneNumber is already in use")
            }
        }
        updateRequest.email?.let { patient?.email = it }
        updateRequest.lastVisit?.let { patient?.lastVisit = it }

        return patientRepository.save(patient)
    }

    /**
     * This is to delete a patient that exists in the database.
     */
    fun deletePatient(patientId: String): String {
        val patient = patientRepository.findById(ObjectId(patientId))
            .orElseThrow { PatientNotFoundException("Patient with ID $patientId not found") }

        patientRepository.delete(patient)

        return "Deleted successfully."
    }

}