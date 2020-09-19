package me.jtux.flux

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

@Document
data class Profile(@Id val id: String? = null, val email: String)


interface ProfileRepository : ReactiveMongoRepository<Profile, String>