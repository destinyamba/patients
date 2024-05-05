package com.example.patients.dto.response

import com.example.patients.models.Medication

data class MedicationResponse(
    val id: String,
    val body: String
) {
    constructor(medication: Medication) : this(
        id = medication.id.toString(),
        body = medication.body
    )
}