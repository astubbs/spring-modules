package org.springmodules.xt.test.ajax;

import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.component.SimpleText;
import org.springmodules.xt.ajax.action.AppendContentAction;
import org.springmodules.xt.ajax.AjaxResponseImpl;

/**
 *
 * @author Sergio Bossa
 */
public class DummyHandler extends AbstractAjaxHandler {
    
    public AjaxResponse action(AjaxActionEvent e) {
        AjaxResponseImpl response = new AjaxResponseImpl();
        AppendContentAction action = new AppendContentAction("action", new SimpleText("action"));
        response.addAction(action);
        return response;
    }
    
    public AjaxResponse actionWithException(AjaxActionEvent e) throws Exception {
        throw new Exception();
    }
}
