buildscript {
    repositories {
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    }

    dependencies {
        //classpath("com.mobidevelop.robovm:robovm-gradle-plugin:2.3.18")
    }
}

repositories {
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
}

plugins {
    `java-library`
}

apply(plugin = "robovm")

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
    val roboVMVersion: String by rootProject.extra
    val gdxVersion: String by rootProject.extra
    val gamesvcsVersion: String by rootProject.extra

    implementation(project(":core"))
    api("com.mobidevelop.robovm:robovm-rt:$roboVMVersion")
    api("com.mobidevelop.robovm:robovm-cocoatouch:$roboVMVersion")
    api("com.badlogicgames.gdx:gdx-backend-robovm:$gdxVersion")
    api("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-ios")

    implementation("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-ios")

    implementation("de.golfgl.gdxgamesvcs:gdx-gamesvcs-ios-gamecenter:$gamesvcsVersion")
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
