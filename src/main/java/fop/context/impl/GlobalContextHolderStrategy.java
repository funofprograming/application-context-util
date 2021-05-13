package fop.context.impl;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import fop.context.ApplicationContext;
import fop.context.ApplicationContextHolderStrategy;
import fop.context.ApplicationContextKey;
import fop.context.ConcurrentApplicationContext;

public final class GlobalContextHolderStrategy implements ApplicationContextHolderStrategy
{
    private static final ConcurrentMap<String, ConcurrentApplicationContext> GLOBAL_CONTEXT_STORE = new ConcurrentHashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationContext getContext(String name)
    {
        if(!GLOBAL_CONTEXT_STORE.containsKey(name)) 
        {
            setContext(new ConcurrentApplicationContextImpl(name));
        }
        
        return GLOBAL_CONTEXT_STORE.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationContext getContext(String name, Set<ApplicationContextKey<?>> permittedKeys)
    {
        if(!GLOBAL_CONTEXT_STORE.containsKey(name)) 
        {
            setContext(new ConcurrentApplicationContextImpl(name, permittedKeys));
        }
        else 
        {
            Set<ApplicationContextKey<?>> permittedKeysExisting = GLOBAL_CONTEXT_STORE.get(name).getPermittedKeys();
            
            if(
                    (Objects.nonNull(permittedKeysExisting) && Objects.nonNull(permittedKeys) && !permittedKeys.equals(permittedKeysExisting))
                    ||
                    (permittedKeysExisting.isEmpty() && Objects.nonNull(permittedKeys) && !permittedKeys.isEmpty())
                    ||
                    ((Objects.isNull(permittedKeys) || permittedKeys.isEmpty()) && !permittedKeysExisting.isEmpty())
               )
            {
                throw new InvalidContextException("A context with this name and different set of permittedKeys exists");
            }
        }
        
        return GLOBAL_CONTEXT_STORE.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContext(ApplicationContext applicationContext)
    {
        Objects.requireNonNull(applicationContext, "Cannot set null ApplicationContext");
        assert applicationContext instanceof ConcurrentApplicationContext : "applicationContext must be of type ConcurrentApplicationContext as Global holder supports only instances of ConcurrentApplicationContext";
        
        GLOBAL_CONTEXT_STORE.putIfAbsent(applicationContext.getName(), (ConcurrentApplicationContext)applicationContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationContext clearContext(String name)
    {
        return GLOBAL_CONTEXT_STORE.remove(name);
    }
}
