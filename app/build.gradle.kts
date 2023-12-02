plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 34
    namespace = "com.codegama.timetreeapplication"

    defaultConfig {
        applicationId = "com.codegama.timetreeapplication"
        minSdk = 33
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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



    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }


}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("io.github.inflationx:calligraphy3:3.1.1")
    implementation("io.github.inflationx:viewpump:2.0.3")

    implementation("androidx.exifinterface:exifinterface:1.3.6")
    implementation("androidx.media:media:1.6.0")
    implementation("androidx.vectordrawable:vectordrawable:1.1.0")

    implementation("androidx.room:room-runtime:2.6.0")
    annotationProcessor("androidx.room:room-compiler:2.6.0")

 //   implementation("com.jakewharton:butterknife:10.2.3")
   // annotationProcessor("com.jakewharton:butterknife-compiler:10.2.3")

  //  implementation("com.github.zubairehman:AlarmManager:v1.2.0-alpha01")

   implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("com.applandeo:material-calendar-view:1.7.0")
    implementation("com.github.antonKozyriatskyi:CircularProgressIndicator:1.3.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.github.uzairiqbal91:CircularTimerView:1.0")
    //implementation("com.applandeo:material-calendar-view:1.7.0")



    //implementation("com.jakewharton:butterknife:10.2.3")


}