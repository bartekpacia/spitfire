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
    implementation(libs.gdx)
    api(libs.gdx.freetype)
    api(libs.visui)

    implementation(libs.gdx.gamesvcs.core)
}
