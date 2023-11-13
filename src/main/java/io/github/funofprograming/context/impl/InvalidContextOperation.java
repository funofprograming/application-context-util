package io.github.funofprograming.context.impl;

public class InvalidContextOperation extends UnsupportedOperationException
{
    private static final long serialVersionUID = -8768646086749074757L;

    public InvalidContextOperation(String message) 
    {
        super(message);
    }
}
