package com.persistentbit.jjson.nodes;



import java.util.Optional;

/**
 * JJNode representing a boolean value
 * @author Peter Muys
 * @since 22/10/2015
 */
public class JJNodeBoolean implements JJNode
{
    private final boolean value;


    private JJNodeBoolean(boolean value){
        this.value = value;
    }

    /**
     * The One and Only True instance
     */
    static public JJNodeBoolean True = new JJNodeBoolean(true);
    /**
     * The One and Only False instance
     */
    static public JJNodeBoolean False = new JJNodeBoolean(false);

    /**
     * Get the JJNodeBoolean instance for the provided boolean
     * @param value true or false
     * @return Shared JJNodeBoolean instance
     */
    static public JJNodeBoolean get(boolean value) {
        return value ? True : False;
    }

    /**
     * Get the value that this instance represent
     * @return true or false
     */
    public boolean getValue(){
        return value;
    }

    @Override
    public String toString() {
        return JJPrinter.print(false,this);
    }

    @Override
    public JType getType()
    {
        return JType.jsonBoolean;
    }

    @Override
    public Optional<JJNodeBoolean> asBoolean()
    {
        return Optional.of(this);
    }
}
