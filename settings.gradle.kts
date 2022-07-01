
rootProject.name = "OpenSourceBungee"
include("core")
include("bukkit")
include("bungeecord")
include("addons")
include("addons:test")
findProject(":addons:test")?.name = "test"
