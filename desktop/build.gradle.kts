java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/"))
        }
    }
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
    // from { configurations.compile.collect { zipTree(it) } } // FIXME: make it work in KTS
    from(files(assetsDir))

    manifest {
        attributes(Pair("Main-Class", mainClassName))
    }
}
