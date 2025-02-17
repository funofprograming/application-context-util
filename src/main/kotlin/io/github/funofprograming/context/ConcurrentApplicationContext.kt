package io.github.funofprograming.context

interface ConcurrentApplicationContext : ApplicationContext {

    /**
     * Same as addIfNotPresent in [ApplicationContext] with additional timeout parameter
     * for wait time in case of multiple threads blocking on this method
     *
     * @param <T>
     * @param key
     * @param value
     * @param timeout in millis
    </T> */
    suspend fun <T> addIfNotPresent(key: Key<T>, value: T?, timeout: Long?)

    /**
     * Same as addWithOverwrite in [ApplicationContext] with additional timeout parameter
     * for wait time in case of multiple threads blocking on this method
     *
     * @param <T>
     * @param key
     * @param value
     * @param timeout in millis
     * @return
    </T> */
    suspend fun <T> addWithOverwrite(key: Key<T>, value: T?, timeout: Long?): Any?

    /**
     * Same as add in [ApplicationContext] with additional timeout parameter
     * for wait time in case of multiple threads blocking on this method
     *
     * @param <T>
     * @param key
     * @param value
     * @param timeout in millis
    </T> */
    suspend fun <T> add(key: Key<T>, value: T?, timeout: Long?)

    /**
     * Same as exists in [ApplicationContext] with additional timeout parameter
     * for wait time in case of multiple threads blocking on this method
     *
     * @param <T>
     * @param key
     * @param timeout in millis
     * @return
    </T> */
    suspend fun <T> exists(key: Key<T>, timeout: Long?): Boolean

    /**
     * Same as fetch in [ApplicationContext] with additional timeout parameter
     * for wait time in case of multiple threads blocking on this method
     *
     * @param <T>
     * @param key
     * @param timeout in millis
     * @return
    </T> */
    suspend fun <T> fetch(key: Key<T>, timeout: Long?): T?

    /**
     * Same as erase in [ApplicationContext] with additional timeout parameter
     * for wait time in case of multiple threads blocking on this method
     *
     * @param <T>
     * @param key
     * @param timeout in millis
     * @return
    </T> */
    suspend fun <T> erase(key: Key<T>, timeout: Long?): T?

    /**
     * Same as clear in [ApplicationContext] with additional timeout parameter
     * for wait time in case of multiple threads blocking on this method
     *
     * @param timeout in millis
     */
    suspend fun clear(timeout: Long?)

    /**
     * Same as merge in [ApplicationContext] with additional timeout parameter
     * for wait time in case of multiple threads blocking on this method
     *
     * @param other
     * @param mergeStrategy
     * @param timeout in millis
     */
    suspend fun merge(other: ApplicationContext?, mergeStrategy: ApplicationContextMergeStrategy, timeout: Long?)
}