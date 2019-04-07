package fop.context.impl.local;

import fop.context.ApplicationContext;
import fop.context.ApplicationContextKey;
import fop.context.ApplicationContextMergeStrategy;
import fop.context.LocalApplicationContext;
import fop.context.UnrestrictedApplicationContext;
import fop.context.impl.AbstractApplicationContext;
import fop.context.impl.InvalidContextOperation;

/**
 * This is an {@linkplain ApplicationContext} extension that implements {@linkplain LocalApplicationContext} and {@linkplain UnrestrictedApplicationContext}
 * 
 * @author Akshay Jain
 *
 */
public class UnrestrictedLocalApplicationContext extends AbstractApplicationContext implements LocalApplicationContext, UnrestrictedApplicationContext
{
    /**
     * Initialize with a name
     * 
     * @param name
     */
    UnrestrictedLocalApplicationContext(String name) 
    {
        super(name);
    }

    /**
     * no validation needed for {@linkplain UnrestrictedApplicationContext}
     */
    @Override
    public void validateKey(ApplicationContextKey<?> key) 
    {
        //unrestricted
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy)
    {
        throw new InvalidContextOperation("merge not supported for local context");
    }
}
