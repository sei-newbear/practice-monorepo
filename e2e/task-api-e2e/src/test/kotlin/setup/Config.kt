package setup

import com.natpryce.konfig.*

class Config {
    companion object {
        val config = EnvironmentVariables() overriding ConfigurationProperties.fromResource("defaults.properties")
    }

    object taskApi : PropertyGroup() {
        val baseUrl by stringType
    }
}