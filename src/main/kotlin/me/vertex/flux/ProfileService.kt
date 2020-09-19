package me.jtux.flux

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProfileService @Autowired constructor(val profileRepository: ProfileRepository,
                                            val eventPublisher: ApplicationEventPublisher) {
    fun getAll(): Flux<Profile> {
        return profileRepository.findAll()
    }

    fun findById(id: String): Mono<Profile> {
        return profileRepository.findById(id)
    }

    fun create(email: String): Mono<Profile> {
        return profileRepository.save(Profile(email = email))
                .doOnSuccess { eventPublisher.publishEvent(ProfileCreatedEvent(it)) }
    }
}