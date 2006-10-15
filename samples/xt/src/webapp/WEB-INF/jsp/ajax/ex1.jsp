<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <script type="text/javascript" src='<c:url value="/js/springxt-min.js"/>'></script>
        <script type="text/javascript" src='<c:url value="/js/prototype.js"/>'></script>
        <script type="text/javascript" src='<c:url value="/js/scriptaculous.js?load=effects"/>'></script>
        <script type="text/javascript">
            function showAlert(options) {
            alert(options.message);
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
                    <td><input type="button" value="Press" onclick="doAjaxAction('appendNumber', this);"></td>
                </tr>
                <tr>
                    <td id="num" colspan="2"></td>
                </tr>
                
                <tr>
                    <td>Press to replace all numbers above with a single number :</td>
                    <td><input type="button" value="Press" onclick="doAjaxAction('replaceNumbers', this);"></td>
                </tr>
                
                <tr>
                    <td>Press to remove all numbers above :</td>
                    <td><input type="button" value="Press" onclick="doAjaxAction('removeNumbers', this);"></td>
                </tr>
                
                <tr>
                    <td>Press to replace the text below, sorrounded by a <i>span</i> element, with an input field :</td>
                    <td><input type="button" value="Press" onclick="doAjaxAction('replaceElement', this);"></td>
                </tr>
                <tr>
                    <td colspan="2"><span id="toReplace" style="color: red;">Text to be replaced</span></td>
                </tr>
                
                <tr>
                    <td>Press to remove the text below, sorrouned by a <i>span</i> element :</td>
                    <td><input type="button" value="Press" onclick="doAjaxAction('removeElement', this);"></td>
                </tr>
                <tr>
                    <td colspan="2"><span id="toRemove" style="color: red;">Text to be removed</span></td>
                </tr>
                
                <tr>
                    <td>Press to complete the greetings message below :</td>
                    <td><input id="insertAfterButton" type="button" value="Press" onclick="doAjaxAction('insertAfter', this);"></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <span id="toInsertAfter">Hello</span>
                    </td>
                </tr>
                
                <tr>
                    <td>Press to complete the greetings message below :</td>
                    <td><input id="insertBeforeButton" type="button" value="Press" onclick="doAjaxAction('insertBefore', this);"></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <span id="toInsertBefore">Spring Modules user</span>
                    </td>
                </tr>
                
                <tr>
                    <td>Press to complete the greetings message below :</td>
                    <td><input id="appendAsFirstButton" type="button" value="Press" onclick="doAjaxAction('appendAsFirst', this);"></td>
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
                    <td><input id="includeJsp" type="button" value="Press" onclick="doAjaxAction('includeJsp', this);"></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div id="jsp">
                        </div>
                    </td>
                </tr>
                
                <tr>
                    <td>Press to show an alert message (by executing a javascript function) :</td>
                    <td><input type="button" value="Press" onclick="doAjaxAction('executeFunction', this);"></td>
                </tr>
                
                <tr>
                    <td>Press to show a greetings message (hidden using a CSS inline rule) :</td>
                    <td><input type="button" value="Press" onclick="doAjaxAction('showElement', this);"></td>
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
                    <td><input type="button" value="Press" onclick="doAjaxAction('hideElement', this);"></td>
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
                    <td><input type="button" value="Highlight" onclick="doAjaxAction('highlightElement', this);"></td>
                    <td><input type="button" value="Set Opacity" onclick="doAjaxAction('setOpacity', this);"></td>
                    <td><input type="button" value="Pulsate" onclick="doAjaxAction('pulsateElement', this);"></td>
                    <td><input type="button" value="Shrink" onclick="doAjaxAction('shrinkElement', this);"></td>
                    <td><input type="button" value="Grow" onclick="doAjaxAction('growElement', this);"></td>
                </tr>
                <tr>
                    <td colspan="6">
                        <div id="toApplyEffect" style="border-style: solid">
                            <div>Your greetings ...</div>
                            <div>Hello, Spring Modules user!</div>
                        </div>
                    </td>
                </tr>
                
            </table>
        </form>
    </body>
</html>