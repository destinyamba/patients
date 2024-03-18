package com.example.patients.dto.response

import org.jetbrains.annotations.Nullable

data class PatientResponseWithMessage(val patientResponse: PatientResponse?, @Nullable val message: String?)