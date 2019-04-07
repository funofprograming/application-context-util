package fop.context;

/**
 * This is a {@linkplain ConcurrentApplicationContext} extension with {@linkplain ApplicationContextTypes.Scope}.GLOBAL scope
 * 
 * @author Akshay Jain
 *
 */
public interface GlobalApplicationContext extends ConcurrentApplicationContext 
{
    default public ApplicationContextTypes.Scope scope()
    {
        return ApplicationContextTypes.Scope.GLOBAL;
    }
}
