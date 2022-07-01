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
            return MiniMessage.miniMessage().deserialize(msg)
        }

        @JvmStatic
        fun wrap(msg: String, vararg placeHolder: PlaceHolder) : Component {

            val list: MutableList<TagResolver> = ArrayList()
            placeHolder.iterator().forEach { holder -> list.add(Placeholder.component(holder.name, holder.component)) }

            return MiniMessage.miniMessage().deserialize(msg, *list.toTypedArray())
        }

    }
}