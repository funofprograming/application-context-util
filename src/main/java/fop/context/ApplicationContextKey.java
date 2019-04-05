package fop.context;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
@EqualsAndHashCode(of = {"keyName"})
public class ApplicationContextKey<T> implements Serializable
{
    private static final long serialVersionUID = -1622914480860236052L;
    
    private final String keyName;
    private final Class<T> keyType;
}
