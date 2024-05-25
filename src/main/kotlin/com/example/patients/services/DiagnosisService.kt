package com.example.patients.services

import com.example.patients.exceptions.PatientNotFoundException
import com.example.patients.models.Diagnosis
import com.example.patients.repositories.DiagnosisRepository
import com.example.patients.repositories.PatientRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class DiagnosisService(
    val diagnosisRepository: DiagnosisRepository,
    val patientRepository: PatientRepository,
) {

    fun getAllDiagnosesForPatient(patientId: String): List<Diagnosis> {
        // Validate patient ID existence
        val patient = patientRepository.findById(ObjectId(patientId))
            .orElseThrow { PatientNotFoundException("Patient with ID $patientId not found") }

        // Return diagnoses associated with the patient
        return patient.diagnosis ?: emptyList()
    }

    fun addDiagnosisToPatient(patientId: String, body: Diagnosis): Diagnosis? {
        val patientOptional = patientRepository.findById(ObjectId(patientId))

        if (patientOptional.isPresent) {
            val savedDiagnosis = diagnosisRepository.save(body)
            val patient = patientOptional.get()
            patient.diagnosis = (patient.diagnosis ?: listOf()) + savedDiagnosis
            patientRepository.save(patient)
            return savedDiagnosis
        }
        return null
    }
}