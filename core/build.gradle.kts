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

dependencies {

    //MySQL (using HikariCP for a Datasource)
    api("com.zaxxer", "HikariCP", "3.4.5")

    //MongoDB Client
    api("org.mongodb", "mongo-java-driver", "3.12.10")

    //Text Message API
    api("net.kyori", "adventure-text-minimessage", "4.11.0")

    //Files
    api("me.carleslc.Simple-YAML", "Simple-Yaml", "1.7.2")
}
