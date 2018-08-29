package ru.use.marathon.utils;

public class Person {
    String personId;
    String personName;
    String personEmail;
    String personPhone;
    String personCity;

    public Person(){

    }

    public Person(String personId, String personName, String personEmail, String personPhone, String personCity) {
        this.personId = personId;
        this.personName = personName;
        this.personEmail = personEmail;
        this.personPhone = personPhone;
        this.personCity = personCity;
    }

    public String getPersonId() {
        return personId;
    }

    public String getPersonName() {
        return personName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public String getPersonPhone() {
        return personPhone;
    }

    public String getPersonCity() {
        return personCity;
    }
}
