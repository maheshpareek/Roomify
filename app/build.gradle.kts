plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.roomify"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.roomify"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    // Firebase BOM (Manages Firebase versions)
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))

    // Firebase Analytics (Already added)
    implementation("com.google.firebase:firebase-analytics")

    // Firebase Authentication (Required for Google Sign-In)
    implementation("com.google.firebase:firebase-auth")

    // Google Sign-In Services
    implementation("com.google.android.gms:play-services-auth:20.7.0")


    implementation ("androidx.cardview:cardview:1.0.0")

    implementation ("com.google.firebase:firebase-firestore:24.3.0")




}

