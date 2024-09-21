package io.github.funofprograming.context.impl;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
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
     * Acquire read lock from ReadWriteLock. This will follow semantics of a {@linkplain ReadWriteLock}
     * 
     * @return
     */
    LockAutoCloseable acquireReadLockCloseable() 
    {
        return acquireReadLockCloseable(null);
    }
    
    /**
     * Acquire read lock from ReadWriteLock. This will follow semantics of a {@linkplain ReadWriteLock}
     * 
     * @param timeout
     * @return
     */
    LockAutoCloseable acquireReadLockCloseable(Long timeout) 
    {
        boolean locked = acquireReadLock(timeout);
        return new LockAutoCloseable(AcquiredLock.AcquiredLockType.READ, locked, this);
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
     * Acquire write lock from ReadWriteLock. This will follow semantics of a {@linkplain ReadWriteLock}
     * 
     * @return
     */
    LockAutoCloseable acquireWriteLockCloseable() 
    {
        return acquireWriteLockCloseable(null);
    }
    
    /**
     * Acquire write lock from ReadWriteLock. This will follow semantics of a {@linkplain ReadWriteLock}
     * 
     * @param timeout
     * @return
     */
    LockAutoCloseable acquireWriteLockCloseable(Long timeout) 
    {
        boolean locked = acquireWriteLock(timeout);
        return new LockAutoCloseable(AcquiredLock.AcquiredLockType.WRITE, locked, this);
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
    
    static interface AcquiredLock extends AutoCloseable
    {
	static enum AcquiredLockType
	{
	    READ, WRITE;
	}
	
	void close();
    }
    
    static class LockAutoCloseable implements AcquiredLock
    {
	private final AcquiredLockType lockType;
	private final AtomicBoolean isLocked;
	private final ContextConcurrencyManager contextConcurrencyManager;
	
	public LockAutoCloseable(AcquiredLockType lockType, boolean isLocked, ContextConcurrencyManager contextConcurrencyManager)
	{
	    this.lockType = lockType;
	    this.isLocked = new AtomicBoolean(isLocked);
	    this.contextConcurrencyManager = contextConcurrencyManager;
	}
	
	@Override
	public void close()
	{
	    if(!isLocked.get())
		return; //not isLocked nothing to do
	    
	    switch (lockType) {
        	    case READ -> contextConcurrencyManager.releaseReadLock();
        	    case WRITE -> contextConcurrencyManager.releaseWriteLock();
        	    default -> throw new IllegalArgumentException("Unexpected value: " + lockType);
	    }
	    
	    isLocked.set(false);
	}
	
	public boolean isLocked()
	{
	    return isLocked.get();
	}
    }
}
