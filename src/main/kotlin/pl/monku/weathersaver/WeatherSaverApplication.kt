package pl.monku.weathersaver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WeatherSaverApplication

fun main(args: Array<String>) {
    runApplication<WeatherSaverApplication>(*args)
}
