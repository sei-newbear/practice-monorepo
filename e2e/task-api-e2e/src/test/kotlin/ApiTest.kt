import com.github.kittinunf.fuel.httpGet
import com.jayway.jsonpath.JsonPath
import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import io.kotest.matchers.shouldBe
import setup.Config

class ApiTest {

    private fun saveStatusCode(statusCode: Int){
        ScenarioDataStore.put("statusCode", statusCode)
    }

    private fun getStatusCode(): Int{
        return ScenarioDataStore.get("statusCode") as Int
    }

    private fun saveResponseBody(responseBody: String){
        ScenarioDataStore.put("responseBody", responseBody)
    }

    private fun getResponseBody(): String{
        return ScenarioDataStore.get("responseBody") as String
    }

    private fun getJsonValue(jsonpath: String): Any? {
        return JsonPath.read<Any>(getResponseBody(), jsonpath)
    }

    private fun getJsonNumberValue(jsonpath: String): Long {
        return JsonPath.read(getResponseBody(), jsonpath)
    }

    private fun getJsonBooleanValue(jsonpath: String): Boolean {
        return JsonPath.read(getResponseBody(), jsonpath)
    }

    private val baseUrl = Config.config[Config.taskApi.baseUrl]

    @Step("<endpoint>へGETリクエストする")
    fun requestGet(endpoint: String){
        val response = "${baseUrl}/${endpoint}".httpGet().responseString()
        val statusCode = response.second.statusCode
        val responseBody = response.third.get()
        saveStatusCode(statusCode)
        saveResponseBody(responseBody)
    }

    @Step("ステータスコードが<code>であること")
    fun equalStatusCode(code: Int){
        getStatusCode() shouldBe code
    }

    @Step("<jsonpath>が存在すること")
    fun existsJsonpath(jsonpath: String){
        getJsonValue(jsonpath)
    }

    @Step("<jsonpath>の値が<value>であること")
    fun assertJsonEqualValue(jsonpath: String, value: String){
        value shouldBe getJsonValue(jsonpath)
    }

    @Step("<jsonpath>の値が数値の<value>であること")
    fun assertJsonEqualNumberValue(jsonpath: String, value: Long){
        value shouldBe getJsonNumberValue(jsonpath)
    }

    @Step("<jsonpath>の値が真偽値の<value>であること")
    fun assertJsonEqualBooleanValue(jsonpath: String, value: Boolean){
        value shouldBe getJsonBooleanValue(jsonpath)
    }
}