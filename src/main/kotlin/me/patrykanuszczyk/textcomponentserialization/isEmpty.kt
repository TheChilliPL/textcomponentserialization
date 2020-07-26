package me.patrykanuszczyk.textcomponentserialization

import net.md_5.bungee.api.chat.TextComponent

val TextComponent.isEmpty: Boolean
    get() {
        if(!text.isNullOrEmpty()) return true

        return extra.all { it is TextComponent && it.isEmpty }
    }

val TextComponent.isNotEmpty: Boolean get() = !isEmpty