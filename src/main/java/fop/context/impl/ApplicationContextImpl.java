package fop.context.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import fop.context.ApplicationContext;
import fop.context.ApplicationContextKey;
import fop.context.ApplicationContextMergeStrategy;

/**
 * Abstract implementation of {@linkplain ApplicationContext}
 * 
 * This class implements almost all methods of {@linkplain ApplicationContext}
 * while leaving key validation task upto its extensions
 * 
 * @author Akshay Jain
 *
 */
public class ApplicationContextImpl implements ApplicationContext {

    private final Set<ApplicationContextKey<?>> permittedKeys;
    private final Map<ApplicationContextKey<?>, Object> store;
    private final String name; 
    
    /**
     * Initialize context with a name and initialize store as a {@linkplain HashMap}
     * 
     * @param name
     */
    public ApplicationContextImpl(String name) 
    {
        this(name, null);
    }
    
    /**
     * Initialize context with a name, set of permittedKeys and initialize store as a {@linkplain HashMap} 
     * 
     * @param name
     */
    public ApplicationContextImpl(String name, Set<ApplicationContextKey<?>> permittedKeys) 
    {
        this.name = name;
        this.permittedKeys = Objects.nonNull(permittedKeys) ? Collections.unmodifiableSet(permittedKeys) : Collections.emptySet();
        this.store = new HashMap<>();
    }
    
    /**
     * Validate a give key if this context was initialized with a non-empty set of permittedKeys
     * 
     * @param key
     */
    protected void validateKey(ApplicationContextKey<?> key)
    {
        if(Objects.nonNull(getPermittedKeys()) && !getPermittedKeys().isEmpty() && !getPermittedKeys().contains(key)) 
        {
            throw new InvalidKeyException("Invalid key:"+key+". Valid keys for this context are: "+getPermittedKeys());
        }
    }

    /**
     * {@inheritDoc}
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
    public Set<ApplicationContextKey<?>> getPermittedKeys()
    {
        return this.permittedKeys;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isKeyValid(ApplicationContextKey<?> key)
    {
        try 
        {
            validateKey(key);
            return true;
        } 
        catch(Throwable t)
        {
            return false;
        }
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
    @SuppressWarnings("unchecked")
    @Override
    public <T> T addWithOverwrite(ApplicationContextKey<T> key, T value) 
    {
        validateKey(key);
        
        return (T)getStore().put(key, value);
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
    @SuppressWarnings("unchecked")
    @Override
    public <T> T fetch(ApplicationContextKey<T> key) 
    {
        validateKey(key);
        
        return (T)getStore().get(key);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T erase(ApplicationContextKey<T> key)
    {
        validateKey(key);
        
        return (T)getStore().remove(key);
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy)
    {
        Set<ApplicationContextKey<?>> keysOther = other.keySet();
        
        for(ApplicationContextKey keyOther: keysOther)
        {
            if(!isKeyValid(keyOther))
            {
                continue;
            }
            
            Object newValue = other.fetch(keyOther);
            
            if(this.exists(keyOther)) 
            {
                Object value = mergeStrategy.merge(keyOther, this.fetch(keyOther), newValue);
                addWithOverwrite(keyOther, value);
            }
            else
            {
                addIfNotPresent(keyOther, newValue);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(ApplicationContext other) 
    {
        if(other == null)
            return false;
        
        if(!(other instanceof ApplicationContextImpl))
            return false;
        
        return this.name.equals(other.getName())
                && this.permittedKeys.equals(other.getPermittedKeys())
                && this.getStore().equals(((ApplicationContextImpl)other).getStore());
    }
}
