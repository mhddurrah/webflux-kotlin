package me.jtux.flux

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class SSEController(val objectMapper: ObjectMapper,
                    publisher: ProfileCreatedEventPublisher) {

    private var events: Flux<ProfileCreatedEvent> = Flux.create(publisher).share()

    @GetMapping(path = ["/sse/profiles"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun profiles(): Flux<String> {
        return events.map {
            objectMapper.writeValueAsString(it);
        }
    }
}