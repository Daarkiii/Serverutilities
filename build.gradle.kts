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

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
    id("java")
    id ("com.github.johnrengelman.shadow") version "7.0.0"
}

allprojects {

    group = "me.daarkii"
    version = "1.0.1"

    apply {
        plugin("java")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("com.github.johnrengelman.shadow")
    }

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://repo.cloudnetservice.eu/repository/releases/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
}

subprojects {

    dependencies {

        //Kotlin
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.0-Beta")

        //CloudNet
        val version = "3.4.0-RELEASE"

        compileOnly("de.dytanic.cloudnet:cloudnet-driver:$version")
        compileOnly("de.dytanic.cloudnet:cloudnet-cloudperms:$version")
        compileOnly("de.dytanic.cloudnet:cloudnet-bridge:$version")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

}