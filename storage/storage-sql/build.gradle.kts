plugins {
    id("blue-common-conventions")
}

version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(mapOf("path" to ":storage")))
    testImplementation("com.mysql:mysql-connector-j:8.3.0")
    testImplementation("com.google.code.gson:gson:2.10.1")
}