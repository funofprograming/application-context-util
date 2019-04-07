package fop.context.impl;

import java.util.ConcurrentModificationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import fop.context.ConcurrentApplicationContext;

/**
 * Concurrency manager for {@linkplain ConcurrentApplicationContext} implementations. This uses a {@linkplain ReentrantReadWriteLock} for mutual exclusion
 * @author Akshay Jain
 *
 */
public class ContextConcurrencyManager
{
    private static final Integer DEFAULT_TRY_DURATION_MILLISECONDS = 0;
    
    private final Integer tryDurationMilliseconds;
    private final ReadWriteLock readWriteLock;
    
    /**
     * Get an instance using the default try duration millis
     * 
     * @param tryDurationMilliseconds
     * @return
     */
    public static ContextConcurrencyManager getInstance(Integer tryDurationMilliseconds)
    {
        return new ContextConcurrencyManager(tryDurationMilliseconds);
    }
    
    /**
     * Initialize using the default try duration millis
     * 
     * @param tryDurationMilliseconds
     * @return
     */
    private ContextConcurrencyManager(Integer tryDurationMilliseconds)
    {
        this.tryDurationMilliseconds = tryDurationMilliseconds != null ? tryDurationMilliseconds : DEFAULT_TRY_DURATION_MILLISECONDS;
        this.readWriteLock = new ReentrantReadWriteLock();
    }
    
    /**
     * Acquire read lock from ReadWriteLock. This will follow semantics of a {@linkplain ReadWriteLock}
     * 
     * @param timeout
     * @return
     */
    public boolean acquireReadLock(int timeout) 
    {
        boolean locked = false;
        try 
        {
            locked = readWriteLock.readLock().tryLock(timeout, TimeUnit.MILLISECONDS);
        } 
        catch (InterruptedException e) 
        {
            locked = false;
        }
        
        if(!locked)
        {
            throw new ConcurrentModificationException("Some other thread modifying this context at the moment");
        }
        
        return locked;
    }
    
    /**
     * Acquire read lock from ReadWriteLock. This will follow semantics of a {@linkplain ReadWriteLock}
     * 
     * @return
     */
    public boolean acquireReadLock() 
    {
        return acquireReadLock(tryDurationMilliseconds);
    }
    
    /**
     * Unlock read lock from ReadWriteLock. This will follow semantics of a {@linkplain ReadWriteLock}
     */
    public void releaseReadLock()
    {
        readWriteLock.readLock().unlock();
    }
    
    /**
     * Acquire write lock from ReadWriteLock. This will follow semantics of a {@linkplain ReadWriteLock}
     * 
     * @param timeout
     * @return
     */
    public boolean acquireWriteLock(int timeout) 
    {
        boolean locked = false;
        try 
        {
            locked = readWriteLock.writeLock().tryLock(timeout, TimeUnit.MILLISECONDS);
        } 
        catch (InterruptedException e) 
        {
            locked = false;
        }
        
        if(!locked)
        {
            throw new ConcurrentModificationException("Some other thread modifying this context at the moment");
        }
        
        return locked;
    }
    
    /**
     * Acquire write lock from ReadWriteLock. This will follow semantics of a {@linkplain ReadWriteLock}
     * 
     * @return
     */
    public boolean acquireWriteLock() 
    {
        return acquireWriteLock(tryDurationMilliseconds);
    }
    
    /**
     * Unlock write lock from ReadWriteLock. This will follow semantics of a {@linkplain ReadWriteLock}
     */
    public void releaseWriteLock()
    {
        readWriteLock.writeLock().unlock();
    }
}
