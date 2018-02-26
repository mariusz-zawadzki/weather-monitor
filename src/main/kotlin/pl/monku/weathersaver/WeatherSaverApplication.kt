package pl.monku.weathersaver

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import reactor.core.publisher.Mono
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets


@SpringBootApplication
@EnableScheduling
class WeatherSaverApplication {

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    fun firebaseInit(@Value("\${firebase.accout.json}") serviceAccountJson: String): FirebaseApp {
        val serviceAccount = ByteArrayInputStream(serviceAccountJson.toByteArray(StandardCharsets.UTF_8))
        val options = FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://monitor-pogody.firebaseio.com")
                .build()
        return FirebaseApp.initializeApp(options)

    }

}

fun main(args: Array<String>) {
    runApplication<WeatherSaverApplication>(*args)
}

@RestController
class TestController(val weatherWriter: WeatherWriter, val firebaseApp: FirebaseApp) {

    @GetMapping("/test")
    fun testEndpoint() = Mono.just("Test mono")

    @GetMapping("/test2")
    fun testEndpoint2() = "Test mono concrete"

    @GetMapping("/weather")
    fun getReadWeather() = weatherWriter.getResults()
}