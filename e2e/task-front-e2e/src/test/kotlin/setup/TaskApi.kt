package setup

import com.github.tomakehurst.wiremock.client.WireMock
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
}