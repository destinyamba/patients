package com.example.patients.controllers

import com.example.patients.dto.response.DiagnosisResponse
import com.example.patients.models.Diagnosis
import com.example.patients.services.DiagnosisService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/patients/diagnosis")
class DiagnosisController(val diagnosisService: DiagnosisService) {

    @GetMapping("/{patientId}")
    fun getAllDiagnosesForPatient(@PathVariable patientId: String): ResponseEntity<List<DiagnosisResponse>> {
        val diagnosis = diagnosisService.getAllDiagnosesForPatient(patientId)
        val diagnosisResponse = diagnosis.map { DiagnosisResponse(it) }
        return ResponseEntity(diagnosisResponse, HttpStatus.OK)
    }

    @PostMapping("/create/{patientId}")
    fun createDiagnosis(
        @PathVariable patientId: String,
        @RequestBody diagnosis: Diagnosis
    ): ResponseEntity<DiagnosisResponse> {
        val diagnosisValue = diagnosisService.addDiagnosisToPatient(patientId, diagnosis)
        val diagnosisResponse = diagnosisValue?.let { DiagnosisResponse(it) }
        return ResponseEntity(diagnosisResponse, HttpStatus.CREATED)
    }
}