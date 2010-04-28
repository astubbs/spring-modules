/*
 * Valang codebase for client side validation
 * 
 * Copyright 2004-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Oliver Hutchison
 * @author Toolman
 */


/******************************************************************************
* 
* Core validation object.
* 
******************************************************************************/
var ValangValidator = function(name, installSelfWithForm, rules, validateOnSubmit) {
    this.name = name;
    this.form = this._findForm(name);
    this.groupedRules = {};
    var thisValidator = this;
    
    this.eventHandler = function(e) {
		ValangValidator.Logger.push('recieved ' + e.type);

		var delay = 5; //yield time
		
		//don't fire for some key presses:
		if(e.type == "keyup" || e.type == "keydown" ){
			var key = null;
    		if (undefined === e.which) {
    			key = e.keyCode; 
    		} else if (e.which != 0) {
    			key = e.keyCode;
    		} else { 
    			ValangValidator.Logger.log('special key' );
    			return true; //special key, ignore
    		}							
			if(key == 9 ){ //ignore tabkey; onblur will catch
				ValangValidator.Logger.push('tab pressed' );
				return true;
			}
			delay = 250;
		}
		if(thisValidator.doValidation) clearTimeout(thisValidator.doValidation);

		var self = this;
		var doValidate = function() { 
			// check this field
			thisValidator.validateField(new ValangValidator.Field(self));
		}
		
			thisValidator.doValidation = setTimeout(function(){doValidate();}, delay);
    };
    
    this.addRules(rules, false);
    
    if (installSelfWithForm) {
        this._installSelfWithForm(validateOnSubmit);
    }
};

