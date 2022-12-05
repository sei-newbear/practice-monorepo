package setup

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.thoughtworks.gauge.Step
import java.io.File

class TaskApi {
    private val taskApi = WireMock(Config.config[Config.taskApi.host], Config.config[Config.taskApi.port])

    fun setup(){
        this.javaClass.classLoader.getResource("setup/task_api")?.let{
            val file = it.toURI().let(::File)
            taskApi.resetMappings()
            taskApi.loadMappingsFrom(file)
        }
    }

    @Step("タスクAPIの<path>へ<jsonpath>に<value>をPOSTリクエストしていること")
    fun assert(path: String, jsonpath: String, value: String) {
        // TODO mockをシナリオごとに初期化しないとテスト増やすと落ちるかも
        taskApi.verifyThat(postRequestedFor(urlEqualTo(path))
            .withRequestBody(matchingJsonPath(jsonpath, equalTo(value))))
    }
}