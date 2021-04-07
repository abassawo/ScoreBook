buildscript {
    val kotlin_version by extra("1.4.30-M1")
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        val navVersion = "2.3.3"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath("com.squareup.sqldelight:gradle-plugin:1.4.4")
    }
}




allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}
