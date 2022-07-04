package me.daarkii.bungee.core.config.impl.messages

import me.daarkii.bungee.core.config.Config
import me.daarkii.bungee.core.utils.PlaceHolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import java.io.File

class Message(private val name: String, private val dataFolder: File) {

    lateinit var config: Config

    init {
        this.load()
        instance = this
    }

    private fun load() {
        val folder = File(dataFolder.absolutePath + "/messages/")
        folder.mkdirs()
        config = MessageFile(folder, name)
    }

    val prefix = Wrapper.wrap(config.getString("messages.prefix"))

    val noPerms = Wrapper.wrap(config.getString("messages.user.noperms"), PlaceHolder("prefix", this.prefix))

    companion object {
        @JvmStatic
        lateinit var instance: Message
            private set
    }

    object Wrapper {

        @JvmStatic
        fun wrap(msg: String) : Component {
            return MiniMessage.miniMessage().deserialize(this.migrate(msg), Placeholder.component("prefix", instance.prefix))
        }

        @JvmStatic
        fun wrap(msg: String, vararg placeHolder: PlaceHolder) : Component {

            val list: MutableList<TagResolver> = ArrayList()
            placeHolder.iterator().forEach { holder -> list.add(Placeholder.component(holder.name, holder.component)) }

            return MiniMessage.miniMessage().deserialize(this.migrate(msg), Placeholder.component("prefix", instance.prefix),  *list.toTypedArray())
        }

        @JvmStatic
        private fun migrate(value: String) : String {

            var current = value
            val builder = StringBuilder()

            /*
            Replace special characters
             */

            current = current
                .replace("&", "§")
                .replace("§l", "<b>")
                .replace("§o", "<i>")
                .replace("§n", "<u>")
                .replace("§m", "<st>")

            val boldArray = current.split("§")

            for(line in boldArray) {

                if(boldArray[0] != line)
                    builder.append("§")

                builder.append(line)

                if(line.contains("<b>"))
                    builder.append("</b>")

                if(line.contains("<i>"))
                    builder.append("</i>")

                if(line.contains("<u>"))
                    builder.append("</u>")

                if(line.contains("<st>"))
                    builder.append("</st>")
            }

            current = builder.toString()

            // Now all Special characters are replaced, so only colors needs to be replaced now
            current = current
                .replace("§0", "<c:black>")
                .replace("§1", "<c:dark_blue>")
                .replace("§2", "<c:dark_green>")
                .replace("§3", "<c:dark_aqua>")
                .replace("§4", "<c:dark_red>")
                .replace("§5", "<c:dark_purple>")
                .replace("§6", "<c:gold>")
                .replace("§7", "<c:gray>")
                .replace("§8", "<c:dark_gray>")
                .replace("§9", "<c:blue>")
                .replace("§a", "<c:green>")
                .replace("§b", "<c:aqua>")
                .replace("§c", "<c:red>")
                .replace("§d", "<c:light_purple>")
                .replace("§e", "<c:yellow>")
                .replace("§f", "<c:white>")

            return current
        }

    }
}