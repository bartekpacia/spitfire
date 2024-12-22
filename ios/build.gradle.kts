plugins {
    `java-library`
    id("robovm") version "2.3.21"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
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

    // TODO: Can this be "implementation" instead?
    api(libs.robovm.rt)
    api(libs.robovm.cocoatouch)
    api(libs.gdx.backend.robovm)
    api(variantOf(libs.gdx.platform) { classifier("natives-ios") })

    implementation(variantOf(libs.gdx.freetype.platform) { classifier("natives-ios") })

    implementation(libs.gdx.gamesvcs.ios.gamecenter)
}

robovm {
    val isAppStore = System.getenv("SPITFIRE_APP_STORE")
    val profile = if (isAppStore == "true") "AppStore" else "AdHoc"

    iosSignIdentity = "Apple Distribution: Bartek Pacia (VX8P4VC6NH)"
    iosProvisioningProfile = "match $profile pl.baftek.spitfire"
    isIosSkipSigning = false
    archs = "arm64"
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
