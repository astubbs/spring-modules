XT.taconite = {};


XT.taconite.isIE = document.uniqueID;


XT.taconite.BaseRequest = function() {
    
    var preRequest = null;
    
    var postRequest = null;
    
    var errorHandler = null;
    
    this.setPreRequest = function(func) {
        preRequest = func;
    };
    
    this.setPostRequest = function(func) {
        postRequest = func;
    };
    
    this.getPreRequest = function() {
        return preRequest;
    };
    
    this.getPostRequest = function() {
        return postRequest;
    };
    
    this.setErrorHandler = function(func){
        errorHandler = func;
    };
    
    this.getErrorHandler = function() {
        return errorHandler;
    };
};


XT.taconite.AjaxRequest = function(url) {
    
    var ajaxRequest = this;
    
    var xmlHttp = createXMLHttpRequest();
    
    var queryString = "";
    
    var requestURL = url;
    
    var method = "GET";
    
    var async = true;
    
    this.getXMLHttpRequestObject = function() {
        return xmlHttp;
    };
    
    this.setUsePOST = function() {
        method = "POST";
    };
    
    this.setUseGET = function() {
        method = "GET";
    };
    
    this.setQueryString = function(qs) {
        queryString = qs;
    };
    
    this.getQueryString = function() {
        return queryString;
    };
    
    this.setAsync = function(asyncBoolean){
        async = asyncBoolean;
    };
    
    this.addFormElements = function(form) {
        var formElements = new Array();
        if (form != null) {
            if (typeof form == "string") {
                var el = document.getElementById(form);
                if (el != null) {
                    formElements = el.elements;
                }
            } else {
                formElements = form.elements;
            }
        }
        var values = toQueryString(formElements);
        accumulateQueryString(values);
    };
    
    this.addNameValuePair = function(name, value) {
        var nameValuePair = name + "=" + encodeURIComponent(value);
        accumulateQueryString(nameValuePair);
    };
    
    this.addNamedFormElementsByFormID = function() {
        var elementName = "";
        var namedElements = null;
        
        for(var i = 1; i < arguments.length; i++) {
            elementName = arguments[i];
            namedElements = document.getElementsByName(elementName);
            var arNamedElements = new Array();
            for(j = 0; j < namedElements.length; j++) {
                if(namedElements[j].form  && namedElements[j].form.getAttribute("id") == arguments[0]){
                    arNamedElements.push(namedElements[j]);				
                }
            }
            if(arNamedElements.length > 0){
                elementValues = toQueryString(arNamedElements);
                accumulateQueryString(elementValues);
            }
        }
    };
    
    this.addNamedFormElements = function() {
        var elementName = "";
        var namedElements = null;
        
        for(var i = 0; i < arguments.length; i++) {
            elementName = arguments[i];
            namedElements = document.getElementsByName(elementName);
            
            elementValues = toQueryString(namedElements);
            
            accumulateQueryString(elementValues);
        }
    };
    
    this.addFormElementsById = function() {
        var id = "";
        var element = null;
        var elements = new Array();
        
        for(var h = 0; h < arguments.length; h++) {
            element = document.getElementById(arguments[h]);
            if(element != null) {
                elements[h] = element;
            }
        }
        
        elementValues = toQueryString(elements);
        accumulateQueryString(elementValues);
    };
    
    this.sendRequest = function() {
        if(this.getPreRequest()) {
            var preRequest = this.getPreRequest();
            preRequest(this);
        }
        
        if(async) {
            xmlHttp.onreadystatechange = handleStateChange;
        }
        
        if(requestURL.indexOf("?") > 0) {
            requestURL = requestURL + "&ts=" + new Date().getTime();
        }
        else {
            requestURL = requestURL + "?ts=" + new Date().getTime();
        }
        
        try {
            if(method == "GET") {
                if(queryString.length > 0) {
                    requestURL = requestURL + "&" + queryString;
                }
                xmlHttp.open(method, requestURL, async);
                xmlHttp.send(null);
            }
            else {
                xmlHttp.open(method, requestURL, async);
                //Fix a bug in Firefox when posting
                try {
                    if (xmlHttp.overrideMimeType) {
                        xmlHttp.setRequestHeader("Connection", "close");//set header after open
                    }			
                }
                catch(e) {
                    // Do nothing
                }
                xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"); 
                xmlHttp.send(queryString);
            }
        }
        catch(exception) {
            if(this.getErrorHandler()) {
                var errorHandler = this.getErrorHandler();
                errorHandler(this, exception);
            }
            else {
                throw exception;
            }
        }
        
        if(!async) {  //synchronous request, handle the state change
            handleStateChange();
        }
    };
    
    function handleStateChange() {
        if(ajaxRequest.getXMLHttpRequestObject().readyState != 4) {
            return;
        }
        if(ajaxRequest.getXMLHttpRequestObject().status != 200) {
            var errorHandler = ajaxRequest.getErrorHandler();
            if (errorHandler) {
                errorHandler(ajaxRequest);
            }
            return;
        }
        try {
            //handle null responseXML
            var nodes = null;
            if (ajaxRequest.getXMLHttpRequestObject().responseXML != null) {
                nodes = ajaxRequest.getXMLHttpRequestObject().responseXML.documentElement.childNodes;
            }
            else {
                nodes = new Array();
            }
            
            var parser = new XT.taconite.XhtmlToDOMParser();
            for(var i = 0; i < nodes.length; i++) {
                if(nodes[i].nodeType != 1) {
                    continue;
                }
                parser.parseXhtml(nodes[i]);
            }
        }
        catch(exception) {
            if(ajaxRequest.getErrorHandler()) {
                var errorHandler = ajaxRequest.getErrorHandler();
                errorHandler(ajaxRequest, exception);
            }
            else {
                throw exception;
            }
        }
        finally {
            try {
                if(ajaxRequest.getPostRequest()) {
                    var postRequest = ajaxRequest.getPostRequest();
                    postRequest(ajaxRequest);
                }
            }
            catch(exception) {
                if(ajaxRequest.getErrorHandler()) {
                    var errorHandler = ajaxRequest.getErrorHandler();
                    errorHandler(ajaxRequest, exception);
                }
            }
        }
    };
    
    function createXMLHttpRequest() {
        var req = false;
        if (window.XMLHttpRequest) {
            req = new XMLHttpRequest();
        }
        else if (window.ActiveXObject) {
            try {
                req = new ActiveXObject("Msxml2.XMLHTTP");
            }
            catch(e) {
                try {
                    req = new ActiveXObject("Microsoft.XMLHTTP");
                }
                catch(e) {
                    req = false;
                }
            }
        }
        return req;
    };
    
    function accumulateQueryString(newValues) {
        if(queryString == "") {
            queryString = newValues; 
        }
        else {
            queryString = queryString + "&" +  newValues;
        }
    };
    
    function toQueryString(elements) {
        var node = null;
        var qs = "";
        var name = "";
        
        var tempString = "";
        for(var i = 0; i < elements.length; i++) {
            tempString = "";
            node = elements[i];
            
            name = node.getAttribute("name");
            //use id if name is null
            if (!name) {
                name = node.getAttribute("id");
            }
            name = encodeURIComponent(name);
            
            if(node.tagName.toLowerCase() == "input") {
                if(node.type.toLowerCase() == "radio" || node.type.toLowerCase() == "checkbox") {
                    if(node.checked) {
                        tempString = name + "=" + encodeURIComponent(node.value);
                    }
                }
                
                if(node.type.toLowerCase() == "text" || node.type.toLowerCase() == "hidden" || node.type.toLowerCase() == "password") {
                    tempString = name + "=" + encodeURIComponent(node.value);
                }
            }
            else if(node.tagName.toLowerCase() == "select") {
                tempString = getSelectedOptions(node);
            }
            
            else if(node.tagName.toLowerCase() == "textarea") {
                tempString = name + "=" + encodeURIComponent(node.value);
            }
            
            if(tempString != "") {
                if(qs == "") {
                    qs = tempString;
                }
                else {
                    qs = qs + "&" + tempString;
                }
            }
            
        }
        
        return qs;
    };
    
    function getSelectedOptions(select) {
        var options = select.options;
        var option = null;
        var qs = "";
        var tempString = "";
        
        for(var x = 0; x < options.length; x++) {
            tempString = "";
            option = options[x];
            
            if(option.selected) {
                tempString = encodeURIComponent(select.name) + "=" + encodeURIComponent(option.value);
            }
            
            if(tempString != "") {
                if(qs == "") {
                    qs = tempString;
                }
                else {
                    qs = qs + "&" + tempString;
                }
            }
        }
        
        return qs;
    };
};


