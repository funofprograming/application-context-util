package io.github.funofprograming.context.impl;

import java.util.Set;

import io.github.funofprograming.context.ApplicationContext;
import io.github.funofprograming.context.ApplicationContextHolderStrategy;
import io.github.funofprograming.context.Key;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationContextHolder
{
    private static final ApplicationContextHolderStrategy globalContextHolderStrategy = new GlobalContextHolderStrategy();
    
    private static final ApplicationContextHolderStrategy threadLocalContextHolderStrategy = new ThreadLocalContextHolderStrategy();
    
    private static final ApplicationContextHolderStrategy inheritableThreadLocalContextHolderStrategy = new InheritableThreadLocalContextHolderStrategy();
    
    /**
     * Get a Global context for given name. If not available then create one, set in holder and return back.
     * 
     * @param name
     * @return
     */
    public static ApplicationContext getGlobalContext(String name)
    {
        return getContext(name, globalContextHolderStrategy);
    }
    
    /**
     * Same as getGlobalContext but with permittedKeys. If context already exist with different set of keys then this method should throw {@link InvalidKeyException}
     * 
     * @param name
     * @return
     */
    public static ApplicationContext getGlobalContext(String name, Set<Key<?>> permittedKeys)
    {
        return getContext(name, permittedKeys, globalContextHolderStrategy);
    }
    
    /**
     * Set any externally created Global context into the context holder strategy. If any context with this name already exist then this method should throw {@link InvalidContextException}
     * 
     * @param applicationContext
     */
    public static void setGlobalContext(ApplicationContext applicationContext)
    {
        setContext(applicationContext, globalContextHolderStrategy);
    }
    
    /**
     * Clear Global context for given name and return back the context
     * 
     * @param name
     */
    public static ApplicationContext clearGlobalContext(String name)
    {
        return clearContext(name, globalContextHolderStrategy);
    }
    
    /**
     * Check if Global context with given name exists
     * 
     * @param name
     * @return
     */
    public static boolean existsGlobalContext(String name)
    {
	return existsContext(name, globalContextHolderStrategy);
    }
    
    /**
     * Check if Global context with given name and permittedKeys exists
     * 
     * @param name
     * @return
     */
    public static boolean existsGlobalContext(String name,  Set<Key<?>> permittedKeys)
    {
	return existsContext(name, permittedKeys, globalContextHolderStrategy);
    }
    
    /**
     * Get a ThreadLocal context for given name. If not available then create one, set in holder and return back.
     * 
     * @param name
     * @return
     */
    public static ApplicationContext getThreadLocalContext(String name)
    {
        return getContext(name, threadLocalContextHolderStrategy);
    }
    
    /**
     * Same as getThreadLocalContext but with permittedKeys. If context already exist with different set of keys then this method should throw {@link InvalidKeyException}
     * 
     * @param name
     * @return
     */
    public static ApplicationContext getThreadLocalContext(String name, Set<Key<?>> permittedKeys)
    {
        return getContext(name, permittedKeys, threadLocalContextHolderStrategy);
    }
    
    /**
     * Set any externally created ThreadLocal context into the context holder strategy. If any context with this name already exist then this method should throw {@link InvalidContextException}
     * 
     * @param applicationContext
     */
    public static void setThreadLocalContext(ApplicationContext applicationContext)
    {
        setContext(applicationContext, threadLocalContextHolderStrategy);
    }
    
    /**
     * Clear ThreadLocal context for given name and return back the context
     * 
     * @param name
     */
    public static ApplicationContext clearThreadLocalContext(String name)
    {
        return clearContext(name, threadLocalContextHolderStrategy);
    }
    
    /**
     * Check if ThreadLocal context with given name exists
     * 
     * @param name
     * @return
     */
    public static boolean existsThreadLocalContext(String name)
    {
	return existsContext(name, threadLocalContextHolderStrategy);
    }
    
    /**
     * Check if ThreadLocal context with given name and permittedKeys exists
     * 
     * @param name
     * @return
     */
    public static boolean existsThreadLocalContext(String name,  Set<Key<?>> permittedKeys)
    {
	return existsContext(name, permittedKeys, threadLocalContextHolderStrategy);
    }
    
    /**
     * Get a InheritableThreadLocal context for given name. If not available then create one, set in holder and return back.
     * 
     * @param name
     * @return
     */
    public static ApplicationContext getInheritableThreadLocalContext(String name)
    {
        return getContext(name, inheritableThreadLocalContextHolderStrategy);
    }
    
    /**
     * Same as getInheritableThreadLocalContext but with permittedKeys. If context already exist with different set of keys then this method should throw {@link InvalidKeyException}
     * 
     * @param name
     * @return
     */
    public static ApplicationContext getInheritableThreadLocalContext(String name, Set<Key<?>> permittedKeys)
    {
        return getContext(name, permittedKeys, inheritableThreadLocalContextHolderStrategy);
    }
    
    /**
     * Set any externally created InheritableThreadLocal context into the context holder strategy. If any context with this name already exist then this method should throw {@link InvalidContextException}
     * 
     * @param applicationContext
     */
    public static void setInheritableThreadLocalContext(ApplicationContext applicationContext)
    {
        setContext(applicationContext, inheritableThreadLocalContextHolderStrategy);
    }
    
    /**
     * Clear InheritableThreadLocal context for given name and return back the context
     * 
     * @param name
     */
    public static ApplicationContext clearInheritableThreadLocalContext(String name)
    {
        return clearContext(name, inheritableThreadLocalContextHolderStrategy);
    }
    
    /**
     * Check if InheritableThreadLocal context with given name exists
     * 
     * @param name
     * @return
     */
    public static boolean existsInheritableThreadLocalContext(String name)
    {
	return existsContext(name, inheritableThreadLocalContextHolderStrategy);
    }
    
    /**
     * Check if InheritableThreadLocal context with given name and permittedKeys exists
     * 
     * @param name
     * @return
     */
    public static boolean existsInheritableThreadLocalContext(String name,  Set<Key<?>> permittedKeys)
    {
	return existsContext(name, permittedKeys, inheritableThreadLocalContextHolderStrategy);
    }
    
    /**
     * Get a context for given name. If not available then create one, set in holder and return back.
     * 
     * @param name
     * @param applicationContextHolderStrategy
     * @return
     */
    private static ApplicationContext getContext(String name, ApplicationContextHolderStrategy applicationContextHolderStrategy) 
    {
        return applicationContextHolderStrategy.getContext(name);
    }
    
    /**
     * Same as getContext but with permittedKeys. If context already exist with different set of keys then this method should throw {@link InvalidKeyException}
     * 
     * @param name
     * @param applicationContextHolderStrategy
     * @return
     */
    private static ApplicationContext getContext(String name, Set<Key<?>> permittedKeys, ApplicationContextHolderStrategy applicationContextHolderStrategy)
    {
        return applicationContextHolderStrategy.getContext(name, permittedKeys);
    }
    
    /**
     * Set any externally created context into the context holder strategy. If any context with this name already exist then this method should throw {@link InvalidContextException}
     * 
     * @param applicationContext
     * @param applicationContextHolderStrategy
     */
    private static void setContext(ApplicationContext applicationContext, ApplicationContextHolderStrategy applicationContextHolderStrategy)
    {
        applicationContextHolderStrategy.setContext(applicationContext);
    }
    
    /**
     * Clear context for given name and return back the context
     * 
     * @param name
     * @param applicationContextHolderStrategy
     */
    private static ApplicationContext clearContext(String name, ApplicationContextHolderStrategy applicationContextHolderStrategy)
    {
        return applicationContextHolderStrategy.clearContext(name);
    }
    
    /**
     * Check if context with given name exists
     * 
     * @param name
     * @return
     */
    private static boolean existsContext(String name, ApplicationContextHolderStrategy applicationContextHolderStrategy)
    {
	return applicationContextHolderStrategy.existsContext(name);
    }
    
    /**
     * Check if context with given name and permittedKeys exists
     * 
     * @param name
     * @return
     */
    private static boolean existsContext(String name, Set<Key<?>> permittedKeys, ApplicationContextHolderStrategy applicationContextHolderStrategy)
    {
	return applicationContextHolderStrategy.existsContext(name, permittedKeys);
    }
}
