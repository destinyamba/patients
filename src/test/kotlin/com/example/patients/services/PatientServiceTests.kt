package com.example.patients.services

import com.example.patients.repositories.PatientRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import com.example.patients.Builders.Patient.PatientBuilder
import com.example.patients.dto.request.UpdatePatientRequest
import com.example.patients.exceptions.PatientNotFoundException
import com.example.patients.models.Patient
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import java.util.Optional

@SpringBootTest
class PatientServiceTests {

    private val patientRepository = mock<PatientRepository>()
    private val patientService = PatientService(patientRepository)

    @Test
    fun `test getAllPatients when repository returns non-null patients`() {
        // Mocking the behavior of findAll() to return a list with some non-null patients
        val patients = listOf(PatientBuilder(), PatientBuilder())
        doReturn(patients).`when`(patientRepository).findAll()

        // Calling the function under test
        val result = patientService.getAllPatients()

        // Asserting that the result matches the non-null patients from the repository
        assertEquals(patients, result)
    }


    @Test
    fun `test getAllPatients when repository returns null patients`() {
        // Mocking the behavior of findAll() to return a list with some null values
        val patients = listOf(PatientBuilder(), PatientBuilder(), null)
        doReturn(patients).`when`(patientRepository).findAll()

        // Calling the function under test
        val result = patientService.getAllPatients()

        // Asserting that the result contains only non-null patients
        assertEquals(listOf(PatientBuilder(), PatientBuilder()), result)
    }

    @Test
    fun `test getPatientById returns correct patient`() {
        val patientId = "65ed00000000000000000001"
        val patient = Patient(ObjectId(patientId), 25, "P001", "John", "Male", "Address", "1234567890", "john@example.com", "2024-03-18", emptyList(), emptyList())

        whenever(patientRepository.findById(ObjectId(patientId))).thenReturn(Optional.of(patient).map { it })

        // Calling the function under test
        val result = patientService.getPatientById(patientId)

        // Asserting that the returned patient matches the expected patient
        assertEquals(patient, result)
        assertEquals(patient.id, result?.id)
    }

    @Test
    fun `test getPatientById throws IllegalArgumentException for invalid patient ID`() {
        val invalidPatientId = "invalidId"

        // Calling the function under test should throw IllegalArgumentException
        assertThrows<IllegalArgumentException> {
            patientService.getPatientById(invalidPatientId)
        }
    }

    @Test
    fun `test updatePatient returns an updatedPatient`() {
        // initialize a patient
        val patientId = "65ed00000000000000000001"
        val updateRequest = UpdatePatientRequest(patientId, 250, "Male", "Address", "1234567890", "john@example.com", "2024-03-18")
        // initialize the expected result after update
        val patient = Patient(ObjectId(patientId), 25, "P001", "John", "Male", "Address", "1234567890", "john@example.com", "2024-03-18", emptyList(), emptyList())

        `when`(patientRepository.findById(ObjectId(patientId))).thenReturn(Optional.of(patient))
        `when`(updateRequest.phone?.let { patientRepository.findByPhone(it) }).thenReturn(null)
        `when`(patientRepository.save(patient)).thenReturn(patient)

        // call the service update function
       val result = patientService.updatePatient(patientId, updateRequest)

        // check the updated values match the expected result values
        assertEquals(result.age, updateRequest.age)
        assertEquals(result.gender, updateRequest.gender)
        assertEquals(result.address, updateRequest.address)
        assertEquals(result.phone, updateRequest.phone)
        assertEquals(result.email, updateRequest.email)
    }
}