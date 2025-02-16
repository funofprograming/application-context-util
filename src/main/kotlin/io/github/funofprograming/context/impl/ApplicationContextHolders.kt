package io.github.funofprograming.context.impl

import io.github.funofprograming.context.ApplicationContext
import io.github.funofprograming.context.ApplicationContextHolderStrategy
import io.github.funofprograming.context.Key
import kotlinx.coroutines.CoroutineScope

private val threadLocalContextHolderStrategy: ApplicationContextHolderStrategy = ThreadLocalContextHolderStrategy()
private val globalContextHolderStrategy: AbstractApplicationContextHolderStrategy = GlobalContextHolderStrategy()

/**
 * Get a ThreadLocal context for given name. If not available then create one, set in holder and return back.
 *
 * @param name
 * @return
 */
fun getThreadLocalContext(name: String): ApplicationContext? = getContext(name, threadLocalContextHolderStrategy)

/**
 * Same as getThreadLocalContext but with permittedKeys. If context already exist with different set of keys then this method should throw [InvalidKeyException]
 *
 * @param name
 * @return
 */
fun getThreadLocalContext(name: String, permittedKeys: Set<Key<*>>?): ApplicationContext? = getContext(name, permittedKeys, threadLocalContextHolderStrategy)

/**
 * Set any externally created ThreadLocal context into the context holder strategy. If any context with this name already exist then this method should throw [InvalidContextException]
 *
 * @param applicationContext
 */
fun setThreadLocalContext(applicationContext: ApplicationContext) {setContext(applicationContext, threadLocalContextHolderStrategy)}

/**
 * Clear ThreadLocal context for given name and return back the context
 *
 * @param name
 */
fun clearThreadLocalContext(name: String): ApplicationContext? = clearContext(name, threadLocalContextHolderStrategy)

/**
 * Check if ThreadLocal context with given name exists
 *
 * @param name
 * @return
 */
fun existsThreadLocalContext(name: String): Boolean = existsContext(name, threadLocalContextHolderStrategy)

/**
 * Check if ThreadLocal context with given name and permittedKeys exists
 *
 * @param name
 * @return
 */
fun existsThreadLocalContext(name: String, permittedKeys: Set<Key<*>>?): Boolean = existsContext(name, permittedKeys, threadLocalContextHolderStrategy)

/**
 * Initialize a new CoroutineScope which can hold ApplicationContext for all coroutines that use this scope
 *
 * @param coroutineScope if any existing coroutineScope passed then a new coroutineScope is created from that param and init for holding ApplicationContext
 *
 * @return
 */
suspend fun initCoroutineScopeForApplicationContext(coroutineScope: CoroutineScope? = null): CoroutineScope = initCoroutineScope(coroutineScope, threadLocalContextHolderStrategy)

/**
 * Get a Global context for given name. If not available then create one, set in holder and return back.
 *
 * @param name
 * @return
 */
fun getGlobalContext(name: String): ApplicationContext? = getContext(name, globalContextHolderStrategy)

/**
 * Same as getGlobalContext but with permittedKeys. If context already exist with different set of keys then this method should throw [InvalidKeyException]
 *
 * @param name
 * @return
 */
fun getGlobalContext(name: String, permittedKeys: Set<Key<*>>?): ApplicationContext? = getContext(name, permittedKeys, globalContextHolderStrategy)

/**
 * Set any externally created Global context into the context holder strategy. If any context with this name already exist then this method should throw [InvalidContextException]
 *
 * @param applicationContext
 */
fun setGlobalContext(applicationContext: ApplicationContext) {setContext(applicationContext, globalContextHolderStrategy)}

/**
 * Clear Global context for given name and return back the context
 *
 * @param name
 */
fun clearGlobalContext(name: String): ApplicationContext? = clearContext(name, globalContextHolderStrategy)

/**
 * Check if Global context with given name exists
 *
 * @param name
 * @return
 */
fun existsGlobalContext(name: String): Boolean = existsContext(name, globalContextHolderStrategy)

/**
 * Check if Global context with given name and permittedKeys exists
 *
 * @param name
 * @return
 */
fun existsGlobalContext(name: String, permittedKeys: Set<Key<*>>?): Boolean = existsContext(name, permittedKeys, globalContextHolderStrategy)

/**
 * Get a context for given name. If not available then create one, set in holder and return back.
 *
 * @param name
 * @param applicationContextHolderStrategy
 * @return
 */
private fun getContext(
    name: String,
    applicationContextHolderStrategy: ApplicationContextHolderStrategy
): ApplicationContext? {
    return applicationContextHolderStrategy.getContext(name)
}

/**
 * Same as getContext but with permittedKeys. If context already exist with different set of keys then this method should throw [InvalidKeyException]
 *
 * @param name
 * @param applicationContextHolderStrategy
 * @return
 */
private fun getContext(
    name: String,
    permittedKeys: Set<Key<*>>?,
    applicationContextHolderStrategy: ApplicationContextHolderStrategy
): ApplicationContext? {
    return applicationContextHolderStrategy.getContext(name, permittedKeys)
}

/**
 * Set any externally created context into the context holder strategy. If any context with this name already exist then this method should throw [InvalidContextException]
 *
 * @param applicationContext
 * @param applicationContextHolderStrategy
 */
private fun setContext(
    applicationContext: ApplicationContext,
    applicationContextHolderStrategy: ApplicationContextHolderStrategy) = applicationContextHolderStrategy.setContext(applicationContext)

/**
 * Clear context for given name and return back the context
 *
 * @param name
 * @param applicationContextHolderStrategy
 */
private fun clearContext(
    name: String,
    applicationContextHolderStrategy: ApplicationContextHolderStrategy): ApplicationContext? = applicationContextHolderStrategy.clearContext(name)

/**
 * Check if context with given name exists
 *
 * @param name
 * @return
 */
private fun existsContext(
    name: String, applicationContextHolderStrategy:
    ApplicationContextHolderStrategy): Boolean = applicationContextHolderStrategy.existsContext(name)

/**
 * Check if context with given name and permittedKeys exists
 *
 * @param name
 * @return
 */
private fun existsContext(
    name: String,
    permittedKeys: Set<Key<*>>?,
    applicationContextHolderStrategy: ApplicationContextHolderStrategy
): Boolean = applicationContextHolderStrategy.existsContext(name, permittedKeys)

private suspend fun initCoroutineScope(
    coroutineScope: CoroutineScope? = null,
    applicationContextHolderStrategy: ApplicationContextHolderStrategy
): CoroutineScope = applicationContextHolderStrategy.initCoroutineScope(coroutineScope)