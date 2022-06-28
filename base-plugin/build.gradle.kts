dependencies {

    //Bungeecord Dependency
    compileOnly("net.md-5", "bungeecord-api", "1.19-R0.1-SNAPSHOT")

    //Spigot Dependency

    //MySQL (using HikariCP for a Datasource)
    api("com.zaxxer", "HikariCP", "3.4.5")

    //MongoDB Client
    api("org.mongodb", "mongo-java-driver", "3.12.10")

    //Adventure API for Message sending
    api("net.kyori", "adventure-platform-bukkit", "4.1.0")
    api("net.kyori", "adventure-platform-bungeecord", "4.1.0")
    api("net.kyori", "adventure-text-minimessage", "4.11.0")

    //Files
    api("me.carleslc.Simple-YAML", "Simple-Yaml", "1.7.2")

    //CloudNet
    val version = "3.4.0-RELEASE"

    compileOnly("de.dytanic.cloudnet:cloudnet-driver:$version")
    compileOnly("de.dytanic.cloudnet:cloudnet-cloudperms:$version")
    compileOnly("de.dytanic.cloudnet:cloudnet-bridge:$version")
}