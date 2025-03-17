package io.github.funofprograming.context;

import io.github.funofprograming.context.impl.ApplicationContextHoldersKt;
import io.github.funofprograming.context.impl.ConcurrentApplicationContextImpl;
import io.github.funofprograming.context.impl.InvalidContextException;
import io.github.funofprograming.context.impl.InvalidKeyException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestJavaGlobalApplicationContext
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
        contextName = "TestGlobalApplicationContext";
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
        ApplicationContextHoldersKt.clearGlobalContext(contextName);
        executorService.shutdown();
    }

    @Test
    public void testGetGlobalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread11 = "Value T1";
        List<String> valueSetInThread12 = List.of("Value T11", "Value T12");

        CompletableFuture.runAsync(()->{
            ApplicationContext globalContext = ApplicationContextHoldersKt.getGlobalContext(contextName);
            globalContext.add(validKey1, valueSetInThread11);
            globalContext.add(validKey2, valueSetInThread12);
        });

        Thread.sleep(1000);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()->{
            ApplicationContext globalContext = ApplicationContextHoldersKt.getGlobalContext(contextName);
            return globalContext.fetch(validKey1);
        });

        Thread.sleep(1000);

        CompletableFuture<List<String>> future3 = CompletableFuture.supplyAsync(()->{
            ApplicationContext globalContext = ApplicationContextHoldersKt.getGlobalContext(contextName);
            return globalContext.fetch(validKey2);
        });


        Thread.sleep(1000);

        assertEquals(valueSetInThread11, future2.get());
        assertEquals(valueSetInThread12, future3.get());
    }

    @Test
    public void testGetGlobalContextWithPermittedKeys() throws Throwable
    {
        String valueSetInThread11 = "Value T1";
        List<String> valueSetInThread12 = List.of("Value T11", "Value T12");

        CompletableFuture.runAsync(()->{
            ApplicationContext globalContext = ApplicationContextHoldersKt.getGlobalContext(contextName, permittedKeys);
            globalContext.add(validKey1, valueSetInThread11);
            globalContext.add(validKey2, valueSetInThread12);
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
        String valueSetInThread11 = "Value T1";
        List<String> valueSetInThread12 = List.of("Value T11", "Value T12");

        CompletableFuture.runAsync(()->{
            ApplicationContext globalContext = ApplicationContextHoldersKt.getGlobalContext(contextName, permittedKeys);
            globalContext.add(validKey1, valueSetInThread11);
            globalContext.add(validKey2, valueSetInThread12);
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
        String valueSetInThread11 = "Value T1";
        List<String> valueSetInThread12 = List.of("Value T11", "Value T12");

        CompletableFuture.runAsync(()->{
            ApplicationContext globalContext = new ConcurrentApplicationContextImpl(contextName, null);
            ApplicationContextHoldersKt.setGlobalContext(globalContext);
            globalContext.add(validKey1, valueSetInThread11);
            globalContext.add(validKey2, valueSetInThread12);
        });

        Thread.sleep(1000);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()->{
            ApplicationContext globalContext = ApplicationContextHoldersKt.getGlobalContext(contextName);
            return globalContext.fetch(validKey1);
        });

        Thread.sleep(1000);

        CompletableFuture<List<String>> future3 = CompletableFuture.supplyAsync(()->{
            ApplicationContext globalContext = ApplicationContextHoldersKt.getGlobalContext(contextName);
            return globalContext.fetch(validKey2);
        });

        Thread.sleep(1000);

        assertEquals(valueSetInThread11, future2.get());
        assertEquals(valueSetInThread12, future3.get());
    }

    @Test
    public void testClearGlobalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread11 = "Value T1";
        List<String> valueSetInThread12 = List.of("Value T11", "Value T12");

        CompletableFuture.runAsync(()->{
            ApplicationContext globalContext = new ConcurrentApplicationContextImpl(contextName, null);
            ApplicationContextHoldersKt.setGlobalContext(globalContext);
            globalContext.add(validKey1, valueSetInThread11);
            globalContext.add(validKey2, valueSetInThread12);
        });

        Thread.sleep(1000);

        CompletableFuture.runAsync(()->{
            ApplicationContextHoldersKt.clearGlobalContext(contextName);
        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(()->{
            ApplicationContext globalContext = ApplicationContextHoldersKt.getGlobalContext(contextName);
            return globalContext.fetch(validKey1);
        });

        Thread.sleep(1000);

        assertNull(future3.get());
    }

    @Test
    public void testCloneGlobalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread11 = "Value T1";
        List<String> valueSetInThread12 = List.of("Value T11", "Value T12");
        ApplicationContext globalContext = ApplicationContextHoldersKt.getGlobalContext(contextName);
        globalContext.add(validKey1, valueSetInThread11);
        globalContext.add(validKey2, valueSetInThread12);
        ApplicationContext globalContextClone = globalContext.clone(cloneContextName, null);
        assertEquals(valueSetInThread11, globalContextClone.fetch(validKey1));
        assertEquals(valueSetInThread12, globalContextClone.fetch(validKey2));
        ApplicationContextHoldersKt.clearGlobalContext(contextName);

        globalContext = ApplicationContextHoldersKt.getGlobalContext(contextName, permittedKeys);
        globalContext.add(validKey1, valueSetInThread11);
        globalContext.add(validKey2, valueSetInThread12);
        globalContextClone = globalContext.clone(cloneContextName, permittedKeys);
        assertEquals(valueSetInThread11, globalContextClone.fetch(validKey1));
        assertEquals(valueSetInThread12, globalContextClone.fetch(validKey2));
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
