package com.example.patients.controllers

import com.example.patients.dto.response.PatientResponse
import com.example.patients.dto.response.PatientResponseWithMessage
import com.example.patients.exceptions.PatientNotFoundException
import com.example.patients.services.PatientService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@RestController
@RequestMapping("/api/v1/patients")
class PatientController(val patientService: PatientService) {

    @GetMapping("/all")
    fun getAllPatients(): ResponseEntity<List<PatientResponse>> {
        val patients = patientService.getAllPatients().map(::PatientResponse)
        return ResponseEntity(patients, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getPatientById(@PathVariable id: String): ResponseEntity<PatientResponseWithMessage> {
        return try {
            val patient = patientService.getPatientById(id)
            ResponseEntity(PatientResponseWithMessage(patient?.let { PatientResponse(it) }, ""), HttpStatus.OK)
        } catch (ex: PatientNotFoundException) {
            ResponseEntity(PatientResponseWithMessage(null, "Patient not found with id: $id"),
                HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/{patientNum}/patientNum")
    fun getPatientByPatientNum(@PathVariable patientNum: String): ResponseEntity<PatientResponseWithMessage> {
        return try {
            val patient = patientService.getPatientByPatientNum(patientNum)
            ResponseEntity(PatientResponseWithMessage(patient?.let {PatientResponse(it)}, ""), HttpStatus.OK)
        } catch (ex: PatientNotFoundException) {
            ResponseEntity(PatientResponseWithMessage(null, "Patient not found with patient: $patientNum"),
                HttpStatus.NOT_FOUND)
        }
    }

}