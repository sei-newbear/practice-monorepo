package setup

import com.thoughtworks.gauge.AfterSuite
import com.thoughtworks.gauge.BeforeSuite

@Suppress("unused")
class RepeatingSetup {
    private val taskDb by lazy { TaskDb() }

    @BeforeSuite
    fun setup(){
        taskDb.setup("setup")
    }

    @AfterSuite
    fun teardown(){
        taskDb.teardown()
    }
}