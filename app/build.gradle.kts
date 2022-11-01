plugins {
    id(Android.applicationPlugin)
    id(Kotlin.androidPlugin)
}

android {
    compileSdk = Application.targetSdk

    defaultConfig {
        applicationId = Application.packageName
        minSdk = Application.minSdk
        targetSdk = Application.targetSdk
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}
