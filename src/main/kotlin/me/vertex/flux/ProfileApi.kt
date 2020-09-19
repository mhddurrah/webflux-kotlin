package me.jtux.flux

import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping(value = ["/"])
@org.springframework.context.annotation.Profile("classic")
class Landing {
    @GetMapping
    fun landing(): String {
        return "Profile Classic Api"
    }
}

@RestController
@RequestMapping(value = ["/profiles"], produces = [MediaType.APPLICATION_JSON_VALUE])
@org.springframework.context.annotation.Profile("classic")
@CrossOrigin(origins = ["*"])
class ProfileApi @Autowired constructor(val profileService: ProfileService) {
    @GetMapping
    fun getAll(): Publisher<Profile> {
        return profileService.getAll()
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: String): Publisher<Profile> {
        return profileService.findById(id)
    }

    @PostMapping
    fun create(@RequestParam(name = "email", required = false) email: String): Mono<Profile> {
        return profileService.create(email)
    }
}