package io.github.funofprograming.context

interface ApplicationContext {

    /**
     * Application context name
     *
     * @return Application context name
     */
    fun getName(): String

    /**
     * Keys permitted for this context. Empty set means all keys permitted.
     *
     * Implementations should allow setting up of PermittedKeys at time of context initialization
     *
     * @return set of permitted keys
     */
    fun getPermittedKeys(): Set<Key<*>>?

    /**
     * Checks if key is valid for this context.
     *
     *
     * @return key
     */
    fun isKeyValid(key: Key<*>): Boolean

    /**
     * Add value for given key if not already present.
     *
     * This is semmantically same as
     *
     * `if(!exists(key))
     * add(key, value)
    ` *
     *
     *
     * @param <T>
     * @param key
     * @param value
    </T> */
    fun <T> addIfNotPresent(key: Key<T>, value: T?)

    /**
     * Add value for given key even if already present.
     *
     *
     * @param <T>
     * @param key
     * @param value
     * @return Previous value associated with key if any or null
    </T> */
    fun <T> addWithOverwrite(key: Key<T>, value: T?): Any?

    /**
     * Add value for given key.
     *
     *
     * @param <T>
     * @param key
     * @param value
    </T> */
    fun <T> add(key: Key<T>, value: T?)

    /**
     * Check if any value or null associated with give key
     *
     * @param <T>
     * @param key
     * @return true if value or null associated with give key otherwise false
    </T> */
    fun <T> exists(key: Key<T>): Boolean

    /**
     * Fetch value associated with key or null
     *
     * @param <T>
     * @param key
     * @return value associated with key or null
    </T> */
    fun <T> fetch(key: Key<T>): T?

    /**
     * Erase value associated with key if any
     *
     * @param <T>
     * @param key
     * @return value associate with key or null
    </T> */
    fun <T> erase(key: Key<T>): T?

    /**
     * Clear the entire context
     */
    fun clear()

    /**
     * Get a set of keys in context
     *
     * @return set of keys in context
     */
    fun keySet(): Set<Key<*>>?

    /**
     * Merge other context with this using the given merge strategy
     *
     * @param other other context. If null then no effect on this context
     * @param mergeStrategy
     */
    fun merge(other: ApplicationContext?, mergeStrategy: ApplicationContextMergeStrategy)

    /**
     * Creates a clone of this context with passed cloneName and clonePermittedKeys. While cloning only the permittedKeys will be added
     *
     * @param cloneName name of the cloned ApplicationContext
     * @param clonePermittedKeys permittedKeys of the cloned ApplicationContext
     */
    fun clone(cloneName: String, clonePermittedKeys: Set<Key<*>>? = null): ApplicationContext
}