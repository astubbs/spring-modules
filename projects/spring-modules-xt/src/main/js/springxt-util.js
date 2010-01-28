XT.util = {};


XT.util.DOMSelector = function() {
    
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
    };
    
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
    };
};

//--- JSON stuff ---/

if (!this.JSON) {
    
    JSON = function () {
        
        function f(n) {    // Format integers to have at least two digits.
            return n < 10 ? '0' + n : n;
        }
        
        Date.prototype.toJSON = function () {
            
            // Eventually, this method will be based on the date.toISOString method.
            
            return this.getUTCFullYear()   + '-' +
            f(this.getUTCMonth() + 1) + '-' +
            f(this.getUTCDate())      + 'T' +
            f(this.getUTCHours())     + ':' +
            f(this.getUTCMinutes())   + ':' +
            f(this.getUTCSeconds())   + 'Z';
        };
        
        
        var m = {    // table of character substitutions
            '\b': '\\b',
            '\t': '\\t',
            '\n': '\\n',
            '\f': '\\f',
            '\r': '\\r',
            '"' : '\\"',
            '\\': '\\\\'
        };
        
        function stringify(value, whitelist) {
            var a,          // The array holding the partial texts.
            i,          // The loop counter.
            k,          // The member key.
            l,          // Length.
            r = /["\\\x00-\x1f\x7f-\x9f]/g,
            v;          // The member value.
            
            switch (typeof value) {
                case 'string':
                    
                    // If the string contains no control characters, no quote characters, and no
                    // backslash characters, then we can safely slap some quotes around it.
                    // Otherwise we must also replace the offending characters with safe sequences.
                    
                    return r.test(value) ?
                        '"' + value.replace(r, function (a) {
                            var c = m[a];
                            if (c) {
                                return c;
                            }
                            c = a.charCodeAt();
                            return '\\u00' + Math.floor(c / 16).toString(16) +
                            (c % 16).toString(16);
                    }) + '"' :
                        '"' + value + '"';
                        
                case 'number':
                    
                    // JSON numbers must be finite. Encode non-finite numbers as null.
                    
                    return isFinite(value) ? String(value) : 'null';
                    
                case 'boolean':
                case 'null':
                    return String(value);
                    
                case 'object':
                    
                    // Due to a specification blunder in ECMAScript,
                    // typeof null is 'object', so watch out for that case.
                    
                    if (!value) {
                        return 'null';
                    }
                    
                    // If the object has a toJSON method, call it, and stringify the result.
                    
                    if (typeof value.toJSON === 'function') {
                        return stringify(value.toJSON());
                    }
                    a = [];
                    if (typeof value.length === 'number' &&
                    !(value.propertyIsEnumerable('length'))) {
                        
                        // The object is an array. Stringify every element. Use null as a placeholder
                        // for non-JSON values.
                        
                        l = value.length;
                        for (i = 0; i < l; i += 1) {
                            a.push(stringify(value[i], whitelist) || 'null');
                        }
                        
                        // Join all of the elements together and wrap them in brackets.
                        
                        return '[' + a.join(',') + ']';
                    }
                    if (whitelist) {
                        
                        // If a whitelist (array of keys) is provided, use it to select the components
                        // of the object.
                        
                        l = whitelist.length;
                        for (i = 0; i < l; i += 1) {
                            k = whitelist[i];
                            if (typeof k === 'string') {
                                v = stringify(value[k], whitelist);
                                if (v) {
                                    a.push(stringify(k) + ':' + v);
                                }
                            }
                        }
                    } else {
                        
                        // Otherwise, iterate through all of the keys in the object.
                        
                        for (k in value) {
                            if (typeof k === 'string') {
                                v = stringify(value[k], whitelist);
                                if (v) {
                                    a.push(stringify(k) + ':' + v);
                                }
                            }
                        }
                    }
                    
                    // Join all of the member texts together and wrap them in braces.
                    
                    return '{' + a.join(',') + '}';
            }
        }
        
        return {
            stringify: stringify,
            parse: function (text, filter) {
                var j;
                
                function walk(k, v) {
                    var i, n;
                    if (v && typeof v === 'object') {
                        for (i in v) {
                            if (Object.prototype.hasOwnProperty.apply(v, [i])) {
                                n = walk(i, v[i]);
                                if (n !== undefined) {
                                    v[i] = n;
                                }
                            }
                        }
                    }
                    return filter(k, v);
                }
                
                
                // Parsing happens in three stages. In the first stage, we run the text against
                // regular expressions that look for non-JSON patterns. We are especially
                // concerned with '()' and 'new' because they can cause invocation, and '='
                // because it can cause mutation. But just to be safe, we want to reject all
                // unexpected forms.
                
                // We split the first stage into 4 regexp operations in order to work around
                // crippling inefficiencies in IE's and Safari's regexp engines. First we
                // replace all backslash pairs with '@' (a non-JSON character). Second, we
                // replace all simple value tokens with ']' characters. Third, we delete all
                // open brackets that follow a colon or comma or that begin the text. Finally,
                // we look to see that the remaining characters are only whitespace or ']' or
                // ',' or ':' or '{' or '}'. If that is so, then the text is safe for eval.
                
                if (/^[\],:{}\s]*$/.test(text.replace(/\\./g, '@').
                replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(:?[eE][+\-]?\d+)?/g, ']').
                replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {
                    
                    // In the second stage we use the eval function to compile the text into a
                    // JavaScript structure. The '{' operator is subject to a syntactic ambiguity
                    // in JavaScript: it can begin a block or an object literal. We wrap the text
                    // in parens to eliminate the ambiguity.
                    
                    j = eval('(' + text + ')');
                    
                    // In the optional third stage, we recursively walk the new structure, passing
                    // each name/value pair to a filter function for possible transformation.
                    
                    return typeof filter === 'function' ? walk('', j) : j;
                }
                
                // If the text is not JSON parseable, then a SyntaxError is thrown.
                
                throw new SyntaxError('parseJSON');
            }
        };
    }();
}

//--- Extensions ---//

String.prototype.trim = function() {
    //skip leading and trailing whitespace
    //and return everything in between
    var x=this;
    x=x.replace(/^\s*(.*)/, "$1");
    x=x.replace(/(.*?)\s*$/, "$1");
    return x;
};


document.getElementsByMatchingId = function(matchingId) {
    var allElements = document.all ? document.all : document.getElementsByTagName('*');
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
