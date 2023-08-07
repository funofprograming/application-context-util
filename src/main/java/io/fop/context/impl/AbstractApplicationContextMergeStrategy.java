package io.fop.context.impl;

import java.util.Objects;
import java.util.Set;

import io.fop.context.ApplicationContextKey;
import io.fop.context.ApplicationContextMergeStrategy;

/**
 * Abstract implementation of {@linkplain ApplicationContextMergeStrategy}
 * 
 * @author Akshay Jain
 *
 */
public abstract class AbstractApplicationContextMergeStrategy implements ApplicationContextMergeStrategy {
    
    
    protected final Set<ApplicationContextKey<?>> keys;
    
    /**
     * Initialize with a set of keys for whom this strategy is to be applied. If null set passed then strategy is applicable for all keys
     * 
     * @param keys
     */
    protected AbstractApplicationContextMergeStrategy(Set<ApplicationContextKey<?>> keys)
    {
        this.keys = keys;
    }

    protected boolean keySetAvailable()
    {
        return Objects.nonNull(keys);
    }
    
    protected boolean keyAvailable(ApplicationContextKey<?> key)
    {
        return keySetAvailable()?keys.contains(key):false;
    }
    
}
