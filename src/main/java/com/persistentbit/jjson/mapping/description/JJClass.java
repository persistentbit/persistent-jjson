package com.persistentbit.jjson.mapping.description;

import com.persistentbit.core.utils.BaseValueClass;



/**
 * Created by petermuys on 9/09/16.
 */
public class JJClass extends BaseValueClass{
    private final String packageName;
    private final String className;

    public JJClass(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }

    public JJClass(String fullClassName){
        int i = fullClassName.lastIndexOf('.');

        this.packageName = i == -1 ? "" : fullClassName.substring(0,i);
        this.className = i == -1 ? fullClassName : fullClassName.substring(i+1);
    }

    public JJClass(Class<?> cls){
        this(cls.getName());
    }

    public String getFullJavaName() {
        return this.packageName + "." + this.className;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String toString() {
        return getPackageName() + "/" + getClassName();
    }
}
