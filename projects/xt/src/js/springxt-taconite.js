/**
 @fileoverview
 This JavaScript file describes actions for sending ajax requests using Taconite.
 */

function doAjaxAction(eventId, sourceElement) {
    var ajaxRequest = new AjaxRequest(document.URL);
    ajaxRequest.addFormElementsByFormEl(document.forms[0]);
    ajaxRequest.setQueryString(ajaxRequest.getQueryString() + "&ajax-request=ajax-action" + "&event-id=" + eventId);
    if (sourceElement != null && sourceElement.name != null) {
        ajaxRequest.setQueryString(ajaxRequest.getQueryString() + "&source-element=" + sourceElement.name);
    }
    ajaxRequest.sendRequest();
}

function doAjaxSubmit(eventId, sourceElement) {
    var ajaxRequest = new AjaxRequest(document.URL);
    ajaxRequest.addFormElementsByFormEl(document.forms[0]);
    ajaxRequest.setQueryString(ajaxRequest.getQueryString() + "&ajax-request=ajax-submit" + "&event-id=" + eventId);
    if (sourceElement != null && sourceElement.name != null) {
        ajaxRequest.setQueryString(ajaxRequest.getQueryString() + "&source-element=" + sourceElement.name);
    }
    ajaxRequest.setUsePOST();
    ajaxRequest.sendRequest();
}