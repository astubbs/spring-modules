package org.springmodules.xt.examples.ajax;

import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxSubmitEvent;

/**
 * Ajax handler raising an exception.
 *
 * @author Sergio Bossa
 */
public class AjaxHandlerWithException extends AbstractAjaxHandler {
    
    public AjaxResponse raiseException(AjaxSubmitEvent event) throws Exception {
        throw new Exception("This is a mapped exception.");
    }
    
    public AjaxResponse returnNoResponse(AjaxSubmitEvent event) throws Exception {
        return null;
    }
}
