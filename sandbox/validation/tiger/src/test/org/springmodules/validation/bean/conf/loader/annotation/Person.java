/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.validation.bean.conf.loader.annotation;

import java.util.Date;
import java.util.List;

import org.springmodules.validation.bean.conf.loader.annotation.handler.CascadeValidation;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Expression;
import org.springmodules.validation.bean.conf.loader.annotation.handler.ExpressionScope;
import org.springmodules.validation.bean.conf.loader.annotation.handler.InThePast;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Length;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Min;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotEmpty;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Validator;

/**
 * A Bean to test the validation annotations with.
 *
 * @author Uri Boness
 */
@Validator(PersonValidator.class)
@Expression("father is not null")
public class Person {

    @NotNull @Length(min = 2, max = 10)
    private String firstName;

    @NotNull @Length(min = 2, max = 20)
    private String lastName;

    @NotNull @NotBlank
    private String nickname;

    @NotNull @InThePast()
    private Date birthday;

    @NotNull @NotEmpty
    private List<Person> friends;

    @NotNull @CascadeValidation
    private Person father;

    @NotNull @CascadeValidation
    private Person mother;

    private boolean homeless;

    @CascadeValidation("homeless == false")
    private Address address;

    @Expression(value = "? >= 0", applyIf = "true = false", scope = ExpressionScope.VALIDATED_VALUE)
    @Min(value = 0, errorCode = "just.another.error.code")
    private int age;

    @Length(min = 5)
    private String nullableString;

    @Min(10)
    private Integer nullableInteger;

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

    public List<Person> getFriends() {
        return friends;
    }

    public void setFriends(List<Person> friends) {
        this.friends = friends;
    }

    public Person getFather() {
        return father;
    }

    public void setFather(Person father) {
        this.father = father;
    }

    public Person getMother() {
        return mother;
    }

    public void setMother(Person mother) {
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

}
