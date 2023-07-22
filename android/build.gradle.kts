import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val natives = configurations.create("natives")

val keystorePropertiesFile = rootProject.file("./android/keystore.properties")
val keystoreProperties = Properties()

keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    signingConfigs {
        create("signed") {
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }

    namespace = "pl.baftek.spitfire"

    compileSdk = 33

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
        targetSdk = 33
        versionCode = (findProperty("versionCode") as? String)?.toIntOrNull() ?: 1
        versionName = findProperty("versionName") as? String ?: "1.0.0"
    }

    packagingOptions {
        resources {
            excludes.add("META-INF/robovm/ios/robovm.xml")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("signed")

        }
    }
}

dependencies {
    val gdxVersion: String by rootProject.extra
    val gamesvcsVersion: String by rootProject.extra

    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-android:$gdxVersion")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi-v7a")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-arm64-v8a")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86_64")
    // Freetype
    implementation("com.badlogicgames.gdx:gdx-freetype:$gdxVersion")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-armeabi-v7a")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-arm64-v8a")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-x86")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-x86_64")
    // Play Games
    implementation("com.google.android.gms:play-services-games:23.1.0")
    // Game Services
    implementation("de.golfgl.gdxgamesvcs:gdx-gamesvcs-android-gpgs:$gamesvcsVersion")
}

// called every time gradle gets executed, takes the native dependencies of
// the natives configuration, and extracts them to the proper libs/ folders
// so they get packed with the APK.
tasks.register<Copy>("copyAndroidNatives") {
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
