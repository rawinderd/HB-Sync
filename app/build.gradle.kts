plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.hook2book.hbsync"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.hook2book.hbsync"
        minSdk = 26
        targetSdk = 35
        versionCode = 6
        versionName = "1.0.6"

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
        dataBinding = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.ui.android)
    implementation(libs.swiperefreshlayout)
    implementation(libs.app.update.ktx)
    implementation(libs.androidx.room.runtime.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.recyclerview)
    implementation (libs.jetbrains.kotlin.parcelize.runtime)
    implementation(libs.hilt.android)
    implementation (libs.paperdb)
    implementation(libs.cardview)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)
    /*implementation (libs.androidx.room.runtime)
   kapt ("androidx.room:room-compiler:2.7.2")
// Optional - Kotlin coroutines support for Room
    implementation ("androidx.room:room-ktx:2.7.2")*/
 //  annotationProcessor ("androidx.room:room-compiler:2.7.2")
    implementation ("androidx.room:room-runtime:2.5.2")
    kapt ("androidx.room:room-compiler:2.5.2")
}