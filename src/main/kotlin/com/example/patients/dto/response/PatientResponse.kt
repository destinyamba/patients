package com.example.patients.dto.response

import com.example.patients.models.Patient

data class PatientResponse(
    val id: String,
    val age: Int,
    val patientNum: String,
    val name: String?,
    val gender: String?,
    val address: String?,
    val phone: String?,
    val email: String?,
    val lastVisit: String?,
    val diagnosis: List<String>?,
    val medication: List<String>?,
) {
    constructor(patient: Patient) : this(
        id = patient.id.toString(),
        age = patient.age,
        patientNum = patient.patientNum,
        name = patient.name,
        gender = patient.gender,
        address = patient.address,
        phone = patient.phone,
        email = patient.email,
        lastVisit = patient.lastVisit,
        diagnosis = patient.diagnosis?.map { it.toString() },
        medication = patient.medication?.map { it.toString() }
    )
}