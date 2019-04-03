package fop.context.impl;

import fop.context.ConcurrentApplicationContext;

public abstract class AbstractConcurrentApplicationContext extends AbstractApplicationContext implements ConcurrentApplicationContext
{
    private final ContextConcurrencyManager contextConcurrencyManager;

    protected AbstractConcurrentApplicationContext(String name, Integer concurrentWriteTimeoutSeconds) 
    {
        super(name);
        this.contextConcurrencyManager = ContextConcurrencyManager.getInstance(concurrentWriteTimeoutSeconds);
    }
    
    @Override
    public void addIfNotPresent(String key, Object value) 
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
    
    @Override
    public void addIfNotPresent(String key, Object value, int timeout) 
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

    @Override
    public Object addWithOverwrite(String key, Object value) 
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

    @Override
    public Object addWithOverwrite(String key, Object value, int timeout) 
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

    @Override
    public void add(String key, Object value) 
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

    @Override
    public void add(String key, Object value, int timeout) 
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

    @Override
    public boolean exists(String key) 
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

    @Override
    public boolean exists(String key, int timeout) 
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

    @Override
    public Object fetch(String key) 
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

    @Override
    public Object fetch(String key, int timeout) 
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

    @Override
    public Object erase(String key) 
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
    
    @Override
    public Object erase(String key, int timeout) 
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

    protected ContextConcurrencyManager getContextConcurrencyManager()
    {
        return contextConcurrencyManager;
    }
}
