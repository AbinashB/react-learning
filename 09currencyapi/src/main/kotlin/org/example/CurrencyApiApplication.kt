package org.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CurrencyApiApplication

fun main(args: Array<String>) {
    runApplication<CurrencyApiApplication>(*args)
}
