import com.github.kittinunf.fuel.core.awaitResponse
import com.github.kittinunf.fuel.gson.gsonDeserializer
import com.github.kittinunf.fuel.httpGet

/**
 * @author Usievaład Kimajeŭ
 * @created 2020-04-13
 */
suspend fun main() {
	val countries: List<Country> = "https://corona.lmao.ninja/v2/countries".httpGet()
			.awaitResponse(gsonDeserializer<List<Country>>())
			.third

	println("Deaths per tests:")

	countries.filter { country -> country.tests != 0 }
			.onEach { country -> country.deathsPerTests = country.deaths.toDouble() / country.tests }
			.sortedByDescending { country -> country.deathsPerTests }
			.take(20)
			.forEachIndexed { index, country -> println("${index + 1}. ${country.country}: ${country.deathsPerTests}") }

	println("Deaths per 1 million:")

	countries.sortedByDescending { country -> country.deathsPerOneMillion }
			.take(20)
			.forEachIndexed { index, country -> println("${index + 1}. ${country.country}: ${country.deathsPerOneMillion}") }
}

data class Country(
		val country: String,
		val tests: Int,
		val deaths: Int,
		val deathsPerOneMillion: Double,
		var deathsPerTests: Double
)
