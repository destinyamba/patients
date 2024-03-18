package com.example.patients.services

import com.example.patients.exceptions.PatientNotFoundException
import com.example.patients.models.Patient
import com.example.patients.repositories.PatientRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.LocalDateTime
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

        return patientRepository.save(patient)
    }

    private fun generatePatientNum(): String {
        var newPatientNum: String
        var isUnique = false

        do {
            val randomDigits = (100..999).random() // Generate random 3-digit number
            newPatientNum = "P$randomDigits"

            // Check if the generated patient number already exists in the database
            val existingPatient = patientRepository.findByPatientNum(newPatientNum)
            isUnique = existingPatient == null
        } while (!isUnique)

        return newPatientNum
    }

}