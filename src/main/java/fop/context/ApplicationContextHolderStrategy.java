package fop.context;

import java.util.Set;

import fop.context.impl.InvalidContextException;
import fop.context.impl.InvalidKeyException;

public interface ApplicationContextHolderStrategy
{
    /**
     * Get a context for given name. If not available then create one, set in holder and return back.
     * 
     * @param name
     * @return
     */
    public ApplicationContext getContext(String name);
    
    /**
     * Same as getContext but with permittedKeys. If context already exist with different set of keys then this method should throw {@link InvalidKeyException}
     * 
     * @param name
     * @return
     */
    public ApplicationContext getContext(String name, Set<ApplicationContextKey<?>> permittedKeys);
    
    /**
     * Set any externally created context into the context holder strategy. If any context with this name already exist then this method should throw {@link InvalidContextException}
     * 
     * @param applicationContext
     */
    public void setContext(ApplicationContext applicationContext);
    
    /**
     * Clear context for given name and return back the context
     * 
     * @param name
     */
    public ApplicationContext clearContext(String name);
}
