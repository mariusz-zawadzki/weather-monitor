package pl.monku.weathersaver

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.math.BigDecimal

@Component
class WeatherReader(val writer: WeatherWriter, val restTemplate: RestTemplate, @Value("\${openweathermap.apiKey}") val appId: String) {

    @Scheduled(fixedRate = 600000)
    fun readWeather() {
        println("APP ID: ${appId}")
        val x: WeatherResult? = restTemplate.getForObject("http://api.openweathermap.org/data/2.5/weather?q=Tuszyn,PL&APPID=${appId}")
        println("Found weather: $x")
        if (x != null) {
            writer.addResult(x)
        }
    }

}

@Component
class WeatherWriter(val firebaseApp: FirebaseApp, val objectMapper: ObjectMapper) {
    private val weatherResults = mutableListOf<WeatherResult>()


    fun addResult(result: WeatherResult): Boolean{
        val defaultDatabase = FirebaseDatabase.getInstance(firebaseApp)
        val reference = defaultDatabase.getReference("weather")
        val writeValueAsString = objectMapper.writeValueAsString(result)
        reference.push().setValueAsync(objectMapper.readValue(writeValueAsString, java.util.Map::class.java))
        return weatherResults.add(result)
    }

    fun getResults() = weatherResults.toList()

}

data class Coordinates(val lon: BigDecimal, val lat: BigDecimal)

data class Weather(val temp: BigDecimal, val temp_min: BigDecimal, val temp_max: BigDecimal, val pressure: Int, val humidity: Int)

data class SunData(val sunrise: Long, val sunset: Long, val country: String)

data class WeatherResult(val coord: Coordinates, val main: Weather, val dt: Long, val sys: SunData)
