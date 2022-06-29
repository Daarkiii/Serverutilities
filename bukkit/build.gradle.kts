dependencies {

    //Text Message API
    implementation("net.kyori", "adventure-platform-bukkit", "4.1.0")

    //Spigot
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    //Core Project
    implementation(project(":core"))
}
