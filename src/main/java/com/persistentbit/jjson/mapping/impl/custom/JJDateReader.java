package com.persistentbit.jjson.mapping.impl.custom;



import com.persistentbit.core.utils.ReflectionUtils;
import com.persistentbit.jjson.mapping.JJReader;
import com.persistentbit.jjson.mapping.description.JJClass;
import com.persistentbit.jjson.mapping.description.JJTypeDescription;
import com.persistentbit.jjson.mapping.description.JJTypeSignature;
import com.persistentbit.jjson.mapping.impl.JJDescriber;
import com.persistentbit.jjson.mapping.impl.JJObjectReader;
import com.persistentbit.jjson.mapping.impl.JJsonException;
import com.persistentbit.jjson.nodes.JJNode;

import java.lang.reflect.Type;
import java.time.*;
import java.time.format.DateTimeParseException;

/**
 * User: petermuys
 * Date: 24/10/15
 * Time: 13:53
 */
public class JJDateReader implements JJObjectReader,JJDescriber {


    @Override
    public Object read(Type type, JJNode node, JJReader reader) {
        if(node.asNull().isPresent()){
            return null;
        }

        Class<?> cls = ReflectionUtils.classFromType(type);
        String str = node.asString().orElseThrow().getValue();
        Instant in;

        if(cls.equals(ZonedDateTime.class)){
            return ZonedDateTime.parse(str);
        }

        try{
            in = Instant.parse(str);
        }catch (DateTimeParseException pe){
            try {
                in = Instant.ofEpochMilli(Long.parseLong(str));
            }catch (NumberFormatException nfe){
                throw new IllegalArgumentException("Don't know how to parse " + str);
            }

        }
        if(cls.equals(java.sql.Date.class)){
            return java.sql.Date.from(in);
        }
        if(cls.equals(LocalDateTime.class)){
            return LocalDateTime.ofInstant(in, ZoneId.systemDefault());
        }
        if(cls.equals(java.sql.Time.class)){
            return java.sql.Time.from(in);
        } else if(cls.equals(java.sql.Timestamp.class)){
            return java.sql.Timestamp.from(in);
        } else if(cls.equals(java.util.Date.class)){
            return java.util.Date.from(in);
        } else if(cls.equals(LocalDate.class)){
            return LocalDate.from(in);
        } else if(cls.equals(LocalTime.class)){
            return LocalTime.from(in);
        }
        throw new JJsonException("Not Yet implemented " + cls.getName());
    }

    @Override
    public JJTypeDescription describe(Type t, JJDescriber masterDescriber) {
        return new JJTypeDescription(new JJTypeSignature(new JJClass(ReflectionUtils.classFromType(t)), JJTypeSignature.JsonType.jsonString));
    }
}
