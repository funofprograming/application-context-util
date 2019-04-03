package fop.context;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data(staticConstructor = "of")
@EqualsAndHashCode(of = {"keyName"})
public class ApplicationContextKey<T> implements Serializable
{
    private static final long serialVersionUID = -1622914480860236052L;
    
    private String keyName;
    private Class<T> keyType;
}
