package fop.context.impl;

import java.util.Set;

import fop.context.ApplicationContext;
import fop.context.ApplicationContextHolderStrategy;
import fop.context.ApplicationContextKey;

public class ApplicationContextHolder
{
    private ApplicationContextHolder() {} //all static methods so should not be initializable
    
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
    public static ApplicationContext getGlobalContext(String name, Set<ApplicationContextKey<?>> permittedKeys)
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
    public static ApplicationContext getThreadLocalContext(String name, Set<ApplicationContextKey<?>> permittedKeys)
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
    public static ApplicationContext getInheritableThreadLocalContext(String name, Set<ApplicationContextKey<?>> permittedKeys)
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
     * Get a context for given name. If not available then create one, set in holder and return back.
     * 
     * @param name
     * @param applicationContextHolderStrategy
     * @return
     */
    public static ApplicationContext getContext(String name, ApplicationContextHolderStrategy applicationContextHolderStrategy) 
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
    public static ApplicationContext getContext(String name, Set<ApplicationContextKey<?>> permittedKeys, ApplicationContextHolderStrategy applicationContextHolderStrategy)
    {
        return applicationContextHolderStrategy.getContext(name, permittedKeys);
    }
    
    /**
     * Set any externally created context into the context holder strategy. If any context with this name already exist then this method should throw {@link InvalidContextException}
     * 
     * @param applicationContext
     * @param applicationContextHolderStrategy
     */
    public static void setContext(ApplicationContext applicationContext, ApplicationContextHolderStrategy applicationContextHolderStrategy)
    {
        applicationContextHolderStrategy.setContext(applicationContext);
    }
    
    /**
     * Clear context for given name and return back the context
     * 
     * @param name
     * @param applicationContextHolderStrategy
     */
    public static ApplicationContext clearContext(String name, ApplicationContextHolderStrategy applicationContextHolderStrategy)
    {
        return applicationContextHolderStrategy.clearContext(name);
    }
}
