package fop.context.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import fop.context.ApplicationContext;
import fop.context.ApplicationContextHolderStrategy;
import fop.context.ApplicationContextKey;

public final class InheritableThreadLocalContextHolderStrategy implements ApplicationContextHolderStrategy
{
    private static final ThreadLocal<Map<String, ApplicationContext>> LOCAL_CONTEXT_STORE = new InheritableThreadLocal<>() {
        
        protected Map<String, ApplicationContext> initialValue() {
            return new HashMap<>();
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationContext getContext(String name)
    {
        if (!LOCAL_CONTEXT_STORE.get().containsKey(name))
        {
            setContext(new ApplicationContextImpl(name));
        }

        return LOCAL_CONTEXT_STORE.get().get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationContext getContext(String name, Set<ApplicationContextKey<?>> permittedKeys)
    {
        if (!LOCAL_CONTEXT_STORE.get().containsKey(name))
        {
            setContext(new ApplicationContextImpl(name, permittedKeys));
        }
        else
        {
            Set<ApplicationContextKey<?>> permittedKeysExisting = LOCAL_CONTEXT_STORE.get().get(name).getPermittedKeys();

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

        return LOCAL_CONTEXT_STORE.get().get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContext(ApplicationContext applicationContext)
    {
        Objects.requireNonNull(applicationContext, "Cannot set null ApplicationContext");

        LOCAL_CONTEXT_STORE.get().putIfAbsent(applicationContext.getName(), applicationContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationContext clearContext(String name)
    {
        return LOCAL_CONTEXT_STORE.get().remove(name);
    }
}
