plugins {
    id("squaremarker.platform")
}

val minecraftVersion: String by rootProject
val bstatsVersion: String by rootProject

dependencies {
    implementation(project(":squaremarker-common"))

    compileOnly("io.papermc.paper:paper-api:$minecraftVersion-R0.1-SNAPSHOT")

    implementation("org.incendo", "cloud-paper")

    implementation("org.bstats", "bstats-bukkit", bstatsVersion)
}

tasks {
    jar {
        archiveClassifier = "not-shadowed"
    }

    shadowJar {
        archiveClassifier = null as String?
        listOf(
            "org.incendo",
        ).forEach { relocate(it, "${rootProject.group}.lib.$it") }
        dependencies {
            exclude(dependency("org.jetbrains:annotations"))
        }
    }
}

squareMarker {
    productionJar = tasks.shadowJar.flatMap { it.archiveFile }
    modInfoFilePath = "plugin.yml"
}
