package com.example.patients.repositories

import com.example.patients.models.Patient
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface PatientRepository : MongoRepository<Patient?, ObjectId?> {
    fun findByPatientNum(patientNum: String?): Patient?
    fun deleteByPatientNum(patientNum: String?): Patient?
    fun findByNameContainingIgnoreCase(name: String?): Patient?
    fun findByPhone(phone: String): Patient?
    fun findFirstByOrderByPatientNumDesc(): Patient?
}