package com.persistentbit.jjson.nodes;

import com.persistentbit.core.result.Result;

/**
 * @author Peter Muys
 * @since 22/10/2015
 */
public class JJNodeNumber implements JJNode
{
    private final Number value;

    public JJNodeNumber(Number value){
        this.value = value;
    }

    public Number getValue(){
        return value;
    }
    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public JType getType()
    {
        return JType.jsonNumber;
    }

    @Override
    public Result<JJNodeNumber> asNumber()
    {
        return Result.success(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JJNodeNumber that = (JJNodeNumber) o;

        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
