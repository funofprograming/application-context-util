package fop.context.impl.local;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import fop.context.ApplicationContext;
import fop.context.ApplicationContextMergeStrategy;
import fop.context.LocalApplicationContext;
import fop.context.RestrictedApplicationContext;
import fop.context.impl.AbstractApplicationContext;
import fop.context.impl.InvalidContextOperation;
import fop.context.impl.InvalidKeyException;

public class RestrictedLocalApplicationContext extends AbstractApplicationContext implements LocalApplicationContext, RestrictedApplicationContext
{
    private final Set<String> keys;
    
    public RestrictedLocalApplicationContext(String name, Set<String> keys) 
    {
        super(name);
        
        if(keys != null && !keys.isEmpty())
        {
            Set<String> keysTemp = new HashSet<>(keys);
            this.keys = Collections.unmodifiableSet(keysTemp);
        }
        else
        {
            throw new InvalidKeyException("RestrictedLocalApplicationContext can be initialized with a set of keys of string type");
        }
    }

    @Override
    public void validateKey(String key)
    {
        if(!getPermittedKeys().contains(key))
        {
            throw new InvalidKeyException("Invalid key:"+key+". Valid keys for this context are: "+keys);
        }
    }

    @Override
    public Set<String> getPermittedKeys() 
    {
        return this.keys;
    }
    
    @Override
    public boolean isKeyValid(String key)
    {
        return getPermittedKeys().contains(key);
    }
    
    @Override
    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy)
    {
        throw new InvalidContextOperation("merge not supported for local context");
    }
}
