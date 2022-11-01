plugins {
    id(Android.applicationPlugin).version(Android.gradlePluginVersion).apply(false)
    id(Kotlin.gradlePlugin).version(Kotlin.version).apply(false)
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
