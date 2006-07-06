package org.springmodules.validation.bean.conf.annotation;

import java.util.Map;
import java.util.HashMap;

import org.easymock.MockControl;

/**
 * @author Uri Boness
 */
public class EasyMock {

    private static Map<Object, MockControl> controlByMock = new HashMap<Object, MockControl>();

    @SuppressWarnings("unchecked")
    public static <T> T createMock(Class<T> clazz) {
        MockControl control = MockControl.createControl(clazz);
        T mock = (T)control.getMock();
        controlByMock.put(mock, control);
        return mock;
    }

    public static void replay(Object mock) {
        controlByMock.get(mock).replay();
    }

    public static void reset(Object mock) {
        controlByMock.get(mock).reset();
    }

    public static void verify(Object mock) {
        controlByMock.get(mock).verify();
    }

    public static void expectAndReturn(Object mock, Object call, Object expected) {
        controlByMock.get(mock).expectAndReturn(call, expected);
    }
}
