package fop.context.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.fop.context.ApplicationContext;
import io.fop.context.ApplicationContextKey;
import io.fop.context.ParameterizedTypeReference;
import io.fop.context.impl.ApplicationContextHolder;
import io.fop.context.impl.ConcurrentApplicationContextImpl;
import io.fop.context.impl.InvalidContextException;
import io.fop.context.impl.InvalidKeyException;

public class TestGlobalApplicationContext
{
    private String contextName;
    private ApplicationContextKey<String> validKey;
    private ApplicationContextKey<String> invalidKey;
    private Set<ApplicationContextKey<?>> permittedKeys;
    private ThreadPoolExecutor executorService;
    
    @Before
    public void setUp() throws Exception
    {
        contextName = "TestGlobalApplicationContext";
        validKey = ApplicationContextKey.of("ValidKey", new ParameterizedTypeReference<String>() {});
        invalidKey = ApplicationContextKey.of("InvalidKey", new ParameterizedTypeReference<String>() {});
        permittedKeys = new HashSet<>(Arrays.asList(validKey));
        executorService = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        executorService.allowCoreThreadTimeOut(true);
    }

    @After
    public void tearDown() throws Exception
    {
        ApplicationContextHolder.clearGlobalContext(contextName);
        executorService.shutdown();
    }

    @Test
    public void testGetGlobalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread1 = "Value T1";
        
        CompletableFuture.runAsync(()->{
            ApplicationContext globalContext = ApplicationContextHolder.getGlobalContext(contextName);
            globalContext.add(validKey, valueSetInThread1);
        });
        
        Thread.sleep(1000);
        
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()->{
            ApplicationContext globalContext = ApplicationContextHolder.getGlobalContext(contextName);
            return globalContext.fetch(validKey);
        });        
        
        
        Thread.sleep(1000);
        
        assertEquals(valueSetInThread1, future2.get());
    }

    @Test(expected = InvalidContextException.class)
    public void testGetGlobalContextWithPermittedKeys() throws Throwable
    {
        String valueSetInThread1 = "Value T1";
        
        CompletableFuture.runAsync(()->{
            ApplicationContext globalContext = ApplicationContextHolder.getGlobalContext(contextName, permittedKeys);
            globalContext.add(validKey, valueSetInThread1);
        });
        
        Thread.sleep(1000);
        
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(()->{
            Set<ApplicationContextKey<?>> permittedKeysInvalid = new HashSet<>(Arrays.asList(invalidKey));
            ApplicationContextHolder.getGlobalContext(contextName, permittedKeysInvalid);
        });        
        
        Thread.sleep(1000);
        
        assertEquals(true, future2.isCompletedExceptionally());
        
        try 
        {
            future2.get();
        }
        catch(ExecutionException ee) 
        {
            throw ee.getCause();
        }
    }
    
    @Test(expected = InvalidKeyException.class)
    public void testGetGlobalContextWithPermittedKeysInvalidKey() throws Throwable
    {
        String valueSetInThread1 = "Value T1";
        
        CompletableFuture.runAsync(()->{
            ApplicationContext globalContext = ApplicationContextHolder.getGlobalContext(contextName, permittedKeys);
            globalContext.add(validKey, valueSetInThread1);
        });
        
        Thread.sleep(1000);
        
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()->{
            ApplicationContext globalContext = ApplicationContextHolder.getGlobalContext(contextName, permittedKeys);
            return globalContext.fetch(invalidKey);
        });        
        
        Thread.sleep(1000);
        
        assertEquals(true, future2.isCompletedExceptionally());
        
        try 
        {
            future2.get();
        }
        catch(ExecutionException ee) 
        {
            throw ee.getCause();
        }
    }

    @Test
    public void testSetGlobalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread1 = "Value T1";
        
        CompletableFuture.runAsync(()->{
            ApplicationContext globalContext = new ConcurrentApplicationContextImpl(contextName);
            ApplicationContextHolder.setGlobalContext(globalContext);
            globalContext.add(validKey, valueSetInThread1);
        });
        
        Thread.sleep(1000);
        
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()->{
            ApplicationContext globalContext = ApplicationContextHolder.getGlobalContext(contextName);
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
            ApplicationContext globalContext = new ConcurrentApplicationContextImpl(contextName);
            ApplicationContextHolder.setGlobalContext(globalContext);
            globalContext.add(validKey, valueSetInThread1);
        });
        
        Thread.sleep(1000);
        
        CompletableFuture.runAsync(()->{
            ApplicationContextHolder.clearGlobalContext(contextName);
        });
        
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(()->{
            ApplicationContext globalContext = ApplicationContextHolder.getGlobalContext(contextName);
            return globalContext.fetch(validKey);
        });        
        
        Thread.sleep(1000);
        
        assertNull(future3.get());
    }
}
