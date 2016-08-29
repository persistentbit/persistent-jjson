package com.persistentbit.jjson.mapping.impl;

import com.persistentbit.core.Immutable;
import com.persistentbit.jjson.mapping.JJWriter;
import com.persistentbit.jjson.nodes.*;

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
    private final JJObjectWriterSupplier writerSupplier;

    public JJDefaultWriter(JJObjectWriterSupplier writerSupplier){
        this.writerSupplier = writerSupplier;
    }

    public JJDefaultWriter() {
        this(new JJObjectWriterSupplier().addCoreWriters());
    }


    public JJObjectWriterSupplier getWriterSupplier() {
        return writerSupplier;
    }

    public JJDefaultWriter withForClass(Class<?> cls,JJObjectWriter ow){
        return new JJDefaultWriter(writerSupplier.withForClass(cls,ow));
    }

    public JJDefaultWriter withAssignableTo(Class<?> clsAssignableTo,JJObjectWriter ow){
        return new JJDefaultWriter(writerSupplier.withAssignableTo(clsAssignableTo,ow));
    }

    public JJDefaultWriter    withNextSupplier(Function<Class<?>,JJObjectWriter>...next){
        return new JJDefaultWriter(writerSupplier.withNextSupplier(next));
    }

    public JJDefaultWriter withPrevSupplier(Function<Class<?>,JJObjectWriter>...prev){
        return new JJDefaultWriter(writerSupplier.withPrevSupplier(prev));
    }

    public JJDefaultWriter withFallbackSupplier(Function<Class<?>,JJObjectWriter> fallback){
        return new JJDefaultWriter(writerSupplier.withFallbackSupplier(fallback));
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
                throw new JJsonException("Cyclic data: " + value );
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

            JJObjectWriter writer = writerSupplier.apply(value.getClass());
            if(writer == null){
                throw new JJsonException("Don't know how to translate a " + value.getClass().getName() + " to json");
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
