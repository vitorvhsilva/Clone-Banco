package br.com.bank.users.api.config

import br.com.bank.users.api.dto.events.PedidoCartaoCompletoDTO
import br.com.bank.users.api.dto.events.PedidoPixEventDTO
import br.com.bank.users.api.dto.events.PixAceitoEventDTO
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaConfig (
    @Value(value = "\${spring.kafka.bootstrap-servers:localhost:9092}") private val bootstrapAddress: String
) {
    @Bean
    fun pedidoCartaoProducerFactory(): ProducerFactory<String, PedidoCartaoCompletoDTO> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress)
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer::class.java)
        return DefaultKafkaProducerFactory<String, PedidoCartaoCompletoDTO>(configProps)
    }

    @Bean
    fun pedidoCartaoKafkaTemplate(): KafkaTemplate<String, PedidoCartaoCompletoDTO> {
        return KafkaTemplate<String, PedidoCartaoCompletoDTO>(pedidoCartaoProducerFactory())
    }

    @Bean
    fun pixAceitoProducerFactory(): ProducerFactory<String, PixAceitoEventDTO> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress)
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer::class.java)
        return DefaultKafkaProducerFactory<String, PixAceitoEventDTO>(configProps)
    }

    @Bean
    fun pixAceitoKafkaTemplate(): KafkaTemplate<String, PixAceitoEventDTO> {
        return KafkaTemplate<String, PixAceitoEventDTO>(pixAceitoProducerFactory())
    }

    @Bean
    fun pedidoPixConsumerFactory(): ConsumerFactory<String, PedidoPixEventDTO> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress)
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer::class.java)
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer::class.java)
        configProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer::class.java)
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer::class.java)
        configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false)
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "br.com.bank.users.api.dto.events.PedidoPixEventDTO")
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*")
        return DefaultKafkaConsumerFactory<String, PedidoPixEventDTO>(configProps)
    }

    @Bean
    fun pedidoPixContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, PedidoPixEventDTO> {
        val factory: ConcurrentKafkaListenerContainerFactory<String, PedidoPixEventDTO> =
            ConcurrentKafkaListenerContainerFactory<String, PedidoPixEventDTO>()
        factory.setConsumerFactory(pedidoPixConsumerFactory())
        return factory
    }
}