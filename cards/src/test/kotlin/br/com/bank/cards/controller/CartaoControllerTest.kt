package br.com.bank.cards.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class CartaoControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Deve retornar codigo 200 para obter os cartoes por segmento`() {
        val response = mockMvc.perform(
            MockMvcRequestBuilders.get("/cartoes/segmentos/BASE")
        ).andReturn().response

        assertEquals(200, response.status)
    }

    @Test
    fun `Deve retornar codigo 200 para obter os cartoes para segmento inexistente`() {
        val response = mockMvc.perform(
            MockMvcRequestBuilders.get("/cartoes/segmentos/XXX")
        ).andReturn().response

        assertEquals(400, response.status)
    }

    @Test
    fun `Deve retornar codigo 200 para obter os cartoes do usuario`() {
        val response = mockMvc.perform(
            MockMvcRequestBuilders.get("/cartoes/usuarios/1")
        ).andReturn().response

        assertEquals(200, response.status)
    }

    @Test
    fun `Deve retornar codigo 200 para obter as faturas por cartao`() {
        val response = mockMvc.perform(
            MockMvcRequestBuilders.get("/cartoes/faturas/1")
        ).andReturn().response

        assertEquals(200, response.status)
    }
}