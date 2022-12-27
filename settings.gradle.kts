pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
        // maven("https://oss.sonatype.org/content/repositories/releases/")
        // maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.name == "robovm") {
                println("found! namespace: ${requested.id.namespace}, name: ${requested.id.name}")
                useModule("com.mobidevelop.robovm:robovm-gradle-plugin:${requested.version}")
            }
        }
    }

    plugins {
        id("robovm") version "2.3.18" apply false
    }
}

include("desktop", "android", "ios", "core")
