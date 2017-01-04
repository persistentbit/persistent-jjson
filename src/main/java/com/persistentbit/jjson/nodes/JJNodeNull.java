package com.persistentbit.jjson.nodes;

import com.persistentbit.core.result.Result;

/**
 * A JJNode representing the null value
 * @author Peter Muys
 * @since 22/10/2015
 */
public class JJNodeNull implements JJNode
{
    private JJNodeNull(){

    }

    /**
     * The One and only JJNodeNull value
     */
    static public final JJNodeNull Null= new JJNodeNull();

    @Override
    public String toString()
    {
        return "null";
    }
    @Override
    public JType getType()
    {
        return JType.jsonNull;
    }

    @Override
    public Result<JJNodeNull> asNull()
    {
        return Result.success(this);
    }


}
