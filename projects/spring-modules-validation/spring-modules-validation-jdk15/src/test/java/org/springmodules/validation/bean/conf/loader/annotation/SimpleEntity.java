/*
 * Copyright 2002-2007 the original author or authors.
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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * @author Uri Boness
 */
public class SimpleEntity {

    @Basic(optional = false)
    private String name;

    @Column(nullable = false, length = 10)
    private String text;

    // making sure that the length constraint is not picked up for non-stirng properties.
    @Column(length = 30)
    private int number;

    @OneToOne(optional = false)
    private SimpleEntity oneToOne;

    @ManyToOne(optional = false)
    private SimpleEntity manyToOne;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SimpleEntity getOneToOne() {
        return oneToOne;
    }

    public void setOneToOne(SimpleEntity oneToOne) {
        this.oneToOne = oneToOne;
    }

    public SimpleEntity getManyToOne() {
        return manyToOne;
    }

    public void setManyToOne(SimpleEntity manyToOne) {
        this.manyToOne = manyToOne;
    }
}
