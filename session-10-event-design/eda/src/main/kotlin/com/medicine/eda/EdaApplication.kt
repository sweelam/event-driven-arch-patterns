package com.medicine.eda

import com.medicine.eda.producer.MedicineProducer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class EdaApplication (val medicineProducer: MedicineProducer)
    : CommandLineRunner {
    override fun run(args: Array<String>) {
        medicineProducer.medicineUpdate()
    }
}

fun main(args: Array<String>) {
	runApplication<EdaApplication>(*args)
}
