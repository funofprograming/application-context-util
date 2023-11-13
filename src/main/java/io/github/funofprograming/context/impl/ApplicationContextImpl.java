package io.github.funofprograming.context.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import io.github.funofprograming.context.ApplicationContext;
import io.github.funofprograming.context.ApplicationContextKey;
import io.github.funofprograming.context.ApplicationContextMergeStrategy;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Plain vanilla implementation of {@linkplain ApplicationContext}
 * 
 * 
 * @author Akshay Jain
 *
 */
@EqualsAndHashCode
@ToString
public class ApplicationContextImpl implements ApplicationContext {

    @Getter
    private final String name;
    
    @Getter
    private final Set<ApplicationContextKey<?>> permittedKeys;
    
    @Getter(AccessLevel.PROTECTED)
    private final Map<ApplicationContextKey<?>, Object> store; 
    
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
        this.permittedKeys = Optional.ofNullable(permittedKeys).map(p->Collections.unmodifiableSet(p)).orElse(Collections.emptySet());
        this.store = new HashMap<>();
    }
    
    /**
     * Validate a give key if this context was initialized with a non-empty set of permittedKeys
     * 
     * @param key
     */
    protected void validateKey(ApplicationContextKey<?> key)
    {
        if(Optional.ofNullable(getPermittedKeys()).map(p->!p.isEmpty() && !p.contains(key)).orElse(Boolean.FALSE))
        {
            throw new InvalidKeyException("Invalid key:"+key+". Valid keys for this context are: "+getPermittedKeys());
        }
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
}
