plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("kotlin-kapt")
  id("com.google.devtools.ksp")
  id("dagger.hilt.android.plugin")
  alias(libs.plugins.google.services)
}

android {
  namespace = "org.dandowney.aimless"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "org.dandowney.aimless"
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.compileSdk.get().toInt()
    versionCode = libs.versions.versionCode.get().toInt()
    versionName = libs.versions.versionName.get()
    setProperty("archivesBaseName", "${applicationId}($versionCode)-$versionName")

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = libs.versions.jvm.get()
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {

  implementation(libs.androidx.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.datastore)
  implementation(libs.androidx.appcompat)

  // Compose
  implementation(platform(libs.compose.bom))
  implementation(libs.compose.ui)
  implementation(libs.compose.ui.graphics)
  implementation(libs.compose.ui.tooling.preview)
  implementation(libs.compose.material)
  implementation(libs.compose.material3)
  implementation(libs.compose.navigation)

  // Hilt
  implementation(libs.dagger.hilt.android)
  implementation(libs.androidx.hilt.navigation.compose)
  kapt(libs.dagger.hilt.android.compiler)
  kapt(libs.androidx.hilt.compiler)

  // Room
  implementation(libs.androidx.room.runtime)
  implementation(libs.androidx.room.ktx)
  ksp(libs.androidx.room.compiler)

  // Firebase
  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.analytics)

  debugImplementation(libs.compose.ui.tooling)
  debugImplementation(libs.compose.ui.test.manifest)

  testImplementation(libs.junit)

  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso)
  androidTestImplementation(platform(libs.compose.bom))
  androidTestImplementation(libs.compose.junit)
}
