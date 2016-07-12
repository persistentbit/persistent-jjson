package com.persistentbit.jjson.readers;



import com.persistentbit.jjson.nodes.JJNode;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * User: petermuys
 * Date: 24/10/15
 * Time: 13:53
 */
public class JJDateReader implements JJObjectReader {
    private DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
    @Override
    public boolean canRead(JJNode node) {
        return node.getType() == JJNode.JType.jsonNull || node.getType() == JJNode.JType.jsonString;
    }

    @Override
    public Object read(Type type, JJNode node, JJReader reader) {
        if(node.asNull().isPresent()){
            return null;
        }
        Class<?> cls = ReflectionUtils.classFromType(type);
        String str = node.asString().get().getValue();
        if(cls.equals(java.sql.Date.class)){
            return java.sql.Date.valueOf(LocalDate.parse(str, DateTimeFormatter.ISO_DATE));
        }
        if(cls.equals(LocalDateTime.class)){
            return LocalDateTime.parse(str, DateTimeFormatter.ISO_DATE_TIME);
        }

        Instant inst = Instant.from(formatter.parse(str));

        if(cls.equals(java.sql.Time.class)){
            return java.sql.Time.from(inst);
        } else if(cls.equals(java.sql.Timestamp.class)){
            return java.sql.Timestamp.from(inst);
        } else if(cls.equals(java.util.Date.class)){
            return java.util.Date.from(inst);
        } else if(cls.equals(LocalDate.class)){
            return LocalDate.from(inst);
        } else if(cls.equals(LocalTime.class)){
            return LocalTime.from(inst);
        }
        throw new RuntimeException("Not Yet implemented " + cls.getName());
    }
}
