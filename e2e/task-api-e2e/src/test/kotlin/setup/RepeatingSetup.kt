package setup

import com.thoughtworks.gauge.AfterScenario
import com.thoughtworks.gauge.AfterSuite
import com.thoughtworks.gauge.BeforeScenario
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

    @BeforeScenario(tags = ["watch-change-data"])
    fun setupWatchChangeData(){
        println("data change start")
    }


    @AfterScenario(tags = ["watch-change-data"])
    fun teardownWatchChangeData(){
        println("data change end")
    }
}