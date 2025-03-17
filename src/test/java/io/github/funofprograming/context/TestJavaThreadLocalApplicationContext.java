package io.github.funofprograming.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.github.funofprograming.context.impl.ApplicationContextHoldersKt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.funofprograming.context.impl.ApplicationContextImpl;
import io.github.funofprograming.context.impl.InvalidContextException;
import io.github.funofprograming.context.impl.InvalidKeyException;

public class TestJavaThreadLocalApplicationContext
{
    private String contextName;
    private String cloneContextName;
    private Key<String> validKey1;
    private Key<List<String>> validKey2;
    private Key<String> invalidKey;
    private Set<Key<?>> permittedKeys;
    private ThreadPoolExecutor executorService;
    
    @BeforeEach
    public void setUp() throws Exception
    {
        contextName = "TestThreadLocalApplicationContext";
        cloneContextName = "TestCloneGlobalApplicationContext";
        validKey1 = Key.Companion.of("ValidKey1", String.class);
        validKey2 = Key.Companion.of("ValidKey2", new KeyType<List<String>>() {});
        invalidKey = Key.Companion.of("InvalidKey", String.class);
        permittedKeys = new HashSet<>(Arrays.asList(validKey1, validKey2));
        executorService = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        executorService.allowCoreThreadTimeOut(true);
    }

    @AfterEach
    public void tearDown() throws Exception
    {
        ApplicationContextHoldersKt.clearThreadLocalContext(contextName);
        executorService.shutdown();
    }

    @Test
    public void testGetThreadLocalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread11 = "Value T1";
        List<String> valueSetInThread12 = List.of("Value T11", "Value T12");
        
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(()->{
            ApplicationContext localContext = ApplicationContextHoldersKt.getThreadLocalContext(contextName);
            localContext.add(validKey1, valueSetInThread11);
            return ApplicationContextHoldersKt.getThreadLocalContext(contextName).fetch(validKey1);
        }, executorService);
        
        CompletableFuture<List<String>> future2 = CompletableFuture.supplyAsync(()->{
            ApplicationContext localContext = ApplicationContextHoldersKt.getThreadLocalContext(contextName);
            localContext.add(validKey2, valueSetInThread12);
            return ApplicationContextHoldersKt.getThreadLocalContext(contextName).fetch(validKey2);
        }, executorService);

        Thread.sleep(1000);
        
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(()->{
            ApplicationContext localContext = ApplicationContextHoldersKt.getThreadLocalContext(contextName);
            return localContext.fetch(validKey1);
        }, executorService);

        
        assertNull(future3.get());
        assertEquals(valueSetInThread11, future1.get());
        assertEquals(valueSetInThread12, future2.get());
    }

    @Test
    public void testGetThreadLocalContextWithPermittedKeys() throws Throwable
    {
        String valueSetInThread11 = "Value T1";
        List<String> valueSetInThread12 = List.of("Value T11", "Value T12");
        ApplicationContext localContext = ApplicationContextHoldersKt.getThreadLocalContext(contextName, permittedKeys);
        localContext.add(validKey1, valueSetInThread11);
        localContext.add(validKey2, valueSetInThread12);
        Set<Key<?>> permittedKeysInvalid = new HashSet<>(Arrays.asList(invalidKey));
        assertThrows(InvalidContextException.class, ()->ApplicationContextHoldersKt.getThreadLocalContext(contextName, permittedKeysInvalid));
    }
    
    @Test
    public void testGetThreadLocalContextWithPermittedKeysInvalidKey() throws Throwable
    {
        String valueSetInThread11 = "Value T1";
        List<String> valueSetInThread12 = List.of("Value T11", "Value T12");
        ApplicationContext localContext = ApplicationContextHoldersKt.getThreadLocalContext(contextName, permittedKeys);
        localContext.add(validKey1, valueSetInThread11);
        localContext.add(validKey2, valueSetInThread12);
        assertThrows(InvalidKeyException.class, ()->ApplicationContextHoldersKt.getThreadLocalContext(contextName, permittedKeys).fetch(invalidKey));
    }

    @Test
    public void testSetThreadLocalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread11 = "Value T1";
        List<String> valueSetInThread12 = List.of("Value T11", "Value T12");
        
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(()->{
            ApplicationContext localContext = new ApplicationContextImpl(contextName, null);
            ApplicationContextHoldersKt.setThreadLocalContext(localContext);
            localContext.add(validKey1, valueSetInThread11);
            localContext.add(validKey2, valueSetInThread12);
            return ApplicationContextHoldersKt.getThreadLocalContext(contextName).fetch(validKey1);
        }, executorService);
        
        Thread.sleep(1000);
        
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()->{
            ApplicationContext localContext = ApplicationContextHoldersKt.getThreadLocalContext(contextName);
            return localContext.fetch(validKey1);
        }, executorService);        
        
        assertNull(future2.get());
        assertEquals(valueSetInThread11, future1.get());
    }

    @Test
    public void testClearThreadLocalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread11 = "Value T1";
        List<String> valueSetInThread12 = List.of("Value T11", "Value T12");
        
        ApplicationContext localContext = new ApplicationContextImpl(contextName, null);
        ApplicationContextHoldersKt.setThreadLocalContext(localContext);
        localContext.add(validKey1, valueSetInThread11);
        localContext.add(validKey2, valueSetInThread12);
        ApplicationContextHoldersKt.clearThreadLocalContext(contextName);
        String val = ApplicationContextHoldersKt.getThreadLocalContext(contextName).fetch(validKey1);
        
        assertNull(val);
    }

    @Test
    public void testCloneThreadLocalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread11 = "Value T1";
        List<String> valueSetInThread12 = List.of("Value T11", "Value T12");
        ApplicationContext threadLocalContext = ApplicationContextHoldersKt.getThreadLocalContext(contextName);
        threadLocalContext.add(validKey1, valueSetInThread11);
        threadLocalContext.add(validKey2, valueSetInThread12);
        ApplicationContext threadLocalContextClone = threadLocalContext.clone(cloneContextName, null);
        assertEquals(valueSetInThread11, threadLocalContextClone.fetch(validKey1));
        assertEquals(valueSetInThread12, threadLocalContextClone.fetch(validKey2));
        ApplicationContextHoldersKt.clearThreadLocalContext(contextName);

        threadLocalContext = ApplicationContextHoldersKt.getThreadLocalContext(contextName, permittedKeys);
        threadLocalContext.add(validKey1, valueSetInThread11);
        threadLocalContext.add(validKey2, valueSetInThread12);
        threadLocalContextClone = threadLocalContext.clone(cloneContextName, permittedKeys);
        assertEquals(valueSetInThread11, threadLocalContextClone.fetch(validKey1));
        assertEquals(valueSetInThread12, threadLocalContextClone.fetch(validKey2));
    }
}