XT.taconite.AjaxRequest.prototype = new XT.taconite.BaseRequest();


XT.taconite.IFrameRequest = function(form, url, parameters) {
    
    var iFrameRequest = this;
    
    var requestForm = form;
    
    var requestURL = url;
    
    var requestParams = parameters;
    
    var containerId = "CONTAINER-" + Math.floor(Math.random() * 99999);
    
    var frameId = "FRAME-" + Math.floor(Math.random() * 99999);
    
    var container = null;
    
    var frame = null;
    
    init();
    
    this.sendRequest = function() {
        if (this.getPreRequest()) {
            var preRequest = this.getPreRequest();
            preRequest(this);
        }
        
        try {
            requestForm.setAttribute("target", frame.getAttribute("id"));
            requestForm.submit();
            var interval = window.setInterval(
            function() {
                if (frames[frameId].document 
                && frames[frameId].document.documentElement 
                && frames[frameId].document.getElementsByTagName("ajax-response").length == 1) {
                    /* We use frames[frameId].document.getElementsByTagName("ajax-response")
                     * instead of a more compact form because IE puts the ajax-response tag
                     * inside a body tag.
                     */
                    onComplete();
                    window.clearInterval(interval);
                }
            },
            250
            );
        } catch(ex) {
            if (this.getErrorHandler()) {
                var errorHandler = this.getErrorHandler();
                errorHandler(this, exception);
            }
            else {
                throw ex;
            }
        }
        
        return true;
    };
    
    function init() { 
        container = document.createElement("div");
        container.setAttribute("id", containerId);
        for (var name in requestParams) {
            var input = document.createElement("input");
            input.setAttribute("type", "hidden");
            input.setAttribute("name", name);
            input.setAttribute("value", requestParams[name]);
            container.appendChild(input);
        }
        
        frame = document.createElement("iframe");
        frame.setAttribute("id", frameId);
        frame.setAttribute("name", frameId);
        frame.setAttribute("src", "");
        frame.setAttribute("style", "width:0;height:0;visibility:hidden;");
        frame.style.cssText = "width:0;height:0;visibility:hidden;";
        
        container.appendChild(frame);
        
        requestForm.appendChild(container);
        
        // IE hack: we need to set id and name if undefined. 
        if (! frames[frameId].id) {
            frames[frameId].id = frameId;
        }
        if (! frames[frameId].name) {
            frames[frameId].name = frameId;
        }
    };
    
    function onComplete() {
        try {
            var nodes = frames[frameId].document.getElementsByTagName("ajax-response")[0].childNodes;
            var parser = new XT.taconite.XhtmlToDOMParser();
            for (var i = 0; i < nodes.length; i++) {
                if (nodes[i].nodeType != 1) {
                    continue;
                }
                parser.parseXhtml(nodes[i]);
            }
        } catch(ex) {
            if (iFrameRequest.getErrorHandler()) {
                var errorHandler = iFrameRequest.getErrorHandler();
                errorHandler(iFrameRequest, exception);
            }
            else {
                throw ex;
            }
        } finally {
            try {
                // Remove the whole container, with the iframe and all hidden input fields:
                requestForm.removeChild(container);
                //
                if (iFrameRequest.getPostRequest()) {
                    var postRequest = iFrameRequest.getPostRequest();
                    postRequest(iFrameRequest);
                }
            }
            catch(exception) {
                if (iFrameRequest.getErrorHandler()) {
                    var errorHandler = iFrameRequest.getErrorHandler();
                    errorHandler(iFrameRequest, exception);
                }
            }
        }
    };
};


