plugins {
    id("java")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}

group = "com.twins"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://github.com/goncalodelima/ms-vip")
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

bukkit {
    name = "twins-common"
    version = "${project.version}"
    main = "com.twins.common.CommonPlugin"
    depend = listOf("MinecraftSolutions", "packetevents")
    author = "ReeachyZ_"
    website = "https://minecraft-solutions.com"
    description = "Common Plugin"
    commands {
        register("language"){
            aliases = listOf("lang", "idioma", "idiomas", "lingua", "linguas")
        }
    }
}
