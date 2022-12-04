package setup

import com.natpryce.konfig.*

class Config {
    companion object {
        val config = EnvironmentVariables() overriding ConfigurationProperties.fromResource("defaults.properties")
    }

    object taskApi : PropertyGroup() {
        val baseUrl by stringType
    }

    object taskDb : PropertyGroup() {
        val driverClass by stringType
        val url by stringType
        val user by stringType
        val password by stringType
        val schema by stringType
    }
}