package org.springmodules.xt.test.ajax;

import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxSubmitEvent;
import org.springmodules.xt.ajax.component.SimpleText;
import org.springmodules.xt.ajax.action.AppendContentAction;
import org.springmodules.xt.ajax.AjaxResponseImpl;

/**
 *
 * @author Sergio Bossa
 */
public class DummySubmitHandler extends AbstractAjaxHandler {

    public AjaxResponse submit(AjaxSubmitEvent e) {
        AjaxResponseImpl response = new AjaxResponseImpl();
        AppendContentAction action = new AppendContentAction("submit", new SimpleText("submit"));
        response.addAction(action);
        return response;
    }
    
    public AjaxResponse submitWithException(AjaxSubmitEvent e) throws Exception {
        throw new Exception();
    }
}
