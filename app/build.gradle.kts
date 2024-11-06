plugins {
    alias(libs.plugins.android.application)  // Ensure this is correctly defined in your version catalog
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 29
        targetSdk = 34
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

    // Correctly configured lintOptions
    lint {
        abortOnError = false // Prevent build failure on lint errors
        // You can customize lint checks here if needed
    }
}

dependencies {
    implementation(libs.appcompat)              // androidx.appcompat:appcompat
    implementation(libs.material)               // com.google.android.material:material
    implementation(libs.activity)               // androidx.activity:activity
    implementation(libs.constraintlayout)       // androidx.constraintlayout:constraintlayout
    testImplementation(libs.junit)              // junit:junit
    androidTestImplementation(libs.ext.junit)   // androidx.test.ext:junit
    androidTestImplementation(libs.espresso.core)  // androidx.test.espresso:espresso-core
}
