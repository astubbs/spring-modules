/**
 @fileoverview
 Spring Modules XT Ajax Framework custom version of the Taconite Ajax Framework.
 Taconite (http://taconite.sourceforge.net/) : Copyright (C) Ryan Asleson.
 */

var springxt_taconite_version=20070526;

var isIE=document.uniqueID;

var TaconiteDOMUtils = {
    getAllSubElements : function(element) {
        // Returns all sub elements. Workaround required for IE5/Windows.
        return element.all ? element.all : element.getElementsByTagName('*');
    },
    
    setMultipleSelectOptions : function(selectNodeId) {
        var selectNode = document.getElementById(selectNodeId);
        var options = document.getElementById(selectNodeId).getElementsByTagName("option");
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
    }
};


function AjaxRequest(url) {
    /** @private */
    var self = this;
    
    /** @private */
    var xmlHttp = createXMLHttpRequest();
    
    /** @private */
    var queryString = "";
    
    /** @private */
    var requestURL = url;
    
    /** @private */
    var method = "GET";
    
    /** @private */
    var preRequest = null;
    
    /** @private */
    var postRequest = null;
    
    /** @private */
    var debugResponse = false;
    
    /** @private */
    var async = true;
    
    /** @private errorHandler*/ 
    var errorHandler = null;
    
    
    /**
     Return the instance of the XMLHttpRequest object wrapped by this object.
     @return XMLHttpRequest
     */
    this.getXMLHttpRequestObject = function() {
        return xmlHttp;
    }
    
    /**
     Set the pre-request function. This function will be called prior to 
     sending the Ajax request. The pre-request function is passed a reference
     to this object.
     @param {Function} The function to be called prior to sending the Ajax
     request. The function is passed a refernce of this object.
     */
    this.setPreRequest = function(func) {
        preRequest = func;
    }
    
    /**
     Set the post-request function. This function will be called after the
     response has been received and after eval() has been called using the 
     XMLHttpRequest object's responseText. The post-request function is passed 
     a reference to this object.
     @param {Function} The function to be called after receiving the Ajax
     response. The function is passed a refernce of this object.
     */
    this.setPostRequest = function(func) {
        postRequest = func;
    }
    
    /**
     Return the post request function.
     */
    this.getPostRequest = function() {
        return postRequest;
    }
    
    /**
     Send the Ajax request using the POST method. Use with caution -- some
     browsers do not support the POST method with the XMLHttpRequest object.
     */
    this.setUsePOST = function() {
        method = "POST";
    }
    
    /**
     Send the Ajax request using the GET method, where parameters are sent
     as a query string appended to the URL. This is the default behavior.
     */
    this.setUseGET = function() {
        method = "GET";
    }
    
    /**
     Enable client-side debugging.  The server's response will be written
     to a text area appended to the bottom of the page.  If parsing is
     performed on the client side, then the results of the parsing operations
     are shown in their own text areas.
     */
    this.setEchoDebugInfo = function() {
        debugResponse = true;
    }
    
    /**
     Indicate if debugging is enabled.
     @return boolean
     */
    this.isEchoDebugInfo = function() {
        return debugResponse;
    }
    
    /**
     Set the query string that will be sent to the server. For GET
     requests, the query string is appended to the URL. For POST
     requests, the query string is sent in the request body. This 
     method is useful, for example, if you want to send an XML string
     or JSON string to the server.
     @param {String} qa, the new query string value.
     */
    this.setQueryString = function(qs) {
        queryString = qs;
    }
    
    /**
     Return the query string.
     @return The query string.
     */
    this.getQueryString = function() {
        return queryString;
    }
    
    /** 
     @param {Boolean} asyncBoolean, set to true if asynchronous request, false synchronous request. 
     */
    this.setAsync = function(asyncBoolean){
        async = asyncBoolean;
    }
    
    /** 
     @param {Function} Set the error handler function that is called if the 
     server's HTTP response code is something other than 200.
     */	
    this.setErrorHandler = function(func){
        errorHandler = func;
    }
    
    /**
     Add all of the form elements under the specified form to the query
     string to be sent to the server as part of the Ajax request. 
     The values are automatically encoded.
     @param form, A form DOM element, or the id attribute of the form element from
     which you wish to accumulate the form values.
     */
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
    }
    
    /**
     Add the name/value pair to the query string.
     @param {String} name
     @param {String} value
     */
    this.addNameValuePair = function(name, value) {
        var nameValuePair = name + "=" + encodeURIComponent(value);
        accumulateQueryString(nameValuePair);
    }
    
    /**
     Same as addNamedFormElements, except it will filter form elements by form's id.
     For example, these are all valid uses:<br>
     <br>ajaxRequest.addNamedFormElements("form-id""element-name-1");
     <br>ajaxRequest.addNamedFormElements("form-id","element-name-1",
     "element-name-2", "element-name-3");
     */
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
    }
    
    /**
     Add the values of the named form elements to the query string to be
     sent to the server as part of the Ajax request. This method takes any 
     number of Strings representing the form elements for wish you wish to 
     accumulate the values. The Strings must be the value of the element's 
     name attribute.<br><br>For example, these are all valid uses:<br>
     <br>ajaxRequest.addNamedFormElements("element-name-1");
     <br>ajaxRequest.addNamedFormElements("element-name-1", "element-name-2", "element-name-3");
     */
    this.addNamedFormElements = function() {
        var elementName = "";
        var namedElements = null;
        
        for(var i = 0; i < arguments.length; i++) {
            elementName = arguments[i];
            namedElements = document.getElementsByName(elementName);
            
            elementValues = toQueryString(namedElements);
            
            accumulateQueryString(elementValues);
        }
        
    }
    
    /**
     Add the values of the id'd form elements to the query string to be
     sent to the server as part of the Ajax request. This method takes any 
     number of Strings representing the ids of the form elements for wish you wish to 
     accumulate the values. The Strings must be the value of the element's 
     name attribute.<br><br>For example, these are all valid uses:<br>
     <br>ajaxRequest.addFormElementsById("element-id-1");
     <br>ajaxRequest.addFormElementsById("element-id-1", "element-id-2", "element-id-3");
     */
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
    }
    
    /**
     Send the Ajax request.
     */
    this.sendRequest = function() {
        if(preRequest) {
            preRequest(self);
        }
        
        var obj = this;
        if(async)
            xmlHttp.onreadystatechange = function () { handleStateChange(self) };
            
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
                if(errorHandler) {
                    errorHandler(self, exception);
                }
                else {
                    throw exception;
                }
            }
            
            if(!async) {  //synchronous request, handle the state change
                handleStateChange(self);
            }
            
            if(self.isEchoDebugInfo()) {
                echoRequestParams();
            }
    }
    
    
    /** @private */
    handleStateChange = function(ajaxRequest) {
        if(ajaxRequest.getXMLHttpRequestObject().readyState != 4) {
            return;
        }
        if(ajaxRequest.getXMLHttpRequestObject().status != 200) {
            errorHandler(self);
            return;
        }
        try {
            var debug = ajaxRequest.isEchoDebugInfo();
            if(debug) {
                echoResponse(ajaxRequest);
            }
            
            //handle null responseXML
            var nodes = null;
            if (ajaxRequest.getXMLHttpRequestObject().responseXML != null) {
                nodes = ajaxRequest.getXMLHttpRequestObject().responseXML.documentElement.childNodes;
            }
            else {
                nodes = new Array();
            }
            
            var parser = new XhtmlToDOMParser();
            for(var i = 0; i < nodes.length; i++) {
                if(nodes[i].nodeType != 1) {
                    continue;
                }
                parser.parseXhtml(nodes[i]);
            }
        }
        catch(exception) {
            if(errorHandler) {
                errorHandler(self, exception);
            }
            else {
                throw exception;
            }
        }
        finally {
            try {
                if(ajaxRequest.getPostRequest()) {
                    var f = ajaxRequest.getPostRequest();
                    f(ajaxRequest);
                }
            }
            catch(exception) {
                if(errorHandler) {
                    errorHandler(self, exception);
                }
            }
        }
    }
    
    
    /**
     Create an instance of the XMLHttpRequest object, using the appropriate
     method for the type of browser in which this script is running. For Internet
     Explorer, it's an ActiveX object, for all others it's a native JavaScript
     object.
     @return an instance of the XMLHttpRequest object.
     @private
     */
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
    }
    
    /** @private */
    function accumulateQueryString(newValues) {
        if(queryString == "") {
            queryString = newValues; 
        }
        else {
            queryString = queryString + "&" +  newValues;
        }
    }
    
    /** @private */
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
    }
    
    /** @private */
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
    }
    
    /** @private */
    function echoResponse(ajaxRequest) {
        var echoTextArea = document.getElementById("debugResponse");
        if(echoTextArea == null) {
            echoTextArea = createDebugTextArea("Server Response:", "debugResponse");
        }
        var debugText = ajaxRequest.getXMLHttpRequestObject().status 
        + " " + ajaxRequest.getXMLHttpRequestObject().statusText + "\n\n\n";
        echoTextArea.value = debugText + ajaxRequest.getXMLHttpRequestObject().responseText;
    }
    
    /** @private */
    function echoParsedJavaScript(js) {
        var echoTextArea = document.getElementById("debugParsedJavaScript");
        if(echoTextArea == null) {
            var echoTextArea = createDebugTextArea("Parsed JavaScript (by JavaScript Parser):", "debugParsedJavaScript");
        }
        echoTextArea.value = js;
    }
    
    /** @private */
    function createDebugTextArea(label, id) {
        echoTextArea = document.createElement("textarea");
        echoTextArea.setAttribute("id", id);
        echoTextArea.setAttribute("rows", "15");
        echoTextArea.setAttribute("style", "width:100%");
        echoTextArea.style.cssText = "width:100%";
        
        document.getElementsByTagName("body")[0].appendChild(document.createTextNode(label));
        document.getElementsByTagName("body")[0].appendChild(echoTextArea);
        return echoTextArea;
    }
    
    
    /** @private */
    function echoRequestParams() {
        var qsTextBox = document.getElementById("qsTextBox");
        if(qsTextBox == null) {
            qsTextBox = createDebugTextBox("Query String:", "qsTextBox");
        }
        qsTextBox.value = queryString;
        
        var urlTextBox = document.getElementById("urlTextBox");
        if(urlTextBox == null) {
            urlTextBox = createDebugTextBox("URL (Includes query string if GET request):", "urlTextBox");
        }
        urlTextBox.value = requestURL;
    }
    
    /** @private */
    function createDebugTextBox(label, id) {
        textBox = document.createElement("input");
        textBox.setAttribute("type", "text");
        textBox.setAttribute("id", id);
        textBox.setAttribute("style", "width:100%");
        textBox.style.cssText = "width:100%";
        
        document.getElementsByTagName("body")[0].appendChild(document.createTextNode(label));
        document.getElementsByTagName("body")[0].appendChild(textBox);
        return textBox;
    }
};


