package fop.context.util;

import java.util.Set;

import fop.context.InstanceApplicationContext;
import fop.context.impl.instance.RestrictedInstanceApplicationContext;
import fop.context.impl.instance.RestrictedInstanceApplicationContext;
import fop.context.impl.instance.UnrestrictedInstanceApplicationContext;
import fop.context.impl.instance.UnrestrictedInstanceApplicationContext;

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
    
    public static InstanceApplicationContext getRestrictedInstanceApplicationContext(String name, Integer defaultConcurrentWriteTimeoutSeconds, Set<String> permittedKeys)
    {
        return new RestrictedInstanceApplicationContext(name, defaultConcurrentWriteTimeoutSeconds, permittedKeys);
    }
    
    public static InstanceApplicationContext getRestrictedInstanceApplicationContext(String name, Set<String> permittedKeys)
    {
        return InstanceContextManager.getRestrictedInstanceApplicationContext(name, null, permittedKeys);
    }
}
