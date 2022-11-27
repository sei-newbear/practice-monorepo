package setup

import com.thoughtworks.gauge.BeforeSuite

@Suppress("unused")
class Setup {
    private val taskApi by lazy { TaskApi() }

    @BeforeSuite
    fun setup(){
//        taskApi.setup()
    }
}