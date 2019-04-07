package fop.context;

import java.util.Set;
/**
 * Fundamental interface that defines Application Context basic functionality
 * 
 * Most of its methods are typesafe
 * 
 * @author Akshay Jain 
 *
 */
public interface ApplicationContext extends Cloneable
{
    /**
     * Scope of context
     * 
     * @return {@linkplain ApplicationContextTypes.Scope}
     */
    public ApplicationContextTypes.Scope scope();
    
    /**
     * Gate of context
     * 
     * @return {@linkplain ApplicationContextTypes.Gate}
     */
    public ApplicationContextTypes.Gate gate();
    
    /**
     * Application context name
     * 
     * @return Application context name
     */
    public String getName();
    
    /**
     * Add value for given key if not already present.
     * 
     * This is semmantically same as 
     * 
     * {@code
     *  if(!exists(key))
     *     add(key, value)
     * }
     * 
     * 
     * @param <T>
     * @param key
     * @param value
     */
    public <T> void addIfNotPresent(ApplicationContextKey<T> key, T value);
    
    /**
     * Add value for given key even if already present.
     * 
     * 
     * @param <T>
     * @param key
     * @param value
     * @return Previous value associated with key if any or null
     */
    public <T> T addWithOverwrite(ApplicationContextKey<T> key, T value);
    
    /**
     * Add value for given key.
     * 
     * 
     * @param <T>
     * @param key
     * @param value
     */
    public <T> void add(ApplicationContextKey<T> key, T value);

    /**
     * Check if any value or null associated with give key
     * 
     * @param <T>
     * @param key
     * @return true if value or null associated with give key otherwise false
     */
    public <T> boolean exists(ApplicationContextKey<T> key);

    /**
     * Fetch value associated with key or null
     * 
     * @param <T>
     * @param key
     * @return value associated with key or null
     */
    public <T> T fetch(ApplicationContextKey<T> key);
    
    /**
     * Erase value associated with key if any
     * 
     * @param <T>
     * @param key
     * @return value associate with key or null
     */
    public <T> T erase(ApplicationContextKey<T> key);
    
    /**
     * Clear the entire context
     */
    public void clear();
    
    /**
     * Get a set of keys in context
     * 
     * @return set of keys in context
     */
    public Set<ApplicationContextKey<?>> keySet();
    
    /**
     * Check equality of this context with the other one
     * 
     * @param other
     * @return true if other context is equal to this otherwise false
     */
    public boolean equals(ApplicationContext other);
    
    /**
     * Merge other context with this using the given merge strategy
     * 
     * @param other
     * @param mergeStrategy
     */
    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy);
}
