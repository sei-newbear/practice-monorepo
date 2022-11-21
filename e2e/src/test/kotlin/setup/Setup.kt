package setup

import com.thoughtworks.gauge.BeforeSuite

@Suppress("unused")
class Setup {
    val taskApi: TaskApi by lazy { TaskApi() }

    @BeforeSuite
    fun setup(){
        WebDriverConfig.setup()
        taskApi.setup()
    }
}