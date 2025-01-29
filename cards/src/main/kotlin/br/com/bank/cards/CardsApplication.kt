package br.com.bank.cards

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class CardsApplication

fun main(args: Array<String>) {
	runApplication<CardsApplication>(*args)
}
