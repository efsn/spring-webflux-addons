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
            name = "githubPackages"
            url = uri("${project.findProperty("GITHUB_URI")}/${rootProject.name}")
            credentials {
                println(System.getenv("GITHUB_ACTOR"))
                println("##########")
                println(System.getenv("GITHUB_TOKEN"))

                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
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