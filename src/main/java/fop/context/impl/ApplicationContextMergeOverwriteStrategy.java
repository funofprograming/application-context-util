package fop.context.impl;

import java.util.Set;

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
    public Object merge(String key, Object oldValue, Object newValue) 
    {
        boolean keySetAvailable = super.keySetAvailable();
        Object mergeValue = null;
        
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
