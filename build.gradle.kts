import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")

    //Paper
    maven("https://repo.papermc.io/repository/maven-public/")

    //ProtocolLib
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
}

dependencies {
    // Paper
    compileOnly("io.papermc.paper", "paper-api", "1.20.6-R0.1-SNAPSHOT")

    //ProtocolLib
    compileOnly("com.comphenix.protocol", "ProtocolLib", "5.3.0-SNAPSHOT")

    // Command
    paperLibrary("org.incendo", "cloud-paper", "2.0.0-beta.8")

    // Item
    implementation("org.incendo.interfaces", "interfaces-paper", "1.0.0-SNAPSHOT")

    // Config
    paperLibrary("org.spongepowered", "configurate-hocon", "4.2.0-SNAPSHOT")
    paperLibrary("net.kyori", "adventure-serializer-configurate4", "4.16.0")

    // Utils
    paperLibrary("com.google.inject", "guice", "7.0.0")
}

version = "1.0-SNAPSHOT"

paper {
    val mainPackage = "github.tyonakaisan.sukesuke"
    generateLibrariesJson = true
    name = rootProject.name
    version = project.version as String
    main = "$mainPackage.Sukesuke"
    loader = "$mainPackage.SukesukeLoader"
    bootstrapper = "$mainPackage.SukesukeBootstrap"
    apiVersion = "1.20"
    author = "tyonakaisan"
    website = "https://github.com/tyonakaisan"

    permissions {
        register("sukesuke.suke")
        description = "Sukesukeします"
        defaultPermission = BukkitPluginDescription.Permission.Default.TRUE
    }

    serverDependencies {
        register("ProtocolLib") {
            required = true
        }
    }
}

tasks {
    val paperPlugins = runPaper.downloadPluginsSpec {
        // TabTps
        url("https://cdn.modrinth.com/data/cUhi3iB2/versions/QmxLremu/tabtps-spigot-1.3.21.jar")
        // Spark
        url("https://ci.lucko.me/job/spark/409/artifact/spark-bukkit/build/libs/spark-1.10.64-bukkit.jar")
        // ProtocolLib
        url("https://ci.dmulloy2.net/job/ProtocolLib/lastStableBuild/artifact/build/libs/ProtocolLib.jar")
    }

    compileJava {
        this.options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    shadowJar {
        this.archiveClassifier.set(null as String?)
        archiveVersion.set(paper.version)
    }

    runServer {
        minecraftVersion("1.20.6")
        downloadPlugins {
            downloadPlugins.from(paperPlugins)
        }
    }
}
