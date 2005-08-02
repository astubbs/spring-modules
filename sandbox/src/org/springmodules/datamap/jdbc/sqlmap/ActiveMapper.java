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

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.jdbc.support.incrementer.HsqlMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.PostgreSQLSequenceMaxValueIncrementer;
import org.springmodules.datamap.dao.DataMapper;
import org.springmodules.datamap.dao.Operators;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;
import java.util.*;

/**
 * Implementation of DataMapper supporting metadata based default mappings to/from
 * database tables and columns to JavaBean compliant java classes.
 *
 * @author Thomas Risberg
 * @since 0.3
 * @see org.springframework.jdbc.core.support.JdbcDaoSupport
 */
public class ActiveMapper extends JdbcDaoSupport implements DataMapper {

    private Class mappedClass;
    private PersistentObject persistentObject;
//    private String plainTableName;
    private String tableNameToUse;
    private boolean overrideTableName = false;
    private Constructor defaultConstruct;
    private Map pluralExceptions;

    public ActiveMapper() {
    }

    public ActiveMapper(Class clazz) {
        setMappedClass(clazz);
    }

    public void setMappedClass(Class clazz) throws DataAccessResourceFailureException {
        this.mappedClass = clazz;
        try {
            defaultConstruct = clazz.getConstructor(null);
        } catch (NoSuchMethodException e) {
            throw new DataAccessResourceFailureException(new StringBuffer().append("Failed to access default constructor of ").append(clazz.getName()).toString(), e);
        }
    }

    public void setPluralExceptions(Map pluralExceptions) {
        this.pluralExceptions = pluralExceptions;
    }

    protected void initDao() throws Exception {
        pluralExceptions = new HashMap(2);
        pluralExceptions.put("address", "addresses");
        pluralExceptions.put("country", "countries");
        persistentObject = getPersistenceMetaData(mappedClass);
        tableNameToUse = persistentObject.getTableName();
    }

    public void setTableNameToUse(String tableNameToUse) {
        if (overrideTableName)
            throw new InvalidDataAccessApiUsageException("Table name can only be overriden once");
        this.tableNameToUse = tableNameToUse;
        this.overrideTableName = true;
    }

    public String getTableNameToUse() {
        return tableNameToUse;
    }

    protected PersistentObject getPersistentObject() {
        return persistentObject;
    }

    public Object find(Object id) {
        if (persistentObject == null)
            throw new InvalidDataAccessApiUsageException("Persistent class not specified");
        String sql = "select * from " + tableNameToUse + " where id = ?";
        List l = getJdbcTemplate().query(sql, new Object[] {id}, new ActiveRowMapper(mappedClass, persistentObject));
        if (l.size() > 0)
            return l.get(0);
        else
            return null;
    }

    public List findAll() {
        if (persistentObject == null)
            throw new InvalidDataAccessApiUsageException("Persistent class not specified");
        String sql = "select * from " + tableNameToUse;
        return getJdbcTemplate().query(sql, new ActiveRowMapper(mappedClass, persistentObject));
    }

    protected List findByWhereClause(String whereClause, Object[] arguments) {
        if (persistentObject == null)
            throw new InvalidDataAccessApiUsageException("Persistent class not specified");
        String sql = "select * from " + tableNameToUse + " where " + whereClause;
        return getJdbcTemplate().query(sql, arguments, new ActiveRowMapper(mappedClass, persistentObject));
    }

