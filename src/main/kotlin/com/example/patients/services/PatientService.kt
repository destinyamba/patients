package com.example.patients.services

import com.example.patients.exceptions.PatientNotFoundException
import com.example.patients.models.Patient
import com.example.patients.repositories.PatientRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.util.*

@Service
class PatientService(val patientRepository: PatientRepository) {

    /**
     * This is to get a list of all the patients from the database.
     */
    fun getAllPatients(): List<Patient> {
        return patientRepository.findAll().filterNotNull()
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

}