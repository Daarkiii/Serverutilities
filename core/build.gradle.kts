import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.archivesName

/*
 * Copyright 2022 original authors & contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("java-library") //need for publishing
    `maven-publish` //need for publishing
    id("org.jetbrains.dokka") version "1.7.10" //need for javadoc building
}

apply {
    plugin("maven-publish")
    plugin("org.jetbrains.dokka")
}

dependencies {

    //MySQL (using HikariCP for a Datasource)
    api("com.zaxxer", "HikariCP", "3.4.5")

    //MongoDB Client
    api("org.mongodb", "mongo-java-driver", "3.12.10")

    //Text Message API
    api("net.kyori", "adventure-text-minimessage", "4.11.0")

    //Files
    api("me.carleslc.Simple-YAML", "Simple-Yaml", "1.7.2")

    //Database
    implementation("me.daarkii:database-adapter:1.0.0")
}

/**
 * Build settings
 */

val buildName = "server-utilities-api-"

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveFileName.set("$buildName${archiveVersion.get()}-all.jar")
    exclude("*.pom")
    exclude("**/*.kotlin_metadata")
    exclude("**/*.kotlin_module")
    exclude("**/*.kotlin_builtins")
}

val shadowJar: com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar by tasks
val javadoc: Javadoc by tasks
val jar: Jar by tasks
val build: Task by tasks
val clean: Task by tasks

val sourcesForRelease = task<Copy>("sourcesForRelease") {
    from("src/main/kotlin")
    into("build/filteredSrc")
    includeEmptyDirs = false
}

val sourcesJar = task<Jar>("sourcesJar") {
    archiveFileName.set("$buildName${archiveVersion.get()}-sources.jar")
    archiveClassifier.set("sources")
    from(sourcesForRelease.destinationDir)

    dependsOn(sourcesForRelease)
}

//Create javadocs
val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    archiveFileName.set("$buildName${archiveVersion.get()}-javadoc.jar")
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
}

jar.apply {
    archiveFileName.set("$buildName${archiveVersion.get()}.jar")
}

//build all files (normal, javadoc, source)
build.apply {
    dependsOn(jar)
    dependsOn(javadocJar)
    dependsOn(sourcesJar)
    dependsOn(shadowJar)

    jar.mustRunAfter(clean)
    shadowJar.mustRunAfter(sourcesJar)
}

/**
 * Publish settings
 */

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {

    publications {
        create<MavenPublication>("maven") {
            artifactId = "server-utilities-api"
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "nexus"

            url = if(version.toString().contains("SNAPSHOT"))
                uri("https://repo.aysu.tv/repository/snapshots/")
            else
                uri("https://repo.aysu.tv/repository/releases/")

            credentials {
                username = System.getenv("NEXUS_USERNAME")
                password = System.getenv("NEXUS_PASSWORD")
            }
        }
    }
}
