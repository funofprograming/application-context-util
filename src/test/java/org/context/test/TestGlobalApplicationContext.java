package org.context.test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

import fop.context.ApplicationContextKey;
import fop.context.GlobalApplicationContext;
import fop.context.impl.InvalidKeyException;
import fop.context.util.ContextUtil;

public class TestGlobalApplicationContext 
{
    public TestGlobalApplicationContext() 
    {
    }
    
    @Test
    public void testUnrestrictedGlobalApplicationContext() throws InterruptedException, ExecutionException 
    {
        
        final ApplicationContextKey<String> key1 = ApplicationContextKey.of("key1", String.class);
        final ApplicationContextKey<String> key2 = ApplicationContextKey.of("key2", String.class);
        final ApplicationContextKey<String> key3 = ApplicationContextKey.of("key3", String.class);
        final String val1 = "val1";
        final String val3 = "val3";
                
        
        String globalContextName = "testUnrestrictedGlobalApplicationContext";
        Set<ApplicationContextKey<?>> permittedKeys = new HashSet<>();
        permittedKeys.add(key1);
        permittedKeys.add(key2);
        
        ExecutorService exec = null;
        
        exec = Executors.newFixedThreadPool(1);
        
        Runnable writer = new Runnable() 
        {
            @Override
            public void run() 
            {
                GlobalApplicationContext ga = ContextUtil.getUnrestrictedGlobalContext(globalContextName);
                ga.add(key1, val1);
                ga.add(key3, val3);
            }
        };
        
        Callable<String[]> reader = new Callable<String[]>() 
        {
            @Override
            public String[] call() throws Exception 
            {
                GlobalApplicationContext ga = ContextUtil.getUnrestrictedGlobalContext(globalContextName);
                String[] vals = new String[2];
                
                vals[0] = (String)ga.fetch(key1);
                vals[1] = (String)ga.fetch(key2);
                
                return vals;
            }
        };
        
        Future<?> writerFuture = exec.submit(writer);
        exec.shutdown();
        try
        {
            writerFuture.get();
        }
        catch(Exception e)
        {
            Assert.assertFalse(e.getMessage().contains("Invalid key:"+key1));
        }
        
        exec = Executors.newFixedThreadPool(1);
        
        Future<String[]> readerFuture = exec.submit(reader);
        exec.shutdown();
        
        String[] vals = readerFuture.get();
        
        Assert.assertEquals(val1, vals[0]);
        Assert.assertNull(vals[1]);
        
        ContextUtil.closeGlobalApplicationContext(globalContextName);
    }

    @Test
    public void testRestrictedGlobalApplicationContext() throws InterruptedException, ExecutionException 
    {
        
        final ApplicationContextKey<String> key1 = ApplicationContextKey.of("key1", String.class);
        final ApplicationContextKey<String> key2 = ApplicationContextKey.of("key2", String.class);
        final ApplicationContextKey<String> key3 = ApplicationContextKey.of("key3", String.class);
        final String val1 = "val1";
        final String val3 = "val3";
                
        
        String globalContextName = "testRestrictedGlobalApplicationContext";
        Set<ApplicationContextKey<?>> permittedKeys = new HashSet<>();
        permittedKeys.add(key1);
        permittedKeys.add(key2);
        
        ExecutorService exec = null;
        
        exec = Executors.newFixedThreadPool(1);
        
        Runnable writer = new Runnable() 
        {
            @Override
            public void run() 
            {
                GlobalApplicationContext ga = ContextUtil.getRestrictedGlobalContext(globalContextName, permittedKeys);
                ga.add(key1, val1);
                ga.add(key3, val3);
            }
        };
        
        Callable<String[]> reader = new Callable<String[]>() 
        {
            @Override
            public String[] call() throws Exception 
            {
                GlobalApplicationContext ga = ContextUtil.getRestrictedGlobalContext(globalContextName, permittedKeys);
                String[] vals = new String[2];
                
                vals[0] = (String)ga.fetch(key1);
                vals[1] = (String)ga.fetch(key2);
                
                return vals;
            }
        };
        
        Future<?> writerFuture = exec.submit(writer);
        exec.shutdown();
        try
        {
            writerFuture.get();
        }
        catch(Exception e)
        {
            Assert.assertEquals(e.getCause().getClass(), InvalidKeyException.class);
            Assert.assertTrue(e.getMessage().contains("Invalid key:"+key3));
            Assert.assertFalse(e.getMessage().contains("Invalid key:"+key1));
        }
        
        exec = Executors.newFixedThreadPool(1);
        
        Future<String[]> readerFuture = exec.submit(reader);
        exec.shutdown();
        
        String[] vals = readerFuture.get();
        
        Assert.assertEquals(val1, vals[0]);
        Assert.assertNull(vals[1]);
        
        ContextUtil.closeGlobalApplicationContext(globalContextName);
    }
    
}
