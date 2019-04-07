package fop.context.impl.instance;

import java.util.Set;

import fop.context.ApplicationContext;
import fop.context.ApplicationContextKey;
import fop.context.ApplicationContextMergeStrategy;
import fop.context.ConcurrentApplicationContext;
import fop.context.InstanceApplicationContext;
import fop.context.UnrestrictedApplicationContext;
import fop.context.impl.AbstractConcurrentApplicationContext;

/**
 * This is a {@linkplain ConcurrentApplicationContext} extension that implements {@linkplain InstanceApplicationContext} and {@linkplain UnrestrictedApplicationContext}
 * 
 * @author Akshay Jain
 *
 */
public class UnrestrictedInstanceApplicationContext extends AbstractConcurrentApplicationContext implements InstanceApplicationContext, UnrestrictedApplicationContext
{
    /**
     * Initialize with name and default concurrent write timeout millis
     * 
     * @param name
     * @param concurrentWriteTimeoutMilliseconds
     */
    UnrestrictedInstanceApplicationContext(String name, Integer concurrentWriteTimeoutMilliseconds)
    {
        super(name, concurrentWriteTimeoutMilliseconds);
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
     * actual merge
     */
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
    
    /**
     * no validation done for {@linkplain UnrestrictedApplicationContext}
     */
    @Override
    public void validateKey(ApplicationContextKey<?> key)
    {
        //unrestricted
    }

}
