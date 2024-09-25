package io.github.funofprograming.context.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.github.funofprograming.context.ApplicationContext;
import io.github.funofprograming.context.ConcurrentApplicationContext;

public class GlobalContextHolderStrategy extends AbstractApplicationContextHolderStrategy
{
    private static final ConcurrentMap<String, ConcurrentApplicationContext> GLOBAL_CONTEXT_STORE = new ConcurrentHashMap<>();
    
    /**
     * Validate context before setting in store. applicationContext must be of type ConcurrentApplicationContext as Global holder supports only instances of ConcurrentApplicationContext
     */
    protected void validateContext(ApplicationContext applicationContext)
    {
        super.validateContext(applicationContext);
        assert applicationContext instanceof ConcurrentApplicationContext : "applicationContext must be of type ConcurrentApplicationContext as Global holder supports only instances of ConcurrentApplicationContext";
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ApplicationContext> Class<T> supportedApplicationContextType()
    {
	return (Class<T>) ConcurrentApplicationContext.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean contextContainedInStore(String name)
    {
        return GLOBAL_CONTEXT_STORE.containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ApplicationContext getContextFromStore(String name)
    {
        return GLOBAL_CONTEXT_STORE.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setContextInStore(ApplicationContext applicationContext)
    {
        GLOBAL_CONTEXT_STORE.putIfAbsent(applicationContext.getName(), (ConcurrentApplicationContext)applicationContext);        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ApplicationContext removeContextFromStore(String name)
    {
        return GLOBAL_CONTEXT_STORE.remove(name);
    }
}
