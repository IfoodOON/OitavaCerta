plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.pedrobonini.oitavacerta.settingsdata"
    compileSdk = 37

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":tuning-data"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)
}
