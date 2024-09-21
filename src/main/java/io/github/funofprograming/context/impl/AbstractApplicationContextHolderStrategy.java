/**
 * 
 */
package io.github.funofprograming.context.impl;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import io.github.funofprograming.context.ApplicationContext;
import io.github.funofprograming.context.ApplicationContextHolderStrategy;
import io.github.funofprograming.context.Key;

/**
 * 
 */
public abstract class AbstractApplicationContextHolderStrategy implements ApplicationContextHolderStrategy
{

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationContext getContext(String name)
    {
        return getContext(name, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationContext getContext(String name, Set<Key<?>> permittedKeys)
    {
        if(!contextContainedInStore(name)) 
        {
            setContext(new ConcurrentApplicationContextImpl(name, permittedKeys));
        }
        else 
        {
            Set<Key<?>> permittedKeysExisting = getContextFromStore(name).getPermittedKeys();
            
            if(
                    !Optional.ofNullable(permittedKeys).orElse(Collections.emptySet()).equals(Optional.ofNullable(permittedKeysExisting).orElse(Collections.emptySet()))
               )
            {
                throw new InvalidContextException("A context with this name and different set of permittedKeys already exists for this ApplicationContextHolderStrategy");
            }
        }
        
        return getContextFromStore(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContext(ApplicationContext applicationContext)
    {
        validateContext(applicationContext);
        setContextInStore(applicationContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationContext clearContext(String name)
    {
        return removeContextFromStore(name);
    }
    
    /**
     * Validate context before setting in store. Child classes can override/add more validations.
     * @param applicationContext
     */
    protected void validateContext(ApplicationContext applicationContext)
    {
        Objects.requireNonNull(applicationContext, "Cannot set null ApplicationContext");
        if(contextContainedInStore(applicationContext.getName()))
        {
            throw new InvalidContextException("Context with same name already exists for this ApplicationContextHolderStrategy");
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean existsContext(String name) 
    {
	return contextContainedInStore(name);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean existsContext(String name, Set<Key<?>> permittedKeys)
    {
	if(contextContainedInStore(name)) 
	{
	    Set<Key<?>> permittedKeysExisting = getContextFromStore(name).getPermittedKeys();
	    return Optional.ofNullable(permittedKeys).orElse(Collections.emptySet()).equals(Optional.ofNullable(permittedKeysExisting).orElse(Collections.emptySet()));
	}
	
	return false;
    }
    
    
    /**
     * Check whether context contained in underlying store
     * 
     * @param name
     * @return true if context contained in underlying store else false
     */
    protected abstract boolean contextContainedInStore(String name);
    
    /**
     * Get context from underlying store 
     * 
     * @param name
     * @return ApplicationContext from underlying store
     */
    protected abstract ApplicationContext getContextFromStore(String name);
    
    /**
     * Add/Override the ApplicationContext into underlying store
     * 
     * @param applicationContext
     */
    protected abstract void setContextInStore(ApplicationContext applicationContext);
    
    /**
     * Remove the context from underlying store
     * 
     * @param name
     * @return the just removed context
     */
    protected abstract ApplicationContext removeContextFromStore(String name);

}
