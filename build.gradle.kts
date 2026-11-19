plugins {
    kotlin("jvm") version "2.4.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "bedwars"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    testImplementation(kotlin("test"))
    //compileOnly(files("libs/spigot-api-1.21.11-R0.2-SNAPSHOT.jar"))
    compileOnly("org.spigotmc:spigot-api:1.21.11-R0.2-SNAPSHOT")
    compileOnly("org.joml:joml:1.10.9")
    implementation(kotlin("stdlib"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks.shadowJar {
    destinationDirectory.set(file("D:/Workspaces/newbedwars/plugins"))
    archiveFileName.set("bedwarsk.jar")
}