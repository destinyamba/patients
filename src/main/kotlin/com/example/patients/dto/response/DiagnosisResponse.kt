package com.example.patients.dto.response

import com.example.patients.models.Diagnosis

data class DiagnosisResponse(
    val id: String,
    val body: String
) {
    constructor(diagnosis: Diagnosis) : this(
        id = diagnosis.id?.toHexString() ?: "",
        body = diagnosis.body
    )
}