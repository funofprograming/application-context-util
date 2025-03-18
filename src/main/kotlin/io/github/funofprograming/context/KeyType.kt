package io.github.funofprograming.context

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.full.*
/**
 * The purpose of this class is to enable capturing and passing a generic
 * [Type]. In order to capture the generic type and retain it at runtime,
 * you need to create a subclass (ideally as anonymous inline class) as follows:
 *
 * <pre class="code">
 * KeyType&lt;List&lt;String&gt;&gt; typeRef = new KeyType&lt;List&lt;String&gt;&gt;() {};
</pre> *
 *
 *
 * The resulting `typeRef` instance can then be used to obtain a [Type]
 * instance that carries the captured parameterized type information at runtime.
 * For more information on "super type tokens" see the link to Neal Gafter's blog post.
 *
 * This is inspired by Spring project
 * @see [KeyType](https://github.com/spring-projects/spring-framework/blob/main/spring-core/src/main/java/org/springframework/core/ParameterizedTypeReference.java)
 */
abstract class KeyType<T> {
    private val type: Type

    protected constructor() {
        val parameterizedTypeReferenceSubclass = findParameterizedTypeReferenceSubclass(javaClass)
        val type = parameterizedTypeReferenceSubclass.genericSuperclass
        assert(type is ParameterizedType) { "Type must be a parameterized type" }
        val parameterizedType = type as ParameterizedType
        val actualTypeArguments = parameterizedType.actualTypeArguments
        assert(actualTypeArguments.size == 1) { "Number of type arguments must be 1" }
        this.type = actualTypeArguments[0]
    }

    protected constructor(type: Type) {
        this.type = type
    }

    /**
     * {@inheritDoc}
     */
    override fun toString(): String {
        return type.typeName
    }

    private fun findParameterizedTypeReferenceSubclass(child: Class<*>): Class<*> {
        val parent = child.superclass
        check(Any::class.java != parent) { "Expected KeyType superclass" }
        if (KeyType::class.java == parent) {
            return child
        } else {
            return findParameterizedTypeReferenceSubclass(parent)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KeyType<*>

        return type == other.type
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }

    companion object {
        /**
         * Build a `KeyType` wrapping the given type.
         *
         * @param type
         * a generic type (possibly obtained via reflection, e.g. from
         * [java.lang.reflect.Method.getGenericReturnType])
         * @return a corresponding reference which may be passed into
         * `KeyType`-accepting methods
         * @since 4.3.12
         */
        fun <T> of(type: Type): KeyType<T> {
            return object : KeyType<T>(type) {}
        }

        inline fun <reified T> of(): KeyType<T> {
            return object : KeyType<T>() {}
        }
    }
}