ValangValidator.prototype = {
	globalErrorsId: 'global_errors',
	fieldErrorIdSuffix: '_error',
	emptyErrorHTML: '',
    classRuleFieldName: "valang-global-rules", // to match CommandObjectToValangConverter.CLASS_RULENAME
    
    addRules: function(newRules, addFirst){ 
	    //create grouped rules by field
	    for (var i = 0; i < newRules.length; i++) {
	    	var ruleField = newRules[i].field;
	    	var fieldArray = this.groupedRules[ruleField];
	    	
	    	if(!fieldArray) {
	    		this.groupedRules[ruleField] = new Array();
	    		fieldArray = this.groupedRules[ruleField];
	    	}

    		fieldArray[addFirst ? "unshift":"push"](newRules[i]);
    		
	    	var fields = this.form.getFieldsWithName(ruleField);
	    	for(field in fields){
	    		this.addEventHandler(fields[field]);
	    	}

	    }	
	},
    
    // check all fields and global checks, returns boolean form valid state 
    validate: function() { 
    	var isValid = true;
        for(i in this.groupedRules) {
        	var rules = this.groupedRules[i];
        	if(rules && rules.length > 0) {
        		var thisFields = this.form.getFieldsWithName(rules[0].field);
        		for (var j = 0; j < thisFields.length; j++) {
        			isValid = isValid && this.validateField(thisFields[j]);
        		}
        	}
        }
        ValangValidator.Logger.log('Is form valid? ' + isValid);
        return isValid;
    },

	
    //checks all rules on a field, updates error message and fires callback
    validateField: function(field) {
        ValangValidator.Logger.push('Evaluating ' + field.name + ' rules');

        this._clearErrorIfExists(field.name);
        var isValid = true;
        var fieldRules = this.groupedRules[field.name];
        var errorRules = new Array();
        var ruleCount = fieldRules ? fieldRules.length : 0;
        
        if(fieldRules && ruleCount > 0){

	        for (var i = 0; i < ruleCount; i++) {
	            var rule = fieldRules[i];
	            rule.form = this.form;
	            var thisValid = false;
	            try {
	            	thisValid =  !!rule.validate();
	            } catch(err) {
	            	ValangValidator.Logger.log("Rule error: " + err);
	            }
	            isValid = isValid && thisValid;
	            if(!thisValid) {
	            	errorRules.push(rule);
	            }
	            ValangValidator.Logger.log(thisValid ? 'Passed' : 'Failed');
	        }
	
	        var errorBox = document.getElementById(field.name + this.fieldErrorIdSuffix);
	        if( errorRules.length > 0) {
	        	//inject first error only
	        	if( errorBox != null) {
	        		this._insertText(errorBox, errorRules[0].getErrorMessage());
	        	} else {
		        	alert(field.name + " : " + errorRules[0].getErrorMessage());
		        	ValangValidator.Logger.log("error node not found.");
	        	}
	        }
	        
        } else {
        	ValangValidator.Logger.pop('No rules');
        }
        
		if(this.fieldValidationCallback) {
        	ValangValidator.Logger.log("Field callback.");
			this.fieldValidationCallback(field.fieldElement, isValid, ruleCount);
		}
        
        ValangValidator.Logger.pop('Finished field rules evaluation');
        return isValid;
    },    
    
    _installSelfWithForm: function(validateOnSubmit) {
        var thisValidator = this;

        var onloadHandler = function() {
        
    		if(validateOnSubmit) {
        		ValangValidator.Logger.log('Installing ValangValidator \'' 
        				+ thisValidator.name + '\' as onsubmit handler');

        		var onSubmitHandler = function(e) {
        			var isValid = thisValidator.validate();
        			if(thisValidator.formValidationCallback) {
        				isValid = thisValidator.formValidationCallback(this, isValid);
        			}
        			
        			return isValid; // callback can allow "true" even with invalid items
        		};
        		
        		thisValidator.addEvent(thisValidator.form.formElement, "submit", onSubmitHandler);
        		
    		} else {
        		ValangValidator.Logger.log('No onSubmit handler'); 
        	}
        }
    	
		//bind event to handler
        thisValidator.addEvent(window, "load", onloadHandler);

    },
	
    //validateEvents: ["keyup", "change", "blur"],
    validateEvents: ["change", "blur"],
    selectValidateEvents: ["change", "blur"],
    radiocheckValidateEvents: ["keyup", "click", "change", "blur"],
    
    addEventHandler: function(field) {
    	
    	var theseEvents = this.validateEvents; //text based inputs
    	if(field.tagName == "select") theseEvents = this.selectValidateEvents; 
    	else if(field.type == "radio") theseEvents = this.radiocheckValidateEvents; 
    	else if(field.type == "checkbox") theseEvents = this.radiocheckValidateEvents; 
    	
		for (evtPtr in theseEvents) {
			var event = theseEvents[evtPtr];
			this.removeEvent(field.fieldElement, event, this.eventHandler); //ensure only 1 instance mounted
			this.addEvent(field.fieldElement, event, this.eventHandler);
		}
		
	},    
    
    // add/remove from http://ejohn.org/blog/flexible-javascript-events/ thanks
    addEvent: function( obj, type, fn ) {
      if ( obj.attachEvent ) {
        obj['e'+type+fn] = fn;
        obj[type+fn] = function(){obj['e'+type+fn]( window.event );}
        obj.attachEvent( 'on'+type, obj[type+fn] );
      } else
        obj.addEventListener( type, fn, false );
    },
    
    removeEvent: function( obj, type, fn ) {
      if ( obj.detachEvent ) {
        if(obj[type+fn]) obj.detachEvent( 'on'+type, obj[type+fn] );
        obj[type+fn] = null;
      } else
        obj.removeEventListener( type, fn, false );
    },

    _findForm: function(name) {
        var element = document.getElementById(name);
        if (!element || element.tagName.toLowerCase() != 'form') {
            element = document.getElementById(name + 'ValangValidator');
	        if (!element || element.tagName.toLowerCase() != 'script') {
	        	throw 'unable to find form with ID \'' + name + '\' or script element with ID \'' 
	        		+ name + 'ValangValidator\'';
	        }
        }
        var foundElement = element;
        while (element && element.tagName.toLowerCase() != 'form') {
            element = element.parentNode;
        }
        if (!element) {
            throw 'unable to find FORM element enclosing element with ID \'' + foundElement.id + '\'';
        }
        return new ValangValidator.Form(element);
    },
    
    _clearErrorIfExists: function(field) {
        var errorBox = document.getElementById(field + this.fieldErrorIdSuffix);
        if (errorBox != null) {
        	this._insertText(errorBox, this.emptyErrorHTML);
        }
    },
    
    _clearGlobalErrors: function() {
        var errorBox = document.getElementById(this.globalErrorsId);
        if (errorBox != null) {
        	this._insertText(errorBox, this.emptyErrorHTML);
        }
    },
    _insertText: function(node, text) {
    	if(node) {
            while (node.childNodes[0]) {
                node.removeChild(node.childNodes[0]);
            }
    		var textNode = document.createTextNode(text);
    		node.appendChild(textNode);
    		/*
    		 this technique is used, as node.innerHTML has some iE6 effects.
    		*/
    	}
    },

    
    // stubs
    fieldValidationCallback: function(field, isValid, ruleCount) {
        ValangValidator.Logger.log('Stub callback: ' + field + " : " + isValid + " : " + ruleCount);
    },
    
    formValidationCallback: function(form, isValid) {
        ValangValidator.Logger.log('Stub callback: ' + form + " : " + isValid);
        return isValid;
    }
};



