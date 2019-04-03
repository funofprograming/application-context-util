package fop.context;

public interface ApplicationContextMergeStrategy 
{
    public <T> T merge(ApplicationContextKey<T> key, T oldValue, T newValue); 
}
