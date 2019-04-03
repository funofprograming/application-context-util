package fop.context;

import java.util.Set;

public interface ApplicationContext extends Cloneable
{
    public ApplicationContextTypes.Scope scope();
    
    public ApplicationContextTypes.Gate gate();
    
    public String getName();
    
    public <T> void addIfNotPresent(ApplicationContextKey<T> key, T value);
    
    public <T> T addWithOverwrite(ApplicationContextKey<T> key, T value);
    
    public <T> void add(ApplicationContextKey<T> key, T value);
    
    public <T> boolean exists(ApplicationContextKey<T> key);

    public <T> T fetch(ApplicationContextKey<T> key);
    
    public <T> T erase(ApplicationContextKey<T> key);
    
    public void clear();
    
    public Set<ApplicationContextKey<?>> keySet();
    
    public boolean equals(ApplicationContext other);
    
    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy);
}
