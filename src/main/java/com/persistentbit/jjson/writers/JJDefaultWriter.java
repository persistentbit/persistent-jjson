package com.persistentbit.jjson.writers;

import com.persistentbit.core.Immutable;
import com.persistentbit.core.collections.PList;
import com.persistentbit.core.collections.PMap;
import com.persistentbit.core.collections.PSet;
import com.persistentbit.core.utils.ImTools;
import com.persistentbit.jjson.nodes.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;

/**
 * The main {@link JJWriter} implementations.<br>
 * This writer allows to add custom Class specific writers and by default
 * used {@link JJReflectionObjectWriter} for unknown classes.<br>
 * @see JJWriter
 * @see JJReflectionObjectWriter
 * @see JJObjectWriter
 */
@Immutable
public class JJDefaultWriter implements JJWriter{
    static private final ImTools<JJDefaultWriter>   im = ImTools.get(JJDefaultWriter.class);

    private final PMap<Class<?>,JJObjectWriter> customWriters;
    private final PMap<Class<?>,JJObjectWriter> generalWriters;
    private final Function<Object,Object> objectMapper;
    private final Function<Class<?>,JJObjectWriter> defaultWriterSupplier;

    private PMap<Class<?>,JJObjectWriter>   writersCache;


    public JJDefaultWriter(
            Function<Class<?>,JJObjectWriter> defaultWriterSupplier,
            Function<Object,Object> objectMapper,
            PMap<Class<?>,JJObjectWriter> customWriters,
            PMap<Class<?>,JJObjectWriter> generalWriters
    ){
        this.defaultWriterSupplier = Objects.requireNonNull(defaultWriterSupplier);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.customWriters = Objects.requireNonNull(customWriters);
        this.generalWriters = Objects.requireNonNull(generalWriters);
        this.writersCache = customWriters;
    }

    public JJDefaultWriter() {
        this((cls) -> new JJReflectionObjectWriter(cls));
    }

    static private PMap<Class<?>,JJObjectWriter> createDefaultCustomWriters()  {
        JJDateWriter ds = new JJDateWriter();
        return  PMap.<Class<?>,JJObjectWriter>empty()
                .put(Date.class,ds)
                .put(java.sql.Date.class,ds)
                .put(java.sql.Time.class,ds)
                .put(java.sql.Timestamp.class,ds)
                .put(LocalDate.class,ds)
                .put(LocalDateTime.class,ds)
                .put(LocalTime.class,ds)
                .put(Optional.class,new JJOptionalWriter())
                .put(PList.class,new JJPListWriter())
                .put(PSet.class,new JJPSetWriter())
                .put(PMap.class,new JJPMapWriter());
    }

    public JJDefaultWriter(Function<Class<?>,JJObjectWriter> defaultWriterSupplier) {
        this(
                defaultWriterSupplier,
                o -> o,
                createDefaultCustomWriters(),
                PMap.<Class<?>,JJObjectWriter>empty().put(Throwable.class,new JJExceptionWriter())
        );
    }

    /**
     * Add an object mapper.<br>
     * Every mapper has a chance to replace an object with an other object before the json writer is
     * called.<br>
     * @param mapper The Mapper to add
     * @return The NEW JJDefaultWriter with the added mapper
     */
    public JJDefaultWriter withMapper(Function<Object,Object> mapper){
        return im.copyWith(this,"objectMapper",objectMapper.andThen(mapper));
    }

    /**
     * Add a custom writer for a class.<br>
     *
     * @param cls The class for this writer
     * @param writer The writer
     * @return The NEW JJDefaultWriter with the added custom writer
     */
    public JJDefaultWriter withWriter(Class<?> cls, JJObjectWriter writer){
        return im.copyWith(this,"customWriters",customWriters.put(cls,writer));
    }

    public JJDefaultWriter withGeneralWriter(Class<?> cls,JJObjectWriter generalWriter){
        return im.copyWith(this,"generalWriters",generalWriters.put(cls,generalWriter));
    }

    /**
     * Added this CacheNode because we can not cache equal Objects
     * with different classes (example: java.sql.Date & java.Date)
     */
    class CachedNode{
        public final Class<?> nodeClass;
        public final Object nodeValue;

        public CachedNode(Object nodeValue){
            this.nodeClass = nodeValue.getClass();
            this.nodeValue =nodeValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CachedNode that = (CachedNode) o;

            if (!nodeClass.equals(that.nodeClass)) return false;
            return nodeValue.equals(that.nodeValue);

        }

        @Override
        public int hashCode() {
            int result = nodeClass.hashCode();
            result = 31 * result + nodeValue.hashCode();
            return result;
        }
    }

    private class JJWriterWriter implements JJWriter{
        private Set<Object> done = new HashSet<>();
        private Map<CachedNode,JJNode> existing = new HashMap<>();



