package com.medicine.eda.producer

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.UUID


data class MedicineData(val medicineName: String,
                         val price: Double,
                         val expiryDate: LocalDate)

data class MedicineEvent(val eventId: UUID,
                         val eventType: String,
                         val correlationId: UUID,
                         val data: MedicineData)

private const val MEDICINE_TOPIC = "medicine-topic"

@Component
public class MedicineProducer (val kafkaTemplate: KafkaTemplate<String, MedicineEvent>) {
    fun medicineUpdate() {
        val medicineData =
            MedicineData("Demastatic", 101.00, LocalDate.now())

        val medicineEvent = MedicineEvent(UUID.randomUUID(),
            "medicine.price.changed",
            UUID.randomUUID(),
            medicineData)

        kafkaTemplate.send(MEDICINE_TOPIC, medicineEvent)
    }
}

