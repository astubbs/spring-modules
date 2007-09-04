/**
 @fileoverview
 XT object declaration, with methods for sending Ajax action and submit events.
 **/

var springxt_core_version=20070827;

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
        if (sourceElement) {
            if (sourceElement.name != null) {
                qs = "&" + this.elementParameter + "=" + sourceElement.name;
            }
            if (sourceElement.id != null) {
                qs = qs + "&" + this.elementIdParameter + "=" + sourceElement.id;
            }
        }
        return qs;
    },
    
    createJSONQueryString : function(serverParams) {
        var qs = "";
        if (serverParams) {
            qs = "&" + this.jsonParamsParameter + "=" + encodeURIComponent(serverParams.toJSONString());
        }
        return qs;
    },
    
    createIFrameRequestParamsObject : function(ajaxActionType, eventId, sourceElement, serverParams) {
        var params = {};
        params[this.ajaxParameter] = ajaxActionType;
        params[this.eventParameter] = eventId;
        if (sourceElement) {
            if (sourceElement.name != null) {
                params[this.elementParameter] = sourceElement.name;
            }
            if (sourceElement.id != null) {
                params[this.elementIdParameter] = sourceElement.id;
            }
        }
        if (serverParams) {
            params[this.jsonParamsParameter] = encodeURIComponent(serverParams.toJSONString());
        }
        return params;
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
    
    doAjaxAction : function(eventId, sourceElement, serverParams, requestParams) {
        var ajaxActionType = "ajax-action";
        
        var ajaxRequest = new AjaxRequest(document.URL);
        
        ajaxRequest.addFormElements(document.forms[0]);
        ajaxRequest.setQueryString(ajaxRequest.getQueryString() 
        + "&" + this.ajaxParameter + "=" + ajaxActionType
        + "&" + this.eventParameter + "=" + eventId 
        + this.createSimpleQueryString(sourceElement) 
        + this.createJSONQueryString(serverParams));
        
        this.configureLoadingInfo(requestParams, ajaxRequest);
        
        ajaxRequest.sendRequest();
    },
    
    doAjaxSubmit : function(eventId, sourceElement, serverParams, requestParams) {
        var ajaxActionType = "ajax-submit";
        
        if (requestParams && requestParams.enableUpload && requestParams.enableUpload == true) {
            var url = document.URL;
            var parameters = this.createIFrameRequestParamsObject(ajaxActionType, eventId, sourceElement, serverParams);
            
            var iframeRequest = new IFrameRequest(document.forms[0], url, parameters);
            
            this.configureLoadingInfo(requestParams, iframeRequest);
            
            iframeRequest.sendRequest();
        } else {
            var ajaxRequest = new AjaxRequest(document.URL);
            
            ajaxRequest.addFormElements(document.forms[0]);
            ajaxRequest.setQueryString(ajaxRequest.getQueryString() 
            + "&" + this.ajaxParameter + "=" + ajaxActionType
            + "&" + this.eventParameter + "=" + eventId 
            + this.createSimpleQueryString(sourceElement) 
            + this.createJSONQueryString(serverParams));
            
            this.configureLoadingInfo(requestParams, ajaxRequest);
            
            ajaxRequest.setUsePOST();
            ajaxRequest.sendRequest();
        }
    }
};
