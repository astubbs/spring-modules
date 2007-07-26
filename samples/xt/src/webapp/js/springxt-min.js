
var springxt_json_version=20070526;(function(){var m={'\b':'\\b','\t':'\\t','\n':'\\n','\f':'\\f','\r':'\\r','"':'\\"','\\':'\\\\'},s={array:function(x){var a=['['],b,f,i,l=x.length,v;for(i=0;i<l;i+=1){v=x[i];f=s[typeof v];if(f){v=f(v);if(typeof v=='string'){if(b){a[a.length]=',';}
a[a.length]=v;b=true;}}}
a[a.length]=']';return a.join('');},'boolean':function(x){return String(x);},'null':function(x){return"null";},number:function(x){return isFinite(x)?String(x):'null';},object:function(x){if(x){if(x instanceof Array){return s.array(x);}
var a=['{'],b,f,i,v;for(i in x){v=x[i];f=s[typeof v];if(f){v=f(v);if(typeof v=='string'){if(b){a[a.length]=',';}
a.push(s.string(i),':',v);b=true;}}}
a[a.length]='}';return a.join('');}
return'null';},string:function(x){if(/["\\\x00-\x1f]/.test(x)){x=x.replace(/([\x00-\x1f\\"])/g,function(a,b){var c=m[b];if(c){return c;}
c=b.charCodeAt();return'\\u00'+
Math.floor(c/16).toString(16)+
(c%16).toString(16);});}
return'"'+x+'"';}};Object.prototype.toJSONString=function(){return s.object(this);};Array.prototype.toJSONString=function(){return s.array(this);};})();String.prototype.parseJSON=function(){try{return!(/[^,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t]/.test(this.replace(/"(\\.|[^"\\])*"/g,'')))&&eval('('+this+')');}catch(e){return false;}};var springxt_taconite_version=20070726;var isIE=document.uniqueID;var TaconiteDOMUtils={getAllSubElements:function(element){return element.all?element.all:element.getElementsByTagName('*');},setMultipleSelectOptions:function(selectNodeId){var selectNode=document.getElementById(selectNodeId);var options=document.getElementById(selectNodeId).getElementsByTagName("option");var option;for(var i=0;i<options.length;i++){option=options[i];if(typeof(option.taconiteOptionSelected)!="undefined"){option.selected=true;}
else{option.selected=false;}}}};function AjaxRequest(url){var self=this;var xmlHttp=createXMLHttpRequest();var queryString="";var requestURL=url;var method="GET";var preRequest=null;var postRequest=null;var debugResponse=false;var async=true;var errorHandler=null;this.getXMLHttpRequestObject=function(){return xmlHttp;}
this.setPreRequest=function(func){preRequest=func;}
this.setPostRequest=function(func){postRequest=func;}
this.getPostRequest=function(){return postRequest;}
this.setUsePOST=function(){method="POST";}
this.setUseGET=function(){method="GET";}
this.setEchoDebugInfo=function(){debugResponse=true;}
this.isEchoDebugInfo=function(){return debugResponse;}
this.setQueryString=function(qs){queryString=qs;}
this.getQueryString=function(){return queryString;}
this.setAsync=function(asyncBoolean){async=asyncBoolean;}
this.setErrorHandler=function(func){errorHandler=func;}
this.addFormElements=function(form){var formElements=new Array();if(form!=null){if(typeof form=="string"){var el=document.getElementById(form);if(el!=null){formElements=el.elements;}}else{formElements=form.elements;}}
var values=toQueryString(formElements);accumulateQueryString(values);}
this.addNameValuePair=function(name,value){var nameValuePair=name+"="+encodeURIComponent(value);accumulateQueryString(nameValuePair);}
this.addNamedFormElementsByFormID=function(){var elementName="";var namedElements=null;for(var i=1;i<arguments.length;i++){elementName=arguments[i];namedElements=document.getElementsByName(elementName);var arNamedElements=new Array();for(j=0;j<namedElements.length;j++){if(namedElements[j].form&&namedElements[j].form.getAttribute("id")==arguments[0]){arNamedElements.push(namedElements[j]);}}
if(arNamedElements.length>0){elementValues=toQueryString(arNamedElements);accumulateQueryString(elementValues);}}}
this.addNamedFormElements=function(){var elementName="";var namedElements=null;for(var i=0;i<arguments.length;i++){elementName=arguments[i];namedElements=document.getElementsByName(elementName);elementValues=toQueryString(namedElements);accumulateQueryString(elementValues);}}
this.addFormElementsById=function(){var id="";var element=null;var elements=new Array();for(var h=0;h<arguments.length;h++){element=document.getElementById(arguments[h]);if(element!=null){elements[h]=element;}}
elementValues=toQueryString(elements);accumulateQueryString(elementValues);}
this.sendRequest=function(){if(preRequest){preRequest(self);}
var obj=this;if(async)
xmlHttp.onreadystatechange=function(){handleStateChange(self)};if(requestURL.indexOf("?")>0){requestURL=requestURL+"&ts="+new Date().getTime();}
else{requestURL=requestURL+"?ts="+new Date().getTime();}
try{if(method=="GET"){if(queryString.length>0){requestURL=requestURL+"&"+queryString;}
xmlHttp.open(method,requestURL,async);xmlHttp.send(null);}
else{xmlHttp.open(method,requestURL,async);try{if(xmlHttp.overrideMimeType){xmlHttp.setRequestHeader("Connection","close");}}
catch(e){}
xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");xmlHttp.send(queryString);}}
catch(exception){if(errorHandler){errorHandler(self,exception);}
else{throw exception;}}
if(!async){handleStateChange(self);}
if(self.isEchoDebugInfo()){echoRequestParams();}}
handleStateChange=function(ajaxRequest){if(ajaxRequest.getXMLHttpRequestObject().readyState!=4){return;}
if(ajaxRequest.getXMLHttpRequestObject().status!=200){errorHandler(self);return;}
try{var debug=ajaxRequest.isEchoDebugInfo();if(debug){echoResponse(ajaxRequest);}
var nodes=null;if(ajaxRequest.getXMLHttpRequestObject().responseXML!=null){nodes=ajaxRequest.getXMLHttpRequestObject().responseXML.documentElement.childNodes;}
else{nodes=new Array();}
var parser=new XhtmlToDOMParser();for(var i=0;i<nodes.length;i++){if(nodes[i].nodeType!=1){continue;}
parser.parseXhtml(nodes[i]);}}
catch(exception){if(errorHandler){errorHandler(self,exception);}
else{throw exception;}}
finally{try{if(ajaxRequest.getPostRequest()){var f=ajaxRequest.getPostRequest();f(ajaxRequest);}}
catch(exception){if(errorHandler){errorHandler(self,exception);}}}}
function createXMLHttpRequest(){var req=false;if(window.XMLHttpRequest){req=new XMLHttpRequest();}
else if(window.ActiveXObject){try{req=new ActiveXObject("Msxml2.XMLHTTP");}
catch(e){try{req=new ActiveXObject("Microsoft.XMLHTTP");}
catch(e){req=false;}}}
return req;}
function accumulateQueryString(newValues){if(queryString==""){queryString=newValues;}
else{queryString=queryString+"&"+newValues;}}
function toQueryString(elements){var node=null;var qs="";var name="";var tempString="";for(var i=0;i<elements.length;i++){tempString="";node=elements[i];name=node.getAttribute("name");if(!name){name=node.getAttribute("id");}
name=encodeURIComponent(name);if(node.tagName.toLowerCase()=="input"){if(node.type.toLowerCase()=="radio"||node.type.toLowerCase()=="checkbox"){if(node.checked){tempString=name+"="+encodeURIComponent(node.value);}}
if(node.type.toLowerCase()=="text"||node.type.toLowerCase()=="hidden"||node.type.toLowerCase()=="password"){tempString=name+"="+encodeURIComponent(node.value);}}
else if(node.tagName.toLowerCase()=="select"){tempString=getSelectedOptions(node);}
else if(node.tagName.toLowerCase()=="textarea"){tempString=name+"="+encodeURIComponent(node.value);}
if(tempString!=""){if(qs==""){qs=tempString;}
else{qs=qs+"&"+tempString;}}}
return qs;}
function getSelectedOptions(select){var options=select.options;var option=null;var qs="";var tempString="";for(var x=0;x<options.length;x++){tempString="";option=options[x];if(option.selected){tempString=encodeURIComponent(select.name)+"="+encodeURIComponent(option.value);}
if(tempString!=""){if(qs==""){qs=tempString;}
else{qs=qs+"&"+tempString;}}}
return qs;}
function echoResponse(ajaxRequest){var echoTextArea=document.getElementById("debugResponse");if(echoTextArea==null){echoTextArea=createDebugTextArea("Server Response:","debugResponse");}
var debugText=ajaxRequest.getXMLHttpRequestObject().status
+" "+ajaxRequest.getXMLHttpRequestObject().statusText+"\n\n\n";echoTextArea.value=debugText+ajaxRequest.getXMLHttpRequestObject().responseText;}
function echoParsedJavaScript(js){var echoTextArea=document.getElementById("debugParsedJavaScript");if(echoTextArea==null){var echoTextArea=createDebugTextArea("Parsed JavaScript (by JavaScript Parser):","debugParsedJavaScript");}
echoTextArea.value=js;}
function createDebugTextArea(label,id){echoTextArea=document.createElement("textarea");echoTextArea.setAttribute("id",id);echoTextArea.setAttribute("rows","15");echoTextArea.setAttribute("style","width:100%");echoTextArea.style.cssText="width:100%";document.getElementsByTagName("body")[0].appendChild(document.createTextNode(label));document.getElementsByTagName("body")[0].appendChild(echoTextArea);return echoTextArea;}
function echoRequestParams(){var qsTextBox=document.getElementById("qsTextBox");if(qsTextBox==null){qsTextBox=createDebugTextBox("Query String:","qsTextBox");}
qsTextBox.value=queryString;var urlTextBox=document.getElementById("urlTextBox");if(urlTextBox==null){urlTextBox=createDebugTextBox("URL (Includes query string if GET request):","urlTextBox");}
urlTextBox.value=requestURL;}
function createDebugTextBox(label,id){textBox=document.createElement("input");textBox.setAttribute("type","text");textBox.setAttribute("id",id);textBox.setAttribute("style","width:100%");textBox.style.cssText="width:100%";document.getElementsByTagName("body")[0].appendChild(document.createTextNode(label));document.getElementsByTagName("body")[0].appendChild(textBox);return textBox;}};function XhtmlToDOMParser(){this.parseXhtml=function(xml){var xmlTagName=xml.tagName.toLowerCase();switch(xmlTagName){case"append-as-children":executeAction(xml,appendAsChildrenAction);break;case"delete":executeAction(xml,deleteAction);break;case"append-as-first-child":executeAction(xml,appendAsFirstChildAction);break;case"insert-after":executeAction(xml,insertAfterAction);break;case"insert-before":executeAction(xml,insertBeforeAction);break;case"replace-children":executeAction(xml,replaceChildrenAction);break;case"replace":executeAction(xml,replaceAction);break;case"set-attributes":executeAction(xml,setAttributesAction);break;case"redirect":executeAction(xml,executeRedirectAction);break;case"execute-javascript":executeAction(xml,executeJavascriptAction);break;}}
function executeAction(xml,action){var context=xml.getElementsByTagName("context")[0];var content=xml.getElementsByTagName("content")[0];if(context){var contextNodes=getContextNodes(context);for(var i=0;i<contextNodes.length;i++){var contextNode=contextNodes[i];action(contextNode,content);}}else{action(content);}}
function getContextNodes(context){var matchMode=context.getElementsByTagName("matcher")[0].getAttribute("matchMode");var contextNodes=new Array();if(matchMode!=null){switch(matchMode){case"plain":contextNodes=getContextNodesByPlainMatch(context);break;case"wildcard":contextNodes=getContextNodesByWildcardMatch(context);break;case"selector":contextNodes=getContextNodesBySelectorMatch(context);break;}}else{contextNodes=getContextNodesByPlainMatch(context);}
return contextNodes;}
function getContextNodesByPlainMatch(context){var contextNodeID=context.getElementsByTagName("matcher")[0].getAttribute("contextNodeID");var contextIDs=contextNodeID.split(',');var contextNodes=new Array();for(var i=0,k=0;i<contextIDs.length;i++){var tmp=document.getElementById(contextIDs[i].trim());if(tmp!=null){contextNodes[k]=tmp;k++;}}
return contextNodes;}
function getContextNodesByWildcardMatch(context){var contextNodeID=context.getElementsByTagName("matcher")[0].getAttribute("contextNodeID");var contextIDs=contextNodeID.split(',');var contextNodes=new Array();for(var i=0;i<contextIDs.length;i++){contextNodes=contextNodes.concat(document.getElementsByMatchingId(contextIDs[i].trim()));}
return contextNodes;}
function getContextNodesBySelectorMatch(context){var selector=new DOMSelector();var contextNodeSelector=context.getElementsByTagName("matcher")[0].getAttribute("contextNodeSelector");var contextSelectors=contextNodeSelector.split(',');var contextNodes=new Array();for(var i=0;i<contextSelectors.length;i++){contextNodes=contextNodes.concat(selector.select(contextSelectors[i].trim()));}
return contextNodes;}
function setAttributesAction(domNode,xml){var sourceNode=xml.getElementsByTagName("attributes")[0];handleAttributes(domNode,sourceNode);}
function appendAsFirstChildAction(domNode,xml){var firstNode=null;if(domNode.childNodes.length>0){firstNode=domNode.childNodes[0];}
for(var i=0;i<xml.childNodes.length;i++){domChildNode=handleNode(xml.childNodes[i]);if(domChildNode!=null){if(firstNode==null){domNode.appendChild(domChildNode);firstNode=domChildNode;}
else{domNode.insertBefore(domChildNode,firstNode);}}}}
function insertAfterAction(domNode,xml){var domChildNode=null;var nextSibling=domNode.nextSibling;for(var i=0;i<xml.childNodes.length;i++){domChildNode=handleNode(xml.childNodes[i]);if(domChildNode!=null){if(nextSibling!=null){domNode.parentNode.insertBefore(domChildNode,nextSibling);}
else{domNode.parentNode.appendChild(domChildNode);}}}}
function insertBeforeAction(domNode,xml){var domChildNode=null;for(var i=0;i<xml.childNodes.length;i++){domChildNode=handleNode(xml.childNodes[i]);if(domChildNode!=null){domNode.parentNode.insertBefore(domChildNode,domNode);}}}
function replaceAction(domNode,xml){insertAfterAction(domNode,xml);domNode.parentNode.removeChild(domNode);}
function deleteAction(domNode){domNode.parentNode.removeChild(domNode);}
function appendAsChildrenAction(domNode,xml){internalAppendOrReplaceChildren(domNode,xml,false);}
function replaceChildrenAction(domNode,xml){internalAppendOrReplaceChildren(domNode,xml,true);}
function executeRedirectAction(xmlNode){var targetUrl=xmlNode.getElementsByTagName("target")[0].getAttribute("url");window.location.href=targetUrl;}
function executeJavascriptAction(xmlNode){var scripts=xmlNode.getElementsByTagName("script");for(var i=0;i<scripts.length;i++){var script=scripts[i];if(script.getAttribute("type")=="text/javascript"){var js="";for(var i=0;i<script.childNodes.length;i++){js=js+script.childNodes[i].nodeValue;}
eval(js);}}}
function internalAppendOrReplaceChildren(domNode,xml,doRemoveChildren){var domChildNode=null;if(doRemoveChildren){while(domNode.childNodes.length>0){domNode.removeChild(domNode.childNodes[0]);}}
for(var i=0;i<xml.childNodes.length;i++){domChildNode=handleNode(xml.childNodes[i]);checkForIEMultipleSelectHack(domNode,domChildNode);if(domChildNode!=null){domNode.appendChild(domChildNode);}}}
function isInlineMode(node){var lowerCaseTag=node.tagName.toLowerCase();if(lowerCaseTag=="button"){return true;}
var attrType=node.getAttribute("type");if(lowerCaseTag=="input"&&(attrType=="radio"||attrType=="checkbox")){return true;}
return false;}
function handleAttributes(domNode,xmlNode,returnAsText){var attr=null;var attrString="";var name="";var value="";for(var x=0;x<xmlNode.attributes.length;x++){attr=xmlNode.attributes[x];name=cleanAttributeName(attr.name.trim());value=attr.value.trim();if(!returnAsText){if(name=="style"){domNode.style.cssText=value;domNode.setAttribute(name,value);}
else if(name.trim().toLowerCase().substring(0,2)=="on"){if(isIE){eval("domNode."+name.trim().toLowerCase()+"=function(){"+value+"}");}
else{domNode.setAttribute(name,value);}}
else if(name=="value"){domNode.value=value;}
else if(useIEFormElementCreationStrategy(xmlNode.tagName)&&name=="name"){continue;}
else{domNode.setAttribute(name,value);}
if(name=="class"){domNode.setAttribute("className",value);}
if(isIE){if(name=="multiple"&&domNode.id!=""){setTimeout("TaconiteDOMUtils.setMultipleSelectOptions('"+domNode.id+"');",100);}
if(name=="selected"){domNode.taconiteOptionSelected=true;}}}else{attrString=attrString+name+"=\""+value+"\" ";}}
return attrString;}
function handleNode(xmlNode){var nodeType=xmlNode.nodeType;switch(nodeType){case 1:return handleElement(xmlNode);case 3:case 4:var textNode=document.createTextNode(xmlNode.nodeValue);if(isIE){textNode.nodeValue=textNode.nodeValue.replace(/\n/g,'\r');}
return textNode;}
return null;}
function handleElement(xmlNode){var domElemNode=null;var xmlNodeTagName=xmlNode.tagName.toLowerCase();if(isIE){if(isInlineMode(xmlNode)){return document.createElement("<"+xmlNodeTagName+" "+handleAttributes(domElemNode,xmlNode,true)+">");}
if(xmlNodeTagName=="style"){var text,rulesArray,styleSheetPtr;var regExp=/\s+/g;text=xmlNode.text.replace(regExp," ");rulesArray=text.split("}");domElemNode=document.createElement("style");styleSheetPtr=document.styleSheets[document.styleSheets.length-1];for(var i=0;i<rulesArray.length;i++){rulesArray[i]=rulesArray[i].trim();var rulePart=rulesArray[i].split("{");if(rulePart.length==2){styleSheetPtr.addRule(rulePart[0],rulePart[1],-1);}}
return domElemNode;}}
if(domElemNode==null){if(useIEFormElementCreationStrategy(xmlNodeTagName)){domElemNode=createFormElementsForIEStrategy(xmlNode);}
else{domElemNode=document.createElement(xmlNodeTagName);}
handleAttributes(domElemNode,xmlNode);if(isIE&&!domElemNode.canHaveChildren){if(xmlNode.childNodes.length>0){domElemNode.text=xmlNode.text;}}
else{for(var z=0;z<xmlNode.childNodes.length;z++){var domChildNode=handleNode(xmlNode.childNodes[z]);if(domChildNode!=null){domElemNode.appendChild(domChildNode);}}}}
return domElemNode;}
function useIEFormElementCreationStrategy(xmlNodeTagName){var useIEStrategy=false;var nodeName=xmlNodeTagName.toLowerCase();if(isIE&&(nodeName=="form"||nodeName=="input"||nodeName=="textarea"||nodeName=="select"||nodeName=="a"||nodeName=="applet"||nodeName=="button"||nodeName=="img"||nodeName=="link"||nodeName=="map"||nodeName=="object")){useIEStrategy=true;}
return useIEStrategy;}
function createFormElementsForIEStrategy(xmlNode){var attr=null;var name="";var value="";for(var x=0;x<xmlNode.attributes.length;x++){attr=xmlNode.attributes[x];name=attr.name.trim();if(name=="name"){value=attr.value.trim();}}
domElemNode=document.createElement("<"+xmlNode.tagName+" name='"+value+"' />");return domElemNode;}
function checkForIEMultipleSelectHack(domNode,domChildNode){if(isIE&&domChildNode.nodeType==1&&domChildNode.tagName.toLowerCase()=="select"&&domChildNode.getAttribute("multiple")!=null){createIEMultipleSelectHack(domNode);}}
function createIEMultipleSelectHack(contextNode){var selectBox=document.createElement("select");selectBox.size=3;for(x=0;x<1;x++){selectBox.options[x]=new Option(x,x);}
selectBox.style.display="none";contextNode.appendChild(selectBox);}
function cleanAttributeName(name){if(isIE==false){return;}
var cleanName=name.toLowerCase();if(cleanName=="cellspacing"){cleanName="cellSpacing";}
else if(cleanName=="cellpadding"){cleanName="cellPadding";}
else if(cleanName=="colspan"){cleanName="colSpan";}
else if(cleanName=="tabindex"){cleanName="tabIndex";}
else if(cleanName=="readonly"){cleanName="readOnly";}
else if(cleanName=="maxlength"){cleanName="maxLength";}
else if(cleanName=="rowspan"){cleanName="rowSpan";}
else if(cleanName=="valign"){cleanName="vAlign";}
return cleanName;}};function DOMSelector(){this.select=function(selector,rootContext){if(!rootContext){rootContext=document;}
selector=selector.replace(/\s*(?=>)/g,'');selector=selector.replace(/>\s*/g,'>');selector=selector.replace(/\s*(?=\+)/g,'');selector=selector.replace(/\+\s*/g,'+');selector=selector.replace(/\s*(?=~)/g,'');selector=selector.replace(/~\s*/g,'~');var splitter=/\s|>|\+|~/g;var combinators=selector.match(splitter);var tokens=selector.split(splitter);var currentContext=new Array(rootContext);var attributesRegexp=/^(\w*)\[(\w+)([=~\|\^\$\*]?)=?"?([^\]"]*)"?\]$/;var pseudoClassesRegexp=/^(\w*)\:(\w+-?\w+)/;var regexpResult=null;for(var i=0;i<tokens.length;i++){var combinator=i==0?" ":combinators[i-1];var token=tokens[i].trim();if(token.indexOf('#')>-1){var tagName=token.substring(0,token.indexOf('#'));var id=token.substring(token.indexOf('#')+1,token.length);var filterFunction=function(e){return(e.id==id);};var found=new Array();for(var h=0;h<currentContext.length;h++){found=selectByFilter(combinator,currentContext[h],tagName,filterFunction);if(found&&found.length==1){break;}}
currentContext=found;continue;}
else if((regexpResult=attributesRegexp.exec(token))){var tagName=regexpResult[1];var attrName=regexpResult[2];var attrOperator=regexpResult[3];var attrValue=regexpResult[4];var filterFunction=null;switch(attrOperator){case'=':filterFunction=function(e){return(e.getAttribute(attrName)&&e.getAttribute(attrName)==attrValue);};break;case'~':filterFunction=function(e){return(e.getAttribute(attrName)&&e.getAttribute(attrName).match(new RegExp('(\\s|^)'+attrValue+'(\\s|$)')));};break;case'|':filterFunction=function(e){return(e.getAttribute(attrName)&&e.getAttribute(attrName).match(new RegExp('^'+attrValue+'-?')));};break;case'^':filterFunction=function(e){return(e.getAttribute(attrName)&&e.getAttribute(attrName).indexOf(attrValue)==0);};break;case'$':filterFunction=function(e){return(e.getAttribute(attrName)&&(e.getAttribute(attrName).lastIndexOf(attrValue)==e.getAttribute(attrName).length-attrValue.length));};break;case'*':filterFunction=function(e){return(e.getAttribute(attrName)&&e.getAttribute(attrName).indexOf(attrValue)>-1);};break;default:filterFunction=function(e){return e.getAttribute(attrName);};}
var found=new Array();var counter=0;for(var h=0;h<currentContext.length;h++){var elements=selectByFilter(combinator,currentContext[h],tagName,filterFunction);for(var j=0;j<elements.length;j++){found[counter++]=elements[j];}}
currentContext=found;continue;}
else if((regexpResult=pseudoClassesRegexp.exec(token))){var tagName=regexpResult[1];var pseudoClass=regexpResult[2];var filterFunction=null;switch(pseudoClass){case'first-child':filterFunction=function(e){var parent=e.parentNode;var i=0;while(parent.childNodes[i]&&parent.childNodes[i].nodeType!=1)i++;return(parent.childNodes[i]==e);};break;case'last-child':filterFunction=function(e){var parent=e.parentNode;var i=parent.childNodes.length-1;while(parent.childNodes[i]&&parent.childNodes[i].nodeType!=1)i--;return(parent.childNodes[i]==e);};break;case'empty':filterFunction=function(e){return(e.childNodes.length==0);};break;default:filterFunction=function(e){return false;};}
var found=new Array();var counter=0;for(var h=0;h<currentContext.length;h++){var elements=selectByFilter(combinator,currentContext[h],tagName,filterFunction);for(var j=0;j<elements.length;j++){found[counter++]=elements[j];}}
currentContext=found;continue;}
else if(token.indexOf('.')>-1){var tagName=token.substring(0,token.indexOf('.'));var className=token.substring(token.indexOf('.')+1,token.length);var regexp=new RegExp('(\\s|^)'+className+'(\\s|$)');var filterFunction=function(e){return(e.className&&e.className.match(regexp));};var found=new Array();var counter=0;for(var h=0;h<currentContext.length;h++){var elements=selectByFilter(combinator,currentContext[h],tagName,filterFunction);for(var j=0;j<elements.length;j++){found[counter++]=elements[j];}}
currentContext=found;continue;}
else{tagName=token;var filterFunction=function(e){return true;};var found=new Array();var counter=0;for(var h=0;h<currentContext.length;h++){var elements=selectByFilter(combinator,currentContext[h],tagName,filterFunction);for(var j=0;j<elements.length;j++){found[counter++]=elements[j];}}
currentContext=found;}}
return currentContext;}
function selectByFilter(combinator,context,tagName,filterFunction){var result=new Array();var elements=new Array();if(combinator==" "){elements=TaconiteDOMUtils.getAllSubElements(context);}else if(combinator==">"){elements=context.childNodes;}else if(combinator=="+"){var sibling=context.nextSibling;while(sibling&&sibling.nodeType!=1){sibling=sibling.nextSibling;}
if(sibling)elements=new Array(sibling);else elements=new Array();}else if(combinator=="~"){var sibling=context.nextSibling;var counter=0;while(sibling){if(sibling.nodeType==1){elements[counter]=sibling;counter++;}
sibling=sibling.nextSibling;}}
var counter=0;if(!tagName||tagName=='*'){for(var k=0;k<elements.length;k++){if(elements[k].nodeType==1&&filterFunction(elements[k])){result[counter]=elements[k];counter++;}}}else{for(var k=0;k<elements.length;k++){if(elements[k].nodeType==1&&elements[k].nodeName.toLowerCase()==tagName.toLowerCase()&&filterFunction(elements[k])){result[counter]=elements[k];counter++;}}}
return result;}};String.prototype.trim=function(){var x=this;x=x.replace(/^\s*(.*)/,"$1");x=x.replace(/(.*?)\s*$/,"$1");return x;};document.getElementsByMatchingId=function(matchingId){var allElements=TaconiteDOMUtils.getAllSubElements(document);var matchingElements=new Array();for(var i=0;i<allElements.length;i++){var currentElement=allElements[i];if(currentElement.nodeType==1){var id=currentElement.getAttribute("id");if(id!=null&&id!=""){if(id.indexOf("_")==(id.length-1)){var pattern="^"+id.replace(/_$/,".*");var rexp=new RegExp(pattern);if(rexp.test(matchingId)){matchingElements.push(currentElement);}}else if(id==matchingId){matchingElements.push(currentElement);}}}}
return matchingElements;};var springxt_core_version=20070526;var XT={ajaxParameter:"ajax-request",eventParameter:"event-id",elementParameter:"source-element",elementIdParameter:"source-element-id",jsonParamsParameter:"json-params",defaultLoadingElementId:null,defaultLoadingImage:null,createSimpleQueryString:function(sourceElement){var qs="";if(sourceElement!=undefined&&sourceElement!=null){if(sourceElement.name!=null){qs="&"+this.elementParameter+"="+sourceElement.name;}
if(sourceElement.id!=null){qs=qs+"&"+this.elementIdParameter+"="+sourceElement.id;}}
return qs;},createJSONQueryString:function(jsonObject){var qs="";if(jsonObject!=undefined&&jsonObject!=null){qs="&"+this.jsonParamsParameter+"="+encodeURIComponent(jsonObject.toJSONString());}
return qs;},configureLoadingInfo:function(loadingInfo,ajaxRequest){if(loadingInfo!=null&&loadingInfo.loadingElementId!=null&&loadingInfo.loadingImage!=null){ajaxRequest.loadingElementId=loadingInfo.loadingElementId;ajaxRequest.loadingImage=loadingInfo.loadingImage;ajaxRequest.setPreRequest(this.showLoadingSign);ajaxRequest.setPostRequest(this.removeLoadingSign);}else if(this.defaultLoadingElementId!=null&&this.defaultLoadingImage!=null){ajaxRequest.loadingElementId=this.defaultLoadingElementId;ajaxRequest.loadingImage=this.defaultLoadingImage;ajaxRequest.setPreRequest(this.showLoadingSign);ajaxRequest.setPostRequest(this.removeLoadingSign);}},showLoadingSign:function(ajaxRequest){var targetEl=document.getElementById(ajaxRequest.loadingElementId);if(targetEl!=null){var img=document.createElement("img");img.setAttribute("src",ajaxRequest.loadingImage);targetEl.appendChild(img);}},removeLoadingSign:function(ajaxRequest){var targetEl=document.getElementById(ajaxRequest.loadingElementId);if(targetEl!=null&&targetEl.childNodes.length>0){targetEl.removeChild(targetEl.childNodes[0]);}},doAjaxAction:function(eventId,sourceElement,jsonObject,loadingInfo){var ajaxRequest=new AjaxRequest(document.URL);ajaxRequest.addFormElements(document.forms[0]);ajaxRequest.setQueryString(ajaxRequest.getQueryString()
+"&"+this.ajaxParameter+"=ajax-action"
+"&"+this.eventParameter+"="+eventId
+this.createSimpleQueryString(sourceElement)
+this.createJSONQueryString(jsonObject));this.configureLoadingInfo(loadingInfo,ajaxRequest);ajaxRequest.sendRequest();},doAjaxSubmit:function(eventId,sourceElement,jsonObject,loadingInfo){var ajaxRequest=new AjaxRequest(document.URL);ajaxRequest.addFormElements(document.forms[0]);ajaxRequest.setQueryString(ajaxRequest.getQueryString()
+"&"+this.ajaxParameter+"=ajax-submit"
+"&"+this.eventParameter+"="+eventId
+this.createSimpleQueryString(sourceElement)
+this.createJSONQueryString(jsonObject));this.configureLoadingInfo(loadingInfo,ajaxRequest);ajaxRequest.setUsePOST();ajaxRequest.sendRequest();}};