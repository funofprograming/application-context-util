package io.github.funofprograming.context.impl

import io.github.funofprograming.context.Key

class ApplicationContextMergeIgnoreStrategy(keys: Set<Key<*>>?=null) : AbstractApplicationContextMergeStrategy(keys) {

    override fun <T> merge(key: Key<T>, oldValue: T?, newValue: T?): T? {
        return if (!super.keySetAvailable() || super.keyAvailable(key)) oldValue else newValue
    }
}