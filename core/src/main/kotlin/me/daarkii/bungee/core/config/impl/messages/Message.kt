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

        println(config.getString("messages.prefix"))
        println(config.getString("messages.prefix"))
        println(config.getString("messages.prefix"))
    }

    private fun load() {
        val folder = File(dataFolder.absolutePath + "/messages/")
        folder.mkdirs()
        config = MessageFile(folder, name)
    }

    val prefix = MiniMessage.miniMessage().deserialize(Wrapper.migrate(config.getString("messages.prefix")))

    val noPerms = Wrapper.wrap(config.getString("messages.user.noperms"))

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
        fun migrate(value: String) : String {

            var current = value
            val builder = StringBuilder()

            /*
            Replace special characters
             */

            current = current
                .replace("&", "§")
                .replace("§l", "<replace_b>")
                .replace("§o", "<replace_i>")
                .replace("§n", "<replace_u>")
                .replace("§m", "<replace_st>")

            val boldArray = current.split("§")

            for(line in boldArray) {

                if(boldArray[0] != line)
                    builder.append("§")

                builder.append(line)

                if(line.contains("<replace_b>"))
                    builder.append("</b>")

                if(line.contains("<replace_i>"))
                    builder.append("</i>")

                if(line.contains("<replace_u>"))
                    builder.append("</u>")

                if(line.contains("<replace_st>"))
                    builder.append("</st>")
            }

            current = builder.toString()

            current.replace("<replace_b>", "<b>")
            current.replace("<replace_i>", "<i>")
            current.replace("<replace_u>", "<u>")
            current.replace("<replace_st>", "<st>")

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