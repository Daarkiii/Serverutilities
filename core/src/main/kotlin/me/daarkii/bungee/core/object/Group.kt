package me.daarkii.bungee.core.`object`

data class Group(
    val id: Int,
    var name: String,
    var potency: Int,
    var permission: String,
    var color: String,
    var default: Boolean
) {

    override fun equals(other: Any?): Boolean {

        if(other == null)
            return false

        if(this === other)
            return true

        if(other !is Group)
            return false

        return other.id == id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}