package com.medicine.eda.producer

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.UUID

data class MedicineData(
    val medicineName: String,
    val price: Double,
    val expiryDate: LocalDate,
)

data class MedicineEvent(
    val eventId: UUID,
    val eventType: String,
    val correlationId: UUID,
    val data: MedicineData,
)

private const val MEDICINE_TOPIC = "medicine-topic"
private const val MEDICINE_PRICE_CHANGED = "medicine.price.changed"

@Component
public class MedicineProducer(
    val kafkaTemplate: KafkaTemplate<String, MedicineEvent>,
) {
    fun medicineUpdate(medicineEvent: MedicineEvent) {
        kafkaTemplate.send(MEDICINE_TOPIC, medicineEvent)
    }

    fun medicineUpdate(
        medicineName: String,
        price: Double,
        expiryDate: LocalDate,
    ) = MedicineEvent(
        eventId = UUID.randomUUID(),
        eventType = MEDICINE_PRICE_CHANGED,
        correlationId = UUID.randomUUID(),
        data =
            MedicineData(
                medicineName = medicineName,
                price = price,
                expiryDate = expiryDate,
            ),
    )
}
