//package io.github.funofprograming.context.impl;
//
//import java.util.Objects;
//import java.util.Set;
//
//import io.github.funofprograming.context.Key;
//import io.github.funofprograming.context.ApplicationContextMergeStrategy;
//
///**
// * Abstract implementation of {@linkplain ApplicationContextMergeStrategy}
// *
// * @author Akshay Jain
// *
// */
//public abstract class AbstractApplicationContextMergeStrategy implements ApplicationContextMergeStrategy {
//
//
//    protected final Set<Key<?>> keys;
//
//    /**
//     * Initialize with a set of keys for whom this strategy is to be applied. If null set passed then strategy is applicable for all keys
//     *
//     * @param keys
//     */
//    protected AbstractApplicationContextMergeStrategy(Set<Key<?>> keys)
//    {
//        this.keys = keys;
//    }
//
//    protected boolean keySetAvailable()
//    {
//        return Objects.nonNull(keys);
//    }
//
//    protected boolean keyAvailable(Key<?> key)
//    {
//        return keySetAvailable()?keys.contains(key):false;
//    }
//
//}
