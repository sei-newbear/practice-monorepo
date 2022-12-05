package setup

import com.thoughtworks.gauge.BeforeSuite

@Suppress("unused")
class RepeatingSetup {
    private val taskApi: TaskApi by lazy { TaskApi() }

    @BeforeSuite
    fun setup(){
        WebDriverConfig.setup()
        taskApi.setup()
    }
}