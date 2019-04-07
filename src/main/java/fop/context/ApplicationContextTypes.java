package fop.context;

import java.util.concurrent.ConcurrentMap;

import fop.context.impl.global.GlobalContextManager;
import fop.context.impl.local.LocalContextManager;

/**
 * Different categorisations of application context
 * @author Akshay Jain
 *
 */
public interface ApplicationContextTypes {

    /**
     * Defines scope of a context inside the application 
     * @author Akshay Jain
     *
     */
    public static enum Scope
    {
        /**
         * Local scope implies that context is local to the thread that created it. Such context are managed by {@linkplain LocalContextManager} using {@linkplain ThreadLocal} variables. 
         */
        LOCAL,
        
        /**
         * Global scope implies that context is available globally in whole application. Such context are thread safe and managed by {@linkplain GlobalContextManager} using {@linkplain ConcurrentMap}.
         */
        GLOBAL,
        
        /**
         * Instance scope implies that context can be encapsulated within an object and is thread safe.
         */
        INSTANCE;
    }
    
    /**
     * Defines gate of context in terms of permitted keys
     * @author Akshay Jain
     *
     */
    public static enum Gate
    {
        /**
         * Restricted scope implies that only a certain set of keys are permitted to be added to this type of context. Permitted set of keys should be set at the time of initialization of context
         */
        RESTRICTED,
        
        /**
         * Unrestricted scope implies that any key can be added to this type of context
         */
        UNRESTRICTED;
    }
    
}
