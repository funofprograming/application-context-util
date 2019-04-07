package fop.context.impl.global;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import fop.context.ApplicationContext;
import fop.context.ApplicationContextKey;
import fop.context.ApplicationContextMergeStrategy;
import fop.context.ConcurrentApplicationContext;
import fop.context.GlobalApplicationContext;
import fop.context.RestrictedApplicationContext;
import fop.context.impl.AbstractConcurrentApplicationContext;
import fop.context.impl.InvalidKeyException;

/**
 * This is a {@linkplain ConcurrentApplicationContext} extension that implements {@linkplain GlobalApplicationContext} and {@linkplain RestrictedApplicationContext}
 * 
 * @author Akshay Jain
 *
 */
public class RestrictedGlobalApplicationContext extends AbstractConcurrentApplicationContext implements GlobalApplicationContext, RestrictedApplicationContext  
{
    private final Set<ApplicationContextKey<?>> keys;
    
    /**
     * Initialize with name, default concurrent write timeout millis and set of permitted keys
     * 
     * @param name
     * @param concurrentWriteTimeoutMilliseconds
     * @param keys
     */
    RestrictedGlobalApplicationContext(String name, Integer concurrentWriteTimeoutMilliseconds, Set<ApplicationContextKey<?>> keys) 
    {
        super(name, concurrentWriteTimeoutMilliseconds);
        
        if(keys != null && !keys.isEmpty())
        {
            Set<ApplicationContextKey<?>> keysTemp = new HashSet<>(keys);
            this.keys = Collections.unmodifiableSet(keysTemp);
        }
        else
        {
            throw new InvalidKeyException("RestrictedGlobalApplicationContext can be initialized with a set of keys of string type");
        }
    }
    
    /**
     * {@inheritDoc} 
     */
    @Override
    public void validateKey(ApplicationContextKey<?> key)
    {
        if(!getPermittedKeys().contains(key))
        {
            throw new InvalidKeyException("Invalid key:"+key+". Valid keys for this context are: "+keys);
        }
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public Set<ApplicationContextKey<?>> getPermittedKeys() 
    {
        return this.keys;
    }
    
    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean isKeyValid(ApplicationContextKey<?> key)
    {
        return getPermittedKeys().contains(key);
    }
    
    /**
     * {@inheritDoc} 
     */
    @Override
    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy)
    {
        getContextConcurrencyManager().acquireWriteLock();
        
        try
        {
            doMerge(other, mergeStrategy);
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
    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy, int timeout)
    {
        getContextConcurrencyManager().acquireWriteLock(timeout);
        
        try
        {
            doMerge(other, mergeStrategy);
        }
        finally
        {
            getContextConcurrencyManager().releaseWriteLock();
        }
        
    }
    
    /**
     * actual merge method
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void doMerge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy)
    {
        Set<ApplicationContextKey<?>> keysOther = other.keySet();
        
        for(ApplicationContextKey keyOther: keysOther)
        {
            if(!isKeyValid(keyOther))
            {
                continue;
            }
            
            Object newValue = other.fetch(keyOther);
            
            if(this.exists(keyOther)) 
            {
                Object value = mergeStrategy.merge(keyOther, this.fetch(keyOther), newValue);
                addWithOverwrite(keyOther, value);
            }
            else
            {
                addIfNotPresent(keyOther, newValue);
            }
        }
    }
}
