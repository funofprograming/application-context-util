package fop.context;

import java.util.Set;

/**
 * This is a {@linkplain ApplicationContext} extension with {@linkplain ApplicationContextTypes.Gate}.RESTRICTED gate
 * 
 * @author Akshay Jain
 *
 */
public interface RestrictedApplicationContext extends ApplicationContext 
{
    default public ApplicationContextTypes.Gate gate() 
    {
        return ApplicationContextTypes.Gate.RESTRICTED;
    }
    
    /**
     * Keys permitted for this context
     * 
     * @return set of permitted keys
     */
    public Set<ApplicationContextKey<?>> getPermittedKeys();
    
    /**
     * Check if given key is valid or not. This method should check parameter key against permitted keys
     * @param key
     * @return true if key is valid otherwise false
     */
    public boolean isKeyValid(ApplicationContextKey<?> key);
    
    /**
     * default merge implementation that merges ONLY those keys of other context which are valid for this context
     */
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
