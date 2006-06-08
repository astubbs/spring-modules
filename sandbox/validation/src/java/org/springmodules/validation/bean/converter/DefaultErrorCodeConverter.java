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

package org.springmodules.validation.bean.converter;

import org.springframework.util.ClassUtils;

/**
 * Converts simple error codes to an error code that expresses the class and perhaps
 * the property that are associated with the error code.
 *
 * @author Uri Boness
 */
public class DefaultErrorCodeConverter implements ErrorCodeConverter {

    private final static String ERROR_CODE_SEPERATOR_PREFIX = "[";
    private final static String ERROR_CODE_SEPERATOR_SUFFIX = "]";
    private final static String PROPERTY_SEPERATOR = ".";

    /**
     * Converts the given error code to the following format:<br/> <code>short_class_name[errorCode]</code></br>
     * where <code>short_class_name</code> is the name of the given class with its package stripped, and
     * <code>error_code</code> is the given error code.
     *
     * @param errorCode The given error code (the one to convert)
     * @param clazz The given class
     * @return The converted error code.
     */
    public String convertGlobalErrorCode(String errorCode, Class clazz) {
        return new StringBuffer(ClassUtils.getShortName(clazz))
            .append(ERROR_CODE_SEPERATOR_PREFIX)
            .append(errorCode)
            .append(ERROR_CODE_SEPERATOR_SUFFIX)
            .toString();
    }

    /**
     * Converts the given error code to the following format:<br/>
     * <code>short_class_name.property_name[errorCode]</code></br>
     * where <code>short_class_name</code> is the name of the given class with its package stripped,
     * <code>property_name</code> is the given property name, and <code>error_code</code> is the given
     * error code.
     *
     * @param errorCode The given error code (the one to convert)
     * @param clazz The given class
     * @param propertyName The property name
     * @return The converted error code.
     */
    public String convertPropertyErrorCode(String errorCode, Class clazz, String propertyName) {
        return new StringBuffer(ClassUtils.getShortName(clazz))
            .append(PROPERTY_SEPERATOR)
            .append(propertyName)
            .append(ERROR_CODE_SEPERATOR_PREFIX)
            .append(errorCode)
            .append(ERROR_CODE_SEPERATOR_SUFFIX)
            .toString();
    }

}
