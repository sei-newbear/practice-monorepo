package setup

import com.codeborne.selenide.Configuration

class WebDriverConfig {
    companion object {
        fun setup() {
            Configuration.baseUrl = Config.config[Config.selenide.baseUrl]
            Configuration.browser = Config.config[Config.selenide.browser]
        }
    }
}