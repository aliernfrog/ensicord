plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val composeVersion = "1.4.0-alpha03"
val composeCompilerVersion = "1.3.2"

android {
    namespace = "com.aliernfrog.ensicord"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.aliernfrog.ensicord"
        minSdk = 21
        targetSdk = 33
        versionCode = 4
        versionName = "1.4"
        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = composeCompilerVersion
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")
    implementation("com.google.code.gson:gson:2.10")
    implementation("com.github.aliernfrog:top-toast-compose:0.0.10")
    implementation("com.github.aliernfrog:ensi-generation-kt:1.0.7")
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("com.github.X1nto:OverlappingPanelsCompose:1.2.0")
}