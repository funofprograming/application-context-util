package fop.context;

public interface GlobalApplicationContext extends ConcurrentApplicationContext 
{
    default public ApplicationContextTypes.Scope scope()
    {
        return ApplicationContextTypes.Scope.GLOBAL;
    }
}
