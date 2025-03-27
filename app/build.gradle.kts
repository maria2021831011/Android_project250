

plugins {
    alias(libs.plugins.android.application)  // Android plugin for the app
    id("com.google.gms.google-services")      // Google services plugin (Firebase)
}

android {
    namespace = "com.example.hospitalmanagement"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.hospitalmanagement"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Core libraries
    implementation(libs.appcompat)             // AppCompat for backwards compatibility
    implementation(libs.material)               // Material Design components
    implementation(libs.activity)               // Activity library for lifecycle management
    implementation(libs.constraintlayout)       // ConstraintLayout for flexible UI

    // Firebase libraries
    implementation(platform(libs.firebase.bom))  // Firebase BOM (Bill of Materials)
    implementation(libs.com.google.firebase.firebase.auth)    // Firebase Authentication
    implementation(libs.google.firebase.analytics) // Firebase Analytics
    implementation(libs.com.google.firebase.firebase.database)  // Firebase Realtime Database
    implementation(libs.com.google.firebase.firebase.firestore)
    implementation(libs.firebase.inappmessaging)
    implementation(libs.recyclerview)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.messaging)  // Firebase Firestore




    // Testing libraries
    testImplementation(libs.junit)                // Unit testing framework
    androidTestImplementation(libs.ext.junit)     // JUnit extensions for Android testing
    androidTestImplementation(libs.espresso.core) // Espresso for UI testing
}
