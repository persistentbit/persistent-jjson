package com.petsistentbit.jjson.examples;

/**
 * @author Peter Muys
 * @since 29/08/2016
 */
public class Address {
    public enum AddressType{
        home,work
    }

    private final String street;
    private final String city;
    private final String country;


    //@FieldNames(names = {"street","city","country"})  enable if you do not compile with the '-parameters' option.
    public Address(String street, String city, String country) {
        this.street = street;
        this.city = city;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
