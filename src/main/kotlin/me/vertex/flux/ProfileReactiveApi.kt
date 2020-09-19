package me.jtux.flux

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ClassPathResource
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
@Profile("reactive", "docker")
class ProfileReactiveApi {

    @Bean
    fun routes(profileHandler: ProfileHandler) = router {
        GET("/") { ServerResponse.ok().bodyValue("Profile Reactive Api") }
        "/profiles".nest {
            GET("/", profileHandler::all)
            GET("/{id}", profileHandler::byId)
            POST("/", profileHandler::create)
        }

        resources("/**", ClassPathResource("static/"))
    }

}
