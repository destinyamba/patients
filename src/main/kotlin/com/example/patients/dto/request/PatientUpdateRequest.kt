package com.example.patients.dto.request

data class UpdatePatientRequest(
    val name: String?,
    val age: Int?,
    val gender: String?,
    val address: String?,
    val phone: String?,
    val email: String?,
    val lastVisit: String?
)