package com.petsistentbit.jjson;

import com.persistentbit.core.utils.ImTools;

/**
 * User: petermuys
 * Date: 22/07/16
 * Time: 11:04
 */
public class JJSubTest {
    static private final ImTools<JJSubTest> im = ImTools.get(JJSubTest.class);
    private final int id;
    private final String name;



    public JJSubTest(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "JJSubTest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return im.hashCodeAll(this);
    }

    @Override
    public boolean equals(Object obj) {
        return im.equalsAll(this,obj);
    }
}
