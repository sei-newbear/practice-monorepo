package setup

import com.thoughtworks.gauge.AfterSuite
import com.thoughtworks.gauge.BeforeScenario
import com.thoughtworks.gauge.BeforeSuite
import com.thoughtworks.gauge.ExecutionContext
import java.io.File
import java.nio.file.Paths

@Suppress("unused")
class RepeatingSetup {
    private val taskDb by lazy { TaskDb() }

    @BeforeSuite
    fun setup() {
        taskDb.setup("setup/init")
    }

    @AfterSuite
    fun teardown() {
        taskDb.teardown()
    }

    @BeforeScenario(tags = ["watch-change-data"])
    fun setupWatchChangeData(context: ExecutionContext) {
        val workingDir = Paths.get("").toAbsolutePath().toString()
        val scenarioName = context.currentScenario.name
        val specDir = context.currentSpecification.fileName

        val specFilePath = specDir.replace("${workingDir}${File.separator}specs${File.separator}", "")
        val scenarioPath = scenarioName.split("--").map { it.trim() }.last()

        val setupPath = "${specFilePath}${File.separator}${scenarioPath}"
        println("${setupPath}ディレクトリを元にテストデータをセットアップします。")

        taskDb.setup(setupPath)
        taskDb.startWatchingChangedDb()
    }
}