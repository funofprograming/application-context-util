package io.github.funofprograming.context.impl

import io.github.funofprograming.context.ApplicationContext
import io.github.funofprograming.context.ConcurrentApplicationContext
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

class GlobalContextHolderStrategy : AbstractApplicationContextHolderStrategy() {

    companion object {
        private val GLOBAL_CONTEXT_STORE: MutableMap<String, ConcurrentApplicationContext> = ConcurrentHashMap()
    }

    override fun validateContext(applicationContext: ApplicationContext) {
        super.validateContext(applicationContext)
        assert(applicationContext is ConcurrentApplicationContext) { "applicationContext must be of type ConcurrentApplicationContext as Global holder supports only instances of ConcurrentApplicationContext" }
    }

    /**
     * Check whether context contained in underlying store
     *
     * @param name
     * @return true if context contained in underlying store else false
     */
    override fun contextContainedInStore(name: String?): Boolean = GLOBAL_CONTEXT_STORE.containsKey(name)

    /**
     * Get context from underlying store
     *
     * @param name
     * @return ApplicationContext from underlying store
     */
    override fun getContextFromStore(name: String?): ApplicationContext? = GLOBAL_CONTEXT_STORE.get(name)

    /**
     * Add/Override the ApplicationContext into underlying store
     *
     * @param applicationContext
     */
    override fun setContextInStore(applicationContext: ApplicationContext) { GLOBAL_CONTEXT_STORE.putIfAbsent(applicationContext.getName(), applicationContext as ConcurrentApplicationContext) }

    /**
     * Remove the context from underlying store
     *
     * @param name
     * @return the just removed context
     */
    override fun removeContextFromStore(name: String?): ApplicationContext? = GLOBAL_CONTEXT_STORE.remove(name)

    /**
     * Get type of context supported by this holder strategy
     *
     * @param <T>
     * @return
    </T> */
    override fun <T : ApplicationContext> supportedApplicationContextType(): KClass<T> = ConcurrentApplicationContext::class as KClass<T>

    /**
     * Initialize a new CoroutineScope which can hold ApplicationContext for all coroutines that use this scope
     *
     * @param coroutineScope if any existing coroutineScope passed then a new coroutineScope is created from that param and init for holding ApplicationContext
     *
     * @return
     */
    override suspend fun initCoroutineScope(coroutineScope: CoroutineScope?): CoroutineScope {
        throw IllegalStateException("Global context holder is independent of coroutines and hence no particular scope initialization required")
    }
}