    public List findBy(String field, int operator, Object argument) {
        if (persistentObject == null)
            throw new InvalidDataAccessApiUsageException("Persistent class not specified");
        StringBuffer whereClause = new StringBuffer();
        StringBuffer endClause = new StringBuffer();
        switch (operator) {
            case Operators.EQUALS:
                whereClause.append(underscoreName(field)).append(" = ");
                break;
            case Operators.LESS_THAN:
                whereClause.append(underscoreName(field)).append(" < ");
                break;
            case Operators.GREATER_THAN:
                whereClause.append(underscoreName(field)).append(" > ");
                break;
            case Operators.LESS_THAN_OR_EQUAL:
                whereClause.append(underscoreName(field)).append(" <= ");
                break;
            case Operators.GREATER_THAN_OR_EQUAL:
                whereClause.append(underscoreName(field)).append(" >= ");
                break;
            case Operators.BETWEEN:
                whereClause.append(underscoreName(field)).append(" between ");
                break;
            case Operators.IN:
                whereClause.append(underscoreName(field)).append(" in (");
                break;
            case Operators.STARTS_WITH:
                whereClause.append(underscoreName(field)).append(" LIKE ");
                if (argument instanceof String) {
                    argument = argument + "%";
                }
                break;
            case Operators.ENDS_WITH:
                whereClause.append(underscoreName(field)).append(" LIKE ");
                if (argument instanceof String) {
                    argument = "%" + argument;
                }
                break;
            case Operators.CONTAINS:
                whereClause.append(underscoreName(field)).append(" LIKE ");
                if (argument instanceof String) {
                    argument = "%" + argument + "%";
                }
                break;
        }
        Object[] arguments;
        if (argument instanceof Object[]) {
            arguments = (Object[])argument;
            for (int i = 0; i < arguments.length; i++) {
                if (i > 0)
                    whereClause.append(", ");
                whereClause.append("?");
            }
            endClause.append(")");
        }
        else {
            arguments = new Object[] {argument};
            whereClause.append("?");
        }
        whereClause.append(endClause);
        return findByWhereClause(whereClause.toString(), arguments);
    }

