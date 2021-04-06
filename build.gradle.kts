buildscript {
    val kotlin_version by extra("1.4.3")
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath("com.squareup.sqldelight:gradle-plugin:1.4.4")
//        classpath("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")
//        classpath("com.google.code.gson:gson:2.2.4")
//        classpath("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.1.0")
    }
}




allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}
