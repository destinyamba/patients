package com.example.patients.repositories

import com.example.patients.models.Diagnosis
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface DiagnosisRepository : MongoRepository<Diagnosis, ObjectId?> {
}