package com.example.patients.controllers

import com.example.patients.dto.request.UpdatePatientRequest
import com.example.patients.dto.response.PagedResponse
import com.example.patients.dto.response.PatientResponse
import com.example.patients.dto.response.PatientResponseWithMessage
import com.example.patients.exceptions.PatientNotFoundException
import com.example.patients.exceptions.PatientPhoneNotFoundException
import com.example.patients.models.Patient
import com.example.patients.services.DiagnosisService
import com.example.patients.services.PatientService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/patients")
class PatientController(val patientService: PatientService, val diagnosisService: DiagnosisService) {

    @GetMapping("/all")
    fun allPatients(
        @RequestParam(required = false, defaultValue = "1") pageNum: Int,
        @RequestParam(required = false, defaultValue = "12") pageSize: Int
    ): ResponseEntity<PagedResponse<PatientResponse>> {
        val response = patientService.getAllPatients(pageNum, pageSize)
        return ResponseEntity.ok(response)
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

    @GetMapping("/{patientNum}/patient")
    fun getPatientByPatientNum(@PathVariable patientNum: String): ResponseEntity<PatientResponseWithMessage> {
        return try {
            val patient = patientService.getPatientByPatientNum(patientNum)
            ResponseEntity(PatientResponseWithMessage(patient?.let {PatientResponse(it)}, ""), HttpStatus.OK)
        } catch (ex: PatientNotFoundException) {
            ResponseEntity(PatientResponseWithMessage(null, "Patient not found with patient: $patientNum"),
                HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/create")
    fun createPatient(@RequestBody patient: Patient): ResponseEntity<Any> {
        return try {
            val createdPatient = patientService.createPatient(patient)
            val response = PatientResponse(createdPatient)
            ResponseEntity(response, HttpStatus.CREATED)
        } catch (ex: PatientNotFoundException) {
            val errorMessage = "Patient with phone number '${patient.phone}' already exists."
            ResponseEntity(PatientResponseWithMessage(null, errorMessage), HttpStatus.CONFLICT)
        }
    }

    @PutMapping("/{patientId}")
    fun updatePatient(
        @PathVariable patientId: String,
        @RequestBody updateRequest: UpdatePatientRequest
    ): ResponseEntity<Any> {
        return try {
            val updatedPatient = patientService.updatePatient(patientId, updateRequest)
            val response = PatientResponse(updatedPatient)
            return ResponseEntity(response, HttpStatus.OK)
        } catch (ex: PatientNotFoundException ) {
            val errorMessage = "Patient with id '${patientId}' not found."
            ResponseEntity(PatientResponseWithMessage(null, errorMessage), HttpStatus.NOT_FOUND)
        }catch (ex: PatientPhoneNotFoundException) {
            val errorMessage = "Patient with phone number '${updateRequest.phone}' already exists."
            ResponseEntity(PatientResponseWithMessage(null, errorMessage), HttpStatus.CONFLICT)
        }
    }

    @DeleteMapping("/{patientId}")
    fun deletePatient(@PathVariable patientId: String): ResponseEntity<Any> {
        return try {
            patientService.deletePatient(patientId)
            return ResponseEntity("Deleted successfully", HttpStatus.OK)
        } catch (ex: PatientNotFoundException) {
            val errorMessage = "Patient with id '${patientId}' does not exist."
            ResponseEntity(PatientResponseWithMessage(null, errorMessage), HttpStatus.CONFLICT)
        }
    }

}