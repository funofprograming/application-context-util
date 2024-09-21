package io.github.funofprograming.context;

/**
 * Thread safe version of {@linkplain ApplicationContext}
 * 
 * @author Akshay Jain
 *
 */
public interface ConcurrentApplicationContext extends ApplicationContext
{
    /**
     * Same as addIfNotPresent in {@linkplain ApplicationContext} with additional timeout parameter 
     * for wait time in case of multiple threads blocking on this method
     * 
     * @param <T>
     * @param key
     * @param value
     * @param timeout in millis
     */
    public <T> void addIfNotPresent(Key<T> key, T value, Long timeout);
    
    /**
     * Same as addWithOverwrite in {@linkplain ApplicationContext} with additional timeout parameter 
     * for wait time in case of multiple threads blocking on this method
     * 
     * @param <T>
     * @param key
     * @param value
     * @param timeout in millis
     * @return
     */
    public <T> T addWithOverwrite(Key<T> key, T value, Long timeout);
    
    /**
     * Same as add in {@linkplain ApplicationContext} with additional timeout parameter 
     * for wait time in case of multiple threads blocking on this method
     * 
     * @param <T>
     * @param key
     * @param value
     * @param timeout in millis
     */
    public <T> void add(Key<T> key, T value, Long timeout);
    
    /**
     * Same as exists in {@linkplain ApplicationContext} with additional timeout parameter 
     * for wait time in case of multiple threads blocking on this method
     * 
     * @param <T>
     * @param key
     * @param timeout in millis
     * @return
     */
    public <T> boolean exists(Key<T> key, Long timeout);

    /**
     * Same as fetch in {@linkplain ApplicationContext} with additional timeout parameter 
     * for wait time in case of multiple threads blocking on this method
     * 
     * @param <T>
     * @param key
     * @param timeout in millis
     * @return
     */
    public <T> T fetch(Key<T> key, Long timeout);
    
    /**
     * Same as erase in {@linkplain ApplicationContext} with additional timeout parameter 
     * for wait time in case of multiple threads blocking on this method
     * 
     * @param <T>
     * @param key
     * @param timeout in millis
     * @return
     */
    public <T> T erase(Key<T> key, Long timeout);
    
    /**
     * Same as clear in {@linkplain ApplicationContext} with additional timeout parameter 
     * for wait time in case of multiple threads blocking on this method
     * 
     * @param timeout in millis
     */
    public void clear(Long timeout);
    
    /**
     * Same as merge in {@linkplain ApplicationContext} with additional timeout parameter 
     * for wait time in case of multiple threads blocking on this method
     * 
     * @param other
     * @param mergeStrategy
     * @param timeout in millis
     */
    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy, Long timeout);
}
