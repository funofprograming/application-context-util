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
import io.fop.context.impl.ApplicationContextImpl;
import io.fop.context.impl.InvalidContextException;
import io.fop.context.impl.InvalidKeyException;

public class TestThreadLocalApplicationContext
{
    private String contextName;
    private ApplicationContextKey<String> validKey;
    private ApplicationContextKey<String> invalidKey;
    private Set<ApplicationContextKey<?>> permittedKeys;
    private ThreadPoolExecutor executorService;
    
    @Before
    public void setUp() throws Exception
    {
        contextName = "TestThreadLocalApplicationContext";
        validKey = ApplicationContextKey.of("ValidKey", new ParameterizedTypeReference<String>() {});
        invalidKey = ApplicationContextKey.of("InvalidKey", new ParameterizedTypeReference<String>() {});
        permittedKeys = new HashSet<>(Arrays.asList(validKey));
        executorService = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        executorService.allowCoreThreadTimeOut(true);
    }

    @After
    public void tearDown() throws Exception
    {
        ApplicationContextHolder.clearThreadLocalContext(contextName);
        executorService.shutdown();
    }

    @Test
    public void testGetThreadLocalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread1 = "Value T1";
        
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(()->{
            ApplicationContext localContext = ApplicationContextHolder.getThreadLocalContext(contextName);
            localContext.add(validKey, valueSetInThread1);
            return ApplicationContextHolder.getThreadLocalContext(contextName).fetch(validKey);
        }, executorService);
        
        Thread.sleep(1000);
        
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()->{
            ApplicationContext localContext = ApplicationContextHolder.getThreadLocalContext(contextName);
            return localContext.fetch(validKey);
        }, executorService);        
        
        assertNull(future2.get());
        assertEquals(valueSetInThread1, future1.get());
    }

    @Test(expected = InvalidContextException.class)
    public void testGetThreadLocalContextWithPermittedKeys() throws Throwable
    {
        String valueSetInThread1 = "Value T1";
        ApplicationContext localContext = ApplicationContextHolder.getThreadLocalContext(contextName, permittedKeys);
        localContext.add(validKey, valueSetInThread1);
        Set<ApplicationContextKey<?>> permittedKeysInvalid = new HashSet<>(Arrays.asList(invalidKey));
        ApplicationContextHolder.getThreadLocalContext(contextName, permittedKeysInvalid);
    }
    
    @Test(expected = InvalidKeyException.class)
    public void testGetThreadLocalContextWithPermittedKeysInvalidKey() throws Throwable
    {
        String valueSetInThread1 = "Value T1";
        ApplicationContext localContext = ApplicationContextHolder.getThreadLocalContext(contextName, permittedKeys);
        localContext.add(validKey, valueSetInThread1);
        localContext = ApplicationContextHolder.getThreadLocalContext(contextName, permittedKeys);
        localContext.fetch(invalidKey);
    }

    @Test
    public void testSetThreadLocalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread1 = "Value T1";
        
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(()->{
            ApplicationContext localContext = new ApplicationContextImpl(contextName);
            ApplicationContextHolder.setThreadLocalContext(localContext);
            localContext.add(validKey, valueSetInThread1);
            return ApplicationContextHolder.getThreadLocalContext(contextName).fetch(validKey);
        }, executorService);
        
        Thread.sleep(1000);
        
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()->{
            ApplicationContext localContext = ApplicationContextHolder.getThreadLocalContext(contextName);
            return localContext.fetch(validKey);
        }, executorService);        
        
        assertNull(future2.get());
        assertEquals(valueSetInThread1, future1.get());
    }

    @Test
    public void testClearThreadLocalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread1 = "Value T1";
        
        ApplicationContext localContext = new ApplicationContextImpl(contextName);
        ApplicationContextHolder.setThreadLocalContext(localContext);
        localContext.add(validKey, valueSetInThread1);
        ApplicationContextHolder.clearThreadLocalContext(contextName);
        String val = ApplicationContextHolder.getThreadLocalContext(contextName).fetch(validKey);
        
        assertNull(val);
    }

}
