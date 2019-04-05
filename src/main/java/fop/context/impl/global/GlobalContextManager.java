package fop.context.impl.global;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import fop.context.ApplicationContextKey;
import fop.context.GlobalApplicationContext;
import fop.context.RestrictedApplicationContext;
import fop.context.ApplicationContextTypes.Gate;
import fop.context.impl.InvalidContextException;

/**
 * GlobalContextManager instantiates and manages context in global JVM level environment
 * @author akshay.jain
 *
 */
public class GlobalContextManager 
{
    private GlobalContextManager() 
    {}

    private static final ConcurrentMap<String, GlobalApplicationContext> GLOBAL_CONTEXT_STORE = new ConcurrentHashMap<>();
    
    public static GlobalApplicationContext getUnrestrictedGlobalContext(String name)
    {
        return GlobalContextManager.getUnrestrictedGlobalContext(name, null);
    }
    
    public static GlobalApplicationContext getUnrestrictedGlobalContext(String name, Integer defaultConcurrentWriteTimeoutSeconds)
    {
        if(!GLOBAL_CONTEXT_STORE.containsKey(name))
        {
            GlobalApplicationContext globalContext = new UnrestrictedGlobalApplicationContext(name, defaultConcurrentWriteTimeoutSeconds);
            GLOBAL_CONTEXT_STORE.putIfAbsent(globalContext.getName(), globalContext);
        }
        else if(GLOBAL_CONTEXT_STORE.get(name).gate()==Gate.RESTRICTED)
        {
            throw new InvalidContextException("A Restricted version of context exists with this name so cannot create an unrestricted one");
        }
        
        return GLOBAL_CONTEXT_STORE.get(name);
    }
    
    public static GlobalApplicationContext getRestrictedGlobalContext(String name, Set<ApplicationContextKey<?>> permittedKeys) 
    {
        return GlobalContextManager.getRestrictedGlobalContext(name, null, permittedKeys);
    }
    
    public static GlobalApplicationContext getRestrictedGlobalContext(String name, Integer defaultConcurrentWriteTimeoutSeconds, Set<ApplicationContextKey<?>> permittedKeys) 
    {
        if(!GLOBAL_CONTEXT_STORE.containsKey(name))
        {
            GlobalApplicationContext globalContext = new RestrictedGlobalApplicationContext(name, defaultConcurrentWriteTimeoutSeconds, permittedKeys);
            GLOBAL_CONTEXT_STORE.putIfAbsent(globalContext.getName(), globalContext);
        }
        else if(GLOBAL_CONTEXT_STORE.get(name).gate()==Gate.UNRESTRICTED)
        {
            throw new InvalidContextException("An Unrestricted version of context exists with this name so cannot create a restricted one");
        }
        
        //in case some other thread already created restricted context with same name but with different keys
        Set<ApplicationContextKey<?>> contextKeys = ((RestrictedApplicationContext)GLOBAL_CONTEXT_STORE.get(name)).getPermittedKeys();
        if(permittedKeys != null && !permittedKeys.isEmpty() && !contextKeys.equals(permittedKeys))
        {
            throw new InvalidContextException("A restricted context exists with same name but different permitted key set so cannot create requested one");
        }
        
        return GLOBAL_CONTEXT_STORE.get(name);
    }
    
    public static Set<String> getAllGlobalContextNamesSnapshot()
    {
        Set<String> globalContextSnapshot = new HashSet<>(GLOBAL_CONTEXT_STORE.keySet());
        return globalContextSnapshot;
    }
    
    public static List<GlobalApplicationContext> getAllGlobalContextsSnapshot()
    {
        List<GlobalApplicationContext> globalContextSnapshot = new ArrayList<>(GLOBAL_CONTEXT_STORE.values());
        return globalContextSnapshot;
    }
    
    public static void closeContext(String name)
    {
        if(GLOBAL_CONTEXT_STORE.containsKey(name))
            GLOBAL_CONTEXT_STORE.remove(name);
    }
}
