package com.medicine.eda.consumer

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TopicConfig {
    @Bean
    fun topic() =
        NewTopic("medicine-topic", 3, 1)
}