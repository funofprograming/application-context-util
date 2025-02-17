package io.github.funofprograming.context.impl

import io.github.funofprograming.context.ApplicationContextMergeStrategy
import io.github.funofprograming.context.Key

/**
 * Abstract implementation of {@linkplain ApplicationContextMergeStrategy}
 *
 * Initialize with a set of keys for whom this strategy is to be applied. If null set passed then strategy is applicable for all keys
 * @param keys
 *
 *  @author Akshay Jain
 *
 */
abstract class AbstractApplicationContextMergeStrategy(protected var keys: Set<Key<*>>? = null) : ApplicationContextMergeStrategy {

    protected fun keySetAvailable(): Boolean {
        return keys.isNullOrEmpty()
    }

    protected fun keyAvailable(key: Key<*>): Boolean {
        return keys?.contains(key) ?: false
    }
}