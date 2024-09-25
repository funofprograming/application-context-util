package io.github.funofprograming.context.impl;

import java.util.HashMap;
import java.util.Map;

import io.github.funofprograming.context.ApplicationContext;

public class ThreadLocalContextHolderStrategy extends AbstractApplicationContextHolderStrategy
{
    private static final ThreadLocal<Map<String, ApplicationContext>> LOCAL_CONTEXT_STORE = new ThreadLocal<>() {
        
        protected Map<String, ApplicationContext> initialValue() {
            return new HashMap<>();
        }
    };
    
    protected ThreadLocal<Map<String, ApplicationContext>> getThreadLocalContextStore()
    {
        return LOCAL_CONTEXT_STORE;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ApplicationContext> Class<T> supportedApplicationContextType()
    {
	return (Class<T>) ApplicationContext.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean contextContainedInStore(String name)
    {
        return getThreadLocalContextStore().get().containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ApplicationContext getContextFromStore(String name)
    {
        return getThreadLocalContextStore().get().get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setContextInStore(ApplicationContext applicationContext)
    {
        getThreadLocalContextStore().get().putIfAbsent(applicationContext.getName(), applicationContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ApplicationContext removeContextFromStore(String name)
    {
        return getThreadLocalContextStore().get().remove(name);
    }
}
