package fop.context;

import java.util.Set;

public interface UnrestrictedApplicationContext extends ApplicationContext 
{
    default public ApplicationContextTypes.Gate gate() 
    {
        return ApplicationContextTypes.Gate.UNRESTRICTED;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    default public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy)
    {
        Set<ApplicationContextKey<?>> keysOther = other.keySet();
        
        for(ApplicationContextKey keyOther: keysOther)
        {
            Object newValue = other.fetch(keyOther);
            
            if(this.exists(keyOther)) 
            {
                Object value = mergeStrategy.merge(keyOther, this.fetch(keyOther), newValue);
                addWithOverwrite(keyOther, value);
            }
            else
            {
                addIfNotPresent(keyOther, newValue);
            }
        }
    }
}
