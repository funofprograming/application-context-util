package fop.context.impl;

import fop.context.ApplicationContextKey;
import fop.context.ConcurrentApplicationContext;

/**
 * Thread safe extension of {@linkplain AbstractApplicationContext} by implementing {@linkplain ConcurrentApplicationContext}
 * 
 * @author Akshay Jain
 *
 */
public abstract class AbstractConcurrentApplicationContext extends AbstractApplicationContext implements ConcurrentApplicationContext
{
    private final ContextConcurrencyManager contextConcurrencyManager;

    /**
     * Initialize with a name and default concurrent write timeout in millis
     * 
     * @param name
     * @param concurrentWriteTimeoutMilliseconds
     */
    protected AbstractConcurrentApplicationContext(String name, Integer concurrentWriteTimeoutMilliseconds) 
    {
        super(name);
        this.contextConcurrencyManager = ContextConcurrencyManager.getInstance(concurrentWriteTimeoutMilliseconds);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void addIfNotPresent(ApplicationContextKey<T> key, T value)
    {
        getContextConcurrencyManager().acquireWriteLock();
        
        try
        {
            super.addIfNotPresent(key, value);
        } 
        finally
        {
            getContextConcurrencyManager().releaseWriteLock();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void addIfNotPresent(ApplicationContextKey<T> key, T value, int timeout) 
    {
        getContextConcurrencyManager().acquireWriteLock(timeout);
        
        try
        {
            super.addIfNotPresent(key, value);
        } 
        finally
        {
            getContextConcurrencyManager().releaseWriteLock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T addWithOverwrite(ApplicationContextKey<T> key, T value) 
    {
        getContextConcurrencyManager().acquireWriteLock();
        
        try
        {
            return super.addWithOverwrite(key, value);
        }
        finally
        {
            getContextConcurrencyManager().releaseWriteLock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T addWithOverwrite(ApplicationContextKey<T> key, T value, int timeout) 
    {
        getContextConcurrencyManager().acquireWriteLock(timeout);
        
        try
        {
            return super.addWithOverwrite(key, value);
        }
        finally
        {
            getContextConcurrencyManager().releaseWriteLock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void add(ApplicationContextKey<T> key, T value)
    {
        getContextConcurrencyManager().acquireWriteLock();
        
        try
        {
            super.add(key, value);
        }
        finally
        {
            getContextConcurrencyManager().releaseWriteLock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void add(ApplicationContextKey<T> key, T value, int timeout) 
    {
        getContextConcurrencyManager().acquireWriteLock(timeout);
        
        try
        {
            super.add(key, value);
        }
        finally
        {
            getContextConcurrencyManager().releaseWriteLock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean exists(ApplicationContextKey<T> key) 
    {
        getContextConcurrencyManager().acquireReadLock();
        
        try
        {
            return super.exists(key);
        }
        finally
        {
            getContextConcurrencyManager().releaseReadLock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean exists(ApplicationContextKey<T> key, int timeout) 
    {
        getContextConcurrencyManager().acquireReadLock(timeout);
        
        try
        {
            return super.exists(key);
        }
        finally
        {
            getContextConcurrencyManager().releaseReadLock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T fetch(ApplicationContextKey<T> key) 
    {
        getContextConcurrencyManager().acquireReadLock();
        
        try
        {
            return super.fetch(key);
        }
        finally
        {
            getContextConcurrencyManager().releaseReadLock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T fetch(ApplicationContextKey<T> key, int timeout) 
    {
        getContextConcurrencyManager().acquireReadLock(timeout);
        
        try
        {
            return super.fetch(key);
        }
        finally
        {
            getContextConcurrencyManager().releaseReadLock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T erase(ApplicationContextKey<T> key) 
    {
        getContextConcurrencyManager().acquireWriteLock();
        
        try
        {
            return super.erase(key);
        }
        finally
        {
            getContextConcurrencyManager().releaseWriteLock();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T erase(ApplicationContextKey<T> key, int timeout) 
    {
        getContextConcurrencyManager().acquireWriteLock(timeout);
        
        try
        {
            return super.erase(key);
        }
        finally
        {
            getContextConcurrencyManager().releaseWriteLock();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() 
    {
        getContextConcurrencyManager().acquireWriteLock();
        
        try
        {
            super.clear();
        }
        finally
        {
            getContextConcurrencyManager().releaseWriteLock();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void clear(int timeout) 
    {
        getContextConcurrencyManager().acquireWriteLock(timeout);
        
        try
        {
            super.clear();
        }
        finally
        {
            getContextConcurrencyManager().releaseWriteLock();
        }
    }

    /**
     * 
     * @return concurrency manager used by this context
     */
    protected ContextConcurrencyManager getContextConcurrencyManager()
    {
        return contextConcurrencyManager;
    }
}
