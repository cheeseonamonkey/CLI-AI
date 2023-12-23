plugins {
    kotlin("jvm") version "1.8.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.21"
    application
}


group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))


    // tui


    // async:
    val coroutinesVersion = "1.6.4"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")

    // script
    implementation("eu.jrie.jetbrains:kotlin-shell-core:0.2.1")
    implementation("org.slf4j:slf4j-nop:1.7.26") // ^ kotlin-shell-core dependency

    implementation("org.jetbrains.kotlin:kotlin-scripting-jsr223:1.5.0")

    // http:
    implementation("com.github.kittinunf.fuel:fuel:3.0.0-alpha1")



//    implementation("org.jetbrains.kotlin:kotlin-script-util:1.5.31")
// embedding shell into app
//  implementation("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:1.5.31")

}



tasks.test {
    useJUnitPlatform()
}



kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}





task("MyShellCopyTask") {



    doLast {
        exec {
            workingDir = File("${projectDir}/build/install/untitled3/bin")
            commandLine("sh", "-c", "cp untitled3 chat")
            commandLine("sh", "-c", "cp untitled3.bat chat.bat")

        }
    }
}

