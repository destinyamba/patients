package com.example.patients

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PatientsApplication

fun main(args: Array<String>) {
	runApplication<PatientsApplication>(*args)
}
