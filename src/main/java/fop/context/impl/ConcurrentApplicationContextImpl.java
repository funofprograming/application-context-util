package fop.context.impl;

import java.util.ConcurrentModificationException;
import java.util.Set;

import fop.context.ApplicationContext;
import fop.context.ApplicationContextKey;
import fop.context.ApplicationContextMergeStrategy;
import fop.context.ConcurrentApplicationContext;

/**
 * Thread safe extension of {@linkplain ApplicationContextImpl} by implementing {@linkplain ConcurrentApplicationContextImpl}
 * 
 * @author Akshay Jain
 *
 */
public class ConcurrentApplicationContextImpl extends ApplicationContextImpl implements ConcurrentApplicationContext
{
    private final ContextConcurrencyManager contextConcurrencyManager;

    /**
     * {@inheritDoc}
     */
    public ConcurrentApplicationContextImpl(String name) 
    {
        this(name, null);
    }
    
    /**
     * {@inheritDoc}
     */
    public ConcurrentApplicationContextImpl(String name, Set<ApplicationContextKey<?>> permittedKeys) 
    {
        super(name, permittedKeys);
        this.contextConcurrencyManager = ContextConcurrencyManager.getInstance();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void addIfNotPresent(ApplicationContextKey<T> key, T value)
    {
        if(!getContextConcurrencyManager().acquireWriteLock())
        {
            throw new ConcurrentModificationException("Some other thread modifying this context at the moment");
        }
        
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
    public <T> void addIfNotPresent(ApplicationContextKey<T> key, T value, Long timeout) 
    {
        if(!getContextConcurrencyManager().acquireWriteLock(timeout))
        {
            throw new ConcurrentModificationException("Some other thread modifying this context at the moment");
        }
        
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
        if(!getContextConcurrencyManager().acquireWriteLock())
        {
            throw new ConcurrentModificationException("Some other thread modifying this context at the moment");
        }
        
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
    public <T> T addWithOverwrite(ApplicationContextKey<T> key, T value, Long timeout) 
    {
        if(!getContextConcurrencyManager().acquireWriteLock(timeout))
        {
            throw new ConcurrentModificationException("Some other thread modifying this context at the moment");
        }
        
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
        if(!getContextConcurrencyManager().acquireWriteLock())
        {
            throw new ConcurrentModificationException("Some other thread modifying this context at the moment");
        }
        
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
    public <T> void add(ApplicationContextKey<T> key, T value, Long timeout) 
    {
        if(!getContextConcurrencyManager().acquireWriteLock(timeout))
        {
            throw new ConcurrentModificationException("Some other thread modifying this context at the moment");
        }
        
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
        if(!getContextConcurrencyManager().acquireReadLock())
        {
            throw new ConcurrentModificationException("Some other thread modifying this context at the moment");
        }
        
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
    public <T> boolean exists(ApplicationContextKey<T> key, Long timeout) 
    {
        if(!getContextConcurrencyManager().acquireReadLock(timeout))
        {
            throw new ConcurrentModificationException("Some other thread modifying this context at the moment");
        }
        
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
        if(!getContextConcurrencyManager().acquireReadLock())
        {
            throw new ConcurrentModificationException("Some other thread modifying this context at the moment");
        }
        
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
    public <T> T fetch(ApplicationContextKey<T> key, Long timeout) 
    {
        if(!getContextConcurrencyManager().acquireReadLock(timeout))
        {
            throw new ConcurrentModificationException("Some other thread modifying this context at the moment");
        }
        
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
        if(!getContextConcurrencyManager().acquireWriteLock())
        {
            throw new ConcurrentModificationException("Some other thread modifying this context at the moment");
        }
        
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
    public <T> T erase(ApplicationContextKey<T> key, Long timeout) 
    {
        if(!getContextConcurrencyManager().acquireWriteLock(timeout))
        {
            throw new ConcurrentModificationException("Some other thread modifying this context at the moment");
        }
        
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
        if(!getContextConcurrencyManager().acquireWriteLock())
        {
            throw new ConcurrentModificationException("Some other thread modifying this context at the moment");
        }
        
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
    public void clear(Long timeout) 
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
    
    @Override
    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy, Long timeout)
    {
        getContextConcurrencyManager().acquireWriteLock(timeout);
        
        try
        {
            super.merge(other, mergeStrategy);
        }
        finally
        {
            getContextConcurrencyManager().releaseWriteLock();
        }
    }

    @Override
    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy)
    {
        this.merge(other, mergeStrategy, null);
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
