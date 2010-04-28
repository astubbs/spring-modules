package org.springmodules.validation.bean.conf.loader.annotation;

import org.springmodules.validation.bean.conf.loader.annotation.handler.*;

import java.util.Date;
import java.util.List;

/**
 * A Bean to test the validation annotations with.
 *
 * @author Uri Boness
 */
@Expression(value = "father is not null", contexts = "ctx1")
public class PersonWithContext {

    @NotNull(contexts = "ctx1")
    @Length(min = 2, max = 10, contexts = "ctx1")
    private String firstName;

    @NotNull(contexts = "ctx1")
    @Length(min = 2, max = 20, contexts = "ctx1")
    private String lastName;

    @NotNull(contexts = "ctx1")
    @NotBlank(contexts = "ctx1")
    private String nickname;

    @NotNull(contexts = "ctx1")
    @InThePast(contexts = "ctx1")
    private Date birthday;

    @NotNull(contexts = "ctx1")
    @NotEmpty(contexts = "ctx1")
    private List<PersonWithContext> friends;

    @NotNull(contexts = "ctx1")
    @CascadeValidation
    private PersonWithContext father;

    @NotNull(contexts = "ctx2")
    @CascadeValidation
    private PersonWithContext mother;

    private boolean homeless;

    @CascadeValidation("homeless == false")
    private Address address;

    @Expression(value = "? >= 0", applyIf = "true = false", scope = ExpressionScope.VALIDATED_VALUE, contexts = "ctx2")
    @Min(value = 0, errorCode = "just.another.error.code", contexts = "ctx2")
    private int age;

    @Length(min = 5, contexts = "ctx2")
    private String nullableString;

    @Min(value = 10, contexts = "ctx2")
    private Integer nullableInteger;

    @Min(value = 10, applyIf = "firstName is not null", contexts = "ctx2")
    private int smallInteger = 5;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<PersonWithContext> getFriends() {
        return friends;
    }

    public void setFriends(List<PersonWithContext> friends) {
        this.friends = friends;
    }

    public PersonWithContext getFather() {
        return father;
    }

    public void setFather(PersonWithContext father) {
        this.father = father;
    }

    public PersonWithContext getMother() {
        return mother;
    }

    public void setMother(PersonWithContext mother) {
        this.mother = mother;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isHomeless() {
        return homeless;
    }

    public void setHomeless(boolean homeless) {
        this.homeless = homeless;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getNullableString() {
        return nullableString;
    }

    public void setNullableString(String nullableString) {
        this.nullableString = nullableString;
    }

    public Integer getNullableInteger() {
        return nullableInteger;
    }

    public void setNullableInteger(Integer nullableInteger) {
        this.nullableInteger = nullableInteger;
    }

    public int getSmallInteger() {
        return smallInteger;
    }

    public void setSmallInteger(int smallInteger) {
        this.smallInteger = smallInteger;
    }

    @ValidationMethod(contexts = "ctx1")
    public boolean validate() {
        return false;
    }

    @ValidationMethod(forProperty = "birthday", contexts = "ctx2")
    public boolean validateBirthdayIsNull() {
        return birthday == null;
    }

}
