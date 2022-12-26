sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/"))
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.compileTestJava {
    options.encoding = "UTF-8"
}

extra.apply {
    set("mainClassName", "pl.baftek.spitfire.IOSLauncher")
}

tasks.launchIPhoneSimulator {
    dependsOn(tasks.build)
}

tasks.launchIPadSimulator {
    dependsOn(tasks.build)
}

tasks.launchIOSDevice {
    dependsOn(tasks.build)
}

tasks.createIPA {
    dependsOn(tasks.build)
}
