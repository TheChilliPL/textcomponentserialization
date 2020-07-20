package me.patrykanuszczyk.spigot.textcomponentserialization

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import javax.xml.soap.Text

@JvmName("serializeTextComponent")
fun TextComponent?.serialize(): Any? {
    if(this == null) return null

    val map = mutableMapOf<String, Any>()

    if (!text.isNullOrEmpty())
        map += "text" to text

    if (!extra.isNullOrEmpty())
        map += "extra" to extra.map { (it as TextComponent).serialize() }

    if (colorRaw != null)
        map += "color" to serializeChatColor(colorRaw)!!
    if (isBoldRaw != null)
        map += "bold" to isBoldRaw
    if (isItalicRaw != null)
        map += "italic" to isItalicRaw
    if (isUnderlinedRaw != null)
        map += "underlined" to isUnderlinedRaw
    if (isStrikethroughRaw != null)
        map += "strikethrough" to isStrikethroughRaw
    if (isObfuscatedRaw != null)
        map += "obfuscated" to isObfuscatedRaw

    if (insertion != null)
        map += "insertion" to insertion

    if (clickEvent != null) {
//        map += "clickEvent" to mapOf(
//            "action" to clickEvent.action.name,
//            "value" to clickEvent.value
//        )

        map += "clickEvent" to serializeClickEvent(clickEvent)!!
    }
    if (hoverEvent != null) {
//        map += "hoverEvent" to mapOf(
//            "action" to hoverEvent.action.name,
//            "value" to hoverEvent.value.map {
//                (it as TextComponent).serialize()
//            }
//        )

        map += "hoverEvent" to serializeHoverEvent(hoverEvent)!!
    }

    if (map.keys.size == 1) {
        when (map.keys.first()) {
            "text" -> return map.getValue("text")
            "extra" -> return map.getValue("extra")
        }
    } else if(map.keys.isEmpty()) {
        return ""
    }

    return map
}

fun serializeChatColor(color: ChatColor?): Any? {
    if(color == null) return null

    return color.name
        .replace('_', ' ')
        .toLowerCase()
}

fun serializeClickEvent(event: ClickEvent?): Any? {
    if(event == null) return null

    val action = event.action.name
        .replace('_', ' ')
        .toLowerCase()

    val value = event.value

    return mapOf(
        "action" to action,
        "value" to value
    )
}

fun serializeHoverEvent(event: HoverEvent?): Any? {
    if(event == null) return null

    val action = event.action.name
        .replace('_', ' ')
        .toLowerCase()

    val values = event.value

    val component = (if(values.size == 1)
        values[0]
    else
        TextComponent(*values)) as TextComponent

    val value = component.serialize()

    return mapOf(
        "action" to action,
        "value" to value
    )
}