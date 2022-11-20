import com.thoughtworks.gauge.BeforeSuite

class Setup {
    val taskApi: TaskApi by lazy { TaskApi() }
    @BeforeSuite
    fun setup(){
        taskApi.setup()
    }
}