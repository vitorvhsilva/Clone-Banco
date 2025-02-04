package br.com.bank.cards.api.config

import br.com.bank.cards.api.dto.events.PedidoCartaoCompletoDTO
import org.apache.commons.lang.mutable.Mutable
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class KafkaConfig (
    @Value(value = "\${spring.kafka.bootstrap-servers:localhost:9092}") private val bootstrapAddress: String
) {
    @Bean
    fun consumerFactory(): ConsumerFactory<String, PedidoCartaoCompletoDTO> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress)
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer::class.java)
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer::class.java)
        configProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer::class.java)
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer::class.java)
        configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false)
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "br.com.bank.cards.api.dto.events.PedidoCartaoCompletoDTO")
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*")
        return DefaultKafkaConsumerFactory<String, PedidoCartaoCompletoDTO>(configProps)
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, PedidoCartaoCompletoDTO> {
        val factory: ConcurrentKafkaListenerContainerFactory<String, PedidoCartaoCompletoDTO> =
            ConcurrentKafkaListenerContainerFactory<String, PedidoCartaoCompletoDTO>()
        factory.setConsumerFactory(consumerFactory())
        return factory
    }


}