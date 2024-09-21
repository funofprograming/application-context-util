package io.github.funofprograming.context.impl;

import java.util.Set;

import io.github.funofprograming.context.Key;
import io.github.funofprograming.context.ApplicationContextMergeStrategy;

/**
 * {@linkplain ApplicationContextMergeStrategy} implementation which ignores the newValue. 
 * 
 * If keyset present then ignore newValue ONLY for those keys and use newValue for all other keys.
 * 
 * If keyset not present then ignore newValue for all keys
 * 
 * @author Akshay Jain
 *
 */
public class ApplicationContextMergeIgnoreStrategy extends AbstractApplicationContextMergeStrategy {

    public ApplicationContextMergeIgnoreStrategy()
    {
        super(null);
    }
    
    public ApplicationContextMergeIgnoreStrategy(Set<Key<?>> keys)
    {
        super(keys);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T merge(Key<T> key, T oldValue, T newValue)
    {
        boolean keySetAvailable = super.keySetAvailable();
        T mergeValue = null;
        
        if(!keySetAvailable)
        {
            mergeValue = oldValue;
        }
        else if(super.keyAvailable(key))
        {
            mergeValue = oldValue;
        }
        else
        {
            mergeValue = newValue;
        }
        
        return mergeValue;
    }

}
