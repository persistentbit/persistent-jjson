package com.persistentbit.jjson.writers;


import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeString;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Peter Muys
 * @since 22/10/2015
 */
public class JJDateWriter implements JJObjectWriter
{
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
    @Override
    public JJNode write(Object value, JJMasterWriter master)
    {
        if(value instanceof java.sql.Date){
            java.sql.Date sd = (java.sql.Date) value;
            return new JJNodeString(sd.toLocalDate().format(DateTimeFormatter.ISO_DATE));
        }
        if(value instanceof Date){
            return new JJNodeString(formatter.format(((Date)value).toInstant()));
        }
        if(value instanceof LocalDateTime){
            LocalDateTime ldt = (LocalDateTime)value;
            return new JJNodeString(ldt.format(DateTimeFormatter.ISO_DATE_TIME));
        }
        return new JJNodeString(formatter.format((Instant)value));
    }
}
