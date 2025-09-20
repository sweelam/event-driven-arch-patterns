package com.medicine.eda.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.medicine.eda.producer.MedicineEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.lang.Thread.sleep

@Component
class MedicineConsumer (val objectMapper: ObjectMapper) {

    @KafkaListener(
        topics = ["medicine-topic"],
        groupId = "medicine-group",
        concurrency = "3"
    )
    fun receiveMessage(value: MedicineEvent) {
        sleep(5000L)
        val jsonVal = objectMapper.writeValueAsString(value)
        println("Received Message in group medicine-group: $value \n in json format: $jsonVal")
    }
}