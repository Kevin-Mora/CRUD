plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "me.aikon"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    testImplementation(kotlin("test"))
    // dependencias para usar el export to excell
    implementation("org.apache.poi:poi:5.1.0")
    implementation("org.apache.poi:poi-ooxml:5.1.0")
    implementation("org.apache.logging.log4j:log4j-core:2.17.1")

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}