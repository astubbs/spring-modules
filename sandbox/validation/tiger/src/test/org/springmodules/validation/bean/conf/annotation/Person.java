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

package org.springmodules.validation.bean.conf.annotation;

import java.util.Date;
import java.util.List;

import org.springmodules.validation.bean.conf.annotation.handler.*;

/**
 * A Bean to test the validation annotations with.
 *
 * @author Uri Boness
 */
@Validator(PersonValidator.class)
@Valang("father is not null")
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

    @NotNull @Valid
    private Person father;

    @NotNull @Valid
    private Person mother;

    @Valang(value = "? >= 0", applyIf = "true = false", scope = ExpressionScope.VALIDATED_VALUE)
    @Min(value = 0, errorCode = "just.another.error.code")
    private int age;


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

}
