//package io.github.funofprograming.context.impl;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import io.github.funofprograming.context.ApplicationContext;
//import io.github.funofprograming.context.ConcurrentApplicationContext;
//
//public final class InheritableThreadLocalContextHolderStrategy extends ThreadLocalContextHolderStrategy
//{
//    private static final ThreadLocal<Map<String, ApplicationContext>> LOCAL_CONTEXT_STORE = new InheritableThreadLocal<>() {
//
//        protected Map<String, ApplicationContext> initialValue() {
//            return new HashMap<>();
//        }
//    };
//
//    protected ThreadLocal<Map<String, ApplicationContext>> getThreadLocalContextStore()
//    {
//        return LOCAL_CONTEXT_STORE;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    public <T extends ApplicationContext> Class<T> supportedApplicationContextType()
//    {
//	return (Class<T>) ConcurrentApplicationContext.class;
//    }
//}
