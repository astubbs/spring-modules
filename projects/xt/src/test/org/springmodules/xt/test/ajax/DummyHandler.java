package org.springmodules.xt.test.ajax;

import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;

/**
 *
 * @author Sergio Bossa
 */
public class DummyHandler extends AbstractAjaxHandler {
    
    public AjaxResponse test(AjaxActionEvent e) {
        return null;
    }
}
