pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }

    pluginManagement {
        resolutionStrategy {
            eachPlugin {
                if (requested.id.id == "kotlin-multiplatform") {
                    useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
                }
                if (requested.id.id == "kotlinx-serialization") {
                    useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
                }
            }
        }
    }
    
}
rootProject.name = "ScoreBook"


include(":androidApp")
include(":shared")

