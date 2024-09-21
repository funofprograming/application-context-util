package io.github.funofprograming.context.impl;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Concurrency manager for {@linkplain ConcurrentApplicationContextImpl} implementations. This uses a {@linkplain ReentrantReadWriteLock} for mutual exclusion
 * @author Akshay Jain
 *
 */
class ContextConcurrencyManager
{
    private static final Long DEFAULT_TRY_DURATION_MILLISECONDS = Long.MAX_VALUE; //block forever
    
    private final ReadWriteLock readWriteLock;
    
    /**
     * Get an instance using the default try duration millis
     * 
     * @param tryDurationMilliseconds
     * @return
     */
    static ContextConcurrencyManager getInstance()
    {
        return new ContextConcurrencyManager();
    }
    
    /**
     * Initialize using the default try duration millis
     * 
     * @param tryDurationMilliseconds
     * @return
     */
    private ContextConcurrencyManager()
    {
        this.readWriteLock = new ReentrantReadWriteLock();
    }
    
    /**
     * Acquire read lock from ReadWriteLock. This will follow semantics of a {@linkplain ReadWriteLock}
     * 
     * @param timeout
     * @return
     */
    boolean acquireReadLock(Long timeout) 
    {
        boolean locked = false;
        try 
        {
            locked = readWriteLock.readLock().tryLock(resolveTryDurationMilliseconds(timeout), TimeUnit.MILLISECONDS);
        } 
        catch (InterruptedException e) 
        {
            locked = false;
        }
        
        return locked;
    }
    
    /**
     * Acquire read lock from ReadWriteLock. This will follow semantics of a {@linkplain ReadWriteLock}
     * 
     * @return
     */
    boolean acquireReadLock() 
    {
        return acquireReadLock(null);
    }
    
    /**
     * Unlock read lock from ReadWriteLock. This will follow semantics of a {@linkplain ReadWriteLock}
     */
    void releaseReadLock()
    {
        readWriteLock.readLock().unlock();
    }
    
    /**
     * Acquire write lock from ReadWriteLock. This will follow semantics of a {@linkplain ReadWriteLock}
     * 
     * @param timeout
     * @return
     */
    boolean acquireWriteLock(Long timeout) 
    {
        boolean locked = false;
        try 
        {
            locked = readWriteLock.writeLock().tryLock(resolveTryDurationMilliseconds(timeout), TimeUnit.MILLISECONDS);
        } 
        catch (InterruptedException e) 
        {
            locked = false;
        }
        
        return locked;
    }
    
    /**
     * Acquire write lock from ReadWriteLock. This will follow semantics of a {@linkplain ReadWriteLock}
     * 
     * @return
     */
    boolean acquireWriteLock() 
    {
        return acquireWriteLock(null);
    }
    
    /**
     * Unlock write lock from ReadWriteLock. This will follow semantics of a {@linkplain ReadWriteLock}
     */
    void releaseWriteLock()
    {
        readWriteLock.writeLock().unlock();
    }
    
    private long resolveTryDurationMilliseconds(Long tryDurationMilliseconds) {
        
        return Optional.ofNullable(tryDurationMilliseconds).orElse(DEFAULT_TRY_DURATION_MILLISECONDS);
    }
}
