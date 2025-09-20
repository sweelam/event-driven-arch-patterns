package com.medicine.eda.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.medicine.eda.producer.MedicineEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.lang.Thread.sleep
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.log

@Component
class MedicineConsumer (val objectMapper: ObjectMapper) {
    private val logger = KotlinLogging.logger {}
    private val count = AtomicInteger()

    @KafkaListener(
        topics = ["medicine-topic"],
        groupId = "medicine-group",
        concurrency = "3"
    )
    fun receiveMessage(value: MedicineEvent) {
        if (count.get() == 0) {
            sleep(5000L)
        }

        val jsonVal = objectMapper.writeValueAsString(value)
        logger.info { "Received Message in group medicine-group in json format: $jsonVal" }

        count.incrementAndGet()
        if (count.get() == 50) {
            logger.info { "Reached the 50 messages to be processed" }
        }
    }

    @KafkaListener(
        topics = ["medicine-topic"],
        groupId = "medicine-group-2",
        concurrency = "3",
        autoStartup = "false"       // to true to start listening
    )
    fun listen(event: ConsumerRecord<String, MedicineEvent>) {
        sleep(5000L)
        val eventData = event.value()
        val jsonVal = objectMapper.writeValueAsString(eventData)
        logger.info { "Received Message key in group medicine-group-2 in json format: $jsonVal" }
    }
}