/******************************************************************************
 * 
 * Encapsulates the HTML form
 *
 * Based on code from http://prototype.conio.net/
 * 
 ******************************************************************************/

ValangValidator.Form = function(formElement) {
    this.formElement = formElement;
};

ValangValidator.Form.prototype = {
    getValue: function(fieldName) {
        var fields = this.getFieldsWithName(fieldName);
        var value = new Array();
        for (var i = 0; i < fields.length; i++) {
            if (fields[i].getValue()) {
                value.push(fields[i].getValue());
            }
        }
        if (value.length == 1) {
            return value[0];
        } else if (value.length > 1) {
            return value;
        }
    },
    getFieldsWithName: function(fieldName) {
        var matchingFields = new Array();
        var fields = this.getFields();
        for (var i = 0; i < fields.length; i++) {
            var field = fields[i];
            if (field.name == fieldName) {
                matchingFields.push(field);
            }
        }
        return matchingFields;
    },
    getFields: function() {
        var fields = new Array();
        var tagElements = this.formElement.elements;
        for (var i = 0; i < tagElements.length; i++) {
        	
        	if(tagElements[i].nodeName == "FIELDSET") continue;
        	if(tagElements[i].nodeName == "BUTTON") continue;
        	
            fields.push(new ValangValidator.Field(tagElements[i]));
        }
        return fields;
    },
    disable: function() {
        var fields = this.getFields();
        for (var i = 0; i < fields.length; i++) {
            fields[i].disable();
        }
    },
    enable: function() {
        var fields = this.getFields();
        for (var i = 0; i < fields.length; i++) {
            fields[i].enable();
        }
    },
    focusFirstElement: function(form) {
        var fields = this.getFields();
        for (var i = 0; i < fields.length; i++) {
            var field = fields[i];
            if (field.type != 'hidden' && !field.isDisabled()) {
                field.activate();
                break;
            }
        }
    }
};


/******************************************************************************
 * 
 * Encapsulates a HTML form field
 *
 * Based on code from http://prototype.conio.net/
 * 
 ******************************************************************************/
ValangValidator.Field = function(fieldElement) {
    this.id = fieldElement.id;
    this.name = fieldElement.name;
    this.type = fieldElement.type.toLowerCase();
    this.tagName = fieldElement.tagName.toLowerCase();
    this.fieldElement = fieldElement;   
    
    if (ValangValidator.Field.ValueGetters[this.tagName]) {
        this.getValue = ValangValidator.Field.ValueGetters[this.tagName];
        
    } else if (this.tagName == 'input') {
        switch (this.type) {
            case 'submit':
            case 'hidden':
            case 'password':
            case 'text':
                this.getValue = ValangValidator.Field.ValueGetters['textarea'];
                break;
            case 'checkbox':
            case 'radio':
                this.getValue = ValangValidator.Field.ValueGetters['inputSelector'];
                break;
            default:
                throw 'unexpected input field type \'' + this.type + '\'';
        }
    } else {
        throw 'unexpected form field tag name \'' + this.tagName + '\'';
    }
};


ValangValidator.Field.prototype = {
    clear: function() {
        this.fieldElement.value = '';
    },
    focus: function() {
        // The following line is sometimes effected by Firefox Bug 236791. 
        // https://bugzilla.mozilla.org/show_bug.cgi?id=236791
        this.fieldElement.focus();
    },
    select: function() {
        if (this.fieldElement.select) {
            this.fieldElement.select();
        }
    },
    activate: function() {
        this.focus();
        this.select();
    },
    isDisabled : function() {
        return element.disabled;
    },
    disable: function() {
        element.blur();
        element.disabled = 'true';
    },
    enable: function() {
        element.disabled = '';
    }
};

