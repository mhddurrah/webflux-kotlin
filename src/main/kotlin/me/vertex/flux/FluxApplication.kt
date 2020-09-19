package me.jtux.flux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FluxApplication

fun main(args: Array<String>) {
    runApplication<FluxApplication>(*args)
}