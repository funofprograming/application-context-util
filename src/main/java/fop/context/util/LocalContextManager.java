package fop.context.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import fop.context.LocalApplicationContext;
import fop.context.RestrictedApplicationContext;
import fop.context.ApplicationContextTypes.Gate;
import fop.context.impl.InvalidContextException;
import fop.context.impl.local.RestrictedLocalApplicationContext;
import fop.context.impl.local.UnrestrictedLocalApplicationContext;

/**
 * LocalContextManager instantiates and manages context on each Thread level environment
 * @author akshay.jain
 *
 */
public class LocalContextManager 
{
    private LocalContextManager() 
    {}

    private static final ThreadLocal<Map<String, LocalApplicationContext>> LOCAL_CONTEXT_STORE = ThreadLocal.withInitial(
        new Supplier<Map<String, LocalApplicationContext>>()
        {
            @Override
            public Map<String, LocalApplicationContext> get()
            {
                return new HashMap<>();
            }
        }
    );
    
    public static LocalApplicationContext getUnrestrictedLocalContext(String name) 
    {
        LocalApplicationContext localContext = LOCAL_CONTEXT_STORE.get().get(name);
        if(localContext == null)
        {
            localContext = new UnrestrictedLocalApplicationContext(name); 
            LOCAL_CONTEXT_STORE.get().put(localContext.getName(), localContext);
        }
        else if(localContext.gate()==Gate.RESTRICTED)
        {
            throw new InvalidContextException("A Restricted version of context exists with this name so cannot create an unrestricted one");
        }
        
        return localContext;
    }
    
    public static LocalApplicationContext getRestrictedLocalContext(String name, Set<String> permittedKeys) 
    {
        LocalApplicationContext localContext = LOCAL_CONTEXT_STORE.get().get(name);
        if(localContext == null)
        {
            localContext = new RestrictedLocalApplicationContext(name, permittedKeys); 
            LOCAL_CONTEXT_STORE.get().put(localContext.getName(), localContext);
        }
        else if(localContext.gate()==Gate.UNRESTRICTED)
        {
            throw new InvalidContextException("An Unrestricted version of context exists with this name so cannot create an restricted one");
        }
        
        //in case this thread already created restricted context with same name but with different keys
        Set<String> contextKeys = ((RestrictedApplicationContext)localContext).getPermittedKeys();
        if(permittedKeys != null && !permittedKeys.isEmpty() && !contextKeys.equals(permittedKeys))
        {
            throw new InvalidContextException("A restricted context exists with same name but different key set so cannot create requested one");
        }
        
        return localContext;
    }
}
