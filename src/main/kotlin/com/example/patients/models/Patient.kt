package com.example.patients.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="patients")
class Patient(
    @Id
    var id: ObjectId,
    var age: Int,
    var patientNum: String,
    var name: String?,
    var gender: String?,
    var address: String?,
    var phone: String?,
    var email: String?,
    var lastVisit: String?,
    val diagnosis: List<ObjectId>?,
    val medication: List<ObjectId>?
)