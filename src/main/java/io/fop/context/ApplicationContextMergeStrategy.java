package io.fop.context;

/**
 * Merge strategy for merging any two context
 * 
 * @author Akshay Jain
 *
 */
public interface ApplicationContextMergeStrategy 
{
    /**
     * To merge for a given key
     * 
     * @param <T>
     * @param key
     * @param oldValue typically the value from context which is being merged into
     * @param newValue typically the value from context which is merged
     * @return
     */
    public <T> T merge(ApplicationContextKey<T> key, T oldValue, T newValue); 
}
