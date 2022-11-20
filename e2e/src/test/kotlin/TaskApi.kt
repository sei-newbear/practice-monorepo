import com.github.tomakehurst.wiremock.client.WireMock
import java.io.File

class TaskApi {
    private val taskApi = WireMock("localhost", 8089)

    fun setup(){
        this.javaClass.classLoader.getResource("setup/task_api")?.let{
            val file = it.toURI().let(::File)
            taskApi.resetMappings()
            taskApi.loadMappingsFrom(file)
        }
    }
}