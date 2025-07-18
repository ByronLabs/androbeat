import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("jacoco")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    id("org.sonarqube") version "4.0.0.2929"
}

kotlin {
    jvmToolchain(21)
}

android {
    namespace = "com.androbeat.androbeatagent"
    compileSdk = 35

    packaging {
        resources.excludes.addAll(
            listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "/lib/arm64-v8a/libmockkjvmtiagent.so"
            )
        )
        jniLibs.excludes.add("/lib/arm64-v8a/libmockkjvmtiagent.so")
    }

    defaultConfig {
        applicationId = "com.androbeat.androbeatagent"
        minSdk = 26
        targetSdk = 35
        versionCode = 5
        versionName = "1.1.5"

        testInstrumentationRunner = "com.androbeat.androbeatagent.HiltTestRunner"

        val properties = Properties().apply {
            load(rootProject.file("local.properties").reader())
        }

        buildConfigField("String", "ELASTIC_HOST", properties["ELASTIC_HOST"].toString())
        buildConfigField("String", "ELASTIC_USER", properties["ELASTIC_USER"].toString())
        buildConfigField("String", "ELASTIC_PASS", properties["ELASTIC_PASS"].toString())
        buildConfigField("Boolean", "DEBUG_SENSORS", properties["DEBUG_SENSORS"].toString())
        buildConfigField("Boolean", "DEBUG_EXTRACTORS", properties["DEBUG_EXTRACTORS"].toString())
        buildConfigField("Boolean", "READ_LOGS", properties["READ_LOGS"].toString())
        buildConfigField("Boolean", "DEBUG_RETROFIT", properties["DEBUG_RETROFIT"].toString())
        buildConfigField("String", "ENROLLMENT_TOKEN", properties["ENROLLMENT_TOKEN"].toString()
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        debug {
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.0"
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
    }

    sourceSets {
        getByName("test") {
            java.srcDirs("src/test/java", "src/robolectric/java")
        }
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
        emulatorSnapshots {
            maxSnapshotsForTestFailures = 0
        }
        packaging {
            jniLibs {
                useLegacyPackaging = true
            }
        }
    }


    applicationVariants.all {
        outputs.all {
            val output = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            val variantName = name
            val versionName = versionName
            val versionCode = versionCode
            val newApkName = "ANDROBEAT-$variantName-v$versionName-$versionCode.apk"
            output.outputFileName = newApkName
        }
    }

}

// Patrones de exclusión actualizados
val exclusionPatterns = listOf(
    //Main exclusions patterns
    "**/jdk/internal/**",
    "**/android/databinding/**",
    "**/databinding/**",
    "**/Dagger*",
    "**/dagger/**",
    "**/di/hiltModules/**",
    "**/Hilt_*.class",
    "**/Hilt*",
    "**/R.class",
    "**/BuildConfig.*",
    "**/Manifest.*",
    "**/androidx/**",
    "**/android/support/**",
    "**/com/google/android/gms/**",
    "**/EntityInsertionAdapter*",
    "**/EntityDeletionOrUpdateAdapter*",
    "**/Callable*",
    "**/*_Impl.class",
    "**/ViewBinding*",
    "**/*.special..inlined.*",
    "**/*$$*",
    "**/*_Module*",
    "**/*_Factory*",
    "**/utils/definitions/*",
    "**/data/model/models/Result*",
    "**/data/remote/**",
    "**/view/**",
    //DI módules
    "/di/hiltModules/",
    "/di/hiltModules",
    "**/di/hiltModules/**",
    "**/*\$toString*",
    "**/*Dagger*",
    "**/*Hilt*",
    "**/*__Impl*new*",
    "**/*new_Function*",
    "**/*\$*",
    "/data/converters/",
    // Fragmentos de pestañas
    "**/fragments/tabs/**",
    "**/*TabFragment*",
    "**/ui/tabs/**",
   " com.androbeat.androbeatagent.presentation.tabFragments",
    "**/presentation/tabFragments/**",
    //Others
    "**/_init_**",
    "**/_init_*",
    "**/_init_$*",
    "**/hilt_aggregated_deps/*",
    "**/utils/enums/*",
    "com/androbeat/androbeatagent/AndroBeatApplication.class",
    "com/androbeat/androbeatagent/utils/MyDeviceAdminReceiver.class",
)

tasks.withType(Test::class) {
    configure<JacocoTaskExtension> {
        excludes = exclusionPatterns
    }
    ignoreFailures = true
}

// Define task names for unit tests and Android tests
val unitTests = "testDebugUnitTest"
val androidTests = "connectedDebugAndroidTest"
tasks.register<JacocoReport>("JacocoDebugCodeCoverage") {
    // Depend on unit tests, Android tests, and compilation tasks
    dependsOn(listOf("compileDebugJavaWithJavac", "compileDebugKotlin", unitTests, androidTests))
    // Set task grouping and description
    group = "Reporting"
    description = "Execute UI and unit tests, generate and combine Jacoco coverage report"
    // Configure reports to generate both XML and HTML formats
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    // Set source directories to the main source directory
    sourceDirectories.setFrom(layout.projectDirectory.dir("src/main/java"), layout.projectDirectory.dir("src/main/kotlin"))
    // Set class directories to compiled Java and Kotlin classes, excluding specified exclusions
    classDirectories.setFrom(files(
        fileTree(layout.buildDirectory.dir("intermediates/javac/debug")) {
            exclude(exclusionPatterns)
        },
        fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
            exclude(exclusionPatterns)
        }
    ))
    // Collect execution data from .exec and .ec files generated during test execution
    executionData.setFrom(files(
        fileTree(layout.buildDirectory) { include(listOf("**/*.exec", "**/*.ec")) }
    ))

    doLast {
        println("JacocoDebugCodeCoverage report folder: ${reports.html.outputLocation.get().asFile.absolutePath}")
    }
}

