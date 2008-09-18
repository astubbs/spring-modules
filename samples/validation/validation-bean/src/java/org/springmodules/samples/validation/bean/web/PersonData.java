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

package org.springmodules.samples.validation.bean.web;

import org.springmodules.validation.bean.conf.loader.annotation.handler.*;

/**
 *
 * @author Uri Boness
 */
public class PersonData {

    @NotBlank
    @Length(min = 3, max = 5)
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @Expression(value = "verifyPassword == password")
    private String verifyPassword;

    private int luckyNumber;

    private int unluckyNumber;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    public int getLuckyNumber() {
        return luckyNumber;
    }

    public void setLuckyNumber(int luckyNumber) {
        this.luckyNumber = luckyNumber;
    }

    public int getUnluckyNumber() {
        return unluckyNumber;
    }

    public void setUnluckyNumber(int unluckyNumber) {
        this.unluckyNumber = unluckyNumber;
    }

    //============================================= Validation Methods =================================================

    @ValidationMethod(forProperty = "luckyNumber", args = "0,10")
    private boolean validateLuckyNumber() {
        return luckyNumber >= 0 && luckyNumber <= 10;
    }

    @ValidationMethod(args = "100,200")
    private boolean validateUnluckyNumber() {
        return unluckyNumber >= 100 && unluckyNumber <= 200;
    }
}
