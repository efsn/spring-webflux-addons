import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    val kotlinVersion = "1.3.60"
    val springBootVersion = "2.2.1.RELEASE"

    java
    `maven-publish`
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    id("org.springframework.boot") version springBootVersion
}

// apply(plugin = "java-library")
// apply(plugin = "kotlin")
// apply(plugin = "org.jetbrains.kotlin.plugin.spring")
// apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")
apply(from = rootProject.file("gradle/ktlint.gradle.kts"))

group = "cn.elmi.spring.webflux"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.slf4j:slf4j-api")
    implementation("ch.qos.logback:logback-core")
    implementation("ch.qos.logback:logback-classic")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.codehaus.groovy:groovy:2.5.7")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.2")

    // testImplementation("com.squareup.okhttp3:okhttp:4.2.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
}

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
    }
}

tasks {
    withType<BootJar> {
        launchScript()
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    register<Jar>("sourcesJar"){
        this.group = "build"
        this.archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    test {
        failFast = true
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

publishing {
    repositories {
        maven {
            name = "githubPackages"
            url = uri("${project.findProperty("GITHUB_URI")}/${project.name}")
            credentials {
                username = project.findProperty("gpr.user") as String
                password = project.findProperty("gpr.key") as String
            }
        }
    }
    afterEvaluate {
        publications {
            register<MavenPublication>("gpr") {
                from(components["java"])
                artifact(tasks["sourcesJar"])
                pom {
                    description.set("xx")
                }
            }
        }
    }
}
