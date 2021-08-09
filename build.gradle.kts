buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
        classpath("com.android.tools.build:gradle:7.1.0-alpha02")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.37")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.0")
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:1.3.0")

    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}