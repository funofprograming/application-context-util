package io.github.funofprograming.context.impl

import io.github.funofprograming.context.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ThreadContextElement
import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.*
import kotlin.reflect.KClass

class ThreadLocalContextHolderStrategy: AbstractApplicationContextHolderStrategy() {

    companion object {
        private val LOCAL_CONTEXT_STORE: ThreadLocal<MutableMap<String, ApplicationContext>> = ThreadLocal.withInitial() { mutableMapOf() }
    }

    /**
     * Check whether context contained in underlying store
     *
     * @param name
     * @return true if context contained in underlying store else false
     */
    override fun contextContainedInStore(name: String?): Boolean = LOCAL_CONTEXT_STORE.get().containsKey(name)

    /**
     * Get context from underlying store
     *
     * @param name
     * @return ApplicationContext from underlying store
     */
    override fun getContextFromStore(name: String?): ApplicationContext? = LOCAL_CONTEXT_STORE.get()[name]

    /**
     * Add/Override the ApplicationContext into underlying store
     *
     * @param applicationContext
     */
    override fun setContextInStore(applicationContext: ApplicationContext) {LOCAL_CONTEXT_STORE.get().putIfAbsent(applicationContext.getName(), applicationContext)}

    /**
     * Remove the context from underlying store
     *
     * @param name
     * @return the just removed context
     */
    override fun removeContextFromStore(name: String?): ApplicationContext? = LOCAL_CONTEXT_STORE.get().remove(name)

    /**
     * Get type of context supported by this holder strategy
     *
     * @param <T>
     * @return
    </T> */
    override fun <T : ApplicationContext> supportedApplicationContextType(): KClass<T> = ApplicationContext::class as KClass<T>

    /**
     * Initialize a new CoroutineScope which can hold ApplicationContext for all coroutines that use this scope
     *
     * @param coroutineScope if any existing coroutineScope passed then a new coroutineScope is created from that param and init for holding ApplicationContext
     *
     * @return
     */
    override suspend fun initCoroutineScope(coroutineScope: CoroutineScope?): CoroutineScope {

        val threadLocal = LOCAL_CONTEXT_STORE.asContextElement(value = mutableMapOf())
        return if(coroutineScope != null) CoroutineScope(coroutineScope.coroutineContext+threadLocal) else CoroutineScope(threadLocal)
    }

}