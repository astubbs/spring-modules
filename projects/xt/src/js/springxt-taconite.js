/**
 @fileoverview
 This JavaScript file describes actions for sending ajax requests using Taconite.
 */

function doAjaxAction(eventId, sourceElement, jsonObject) {
    var ajaxRequest = new AjaxRequest(document.URL);
    ajaxRequest.addFormElementsByFormEl(document.forms[0]);
    ajaxRequest.setQueryString(ajaxRequest.getQueryString() + "&ajax-request=ajax-action" + "&event-id=" + eventId + createSimpleQueryString(sourceElement) + createJSONQueryString(jsonObject));
    ajaxRequest.sendRequest();
}

function doAjaxSubmit(eventId, sourceElement, jsonObject) {
    var ajaxRequest = new AjaxRequest(document.URL);
    ajaxRequest.addFormElementsByFormEl(document.forms[0]);
    ajaxRequest.setQueryString(ajaxRequest.getQueryString() + "&ajax-request=ajax-submit" + "&event-id=" + eventId + createSimpleQueryString(sourceElement) + createJSONQueryString(jsonObject));
    ajaxRequest.setUsePOST();
    ajaxRequest.sendRequest();
}

function createSimpleQueryString(sourceElement) {
    var qs = "";
    if (sourceElement != undefined && sourceElement != null) {
        if (sourceElement.name != null && sourceElement.name != "") {
            qs = qs + "&source-element=" + sourceElement.name;
        }
        if (sourceElement.id != null && sourceElement.id != "") {
            qs = qs + "&source-element-id=" + sourceElement.id;
        }
    }
    return qs;
}

function createJSONQueryString(jsonObject) {
    var qs = "";
    if (jsonObject != undefined && jsonObject != null) {
        qs = "&json-params=" + escape(jsonObject.toJSONString());
    }
    return qs;
}