tasks.register<JacocoReport>("JacocoDebugCodeCoverageJustUnitTest") {
    // Depend on unit tests, Android tests, and compilation tasks
    dependsOn(listOf("compileDebugJavaWithJavac", "compileDebugKotlin", unitTests))
    // Set task grouping and description
    group = "Reporting"
    description = "Execute UI and unit tests, generate and combine Jacoco coverage report"
    // Configure reports to generate both XML and HTML formats
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    // Set source directories to the main source directory
    sourceDirectories.setFrom(layout.projectDirectory.dir("src/main/java"), layout.projectDirectory.dir("src/main/kotlin"))
    // Set class directories to compiled Java and Kotlin classes, excluding specified exclusions
    classDirectories.setFrom(files(
        fileTree(layout.buildDirectory.dir("intermediates/javac/debug")) {
            exclude(exclusionPatterns)
        },
        fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
            exclude(exclusionPatterns)
        }
    ))
    // Collect execution data from .exec and .ec files generated during test execution
    executionData.setFrom(files(
        fileTree(layout.buildDirectory) { include(listOf("**/*.exec", "**/*.ec")) }
    ))

    doLast {
        println("JacocoDebugCodeCoverageJustUnitTest report folder: ${reports.html.outputLocation.get().asFile.absolutePath}")
    }
}

tasks.named<JacocoReport>("JacocoDebugCodeCoverage") {
    dependsOn("generateReleaseUnitTestConfig")
    dependsOn("testReleaseUnitTest")
}

