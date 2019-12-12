import org.springframework.boot.gradle.tasks.bundling.BootJar

// repositories {
//     maven {
//         val githubUrl = project.findProperty("GITHUB_URI") as String? ?: System.getenv("GITHUB_URL")
//         name = "githubPackages"
//         url = uri("$githubUrl/${rootProject.name}")
//         credentials {
//             username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
//             password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
//         }
//     }
//     mavenCentral()
//     jcenter()
// }

dependencies {
    implementation(project(":spring-webflux-addons-starter"))
    // implementation("cn.elmi.spring.webflux:spring-webflux-addons-starter:1.0.0-snapshot")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.codehaus.groovy:groovy:2.5.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.2")
}

tasks {
    withType<BootJar> {
        launchScript()
    }
}

