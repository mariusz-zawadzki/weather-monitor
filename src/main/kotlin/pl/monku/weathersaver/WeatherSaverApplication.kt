package pl.monku.weathersaver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import reactor.core.publisher.Mono


@SpringBootApplication
@EnableScheduling
class WeatherSaverApplication {

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

}

fun main(args: Array<String>) {
    runApplication<WeatherSaverApplication>(*args)
}

@RestController
class TestController(val weatherWriter: WeatherWriter) {

    @GetMapping("/test")
    fun testEndpoint() = Mono.just("Test mono")

    @GetMapping("/test2")
    fun testEndpoint2() = "Test mono concrete"

    @GetMapping("/weather")
    fun getReadWeather() = weatherWriter.getResults()
}