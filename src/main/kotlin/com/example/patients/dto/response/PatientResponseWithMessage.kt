package com.example.patients.dto.response

import com.example.patients.models.Patient
import org.apache.coyote.Response
import org.jetbrains.annotations.Nullable

data class PatientResponseWithMessage(val patientResponse: PatientResponse?, @Nullable val message: String?)
data class PatientResponseWithMessageForPatient(val patient: Patient?, val message: String? = null)
