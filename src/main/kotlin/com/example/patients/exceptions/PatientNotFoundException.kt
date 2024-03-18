package com.example.patients.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class PatientNotFoundException(id: String) : RuntimeException("Patient with id $id was not found")