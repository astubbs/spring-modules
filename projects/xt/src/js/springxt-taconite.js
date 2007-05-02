/**
 @fileoverview
 This JavaScript file describes the XT object with actions for sending ajax requests using the XT Ajax Framework and Taconite.
 **/

var springxt_taconite_version=20070426;

var XT = {
    
    ajaxParameter : "ajax-request",
    
    eventParameter : "event-id",
    
    elementParameter : "source-element",
    
    elementIdParameter : "source-element-id",
    
    jsonParamsParameter : "json-params",
    
    defaultLoadingElementId : null,
    
    defaultLoadingImage : null,
    
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
            qs = "&" + this.jsonParamsParameter + "=" + encodeURIComponent(jsonObject.toJSONString());
        }
        return qs;
    },
    
    configureLoadingInfo : function(loadingInfo, ajaxRequest) {
        if (loadingInfo != null && loadingInfo.loadingElementId != null && loadingInfo.loadingImage != null) {
            ajaxRequest.loadingElementId = loadingInfo.loadingElementId; 
            ajaxRequest.loadingImage = loadingInfo.loadingImage;
            ajaxRequest.setPreRequest(this.showLoadingSign);
            ajaxRequest.setPostRequest(this.removeLoadingSign);
        } else if (this.defaultLoadingElementId != null && this.defaultLoadingImage != null) {
            ajaxRequest.loadingElementId = this.defaultLoadingElementId; 
            ajaxRequest.loadingImage = this.defaultLoadingImage;
            ajaxRequest.setPreRequest(this.showLoadingSign);
            ajaxRequest.setPostRequest(this.removeLoadingSign);
        }
    },
    
    showLoadingSign : function(ajaxRequest) {
        var targetEl = document.getElementById(ajaxRequest.loadingElementId);
        if (targetEl != null) {
            var img = document.createElement("img");
            img.setAttribute("src", ajaxRequest.loadingImage);
            targetEl.appendChild(img);
        }
    },
    
    removeLoadingSign : function(ajaxRequest) {
        var targetEl = document.getElementById(ajaxRequest.loadingElementId);
        if (targetEl != null && targetEl.childNodes.length > 0) {
            targetEl.removeChild(targetEl.childNodes[0]);
        }
    },
    
    doAjaxAction : function(eventId, sourceElement, jsonObject, loadingInfo) {
        var ajaxRequest = new AjaxRequest(document.URL);
        
        ajaxRequest.addFormElements(document.forms[0]);
        ajaxRequest.setQueryString(ajaxRequest.getQueryString() 
        + "&" + this.ajaxParameter + "=ajax-action" 
        + "&" + this.eventParameter + "=" + eventId 
        + this.createSimpleQueryString(sourceElement) 
        + this.createJSONQueryString(jsonObject));
        
        this.configureLoadingInfo(loadingInfo, ajaxRequest);
        
        ajaxRequest.sendRequest();
    },
    
    doAjaxSubmit : function(eventId, sourceElement, jsonObject, loadingInfo) {
        var ajaxRequest = new AjaxRequest(document.URL);
        
        ajaxRequest.addFormElements(document.forms[0]);
        ajaxRequest.setQueryString(ajaxRequest.getQueryString() 
        + "&" + this.ajaxParameter + "=ajax-submit" 
        + "&" + this.eventParameter + "=" + eventId 
        + this.createSimpleQueryString(sourceElement) 
        + this.createJSONQueryString(jsonObject));
        
        this.configureLoadingInfo(loadingInfo, ajaxRequest);
        
        ajaxRequest.setUsePOST();
        ajaxRequest.sendRequest();
    }
};
