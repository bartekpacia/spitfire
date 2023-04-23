pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.name == "robovm") {
                useModule("com.mobidevelop.robovm:robovm-gradle-plugin:${requested.version}")
            }
        }
    }

    plugins {
        id("robovm") version "2.3.19" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS

    repositories {
        mavenCentral()
        google()
    }
}

include("desktop", "android", "ios", "core")
