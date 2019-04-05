package fop.context.impl.instance;

import java.util.Set;

import fop.context.ApplicationContextKey;
import fop.context.InstanceApplicationContext;

/**
 * InstanceContextManager only instantiates the context but no other management
 * @author akshay.jain
 *
 */
public class InstanceContextManager 
{
    private InstanceContextManager() 
    {}
    
    public static InstanceApplicationContext getUnrestrictedInstanceApplicationContext(String name, Integer defaultConcurrentWriteTimeoutSeconds)
    {
        return new UnrestrictedInstanceApplicationContext(name, defaultConcurrentWriteTimeoutSeconds);
    }
    
    public static InstanceApplicationContext getUnrestrictedInstanceApplicationContext(String name)
    {
        return InstanceContextManager.getUnrestrictedInstanceApplicationContext(name, null);
    }
    
    public static InstanceApplicationContext getRestrictedInstanceApplicationContext(String name, Integer defaultConcurrentWriteTimeoutSeconds, Set<ApplicationContextKey<?>> permittedKeys)
    {
        return new RestrictedInstanceApplicationContext(name, defaultConcurrentWriteTimeoutSeconds, permittedKeys);
    }
    
    public static InstanceApplicationContext getRestrictedInstanceApplicationContext(String name, Set<ApplicationContextKey<?>> permittedKeys)
    {
        return InstanceContextManager.getRestrictedInstanceApplicationContext(name, null, permittedKeys);
    }
}
