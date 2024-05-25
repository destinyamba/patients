package com.example.patients.services

import com.example.patients.exceptions.PatientNotFoundException
import com.example.patients.models.Diagnosis
import com.example.patients.models.Medication
import com.example.patients.models.Patient
import com.example.patients.repositories.MedicationRepository
import com.example.patients.repositories.PatientRepository
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service

@Service
class MedicationService(
    val medicationRepository: MedicationRepository,
    val patientRepository: PatientRepository,
    val mongoTemplate: MongoTemplate,
) {

    fun getAllMedicationForPatient(patientId: String): List<Medication> {
        // Validate patient ID existence
        val patient = patientRepository.findById(ObjectId(patientId))
            .orElseThrow { PatientNotFoundException("Patient with ID $patientId not found") }

        // Return medication associated with the patient
        return patient.medication ?: emptyList()
    }

    fun addMedicationToPatient(patientId: String, body: Medication): Medication? {
        val patientOptional = patientRepository.findById(ObjectId(patientId))

        if (patientOptional.isPresent) {
            val savedMedication = medicationRepository.save(body)
            val patient = patientOptional.get()
            patient.medication = (patient.medication ?: listOf()) + savedMedication
            patientRepository.save(patient)
            return savedMedication
        }
        return null
    }
}