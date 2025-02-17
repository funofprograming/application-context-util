package io.github.funofprograming.context.impl

import io.github.funofprograming.context.ApplicationContext
import io.github.funofprograming.context.ApplicationContextMergeStrategy
import io.github.funofprograming.context.Key

/**
 * Plain vanilla implementation of {@linkplain ApplicationContext}
 *
 *
 * @author Akshay Jain
 * @property name name of the context
 * @property permittedKeys optional set of permitted keys for this context useful when restricted key set to be allowed. null or empty means any key allowed
 */
open class ApplicationContextImpl(private val name: String, private val permittedKeys: Set<Key<*>>? = null) : ApplicationContext {

    private val store: MutableMap<Key<*>, Any?> = mutableMapOf()

    public constructor(name: String, permittedKeys: Set<Key<*>>? = null, initialCopy: ApplicationContext) : this(name, permittedKeys) {
        merge(initialCopy, ApplicationContextMergeOverwriteStrategy(permittedKeys))
    }

    private fun validateKey(key: Key<*>) {
        if(!permittedKeys.isNullOrEmpty() && !permittedKeys.contains(key))
            throw InvalidKeyException("Invalid key: $key. Valid keys for this context are: $permittedKeys")
    }

    override fun getName(): String {
        return name
    }

    override fun getPermittedKeys(): Set<Key<*>>? {
        return permittedKeys
    }

    override fun isKeyValid(key: Key<*>): Boolean {
        try{
            validateKey(key)
            return true
        } catch (e:InvalidKeyException) {
            return false
        }
    }

    override fun <T> addIfNotPresent(key: Key<T>, value: T?) {
        validateKey(key)
        store.putIfAbsent(key, value)
    }

    override fun <T> addWithOverwrite(key: Key<T>, value: T?): Any? {
        validateKey(key)
        return store.put(key, value)
    }

    override fun <T> add(key: Key<T>, value: T?) {
        addWithOverwrite(key, value)
    }

    override fun <T> exists(key: Key<T>): Boolean {
        validateKey(key);
        return store.contains(key)
    }

    override fun <T> fetch(key: Key<T>): T? {
        validateKey(key);
        return store.get(key) as T
    }

    override fun <T> erase(key: Key<T>): T? {
        validateKey(key)
        return store.remove(key) as T
    }

    override fun clear() = store.clear()

    override fun keySet(): Set<Key<*>>? = store.keys.toSet()

    override fun merge(other: ApplicationContext?, mergeStrategy: ApplicationContextMergeStrategy) {

        val keysOther:Set<Key<Any>>? = other?.keySet() as Set<Key<Any>>

        for (keyOther in keysOther?.toSet()?:emptySet()) {
            if (!isKeyValid(keyOther)) {
                continue
            }

            val newValue = other.fetch(keyOther)

            if (exists(keyOther)) {
                val value = mergeStrategy.merge(keyOther, fetch(keyOther), newValue)
                addWithOverwrite(keyOther, value)
            } else {
                addIfNotPresent(keyOther, newValue)
            }
        }
    }

    override fun clone(cloneName: String, clonePermittedKeys: Set<Key<*>>?): ApplicationContext = ApplicationContextImpl(cloneName, clonePermittedKeys, this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApplicationContextImpl

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}