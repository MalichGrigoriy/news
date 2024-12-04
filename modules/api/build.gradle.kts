plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.annotation)
    implementation(libs.retrofit.serialization.kotlinx)
    implementation(libs.retrofit.result.adapter)
    api(libs.kotlinx.serialization.json)
    api(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    testImplementation(libs.junit)
}
