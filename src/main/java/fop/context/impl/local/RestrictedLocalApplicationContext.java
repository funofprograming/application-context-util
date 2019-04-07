package fop.context.impl.local;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import fop.context.ApplicationContext;
import fop.context.ApplicationContextKey;
import fop.context.ApplicationContextMergeStrategy;
import fop.context.LocalApplicationContext;
import fop.context.RestrictedApplicationContext;
import fop.context.impl.AbstractApplicationContext;
import fop.context.impl.InvalidContextOperation;
import fop.context.impl.InvalidKeyException;

/**
 * This is an {@linkplain ApplicationContext} extension that implements {@linkplain LocalApplicationContext} and {@linkplain RestrictedApplicationContext}
 * 
 * @author Akshay Jain
 *
 */
public class RestrictedLocalApplicationContext extends AbstractApplicationContext implements LocalApplicationContext, RestrictedApplicationContext
{
    private final Set<ApplicationContextKey<?>> keys;
    
    /**
     * Initialize with a name and set of permitted keys
     * 
     * @param name
     * @param keys
     */
    RestrictedLocalApplicationContext(String name, Set<ApplicationContextKey<?>> keys) 
    {
        super(name);
        
        if(keys != null && !keys.isEmpty())
        {
            Set<ApplicationContextKey<?>> keysTemp = new HashSet<>(keys);
            this.keys = Collections.unmodifiableSet(keysTemp);
        }
        else
        {
            throw new InvalidKeyException("RestrictedLocalApplicationContext can be initialized with a set of keys of string type");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateKey(ApplicationContextKey<?> key)
    {
        if(!getPermittedKeys().contains(key))
        {
            throw new InvalidKeyException("Invalid key:"+key+". Valid keys for this context are: "+keys);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<ApplicationContextKey<?>> getPermittedKeys() 
    {
        return this.keys;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isKeyValid(ApplicationContextKey<?> key)
    {
        return getPermittedKeys().contains(key);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy)
    {
        throw new InvalidContextOperation("merge not supported for local context");
    }
}
