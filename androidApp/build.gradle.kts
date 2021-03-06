
plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-android-extensions")
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlin_version"]}")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("nl.dionsegijn:konfetti:1.2.6")


    implementation("com.facebook.stetho:stetho:1.5.1")
    implementation("com.google.dagger:dagger-android:2.33")
    implementation("com.google.dagger:dagger:2.33")
    annotationProcessor("com.google.dagger:dagger-android-processor:2.33")

//    def room_version = "2.2.6"

    implementation("androidx.room:room-runtime:2.2.6")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.3")
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("com.google.code.gson:gson:2.8.6")
    kapt("androidx.room:room-compiler:2.2.6")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.2.6")

    // optional - Test helpers
    testImplementation("androidx.room:room-testing:2.2.6")
}

android {
    compileSdkVersion(29)
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    defaultConfig {
        applicationId = "com.lindenlabs.scorebook.androidApp"
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        viewBinding = true
    }
}