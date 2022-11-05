plugins {
    kotlin(Kotlin.serializationPlugin)
    id(Android.applicationPlugin)
    id(Kotlin.androidPlugin)
    id(KSP.gradlePlugin)
}

android {
    compileSdk = Application.targetSdk
    namespace = Application.packageName

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
        freeCompilerArgs = freeCompilerArgs + "-Xjvm-default=all"
    }
}

kotlin.sourceSets.main {
    kotlin.srcDirs(file("$buildDir/generated/ksp/debug/kotlin"))
    kotlin.srcDirs(file("$buildDir/generated/ksp/main/kotlin"))
}

dependencies {
    implementation(Gson.dependency)
    implementation(DataStore.preferences)
    implementation(DataStore.proto)
    implementation(Kotlin.Coroutines.android)
    implementation(Kotlinx.serialization)

    implementation(project(":processor"))
    ksp(project(":processor"))
}
