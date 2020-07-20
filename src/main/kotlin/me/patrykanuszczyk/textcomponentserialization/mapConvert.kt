package me.patrykanuszczyk.textcomponentserialization

@JvmSynthetic
internal inline fun <reified K, reified V> Map<*, *>.toMapOf(): MutableMap<K, V> {
    val map = mutableMapOf<K, V>()

    for ((key, value) in this) {
        require(key is K) { "Key $key with value $value cannot be converted to ${K::class.qualifiedName}!" }
        require(value is V) { "Value $value of key $key cannot be converted to ${V::class.qualifiedName}!" }

        map[key] = value
    }

    return map
}