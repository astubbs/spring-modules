/**
 @fileoverview
 DOMSelector object implementing CSS Selectors.
 **/

var springxt_domselector_version=20070827;

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
            elements = context.all ? context.all : context.getElementsByTagName('*');
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
