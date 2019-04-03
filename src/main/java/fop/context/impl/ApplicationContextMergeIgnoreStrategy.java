package fop.context.impl;

import java.util.Set;

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
    public Object merge(String key, Object oldValue, Object newValue) 
    {
        boolean keySetAvailable = super.keySetAvailable();
        Object mergeValue = null;
        
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
