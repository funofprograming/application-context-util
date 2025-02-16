package io.github.funofprograming.context

import io.github.funofprograming.context.impl.InvalidContextException
import io.github.funofprograming.context.impl.InvalidKeyException
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

interface ApplicationContextHolderStrategy {
    /**
     * Get type of context supported by this holder strategy
     *
     * @param <T>
     * @return
    </T> */
    fun <T : ApplicationContext> supportedApplicationContextType(): KClass<T>

    /**
     * Get a context for given name. If not available then create one, set in holder and return back.
     *
     * @param name
     * @return
     */
    fun getContext(name: String): ApplicationContext?

    /**
     * Same as getContext but with permittedKeys. If context already exist with different set of keys then this method should throw [InvalidKeyException]
     *
     * @param name
     * @return
     */
    fun getContext(name: String, permittedKeys: Set<Key<*>>?): ApplicationContext?

    /**
     * Set any externally created context into the context holder strategy. If any context with this name already exist then this method should throw [InvalidContextException]
     *
     * @param applicationContext
     */
    fun setContext(applicationContext: ApplicationContext)

    /**
     * Clear context for given name and return back the context
     *
     * @param name
     * @return
     */
    fun clearContext(name: String): ApplicationContext?

    /**
     * Check if context with given name exists in this context holder strategy
     *
     * @param name
     * @return
     */
    fun existsContext(name: String): Boolean

    /**
     * Check if context with given name and permittedKeys exists in this context holder strategy
     *
     * @param name
     * @return
     */
    fun existsContext(name: String, permittedKeys: Set<Key<*>>?): Boolean

    /**
     * Initialize a new CoroutineScope which can hold ApplicationContext for all coroutines that use this scope
     *
     * @param coroutineScope if any existing coroutineScope passed then a new coroutineScope is created from that param and init for holding ApplicationContext
     *
     * @return
     */
    suspend fun initCoroutineScope(coroutineScope: CoroutineScope? = null): CoroutineScope
}