ValangValidator.Field.ValueGetters = {
    inputSelector: function() {
        if (this.fieldElement.checked) {
            return this.fieldElement.value;
        }
    },
    textarea: function() {
        return this.fieldElement.value;
    },
    select: function() {
        var value = '';
        if (this.fieldElement.type == 'select-one') {
            var index = this.fieldElement.selectedIndex;
            if (index >= 0) {
                value = this.fieldElement.options[index].value;
            }
        } else {
            value = new Array();
            for (var i = 0; i < element.length; i++) {
                var option = this.fieldElement.options[i];
                if (option.selected) {
                    value.push(option.value);
                }
            }
        }
        return value;
    }
};

/******************************************************************************
 * 
 * Represents a single valang rule and the functions needed to evaluate that rule.
 * 
 ******************************************************************************/

ValangValidator.Rule = function(field, valang, errorMessage, validationFunction) {
    this.field = field;
    this.valang = valang;
    this.errorMessage = errorMessage;
    this.validate = validationFunction;
}

ValangValidator.Rule.prototype = {
    getErrorMessage: function() {
        return this.errorMessage;
    },

    /* 	Property Accessor
     *  If you want a custom javascript binding conversion (eg dates!) override this.bindingConversion()
     */
    getPropertyValue: function(propertyName) {
    	
    	var rawValue = this.form.getValue(propertyName);
    	try {
    		var bindValue = this.bindingConversion(rawValue, propertyName);
    		if(bindValue){
        		ValangValidator.Logger.log("Value bound for " + propertyName + ": " + rawValue + " -> " + bindValue);
    			return bindValue;
    		}
    	} 
    	catch(e){}
		ValangValidator.Logger.log("Using raw value for " + propertyName + ": " + rawValue);
        return rawValue;
    },

    // 	Assertions
    _assertHasLength: function(value) {
        if (!value.length) {
            throw 'value \'' + value + '\' does not have length';
        }
    },
    _assertLength: function(value, length) {
        this._assertHasLength(value);
        if (value.length != length) {
            throw 'value\'s length != \'' + length + '\'';
        }
    },
    _throwError: function(msg) {
        throw msg;
    },

	// Type safety checks
    
	/* 	This function tries to convert the lhs into a type that is compatible with the rhs for the various
		JS compare operations.  Supports Date, Number, String 
	 */    
	_makeCompatible: function(lhs, rhs) {
        if(this._isSameType(lhs, rhs)) return lhs;
        // else must be different types
/*
		ValangValidator.Logger.log("LHS: " + lhs + " : " + this._realTypeOf(lhs));
		ValangValidator.Logger.log("RHS: " + rhs + " : " + this._realTypeOf(rhs));
*/
        if(typeof lhs == 'date' || typeof lhs == 'date') {
        	throw "Can\'t make date compatible with non-date: " + lhs + " : " + rhs;
        }
        
        //if RHS is number-able, attempt force of LHS
    	try {
            this._forceNumber(rhs);
            return this._forceNumber(lhs);
        } catch(ex) {
        	// lhs not number
        }
        
        // else string
        if (typeof rhs == 'string') {
            return (new String(lhs)).valueOf(); 
        } else {
            throw 'unable to convert [' + lhs + '] and [' + rhs + '] to compatible types';
        }
    },
    _forceNumber: function(value) {
        if (value && typeof value != 'number' && typeof value != 'date') {

        	var maybeNum = new Number(value);
        	if(!isNaN(maybeNum)) {
        		ValangValidator.Logger.log("Num forced: " + value + " -> " + maybeNum);
        		return maybeNum;
        	}
            throw 'unable to convert value [' + value + '] to number';
        }
        
        return value;
    },
    _isSameType: function(lhs, rhs) {
    	var sameType = false;
    
    	if (lhs == null && rhs == null){ sameType = true;}
    	else if(lhs == null || rhs == null){ sameType = false;}
    	else if(this._realTypeOf(lhs) == this._realTypeOf(rhs)){ sameType = true;}
    		
    	ValangValidator.Logger.log("Same type?: " + sameType + " for " + lhs + " : " + rhs);
    	return sameType;
    },
    _realTypeOf: function(v) { //thanks http://joncom.be/code/realtypeof/
		if (typeof(v) == "object") {
			if (v === null) return "null";
			if (v.constructor == (new Array).constructor) return "array";
			if (v.constructor == (new Date).constructor) return "date";
			if (v.constructor == (new RegExp).constructor) return "regex";
			return "object";
		}
		return typeof(v);
	},

    // Unary Operators
    
    lengthOf: function(value) {
		// only count a CRLF as 1 char: makes LF/CRLF/CR count consistant across OS and browsers. 
        return (value != null) ? value.replace(/\r\n/g,"\n").length : 0; 
    },
    lowerCase: function(value) {
        return (value != null) ? value.toLowerCase(): null;
    },
    upperCase: function(value) {
        return (value != null) ? value.toUpperCase(): null;
    },

    // Binary Operators
    equals: function(lhs, rhs) {
        if ((lhs == null && rhs != null) || (rhs == null && lhs != null)) {
            return false;
        }
        if (lhs == rhs) {
            return true;
        }
        lhs = this._makeCompatible(lhs, rhs);
        rhs = this._makeCompatible(rhs, lhs);
        return lhs === rhs;
    },
    lessThan: function(lhs, rhs) {
    	if(typeof lhs == 'string' ) lhs = this._forceNumber(lhs);
        lhs = this._makeCompatible(lhs, rhs);
        rhs = this._makeCompatible(rhs, lhs);
        return lhs < rhs;
    },
    lessThanOrEquals: function(lhs, rhs) {
    	if(typeof lhs == 'string' ) lhs = this._forceNumber(lhs);
        lhs = this._makeCompatible(lhs, rhs);
        rhs = this._makeCompatible(rhs, lhs);
        return lhs <= rhs;
    },
    moreThan: function(lhs, rhs) {
    	if(typeof lhs == 'string' ) lhs = this._forceNumber(lhs);
        lhs = this._makeCompatible(lhs, rhs);
        rhs = this._makeCompatible(rhs, lhs);
        return lhs > rhs;
    },
    moreThanOrEquals: function(lhs, rhs) {
    	if(typeof lhs == 'string' ) lhs = this._forceNumber(lhs);
        lhs = this._makeCompatible(lhs, rhs);
        rhs = this._makeCompatible(rhs, lhs);
        return lhs >= rhs;
    },
    between: function(lhs, rhs) {
    	this._assertLength(rhs, 2);
    	if(typeof lhs == 'string' ) lhs = this._forceNumber(lhs);
    	lhs = this._makeCompatible(lhs, rhs[0]);
    	rhs[0] = this._makeCompatible(rhs[0], lhs);
    	rhs[1] = this._makeCompatible(rhs[1], lhs);
    	
    	return lhs >= rhs[0] && lhs <= rhs[1];
    },
    
    inFunc: function(lhs, rhs) {
        for (var i = 0; i < rhs.length; i++) {
            var value = rhs[i];
            if (lhs == value) {
                return true;
            }
        }
        return false;
    },
    nullFunc: function(lhs, rhs) {
        return lhs === null || typeof lhs == 'undefined';
    },
    hasText: function(lhs, rhs) {
        return lhs && lhs.replace(/\s/g, '').length > 0;
    },
    hasLength: function(lhs, rhs) {
        return lhs && lhs.length > 0;
    },
    isBlank: function(lhs, rhs) {
        return !lhs || lhs.length === 0;
    },
    isWord: function(lhs, rhs) {
        return lhs && lhs.replace(/\s/g, '') == lhs;
    },
    isUpper: function(lhs, rhs) {
        return lhs && lhs.toUpperCase() == lhs;
    },
    isLower: function(lhs, rhs) {
        return lhs && lhs.toLowerCase() == lhs;
    },

    // Math operators
    add: function(lhs, rhs) {
        return this._forceNumber(lhs) + this._forceNumber(rhs);
    },
    divide: function(lhs, rhs) {
        return this._forceNumber(lhs) / this._forceNumber(rhs);
    },
    modulo: function(lhs, rhs) {
        return this._forceNumber(lhs) % this._forceNumber(rhs);
    },
    multiply: function(lhs, rhs) {
        return this._forceNumber(lhs) * this._forceNumber(rhs);
    },
    subtract: function(lhs, rhs) {
        return this._forceNumber(lhs) - this._forceNumber(rhs);
    },

    // Custom Function
    RegExFunction: function(pattern, value) {
        if (!value.match) {
            throw 'don\'t know how to apply regexp to value \'' + value + '\'';
        }
        var matches = value.match(pattern); 
        return (matches != null && matches[0] == value);
    },
    EmailFunction: function(value) {
        var filter = /^(([A-Za-z0-9]+_+)|([A-Za-z0-9]+\-+)|([A-Za-z0-9]+\.+)|([A-Za-z0-9]+\++))*[A-Za-z0-9]+@((\w+\-+)|(\w+\.))*\w{1,63}\.[a-zA-Z]{2,6}$/;
        return filter.test(value);
    },
    
    // Other functions
    isDate: function(maybeDate) {
        if (maybeDate && this._realTypeOf(maybeDate) == 'date') { //is a date
        	return true;
        }
        return false;
    },
    
    /*
     * This is the default bindingConversion implementation: you probably want 
     * to override it with a converter that knows your binding conversions., 
     * 
     */
     bindingConversion: function(rawValue, fieldName) {
    	
    	// default blindly attempts to convert  value to a date, 
    	var maybeDate = this.parseDate(rawValue, fieldName);
    	if(maybeDate != null) return maybeDate;

    	var maybeNum = this.parseNumber(rawValue, fieldName);
    	if(maybeNum != null) return maybeNum;
    	
    	return null;
    },
    parseDate: function(rawValue, fieldName) {
    	// This implementation favours US dates as per Date.parse - override this for better date support 
    	var maybeDate = new Date(Date.parse(rawValue));
    	if(!isNaN(maybeDate)){
    		ValangValidator.Logger.log("Date parsed: " + fieldName + ": " + rawValue + " -> " + maybeDate);
    		return maybeDate;
    	}
    	return;
    },
    parseNumber: function(rawValue, fieldName) {
    	// strip commas to see if its just a decorated number: 
    	var maybeNum = new Number(rawValue.replace(/,/g,""));
    	if(!isNaN(maybeNum)) {
    		ValangValidator.Logger.log("Num parsed: " + fieldName + ": " + rawValue + " -> " + maybeNum);
    		return maybeNum;
    	}
    	return;
    }    
    
};

