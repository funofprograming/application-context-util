package fop.context.impl.local;

import fop.context.ApplicationContext;
import fop.context.ApplicationContextKey;
import fop.context.ApplicationContextMergeStrategy;
import fop.context.LocalApplicationContext;
import fop.context.UnrestrictedApplicationContext;
import fop.context.impl.AbstractApplicationContext;
import fop.context.impl.InvalidContextOperation;

public class UnrestrictedLocalApplicationContext extends AbstractApplicationContext implements LocalApplicationContext, UnrestrictedApplicationContext
{
    public UnrestrictedLocalApplicationContext(String name) 
    {
        super(name);
    }

    @Override
    public void validateKey(ApplicationContextKey<?> key) 
    {
        //unrestricted
    }
    @Override
    public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy)
    {
        throw new InvalidContextOperation("merge not supported for local context");
    }
}
