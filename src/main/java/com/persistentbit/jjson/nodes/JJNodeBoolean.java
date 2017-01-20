package com.persistentbit.jjson.nodes;


import com.persistentbit.core.result.Result;

import java.util.function.Function;

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
    static public final JJNodeBoolean True = new JJNodeBoolean(true);
    /**
     * The One and Only False instance
     */
    static public final JJNodeBoolean False = new JJNodeBoolean(false);

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
    public Result<JJNodeBoolean> asBoolean()
    {
        return Result.success(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JJNodeBoolean that = (JJNodeBoolean) o;

        return value == that.value;

    }

    @Override
    public int hashCode() {
        return (value ? 1 : 0);
    }

    @Override
    public <T> T match(Function<JJNodeArray, T> anArray, Function<JJNodeBoolean, T> aBoolean,
                       Function<JJNodeNull, T> aNull, Function<JJNodeNumber, T> aNumber,
                       Function<JJNodeObject, T> anObject, Function<JJNodeString, T> aString
    ) {
        return aBoolean.apply(this);
    }
}
