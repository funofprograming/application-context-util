//package io.github.funofprograming.context;
//
//import java.util.Set;
//
//import io.github.funofprograming.context.impl.InvalidContextException;
//import io.github.funofprograming.context.impl.InvalidKeyException;
//
//public interface ApplicationContextHolderStrategy
//{
//
//    /**
//     * Get type of context supported by this holder strategy
//     *
//     * @param <T>
//     * @return
//     */
//    public <T extends ApplicationContext> Class<T> supportedApplicationContextType();
//
//    /**
//     * Get a context for given name. If not available then create one, set in holder and return back.
//     *
//     * @param name
//     * @return
//     */
//    public ApplicationContext getContext(String name);
//
//    /**
//     * Same as getContext but with permittedKeys. If context already exist with different set of keys then this method should throw {@link InvalidKeyException}
//     *
//     * @param name
//     * @return
//     */
//    public ApplicationContext getContext(String name, Set<Key<?>> permittedKeys);
//
//    /**
//     * Set any externally created context into the context holder strategy. If any context with this name already exist then this method should throw {@link InvalidContextException}
//     *
//     * @param applicationContext
//     */
//    public void setContext(ApplicationContext applicationContext);
//
//    /**
//     * Clear context for given name and return back the context
//     *
//     * @param name
//     * @return
//     */
//    public ApplicationContext clearContext(String name);
//
//    /**
//     * Check if context with given name exists in this context holder strategy
//     *
//     * @param name
//     * @return
//     */
//    public boolean existsContext(String name);
//
//    /**
//     * Check if context with given name and permittedKeys exists in this context holder strategy
//     *
//     * @param name
//     * @return
//     */
//    public boolean existsContext(String name, Set<Key<?>> permittedKeys);
//}
