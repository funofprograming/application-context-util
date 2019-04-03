package fop.context.impl.global;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import fop.context.ApplicationContext;
import fop.context.ApplicationContextMergeStrategy;
import fop.context.GlobalApplicationContext;
import fop.context.RestrictedApplicationContext;
import fop.context.impl.AbstractConcurrentApplicationContext;
import fop.context.impl.InvalidKeyException;

public class RestrictedGlobalApplicationContext extends AbstractConcurrentApplicationContext implements GlobalApplicationContext, RestrictedApplicationContext  
{
    private final Set<String> keys;
    
    public RestrictedGlobalApplicationContext(String name, Integer concurrentWriteTimeoutSeconds, Set<String> keys) 
    {
        super(name, concurrentWriteTimeoutSeconds);
        
        if(keys != null && !keys.isEmpty())
        {
            Set<String> keysTemp = new HashSet<>(keys);
            this.keys = Collections.unmodifiableSet(keysTemp);
        }
        else
        {
            throw new InvalidKeyException("RestrictedGlobalApplicationContext can be initialized with a set of keys of string type");
        }
    }
    
    
    @Override
    public void validateKey(String key)
    {
        if(!getPermittedKeys().contains(key))
        {
            throw new InvalidKeyException("Invalid key:"+key+". Valid keys for this context are: "+keys);
        }
    }

    @Override
    public Set<String> getPermittedKeys() 
    {
        return this.keys;
    }
    
    @Override
    public boolean isKeyValid(String key)
    {
        return getPermittedKeys().contains(key);
    }
    
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
    
    private void doMerge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy)
    {
        Set<String> keysOther = other.keySet();
        
        for(String keyOther: keysOther)
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
