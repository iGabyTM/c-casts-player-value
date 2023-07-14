plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

group = "me.gabytm.minecraftc"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.triumphteam.dev/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("dev.triumphteam:triumph-cmd-bukkit:2.0.0-ALPHA-8")
    compileOnly("me.clip:placeholderapi:2.11.3")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }

    processResources {
        filesMatching("*.yml") {
            expand("version" to project.version)
        }
    }

    shadowJar {
        archiveFileName.set("CastsPlayerValue_${project.version}.jar")
        mapOf(
            "dev.triumphteam.cmd" to "cmd",
            "org.spongepowered.configurate" to "configurate",
            "io.leangen.geantyref" to "geantyref", // Lib from configurate
            "org.yaml.snakeyaml" to "snakeyaml"
        ).forEach { (source, destination) -> relocate(source, "me.gabytm.minecraftc.castsplayervalue.libs.$destination") }
    }

    register<Copy>("buildToServer") {
        group = "build"
        from(shadowJar)
        into("./testServer/plugins")
    }
}