XT.taconite.IFrameRequest.prototype = new XT.taconite.BaseRequest();


XT.taconite.XhtmlToDOMParser = function() {
    
    this.parseXhtml = function(xml) {
        var xmlTagName=xml.tagName.toLowerCase();
        switch (xmlTagName) {
            case "append-as-children":
                executeAction(xml, appendAsChildrenAction);
                break;
            case "delete":
                executeAction(xml, deleteAction);
                break;
            case "append-as-first-child":
                executeAction(xml, appendAsFirstChildAction);
                break;                         
            case "insert-after":
                executeAction(xml, insertAfterAction);
                break;
            case "insert-before":
                executeAction(xml, insertBeforeAction);
                break;                         
            case "replace-children":
                executeAction(xml, replaceChildrenAction);
                break;
            case "replace":
                executeAction(xml, replaceAction);
                break;                         
            case "set-attributes":
                executeAction(xml, setAttributesAction);
                break;
            case "redirect":
                executeAction(xml, executeRedirectAction);
                break;
            case "execute-javascript":
                executeAction(xml, executeJavascriptAction);
                break;
            default :
                throw {'message' : 'Wrong tag name!'};
        }
    };
    
    function executeAction(xml, action) {
        var context = xml.getElementsByTagName("context")[0];
        var content = xml.getElementsByTagName("content")[0];
        if (context) {
            var contextNodes = getContextNodes(context);
            for (var i = 0; i < contextNodes.length; i++) {
                var contextNode = contextNodes[i];
                action(contextNode, content);
            }
        } else {
            action(content);
        }
    };
    
    function getContextNodes(context) {
        var matchMode = context.getElementsByTagName("matcher")[0].getAttribute("matchMode");
        var contextNodes = new Array();
        if (matchMode != null) {
            switch(matchMode) {
                case "plain" : 
                    contextNodes = getContextNodesByPlainMatch(context);
                    break;
                case "wildcard" : 
                    contextNodes = getContextNodesByWildcardMatch(context);
                    break;
                case "selector" : 
                    contextNodes = getContextNodesBySelectorMatch(context);
                    break;
            }
        } else {
            contextNodes = getContextNodesByPlainMatch(context);
        }
        return contextNodes;
    };
    
    function getContextNodesByPlainMatch(context) {
        var contextNodeID = context.getElementsByTagName("matcher")[0].getAttribute("contextNodeID");
        var contextIDs = contextNodeID.split(',');
        var contextNodes = new Array();
        for (var i = 0, k = 0; i < contextIDs.length; i++) {
            var tmp = document.getElementById(contextIDs[i].trim());
            if (tmp != null) {
                contextNodes[k] = tmp;
                k++;
            }
        }
        return contextNodes;
    };
    
    function getContextNodesByWildcardMatch(context) {
        var contextNodeID = context.getElementsByTagName("matcher")[0].getAttribute("contextNodeID");
        var contextIDs = contextNodeID.split(',');
        var contextNodes = new Array();
        for (var i = 0; i < contextIDs.length; i++) {
            contextNodes = contextNodes.concat(document.getElementsByMatchingId(contextIDs[i].trim()));
        }
        return contextNodes;
    };
    
    function getContextNodesBySelectorMatch(context) {
        var selector = new XT.util.DOMSelector();
        var contextNodeSelector = context.getElementsByTagName("matcher")[0].getAttribute("contextNodeSelector");
        var contextSelectors = contextNodeSelector.split(',');
        var contextNodes = new Array();
        for (var i = 0; i < contextSelectors.length; i++) {
            contextNodes = contextNodes.concat(selector.select(contextSelectors[i].trim()));
        }
        return contextNodes;
    };
    
    function setAttributesAction(domNode,xml){
        var sourceNode = xml.getElementsByTagName("attributes")[0];
        handleAttributes(domNode, sourceNode);
    };
    
    function appendAsFirstChildAction(domNode,xml){
        var firstNode=null;
        if(domNode.childNodes.length > 0) {
            firstNode=domNode.childNodes[0];
        }
        
        for(var i=0;i<xml.childNodes.length;i++){
            domChildNode=handleNode(xml.childNodes[i]);
            if(domChildNode!=null){
                if(firstNode==null){
                    domNode.appendChild(domChildNode);
                    firstNode=domChildNode;
                }
                else {
                    domNode.insertBefore(domChildNode,firstNode);
                }
            }
        }              
    };
    
    function insertAfterAction(domNode,xml){
        var domChildNode=null;
        var nextSibling=domNode.nextSibling;
        for(var i=0;i<xml.childNodes.length;i++){
            domChildNode=handleNode(xml.childNodes[i]);
            if(domChildNode!=null){
                if(nextSibling!=null) {
                    domNode.parentNode.insertBefore(domChildNode,nextSibling);
                }
                else {
                    domNode.parentNode.appendChild(domChildNode);
                }
            }
        }              
    };
    
    function insertBeforeAction(domNode,xml){
        var domChildNode=null;
        for(var i=0;i<xml.childNodes.length;i++){
            domChildNode=handleNode(xml.childNodes[i]);
            if(domChildNode!=null) {
                domNode.parentNode.insertBefore(domChildNode,domNode);
            }
        }              
    };  
    
    function replaceAction(domNode,xml){
        insertAfterAction(domNode,xml);
        domNode.parentNode.removeChild(domNode);
    };
    
    function deleteAction(domNode) {
        domNode.parentNode.removeChild(domNode);
    };
    
    function appendAsChildrenAction(domNode,xml) {
        internalAppendOrReplaceChildren(domNode, xml, false);
    };
    
    function replaceChildrenAction(domNode,xml) {
        internalAppendOrReplaceChildren(domNode, xml, true);
    };
    
    function executeRedirectAction(xmlNode) {
        var targetUrl = xmlNode.getElementsByTagName("target")[0].getAttribute("url");
        window.location.href = targetUrl;
    };
    
    function executeJavascriptAction(xmlNode) {
        var scripts = xmlNode.getElementsByTagName("script");
        for (var i = 0; i < scripts.length; i++) {
            var script = scripts[i];
            if (script.getAttribute("type") == "text/javascript") {
                var js = "";
                for (var i = 0; i < script.childNodes.length; i++) {
                    js = js + script.childNodes[i].nodeValue;
                }
                eval(js);
            }
        }
    };
    
    function internalAppendOrReplaceChildren(domNode,xml,doRemoveChildren) {
        var domChildNode=null;
        if(doRemoveChildren){
            while(domNode.childNodes.length >0){
                domNode.removeChild(domNode.childNodes[0]);
            }      
        }
        for(var i=0;i<xml.childNodes.length;i++){
            domChildNode=handleNode(xml.childNodes[i]);
            if(domChildNode!=null) {
                // Here we have to check xml.childNodes[i].nodeType==1 because of an IE7 bug 
                if (XT.taconite.isIE && xml.childNodes[i].nodeType==1) {
                    checkForIEMultipleSelectHack(domNode, domChildNode);
                }
                domNode.appendChild(domChildNode);
            }
        }              
    };
    
    function isInlineMode(node) {
        var lowerCaseTag = node.tagName.toLowerCase();
        
        if(lowerCaseTag == "button") {
            return true;
        }
        
        var attrType = node.getAttribute("type");
        
        if(lowerCaseTag == "input" && (attrType == "radio" || attrType == "checkbox")) {
            return true;
        }
        return false;
    };  
    
    function handleAttributes(domNode, xmlNode, returnAsText) {
        var attr = null;
        var attrString = "";
        var name = "";
        var value = "";
        for(var x = 0; x < xmlNode.attributes.length; x++) {
            attr = xmlNode.attributes[x];
            name = cleanAttributeName(attr.name.trim());
            value = attr.value.trim();
            if(!returnAsText){
                if(name == "style") {
                    /* IE workaround */
                    domNode.style.cssText = value;
                    /* Standards compliant */
                    domNode.setAttribute(name, value);
                }
                else if(name.trim().toLowerCase().substring(0, 2) == "on") {
                    /* IE workaround for event handlers */
                    if(XT.taconite.isIE) { 
                        eval("domNode." + name.trim().toLowerCase() + "=function(){" + value + "}"); 
                    }
                    else { 
                        domNode.setAttribute(name,value); 
                    }                    
                }
                else if(name == "value") {
                    /* IE workaround for the value attribute -- makes form elements selectable/editable */
                    domNode.value = value;
                }
                else if(useIEFormElementCreationStrategy(xmlNode.tagName) && name == "name") {
                    //Do nothing, as the "name" attribute was handled in the createFormElementsForIEStrategy function
                    continue;
                }
                else {
                    /* Standards compliant */
                    domNode.setAttribute(name,value);
                }
                /* class attribute workaround for IE */
                if(name == "class") {
                    domNode.setAttribute("className",value);
                }
                
                /* This is a workaround for a bug in IE where select elemnts with multiple don't have 
                    all the appropriate options selected.  Only one is selected.  Appears fixed in IE7.
                 */
                if(XT.taconite.isIE) {
                    if(name == "multiple" && domNode.id != "") {
                        setTimeout(
                        function() {
                            var selectNode = document.getElementById(domNode.id);
                            var options = selectNode.getElementsByTagName("option");
                            var option;
                            for(var i = 0; i < options.length; i++) {
                                option = options[i];
                                if (typeof(option.taconiteOptionSelected) != "undefined") {
                                    option.selected = true;
                                } 
                                else {
                                    option.selected = false;
                                }
                            }
                        },
                        100);
                    }
                    if(name == "selected") {
                        domNode.taconiteOptionSelected = true;
                    }
                }
            } else{
                attrString = attrString + name + "=\"" + value + "\" " ;
            }
        }
        return attrString;
    };
    
    function handleNode(xmlNode){
        var nodeType = xmlNode.nodeType;               
        switch(nodeType) {
            case 1:  //ELEMENT_NODE
                return handleElement(xmlNode);
            case 3:  //TEXT_NODE
            case 4:  //CDATA_SECTION_NODE
                var textNode = document.createTextNode(xmlNode.nodeValue);
                if(XT.taconite.isIE) {
                    textNode.nodeValue = textNode.nodeValue.replace(/\n/g, '\r');
                }
                return textNode;
        }      
        return null;
    };
    
    function handleElement(xmlNode){
        var domElemNode = null;
        var xmlNodeTagName = xmlNode.tagName.toLowerCase();
        if(XT.taconite.isIE){
            if(isInlineMode(xmlNode)) {
                return document.createElement("<" + xmlNodeTagName + " " + handleAttributes(domElemNode, xmlNode, true) + ">");
            }
            if(xmlNodeTagName == "style"){
                //In internet explorer, we have to use styleSheets array.		
                var text,rulesArray,styleSheetPtr;
                var regExp = /\s+/g;
                text=xmlNode.text.replace(regExp, " ");
                rulesArray=text.split("}");
                
                domElemNode=document.createElement("style");
                styleSheetPtr=document.styleSheets[document.styleSheets.length-1];
                for(var i=0;i<rulesArray.length;i++){
                    rulesArray[i]=rulesArray[i].trim();
                    var rulePart=rulesArray[i].split("{");
                    if(rulePart.length==2) {//Add only if the rule is valid
                        styleSheetPtr.addRule(rulePart[0],rulePart[1],-1);//Append at the end of stylesheet.
                    }
                }							
                return domElemNode;			
            }
            
        }
        if(domElemNode == null){
            if(useIEFormElementCreationStrategy(xmlNodeTagName)) {
                domElemNode = createFormElementsForIEStrategy(xmlNode);
            }
            else {
                domElemNode = document.createElement(xmlNodeTagName);
            }
            
            handleAttributes(domElemNode,xmlNode);
            //Fix for IE Script tag: Unexpected call to method or property access error
            //IE don't allow script tag to have child
            if(XT.taconite.isIE && !domElemNode.canHaveChildren){
                if(xmlNode.childNodes.length > 0){
                    domElemNode.text=xmlNode.text;
                }
                
            }                              
            else{
                for(var z = 0; z < xmlNode.childNodes.length; z++) {
                    var domChildNode=handleNode(xmlNode.childNodes[z]);
                    if(domChildNode!=null) {
                        domElemNode.appendChild(domChildNode);
                    }
                }
            }
        }      
        
        return domElemNode;
    };
    
    function useIEFormElementCreationStrategy(xmlNodeTagName) {
        var useIEStrategy = false;
        
        var nodeName = xmlNodeTagName.toLowerCase();
        if (XT.taconite.isIE && (nodeName == "form" ||
        nodeName == "input" ||
        nodeName == "textarea" ||
        nodeName == "select" ||
        nodeName == "a" ||
        nodeName == "applet" ||
        nodeName == "button" ||
        nodeName == "img" ||
        nodeName == "link" ||
        nodeName == "map" ||
        nodeName == "object")) {
            useIEStrategy = true;
        }
        
        return useIEStrategy;
    };
    
    function createFormElementsForIEStrategy(xmlNode) {
        var attr = null;
        var name = "";
        var value = "";
        for (var x = 0; x < xmlNode.attributes.length; x++) {
            attr = xmlNode.attributes[x];
            name = attr.name.trim();
            if (name == "name") {
                value = attr.value.trim();
            }
        }
        
        domElemNode = document.createElement("<" + xmlNode.tagName + " name='" + value + "' />"); // e.g. document.createElement("<input name='slot2'>");
        
        return domElemNode;
    };
    
    function checkForIEMultipleSelectHack(domNode, domChildNode) {
        if(XT.taconite.isIE && domChildNode.nodeType == 1 && domChildNode.tagName.toLowerCase() == "select" && domChildNode.getAttribute("multiple") != null) {
            createIEMultipleSelectHack(domNode);
        }
    };
    
    function createIEMultipleSelectHack(contextNode) {
        //this is a total and complete hack for IE 6's totally broken select
        //box implementation.  A "multiple" select box only appears as a drop-
        //down box... but for some reason, creating another select around it
        //makes it render correctly (??).  So, create a bogus select box and hide
        //it.
        var selectBox = document.createElement("select");
        selectBox.size = 3;
        
        for (x=0; x < 1; x++) {
            selectBox.options[x] = new Option(x, x);
        }
        
        //hide it
        selectBox.style.display = "none";
        
        contextNode.appendChild(selectBox);
    };
    
    function cleanAttributeName(name) {
        if(XT.taconite.isIE == false) {
            return;
        }
        
        // IE workaround to change cellspacing to cellSpacing, etc
        var cleanName = name.toLowerCase();
        if(cleanName == "cellspacing") {
            cleanName = "cellSpacing";
        }
        else if(cleanName == "cellpadding") {
            cleanName = "cellPadding";
        }
        else if(cleanName == "colspan") {
            cleanName = "colSpan";
        }
        else if(cleanName == "tabindex") {
            cleanName = "tabIndex";
        }
        else if(cleanName == "readonly") {
            cleanName = "readOnly";
        }
        else if(cleanName == "maxlength") {
            cleanName = "maxLength";
        }
        else if(cleanName == "rowspan") {
            cleanName = "rowSpan";
        }
        else if(cleanName == "valign") {
            cleanName = "vAlign";
        }
        
        return cleanName;
    };
};
