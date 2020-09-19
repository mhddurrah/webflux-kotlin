package me.jtux.flux

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import reactor.core.publisher.Flux
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
class WebSocketConfig {

    @Bean
    fun executor(): ExecutorService = Executors.newSingleThreadExecutor()

    @Bean
    fun wsHandlerMapping(wsh: WebSocketHandler): HandlerMapping {
        val mapping = SimpleUrlHandlerMapping()
        with(mapping) {
            urlMap = mapOf("/ws/profiles" to wsh)
            order = 10
        }
        return mapping
    }

    @Bean
    fun webSocketHandlerAdapter(): WebSocketHandlerAdapter = WebSocketHandlerAdapter()

    @Bean
    fun webSocketHandler(objectMapper: ObjectMapper,
                         p: ProfileCreatedEventPublisher): WebSocketHandler {
        val publish = Flux.create(p).share()
        return WebSocketHandler { session ->

            val messageFlux: Flux<WebSocketMessage> = publish.map { evt: ProfileCreatedEvent? ->
                try {
                    println("write message")
                    objectMapper.writeValueAsString(evt)
                } catch (jpe: JsonProcessingException) {
                    throw RuntimeException(jpe)
                }
            }.map {
                session.textMessage(it)
            }
            session.send(messageFlux)
        }
    }

}