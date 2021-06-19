plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
}

val composeVersion = "1.0.0-beta09"
val lifecycleVersion = "2.2.0"

dependencies {
    implementation(project(":shared"))
    implementation("androidx.activity:activity-compose:1.3.0-beta02")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "io.github.wparks.androidApp"
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}