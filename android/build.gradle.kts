import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
}

val natives = configurations.create("natives")

val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(rootProject.file("./android/keystore.properties")))

android {
    compileSdk = 34
    namespace = "pl.baftek.spitfire"

    sourceSets {
        getByName("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.setSrcDirs(listOf("src"))
            aidl.setSrcDirs(listOf("src"))
            renderscript.setSrcDirs(listOf("src"))
            res.setSrcDirs(listOf("res"))
            assets.setSrcDirs(listOf("assets"))
            jniLibs.setSrcDirs(listOf("libs"))
        }
    }

    defaultConfig {
        applicationId = "pl.baftek.spitfire"
        minSdk = 21
        targetSdk = 34
        versionCode = (findProperty("versionCode") as? String)?.toIntOrNull() ?: 1
        versionName = findProperty("versionName") as? String ?: "1.0.0"
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packaging {
        resources {
            excludes.add("META-INF/robovm/ios/robovm.xml")
        }
    }

    signingConfigs {
        create("signed") {
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }

    buildTypes {
        named("debug") {
            signingConfig = signingConfigs.getByName("debug")
            isShrinkResources = false
            isMinifyEnabled = false
        }

        getByName("release") {
            signingConfig = signingConfigs.getByName("signed")
            isShrinkResources = true
            isMinifyEnabled = true

            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    implementation(projects.core)
    implementation(libs.gdx.backend.android)
    natives(variantOf(libs.gdx.platform) { classifier("natives-armeabi-v7a") })
    natives(variantOf(libs.gdx.platform) { classifier("natives-arm64-v8a") })
    natives(variantOf(libs.gdx.platform) { classifier("natives-x86") })
    natives(variantOf(libs.gdx.platform) { classifier("natives-x86_64") })
    implementation(libs.gdx.freetype)
    natives(variantOf(libs.gdx.freetype.platform) { classifier("natives-armeabi-v7a") })
    natives(variantOf(libs.gdx.freetype.platform) { classifier("natives-arm64-v8a") })
    natives(variantOf(libs.gdx.freetype.platform) { classifier("natives-x86") })
    natives(variantOf(libs.gdx.freetype.platform) { classifier("natives-x86_64") })
    implementation(libs.play.services.games)
    implementation(libs.gdx.gamesvcs.android.gpgs)
}

tasks.named("preBuild").configure {
    dependsOn("copyAndroidNatives")
}

// called every time gradle gets executed, takes the native dependencies of
// the natives configuration, and extracts them to the proper libs/ folders
// so they get packed with the APK.
tasks.register("copyAndroidNatives") {
    doFirst {
        file("libs/armeabi-v7a/").mkdirs()
        file("libs/arm64-v8a/").mkdirs()
        file("libs/x86_64/").mkdirs()
        file("libs/x86/").mkdirs()

        project.configurations["natives"].files.forEach { jar ->
            var outputDir: File? = null
            if (jar.name.endsWith("natives-arm64-v8a.jar")) outputDir = file("libs/arm64-v8a")
            if (jar.name.endsWith("natives-armeabi-v7a.jar")) outputDir = file("libs/armeabi-v7a")
            if (jar.name.endsWith("natives-x86_64.jar")) outputDir = file("libs/x86_64")
            if (jar.name.endsWith("natives-x86.jar")) outputDir = file("libs/x86")
            if (outputDir != null) {
                copy {
                    from(zipTree(jar))
                    into(outputDir)
                    include("*.so")
                }
            }
        }
    }
}

tasks.register<Exec>("run") {
    description = "Runs the application with ADB"
    group = "install"

    val localProperties = project.file("../local.properties")
    val path = if (localProperties.exists()) {
        val properties = Properties()
        properties.load(localProperties.inputStream())
        val sdkDir = properties.getProperty("sdk.dir")
        sdkDir ?: System.getenv("ANDROID_HOME")
    } else {
        System.getenv("ANDROID_HOME")
    }

    val adb = "$path/platform-tools/adb"
    commandLine(adb, "shell", "am", "start", "-n", "pl.baftek.spitfire/pl.baftek.spitfire.AndroidLauncher")
}
