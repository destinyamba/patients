package com.example.patients.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "medication")
class Medication(
    @Id
    var id: ObjectId? = null,
    var body: String,
)