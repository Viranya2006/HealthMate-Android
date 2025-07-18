plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.healthmate"
    // --- THIS IS THE FIX ---
    // The compileSdk version has been updated from 34 to 35.
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.healthmate"
        minSdk = 26
        targetSdk = 34 // targetSdk can often stay the same for now
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
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

    // For making API calls
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    // For the card-based UI
    implementation("androidx.cardview:cardview:1.0.0")
}
