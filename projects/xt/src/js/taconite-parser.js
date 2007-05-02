/**
 @fileoverview
 This JavaScript file represents the JavaScript parser used by Taconite
 for converting Xhtml documents sent by the server into actions acting on the
 Javascript DOM.
 
 Copyright notes:
 The DOMSelector is based on document.getElementsBySelector(selector) method, Copyright (C) Simon Willison 2004.
 */

var taconite_parser_xt_version=20070502;

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

function XhtmlToDOMParser(){
    
    this.parseXhtml = function(xml){
        var xmlTagName=xml.tagName.toLowerCase();
        if (requiresContextNode(xmlTagName)) {
            var contextNodes = getContextNodes(xml);
            if(contextNodes == null || contextNodes.length == 0) {
                return false;
            } else {
                // Remove no more necessary attributes:
                xml.removeAttribute("contextNodeID");
                xml.removeAttribute("contextNodeSelector");
                xml.removeAttribute("matchMode");
                xml.removeAttribute("parseInBrowser");
                //
                switch (xmlTagName) {
                    case "taconite-append-as-children":
                        for (var i = 0; i < contextNodes.length; i++) {
                            var contextNode = contextNodes[i];
                            getReplaceChildren(contextNode,xml,false);
                        }
                        break;
                    case "taconite-delete":
                        for (var i = 0; i < contextNodes.length; i++) {
                            var contextNode = contextNodes[i];
                            getDelete(contextNode,xml);
                        }
                        break;
                    case "taconite-append-as-first-child":
                        for (var i = 0; i < contextNodes.length; i++) {
                            var contextNode = contextNodes[i];
                            getAppendAsFirstChild(contextNode,xml);
                        }
                        break;                         
                    case "taconite-insert-after":
                        for (var i = 0; i < contextNodes.length; i++) {
                            var contextNode = contextNodes[i];
                            getInsertAfter(contextNode,xml);
                        }
                        break;
                    case "taconite-insert-before":
                        for (var i = 0; i < contextNodes.length; i++) {
                            var contextNode = contextNodes[i];
                            getInsertBefore(contextNode,xml);
                        }
                        break;                         
                    case "taconite-replace-children":
                        for (var i = 0; i < contextNodes.length; i++) {
                            var contextNode = contextNodes[i];
                            getReplaceChildren(contextNode,xml,true);
                        }
                        break;
                    case "taconite-replace":
                        for (var i = 0; i < contextNodes.length; i++) {
                            var contextNode = contextNodes[i];
                            getReplace(contextNode,xml);
                        }
                        break;                         
                    case "taconite-set-attributes":
                        for (var i = 0; i < contextNodes.length; i++) {
                            var contextNode = contextNodes[i];
                            handleAttributes(contextNode,xml);
                        }
                        break;
                }
                return true;
            }
        } else {
            switch (xmlTagName) {
                case "taconite-redirect":
                    handleRedirect(xml);
                    break;
                case "taconite-execute-javascript":
                    executeJavascript(xml);
                    break;
            }  
            return true;
        }
    }
    
    this.getJavaScript= function() {
        return "var dummy_taconite_variable=0";
    }
    
    function requiresContextNode(xmlTagName) {
        return !(xmlTagName == "taconite-execute-javascript" || xmlTagName == "taconite-redirect");
    }
    
    function getContextNodes(xml) {
        var matchMode = xml.getAttribute("matchMode");
        var contextNodes = new Array();
        if (matchMode != null) {
            switch(matchMode) {
                case "plain" : 
                    contextNodes = getContextNodesByPlainMatch(xml);
                    break;
                case "wildcard" : 
                    contextNodes = getContextNodesByWildcardMatch(xml);
                    break;
                case "selector" : 
                    contextNodes = getContextNodesBySelectorMatch(xml);
                    break;
            }
        } else {
            contextNodes = getContextNodesByPlainMatch(xml);
        }
        return contextNodes;
    }
    
    function getContextNodesByPlainMatch(xml) {
        var contextNodeID = xml.getAttribute("contextNodeID");
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
    
    function getContextNodesByWildcardMatch(xml) {
        var contextNodeID = xml.getAttribute("contextNodeID");
        var contextIDs = contextNodeID.split(',');
        var contextNodes = new Array();
        for (var i = 0; i < contextIDs.length; i++) {
            contextNodes = contextNodes.concat(document.getElementsByMatchingId(contextIDs[i].trim()));
        }
        return contextNodes;
    }
    
    function getContextNodesBySelectorMatch(xml) {
        var selector = new DOMSelector();
        var contextNodeSelector = xml.getAttribute("contextNodeSelector");
        var contextSelectors = contextNodeSelector.split(',');
        var contextNodes = new Array();
        for (var i = 0; i < contextSelectors.length; i++) {
            contextNodes = contextNodes.concat(selector.select(contextSelectors[i].trim()));
        }
        return contextNodes;
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
    
    function handleAttributes(domNode, xmlNode) {
        var attr = null;
        var attrString = "";
        var name = "";
        var value = "";
        var returnAsText = false;
        if(arguments.length == 3) {
            returnAsText = true;
        }
        
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
    
    function getAppendAsFirstChild(domNode,xml){
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
    
    function getInsertAfter(domNode,xml){
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
    
    function getInsertBefore(domNode,xml){
        var domChildNode=null;
        for(var i=0;i<xml.childNodes.length;i++){
            domChildNode=handleNode(xml.childNodes[i]);
            if(domChildNode!=null) {
                domNode.parentNode.insertBefore(domChildNode,domNode);
            }
        }              
    }  
    
    function getReplace(domNode,xml){
        getInsertAfter(domNode,xml);
        domNode.parentNode.removeChild(domNode);
    }
    
    function getDelete(domNode) {
        domNode.parentNode.removeChild(domNode);
    }
    
    function getReplaceChildren(domNode,xml,doRemoveChildren) {
        var domChildNode=null;
        if(doRemoveChildren){
            while(domNode.childNodes.length >0){
                domNode.removeChild(domNode.childNodes[0]);
            }      
        }
        for(var i=0;i<xml.childNodes.length;i++){
            domChildNode=handleNode(xml.childNodes[i]);
            if(domChildNode!=null) {
                domNode.appendChild(domChildNode);
            }
        }              
    }
    
    function handleRedirect(xmlNode) {
        var targetUrl = xmlNode.getAttribute("targetUrl");
        window.location.href = targetUrl;
    }
    
    function executeJavascript(xmlNode) {
        var scripts = xmlNode.getElementsByTagName("script");
        for (var i = 0; i < scripts.length; i++) {
            var script = scripts[i];
            if (script.getAttribute("type") == "text/javascript") {
                var js = script.firstChild.nodeValue;
                eval(js);
            }
        }
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
                            return (e.getAttribute(attrName) == attrValue); 
                        };
                        break;
                    case '~': // Match one of space seperated words 
                        filterFunction = function(e) { 
                            return (e.getAttribute(attrName).match(new RegExp('(\\s|^)' + attrValue + '(\\s|$)'))); 
                        };
                        break;
                    case '|': // Match start with value followed by optional hyphen
                        filterFunction = function(e) { 
                            return (e.getAttribute(attrName).match(new RegExp('^' + attrValue + '-?'))); 
                        };
                        break;
                    case '^': // Match starts with value
                        filterFunction = function(e) { 
                            return (e.getAttribute(attrName).indexOf(attrValue) == 0); 
                        };
                        break;
                    case '$': // Match ends with value - fails with "Warning" in Opera 7
                        filterFunction = function(e) { 
                            return (e.getAttribute(attrName).lastIndexOf(attrValue) == e.getAttribute(attrName).length - attrValue.length); 
                        };
                        break;
                    case '*': // Match contains value
                        filterFunction = function(e) { 
                            return (e.getAttribute(attrName).indexOf(attrValue) > -1); 
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