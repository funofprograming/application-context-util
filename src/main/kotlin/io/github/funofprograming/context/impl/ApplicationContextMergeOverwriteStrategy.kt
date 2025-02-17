package io.github.funofprograming.context.impl

import io.github.funofprograming.context.Key

class ApplicationContextMergeOverwriteStrategy(keys: Set<Key<*>>?=null) : AbstractApplicationContextMergeStrategy(keys) {

    override fun <T> merge(key: Key<T>, oldValue: T?, newValue: T?): T? {
        return if (!super.keySetAvailable() || super.keyAvailable(key)) newValue else oldValue
    }
}