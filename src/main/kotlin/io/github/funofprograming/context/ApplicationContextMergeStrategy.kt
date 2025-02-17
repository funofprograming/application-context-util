package io.github.funofprograming.context

interface ApplicationContextMergeStrategy {

    fun <T> merge(key: Key<T>, oldValue: T?, newValue: T?) : T?
}