package org.springmodules.validation.bean;

/**
 * @author Uri Boness
 */
public class Person {

    private String name;

    private Address address;

    private boolean homeless;

    private String phone;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setHomeless(boolean homeless) {
        this.homeless = homeless;
    }

    public boolean isHomeless() {
        return homeless;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
