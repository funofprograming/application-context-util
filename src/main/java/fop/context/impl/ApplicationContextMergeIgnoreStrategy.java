package fop.context.impl;

import java.util.Set;

import fop.context.ApplicationContextKey;

public class ApplicationContextMergeIgnoreStrategy extends AbstractApplicationContextMergeStrategy {

    public ApplicationContextMergeIgnoreStrategy()
    {
        super(null);
    }
    
    public ApplicationContextMergeIgnoreStrategy(Set<String> keys)
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
