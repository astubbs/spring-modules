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

package org.springmodules.orm.orbroker;

import org.springframework.dao.DataAccessException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Omar Irbouh
 * @since 2005.06.02
 */
public interface BrokerOperations {

  int execute(String statementID) throws DataAccessException;

  int execute(String statementID, String paramName, Object value) throws DataAccessException;

  int execute(String statementID, String[] paramNames, Object[] values) throws DataAccessException;

  int executeBatch(String statementID, String batchParameterName,
                   Collection batchParameters) throws DataAccessException;

  int[] executeBatch(String statementID, String batchParameterName,
                     Object[] batchParameters) throws DataAccessException;

  Object selectOne(String statementID) throws DataAccessException;

  Object selectOne(String statementID, String paramName, Object value) throws DataAccessException;

  Object selectOne(String statementID, String[] paramNames, Object[] values) throws DataAccessException;

  boolean selectOne(String statementID, Object resultObject) throws DataAccessException;

  boolean selectOne(String statementID, String paramName, Object value,
                    Object resultObject) throws DataAccessException;

  boolean selectOne(String statementID, String[] paramNames, Object[] values,
                    Object resultObject) throws DataAccessException;

  Object selectOneFromMany(String statementID, int fromRow) throws DataAccessException;

  Object selectOneFromMany(String statementID, int fromRow,
                           String paramName, Object value) throws DataAccessException;

  Object selectOneFromMany(String statementID, int fromRow,
                           String[] paramNames, Object[] values) throws DataAccessException;

  List selectMany(String statementID) throws DataAccessException;

  List selectMany(String statementID, String paramName, Object value) throws DataAccessException;

  List selectMany(String statementID, String[] paramNames, Object[] values) throws DataAccessException;

  int selectMany(String statementID, Collection resultCollection) throws DataAccessException;

  int selectMany(String statementID, String paramName, Object value,
                 Collection resultCollection) throws DataAccessException;

  int selectMany(String statementID, String[] paramNames, Object[] values,
                 Collection resultCollection) throws DataAccessException;

  List selectMany(String statementID, int startRow, int rowCount) throws DataAccessException;

  List selectMany(String statementID, String paramName, Object value,
                  int startRow, int rowCount) throws DataAccessException;

  List selectMany(String statementID, String[] paramNames, Object[] values,
                  int startRow, int rowCount) throws DataAccessException;

  int selectMany(String statementID, Collection resultCollection,
                 int startRow, int rowCount) throws DataAccessException;

  int selectMany(String statementID, String paramName, Object value,
                 Collection resultCollection, int startRow, int rowCount) throws DataAccessException;

  int selectMany(String statementID, String[] paramNames, Object[] values,
                 Collection resultCollection, int startRow, int rowCount) throws DataAccessException;

  Iterator iterate(String statementID, int fetchSize) throws DataAccessException;

  Iterator iterate(String statementID, int fetchSize,
                   String paramName, Object value) throws DataAccessException;

  Iterator iterate(String statementID, int fetchSize,
                   String[] paramNames, Object[] values) throws DataAccessException;
}