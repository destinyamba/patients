package com.example.patients.Builders

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

class Patient private constructor(
    @Id
    var id: ObjectId?,
    var age: Int?,
    var patientNum: String?,
    var name: String?,
    var gender: String?,
    var address: String?,
    var phone: String?,
    var email: String?,
    var lastVisit: String?,
    val diagnosis: List<ObjectId>?,
    val medication: List<ObjectId>?
) {
    data class PatientBuilder(
        var id: ObjectId? = null,
        var age: Int? = null,
        var patientNum: String? = null,
        var name: String? = null,
        var gender: String? = null,
        var address: String? = null,
        var phone: String? = null,
        var email: String? = null,
        var lastVisit: String? = null,
        var diagnosis: List<ObjectId>? = null,
        var medication: List<ObjectId>? = null
    ) {
        fun id(id: ObjectId) = apply { this.id = id }
        fun age(age: Int) = apply { this.age = age }
        fun patientNum(patientNum: String) = apply { this.patientNum = patientNum }
        fun name(name: String) = apply { this.name = name }
        fun gender(gender: String) = apply { this.gender = gender }
        fun address(address: String) = apply { this.address = address }
        fun phone(phone: String) = apply { this.phone = phone }
        fun email(email: String) = apply { this.email = email }
        fun lastVisit(lastVisit: String) = apply { this.lastVisit = lastVisit }
        fun diagnosis(diagnosis: List<ObjectId>) = apply { this.diagnosis = diagnosis }
        fun medication(medication: List<ObjectId>) = apply { this.medication = medication }

        fun build() = Patient(
            id,
            age,
            patientNum,
            name,
            gender,
            address,
            phone,
            email,
            lastVisit,
            diagnosis,
            medication
        )
    }
}