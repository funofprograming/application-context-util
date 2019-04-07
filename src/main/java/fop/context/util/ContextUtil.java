package fop.context.util;

import java.util.Set;

import fop.context.ApplicationContextKey;
import fop.context.GlobalApplicationContext;
import fop.context.InstanceApplicationContext;
import fop.context.LocalApplicationContext;
import fop.context.RestrictedApplicationContext;
import fop.context.UnrestrictedApplicationContext;
import fop.context.impl.InvalidContextException;
import fop.context.impl.global.GlobalContextManager;
import fop.context.impl.instance.InstanceContextManager;
import fop.context.impl.local.LocalContextManager;

/**
 * Simple utility class for generating various types of context
 * 
 * @author Akshay Jain
 *
 */
public class ContextUtil {

    private ContextUtil() 
    {}
   
    /**
     * Get a {@linkplain GlobalApplicationContext} that also implements {@linkplain UnrestrictedApplicationContext}
     * 
     * If {@linkplain GlobalApplicationContext} with this name already exists then that will be returned otherwise a new one will be created, stored and returned
     * 
     * @param name
     * @return
     */
    public static GlobalApplicationContext getUnrestrictedGlobalContext(String name) 
    {
        return GlobalContextManager.getUnrestrictedGlobalContext(name);
    }

    /**
     * Get a {@linkplain GlobalApplicationContext} that also implements {@linkplain UnrestrictedApplicationContext}
     * 
     * If {@linkplain GlobalApplicationContext} with this name already exists then that will be returned otherwise a new one will be created, stored and returned
     * 
     * @param name
     * @param defaultConcurrentWriteTimeoutSeconds
     * @return
     */
    public static GlobalApplicationContext getUnrestrictedGlobalContext(String name, Integer defaultConcurrentWriteTimeoutSeconds) 
    {
        return GlobalContextManager.getUnrestrictedGlobalContext(name, defaultConcurrentWriteTimeoutSeconds);
    }
    
    /**
     * Get a {@linkplain GlobalApplicationContext} that also implements {@linkplain RestrictedApplicationContext}
     * 
     * If {@linkplain GlobalApplicationContext} with this name already exists then that will be returned otherwise a new one will be created, stored and returned
     * 
     * If there is a {@linkplain GlobalApplicationContext} with same name and different keyset exists then {@linkplain InvalidContextException} will be thrown
     * 
     * @param name
     * @param permittedKeys
     * @return
     * @throws InvalidContextException
     */
    public static GlobalApplicationContext getRestrictedGlobalContext(String name, Set<ApplicationContextKey<?>> permittedKeys) 
    {
        return GlobalContextManager.getRestrictedGlobalContext(name, permittedKeys);
    }
    
    /**
     * Get a {@linkplain GlobalApplicationContext} that also implements {@linkplain RestrictedApplicationContext}
     * 
     * If {@linkplain GlobalApplicationContext} with this name already exists then that will be returned otherwise a new one will be created, stored and returned
     * 
     * If there is a {@linkplain GlobalApplicationContext} with same name and different keyset exists then {@linkplain InvalidContextException} will be thrown
     * 
     * @param name
     * @param defaultConcurrentWriteTimeoutSeconds
     * @param permittedKeys
     * @return
     * @throws InvalidContextException
     */
    public static GlobalApplicationContext getRestrictedGlobalContext(String name, Integer defaultConcurrentWriteTimeoutSeconds, Set<ApplicationContextKey<?>> permittedKeys) 
    {
        return GlobalContextManager.getRestrictedGlobalContext(name, defaultConcurrentWriteTimeoutSeconds, permittedKeys);
    }
    
    /**
     * Close a {@linkplain GlobalApplicationContext} 
     * 
     * If {@linkplain GlobalApplicationContext} with this name already exists then that will be closed and removed from global context store.
     * 
     * @param name
     */
    public static void closeGlobalApplicationContext(String name)
    {
        GlobalContextManager.closeContext(name);
    }
    
    /**
     * Get a {@linkplain LocalApplicationContext} that also implements {@linkplain UnrestrictedApplicationContext}
     * 
     * @param name
     * @return
     */
    public static LocalApplicationContext getUnrestrictedLocalContext(String name) 
    {
        return LocalContextManager.getUnrestrictedLocalContext(name);
    }
    
    /**
     * Get a {@linkplain LocalApplicationContext} that also implements {@linkplain RestrictedApplicationContext}
     * 
     * @param name
     * @param permittedKeys
     * @return
     */
    public static LocalApplicationContext getRestrictedLocalContext(String name, Set<ApplicationContextKey<?>> permittedKeys) 
    {
        return LocalContextManager.getRestrictedLocalContext(name, permittedKeys);
    }
    
    /**
     * Get a {@linkplain InstanceApplicationContext} that also implements {@linkplain UnrestrictedApplicationContext}
     * 
     * @param name
     * @return
     */
    public static InstanceApplicationContext getUnrestrictedInstanceApplicationContext(String name)
    {
        return InstanceContextManager.getUnrestrictedInstanceApplicationContext(name);
    }
    
    /**
     * Get a {@linkplain InstanceApplicationContext} that also implements {@linkplain RestrictedApplicationContext}
     * 
     * @param name
     * @param permittedKeys
     * @return
     */
    public static InstanceApplicationContext getRestrictedInstanceApplicationContext(String name, Set<ApplicationContextKey<?>> permittedKeys)
    {
    	return InstanceContextManager.getRestrictedInstanceApplicationContext(name, permittedKeys);
    }
}
