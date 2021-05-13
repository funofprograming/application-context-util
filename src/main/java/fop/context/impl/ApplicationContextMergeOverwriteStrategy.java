package fop.context.impl;

import java.util.Set;

import fop.context.ApplicationContextKey;
import fop.context.ApplicationContextMergeStrategy;

/**
 * {@linkplain ApplicationContextMergeStrategy} implementation which overwrites the oldValue. 
 * 
 * If keyset present then overwrites the oldValue ONLY for those keys and use oldValue for all other keys.
 * 
 * If keyset not present then overwrites the oldValue for all keys
 * 
 * @author Akshay Jain
 *
 */
public class ApplicationContextMergeOverwriteStrategy extends AbstractApplicationContextMergeStrategy {

    public ApplicationContextMergeOverwriteStrategy() 
    {
        super(null);
    }
    
    public ApplicationContextMergeOverwriteStrategy(Set<ApplicationContextKey<?>> keys) 
    {
        super(keys);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T merge(ApplicationContextKey<T> key, T oldValue, T newValue) 
    {
        boolean keySetAvailable = super.keySetAvailable();
        T mergeValue = null;
        
        if(!keySetAvailable)
        {
            mergeValue = newValue;
        }
        else if(super.keyAvailable(key))
        {
            mergeValue = newValue;
        }
        else
        {
            mergeValue = oldValue;
        }
        
        return mergeValue;
    }

}
