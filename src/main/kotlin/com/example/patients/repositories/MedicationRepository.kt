package com.example.patients.repositories

import com.example.patients.models.Medication
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MedicationRepository : MongoRepository<Medication, ObjectId?> {
}
