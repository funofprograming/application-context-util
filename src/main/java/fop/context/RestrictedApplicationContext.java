package fop.context;

import java.util.Set;

public interface RestrictedApplicationContext extends ApplicationContext 
{
    default public ApplicationContextTypes.Gate gate() 
    {
        return ApplicationContextTypes.Gate.RESTRICTED;
    }
    
    public Set<ApplicationContextKey<?>> getPermittedKeys();
    
    public boolean isKeyValid(ApplicationContextKey<?> key);
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default public void merge(ApplicationContext other, ApplicationContextMergeStrategy mergeStrategy)
    {
        Set<ApplicationContextKey<?>> keysOther = other.keySet();
        
        for(ApplicationContextKey keyOther: keysOther)
        {
            if(!isKeyValid(keyOther))
            {
                continue;
            }
            
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
