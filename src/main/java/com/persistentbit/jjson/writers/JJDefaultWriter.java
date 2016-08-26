package com.persistentbit.jjson.writers;

import com.persistentbit.core.collections.PList;
import com.persistentbit.core.collections.PMap;
import com.persistentbit.core.collections.PSet;
import com.persistentbit.jjson.nodes.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * User: petermuys
 * Date: 26/08/16
 * Time: 09:46
 */
public class JJDefaultWriter implements JJWriter{
    private final Map<Class<?>,JJObjectWriter> customWriters = new HashMap<>();
    private final Map<Class<?>,JJObjectWriter> generalWriters = new HashMap<>();
    private final List<JJWriterObjectMapper> mappers = new ArrayList<>();

    public JJDefaultWriter() {
        JJDateWriter ds = new JJDateWriter();
        customWriters.put(Date.class,ds);
        customWriters.put(java.sql.Date.class,ds);
        customWriters.put(java.sql.Time.class,ds);
        customWriters.put(java.sql.Timestamp.class,ds);
        customWriters.put(LocalDate.class,ds);
        customWriters.put(LocalDateTime.class,ds);
        customWriters.put(LocalTime.class,ds);
        customWriters.put(Optional.class,new JJOptionalWriter());
        customWriters.put(PList.class,new JJPListWriter());
        customWriters.put(PSet.class,new JJPSetWriter());
        customWriters.put(PMap.class,new JJPMapWriter());
        JJExceptionWriter exceptionWriter = new JJExceptionWriter();
        generalWriters.put(Throwable.class,exceptionWriter);
    }

    public JJDefaultWriter addMapper(JJWriterObjectMapper mapper){
        mappers.add(mapper);
        return this;
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

    private class JJWriterWriter implements JJObjectWriter.JJMasterWriter{
        private Set<Object> done = new HashSet<>();
        private Map<CachedNode,JJNode> existing = new HashMap<>();



        public JJNode write(Object value) {
            if(value == null){
                return JJNodeNull.Null;
            }
            for(JJWriterObjectMapper mapper : mappers){
                value = mapper.map(value);
            }
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

            JJObjectWriter writer = customWriters.get(value.getClass());
            if(writer == null){
                for(Map.Entry<Class<?>,JJObjectWriter> entry : generalWriters.entrySet()){
                    if(entry.getKey().isAssignableFrom(value.getClass())){
                        customWriters.put(value.getClass(),entry.getValue());
                        writer = entry.getValue();
                        break;
                    }
                }
                if(writer == null)
                {
                    writer = createDefaultWriter(value.getClass());
                }
                customWriters.put(value.getClass(),writer);
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



    private JJObjectWriter createDefaultWriter(Class<?> cls){
        return new JJReflectionObjectWriter(cls);
    }

}
