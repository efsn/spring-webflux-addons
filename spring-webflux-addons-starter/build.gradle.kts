import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    `maven-publish`
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-webflux")
    api("io.projectreactor.kotlin:reactor-kotlin-extensions")
}

tasks {
    register<Jar>("sourcesJar") {
        group = "build"
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }
    named<Jar>("jar") {
        enabled = true
    }
    named<BootJar>("bootJar") {
        enabled = false
    }
}

publishing {
    repositories {
        maven {
            val githubUrl = project.findProperty("GITHUB_URI") as String? ?: System.getenv("GITHUB_URL")

            name = "githubPackages"
            url = uri("$githubUrl/${rootProject.name}")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_ACCESS_TOKEN")
            }
        }
    }
    afterEvaluate {
        publications {
            register<MavenPublication>("gpr") {
                from(components["java"])
                artifact(tasks["sourcesJar"])
                pom {
                    description.set("GPR publishing")
                }
            }
        }
    }
}