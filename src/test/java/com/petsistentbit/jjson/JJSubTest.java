package com.petsistentbit.jjson;

/**
 * User: petermuys
 * Date: 22/07/16
 * Time: 11:04
 */
public class JJSubTest {
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
}
