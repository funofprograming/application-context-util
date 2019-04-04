package fop.context.impl.global;

import java.util.Set;

import fop.context.ApplicationContext;
import fop.context.ApplicationContextKey;
import fop.context.ApplicationContextMergeStrategy;
import fop.context.GlobalApplicationContext;
import fop.context.UnrestrictedApplicationContext;
import fop.context.impl.AbstractConcurrentApplicationContext;

public class UnrestrictedGlobalApplicationContext extends AbstractConcurrentApplicationContext implements GlobalApplicationContext, UnrestrictedApplicationContext
{
    public UnrestrictedGlobalApplicationContext(String name, Integer concurrentWriteTimeoutSeconds)
    {
        super(name, concurrentWriteTimeoutSeconds);
    }
    
    @Override
    public void validateKey(ApplicationContextKey<?> key)
    {
        //unrestricted
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
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void doMerge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy)
    {
        Set<ApplicationContextKey<?>> keysOther = other.keySet();
        
        for(ApplicationContextKey keyOther: keysOther)
        {
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
