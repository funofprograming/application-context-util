package fop.context.impl;

import java.util.ConcurrentModificationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ContextConcurrencyManager
{
    private static final Integer DEFAULT_TRY_DURATION_MILLISECONDS = 0;
    
    private final Integer tryDurationMilliseconds;
    private final ReadWriteLock readWriteLock;
    
    public static ContextConcurrencyManager getInstance(Integer tryDurationMilliseconds)
    {
        return new ContextConcurrencyManager(tryDurationMilliseconds);
    }
    
    private ContextConcurrencyManager(Integer tryDurationMilliseconds)
    {
        this.tryDurationMilliseconds = tryDurationMilliseconds != null ? tryDurationMilliseconds : DEFAULT_TRY_DURATION_MILLISECONDS;
        this.readWriteLock = new ReentrantReadWriteLock();
    }
    
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
    
    public boolean acquireReadLock() 
    {
        return acquireReadLock(tryDurationMilliseconds);
    }
    
    public void releaseReadLock()
    {
        readWriteLock.readLock().unlock();
    }
    
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
    
    public boolean acquireWriteLock() 
    {
        return acquireWriteLock(tryDurationMilliseconds);
    }
    
    public void releaseWriteLock()
    {
        readWriteLock.writeLock().unlock();
    }
}