/******************************************************************************
 * 
 * Simple static logger implementation; logs output to logId as listed below
 * overide the variable for a new location
 * 
 *****************************************************************************/

ValangValidator.Logger = {
	logId: 'valangLogDiv',
    log: function(msg) {
		if(window.console && console.log) { 
			// firebug logger
			console.log(msg);
		}
		if(! this._logAvailable()) return;
		var msgLi = document.createElement("li");
    	msgLi.appendChild(document.createTextNode(msg));
    	this._logElement.appendChild(msgLi);
    },
    push: function(msg) {
    	this.log(msg);
		if(! this._logAvailable()) return;
		var logElem = document.createElement("ul");
		this._logElement.appendChild(logElem);
		this._logElement = logElem;
    },
    pop: function(msg) {
    	this.log(msg);
    	if(! this._logAvailable()) return;
    	var parent = this._logElement.parentNode;
    	if (parent && parent.nodeName == "UL"){ //check for over-pop!
    		this._logElement = parent;
    	}
    },
    _logElement: null,
    _logAvailableFlag: null,
    _logAvailable: function() {
    	if(this._logAvailableFlag == null) { //first call, setup
			var logDiv = document.getElementById(this.logId);
			if(logDiv){
				var logElem = document.createElement("ul");
				logDiv.appendChild(logElem);
				this._logElement = logElem;
			}
			this._logAvailableFlag = (logDiv != null);
    	}
    	return this._logAvailableFlag;
    	
    	
    },
    _retrieveLogElement: function() {
    	var logDiv = document.getElementById(this.logId);
    	if(!logDiv) return; 
    	
    	if(this._logElement == null) {
    		var logElem = document.createElement("ul");
    		logDiv.appendChild(logElem);
    		this._logElement = logElem;
    	}
    },
    logFunctionCalls: function(object) {
        for (var elementName in object) {
            var theElement = object[elementName]
            if (typeof theElement == 'function') {
                object[elementName] = this._wrapFunctionCallWithLog(elementName, theElement)
            }
        }
    },
    _wrapFunctionCallWithLog: function(functionName, theFunction) {
        return function() {
            ValangValidator.Logger.push('calling ' + functionName + '(' + arguments[0] + ', ' + arguments[1] + ')')
            try {
                var result = theFunction.apply(this, arguments)
            } catch(ex) {
                ValangValidator.Logger.pop('threw ' + ex)
                throw ex
            }
            ValangValidator.Logger.pop('result = ' + result)
            return result
        }
    }
};

/******************************************************************************
 * 
 * utility add-ons: push
 * 
 ******************************************************************************/

if (!Array.prototype.push) {
    // Based on code from http://prototype.conio.net/
    Array.prototype.push = function() {
        var startLength = this.length;
        for (var i = 0; i < arguments.length; i++) {
            this[startLength + i] = arguments[i];
        }
        return this.length;
    }
}


