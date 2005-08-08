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
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springmodules.datamap.dao.DataMapper;
import org.springmodules.datamap.dao.Operators;
import org.springmodules.datamap.jdbc.sqlmap.support.ActiveMapperUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.Date;
import java.sql.*;

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
    private String tableNameToUse;
    private boolean overrideTableName = false;
    private Map pluralExceptions;
    private ActiveRowMapper rowMapper;

    public ActiveMapper() {
    }

    public ActiveMapper(Class clazz) {
        setMappedClass(clazz);
    }

    public void setMappedClass(Class clazz) throws DataAccessResourceFailureException {
        this.mappedClass = clazz;
    }

    public void setPluralExceptions(Map pluralExceptions) {
        this.pluralExceptions = pluralExceptions;
    }

    public Map getPluralExceptions() {
        return pluralExceptions;
    }

    protected void initDao() throws Exception {
        pluralExceptions = new HashMap(2);
        pluralExceptions.put("address", "addresses");
        pluralExceptions.put("country", "countries");
        persistentObject = ActiveMapperUtils.getPersistenceMetaData(mappedClass, getDataSource(), getPluralExceptions());
        tableNameToUse = persistentObject.getTableName();
        rowMapper = new ActiveRowMapper(mappedClass, persistentObject);
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
        List l = getJdbcTemplate().query(sql, new Object[] {id}, rowMapper);
        if (l.size() > 0)
            return l.get(0);
        else
            return null;
    }

    public List findAll() {
        if (persistentObject == null)
            throw new InvalidDataAccessApiUsageException("Persistent class not specified");
        String sql = "select * from " + tableNameToUse;
        return getJdbcTemplate().query(sql, rowMapper);
    }

    protected List findByWhereClause(String whereClause, Object[] arguments) {
        if (persistentObject == null)
            throw new InvalidDataAccessApiUsageException("Persistent class not specified");
        String sql = "select * from " + tableNameToUse + " where " + whereClause;
        return getJdbcTemplate().query(sql, arguments, rowMapper);
    }

    public List findBy(String field, int operator, Object argument) {
        if (persistentObject == null)
            throw new InvalidDataAccessApiUsageException("Persistent class not specified");
        StringBuffer whereClause = new StringBuffer();
        StringBuffer endClause = new StringBuffer();
        switch (operator) {
            case Operators.EQUALS:
                whereClause.append(ActiveMapperUtils.underscoreName(field)).append(" = ");
                break;
            case Operators.LESS_THAN:
                whereClause.append(ActiveMapperUtils.underscoreName(field)).append(" < ");
                break;
            case Operators.GREATER_THAN:
                whereClause.append(ActiveMapperUtils.underscoreName(field)).append(" > ");
                break;
            case Operators.LESS_THAN_OR_EQUAL:
                whereClause.append(ActiveMapperUtils.underscoreName(field)).append(" <= ");
                break;
            case Operators.GREATER_THAN_OR_EQUAL:
                whereClause.append(ActiveMapperUtils.underscoreName(field)).append(" >= ");
                break;
            case Operators.BETWEEN:
                whereClause.append(ActiveMapperUtils.underscoreName(field)).append(" between ");
                break;
            case Operators.IN:
                whereClause.append(ActiveMapperUtils.underscoreName(field)).append(" in (");
                break;
            case Operators.STARTS_WITH:
                whereClause.append(ActiveMapperUtils.underscoreName(field)).append(" LIKE ");
                if (argument instanceof String) {
                    argument = argument + "%";
                }
                break;
            case Operators.ENDS_WITH:
                whereClause.append(ActiveMapperUtils.underscoreName(field)).append(" LIKE ");
                if (argument instanceof String) {
                    argument = "%" + argument;
                }
                break;
            case Operators.CONTAINS:
                whereClause.append(ActiveMapperUtils.underscoreName(field)).append(" LIKE ");
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

    protected Object completeMappingOnFind(Object result, ResultSet rs, int rowNumber, Map mappedColumns, Map unmappedColumns) throws SQLException {
        return result;
    }

    public void save(Object o) {
        Map mappedFields = new HashMap(10);
        Map unmappedFields = new HashMap(10);
        List columnValues = new LinkedList();
        List idValues = new LinkedList();
        Set fieldSet = persistentObject.getPersistentFields().entrySet();
        boolean isNewRow = false;
        for (Iterator i = fieldSet.iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry)i.next();
            PersistentField pf = (PersistentField)e.getValue();
            if (pf.getSqlType() > 0) {
                mappedFields.put(pf.getFieldName(), pf);
                try {
                    Method m = o.getClass().getMethod(ActiveMapperUtils.getterName(pf.getFieldName()), new Class[] {});
                    Object r = m.invoke(o, new Object[] {});
                    if (pf.isIdField()) {
                        idValues.add(new PersistentValue(pf.getColumnName(), pf.getSqlType(), r));
                        if (r == null) {
                            isNewRow = true;
                        }
                    }
                    else {
                        columnValues.add(new PersistentValue(pf.getColumnName(), pf.getSqlType(), r));
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
                unmappedFields.put(pf.getFieldName(), pf);
            }
        }
        List additionalColumnValues = completeMappingOnSave(o, mappedFields, unmappedFields);
        for (Iterator iter = additionalColumnValues.iterator(); iter.hasNext(); ) {
            Object colVal = iter.next();
            if (colVal instanceof PersistentValue) {
                columnValues.add(colVal);
            }
            else {
                throw new InvalidDataAccessApiUsageException("Invalid type returned for additional columns.  Must be of type PersistentValue.");
            }
        }
        StringBuffer sql = new StringBuffer();
        final List parameterValues = new LinkedList();
        if (!isNewRow) {
            sql.append("update ").append(tableNameToUse).append(" set ");
            for (int i = 0; i < columnValues.size(); i++) {
                if (i > 0)
                    sql.append(", ");
                sql.append(((PersistentValue)columnValues.get(i)).getColumnName());
                sql.append(" = ?");
                parameterValues.add(columnValues.get(i));
            }
            sql.append(" where ").append(((PersistentValue) idValues.get(0)).getColumnName()).append(" = ?");
            parameterValues.add(idValues.get(0));
        }
        else {
            StringBuffer placeholders = new StringBuffer();
            sql.append("insert into ");
            sql.append(tableNameToUse);
            sql.append(" (");
            if (!persistentObject.isUsingGeneratedKeysStrategy()) {
                sql.append(((PersistentValue)idValues.get(0)).getColumnName());
                ((PersistentValue)idValues.get(0)).setValue(assignNewId(o));
                placeholders.append("?");
                parameterValues.add(idValues.get(0));
            }
            for (int i = 0; i < columnValues.size(); i++) {
                if (i > 0 || !persistentObject.isUsingGeneratedKeysStrategy()) {
                    sql.append(", ");
                    placeholders.append(", ");
                }
                sql.append(((PersistentValue)columnValues.get(i)).getColumnName());
                placeholders.append("?");
                parameterValues.add(columnValues.get(i));
            }
            sql.append(") values(");
            sql.append(placeholders);
            sql.append(")");
        }
        if (isNewRow && persistentObject.isUsingGeneratedKeysStrategy()) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            final String prepSql = sql.toString();
            getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(prepSql, Statement.RETURN_GENERATED_KEYS);
                    int cnt = 0;
                    for (Iterator iter = parameterValues.iterator(); iter.hasNext(); ) {
                        PersistentValue pv = (PersistentValue)iter.next();
                        StatementCreatorUtils.setParameterValue(ps, cnt+1, pv.getSqlType(), null, pv.getValue());
                        cnt++;
                    }
                    return ps;
                }
            }, keyHolder);
            assignGeneratedKey(o, keyHolder);
        }
        else {
            final String prepSql = sql.toString();
            getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(prepSql);
                    int cnt = 0;
                    for (Iterator iter = parameterValues.iterator(); iter.hasNext(); ) {
                        PersistentValue pv = (PersistentValue)iter.next();
                        StatementCreatorUtils.setParameterValue(ps, cnt+1, pv.getSqlType(), null, pv.getValue());
                        cnt++;
                    }
                    return ps;
                }
            });
        }
    }

    protected List completeMappingOnSave(Object input, Map mappedFields, Map unmappedFields) {
        return new ArrayList(0);
    }

    public void delete(Object o) {
        String sql = "delete from " + tableNameToUse + " where id = ?";
        PersistentField pf = (PersistentField)persistentObject.getPersistentFields().get("id");
        Object[] idValue = new Object[1];
        try {
            Method m = o.getClass().getMethod(ActiveMapperUtils.getterName(pf.getFieldName()), new Class[] {});
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
            Method m = o.getClass().getMethod(ActiveMapperUtils.setterName(pf.getFieldName()), new Class[] {newId.getClass()});
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

    protected Object assignGeneratedKey(Object o, KeyHolder kh) {
        PersistentField pf = (PersistentField)persistentObject.getPersistentFields().get("id");
        Object newId = kh.getKey();
        try {
            Method m = o.getClass().getMethod(ActiveMapperUtils.setterName(pf.getFieldName()), new Class[] {newId.getClass()});
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

    private class ActiveRowMapper implements RowMapper {
        private Class mappedClass = null;
        private PersistentObject persistentObject = null;
        private Constructor defaultConstruct;

        public ActiveRowMapper(Class clazz, PersistentObject persistentObject) {
            this.mappedClass = clazz;
            this.persistentObject = persistentObject;
            try {
                defaultConstruct = mappedClass.getConstructor(null);
            } catch (NoSuchMethodException e) {
                throw new DataAccessResourceFailureException(new StringBuffer().append("Failed to access default constructor of ").append(mappedClass.getName()).toString(), e);
            }
        }

        public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
            Object result;
            try {
                result = defaultConstruct.newInstance(null);
            } catch (IllegalAccessException e) {
                throw new DataAccessResourceFailureException("Failed to load class " + mappedClass.getName(), e);
            } catch (InvocationTargetException e) {
                throw new DataAccessResourceFailureException("Failed to load class " + mappedClass.getName(), e);
            } catch (InstantiationException e) {
                throw new DataAccessResourceFailureException("Failed to load class " + mappedClass.getName(), e);
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
                            m = result.getClass().getMethod(ActiveMapperUtils.setterName(fieldMeta.getColumnName()), new Class[] {String.class});
                            value = rs.getString(x);
                        }
                        else if (fieldMeta.getJavaType().equals(Byte.class)) {
                            m = result.getClass().getMethod(ActiveMapperUtils.setterName(fieldMeta.getColumnName()), new Class[] {Byte.class});
                            value = new Byte(rs.getByte(x));
                        }
                        else if (fieldMeta.getJavaType().equals(Short.class)) {
                            m = result.getClass().getMethod(ActiveMapperUtils.setterName(fieldMeta.getColumnName()), new Class[] {Short.class});
                            value = new Short(rs.getShort(x));
                        }
                        else if (fieldMeta.getJavaType().equals(Integer.class)) {
                            m = result.getClass().getMethod(ActiveMapperUtils.setterName(fieldMeta.getColumnName()), new Class[] {Integer.class});
                            value = new Integer(rs.getInt(x));
                        }
                        else if (fieldMeta.getJavaType().equals(Long.class)) {
                            m = result.getClass().getMethod(ActiveMapperUtils.setterName(fieldMeta.getColumnName()), new Class[] {Long.class});
                            value = new Long(rs.getLong(x));
                        }
                        else if (fieldMeta.getJavaType().equals(Float.class)) {
                            m = result.getClass().getMethod(ActiveMapperUtils.setterName(fieldMeta.getColumnName()), new Class[] {Float.class});
                            value = new Float(rs.getFloat(x));
                        }
                        else if (fieldMeta.getJavaType().equals(Double.class)) {
                            m = result.getClass().getMethod(ActiveMapperUtils.setterName(fieldMeta.getColumnName()), new Class[] {Double.class});
                            value = new Double(rs.getDouble(x));
                        }
                        else if (fieldMeta.getJavaType().equals(BigDecimal.class)) {
                            m = result.getClass().getMethod(ActiveMapperUtils.setterName(fieldMeta.getColumnName()), new Class[] {BigDecimal.class});
                            value = rs.getBigDecimal(x);
                        }
                        else if (fieldMeta.getJavaType().equals(Boolean.class)) {
                            m = result.getClass().getMethod(ActiveMapperUtils.setterName(fieldMeta.getColumnName()), new Class[] {Boolean.class});
                            value = (rs.getBoolean(x)) ? Boolean.TRUE : Boolean.FALSE;
                        }
                        else if (fieldMeta.getJavaType().equals(Date.class)) {
                            m = result.getClass().getMethod(ActiveMapperUtils.setterName(fieldMeta.getColumnName()), new Class[] {Date.class});
                            if (fieldMeta.getSqlType() == Types.DATE) {
                                value = rs.getDate(x);
                            }
                            else if (fieldMeta.getSqlType() == Types.TIME) {
                                value = rs.getTime(x);
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
