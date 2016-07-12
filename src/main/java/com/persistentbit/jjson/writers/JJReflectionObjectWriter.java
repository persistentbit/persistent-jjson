package com.persistentbit.jjson.writers;

import com.persistentbit.core.properties.PropertyGetter;
import com.persistentbit.core.properties.PropertyGetterField;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeObject;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: petermuys
 * Date: 24/10/15
 * Time: 18:32
 */
public class JJReflectionObjectWriter implements JJObjectWriter{


    private static class Getter {
        public PropertyGetter getter;
        public String jsonName;
        public Getter(PropertyGetter getter, String jsonName){
            this.getter = getter;
            this.jsonName = jsonName;
        }

        @Override
        public String toString()
        {
            return "FieldGetter[" + jsonName + "]";
        }
    }

    private final Class<?> cls;
    private List<Getter> getters = new ArrayList<>();
    public JJReflectionObjectWriter(Class<?> cls){
        this.cls = cls;
        try{
            while(cls.equals(Object.class) == false){
                for(Field f : cls.getDeclaredFields()){
                    if(Modifier.isTransient(f.getModifiers())){
                        continue;
                    }
                    if(Modifier.isStatic(f.getModifiers())){
                        continue;
                    }
                    addPropertie(f);
                }
                cls = cls.getSuperclass();
            }

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    private void addPropertie(Field f){
        f.setAccessible(true);
        getters.add(new Getter(new PropertyGetterField(f),f.getName()));
    }
    @Override
    public JJNode write(Object value, JJMasterWriter master)
    {
        Map<String,JJNode> props = new LinkedHashMap<>();
        for(Getter g : getters){
            try
            {
                Object propValue = g.getter.get(value);
                JJNode propNode = master.write(propValue);
                /*if(g.getter.getPropertyClass().equals(Object.class) && propValue != null){
                    JJNodeString jcls = new JJNodeString(propValue.getClass().getName());
                    Map<String,JJNode> propsMap = new HashMap<>();
                    propsMap.put("objectClass",jcls);
                    propsMap.put("value",propNode);
                    JJNodeObject jobj = new JJNodeObject(propsMap);
                    props.put(g.jsonName, jobj);
                } else
                {
                    props.put(g.jsonName, propNode);
                }*/
                props.put(g.jsonName, propNode);
            }catch(Exception e){
                throw new RuntimeException("Error writing "+ g + " from " + value,e);
            }
        }
        JJNodeObject n = new JJNodeObject(props);
        return n;
    }
}
