package io.github.funofprograming.context;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * Application context key with type safety. 
 * 
 * @author Akshay Jain
 *
 * @param <T>
 */
@Data
@RequiredArgsConstructor(staticName = "of")
@EqualsAndHashCode(of = {"keyName"})
public class Key<T> implements Serializable
{
    private static final long serialVersionUID = -1622914480860236052L;
    
    /**
     * Key name as {@linkplain String}
     */
    private final String keyName;
    
    /**
     * Type of key as {@linkplain Class}
     */
    private final KeyType<T> valueType;
    
    public static <T> Key<T> of(String keyName, Class<T> type)
    {
        return Key.<T>of(keyName, KeyType.<T>of(type));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() 
    {
        return keyName+"<"+valueType+">";
    }
}
