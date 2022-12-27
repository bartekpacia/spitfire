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
    val gdxVersion: String by rootProject.extra
    val gamesvcsVersion: String by rootProject.extra
    val visuiVersion: String by rootProject.extra

    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
    api("com.badlogicgames.gdx:gdx-freetype:$gdxVersion")
    api("com.kotcrab.vis:vis-ui:$visuiVersion")

    implementation("de.golfgl.gdxgamesvcs:gdx-gamesvcs-core:$gamesvcsVersion")
}
