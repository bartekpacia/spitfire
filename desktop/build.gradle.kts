plugins {
    `java-library`
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/"))
        }
    }
}

dependencies {
    implementation(projects.core)
    implementation(libs.gdx.backend.lwjgl3)
    implementation(variantOf(libs.gdx.platform) { classifier("natives-desktop") })
    implementation(variantOf(libs.gdx.freetype.platform) { classifier("natives-desktop") })

    implementation(libs.gdx.gamesvcs.desktop.gpgs)
}

val mainClassName by extra("pl.baftek.spitfire.desktop.DesktopLauncher")
val assetsDir by extra(File("../android/assets"))

tasks.register<JavaExec>("run") {
    dependsOn(tasks.classes)

    mainClass = mainClassName
    classpath = sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
    workingDir = assetsDir
    isIgnoreExitValue = true

    // Fix for macOS
    jvmArgs = listOf("-XstartOnFirstThread")
}

tasks.register<JavaExec>("debug") {
    dependsOn(tasks.classes)

    mainClass = mainClassName
    classpath = sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
    workingDir = assetsDir
    isIgnoreExitValue = true
    debug = true
}

tasks.register<Jar>("dist") {
    description = "Creates a JAR ready to be distributed"

    dependsOn(tasks.classes)

    from(files(sourceSets["main"].output.classesDirs))
    from(files(sourceSets["main"].output.resourcesDir))
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from({
        configurations.runtimeClasspath.get().filter { file ->
            file.name.endsWith("jar")
        }.map { zipTree(it) }
    })

    from(files(assetsDir))

    manifest {
        attributes["Main-Class"] = mainClassName
    }
}
