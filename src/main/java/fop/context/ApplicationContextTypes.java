package fop.context;

public interface ApplicationContextTypes {

    public static enum Scope
    {
        LOCAL,
        GLOBAL,
        INSTANCE;
    }
    
    public static enum Gate
    {
        RESTRICTED,
        UNRESTRICTED;
    }
    
}
