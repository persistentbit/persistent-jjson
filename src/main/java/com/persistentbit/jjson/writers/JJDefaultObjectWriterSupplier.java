package com.persistentbit.jjson.writers;

import com.persistentbit.core.Immutable;
import com.persistentbit.core.collections.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;


/**
 * Created by petermuys on 27/08/16.
 */
@Immutable
public class JJDefaultObjectWriterSupplier implements JJObjectWriterSupplier {

    private PMap<Class<?>,JJObjectWriter>   cache = PMap.empty();
    private final PList<JJObjectWriterSupplier> suppliers;
    private final JJObjectWriterSupplier    fallBack;

    public JJDefaultObjectWriterSupplier(PList<JJObjectWriterSupplier> suppliers,JJObjectWriterSupplier fallBack){
        this.suppliers = suppliers;
        this.fallBack = fallBack;
    }
    public JJDefaultObjectWriterSupplier(PList<JJObjectWriterSupplier> suppliers){
        this(suppliers,c -> new JJReflectionObjectWriter(c));
    }

    public JJDefaultObjectWriterSupplier() {
        this(PList.empty());
    }

    public JJDefaultObjectWriterSupplier addCoreWriters(){
        JJDefaultObjectWriterSupplier s = this;
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



    @Override
    public JJObjectWriter getWriterForClass(Class<?> cls) {
        JJObjectWriter ow = cache.get(cls);
        if(ow != null){
            return ow;
        }
        for(JJObjectWriterSupplier s : suppliers){
            ow = s.getWriterForClass(cls);
            if(ow != null){
                cache = cache.put(cls,ow);
                return ow;
            }
        }
        return null;
    }

    public JJDefaultObjectWriterSupplier withForClass(Class<?> cls,JJObjectWriter ow){
        return withPrevSupplier(c -> cls.equals(c) ? ow : null);
    }

    public JJDefaultObjectWriterSupplier withAssignableTo(Class<?> clsAssignableTo,JJObjectWriter ow){
        return withNextSupplier(c -> clsAssignableTo.isAssignableFrom(c) ? ow : null);
    }

    public JJDefaultObjectWriterSupplier    withNextSupplier(JJObjectWriterSupplier...next){
        PList<JJObjectWriterSupplier> res = suppliers;
        for(JJObjectWriterSupplier s : next){
            res = res.plus(s);
        }
        return new JJDefaultObjectWriterSupplier(res,fallBack);
    }

    public JJDefaultObjectWriterSupplier withPrevSupplier(JJObjectWriterSupplier...prev){
        PList<JJObjectWriterSupplier> res = PList.empty();
        for(JJObjectWriterSupplier s : prev){
            res = res.plus(s);
        }
        res = res.plusAll(suppliers);
        return new JJDefaultObjectWriterSupplier(res,fallBack);
    }
}
