package fop.context;

public interface LocalApplicationContext extends ApplicationContext
{
    default public ApplicationContextTypes.Scope scope()
    {
        return ApplicationContextTypes.Scope.LOCAL;
    }
}
