plugins {
    `java-library`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/"))
        }
    }
}

dependencies {
    val gdxVersion: String by rootProject.extra
    val gamesvcsVersion: String by rootProject.extra

    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-desktop")

    implementation("de.golfgl.gdxgamesvcs:gdx-gamesvcs-desktop-gpgs:$gamesvcsVersion")
}

val mainClassName by extra("pl.baftek.spitfire.desktop.DesktopLauncher")
val assetsDir by extra(File("../android/assets"))

tasks.register<JavaExec>("run") {
    dependsOn(tasks.classes)

    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
    workingDir = assetsDir
    isIgnoreExitValue = true
}

tasks.register<JavaExec>("debug") {
    dependsOn(tasks.classes)

    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
    workingDir = assetsDir
    isIgnoreExitValue = true
    debug = true
}

tasks.register<Jar>("dist") {
    dependsOn(tasks.classes)

    from(files(sourceSets["main"].output.classesDirs))
    from(files(sourceSets["main"].output.resourcesDir))
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    // configurations.compileClasspath.files.forEach {
    //     from(if (it.isDirectory) it else zipTree(it))
    // }
    // duplicatesStrategy = DuplicatesStrategy.INCLUDE
    // from { configurations.compile.collect { zipTree(it) } } // FIXME: make it work in KTS

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })

    from(files(assetsDir))

    manifest {
        attributes["Main-Class"] = mainClassName
    }
}
