
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
    implementation("com.airbnb.android:lottie:3.6.1")
    implementation("nl.dionsegijn:konfetti:1.2.6")



    implementation("com.facebook.stetho:stetho:1.5.1")

    val daggerVersion=2.33

    implementation("com.google.dagger:dagger:$daggerVersion")
    implementation("com.google.dagger:dagger-android:2.33")
    kapt("com.google.dagger:dagger-android-processor:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")

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

    // Required -- JUnit 4 framework
    testImplementation("junit:junit:4.12")
    // Optional -- Robolectric environment
    testImplementation("androidx.test:core:1.0.0")
//    val coroutines = "1.4.3"
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines")

    // testImplementation for pure JVM unit tests
//    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines")

    // androidTestImplementation for Android instrumentation tests
//    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines")

    testImplementation("androidx.test.ext:junit:1.1.2-alpha03")
    testImplementation("org.mockito:mockito-core:3.0.0")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    testImplementation("org.mockito:mockito-inline:3.0.0")
    testImplementation("org.amshove.kluent:kluent:1.51")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.3")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
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
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.incremental"] = "true"
            }
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        viewBinding = true
    }
    testOptions {
        unitTests.apply {
            isReturnDefaultValues = true
        }
    }
}