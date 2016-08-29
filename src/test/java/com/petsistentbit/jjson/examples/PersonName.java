package com.petsistentbit.jjson.examples;

import java.util.Optional;

/**
 * @author Peter Muys
 * @since 29/08/2016
 */
public class PersonName {
    private final String firstName;
    private final String middleName;
    private final String lastName;

    public PersonName(String firstName, String lastName) {
        this(firstName,null,lastName);
    }
    public PersonName(String firstName,String middleName,String lastName){
        this.firstName  = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Optional<String> getMiddleName() {
        return Optional.ofNullable(middleName);
    }

    public String getLastName() {
        return lastName;
    }
}
