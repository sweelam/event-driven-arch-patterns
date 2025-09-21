package com.medicine.eda

import com.medicine.eda.producer.MedicineProducer
import net.datafaker.Faker
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.LocalDate

@SpringBootApplication
class EdaApplication(
    val medicineProducer: MedicineProducer,
) : CommandLineRunner {
    val faker = Faker()

    override fun run(args: Array<String>) {
        for (i in 1..100) {
            medicineProducer.medicineUpdate(
                medicineProducer.medicineUpdate(
                    faker.funnyName().name(),
                    faker.number().randomDouble(2, 10, 1000),
                    LocalDate.now().plusYears(2),
                ),
            )
        }
    }
}

fun main(args: Array<String>) {
    runApplication<EdaApplication>(*args)
}
