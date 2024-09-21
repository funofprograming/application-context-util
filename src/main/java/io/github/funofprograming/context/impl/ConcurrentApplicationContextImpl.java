package io.github.funofprograming.context.impl;

import java.util.ConcurrentModificationException;
import java.util.Set;

import io.github.funofprograming.context.ApplicationContext;
import io.github.funofprograming.context.ApplicationContextMergeStrategy;
import io.github.funofprograming.context.ConcurrentApplicationContext;
import io.github.funofprograming.context.Key;
import io.github.funofprograming.context.impl.ContextConcurrencyManager.AcquiredLock;
import io.github.funofprograming.context.impl.ContextConcurrencyManager.LockAutoCloseable;
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
        addIfNotPresent(key, value, null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void addIfNotPresent(Key<T> key, T value, Long timeout) 
    {
	try(AcquiredLock lock = setupWriteAccess(timeout))
        {
            super.addIfNotPresent(key, value);
        } 
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T addWithOverwrite(Key<T> key, T value) 
    {
        return addWithOverwrite(key, value, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T addWithOverwrite(Key<T> key, T value, Long timeout) 
    {
        try(AcquiredLock lock = setupWriteAccess(timeout))
        {
            return super.addWithOverwrite(key, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void add(Key<T> key, T value)
    {
        add(key, value, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void add(Key<T> key, T value, Long timeout) 
    {
	try(AcquiredLock lock = setupWriteAccess(timeout))
        {
            super.add(key, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean exists(Key<T> key) 
    {
        return exists(key, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean exists(Key<T> key, Long timeout) 
    {
	try(AcquiredLock lock = setupReadAccess(timeout))
        {
            return super.exists(key);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T fetch(Key<T> key) 
    {
        return fetch(key, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T fetch(Key<T> key, Long timeout) 
    {
	try(AcquiredLock lock = setupReadAccess(timeout))
        {
            return super.fetch(key);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T erase(Key<T> key) 
    {
        return erase(key, null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T erase(Key<T> key, Long timeout) 
    {
	try(AcquiredLock lock = setupWriteAccess(timeout))
        {
            return super.erase(key);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() 
    {
        clear(null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void clear(Long timeout) 
    {
	try(AcquiredLock lock = setupWriteAccess(timeout))
        {
            super.clear();
        }
    }

    @Override
    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy)
    {
        this.merge(other, mergeStrategy, null);
    }
    
    @Override
    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy, Long timeout)
    {
	try(AcquiredLock lock = setupWriteAccess(timeout))
        {
            super.merge(other, mergeStrategy);
        }
    }
    
    private LockAutoCloseable setupWriteAccess(Long timeout)
    {
	LockAutoCloseable writeLockCloseable = getContextConcurrencyManager().acquireWriteLockCloseable(timeout);
	verifyLockAutoCloseable(writeLockCloseable);
	return writeLockCloseable;
    }
    
    private LockAutoCloseable setupReadAccess(Long timeout)
    {
	LockAutoCloseable readLockCloseable = getContextConcurrencyManager().acquireReadLockCloseable(timeout);
	verifyLockAutoCloseable(readLockCloseable);
	return readLockCloseable;
    }
    
    private void verifyLockAutoCloseable(LockAutoCloseable lockAutoCloseable)
    {
	if(!lockAutoCloseable.isLocked())
	{
	    throw new ConcurrentModificationException("Some other thread modifying this context at the moment");
	}
    }
}
