package setup

import com.thoughtworks.gauge.BeforeSuite

@Suppress("unused")
class Setup {

    @BeforeSuite
    fun setup(){
        taskApi.setup()
    }
}