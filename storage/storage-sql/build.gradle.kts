plugins {
    id("blue-common-conventions")
}

version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":storage"))
    implementation(project(":sql"))
    compileOnly("com.zaxxer:HikariCP:4.0.3")
    testImplementation("com.mysql:mysql-connector-j:8.3.0")
    testImplementation("com.google.code.gson:gson:2.10.1")
}