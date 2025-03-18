package io.github.funofprograming.context

/**
 * Application context key with type safety.
 *
 * @author Akshay Jain
 *
 * @param <T>
 * @property keyName name of the key
 * @property valueType type of value that will be stored for this key in the context
 */
data class Key<T> (val keyName: String, val valueType: KeyType<T>) {

    override fun toString(): String {
        return "$keyName<$valueType>"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Key<*>

        return keyName == other.keyName
    }

    override fun hashCode(): Int {
        return keyName.hashCode()
    }

    companion object {
        inline fun <reified T> of(keyName: String):Key<T> = Key<T>(keyName, KeyType.of<T>())
        fun <T> of(keyName: String, valueType: KeyType<T>):Key<T> = Key<T>(keyName, valueType) //needed for Java compatibility since inline functions don't work in Java
        fun <T> of(keyName: String, valueType: Class<T>):Key<T> = Key<T>(keyName, KeyType.of(valueType)) //needed for Java compatibility since inline functions don't work in Java
    }
}