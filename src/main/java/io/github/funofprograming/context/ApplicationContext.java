//package io.github.funofprograming.context;
//
//import java.util.Set;
///**
// * Fundamental interface that defines Application Context basic functionality
// *
// * Most of its methods are typesafe
// *
// * @author Akshay Jain
// *
// */
//public interface ApplicationContext extends Cloneable
//{
//
//    /**
//     * Application context name
//     *
//     * @return Application context name
//     */
//    public String getName();
//
//    /**
//     * Keys permitted for this context. Empty set means all keys permitted.
//     *
//     * Implementations should allow setting up of PermittedKeys at time of context initialization
//     *
//     * @return set of permitted keys
//     */
//    public Set<Key<?>> getPermittedKeys();
//
//    /**
//     * Checks if key is valid for this context.
//     *
//     *
//     * @return key
//     */
//    public boolean isKeyValid(Key<?> key);
//
//    /**
//     * Add value for given key if not already present.
//     *
//     * This is semmantically same as
//     *
//     * {@code
//     *  if(!exists(key))
//     *     add(key, value)
//     * }
//     *
//     *
//     * @param <T>
//     * @param key
//     * @param value
//     */
//    public <T> void addIfNotPresent(Key<T> key, T value);
//
//    /**
//     * Add value for given key even if already present.
//     *
//     *
//     * @param <T>
//     * @param key
//     * @param value
//     * @return Previous value associated with key if any or null
//     */
//    public <T> T addWithOverwrite(Key<T> key, T value);
//
//    /**
//     * Add value for given key.
//     *
//     *
//     * @param <T>
//     * @param key
//     * @param value
//     */
//    public <T> void add(Key<T> key, T value);
//
//    /**
//     * Check if any value or null associated with give key
//     *
//     * @param <T>
//     * @param key
//     * @return true if value or null associated with give key otherwise false
//     */
//    public <T> boolean exists(Key<T> key);
//
//    /**
//     * Fetch value associated with key or null
//     *
//     * @param <T>
//     * @param key
//     * @return value associated with key or null
//     */
//    public <T> T fetch(Key<T> key);
//
//    /**
//     * Erase value associated with key if any
//     *
//     * @param <T>
//     * @param key
//     * @return value associate with key or null
//     */
//    public <T> T erase(Key<T> key);
//
//    /**
//     * Clear the entire context
//     */
//    public void clear();
//
//    /**
//     * Get a set of keys in context
//     *
//     * @return set of keys in context
//     */
//    public Set<Key<?>> keySet();
//
//    /**
//     * Merge other context with this using the given merge strategy
//     *
//     * @param other
//     * @param mergeStrategy
//     */
//    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy);
//}
