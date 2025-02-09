package io.github.funofprograming.context.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import io.github.funofprograming.context.ApplicationContext;
import io.github.funofprograming.context.ApplicationContextMergeStrategy;
import io.github.funofprograming.context.Key;
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
@ToString(of = {"name"})
public class ApplicationContextImpl implements ApplicationContext {

    @Getter
    private final String name;
    
    @Getter
    private final Set<Key<?>> permittedKeys;
    
    @Getter(AccessLevel.PROTECTED)
    private final Map<Key<?>, Optional<Object>> store; 
    
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
    public ApplicationContextImpl(String name, Set<Key<?>> permittedKeys) 
    {
        this.name = name;
        this.permittedKeys = Optional.ofNullable(permittedKeys).map(p->Collections.unmodifiableSet(p)).orElse(Collections.emptySet());
        this.store = new LinkedHashMap<>();
    }
    
    /**
     * Validate a give key if this context was initialized with a non-empty set of permittedKeys
     * 
     * @param key
     */
    protected void validateKey(Key<?> key)
    {
        if(Optional.ofNullable(getPermittedKeys()).map(p->!p.isEmpty() && !p.contains(key)).orElse(Boolean.FALSE))
        {
            throw new InvalidKeyException("Invalid key:"+key+". Valid keys for this context are: "+getPermittedKeys());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isKeyValid(Key<?> key)
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
    public <T> void addIfNotPresent(Key<T> key, T value)
    {
        validateKey(key);
        
        getStore().putIfAbsent(key, Optional.ofNullable(value));
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T addWithOverwrite(Key<T> key, T value) 
    {
        validateKey(key);
        
        return (T)getStore().put(key, Optional.ofNullable(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void add(Key<T> key, T value)
    {
        validateKey(key);
        
        getStore().put(key, Optional.ofNullable(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean exists(Key<T> key) 
    {
        validateKey(key);
        
        return getStore().containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T fetch(Key<T> key) 
    {
        validateKey(key);
        
        return Optional.ofNullable(getStore().get(key)).flatMap(o->o.map(ob->(T)ob)).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T erase(Key<T> key)
    {
        validateKey(key);
        
        return Optional.ofNullable(getStore().remove(key)).flatMap(o->o.map(ob->(T)ob)).orElse(null);
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
    public Set<Key<?>> keySet()
    {
        return Collections.unmodifiableSet(getStore().keySet());
    } 

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy)
    {
        Set<Key<?>> keysOther = other.keySet();
        
        for(Key keyOther: keysOther)
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
