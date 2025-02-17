package io.github.funofprograming.context.impl

import io.github.funofprograming.context.ApplicationContext
import io.github.funofprograming.context.ApplicationContextHolderStrategy
import io.github.funofprograming.context.ConcurrentApplicationContext
import io.github.funofprograming.context.Key
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

abstract class AbstractApplicationContextHolderStrategy : ApplicationContextHolderStrategy {

    override fun getContext(name: String): ApplicationContext? = getContext(name, null)

    override fun getContext(name: String, permittedKeys: Set<Key<*>>?): ApplicationContext? {

        if (!contextContainedInStore(name)) {
            setContext(createApplicationContext(name, permittedKeys))

        } else {
            val permittedKeysExisting = getContextFromStore(name)?.getPermittedKeys()
            if ((permittedKeysExisting?.toSet() ?: emptySet<Key<*>>()) != (permittedKeys?.toSet() ?: emptySet<Key<*>>())) {
                throw InvalidContextException("A context with this name and different set of permittedKeys already exists for this ApplicationContextHolderStrategy")
            }
        }

        return getContextFromStore(name)
    }

    protected fun <T : ApplicationContext> createApplicationContext(name: String, permittedKeys: Set<Key<*>>?): T {

        val supportedApplicationContextType: KClass<T> = supportedApplicationContextType<T>()

        if (ConcurrentApplicationContext::class.isSuperclassOf(supportedApplicationContextType)) {
            return ConcurrentApplicationContextImpl(name, permittedKeys) as T
        } else if (ApplicationContext::class.isSuperclassOf(supportedApplicationContextType)) {
            return ApplicationContextImpl(name, permittedKeys) as T
        }

        throw InvalidContextException("Invalid context type : $supportedApplicationContextType")
    }

    /**
     * {@inheritDoc}
     */
    override fun setContext(applicationContext: ApplicationContext) {
        validateContext(applicationContext)
        setContextInStore(applicationContext)
    }

    /**
     * {@inheritDoc}
     */
    override fun clearContext(name: String): ApplicationContext? = removeContextFromStore(name)

    /**
     * Validate context before setting in store. Child classes can override/add more validations.
     * @param applicationContext
     */
    protected open fun validateContext(applicationContext: ApplicationContext) {

        if (contextContainedInStore(applicationContext.getName())) {
            throw InvalidContextException("Context with same name already exists for this ApplicationContextHolderStrategy")
        }
    }

    /**
     * Check if context with given name and permittedKeys exists in this context holder strategy
     *
     * @param name
     * @return
     */
    override fun existsContext(name: String, permittedKeys: Set<Key<*>>?): Boolean {

        if (contextContainedInStore(name)) {
            val permittedKeysExisting = getContextFromStore(name)?.getPermittedKeys()
            return (permittedKeysExisting?.toSet() ?: emptySet<Key<*>>()) == (permittedKeys?.toSet() ?: emptySet<Key<*>>())
        }

        return false
    }

    /**
     * Check whether context contained in underlying store
     *
     * @param name
     * @return true if context contained in underlying store else false
     */
    protected abstract fun contextContainedInStore(name: String?): Boolean

    /**
     * Get context from underlying store
     *
     * @param name
     * @return ApplicationContext from underlying store
     */
    protected abstract fun getContextFromStore(name: String?): ApplicationContext?

    /**
     * Add/Override the ApplicationContext into underlying store
     *
     * @param applicationContext
     */
    protected abstract fun setContextInStore(applicationContext: ApplicationContext)

    /**
     * Remove the context from underlying store
     *
     * @param name
     * @return the just removed context
     */
    protected abstract fun removeContextFromStore(name: String?): ApplicationContext?
}