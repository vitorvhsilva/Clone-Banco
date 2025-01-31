package br.com.bank.users.api.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LoggerConfig {
    @Bean
    fun getLogger(): Logger {
        return LoggerFactory.getLogger(LoggerConfig::class.java)
    }

}