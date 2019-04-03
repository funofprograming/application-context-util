package fop.context.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;

import fop.context.ApplicationContext;
import fop.context.ApplicationContextKey;

public abstract class AbstractApplicationContext implements ApplicationContext {

    private final Map<ApplicationContextKey<?>, Object> store;
    private final String name; 
    
    protected AbstractApplicationContext(String name) 
    {
        this.name = name;
        this.store = new HashMap<>();
    }
    
    public abstract void validateKey(ApplicationContextKey<?> key);
    
    public String getName()
    {
        return name;
    }
    
    protected Map<ApplicationContextKey<?>, Object> getStore()
    {
        return store;
    }

    @Override
    public <T> void addIfNotPresent(ApplicationContextKey<T> key, T value)
    {
        validateKey(key);
        
        getStore().putIfAbsent(key, value);
    }

    @Override
    public <T> T addWithOverwrite(ApplicationContextKey<T> key, T value) 
    {
        validateKey(key);
        
        return key.getKeyType().cast(getStore().put(key, value));
    }

    @Override
    public <T> void add(ApplicationContextKey<T> key, T value)
    {
        validateKey(key);
        
        getStore().put(key, value);
    }

    @Override
    public <T> boolean exists(ApplicationContextKey<T> key) 
    {
        validateKey(key);
        
        return getStore().containsKey(key);
    }

    @Override
    public <T> T fetch(ApplicationContextKey<T> key) 
    {
        validateKey(key);
        
        return key.getKeyType().cast(getStore().get(key));
    }

    @Override
    public <T> T erase(ApplicationContextKey<T> key)
    {
        validateKey(key);
        
        return key.getKeyType().cast(getStore().remove(key));
    }
    
    @Override
    public void clear() 
    {
        getStore().clear();
    }

    @Override
    public Set<ApplicationContextKey<?>> keySet()
    {
        return Collections.unmodifiableSet(getStore().keySet());
    } 
    
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
