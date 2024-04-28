package com.example.patients.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document(collection="patients")
class Patient(
    @Id
    var id: ObjectId? = null,
    var age: Int,
    var patientNum: String = "",
    var name: String,
    var gender: String?,
    var address: String?,
    var phone: String,
    var email: String,
    var lastVisit: String?,
    @DocumentReference
    var diagnosis: List<Diagnosis>?,
    val medication: List<ObjectId>?
)