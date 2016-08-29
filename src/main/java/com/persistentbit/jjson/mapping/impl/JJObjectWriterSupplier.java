package com.persistentbit.jjson.mapping.impl;

import com.persistentbit.core.Immutable;
import com.persistentbit.core.collections.*;
import com.persistentbit.jjson.mapping.impl.custom.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;


/**
 * A {@link JJObjectWriterSupplier} is used by {@link JJDefaultWriter} to get a Class specific Object to JSON translator<br>
 * @see JJDefaultWriter
 */
@Immutable
public class JJObjectWriterSupplier implements Function<Class<?>,JJObjectWriter> {






    private PMap<Class<?>,JJObjectWriter>   cache = PMap.empty();
    private final PList<Function<Class<?>,JJObjectWriter>> suppliers;
    private final Function<Class<?>,JJObjectWriter>    fallBack;

    public JJObjectWriterSupplier(PList<Function<Class<?>,JJObjectWriter>> suppliers, Function<Class<?>,JJObjectWriter> fallBack){
        this.suppliers = suppliers;
        this.fallBack = fallBack;
    }
    public JJObjectWriterSupplier(PList<Function<Class<?>,JJObjectWriter>> suppliers){
        this(suppliers,c -> new JJReflectionObjectWriter(c));
    }

    public JJObjectWriterSupplier() {
        this(PList.empty());
    }

    /**
     *
     * @return A new {@link JJObjectWriterSupplier} with writers added for general classes like Date, LocalTime, PList,Optional...
     */
    public JJObjectWriterSupplier addCoreWriters(){
        JJObjectWriterSupplier s = this;
        JJDateWriter dw = new JJDateWriter();
        s = s.withForClass(Date.class,dw);
        s = s.withForClass(java.sql.Date.class,dw);
        s = s.withForClass(java.sql.Time.class,dw);
        s = s.withForClass(java.sql.Timestamp.class,dw);
        s = s.withForClass(LocalDate.class,dw);
        s = s.withForClass(LocalDateTime.class,dw);
        s = s.withForClass(LocalTime.class,dw);

        s = s.withForClass(Optional.class,new JJOptionalWriter());
        JJPListWriter lw = new JJPListWriter();
        s = s.withForClass(PList.class,lw).withForClass(LList.class,lw);
        JJPSetWriter sw = new JJPSetWriter();
        s = s.withForClass(PSet.class,sw).withForClass(POrderedSet.class,sw);
        JJPMapWriter mw = new JJPMapWriter();
        s = s.withForClass(PMap.class,mw).withForClass(POrderedMap.class,mw);
        s = s.withAssignableTo(Throwable.class,new JJExceptionWriter());

        return s;
    }


    /**
     * Get the Object Writer for the given cls
     * @param cls The class to get an Object writer for
     * @return the {@link JJObjectWriter} for the given class
     */
    public JJObjectWriter apply(Class<?> cls) {
        JJObjectWriter ow = cache.get(cls);
        if(ow != null){
            return ow;
        }
        for(Function<Class<?>,JJObjectWriter> s : suppliers){
            ow = s.apply(cls);
            if(ow != null){
                cache = cache.put(cls,ow);
                return ow;
            }
        }
        return fallBack == null ? null : fallBack.apply(cls);
    }

    /**
     * Add a specific custom {@link JJObjectWriter} for the given class
     * @param cls The class
     * @param ow The Object Writer
     * @return a new {@link JJObjectWriterSupplier} with the added class Object Writer.
     */
    public JJObjectWriterSupplier withForClass(Class<?> cls, JJObjectWriter ow){
        return withPrevSupplier(c -> cls.equals(c) ? ow : null);
    }

    public JJObjectWriterSupplier withAssignableTo(Class<?> clsAssignableTo, JJObjectWriter ow){
        return withNextSupplier(c -> clsAssignableTo.isAssignableFrom(c) ? ow : null);
    }

    public JJObjectWriterSupplier withNextSupplier(Function<Class<?>,JJObjectWriter>...next){
        PList<Function<Class<?>,JJObjectWriter>> res = suppliers;
        for(Function<Class<?>,JJObjectWriter> s : next){
            res = res.plus(s);
        }
        return new JJObjectWriterSupplier(res,fallBack);
    }

    public JJObjectWriterSupplier withPrevSupplier(Function<Class<?>,JJObjectWriter>...prev){
        PList<Function<Class<?>,JJObjectWriter>> res = PList.empty();
        for(Function<Class<?>,JJObjectWriter> s : prev){
            res = res.plus(s);
        }
        res = res.plusAll(suppliers);
        return new JJObjectWriterSupplier(res,fallBack);
    }

    public JJObjectWriterSupplier   withFallbackSupplier(Function<Class<?>,JJObjectWriter> fallBack){
        return new JJObjectWriterSupplier(suppliers,fallBack);
    }
}
