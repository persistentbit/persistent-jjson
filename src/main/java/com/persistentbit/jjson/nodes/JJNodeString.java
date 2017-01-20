package com.persistentbit.jjson.nodes;


import com.persistentbit.core.lenses.Lens;
import com.persistentbit.core.lenses.LensImpl;
import com.persistentbit.core.result.Result;

import java.util.function.Function;

/**
 * @author Peter Muys
 * @since 22/10/2015
 */
public class JJNodeString implements JJNode
{
    private final String value;

    public JJNodeString(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
    @Override
    public JType getType()
    {
        return JType.jsonString;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public Result<JJNodeString> asString()
    {
        return Result.success(this);
    }

    static public Lens<JJNodeString,String> valueLens = new LensImpl<>((s)-> s.getValue(), (p, c)-> new JJNodeString(c));

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JJNodeString that = (JJNodeString) o;

        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public <T> T match(Function<JJNodeArray, T> anArray, Function<JJNodeBoolean, T> aBoolean,
                       Function<JJNodeNull, T> aNull, Function<JJNodeNumber, T> aNumber,
                       Function<JJNodeObject, T> anObject, Function<JJNodeString, T> aString
    ) {
        return aString.apply(this);
    }
}
