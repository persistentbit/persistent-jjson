package com.persistentbit.jjson.mapping.description;

import com.persistentbit.core.collections.PList;
import com.persistentbit.core.collections.PSet;
import com.persistentbit.core.utils.BaseValueClass;
import com.persistentbit.core.utils.NoEqual;

/**
 * @author Peter Muys
 * @since 31/08/2016
 */
public class JJPropertyDescription extends BaseValueClass{
    private final String name;
    private final JJTypeSignature   typeSignature;
    @NoEqual  private final PList<String> doc;

    public JJPropertyDescription(String name, JJTypeSignature typeSignature, PList<String> doc) {
        this.name = name;
        this.typeSignature = typeSignature;
        this.doc = doc;
    }

    public String getName() {
        return name;
    }

    public JJTypeSignature getTypeSignature() {
        return typeSignature;
    }

    public PList<String> getDoc() {
        return doc;
    }

    public PSet<String> getAllUsedClassNames(){
        return typeSignature.getAllUsedClassNames();
    }
}
