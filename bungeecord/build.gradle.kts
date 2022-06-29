dependencies {

    //Text Message API
    implementation("net.kyori", "adventure-platform-bungeecord", "4.1.0")

    //Bungeecord
    compileOnly("net.md-5", "bungeecord-api", "1.19-R0.1-SNAPSHOT")

    //Core Project
    implementation(project(":core"))
}
