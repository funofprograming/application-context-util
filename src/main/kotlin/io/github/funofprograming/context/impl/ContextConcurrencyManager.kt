package io.github.funofprograming.context.impl

import java.util.ConcurrentModificationException
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

internal class ContextConcurrencyManager {

    companion object {

        const val DEFAULT_TRY_DURATION_MILLISECONDS: Long = Long.MAX_VALUE

        fun getInstance(): ContextConcurrencyManager = ContextConcurrencyManager()
    }

    private val readWriteLock: ReadWriteLock = ReentrantReadWriteLock()

    fun <T> executeReadWithLock(timeout: Long?, read: ()->T): T {

        if(readWriteLock.readLock().tryLock(timeout?.toLong()?:DEFAULT_TRY_DURATION_MILLISECONDS, TimeUnit.MILLISECONDS))
            try{
                return read()
            } finally {
                readWriteLock.readLock().unlock()
            }
        else
            throw ConcurrentModificationException("Some other thread modifying this context at the moment")
    }

    fun <T> executeWriteWithLock(timeout: Long?, write: ()->T) : T {
        if(readWriteLock.writeLock().tryLock(timeout?.toLong()?:DEFAULT_TRY_DURATION_MILLISECONDS, TimeUnit.MILLISECONDS))
            try{
                return write()
            } finally {
                readWriteLock.writeLock().unlock()
            }
        else
            throw ConcurrentModificationException("Some other thread modifying this context at the moment")
    }
}