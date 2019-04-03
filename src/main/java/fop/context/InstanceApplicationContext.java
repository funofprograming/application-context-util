package fop.context;

public interface InstanceApplicationContext extends ApplicationContext 
{
    default public ApplicationContextTypes.Scope scope()
    {
        return ApplicationContextTypes.Scope.INSTANCE;
    }
}
