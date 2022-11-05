plugins {
    kotlin("jvm")
    id(KSP.gradlePlugin)
}

dependencies {
    implementation(Gson.dependency)
    implementation(KSP.dependency)
    implementation(KotlinPoet.dependency)
    implementation(KotlinPoet.ksp)
    implementation(Google.Guava)

    api(project(":annotations"))
}
