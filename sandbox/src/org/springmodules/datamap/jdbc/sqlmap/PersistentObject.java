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

import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

import java.util.Map;

/**
 * Class to hold information we need for any persistent classes
 *
 * @author Thomas Risberg
 * @since 0.3
 */
public class PersistentObject {

    private String tableName;
    private String baseName;
    private String databaseProductName;
    private boolean usingGeneratedKeysStrategy;
    private DataFieldMaxValueIncrementer incrementer;
    private boolean usingLowerCaseIdentifiers;
    private Map persistentFields;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getDatabaseProductName() {
        return databaseProductName;
    }

    public void setDatabaseProductName(String databaseProductName) {
        this.databaseProductName = databaseProductName;
    }

    public boolean isUsingLowerCaseIdentifiers() {
        return usingLowerCaseIdentifiers;
    }

    public boolean isUsingGeneratedKeysStrategy() {
        return usingGeneratedKeysStrategy;
    }

    public void setUsingGeneratedKeysStrategy(boolean usingGeneratedKeysStrategy) {
        this.usingGeneratedKeysStrategy = usingGeneratedKeysStrategy;
    }

    public DataFieldMaxValueIncrementer getIncrementer() {
        return incrementer;
    }

    public void setIncrementer(DataFieldMaxValueIncrementer incrementer) {
        this.incrementer = incrementer;
    }

    public void setUsingLowerCaseIdentifiers(boolean usingLowerCaseIdentifiers) {
        this.usingLowerCaseIdentifiers = usingLowerCaseIdentifiers;
    }

    public Map getPersistentFields() {
        return persistentFields;
    }

    public void setPersistentFields(Map persistentFields) {
        this.persistentFields = persistentFields;
    }
    
}
