package io.github.funofprograming.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.github.funofprograming.context.impl.ApplicationContextHoldersKt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.funofprograming.context.impl.ConcurrentApplicationContextImpl;
import io.github.funofprograming.context.impl.InvalidContextException;
import io.github.funofprograming.context.impl.InvalidKeyException;

public class TestJavaGlobalApplicationContext
{
    private String contextName;
    private Key<String> validKey;
    private Key<String> invalidKey;
    private Set<Key<?>> permittedKeys;
    private ThreadPoolExecutor executorService;

    @BeforeEach
    public void setUp() throws Exception
    {
        contextName = "TestGlobalApplicationContext";
        validKey = Key.Companion.of("ValidKey", String.class);
        invalidKey = Key.Companion.of("InvalidKey", String.class);
        permittedKeys = new HashSet<>(Arrays.asList(validKey));
        executorService = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        executorService.allowCoreThreadTimeOut(true);
    }

    @AfterEach
    public void tearDown() throws Exception
    {
        ApplicationContextHoldersKt.clearGlobalContext(contextName);
        executorService.shutdown();
    }

    @Test
    public void testGetGlobalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread1 = "Value T1";

        CompletableFuture.runAsync(()->{
            ApplicationContext globalContext = ApplicationContextHoldersKt.getGlobalContext(contextName);
            globalContext.add(validKey, valueSetInThread1);
        });

        Thread.sleep(1000);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()->{
            ApplicationContext globalContext = ApplicationContextHoldersKt.getGlobalContext(contextName);
            return globalContext.fetch(validKey);
        });


        Thread.sleep(1000);

        assertEquals(valueSetInThread1, future2.get());
    }

    @Test
    public void testGetGlobalContextWithPermittedKeys() throws Throwable
    {
        String valueSetInThread1 = "Value T1";

        CompletableFuture.runAsync(()->{
            ApplicationContext globalContext = ApplicationContextHoldersKt.getGlobalContext(contextName, permittedKeys);
            globalContext.add(validKey, valueSetInThread1);
        });

        Thread.sleep(1000);

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(()->{
            Set<Key<?>> permittedKeysInvalid = new HashSet<>(Arrays.asList(invalidKey));
            ApplicationContextHoldersKt.getGlobalContext(contextName, permittedKeysInvalid);
        });

        Thread.sleep(1000);

        assertEquals(true, future2.isCompletedExceptionally());
        assertThrows(InvalidContextException.class, ()->rethrowCause(future2));
    }

    @Test
    public void testGetGlobalContextWithPermittedKeysInvalidKey() throws Throwable
    {
        String valueSetInThread1 = "Value T1";

        CompletableFuture.runAsync(()->{
            ApplicationContext globalContext = ApplicationContextHoldersKt.getGlobalContext(contextName, permittedKeys);
            globalContext.add(validKey, valueSetInThread1);
        });

        Thread.sleep(1000);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()->{
            ApplicationContext globalContext = ApplicationContextHoldersKt.getGlobalContext(contextName, permittedKeys);
            return globalContext.fetch(invalidKey);
        });

        Thread.sleep(1000);

        assertEquals(true, future2.isCompletedExceptionally());
        assertThrows(InvalidKeyException.class, ()->rethrowCause(future2));
    }

    @Test
    public void testSetGlobalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread1 = "Value T1";

        CompletableFuture.runAsync(()->{
            ApplicationContext globalContext = new ConcurrentApplicationContextImpl(contextName, null);
            ApplicationContextHoldersKt.setGlobalContext(globalContext);
            globalContext.add(validKey, valueSetInThread1);
        });

        Thread.sleep(1000);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()->{
            ApplicationContext globalContext = ApplicationContextHoldersKt.getGlobalContext(contextName);
            return globalContext.fetch(validKey);
        });

        Thread.sleep(1000);

        assertEquals(valueSetInThread1, future2.get());
    }

    @Test
    public void testClearGlobalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread1 = "Value T1";

        CompletableFuture.runAsync(()->{
            ApplicationContext globalContext = new ConcurrentApplicationContextImpl(contextName, null);
            ApplicationContextHoldersKt.setGlobalContext(globalContext);
            globalContext.add(validKey, valueSetInThread1);
        });

        Thread.sleep(1000);

        CompletableFuture.runAsync(()->{
            ApplicationContextHoldersKt.clearGlobalContext(contextName);
        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(()->{
            ApplicationContext globalContext = ApplicationContextHoldersKt.getGlobalContext(contextName);
            return globalContext.fetch(validKey);
        });

        Thread.sleep(1000);

        assertNull(future3.get());
    }

    private void rethrowCause(Future future) throws Throwable
    {
        try
        {
            future.get();
        }
        catch(ExecutionException | InterruptedException ee)
        {
            throw ee.getCause();
        }
    }
}
