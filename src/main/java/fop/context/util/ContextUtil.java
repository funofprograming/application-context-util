package fop.context.util;

import java.util.Set;

import fop.context.ApplicationContextKey;
import fop.context.GlobalApplicationContext;
import fop.context.InstanceApplicationContext;
import fop.context.LocalApplicationContext;
import fop.context.impl.global.GlobalContextManager;
import fop.context.impl.instance.InstanceContextManager;
import fop.context.impl.local.LocalContextManager;

public class ContextUtil {

    private ContextUtil() 
    {}
    
    public static GlobalApplicationContext getUnrestrictedGlobalContext(String name) 
    {
        return GlobalContextManager.getUnrestrictedGlobalContext(name);
    }

    public static GlobalApplicationContext getUnrestrictedGlobalContext(String name, Integer defaultConcurrentWriteTimeoutSeconds) 
    {
        return GlobalContextManager.getUnrestrictedGlobalContext(name, defaultConcurrentWriteTimeoutSeconds);
    }
    
    public static GlobalApplicationContext getRestrictedGlobalContext(String name, Set<ApplicationContextKey<?>> permittedKeys) 
    {
        return GlobalContextManager.getRestrictedGlobalContext(name, permittedKeys);
    }
    
    public static GlobalApplicationContext getRestrictedGlobalContext(String name, Integer defaultConcurrentWriteTimeoutSeconds, Set<ApplicationContextKey<?>> permittedKeys) 
    {
        return GlobalContextManager.getRestrictedGlobalContext(name, defaultConcurrentWriteTimeoutSeconds, permittedKeys);
    }
    
    public static void closeGlobalApplicationContext(String name)
    {
        GlobalContextManager.closeContext(name);
    }
    
    public static LocalApplicationContext getUnrestrictedLocalContext(String name) 
    {
        return LocalContextManager.getUnrestrictedLocalContext(name);
    }
    
    public static LocalApplicationContext getRestrictedLocalContext(String name, Set<ApplicationContextKey<?>> permittedKeys) 
    {
        return LocalContextManager.getRestrictedLocalContext(name, permittedKeys);
    }
    
    public static InstanceApplicationContext getUnrestrictedInstanceApplicationContext(String name)
    {
        return InstanceContextManager.getUnrestrictedInstanceApplicationContext(name);
    }
    
    public static InstanceApplicationContext getRestrictedInstanceApplicationContext(String name, Set<ApplicationContextKey<?>> permittedKeys)
    {
    	return InstanceContextManager.getRestrictedInstanceApplicationContext(name, permittedKeys);
    }
}
