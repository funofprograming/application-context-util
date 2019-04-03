package fop.context;

public interface ConcurrentApplicationContext extends ApplicationContext
{
    public <T> void addIfNotPresent(ApplicationContextKey<T> key, T value, int timeout);
    
    public <T> T addWithOverwrite(ApplicationContextKey<T> key, T value, int timeout);
    
    public <T> void add(ApplicationContextKey<T> key, T value, int timeout);
    
    public <T> boolean exists(ApplicationContextKey<T> key, int timeout);

    public <T> T fetch(ApplicationContextKey<T> key, int timeout);
    
    public <T> T erase(ApplicationContextKey<T> key, int timeout);
    
    public void clear(int timeout);
    
    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy, int timeout);
}
