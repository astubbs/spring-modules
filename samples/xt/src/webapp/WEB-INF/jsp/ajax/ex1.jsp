<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <script type="text/javascript" src='../js/springxt.js'></script>
        <script type="text/javascript" src='../js/prototype.js'></script>
        <script type="text/javascript" src='../js/scriptaculous.js?load=effects,dragdrop'></script>
        <script type="text/javascript">
            function showAlert(options) {
                alert(options.message);
            }
            
            function showAlertOnDrop(draggable, droppable) {
                alert(draggable.id + " on " + droppable.id);
            }
        </script>
        <title>XT Ajax Framework : Example 1</title>
        <link href='<c:url value="/springxt.css"/>' rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="titleBar"></div>
        <h1>XT Ajax Framework</h1>
        <h3 align="center">Test page updating actions ...</h3>
        <p>
            This example gives you a taste of ajax updating actions.
        </p>
        <form name="" action="">
            <table>
                
                <tr>
                    <td>Press to append a random number :</td>
                    <td><input type="button" value="Press" onclick="XT.doAjaxAction('appendNumber', this, null, {'loadingElementId' : 'loading1', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><div id="loading1"></div></td>
                </tr>
                <tr>
                    <td id="num" colspan="2"></td>
                </tr>
                
                <tr>
                    <td>Press to replace all numbers above with a single number :</td>
                    <td><input type="button" value="Press" onclick="XT.doAjaxAction('replaceNumbers', this, null, {'loadingElementId' : 'loading2', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><div id="loading2"></div></td>
                </tr>
                
                <tr>
                    <td>Press to remove all numbers above :</td>
                    <td><input type="button" value="Press" onclick="XT.doAjaxAction('removeNumbers', this, null, {'loadingElementId' : 'loading3', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><div id="loading3"></div></td>
                </tr>
                
                <tr>
                    <td>Press to replace the text below, sorrounded by a <i>span</i> element, with an input field :</td>
                    <td><input type="button" value="Press" onclick="XT.doAjaxAction('replaceElement', this, null, {'loadingElementId' : 'loading4', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><div id="loading4"></div></td>
                </tr>
                <tr>
                    <td colspan="2"><span id="toReplace" style="color: red;">Text to be replaced</span></td>
                </tr>
                
                <tr>
                    <td>Press to remove the text below, sorrouned by a <i>span</i> element :</td>
                    <td><input type="button" value="Press" onclick="XT.doAjaxAction('removeElement', this, null, {'loadingElementId' : 'loading5', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><div id="loading5"></div></td>
                </tr>
                <tr>
                    <td colspan="2"><span id="toRemove" style="color: red;">Text to be removed</span></td>
                </tr>
                
                <tr>
                    <td>Press to complete the greetings message below :</td>
                    <td><input id="insertAfterButton" type="button" value="Press" onclick="XT.doAjaxAction('insertAfter', this, null, {'loadingElementId' : 'loading6', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><div id="loading6"></div></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <span id="toInsertAfter">Hello</span>
                    </td>
                </tr>
                
                <tr>
                    <td>Press to complete the greetings message below :</td>
                    <td><input id="insertBeforeButton" type="button" value="Press" onclick="XT.doAjaxAction('insertBefore', this, null, {'loadingElementId' : 'loading7', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><div id="loading7"></div></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <span id="toInsertBefore">Spring Modules user</span>
                    </td>
                </tr>
                
                <tr>
                    <td>Press to complete the greetings message below :</td>
                    <td><input id="appendAsFirstButton" type="button" value="Press" onclick="XT.doAjaxAction('appendAsFirst', this, null, {'loadingElementId' : 'loading8', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><div id="loading8"></div></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div id="toAppendAsFirst">
                            <span>Spring Modules user</span>
                        </div>
                    </td>
                </tr>
                
                <tr>
                    <td>Press to include external dynamic content through a Jsp:</td>
                    <td><input id="includeJsp" type="button" value="Press" onclick="XT.doAjaxAction('includeJsp', this, null, {'loadingElementId' : 'loading9', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><div id="loading9"></div></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div id="jsp">
                        </div>
                    </td>
                </tr>
                
                <tr>
                    <td>Press to show an alert message (by executing a javascript function) :</td>
                    <td><input type="button" value="Press" onclick="XT.doAjaxAction('executeFunction', this, null, {'loadingElementId' : 'loading10', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><div id="loading10"></div></td>
                </tr>
                
                <tr>
                    <td>Press to show a greetings message (hidden using a CSS inline rule) :</td>
                    <td><input type="button" value="Press" onclick="XT.doAjaxAction('showElement', this, null, {'loadingElementId' : 'loading11', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><div id="loading11"></div></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div id="toShow" style="display: none;">
                            <div style="color: red;">Your greetings ...</div>
                            <div style="color: red;">Hello, Spring Modules user!</div>
                        </div>
                    </td>
                </tr>
                
                <tr>
                    <td>Press to hide the greetings message below :</td>
                    <td><input type="button" value="Press" onclick="XT.doAjaxAction('hideElement', this, null, {'loadingElementId' : 'loading12', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><div id="loading12"></div></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div id="toHide">
                            <div style="color: red;">Your greetings to hide ...</div>
                            <div style="color: red;">Hello, Spring Modules user!</div>
                        </div>
                    </td>
                </tr>
                
                <tr>
                    <td>Press the corresponding button to apply an effect to the greetings message below :</td>
                    <td><input type="button" value="Highlight" onclick="XT.doAjaxAction('highlightElement', this, null, {'loadingElementId' : 'loading13', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><input type="button" value="Puff" onclick="XT.doAjaxAction('puffElement', this, null, {'loadingElementId' : 'loading13', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><input type="button" value="Pulsate" onclick="XT.doAjaxAction('pulsateElement', this, null, {'loadingElementId' : 'loading13', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><input type="button" value="Shrink" onclick="XT.doAjaxAction('shrinkElement', this, null, {'loadingElementId' : 'loading13', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><input type="button" value="Grow" onclick="XT.doAjaxAction('growElement', this, null, {'loadingElementId' : 'loading13', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><div id="loading13"></div></td>
                </tr>
                <tr>
                    <td colspan="6">
                        <div id="toApplyEffect" style="border-style: solid">
                            <div>Your greetings ...</div>
                            <div>Hello, Spring Modules user!</div>
                        </div>
                    </td>
                </tr>
                
                <tr>
                    <td>Press the button to enable drag and drop below :</td>
                    <td><input type="button" value="Enable" onclick="XT.doAjaxAction('enableDnD', this, null, {'loadingElementId' : 'loading14', 'loadingImage' : '../images/loading.gif'});"></td>
                    <td><div id="loading14"></div></td>
                </tr>
                <tr>
                    <td>
                        <div id="draggable">
                            Drag This
                        </div>
                    </td>
                    <td id="droppable">
                        <div style="border-style: solid">
                            Here
                        </div>
                    </td>
                </tr>
                
            </table>
        </form>
    </body>
</html>
