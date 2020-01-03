import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    val kotlinVersion = "1.3.60"
    val springBootVersion = "2.2.1.RELEASE"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("org.springframework.boot") version springBootVersion
}

repositories {
    mavenCentral()
    jcenter()
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(from = rootProject.file("gradle/ktlint.gradle.kts"))

    group = "cn.elmi.spring.webflux"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation(kotlin("reflect"))
        implementation(platform(SpringBootPlugin.BOM_COORDINATES))

        implementation("org.slf4j:slf4j-api")
        implementation("ch.qos.logback:logback-core")
        implementation("ch.qos.logback:logback-classic")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
                freeCompilerArgs = listOf("-Xjsr305=strict")
            }
        }
        test {
            failFast = true
            useJUnitPlatform()
            testLogging {
                events("passed", "skipped", "failed")
            }
            dependsOn(project.tasks.named("ktlint"))
        }
    }

}