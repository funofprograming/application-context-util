package fop.context.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fop.context.ApplicationContext;
import fop.context.ApplicationContextKey;

/**
 * Abstract implementation of {@linkplain ApplicationContext}
 * 
 * This class implements almost all methods of {@linkplain ApplicationContext}
 * while leaving key validation task upto its extensions
 * 
 * @author Akshay Jain
 *
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    private final Map<ApplicationContextKey<?>, Object> store;
    private final String name; 
    
    /**
     * Initialize context with a name and initialize store as a {@linkplain HashMap}
     * 
     * @param name
     */
    protected AbstractApplicationContext(String name) 
    {
        this.name = name;
        this.store = new HashMap<>();
    }
    
    /**
     * Validate a give key. 
     * 
     * Extensions should throw {@linkplain RuntimeException} extensions if key not valid
     * 
     * @param key
     */
    public abstract void validateKey(ApplicationContextKey<?> key);
    
    /**
     * Name of the context
     * 
     * @return name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Underlying store
     * 
     * @return store
     */
    protected Map<ApplicationContextKey<?>, Object> getStore()
    {
        return store;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void addIfNotPresent(ApplicationContextKey<T> key, T value)
    {
        validateKey(key);
        
        getStore().putIfAbsent(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T addWithOverwrite(ApplicationContextKey<T> key, T value) 
    {
        validateKey(key);
        
        return key.getKeyType().cast(getStore().put(key, value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void add(ApplicationContextKey<T> key, T value)
    {
        validateKey(key);
        
        getStore().put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean exists(ApplicationContextKey<T> key) 
    {
        validateKey(key);
        
        return getStore().containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T fetch(ApplicationContextKey<T> key) 
    {
        validateKey(key);
        
        return key.getKeyType().cast(getStore().get(key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T erase(ApplicationContextKey<T> key)
    {
        validateKey(key);
        
        return key.getKeyType().cast(getStore().remove(key));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() 
    {
        getStore().clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<ApplicationContextKey<?>> keySet()
    {
        return Collections.unmodifiableSet(getStore().keySet());
    } 
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(ApplicationContext other) 
    {
        if(other == null)
            return false;
        
        if(!(other instanceof AbstractApplicationContext))
            return false;
        
        return this.name.equals(((AbstractApplicationContext)other).getName())
                && this.getStore().equals(((AbstractApplicationContext)other).getStore());
    }
}