val annotationVersion = "1.9.1"
val appcompatVersion = "1.7.0"
val composeRuntimeVersion = "1.6.8"
val composeUiTestVersion = "1.6.8"
val constraintLayoutVersion = "2.1.4"
val coroutinesVersion = "1.7.3"
val coreTestingVersion = "2.1.0"
val espressoCoreVersion = "3.6.1"
val gsonVersion = "2.12.1"
val hiltVersion = "2.55"
val junit4Version = "4.13.2"
val junitExtVersion = "1.2.1"
val junitVersion = "4.13.2"
val kotlinStdlibVersion = "2.0.1"
val kotlinReflectVersion = "1.8.1"
val lifecycleVersion = "2.8.4"
val lifecycleViewModelKtxVersion = "2.8.4"
val materialVersion = "1.12.0"
val mockitoInlineVersion = "4.11.0"
val mockitoVersion = "5.15.2"
val mockkVersion = "1.13.16"
val navigationVersion = "2.7.7"
val okhttpLoggingInterceptorVersion = "4.12.0"
val pahoAndroidServiceVersion = "1.1.1"
val playServicesFitnessVersion = "21.2.0"
val retrofitVersion = "2.11.0"
val roomVersion = "2.6.1"
val rulesVersion = "1.6.1"
val kotlinReflectVersionForTest = "1.9.10"
val workVersion = "2.9.1"
val fragmentTesting = "1.5.7"
val fragmentVersion = "1.5.7"

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("androidx.test:rules:$rulesVersion")
    implementation("androidx.compose.ui:ui-test-android:$composeUiTestVersion")
    implementation("androidx.annotation:annotation:$annotationVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("com.google.android.gms:play-services-fitness:$playServicesFitnessVersion")
    implementation("androidx.test.uiautomator:uiautomator:2.4.0-alpha01")
    implementation("com.google.android.gms:play-services-ads-identifier:18.2.0")
    implementation("androidx.lifecycle:lifecycle-service:$lifecycleVersion")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.2")
    implementation("androidx.media3:media3-extractor:1.6.0-alpha02")
    implementation("androidx.activity:activity:1.10.0")
    implementation("androidx.test.espresso:espresso-intents:3.6.1")
    implementation("androidx.test.ext:junit-ktx:1.2.1")
    androidTestImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-scalars:$retrofitVersion")
    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpLoggingInterceptorVersion")
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    implementation("com.google.android.material:material:$materialVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutVersion")
    implementation("androidx.navigation:navigation-fragment:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    implementation("androidx.work:work-runtime:$workVersion")
    implementation("androidx.work:work-runtime-ktx:$workVersion")
    kapt("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-rxjava3:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    annotationProcessor("com.google.dagger:hilt-compiler:$hiltVersion")
    testImplementation("junit:junit:$junit4Version")
    androidTestImplementation("androidx.test.ext:junit:$junitExtVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinStdlibVersion")
    implementation("androidx.compose.runtime:runtime:$composeRuntimeVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleViewModelKtxVersion")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinStdlibVersion")


    // Testing dependencies
    androidTestImplementation("org.mockito:mockito-core:$mockitoVersion")
    androidTestImplementation("org.mockito:mockito-android:$mockitoVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-android:$mockitoVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoCoreVersion")
    testImplementation("androidx.test.espresso:espresso-core:$espressoCoreVersion")
    implementation("androidx.test.espresso:espresso-core:$espressoCoreVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinReflectVersionForTest")
    androidTestImplementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinReflectVersionForTest")
    androidTestImplementation("io.mockk:mockk-android:$mockkVersion")
    implementation("io.mockk:mockk:$mockkVersion")
//    testImplementation("org.mockito:mockito-inline:$mockitoInlineVersion")
//    androidTestImplementation("org.mockito:mockito-inline:$mockitoInlineVersion")
    testImplementation("androidx.arch.core:core-testing:$coreTestingVersion")
    androidTestImplementation("androidx.arch.core:core-testing:$coreTestingVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinReflectVersion")
    debugImplementation("androidx.fragment:fragment-testing:$fragmentVersion")
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")

    // Hilt Testing Library
    implementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hiltVersion")
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    // Agregar explícitamente para API 28+
    androidTestImplementation("org.objenesis:objenesis:3.3")
    androidTestImplementation("net.bytebuddy:byte-buddy:1.12.23")
    androidTestImplementation("net.bytebuddy:byte-buddy-agent:1.12.23")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}