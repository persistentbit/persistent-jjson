package com.persistentbit.jjson.mapping.impl;

import com.persistentbit.core.Immutable;
import com.persistentbit.core.collections.PList;
import com.persistentbit.jjson.mapping.JJReader;
import com.persistentbit.jjson.nodes.*;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.function.Function;

/**
 *
 * @author Peter Muys
 */
@Immutable
public class JJDefaultReader  implements JJReader {
    JJObjectReaderSupplier supplier;
    public JJDefaultReader(JJObjectReaderSupplier supplier){
        this.supplier = supplier;
    }

    public JJDefaultReader() {
        this(new JJObjectReaderSupplier().addCoreReaders());
    }



    public <T>T read(JJNode node, Class<T> cls, Type type){
        boolean isNull = node.getType() == JJNode.JType.jsonNull;

        if(cls.equals(int.class) || cls.equals(Integer.class)){
            return isNull ? null : (T) Integer.valueOf(number(node).intValue());
        }
        if(cls.equals(long.class) || cls.equals(Long.class)){
            return isNull ? null :(T) Long.valueOf(number(node).longValue());
        }
        if(cls.equals(short.class) || cls.equals(Short.class)){
            return isNull ? null :(T) Short.valueOf(number(node).shortValue());
        }
        if(cls.equals(byte.class) || cls.equals(Byte.class)){
            return isNull ? null :(T) Byte.valueOf(number(node).byteValue());
        }
        if(cls.equals(float.class) || cls.equals(Float.class)){
            return isNull ? null : (T) Float.valueOf(number(node).floatValue());
        }
        if(cls.equals(double.class) || cls.equals(Double.class)){
            return isNull ? null :(T) Double.valueOf(number(node).doubleValue());
        }
        if(cls.equals(char.class) || cls.equals(Character.class)){
            return isNull ? null :(T) Character.valueOf(string(node).charAt(0));
        }
        if(cls.equals(String.class)){
            return isNull ? null :(T)string(node);
        }
        if(cls.equals(Boolean.class) || cls.equals(boolean.class)){
            return isNull ? null :(T) bool(node);
        }
        if(cls.equals(Class.class)){
            if(isNull) {
                return null;
            }
            try {

                return (T)this.getClass().getClassLoader().loadClass(((JJNodeString)node).getValue());

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if(type instanceof GenericArrayType){
            GenericArrayType gat = (GenericArrayType)type;
            return isNull ? null :(T) array(cls.getComponentType(),gat.getGenericComponentType(),node);
        }
        if(cls.isArray()){
            return isNull ? null : (T) array(cls.getComponentType(),cls.getComponentType(),node);
        }
        try {

            if(Enum.class.isAssignableFrom(cls)){
                JJObjectReader r = supplier.apply(cls);
                if(r != null){
                    return (T)r.read(type,node,this);
                }
                if(node.asNull().isPresent()){
                    return null;
                }
                String name = string(node);

                try {
                    return (T) cls.getDeclaredField(name).get(null);
                }catch(Exception e){
                    throw new JJsonException("Error reading field " + name + " for enum " + cls,e);
                }
            }

            JJObjectReader reader = supplier.apply(cls);
            return (T) reader.read(type, node, this);
        }catch(Exception e){
            throw new JJsonException("Error reading "  + node,e);
        }
    }

    private <T> Object array(Class<T> itemClass, Type itemType, JJNode node){
        if(node.getType() == JJNode.JType.jsonNull) { return null; }
        PList<JJNode> elements = ((JJNodeArray)node).pstream();
        if(itemClass.equals(int.class)){
            int[] result = new int[elements.size()];
            for(int t = 0; t< result.length;t++){
                result[t] = (Integer)read(elements.get(t),itemClass,itemType);
            }
            return result;
        } else if(itemClass.equals(boolean.class)){
            boolean[] result = new boolean[elements.size()];
            for(int t = 0; t< result.length;t++){
                result[t] = (Boolean)read(elements.get(t),itemClass,itemType);
            }
            return result;
        } else if(itemClass.equals(byte.class)){
            byte[] result = new byte[elements.size()];
            for(int t = 0; t< result.length;t++){
                result[t] = (Byte)read(elements.get(t),itemClass,itemType);
            }
            return result;
        } else if(itemClass.equals(char.class)){
            char[] result = new char[elements.size()];
            for(int t = 0; t< result.length;t++){
                result[t] = (Character)read(elements.get(t),itemClass,itemType);
            }
            return result;
        } else if(itemClass.equals(short.class)){
            short[] result = new short[elements.size()];
            for(int t = 0; t< result.length;t++){
                result[t] = (Short)read(elements.get(t),itemClass,itemType);
            }
            return result;
        } else if(itemClass.equals(long.class)){
            long[] result = new long[elements.size()];
            for(int t = 0; t< result.length;t++){
                result[t] = (Long)read(elements.get(t),itemClass,itemType);
            }
            return result;
        } else if(itemClass.equals(float.class)){
            float[] result = new float[elements.size()];
            for(int t = 0; t< result.length;t++){
                result[t] = (Float)read(elements.get(t),itemClass,itemType);
            }
            return result;
        } else if(itemClass.equals(double.class)){
            double[] result = new double[elements.size()];
            for(int t = 0; t< result.length;t++){
                result[t] = (Double)read(elements.get(t),itemClass,itemType);
            }
            return result;
        } else {
            T[] result = (T[]) Array.newInstance(itemClass, elements.size());
            for (int t = 0; t < result.length; t++) {
                result[t] = read(elements.get(t), itemClass, itemType);
            }
            return result;
        }
    }




    public Number number(JJNode node){
        if(node.getType() == JJNode.JType.jsonNull) { return null; }
        if(node.getType() == JJNode.JType.jsonString){
            try{
                return new BigDecimal(string(node));
            }catch (NumberFormatException nfe){
                throw new JJsonException(nfe);
            }
        }
        return ((JJNodeNumber)node).getValue();
    }
    public Boolean bool(JJNode node){
        if(node.getType() == JJNode.JType.jsonNull) { return null; }
        if(node.getType() == JJNode.JType.jsonString){
            return Boolean.valueOf(string(node));
        }
        return ((JJNodeBoolean)node).getValue();
    }

    public String string(JJNode node){
        if(node.getType() == JJNode.JType.jsonNull) { return null; }
        return ((JJNodeString)node).getValue();
    }


    public JJDefaultReader withForClass(Class<?> cls, JJObjectReader ow){
        return new JJDefaultReader(supplier.withForClass(cls,ow));
    }

    public JJDefaultReader withAssignableTo(Class<?> clsAssignableTo, JJObjectReader ow){
        return new JJDefaultReader(supplier.withAssignableTo(clsAssignableTo,ow));
    }

    public JJDefaultReader withNextSupplier(Function<Class<?>,JJObjectReader>...next){
        return new JJDefaultReader(supplier.withNextSupplier(next));
    }

    public JJDefaultReader withPrevSupplier(Function<Class<?>,JJObjectReader>...prev){
        return new JJDefaultReader(supplier.withPrevSupplier(prev));
    }

    public JJDefaultReader withFallbackSupplier(Function<Class<?>,JJObjectReader> fallBack){
        return new JJDefaultReader(supplier.withFallbackSupplier(fallBack));
    }

}
