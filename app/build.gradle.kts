plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.devtools.ksp")
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
    alias(libs.plugins.googleGmsGoogleServices)

}

android {
    namespace = "com.capstone.plasticwise"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.capstone.plasticwise"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"https://plasticwise-be-deploy-ddrv5fqxha-et.a.run.app/api/v1/\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt") ,
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.maps)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.activity)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.databinding.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)

    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.activity.ktx)


    implementation(libs.androidx.room.ktx)
    implementation("androidx.camera:camera-camera2:1.3.3")
    implementation("androidx.camera:camera-lifecycle:1.3.3")
    implementation("androidx.camera:camera-view:1.3.3")
    implementation("androidx.exifinterface:exifinterface:1.3.7")
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("com.airbnb.android:lottie:6.1.0")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation("androidx.paging:paging-runtime-ktx:3.1.1") //PagingRuntime
    implementation("androidx.room:room-paging:2.6.1")

    androidTestImplementation("androidx.arch.core:core-testing:2.1.0") //InstantTaskExecutorRule
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1") //TestDispatcher
    testImplementation("androidx.arch.core:core-testing:2.1.0") // InstantTaskExecutorRule
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1") //TestDispatcher

    //mockito
    testImplementation("org.mockito:mockito-core:4.4.0")
    testImplementation("org.mockito:mockito-inline:4.4.0")

    implementation("com.google.android.gms:play-services-maps:18.0.0")
    implementation("com.google.android.gms:play-services-location:18.0.0")

//    GRAPH and Chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

//   Rounded Glide
    implementation ("com.github.bumptech.glide:glide:4.13.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.0")
    implementation (libs.glide.transformations)
//    UCROP
    implementation ("com.github.yalantis:ucrop:2.2.8-native")
}