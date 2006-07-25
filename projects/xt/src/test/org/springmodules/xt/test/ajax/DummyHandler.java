package org.springmodules.xt.test.ajax;

import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.component.SimpleText;
import org.springmodules.xt.ajax.taconite.TaconiteAppendContentAction;
import org.springmodules.xt.ajax.taconite.TaconiteResponse;

/**
 *
 * @author Sergio Bossa
 */
public class DummyHandler extends AbstractAjaxHandler {
    
    public AjaxResponse test(AjaxActionEvent e) {
        TaconiteResponse response = new TaconiteResponse();
        TaconiteAppendContentAction action = new TaconiteAppendContentAction("test", new SimpleText("test"));
        response.addAction(action);
        return response;
    }
}
