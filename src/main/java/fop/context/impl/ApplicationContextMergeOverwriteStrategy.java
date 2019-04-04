package fop.context.impl;

import java.util.Set;

import fop.context.ApplicationContextKey;

public class ApplicationContextMergeOverwriteStrategy extends AbstractApplicationContextMergeStrategy {

    public ApplicationContextMergeOverwriteStrategy() 
    {
        super(null);
    }
    
    public ApplicationContextMergeOverwriteStrategy(Set<String> keys) 
    {
        super(keys);
    }

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
