package com.example.patients.controllers

import com.example.patients.dto.response.DiagnosisResponse
import com.example.patients.dto.response.MedicationResponse
import com.example.patients.models.Medication
import com.example.patients.services.MedicationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping


@RestController
@RequestMapping("/api/v1/patients/medication")
class MedicationController(val medicationService: MedicationService) {

    @GetMapping("/{patientId}")
    fun getAllMedicationForPatient(@PathVariable patientId: String): ResponseEntity<List<MedicationResponse>> {
        val medication = medicationService.getAllMedicationForPatient(patientId)
        val medicationResponse = medication.map { MedicationResponse(it) }
        return ResponseEntity(medicationResponse, HttpStatus.OK)
    }

    @PostMapping("/create/{patientId}")
    fun createMedication(
        @PathVariable patientId: String,
        @RequestBody medicationBody: Medication
    ): ResponseEntity<DiagnosisResponse> {
        val medication = medicationService.createMedication(patientId, medicationBody.toString())
        val diagnosisResponse = DiagnosisResponse(medicationBody.id.toString(), medicationBody.body)
        return ResponseEntity(diagnosisResponse, HttpStatus.CREATED)
    }
}