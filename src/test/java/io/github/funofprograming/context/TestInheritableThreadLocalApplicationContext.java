package io.github.funofprograming.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.github.funofprograming.context.impl.ApplicationContextHolder;
import io.github.funofprograming.context.impl.ApplicationContextImpl;
import io.github.funofprograming.context.impl.InvalidContextException;
import io.github.funofprograming.context.impl.InvalidKeyException;

public class TestInheritableThreadLocalApplicationContext
{
    private String contextName;
    private Key<String> validKey;
    private Key<String> invalidKey;
    private Set<Key<?>> permittedKeys;
    private ThreadPoolExecutor executorService;
    
    @Before
    public void setUp() throws Exception
    {
        contextName = "TestThreadLocalApplicationContext";
        validKey = Key.of("ValidKey", String.class);
        invalidKey = Key.of("InvalidKey", String.class);
        permittedKeys = new HashSet<>(Arrays.asList(validKey));
        executorService = new ThreadPoolExecutor(2, 2, 1L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        executorService.allowCoreThreadTimeOut(true);
    }

    @After
    public void tearDown() throws Exception
    {
        ApplicationContextHolder.clearInheritableThreadLocalContext(contextName);
        executorService.shutdown();
    }

    @Test
    public void testGetInheritableThreadLocalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread1 = "Value T1";
        
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(()->{
            ApplicationContext localContext = ApplicationContextHolder.getInheritableThreadLocalContext(contextName);
            localContext.add(validKey, valueSetInThread1);
            Callable<String> childProcess = ()->ApplicationContextHolder.getInheritableThreadLocalContext(contextName).fetch(validKey);
            FutureTask<String> ft = new FutureTask<>(childProcess);
            new Thread(ft).start();
            try
            {
                Thread.sleep(2000);
                return ft.get();
            }
            catch (InterruptedException | ExecutionException e)
            {
                throw new RuntimeException(e);
            }
        }, executorService);     
        
        assertEquals(valueSetInThread1, future1.get());
    }

    @Test(expected = InvalidContextException.class)
    public void testGetInheritableThreadLocalContextWithPermittedKeys() throws Throwable
    {
        String valueSetInThread1 = "Value T1";
        
        ApplicationContext localContext = ApplicationContextHolder.getInheritableThreadLocalContext(contextName, permittedKeys);
        localContext.add(validKey, valueSetInThread1);
        Callable<String> childProcess = ()->{
            Set<Key<?>> permittedKeysInvalid = new HashSet<>(Arrays.asList(invalidKey));
            return ApplicationContextHolder.getInheritableThreadLocalContext(contextName, permittedKeysInvalid).fetch(validKey);
        };
        FutureTask<String> ft = new FutureTask<>(childProcess);
        new Thread(ft).start();
        try
        {
            Thread.sleep(1000);
            ft.get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            throw e.getCause();
        }
    }

    @Test(expected = InvalidKeyException.class)
    public void testGetInheritableThreadLocalContextWithPermittedKeysInvalidKey() throws Throwable
    {
        String valueSetInThread1 = "Value T1";
        
        ApplicationContext localContext = ApplicationContextHolder.getInheritableThreadLocalContext(contextName, permittedKeys);
        localContext.add(validKey, valueSetInThread1);
        Callable<String> childProcess = ()->{
            return ApplicationContextHolder.getInheritableThreadLocalContext(contextName, permittedKeys).fetch(invalidKey);
        };
        FutureTask<String> ft = new FutureTask<>(childProcess);
        new Thread(ft).start();
        try
        {
            Thread.sleep(1000);
            ft.get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            throw e.getCause();
        }
    }

    @Test
    public void testSetInheritableThreadLocalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread1 = "Value T1";
        
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(()->{
            ApplicationContext localContext = new ApplicationContextImpl(contextName);
            ApplicationContextHolder.setInheritableThreadLocalContext(localContext);
            localContext.add(validKey, valueSetInThread1);
            Callable<String> childProcess = ()->ApplicationContextHolder.getInheritableThreadLocalContext(contextName).fetch(validKey);
            FutureTask<String> ft = new FutureTask<>(childProcess);
            new Thread(ft).start();
            try
            {
                Thread.sleep(2000);
                return ft.get();
            }
            catch (InterruptedException | ExecutionException e)
            {
                throw new RuntimeException(e);
            }
        }, executorService);   
        
        assertEquals(valueSetInThread1, future1.get());
    }

    @Test
    public void testClearInheritableThreadLocalContext() throws InterruptedException, ExecutionException
    {
        String valueSetInThread1 = "Value T1";
        
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(()->{
            ApplicationContext localContext = ApplicationContextHolder.getInheritableThreadLocalContext(contextName);
            localContext.add(validKey, valueSetInThread1);
            new Thread(()->ApplicationContextHolder.clearInheritableThreadLocalContext(contextName)).start();
            try
            {
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
            localContext = ApplicationContextHolder.getInheritableThreadLocalContext(contextName);
            return localContext.fetch(validKey);
        }, executorService);       
        
        assertNull(future1.get());
    }

}
