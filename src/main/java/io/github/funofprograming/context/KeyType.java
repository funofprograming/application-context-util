//package io.github.funofprograming.context;
//
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//
//import lombok.EqualsAndHashCode;
//
///**
// * The purpose of this class is to enable capturing and passing a generic
// * {@link Type}. In order to capture the generic type and retain it at runtime,
// * you need to create a subclass (ideally as anonymous inline class) as follows:
// *
// * <pre class="code">
// * KeyType&lt;List&lt;String&gt;&gt; typeRef = new KeyType&lt;List&lt;String&gt;&gt;() {};
// * </pre>
// *
// * <p>The resulting {@code typeRef} instance can then be used to obtain a {@link Type}
// * instance that carries the captured parameterized type information at runtime.
// * For more information on "super type tokens" see the link to Neal Gafter's blog post.
// *
// * This is inspired by Spring project
// * @see <a href="https://github.com/spring-projects/spring-framework/blob/main/spring-core/src/main/java/org/springframework/core/ParameterizedTypeReference.java">KeyType</a>
// */
//@EqualsAndHashCode
//public abstract class KeyType<T>
//{
//    private final Type type;
//
//    protected KeyType()
//    {
//        Class<?> parameterizedTypeReferenceSubclass = findParameterizedTypeReferenceSubclass(getClass());
//        Type type = parameterizedTypeReferenceSubclass.getGenericSuperclass();
//        assert type instanceof ParameterizedType : "Type must be a parameterized type";
//        ParameterizedType parameterizedType = (ParameterizedType) type;
//        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
//        assert actualTypeArguments.length == 1 : "Number of type arguments must be 1";
//        this.type = actualTypeArguments[0];
//    }
//
//    protected KeyType(Type type)
//    {
//        this.type = type;
//    }
//
//    /**
//     * Build a {@code KeyType} wrapping the given type.
//     *
//     * @param type
//     *                 a generic type (possibly obtained via reflection, e.g. from
//     *                 {@link java.lang.reflect.Method#getGenericReturnType()})
//     * @return a corresponding reference which may be passed into
//     *         {@code KeyType}-accepting methods
//     * @since 4.3.12
//     */
//    public static <T> KeyType<T> of(Type type)
//    {
//        return new KeyType<T>(type)
//        {
//        };
//    }
//
//    private static Class<?> findParameterizedTypeReferenceSubclass(Class<?> child)
//    {
//        Class<?> parent = child.getSuperclass();
//        if (Object.class == parent)
//        {
//            throw new IllegalStateException("Expected KeyType superclass");
//        }
//        else if (KeyType.class == parent)
//        {
//            return child;
//        }
//        else
//        {
//            return findParameterizedTypeReferenceSubclass(parent);
//        }
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public String toString()
//    {
//        return type.getTypeName();
//    }
//}
