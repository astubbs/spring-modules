/**
 @fileoverview
 This JavaScript file describes the XT object with actions for sending ajax requests using the XT Ajax Framework and Taconite.
 **/

var springxt-taconite_version=20070226;

var XT = {
    
    ajaxParameter : "ajax-request",
    
    eventParameter : "event-id",
    
    elementParameter : "source-element",
    
    elementIdParameter : "source-element-id",
    
    jsonParamsParameter : "json-params",
    
    createSimpleQueryString : function(sourceElement) {
        var qs = "";
        if (sourceElement != undefined && sourceElement != null) {
            if (sourceElement.name != null) {
                qs = "&" + this.elementParameter + "=" + sourceElement.name;
            }
            if (sourceElement.id != null) {
                qs = qs + "&" + this.elementIdParameter + "=" + sourceElement.id;
            }
        }
        return qs;
    },
    
    createJSONQueryString : function(jsonObject) {
        var qs = "";
        if (jsonObject != undefined && jsonObject != null) {
            qs = "&" + this.jsonParamsParameter + "=" + escape(jsonObject.toJSONString());
        }
        return qs;
    },
    
    doAjaxAction : function(eventId, sourceElement, jsonObject) {
        var ajaxRequest = new AjaxRequest(document.URL);
        ajaxRequest.addFormElementsByFormEl(document.forms[0]);
        ajaxRequest.setQueryString(ajaxRequest.getQueryString() 
        + "&" + this.ajaxParameter + "=ajax-action" 
        + "&" + this.eventParameter + "=" + eventId 
        + this.createSimpleQueryString(sourceElement) 
        + this.createJSONQueryString(jsonObject));
        ajaxRequest.sendRequest();
    },
    
    doAjaxSubmit : function(eventId, sourceElement, jsonObject) {
        var ajaxRequest = new AjaxRequest(document.URL);
        ajaxRequest.addFormElementsByFormEl(document.forms[0]);
        ajaxRequest.setQueryString(ajaxRequest.getQueryString() 
        + "&" + this.ajaxParameter + "=ajax-submit" 
        + "&" + this.eventParameter + "=" + eventId 
        + this.createSimpleQueryString(sourceElement) 
        + this.createJSONQueryString(jsonObject));
        ajaxRequest.setUsePOST();
        ajaxRequest.sendRequest();
    }
};
