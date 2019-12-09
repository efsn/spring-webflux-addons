import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":spring-webflux-addons-starter"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.codehaus.groovy:groovy:2.5.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.2")
}

tasks {
    withType<BootJar> {
        launchScript()
    }
}

