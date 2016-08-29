package com.petsistentbit.jjson.examples;

import java.util.List;
import java.util.Map;

/**
 * @author Peter Muys
 * @since 29/08/2016
 */
public class Person {
    public enum Gender{
        male,female
    }

    private final int id;
    private final PersonName name;
    private final List<String> phones;
    private final Gender gender;
    private final Map<Address.AddressType,Address> addresses;
    private transient String notConverted = "transient values are note converted";

    public Person(int id, PersonName name, List<String> phones, Gender gender, Map<Address.AddressType,Address> addresses) {
        this.id = id;
        this.name = name;
        this.phones = phones;
        this.gender = gender;
        this.addresses = addresses;
    }

    public int getId() {
        return id;
    }

    public PersonName getName() {
        return name;
    }

    public List<String> getPhones() {
        return phones;
    }

    public Gender getGender() {
        return gender;
    }

    public Map<Address.AddressType, Address> getAddresses() {
        return addresses;
    }
}
