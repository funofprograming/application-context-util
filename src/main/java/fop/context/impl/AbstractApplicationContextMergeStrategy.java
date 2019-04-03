package fop.context.impl;

import java.util.Objects;
import java.util.Set;

import fop.context.ApplicationContextMergeStrategy;

public abstract class AbstractApplicationContextMergeStrategy implements ApplicationContextMergeStrategy {
    
    
    protected final Set<String> keys;
    
    protected AbstractApplicationContextMergeStrategy(Set<String> keys)
    {
        this.keys = keys;
    }

    protected boolean keySetAvailable()
    {
        return Objects.nonNull(keys);
    }
    
    protected boolean keyAvailable(String key)
    {
        return keySetAvailable()?keys.contains(key):false;
    }
    
}
