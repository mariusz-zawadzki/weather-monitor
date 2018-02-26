package pl.monku.weathersaver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@SpringBootApplication
open class WeatherSaverApplication

fun main(args: Array<String>) {
    runApplication<WeatherSaverApplication>(*args)
}


@RestController
open class TestController {

    @GetMapping("/test")
    fun testEndpoint() = Mono.just("Test mono")
}