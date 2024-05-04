package com.example.patients.services

import com.example.patients.exceptions.PatientNotFoundException
import com.example.patients.models.Diagnosis
import com.example.patients.models.Patient
import com.example.patients.repositories.DiagnosisRepository
import com.example.patients.repositories.PatientRepository
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service

@Service
class DiagnosisService(
    val diagnosisRepository: DiagnosisRepository,
    val patientRepository: PatientRepository,
    val mongoTemplate: MongoTemplate,
) {

    fun getAllDiagnosesForPatient(patientId: String): List<Diagnosis> {
        // Validate patient ID existence
        val patient = patientRepository.findById(ObjectId(patientId))
            .orElseThrow { PatientNotFoundException("Patient with ID $patientId not found") }

        // Return diagnoses associated with the patient
        return patient.diagnosis ?: emptyList()
    }

//    fun createDiagnosisForPatient(patientId: String, diagnosisBody: String): Diagnosis {
//        // Validate patient ID existence
//        val patient = patientRepository.findById(ObjectId(patientId))
//            .orElseThrow { PatientNotFoundException("Patient with ID $patientId not found") }
//
//        // Create a new Diagnosis object
//        val diagnosis = Diagnosis(body = diagnosisBody)
//
//        // Associate the diagnosis with the patient
//        patient.diagnosis = (patient.diagnosis?.toMutableList() ?: mutableListOf()).apply { add(diagnosis) }
//
//        // Save the updated patient to ensure the diagnosis association is persisted
//        patientRepository.save(patient)
//
//        // Save the diagnosis to the database
//        return diagnosisRepository.save(diagnosis)
//    }


    fun createDiagnosis(diagnosisBody: String, patientId: String): Diagnosis {
        val diagnosis = diagnosisRepository.insert(Diagnosis(body = diagnosisBody))

        mongoTemplate.update(Patient::class.java)
            .matching(Criteria.where("_id").`is`(patientId))
            .apply(Update().push("diagnosis", diagnosis))
            .first()

        return diagnosis
    }

}