import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.jayway.jsonpath.JsonPath
import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldHave
import io.kotest.matchers.shouldNotBe
import setup.Config

private const val STATUS_CODE = "statusCode"

private const val RESPONSE_BODY = "responseBody"

class ApiTest {

    private fun saveStatusCode(statusCode: Int){
        ScenarioDataStore.put(STATUS_CODE, statusCode)
    }

    private fun getStatusCode(): Int{
        return ScenarioDataStore.get(STATUS_CODE) as Int
    }

    private fun saveResponseBody(responseBody: String){
        ScenarioDataStore.put(RESPONSE_BODY, responseBody)
    }

    private fun getResponseBody(): String{
        return ScenarioDataStore.get(RESPONSE_BODY) as String
    }

    private fun saveResponse(response: Triple<Request, Response, Result<String, FuelError>>) {
        val statusCode = response.second.statusCode
        val responseBody = response.third.get()
        saveStatusCode(statusCode)
        saveResponseBody(responseBody)
    }

    private fun getJsonValue(jsonpath: String): Any? {
        return JsonPath.read(getResponseBody(), jsonpath)
    }

    private val baseUrl = Config.config[Config.taskApi.baseUrl]

    @Step("<path>へGETリクエストする")
    fun requestGet(path: String){
        val url = "${baseUrl}/${path}"
        val response = url.httpGet().responseString()
        saveResponse(response)
    }

    @Step("ステータスコードが<code>であること")
    fun equalStatusCode(code: Int){
        getStatusCode() shouldBe code
    }

    @Step("<jsonpath>が存在すること")
    fun existsJsonpath(jsonpath: String){
        val jsonValue = getJsonValue(jsonpath)
        jsonValue shouldNotBe null
    }

    @Step("<jsonpath>がNullであること")
    fun isNullJsonpath(jsonpath: String){
        val jsonValue = getJsonValue(jsonpath)
        jsonValue shouldBe null
    }

    @Step("<jsonpath>の要素数が<length>であること")
    fun lengthListJsonpath(jsonpath: String, length: Int){
        val jsonValue = getJsonValue(jsonpath)
        if(jsonValue is Collection<*>){
            jsonValue shouldHave haveSize(length)
        }else{
            throw IllegalArgumentException("「${jsonpath}」は配列ではありません。値: $jsonValue")
        }
    }

    @Step("<jsonpath>の値が文字列の<value>であること")
    fun assertJsonEqualValue(jsonpath: String, value: String){
        value shouldBe getJsonValue(jsonpath)
    }

    @Step("<jsonpath>の値が整数の<value>であること")
    fun assertJsonEqualNumberValue(jsonpath: String, value: Int){
        getJsonValue(jsonpath) shouldBe value
    }

    @Step("<jsonpath>の値が真偽値の<value>であること")
    fun assertJsonEqualBooleanValue(jsonpath: String, value: Boolean){
        getJsonValue(jsonpath) shouldBe value
    }
}