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

package org.springmodules.datamap.dao;

/**
 * Class defining the operators supported.
 *
 * @author Thomas Risberg
 * @since 0.3
 */
public class Operators {
    public static final int EQUALS = 1;
    public static final int GREATER_THAN = 2;
    public static final int LESS_THAN = 3;
    public static final int GREATER_THAN_OR_EQUAL = 4;
    public static final int LESS_THAN_OR_EQUAL = 5;
    public static final int BETWEEN = 6;
    public static final int IN = 7;
    public static final int STARTS_WITH = 8;
    public static final int ENDS_WITH = 9;
    public static final int CONTAINS = 10;
}
