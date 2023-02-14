plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "github.tyonakaisan"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.broccol.ai")
}

dependencies {

    compileOnly("io.papermc.paper", "paper-api", "1.19.3-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol", "ProtocolLib", "4.7.0")
    implementation("broccolai.corn", "corn-minecraft-paper", "3.2.0")
    implementation("org.incendo.interfaces", "interfaces-paper", "1.0.0-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    compileJava {
        this.options.encoding = "UTF-8"
    }
    shadowJar {
        this.archiveClassifier.set(null as String?)
    }
}
