/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.datamap.jdbc.sqlmap;

/**
 * Class to hold information we need for any values to persist
 *
 * @author Thomas Risberg
 * @since 0.3
 */
public class PersistentValue {

    private String columnName;
    private int sqlType;
    private Object value;
    private boolean idValue = false;

    public PersistentValue() {
    }

    public PersistentValue(String columnName, int sqlType, Object value) {
        this.columnName = columnName;
        this.sqlType = sqlType;
        this.value = value;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isIdValue() {
        return idValue;
    }

    public void setIdValue(boolean idValue) {
        this.idValue = idValue;
    }

    public String toString() {
        return this.columnName + " [" + this.sqlType + "] = " + value + (isIdValue() ? " (ID)" : "");
    }
}
