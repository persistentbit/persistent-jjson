package com.persistentbit.jjson.mapping.description;

import com.persistentbit.core.collections.PList;
import com.persistentbit.core.collections.PSet;
import com.persistentbit.core.utils.BaseValueClass;
import com.persistentbit.core.utils.ImTools;
import com.persistentbit.core.utils.NoEqual;

/**
 */
public class JJTypeDescription extends BaseValueClass{
    private final JJTypeSignature typeSignature;
    @NoEqual private final PList<String> doc;
    @NoEqual private final PList<JJPropertyDescription>  properties;


    public JJTypeDescription(JJTypeSignature typeSignature){
        this(typeSignature,PList.empty());
    }

    public JJTypeDescription(JJTypeSignature typeSignature, PList<String> doc){
        this(typeSignature,doc,PList.empty());
    }

    public JJTypeDescription(JJTypeSignature typeSignature, PList<String> doc,PList<JJPropertyDescription> properties) {
        this.typeSignature = typeSignature;
        this.doc = doc;
        this.properties = properties;
    }

    public JJTypeDescription withIsArray(boolean isArray){
        ImTools<JJTypeDescription> im = ImTools.get(JJTypeDescription.class);
        return im.copyWith(this,"isArray",isArray);
    }

    public JJTypeSignature getTypeSignature() {
        return typeSignature;
    }

    public PList<String> getDoc() {
        return doc;
    }

    public PList<JJPropertyDescription> getProperties() {
        return properties;
    }


    public PSet<String> getAllUsedClassNames() {
        return properties.map(i -> i.getAllUsedClassNames()).join((a,b)-> a.plusAll(b)).orElse(PSet.empty()).plusAll(typeSignature.getAllUsedClassNames());
    }
}