function XhtmlToDOMParser() {
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
        }
    }
    
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
    }
    
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
    }
    
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
    }
    
    function getContextNodesByWildcardMatch(context) {
        var contextNodeID = context.getElementsByTagName("matcher")[0].getAttribute("contextNodeID");
        var contextIDs = contextNodeID.split(',');
        var contextNodes = new Array();
        for (var i = 0; i < contextIDs.length; i++) {
            contextNodes = contextNodes.concat(document.getElementsByMatchingId(contextIDs[i].trim()));
        }
        return contextNodes;
    }
    
    function getContextNodesBySelectorMatch(context) {
        var selector = new DOMSelector();
        var contextNodeSelector = context.getElementsByTagName("matcher")[0].getAttribute("contextNodeSelector");
        var contextSelectors = contextNodeSelector.split(',');
        var contextNodes = new Array();
        for (var i = 0; i < contextSelectors.length; i++) {
            contextNodes = contextNodes.concat(selector.select(contextSelectors[i].trim()));
        }
        return contextNodes;
    }
    
    function setAttributesAction(domNode,xml){
        var sourceNode = xml.getElementsByTagName("attributes")[0];
        handleAttributes(domNode, sourceNode);
    }
    
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
    }
    
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
    }
    
    function insertBeforeAction(domNode,xml){
        var domChildNode=null;
        for(var i=0;i<xml.childNodes.length;i++){
            domChildNode=handleNode(xml.childNodes[i]);
            if(domChildNode!=null) {
                domNode.parentNode.insertBefore(domChildNode,domNode);
            }
        }              
    }  
    
    function replaceAction(domNode,xml){
        insertAfterAction(domNode,xml);
        domNode.parentNode.removeChild(domNode);
    }
    
    function deleteAction(domNode) {
        domNode.parentNode.removeChild(domNode);
    }
    
    function appendAsChildrenAction(domNode,xml) {
        internalAppendOrReplaceChildren(domNode, xml, false);
    }
    
    function replaceChildrenAction(domNode,xml) {
        internalAppendOrReplaceChildren(domNode, xml, true);
    }
    
    function executeRedirectAction(xmlNode) {
        var targetUrl = xmlNode.getElementsByTagName("target")[0].getAttribute("url");
        window.location.href = targetUrl;
    }
    
    function executeJavascriptAction(xmlNode) {
        var scripts = xmlNode.getElementsByTagName("script");
        for (var i = 0; i < scripts.length; i++) {
            var script = scripts[i];
            if (script.getAttribute("type") == "text/javascript") {
                var js = script.firstChild.nodeValue;
                eval(js);
            }
        }
    }
    
    function internalAppendOrReplaceChildren(domNode,xml,doRemoveChildren) {
        var domChildNode=null;
        if(doRemoveChildren){
            while(domNode.childNodes.length >0){
                domNode.removeChild(domNode.childNodes[0]);
            }      
        }
        for(var i=0;i<xml.childNodes.length;i++){
            domChildNode=handleNode(xml.childNodes[i]);
            checkForIEMultipleSelectHack(domNode, domChildNode);
            if(domChildNode!=null) {
                domNode.appendChild(domChildNode);
            }
        }              
    }
    
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
    }  
    
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
                    if(isIE) { 
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
                if(isIE) {
                    if(name == "multiple" && domNode.id != "") {
                        setTimeout("TaconiteDOMUtils.setMultipleSelectOptions('" + domNode.id + "');", 100);
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
    }
    
    function handleNode(xmlNode){
        var nodeType = xmlNode.nodeType;               
        switch(nodeType) {
            case 1:  //ELEMENT_NODE
                return handleElement(xmlNode);
            case 3:  //TEXT_NODE
            case 4:  //CDATA_SECTION_NODE
                var textNode = document.createTextNode(xmlNode.nodeValue);
                if(isIE) {
                    textNode.nodeValue = textNode.nodeValue.replace(/\n/g, '\r'); 
                }
                return textNode;
        }      
        return null;
    }
    
    function handleElement(xmlNode){
        var domElemNode = null;
        var xmlNodeTagName = xmlNode.tagName.toLowerCase();
        if(isIE){
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
            if(isIE && !domElemNode.canHaveChildren){
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
    }
    
    function useIEFormElementCreationStrategy(xmlNodeTagName) {
        var useIEStrategy = false;
        
        var nodeName = xmlNodeTagName.toLowerCase();
        if (isIE && (nodeName == "form" ||
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
    }
    
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
    }
    
    function checkForIEMultipleSelectHack(domNode, domChildNode) {
        if(isIE && domChildNode.nodeType == 1 && domChildNode.tagName.toLowerCase() == "select" && domChildNode.getAttribute("multiple") != null) {
            createIEMultipleSelectHack(domNode);
        }
    }
    
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
    }
    
    function cleanAttributeName(name) {
        if(isIE == false) {
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
    }
};


function DOMSelector() {
    
    this.select = function(selector, rootContext) {
        if (! rootContext) {
            rootContext = document;
        }
        // Remove unwanted spaces between combinators:
        // > combinator
        selector = selector.replace(/\s*(?=>)/g, '');
        selector = selector.replace(/>\s*/g, '>');
        // + combinator
        selector = selector.replace(/\s*(?=\+)/g, '');
        selector = selector.replace(/\+\s*/g, '+');
        // ~ combinator
        selector = selector.replace(/\s*(?=~)/g, '');
        selector = selector.replace(/~\s*/g, '~');
        // Split selector into tokens
        var splitter = /\s|>|\+|~/g;
        var combinators = selector.match(splitter);
        var tokens = selector.split(splitter);
        var currentContext = new Array(rootContext);
        // Prepare regular expressions that will be used later:
        var attributesRegexp = /^(\w*)\[(\w+)([=~\|\^\$\*]?)=?"?([^\]"]*)"?\]$/;
        var pseudoClassesRegexp = /^(\w*)\:(\w+-?\w+)/;
        var regexpResult = null;
        // Go!
        for (var i = 0; i < tokens.length; i++) {
            var combinator = i == 0 ? " " : combinators[i - 1];
            var token = tokens[i].trim();
            if (token.indexOf('#') > -1) {
                // Token is an ID selector
                var tagName = token.substring(0, token.indexOf('#'));
                var id = token.substring(token.indexOf('#') + 1, token.length);
                var filterFunction = function(e) { 
                    return (e.id == id); 
                };
                var found = new Array();
                for (var h = 0; h < currentContext.length; h++) {
                    found = selectByFilter(combinator, currentContext[h], tagName, filterFunction);
                    if (found && found.length == 1) {
                        break;
                    }
                }
                // Set currentContext to contain just this element
                currentContext = found;
                // Skip to next token
                continue; 
            }
            else if ((regexpResult = attributesRegexp.exec(token))) {
                // Token contains attribute selectors
                var tagName = regexpResult[1];
                var attrName = regexpResult[2];
                var attrOperator = regexpResult[3];
                var attrValue = regexpResult[4];
                // Attribute filtering functions:
                var filterFunction = null; // This function will be used to filter the elements
                switch (attrOperator) {
                    case '=': // Equality
                        filterFunction = function(e) { 
                            return (e.getAttribute(attrName) && e.getAttribute(attrName) == attrValue); 
                        };
                        break;
                    case '~': // Match one of space seperated words 
                        filterFunction = function(e) { 
                            return (e.getAttribute(attrName) && e.getAttribute(attrName).match(new RegExp('(\\s|^)' + attrValue + '(\\s|$)'))); 
                        };
                        break;
                    case '|': // Match start with value followed by optional hyphen
                        filterFunction = function(e) { 
                            return (e.getAttribute(attrName) && e.getAttribute(attrName).match(new RegExp('^' + attrValue + '-?'))); 
                        };
                        break;
                    case '^': // Match starts with value
                        filterFunction = function(e) { 
                            return (e.getAttribute(attrName) && e.getAttribute(attrName).indexOf(attrValue) == 0); 
                        };
                        break;
                    case '$': // Match ends with value - fails with "Warning" in Opera 7
                        filterFunction = function(e) { 
                            return (e.getAttribute(attrName) && (e.getAttribute(attrName).lastIndexOf(attrValue) == e.getAttribute(attrName).length - attrValue.length)); 
                        };
                        break;
                    case '*': // Match contains value
                        filterFunction = function(e) { 
                            return (e.getAttribute(attrName) && e.getAttribute(attrName).indexOf(attrValue) > -1); 
                        };
                        break;
                    default :
                        // Just test for existence of attribute
                        filterFunction = function(e) { 
                            return e.getAttribute(attrName); 
                        };
                }
                var found = new Array();
                var counter = 0;
                for (var h = 0; h < currentContext.length; h++) {
                    var elements = selectByFilter(combinator, currentContext[h], tagName, filterFunction);
                    for (var j = 0; j < elements.length; j++) {
                        found[counter++] = elements[j];
                    }
                }
                currentContext = found;
                // Skip to next token
                continue;
            } 
            else if ((regexpResult = pseudoClassesRegexp.exec(token))) {
                // Token contains pseudo-class selectors
                var tagName = regexpResult[1];
                var pseudoClass = regexpResult[2];
                // Pseudo class filtering functions:
                var filterFunction = null; // This function will be used to filter the elements
                switch (pseudoClass) {
                    case 'first-child': 
                        filterFunction = function(e) { 
                            var parent = e.parentNode;
                            var i = 0;
                            while (parent.childNodes[i] && parent.childNodes[i].nodeType != 1) i++;
                            return (parent.childNodes[i] == e); 
                        };
                        break;
                    case 'last-child': 
                        filterFunction = function(e) { 
                            var parent = e.parentNode;
                            var i = parent.childNodes.length - 1;
                            while (parent.childNodes[i] && parent.childNodes[i].nodeType != 1) i--;
                            return (parent.childNodes[i] == e); 
                        };
                        break;    
                    case 'empty': 
                        filterFunction = function(e) { 
                            return (e.childNodes.length == 0); 
                        };
                        break;
                    default :
                        filterFunction = function(e) { 
                            return false; 
                        };
                }
                var found = new Array();
                var counter = 0;
                for (var h = 0; h < currentContext.length; h++) {
                    var elements = selectByFilter(combinator, currentContext[h], tagName, filterFunction);
                    for (var j = 0; j < elements.length; j++) {
                        found[counter++] = elements[j];
                    }
                }
                currentContext = found;
                // Skip to next token
                continue;
            } 
            else if (token.indexOf('.') > -1) {
                // Token contains a class selector
                var tagName = token.substring(0, token.indexOf('.'));
                var className = token.substring(token.indexOf('.') + 1, token.length);
                var regexp = new RegExp('(\\s|^)' + className + '(\\s|$)');
                var filterFunction = function(e) { 
                    return (e.className && e.className.match(regexp)); 
                };
                var found = new Array();
                var counter = 0;
                for (var h = 0; h < currentContext.length; h++) {
                    var elements = selectByFilter(combinator, currentContext[h], tagName, filterFunction);
                    for (var j = 0; j < elements.length; j++) {
                        found[counter++] = elements[j];
                    }
                }
                currentContext = found;
                // Skip to next token
                continue; 
            }
            else {
                // If we get here, token is just an element (not a class or ID selector)
                tagName = token;
                var filterFunction = function(e) { 
                    return true; 
                };
                var found = new Array();
                var counter = 0;
                for (var h = 0; h < currentContext.length; h++) {
                    var elements = selectByFilter(combinator, currentContext[h], tagName, filterFunction);
                    for (var j = 0; j < elements.length; j++) {
                        found[counter++] = elements[j];
                    }
                }
                currentContext = found;
            }
        }
        return currentContext;
    }
    
    function selectByFilter(combinator, context, tagName, filterFunction) {
        var result = new Array();
        var elements = new Array();
        // Get elements to filter depending on the combinator:
        if (combinator == " ") {
            elements = TaconiteDOMUtils.getAllSubElements(context);
        } else if (combinator == ">") {
            elements = context.childNodes;
        } else if (combinator == "+") {
            var sibling = context.nextSibling;
            while (sibling && sibling.nodeType != 1) {
                sibling = sibling.nextSibling;
            }
            if (sibling) elements = new Array(sibling);
            else elements = new Array();
        } else if (combinator == "~") {
            var sibling = context.nextSibling;
            var counter = 0;
            while (sibling) {
                if (sibling.nodeType == 1) {
                    elements[counter] = sibling;
                    counter++;
                }
                sibling = sibling.nextSibling;
            }
        }
        // Actually filter elements by tag name and filter function:
        var counter = 0;
        if (!tagName || tagName == '*') {
            for (var k = 0; k < elements.length; k++) {
                if (elements[k].nodeType == 1 && filterFunction(elements[k])) {
                    result[counter] = elements[k];
                    counter++;
                }
            }
        } else {
            for (var k = 0; k < elements.length; k++) {
                if (elements[k].nodeType == 1 && elements[k].nodeName.toLowerCase() == tagName.toLowerCase() && filterFunction(elements[k])) {
                    result[counter] = elements[k];
                    counter++;
                }
            }
        }
        return result;
    }
};


String.prototype.trim = function() {
    //skip leading and trailing whitespace
    //and return everything in between
    var x=this;
    x=x.replace(/^\s*(.*)/, "$1");
    x=x.replace(/(.*?)\s*$/, "$1");
    return x;
};

document.getElementsByMatchingId = function(matchingId) {
    var allElements = TaconiteDOMUtils.getAllSubElements(document);
    var matchingElements = new Array();
    for (var i = 0; i < allElements.length; i++) {
        var currentElement = allElements[i];
        if (currentElement.nodeType == 1) {
            var id = currentElement.getAttribute("id");
            if (id != null && id != "") {
                if (id.indexOf("_") == (id.length - 1)) {
                    var pattern = "^" + id.replace(/_$/, ".*");
                    var rexp = new RegExp(pattern);
                    if (rexp.test(matchingId)) {
                        matchingElements.push(currentElement);
                    }
                } else if (id == matchingId) {
                    matchingElements.push(currentElement);
                }
            }
        }
    }
    return matchingElements;
};
