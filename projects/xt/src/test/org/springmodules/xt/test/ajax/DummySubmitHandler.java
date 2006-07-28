package org.springmodules.xt.test.ajax;

import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxSubmitEvent;
import org.springmodules.xt.ajax.component.SimpleText;
import org.springmodules.xt.ajax.taconite.TaconiteAppendContentAction;
import org.springmodules.xt.ajax.taconite.TaconiteResponse;

/**
 *
 * @author Sergio Bossa
 */
public class DummySubmitHandler extends AbstractAjaxHandler {

    public AjaxResponse submit(AjaxSubmitEvent e) {
        TaconiteResponse response = new TaconiteResponse();
        TaconiteAppendContentAction action = new TaconiteAppendContentAction("submit", new SimpleText("submit"));
        response.addAction(action);
        return response;
    }
}
