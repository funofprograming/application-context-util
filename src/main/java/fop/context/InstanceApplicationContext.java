package fop.context;

/**
 * This is a {@linkplain ConcurrentApplicationContext} extension with {@linkplain ApplicationContextTypes.Scope}.INSTANCE scope
 * 
 * @author Akshay Jain
 *
 */
public interface InstanceApplicationContext extends ConcurrentApplicationContext 
{
    default public ApplicationContextTypes.Scope scope()
    {
        return ApplicationContextTypes.Scope.INSTANCE;
    }
}
