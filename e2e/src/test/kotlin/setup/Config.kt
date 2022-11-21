package setup

import com.natpryce.konfig.*

class Config {
    companion object {
        val config = EnvironmentVariables() overriding ConfigurationProperties.fromResource("defaults.properties")
    }

    object selenide : PropertyGroup() {
        val baseUrl by stringType
        val browser by stringType
    }

    object taskApi : PropertyGroup() {
        val host by stringType
        val port by intType
    }
}