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
    implementation(libs.gdx)
    api(libs.gdx.freetype)
    api(libs.visui)

    implementation(libs.gdx.gamesvcs.core)
}
