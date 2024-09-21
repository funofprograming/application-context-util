package io.github.funofprograming.context.impl;

import java.util.ConcurrentModificationException;
import java.util.Set;

import io.github.funofprograming.context.ApplicationContext;
import io.github.funofprograming.context.Key;
import io.github.funofprograming.context.ApplicationContextMergeStrategy;
import io.github.funofprograming.context.ConcurrentApplicationContext;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Thread safe extension of {@linkplain ApplicationContextImpl} by implementing {@linkplain ConcurrentApplicationContextImpl}
 * 
 * @author Akshay Jain
 *
 */
public class ConcurrentApplicationContextImpl extends ApplicationContextImpl implements ConcurrentApplicationContext
{
    @Getter(AccessLevel.PROTECTED)
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
    public ConcurrentApplicationContextImpl(String name, Set<Key<?>> permittedKeys) 
    {
        super(name, permittedKeys);
        this.contextConcurrencyManager = ContextConcurrencyManager.getInstance();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void addIfNotPresent(Key<T> key, T value)
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
    public <T> void addIfNotPresent(Key<T> key, T value, Long timeout) 
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
    public <T> T addWithOverwrite(Key<T> key, T value) 
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
    public <T> T addWithOverwrite(Key<T> key, T value, Long timeout) 
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
    public <T> void add(Key<T> key, T value)
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
    public <T> void add(Key<T> key, T value, Long timeout) 
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
    public <T> boolean exists(Key<T> key) 
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
    public <T> boolean exists(Key<T> key, Long timeout) 
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
    public <T> T fetch(Key<T> key) 
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
    public <T> T fetch(Key<T> key, Long timeout) 
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
    public <T> T erase(Key<T> key) 
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
    public <T> T erase(Key<T> key, Long timeout) 
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
        if(!getContextConcurrencyManager().acquireWriteLock(timeout))
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
    
    @Override
    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy, Long timeout)
    {
        if(!getContextConcurrencyManager().acquireWriteLock(timeout))
        {
            throw new ConcurrentModificationException("Some other thread modifying this context at the moment");
        }
        
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
}
