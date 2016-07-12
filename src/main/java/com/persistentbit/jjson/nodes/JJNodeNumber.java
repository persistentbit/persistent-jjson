package com.persistentbit.jjson.nodes;

import java.util.Optional;

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
    public Optional<JJNodeNumber> asNumber()
    {
        return Optional.of(this);
    }
}