    public void save(Object o) {
        Map mappedFields = new HashMap(10);
        Map unmappedFields = new HashMap(10);
        List mappedColumns = new LinkedList();
        List mappedValues = new LinkedList();
        List idValue = new LinkedList();
        Set fieldSet = persistentObject.getPersistentFields().entrySet();
        boolean hasId = false;
        for (Iterator i = fieldSet.iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry)i.next();
            PersistentField pf = (PersistentField)e.getValue();
            if (pf.getSqlType() > 0) {
                mappedFields.put(pf.getFieldName(), e);
                try {
                    Method m = o.getClass().getMethod(getterName(pf.getFieldName()), new Class[] {});
                    Object r = m.invoke(o, new Object[] {});
                    if ("id".equals(pf.getFieldName())) {
                        if (r != null) {
                            idValue.add(r);
                            hasId = true;
                        }
                    }
                    else {
                        mappedColumns.add(pf.getFieldName());
                        mappedValues.add(r);
                    }
                } catch (NoSuchMethodException e1) {
                    throw new DataAccessResourceFailureException(new StringBuffer().append("Failed to map field ").append(pf.getFieldName()).append(".").toString(), e1);
                } catch (IllegalAccessException e1) {
                    throw new DataAccessResourceFailureException(new StringBuffer().append("Failed to map field ").append(pf.getFieldName()).append(".").toString(), e1);
                } catch (InvocationTargetException e1) {
                    throw new DataAccessResourceFailureException(new StringBuffer().append("Failed to map field ").append(pf.getFieldName()).append(".").toString(), e1);
                }

            }
            else {
                unmappedFields.put(pf.getFieldName(), e);
            }
        }
        List additionalColumns = new LinkedList();
        Object[] additionalValues = completeMappingOnSave(o, additionalColumns, mappedFields, unmappedFields);
        StringBuffer sql = new StringBuffer();
        if (hasId) {
            sql.append("update ").append(tableNameToUse).append(" set ");
            for (int i = 0; i < mappedColumns.size(); i++) {
                if (i > 0)
                    sql.append(", ");
                sql.append(mappedColumns.get(i));
                sql.append(" = ?");
            }
            sql.append(" where id = ?");
        }
        else {
            idValue.add(assignNewId(o));
            StringBuffer placeholders = new StringBuffer();
            sql.append("insert into ");
            sql.append(tableNameToUse);
            sql.append(" (");
            sql.append("id");
            placeholders.append("?");
            for (int i = 0; i < mappedColumns.size(); i++) {
                sql.append(", ");
                sql.append(mappedColumns.get(i));
                placeholders.append(", ?");
            }
            sql.append(") values(");
            sql.append(placeholders);
            sql.append(")");
        }
        Object[] values = new Object[mappedValues.size() + idValue.size() + additionalValues.length];
        int vix = 0;
        if (!hasId) {
            for (int i = 0; i < idValue.size(); i++) {
                values[vix++] = idValue.get(i);
            }
        }
        for (int i = 0; i < mappedValues.size(); i++) {
            values[vix++] = mappedValues.get(i);
        }
        for (int i = 0; i < additionalValues.length; i++) {
            values[vix++] = additionalValues[i];
        }
        if (hasId) {
            for (int i = 0; i < idValue.size(); i++) {
                values[vix++] = idValue.get(i);
            }
        }
        getJdbcTemplate().update(sql.toString(), values);
    }

    public void delete(Object o) {
        String sql = "delete from " + tableNameToUse + " where id = ?";
        PersistentField pf = (PersistentField)persistentObject.getPersistentFields().get("id");
        Object[] idValue = new Object[1];
        try {
            Method m = o.getClass().getMethod(getterName(pf.getFieldName()), new Class[] {});
            idValue[0] = m.invoke(o, new Object[] {});
        } catch (NoSuchMethodException e1) {
            throw new DataAccessResourceFailureException(new StringBuffer().append("Failed to map field ").append(pf.getFieldName()).append(".").toString(), e1);
        } catch (IllegalAccessException e1) {
            throw new DataAccessResourceFailureException(new StringBuffer().append("Failed to map field ").append(pf.getFieldName()).append(".").toString(), e1);
        } catch (InvocationTargetException e1) {
            throw new DataAccessResourceFailureException(new StringBuffer().append("Failed to map field ").append(pf.getFieldName()).append(".").toString(), e1);
        }
        getJdbcTemplate().update(sql, idValue);
    }

    protected Object completeMappingOnFind(Object result, ResultSet rs, int rowNumber, Map mappedColumns, Map unmappedColumns) throws SQLException {
        return result;
    }

    protected Object[] completeMappingOnSave(Object input, List columns, Map mappedFields, Map unmappedFields) {
        return new Object[] {};
    }

    protected Object assignNewId(Object o) {
        PersistentField pf = (PersistentField)persistentObject.getPersistentFields().get("id");
        Object newId = null;
        if (pf.getJavaType() == Long.class) {
            newId = new Long(persistentObject.getIncrementer().nextLongValue());
        }
        else if (pf.getJavaType() == Integer.class) {
            newId = new Integer(persistentObject.getIncrementer().nextIntValue());
        }
        try {
            Method m = o.getClass().getMethod(setterName(pf.getFieldName()), new Class[] {newId.getClass()});
            m.invoke(o, new Object[] {newId});
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        }
        return newId;
    }

    private PersistentObject getPersistenceMetaData(Class clazz) {
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
            pf.setColumnName(underscoreName(f[i].getName()));
            pf.setJavaType(f[i].getType());
            newMetaData.put(pf.getColumnName(), pf);
        }

        try {
            JdbcUtils.extractDatabaseMetaData(getDataSource(), new DatabaseMetaDataCallback() {
                public Object processMetaData(DatabaseMetaData databaseMetaData) throws SQLException {
                    newPo.setUsingLowerCaseIdentifiers(databaseMetaData.storesLowerCaseIdentifiers());
                    newPo.setDatabaseProductName(databaseMetaData.getDatabaseProductName());
                    if ("PostgreSQL".equals(newPo.getDatabaseProductName())) {
                        newPo.setUsingGeneratedKeysStrategy(false);
                        newPo.setIncrementer(new PostgreSQLSequenceMaxValueIncrementer(getDataSource(), newPo.getBaseName() + "_seq"));
                    }
                    else if ("HSQL Database Engine".equals(newPo.getDatabaseProductName())) {
                        newPo.setUsingGeneratedKeysStrategy(false);
                        newPo.setIncrementer(new HsqlMaxValueIncrementer(getDataSource(), newPo.getBaseName() + "_seq", "nextval"));
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

    private String underscoreName(String name) {
        return name.substring(0,1).toLowerCase() + name.substring(1).replaceAll("([A-Z])", "_$1").toLowerCase();
    }

    private String getterName(String fieldName) {
        return "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
    }

    private String setterName(String columnName) {
        StringTokenizer tokenizer = new StringTokenizer(columnName, "_");
        StringBuffer propertyName = new StringBuffer();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            propertyName.append(token.substring(0, 1).toUpperCase());
            propertyName.append(token.substring(1));
        }
        return "set" + propertyName.toString();
    }

    private class ActiveRowMapper implements RowMapper {
        private final Class clazz;
        private final PersistentObject persistentObject;

        public ActiveRowMapper(Class clazz, PersistentObject persistentObject) {
            this.clazz = clazz;
            this.persistentObject = persistentObject;
        }

        public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
            Object result;
            try {
                result = defaultConstruct.newInstance(null);
            } catch (IllegalAccessException e) {
                throw new DataAccessResourceFailureException("Failed to load class " + clazz.getName(), e);
            } catch (InvocationTargetException e) {
                throw new DataAccessResourceFailureException("Failed to load class " + clazz.getName(), e);
            } catch (InstantiationException e) {
                throw new DataAccessResourceFailureException("Failed to load class " + clazz.getName(), e);
            }
            ResultSetMetaData meta = rs.getMetaData();
            int columns = meta.getColumnCount();
            Map mappedColumns = new HashMap(10);
            Map unmappedColumns = new HashMap(10);
            for (int x = 1; x <= columns; x++) {
                String field = meta.getColumnName(x).toLowerCase();
                PersistentField fieldMeta = (PersistentField)persistentObject.getPersistentFields().get(field);
                if (fieldMeta != null) {
                    Object value = null;
                    Method m = null;
                    try {
                        if (fieldMeta.getJavaType().equals(String.class)) {
                            m = result.getClass().getMethod(setterName(fieldMeta.getColumnName()), new Class[] {String.class});
                            value = rs.getString(x);
                        }
                        else if (fieldMeta.getJavaType().equals(Long.class)) {
                            m = result.getClass().getMethod(setterName(fieldMeta.getColumnName()), new Class[] {Long.class});
                            value = new Long(rs.getLong(x));
                        }
                        else if (fieldMeta.getJavaType().equals(BigDecimal.class)) {
                            m = result.getClass().getMethod(setterName(fieldMeta.getColumnName()), new Class[] {BigDecimal.class});
                            value = rs.getBigDecimal(x);
                        }
                        else if (fieldMeta.getJavaType().equals(Date.class)) {
                            m = result.getClass().getMethod(setterName(fieldMeta.getColumnName()), new Class[] {Date.class});
                            if (fieldMeta.getSqlType() == Types.DATE) {
                                value = rs.getDate(x);
                            }
                            else {
                                value = rs.getTimestamp(x);
                            }
                        }
                        else {
                            unmappedColumns.put(fieldMeta.getColumnName(), fieldMeta);
                        }
                        if (m != null) {
                            m.invoke(result , new Object[] {value});
                            mappedColumns.put(meta.getColumnName(x), meta);
                        }
                    } catch (NoSuchMethodException e) {
                        throw new DataAccessResourceFailureException(new StringBuffer().append("Failed to map field ").append(fieldMeta.getFieldName()).append(".").toString(), e);
                    } catch (IllegalAccessException e) {
                        throw new DataAccessResourceFailureException(new StringBuffer().append("Failed to map field ").append(fieldMeta.getFieldName()).append(".").toString(), e);
                    } catch (InvocationTargetException e) {
                        throw new DataAccessResourceFailureException(new StringBuffer().append("Failed to map field ").append(fieldMeta.getFieldName()).append(".").toString(), e);
                    }
                }
                else {
                    unmappedColumns.put(meta.getColumnName(x), meta);
                }
            }
            return completeMappingOnFind(result, rs, rowNumber, mappedColumns, unmappedColumns);
        }
    }
}
