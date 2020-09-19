package me.jtux.flux

import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URI

@Component
class ProfileHandler @Autowired constructor(val profileService: ProfileService) {

    fun all(req: ServerRequest): Mono<ServerResponse> {
        return defaultReadResponse(profileService.getAll())
    }

    fun byId(req: ServerRequest): Mono<ServerResponse> {
        return defaultReadResponse(profileService.findById(req.pathVariable("id")))
    }

    fun create(req: ServerRequest): Mono<ServerResponse> {
        val flux: Flux<Profile> = req.bodyToFlux(Profile::class.java)
                .flatMap { profileService.create(it.email) }
        return defaultWriteResponse(flux)
    }

    fun defaultWriteResponse(profiles: Publisher<Profile>): Mono<ServerResponse> {
        return Mono.from(profiles)
                .flatMap {
                    ServerResponse
                            .created(URI.create("/profiles/${it.id}"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .build()
                }
    }

    fun defaultReadResponse(profiles: Publisher<Profile>): Mono<ServerResponse> {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(profiles, Profile::class.java)
    }
}