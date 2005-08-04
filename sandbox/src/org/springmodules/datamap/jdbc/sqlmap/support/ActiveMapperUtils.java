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

package org.springmodules.datamap.jdbc.sqlmap.support;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.jdbc.support.incrementer.HsqlMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.PostgreSQLSequenceMaxValueIncrementer;
import org.springmodules.datamap.jdbc.sqlmap.PersistentField;
import org.springmodules.datamap.jdbc.sqlmap.PersistentObject;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Suppport functionality used by ActiveMapper.
 *
 * @author Thomas Risberg
 * @since 0.3
 */
public class ActiveMapperUtils {

    public static String underscoreName(String name) {
        return name.substring(0,1).toLowerCase() + name.substring(1).replaceAll("([A-Z])", "_$1").toLowerCase();
    }

    public static String getterName(String fieldName) {
        return "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
    }

    public static String setterName(String columnName) {
        StringTokenizer tokenizer = new StringTokenizer(columnName, "_");
        StringBuffer propertyName = new StringBuffer();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            propertyName.append(token.substring(0, 1).toUpperCase());
            propertyName.append(token.substring(1));
        }
        return "set" + propertyName.toString();
    }

    public static PersistentObject getPersistenceMetaData(Class clazz, final DataSource dataSource, Map pluralExceptions) {
        final PersistentObject newPo = new PersistentObject();
        String baseName = clazz.getName().toLowerCase();
        baseName = baseName.substring(baseName.lastIndexOf(".") + 1);
        String tableName;
        if (pluralExceptions.containsKey(baseName)) {
            tableName = (String) pluralExceptions.get(baseName);
        }
        else {
            tableName = baseName + "s";
        }
        newPo.setBaseName(baseName);
        newPo.setTableName(tableName);
        final String sqlTableName = tableName;
        final Map newMetaData = new HashMap(10);
        Field[] f = clazz.getDeclaredFields();
        for (int i = 0; i < f.length; i++) {
            PersistentField pf = new PersistentField();
            pf.setFieldName(f[i].getName());
            pf.setColumnName(ActiveMapperUtils.underscoreName(f[i].getName()));
            pf.setJavaType(f[i].getType());
            newMetaData.put(pf.getColumnName(), pf);
        }

        try {
            JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback() {
                public Object processMetaData(DatabaseMetaData databaseMetaData) throws SQLException {
                    newPo.setUsingLowerCaseIdentifiers(databaseMetaData.storesLowerCaseIdentifiers());
                    newPo.setDatabaseProductName(databaseMetaData.getDatabaseProductName());
                    if ("PostgreSQL".equals(newPo.getDatabaseProductName())) {
                        newPo.setUsingGeneratedKeysStrategy(false);
                        newPo.setIncrementer(new PostgreSQLSequenceMaxValueIncrementer(dataSource, newPo.getBaseName() + "_seq"));
                    }
                    else if ("HSQL Database Engine".equals(newPo.getDatabaseProductName())) {
                        newPo.setUsingGeneratedKeysStrategy(false);
                        newPo.setIncrementer(new HsqlMaxValueIncrementer(dataSource, newPo.getBaseName() + "_seq", "value"));
                    }
                    else {
                        newPo.setUsingGeneratedKeysStrategy(true);
                    }
                    String metaDataTableName;
                    if (newPo.isUsingLowerCaseIdentifiers())
                        metaDataTableName = sqlTableName.toLowerCase();
                    else
                        metaDataTableName = sqlTableName.toUpperCase();
                        ResultSet crs = databaseMetaData.getColumns(null, null, metaDataTableName, null);
                    while (crs.next()) {
                        PersistentField pf = (PersistentField)newMetaData.get(crs.getString(4).toLowerCase());
                        if (pf != null)
                            pf.setSqlType(crs.getInt(5));
                    }
                    return null;
                }
            });
        } catch (MetaDataAccessException e) {
            throw new DataAccessResourceFailureException("Error retreiving metadata", e);
        }
        newPo.setPersistentFields(newMetaData);
        return newPo;
    }
}