        public JJNode write(Object value) {
            if(value == null){
                return JJNodeNull.Null;
            }
            value = objectMapper.apply(value);
            CachedNode cachedNode = new CachedNode(value);
            JJNode e = existing.get(cachedNode);
            if(e != null){
                return e;
            }
            if(value instanceof Number){
                JJNodeNumber n = new JJNodeNumber((Number)value);
                existing.put(cachedNode,n);
                return n;
            }
            if(value instanceof Boolean){
                return JJNodeBoolean.get((Boolean)value);

            }
            if(value instanceof Character){
                JJNodeString n = new JJNodeString(value.toString());
                existing.put(cachedNode,n);
                return n;
            }
            if(value instanceof String){
                JJNodeString n = new JJNodeString((String)value);
                existing.put(cachedNode,n);
                return n;
            }

            if(done.contains(value) && (value instanceof Collection == false && ((Collection)value).isEmpty() == false)){
                throw new RuntimeException("Cyclic data: " + value );
            }
            done.add(value);


            if(value instanceof Set || value instanceof List){
                List<JJNode> s = new ArrayList<>();
                for(Object v : (Collection<?>)value){
                    s.add(write(v));
                }
                JJNodeArray n = new JJNodeArray(s);
                existing.put(cachedNode,n);
                return n;
            }
            if(value.getClass().isArray()){
                List<JJNode> s = new ArrayList<>();
                if(value.getClass().getComponentType().equals(int.class)){
                    for(int v : (int[]) value){
                        s.add(write(v));
                    }
                } else if(value.getClass().getComponentType().equals(short.class)){
                    for(short v : (short[]) value){
                        s.add(write(v));
                    }
                } else if(value.getClass().getComponentType().equals(byte.class)){
                    for(byte v : (byte[]) value){
                        s.add(write(v));
                    }
                } else if(value.getClass().getComponentType().equals(char.class)){
                    for(char v : (char[]) value){
                        s.add(write(v));
                    }
                } else if(value.getClass().getComponentType().equals(boolean.class)){
                    for(boolean v : (boolean[]) value){
                        s.add(write(v));
                    }
                } else if(value.getClass().getComponentType().equals(long.class)){
                    for(long v : (long[]) value){
                        s.add(write(v));
                    }
                } else if(value.getClass().getComponentType().equals(float.class)){
                    for(float v : (float[]) value){
                        s.add(write(v));
                    }
                } else if(value.getClass().getComponentType().equals(double.class)){
                    for(double v : (double[]) value){
                        s.add(write(v));
                    }
                }else {
                    /*if(value.getClass().getComponentType().equals(Object.class)){
                        for (Object v : (Object[]) value)
                        {
                            JJNodeString jcls = new JJNodeString(v.getClass().getName());
                            Map<String,JJNode> propsMap = new HashMap<>();
                            propsMap.put("objectClass",jcls);
                            propsMap.put("value",write(v));
                            JJNodeObject jobj = new JJNodeObject(propsMap);
                            s.add(jobj);
                        }

                    } else
                    {
                        for (Object v : (Object[]) value)
                        {
                            s.add(write(v));
                        }
                    }*/
                    for (Object v : (Object[]) value)
                    {
                        s.add(write(v));
                    }
                }
                JJNodeArray n = new JJNodeArray(s);
                existing.put(cachedNode,n);
                return n;
            }
            if(value instanceof Map){
                List<JJNode> s = new ArrayList<>();
                for(Map.Entry<?,?> entry : ((Map<?,?>)value).entrySet()){
                    s.add(new JJNodeArray(write(entry.getKey()),write(entry.getValue())));
                }
                JJNodeArray n = new JJNodeArray(s);
                existing.put(cachedNode,n);
                return n;
            }

            if(value instanceof Enum){
                Enum<?> en = (Enum<?>)value;
                JJNodeString n = new JJNodeString(en.name());
                existing.put(cachedNode,n);
                return n;
            }

            if(value instanceof Class){
                JJNodeString n = new JJNodeString(((Class<?>)value).getName());
                existing.put(cachedNode,n);
                return n;
            }

            JJObjectWriter writer = writersCache.get(value.getClass());
            if(writer == null){
                Class<?> valueClass = value.getClass();
                writer = generalWriters.find(t-> t._1.isAssignableFrom(valueClass)).map(t -> t._2).orElseGet(() -> defaultWriterSupplier.apply(valueClass));
                /*for(Map.Entry<Class<?>,JJObjectWriter> entry : generalWriters.entrySet()){
                    if(entry.getKey().isAssignableFrom(value.getClass())){
                        customWriters.put(value.getClass(),entry.getValue());
                        writer = entry.getValue();
                        break;
                    }
                }
                if(writer == null)
                {
                    writer = defaultWriterSupplier.apply(value.getClass());
                }*/
                writersCache = writersCache.put(value.getClass(),writer);
            }
            JJNode result =  writer.write(value,this);
            existing.put(cachedNode,result);
            done.remove(value);
            return result;
        }
    }


    public JJNode write(Object value){
        return new JJWriterWriter().write(value);
    }





}
