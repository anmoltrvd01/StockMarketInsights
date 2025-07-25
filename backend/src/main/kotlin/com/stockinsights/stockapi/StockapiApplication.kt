package com.stockinsights.stockapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.cache.annotation.EnableCaching
import org.springframework.web.client.RestTemplate
import io.github.cdimascio.dotenv.dotenv

@EnableCaching
@SpringBootApplication
class StockapiApplication {
	val dotenv = dotenv()
	val alphaVantageApiKey = dotenv["ALPHA_VANTAGE_API_KEY"]
	@Bean
	fun restTemplate(): RestTemplate {
		return RestTemplate()
	}
}

fun main(args: Array<String>) {
	runApplication<StockapiApplication>(*args)
}
