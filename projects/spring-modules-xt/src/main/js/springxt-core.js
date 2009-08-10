var XT = {};


XT.version = 20080223;


XT.defaultLoadingElementId = null;


XT.defaultLoadingImage = null;


XT.defaultErrorHandler = null;


XT.doAjaxAction = function(eventId, sourceElement, serverParams, clientParams) {
    if (! clientParams) {
        clientParams = {};
    }
    if ((! clientParams.loadingElementId) || (! clientParams.loadingImage)) {
        clientParams.loadingElementId = this.defaultLoadingElementId; 
        clientParams.loadingImage = this.defaultLoadingImage;
    }
    if (! clientParams.errorHandler) {
        clientParams.errorHandler = this.defaultErrorHandler;
    }
    
    var ajaxClient = null;
    
    if (clientParams.clearQueryString && clientParams.clearQueryString == true && document.URL.indexOf("?") != -1) {
        ajaxClient = new XT.ajax.Client(document.URL.substring(0, document.URL.indexOf("?")));
    } else {
        ajaxClient = new XT.ajax.Client(document.URL);
    }
    
    if (clientParams.formName) {
        return ajaxClient.doAjaxAction(eventId, document.forms[clientParams.formName], sourceElement, serverParams, clientParams);
    } else if (clientParams.formId) {
        return ajaxClient.doAjaxAction(eventId, document.getElementById(clientParams.formId), sourceElement, serverParams, clientParams);
    } else {
        return ajaxClient.doAjaxAction(eventId, document.forms[0], sourceElement, serverParams, clientParams);
    }
};


XT.doAjaxSubmit = function(eventId, sourceElement, serverParams, clientParams) {
    if (! clientParams) {
        clientParams = {};
    }
    if ((! clientParams.loadingElementId) || (! clientParams.loadingImage)) {
        clientParams.loadingElementId = this.defaultLoadingElementId; 
        clientParams.loadingImage = this.defaultLoadingImage;
    }
    if (! clientParams.errorHandler) {
        clientParams.errorHandler = this.defaultErrorHandler;
    }
    
    var ajaxClient = null;
    
    if (clientParams.clearQueryString && clientParams.clearQueryString == true && document.URL.indexOf("?") != -1) {
        ajaxClient = new XT.ajax.Client(document.URL.substring(0, document.URL.indexOf("?")));
    } else {
        ajaxClient = new XT.ajax.Client(document.URL);
    }
    
    if (clientParams.formName) {
        return ajaxClient.doAjaxSubmit(eventId, document.forms[clientParams.formName], sourceElement, serverParams, clientParams);
    } else if (clientParams.formId) {
        return ajaxClient.doAjaxSubmit(eventId, document.getElementById(clientParams.formId), sourceElement, serverParams, clientParams);
    } else {
        return ajaxClient.doAjaxSubmit(eventId, document.forms[0], sourceElement, serverParams, clientParams);
    }
};


XT.ajax = {};


XT.ajax.Client = function(url) {
    
    var requestUrl = url
    
    var ajaxParameter = "ajax-request";
    var eventParameter = "event-id";
    var elementParameter = "source-element";
    var elementIdParameter = "source-element-id";
    var jsonParameters = "json-params";
    
    this.doAjaxAction = function(eventId, sourceForm, sourceElement, serverParams, clientParams) {
        var ajaxRequestType = "ajax-action";
        var queryString = prepareQueryString(ajaxRequestType, eventId, sourceElement, serverParams);
        
        var ajaxRequest = new XT.taconite.AjaxRequest(requestUrl);
        
        configureRequest(ajaxRequest, clientParams);
        
        ajaxRequest.addFormElements(sourceForm);
        ajaxRequest.setQueryString(ajaxRequest.getQueryString() + "&" + queryString);
        
        ajaxRequest.sendRequest();
    };
    
    this.doAjaxSubmit = function(eventId, sourceForm, sourceElement, serverParams, clientParams) {
        var ajaxRequestType = "ajax-submit";
        
        if (clientParams && clientParams.enableUpload && clientParams.enableUpload == true) {
            var queryParameters = prepareQueryParameters(ajaxRequestType, eventId, sourceElement, serverParams);
            
            var iframeRequest = new XT.taconite.IFrameRequest(sourceForm, requestUrl, queryParameters);
            
            configureRequest(iframeRequest, clientParams);
            
            iframeRequest.sendRequest();
        } else {
            var queryString = prepareQueryString(ajaxRequestType, eventId, sourceElement, serverParams);
            
            var ajaxRequest = new XT.taconite.AjaxRequest(requestUrl);
            
            configureRequest(ajaxRequest, clientParams);
            
            ajaxRequest.addFormElements(sourceForm);
            ajaxRequest.setQueryString(ajaxRequest.getQueryString() + "&" + queryString);
            ajaxRequest.setUsePOST();
            
            ajaxRequest.sendRequest();
        }
    };
    
    function prepareQueryString(ajaxRequestType, eventId, sourceElement, serverParams) {
        var qs = "";
        if (ajaxRequestType) {
            qs = ajaxParameter + "=" + ajaxRequestType;
        }
        if (eventId) {
            qs = qs + "&" + eventParameter + "=" + eventId;
        }
        if (sourceElement) {
            if (sourceElement.name != null) {
                qs = qs + "&" + elementParameter + "=" + sourceElement.name;
            }
            if (sourceElement.id != null) {
                qs = qs + "&" + elementIdParameter + "=" + sourceElement.id;
            }
        }
        if (serverParams) {
            qs = qs + "&" + jsonParameters + "=" + encodeURIComponent(JSON.stringify(serverParams));
        }
        return qs;
    };
    
    function prepareQueryParameters(ajaxRequestType, eventId, sourceElement, serverParams) {
        var params = {};
        params[ajaxParameter] = ajaxRequestType;
        params[eventParameter] = eventId;
        if (sourceElement) {
            if (sourceElement.name != null) {
                params[elementParameter] = sourceElement.name;
            }
            if (sourceElement.id != null) {
                params[elementIdParameter] = sourceElement.id;
            }
        }
        if (serverParams) {
            params[jsonParameters] = encodeURIComponent(JSON.stringify(serverParams));
        }
        return params;
    };
    
    function configureRequest(request, clientParams) {
        if (! clientParams) {
            return;
        }
        
        if (clientParams.loadingElementId != null && clientParams.loadingImage != null) {
            request.loadingElementId = clientParams.loadingElementId; 
            request.loadingImage = clientParams.loadingImage;
            request.setPreRequest(showLoadingSign);
            request.setPostRequest(hideLoadingSign);
        }
        
        if (clientParams.errorHandler != null) {
            request.setErrorHandler(clientParams.errorHandler);
        }
    };
    
    function showLoadingSign(request) {
        var targetEl = document.getElementById(request.loadingElementId);
        if (targetEl != null) {
            var img = document.createElement("img");
            img.setAttribute("src", request.loadingImage);
            targetEl.appendChild(img);
        }
    };
    
    function hideLoadingSign(request) {
        var targetEl = document.getElementById(request.loadingElementId);
        if (targetEl != null && targetEl.childNodes.length > 0) {
            targetEl.removeChild(targetEl.childNodes[0]);
        }
    };
};
