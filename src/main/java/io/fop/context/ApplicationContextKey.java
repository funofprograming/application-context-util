package io.fop.context;

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
public class ApplicationContextKey<T> implements Serializable
{
    private static final long serialVersionUID = -1622914480860236052L;
    
    /**
     * Key name as {@linkplain String}
     */
    private final String keyName;
    
    /**
     * Type of key as {@linkplain Class}
     */
    private final ParameterizedTypeReference<T> valueType;
}
