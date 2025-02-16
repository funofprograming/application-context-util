package io.github.funofprograming.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashSet;
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
    private Key<String> validKey;
    private Key<String> invalidKey;
    private Set<Key<?>> permittedKeys;
    private ThreadPoolExecutor executorService;
    
    @BeforeEach
    public void setUp() throws Exception
    {
        contextName = "TestThreadLocalApplicationContext";
        validKey = Key.Companion.of("ValidKey", String.class);
        invalidKey = Key.Companion.of("InvalidKey", String.class);
        permittedKeys = new HashSet<>(Arrays.asList(validKey));
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
        String valueSetInThread1 = "Value T1";
        
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(()->{
            ApplicationContext localContext = ApplicationContextHoldersKt.getThreadLocalContext(contextName);
            localContext.add(validKey, valueSetInThread1);
            return ApplicationContextHoldersKt.getThreadLocalContext(contextName).fetch(validKey);
        }, executorService);
        
        Thread.sleep(1000);
        
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()->{
            ApplicationContext localContext = ApplicationContextHoldersKt.getThreadLocalContext(contextName);
            return localContext.fetch(validKey);
        }, executorService);        
        
        assertNull(future2.get());
        assertEquals(valueSetInThread1, future1.get());
    }

    @Test
    public void testGetThreadLocalContextWithPermittedKeys() throws Throwable
    {
        String valueSetInThread1 = "Value T1";
        ApplicationContext localContext = ApplicationContextHoldersKt.getThreadLocalContext(contextName, permittedKeys);
        localContext.add(validKey, valueSetInThread1);
        Set<Key<?>> permittedKeysInvalid = new HashSet<>(Arrays.asList(invalidKey));
        assertThrows(InvalidContextException.class, ()->ApplicationContextHoldersKt.getThreadLocalContext(contextName, permittedKeysInvalid));
    }
    
    @Test
    public void testGetThreadLocalContextWithPermittedKeysInvalidKey() throws Throwable
    {
        String valueSetInThread1 = "Value T1";
        ApplicationContext localContext = ApplicationContextHoldersKt.getThreadLocalContext(contextName, permittedKeys);
        localContext.add(validKey, valueSetInThread1);
        assertThrows(InvalidKeyException.class, ()->ApplicationContextHoldersKt.getThreadLocalContext(contextName, permittedKeys).fetch(invalidKey));
    }

    @Test
    public void testSetThreadLocalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread1 = "Value T1";
        
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(()->{
            ApplicationContext localContext = new ApplicationContextImpl(contextName, null);
            ApplicationContextHoldersKt.setThreadLocalContext(localContext);
            localContext.add(validKey, valueSetInThread1);
            return ApplicationContextHoldersKt.getThreadLocalContext(contextName).fetch(validKey);
        }, executorService);
        
        Thread.sleep(1000);
        
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()->{
            ApplicationContext localContext = ApplicationContextHoldersKt.getThreadLocalContext(contextName);
            return localContext.fetch(validKey);
        }, executorService);        
        
        assertNull(future2.get());
        assertEquals(valueSetInThread1, future1.get());
    }

    @Test
    public void testClearThreadLocalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread1 = "Value T1";
        
        ApplicationContext localContext = new ApplicationContextImpl(contextName, null);
        ApplicationContextHoldersKt.setThreadLocalContext(localContext);
        localContext.add(validKey, valueSetInThread1);
        ApplicationContextHoldersKt.clearThreadLocalContext(contextName);
        String val = ApplicationContextHoldersKt.getThreadLocalContext(contextName).fetch(validKey);
        
        assertNull(val);
    }
}
