object Kotlin {
    const val version = "1.7.20"
    const val gradlePlugin = "org.jetbrains.kotlin.android"
    const val androidPlugin = "kotlin-android"
    const val serializationPlugin = "plugin.serialization"

    object Coroutines {
        private const val version = "1.6.0"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }
}

object Kotlinx {
    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1"
}

object Android {
    const val gradlePluginVersion = "7.1.3"
    const val applicationPlugin = "com.android.application"
}

object Google {
    private const val version = "31.1-jre"
    const val Guava = "com.google.guava:guava:$version"
}

object Gson {
    private const val version = "2.10"
    const val dependency = "com.google.code.gson:gson:$version"
}

object KSP {
    const val version = "1.7.20-1.0.8"
    const val dependency = "com.google.devtools.ksp:symbol-processing-api:$version"
    const val gradlePlugin = "com.google.devtools.ksp"
}

object KotlinPoet {
    private const val version = "1.12.0"
    const val dependency = "com.squareup:kotlinpoet:$version"
    const val ksp = "com.squareup:kotlinpoet-ksp:$version"
}

object DataStore {
    private const val version = "1.0.0"
    const val preferences = "androidx.datastore:datastore-preferences:$version"
    const val proto = "androidx.datastore:datastore:$version"
}
