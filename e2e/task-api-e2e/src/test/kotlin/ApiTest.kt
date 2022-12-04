import com.github.kittinunf.fuel.core.ResponseResultOf
import com.github.kittinunf.fuel.httpGet
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldHave
import setup.Config
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.fail

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

    private fun saveResponse(response: ResponseResultOf<ByteArray>) {
        val statusCode = response.second.statusCode
        val responseBody = response.second.data.let(::String)
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
        val response = url.httpGet().response()
        saveResponse(response)
    }

    @Step("ステータスコードが<code>であること")
    fun equalStatusCode(code: Int){
        getStatusCode() shouldBe code
    }

    @Step("<jsonpath>が存在すること")
    fun existsJsonpath(jsonpath: String){
        assertNotNull(getJsonValue(jsonpath), "\"$jsonpath\"")
    }

    @Step("<jsonpath>が存在しないこと")
    fun notExistsJsonpath(jsonpath: String){
        try {
            val jsonValue = getJsonValue(jsonpath)
            fail("${jsonpath}は存在します。値: $jsonValue")
        }catch (_: PathNotFoundException){
        }
    }

    @Step("<jsonpath>がNullであること")
    fun isNullJsonpath(jsonpath: String){
        assertNull(getJsonValue(jsonpath), "\"$jsonpath\"")
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
        getJsonValue(jsonpath) shouldBe value
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