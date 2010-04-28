package org.springmodules.validation.bean.conf.namespace;

import org.springmodules.validation.bean.converter.KeepAsIsErrorCodeConverter;

/**
 * @author Uri Boness
 */
public class TestErrorCodeConverter extends KeepAsIsErrorCodeConverter {

    public String convertGlobalErrorCode(String errorCode, Class clazz) {
        return super.convertGlobalErrorCode("test." + errorCode, clazz);
    }

    public String convertPropertyErrorCode(String errorCode, Class clazz, String propertyName) {
        return super.convertPropertyErrorCode("test." + errorCode, clazz, propertyName);
    }
}
