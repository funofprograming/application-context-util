package fop.context;

/**
 * {@linkplain ApplicationContext} extension with {@linkplain ApplicationContextTypes.Scope}.LOCAL scope
 * 
 * @author Akshay Jain
 *
 */
public interface LocalApplicationContext extends ApplicationContext
{
    default public ApplicationContextTypes.Scope scope()
    {
        return ApplicationContextTypes.Scope.LOCAL;
    }
}
