
function doAjaxAction(eventId,sourceElement,jsonObject){var ajaxRequest=new AjaxRequest(document.URL);ajaxRequest.addFormElementsByFormEl(document.forms[0]);ajaxRequest.setQueryString(ajaxRequest.getQueryString()+"&ajax-request=ajax-action"+"&event-id="+eventId+createSimpleQueryString(sourceElement)+createJSONQueryString(jsonObject));ajaxRequest.sendRequest();}
function doAjaxSubmit(eventId,sourceElement,jsonObject){var ajaxRequest=new AjaxRequest(document.URL);ajaxRequest.addFormElementsByFormEl(document.forms[0]);ajaxRequest.setQueryString(ajaxRequest.getQueryString()+"&ajax-request=ajax-submit"+"&event-id="+eventId+createSimpleQueryString(sourceElement)+createJSONQueryString(jsonObject));ajaxRequest.setUsePOST();ajaxRequest.sendRequest();}
function createSimpleQueryString(sourceElement){var qs="";if(sourceElement!=undefined&&sourceElement!=null){if(sourceElement.name!=null&&sourceElement.name!=""){qs=qs+"&source-element="+sourceElement.name;}
if(sourceElement.id!=null&&sourceElement.id!=""){qs=qs+"&source-element-id="+sourceElement.id;}}
return qs;}
function createJSONQueryString(jsonObject){var qs="";if(jsonObject!=undefined&&jsonObject!=null){qs="&json-params="+escape(jsonObject.toJSONString());}
return qs;}
function AjaxRequest(url){var self=this;var xmlHttp=createXMLHttpRequest();var queryString="";var requestURL=url;var method="GET";var preRequest=null;var postRequest=null;var debugResponse=false;var async=true;var errorHandler=null;this.getXMLHttpRequestObject=function(){return xmlHttp;}
this.setPreRequest=function(func){preRequest=func;}
this.setPostRequest=function(func){postRequest=func;}
this.setUsePOST=function(){method="POST";}
this.setUseGET=function(){method="GET";}
this.setEchoDebugInfo=function(){debugResponse=true;}
this.isEchoDebugInfo=function(){return debugResponse;}
this.setQueryString=function(qs){queryString=qs;}
this.getQueryString=function(){return queryString;}
this.setAsync=function(asyncBoolean){async=asyncBoolean;}
this.setErrorHandler=function(func){errorHandler=func;}
this.addFormElements=function(formID){var formElements=document.getElementById(formID).elements;var values=toQueryString(formElements);accumulateQueryString(values);}
this.addFormElementsByFormEl=function(formEl){var formElements=formEl.elements;var values=toQueryString(formElements);accumulateQueryString(values);}
function accumulateQueryString(newValues){if(queryString==""){queryString=newValues;}
else{queryString=queryString+"&"+newValues;}}
this.addNamedFormElementsByFormID=function(){var elementName="";var namedElements=null;for(var i=1;i<arguments.length;i++){elementName=arguments[i];namedElements=document.getElementsByName(elementName);var arNamedElements=new Array();for(j=0;j<namedElements.length;j++){if(namedElements[j].form&&namedElements[j].form.getAttribute("id")==arguments[0]){arNamedElements.push(namedElements[j]);}}
if(arNamedElements.length>0){elementValues=toQueryString(arNamedElements);accumulateQueryString(elementValues);}}}
this.addNamedFormElements=function(){var elementName="";var namedElements=null;for(var i=0;i<arguments.length;i++){elementName=arguments[i];namedElements=document.getElementsByName(elementName);elementValues=toQueryString(namedElements);accumulateQueryString(elementValues);}}
this.addFormElementsById=function(){var id="";var element=null;var elements=new Array();for(var h=0;h<arguments.length;h++){element=document.getElementById(arguments[h]);if(element!=null){elements[h]=element;}}
elementValues=toQueryString(elements);accumulateQueryString(elementValues);}
this.sendRequest=function(){if(preRequest){preRequest(self);}
var obj=this;xmlHttp.onreadystatechange=function(){handleStateChange(self)};if(requestURL.indexOf("?")>0){requestURL=requestURL+"&ts="+new Date().getTime();}
else{requestURL=requestURL+"?ts="+new Date().getTime();}
if(method=="GET"){if(queryString.length>0){requestURL=requestURL+"&"+queryString;}
xmlHttp.open(method,requestURL,true);xmlHttp.send(null);}
else{xmlHttp.open(method,requestURL,true);xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");xmlHttp.send(queryString);}
if(!async){handleStateChange(self);}
if(self.isEchoDebugInfo()){echoRequestParams();}}
handleStateChange=function(ajaxRequest){if(ajaxRequest.getXMLHttpRequestObject().readyState!=4){return;}
if(ajaxRequest.getXMLHttpRequestObject().status==200){var debug=ajaxRequest.isEchoDebugInfo();if(debug){echoResponse(ajaxRequest);}
var nodes=null;if(ajaxRequest.getXMLHttpRequestObject().responseXML!=null){nodes=ajaxRequest.getXMLHttpRequestObject().responseXML.documentElement.childNodes;}
else{nodes=new Array();}
var parser=null;var parseInBrowser="";for(var i=0;i<nodes.length;i++){if(nodes[i].nodeType!=1||!isTaconiteTag(nodes[i])){continue;}
parseInBrowser=nodes[i].getAttribute("parseInBrowser");if(parseInBrowser=="true"){parser=new XhtmlToDOMParser(nodes[i]);parser.startParsing();var js=parser.getJavaScript();if(debug){echoParsedJavaScript(js);}
eval(parser.getJavaScript());}
else{eval(nodes[i].firstChild.nodeValue);}}
if(postRequest){postRequest(ajaxRequest);}}
else{if(errorHandler){errorHandler(self);}}}
function isTaconiteTag(node){return node.tagName.substring(0,9)=="taconite-";}
function toQueryString(elements){var node=null;var qs="";var name="";var tempString="";for(var i=0;i<elements.length;i++){tempString="";node=elements[i];name=node.getAttribute("name");if(!name){name=node.getAttribute("id");}
if(node.tagName.toLowerCase()=="input"){if(node.type.toLowerCase()=="radio"||node.type.toLowerCase()=="checkbox"){if(node.checked){tempString=name+"="+node.value;}}
if(node.type.toLowerCase()=="text"||node.type.toLowerCase()=="hidden"){tempString=name+"="+encodeURIComponent(node.value);}}
else if(node.tagName.toLowerCase()=="select"){tempString=getSelectedOptions(node);}
else if(node.tagName.toLowerCase()=="textarea"){tempString=name+"="+encodeURIComponent(node.value);}
if(tempString!=""){if(qs==""){qs=tempString;}
else{qs=qs+"&"+tempString;}}}
return qs;}
function getSelectedOptions(select){var options=select.options;var option=null;var qs="";var tempString="";for(var x=0;x<options.length;x++){tempString="";option=options[x];if(option.selected){tempString=select.name+"="+option.value;}
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
function createDebugTextBox(label,id){textBox=document.createElement("input");textBox.setAttribute("type","text");textBox.setAttribute("id",id);textBox.setAttribute("style","width:100%");textBox.style.cssText="width:100%";document.getElementsByTagName("body")[0].appendChild(document.createTextNode(label));document.getElementsByTagName("body")[0].appendChild(textBox);return textBox;}}
function createXMLHttpRequest(){var req=false;if(window.XMLHttpRequest){req=new XMLHttpRequest();}
else if(window.ActiveXObject){try{req=new ActiveXObject("Msxml2.XMLHTTP");}
catch(e){try{req=new ActiveXObject("Microsoft.XMLHTTP");}
catch(e){req=false;}}}
return req;}
var isIE=document.uniqueID;String.prototype.trim=function(){var x=this;x=x.replace(/^\s*(.*)/,"$1");x=x.replace(/(.*?)\s*$/,"$1");return x;};function XhtmlToDOMParser(xml){var xmlTagName=xml.tagName.toLowerCase();var contextNode=document.getElementById(xml.getAttribute("contextNodeID"));this.startParsing=function(){switch(xmlTagName){case"taconite-append-as-children":getReplaceChildren(contextNode,xml,false);break;case"taconite-delete":getDelete(contextNode,xml);break;case"taconite-append-as-first-child":getAppendAsFirstChild(contextNode,xml);break;case"taconite-insert-after":getInsertAfter(contextNode,xml);break;case"taconite-insert-before":getInsertBefore(contextNode,xml);break;case"taconite-replace-children":getReplaceChildren(contextNode,xml,true);break;case"taconite-replace":getReplace(contextNode,xml);break;case"taconite-set-attributes":xml.removeAttribute("contextNodeID");xml.removeAttribute("parseInBrowser");handleAttributes(contextNode,xml);break;case"taconite-redirect":handleRedirect(xml);break;case"taconite-execute-javascript":executeJavascript(xml);break;}};function isInlineMode(node){var attrType;if(!node.tagName.toLowerCase()=="input"){return false;}
attrType=node.getAttribute("type");if(attrType=="radio"||attrType=="checkbox"){return true;}
return false;}
this.getJavaScript=function(){return"var dummy_taconite_variable=0";};function handleNode(xmlNode){var nodeType=xmlNode.nodeType;switch(nodeType){case 1:return handleElement(xmlNode);case 3:case 4:return document.createTextNode(xmlNode.nodeValue);}
return null;}
function handleElement(xmlNode){var domElemNode=null;var xmlNodeTagName=xmlNode.tagName.toLowerCase();if(isIE){if(isInlineMode(xmlNode)){return document.createElement("<INPUT "+handleAttributes(domElemNode,xmlNode,true)+">");}
if(xmlNodeTagName=="style"){var text,rulesArray,styleSheetPtr;var regExp=/\s+/g;text=xmlNode.text.replace(regExp," ");rulesArray=text.split("}");domElemNode=document.createElement("style");styleSheetPtr=document.styleSheets[document.styleSheets.length-1];for(var i=0;i<rulesArray.length;i++){rulesArray[i]=rulesArray[i].trim();var rulePart=rulesArray[i].split("{");if(rulePart.length==2){styleSheetPtr.addRule(rulePart[0],rulePart[1],-1);}}
return domElemNode;}}
if(domElemNode==null){domElemNode=document.createElement(xmlNodeTagName);handleAttributes(domElemNode,xmlNode);if(isIE&&!domElemNode.canHaveChildren){if(xmlNode.childNodes.length>0){domElemNode.text=xmlNode.text;}}
else{for(var z=0;z<xmlNode.childNodes.length;z++){var domChildNode=handleNode(xmlNode.childNodes[z]);if(domChildNode!=null){domElemNode.appendChild(domChildNode);}}}}
return domElemNode;}
function handleAttributes(domNode,xmlNode){var attr=null;var attrString="";var name="";var value="";var returnAsText=false;if(arguments.length==3){returnAsText=true;}
for(var x=0;x<xmlNode.attributes.length;x++){attr=xmlNode.attributes[x];name=attr.name.trim();value=attr.value.trim();if(!returnAsText){if(name=="style"){domNode.style.cssText=value;domNode.setAttribute(name,value);}
else if(name.trim().toLowerCase().substring(0,2)=="on"){eval("domNode."+name.trim().toLowerCase()+"=function(){"+value+"}");}
else{eval("domNode."+name.trim().toLowerCase()+"='"+value+"'");}
if(name=="class"){domNode.setAttribute("className",value);}}else{attrString=attrString+name+"=\""+value+"\" ";}}
return attrString;}
function getAppendAsFirstChild(domNode,xml){var firstNode=null;if(domNode.childNodes.length>0){firstNode=domNode.childNodes[0];}
for(var i=0;i<xml.childNodes.length;i++){domChildNode=handleNode(xml.childNodes[i]);if(domChildNode!=null){if(firstNode==null){domNode.appendChild(domChildNode);firstNode=domChildNode;}
else{domNode.insertBefore(domChildNode,firstNode);}}}}
function getInsertAfter(domNode,xml){var domChildNode=null;var nextSibling=domNode.nextSibling;for(var i=0;i<xml.childNodes.length;i++){domChildNode=handleNode(xml.childNodes[i]);if(domChildNode!=null){if(nextSibling!=null){domNode.parentNode.insertBefore(domChildNode,nextSibling);}
else{domNode.parentNode.appendChild(domChildNode);}}}}
function getInsertBefore(domNode,xml){var domChildNode=null;for(var i=0;i<xml.childNodes.length;i++){domChildNode=handleNode(xml.childNodes[i]);if(domChildNode!=null){domNode.parentNode.insertBefore(domChildNode,domNode);}}}
function getReplace(domNode,xml){getInsertAfter(domNode,xml);domNode.parentNode.removeChild(domNode);}
function getDelete(domNode){domNode.parentNode.removeChild(domNode);}
function getReplaceChildren(domNode,xml,doRemoveChildren){var domChildNode=null;if(doRemoveChildren){while(contextNode.childNodes.length>0){contextNode.removeChild(contextNode.childNodes[0]);}}
for(var i=0;i<xml.childNodes.length;i++){domChildNode=handleNode(xml.childNodes[i]);if(domChildNode!=null){domNode.appendChild(domChildNode);}}}
function handleRedirect(xmlNode){var targetUrl=xmlNode.getAttribute("targetUrl");window.location.replace(targetUrl);}
function executeJavascript(xmlNode){var scripts=xmlNode.getElementsByTagName("script");for(var i=0;i<scripts.length;i++){var script=scripts[i];if(script.getAttribute("type")=="text/javascript"){var js=script.firstChild.nodeValue;eval(js);}}}}
(function(){var m={'\b':'\\b','\t':'\\t','\n':'\\n','\f':'\\f','\r':'\\r','"':'\\"','\\':'\\\\'},s={array:function(x){var a=['['],b,f,i,l=x.length,v;for(i=0;i<l;i+=1){v=x[i];f=s[typeof v];if(f){v=f(v);if(typeof v=='string'){if(b){a[a.length]=',';}
a[a.length]=v;b=true;}}}
a[a.length]=']';return a.join('');},'boolean':function(x){return String(x);},'null':function(x){return"null";},number:function(x){return isFinite(x)?String(x):'null';},object:function(x){if(x){if(x instanceof Array){return s.array(x);}
var a=['{'],b,f,i,v;for(i in x){v=x[i];f=s[typeof v];if(f){v=f(v);if(typeof v=='string'){if(b){a[a.length]=',';}
a.push(s.string(i),':',v);b=true;}}}
a[a.length]='}';return a.join('');}
return'null';},string:function(x){if(/["\\\x00-\x1f]/.test(x)){x=x.replace(/([\x00-\x1f\\"])/g,function(a,b){var c=m[b];if(c){return c;}
c=b.charCodeAt();return'\\u00'+
Math.floor(c/16).toString(16)+
(c%16).toString(16);});}
return'"'+x+'"';}};Object.prototype.toJSONString=function(){return s.object(this);};Array.prototype.toJSONString=function(){return s.array(this);};})();String.prototype.parseJSON=function(){try{return!(/[^,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t]/.test(this.replace(/"(\\.|[^"\\])*"/g,'')))&&eval('('+this+')');}catch(e){return false;}};