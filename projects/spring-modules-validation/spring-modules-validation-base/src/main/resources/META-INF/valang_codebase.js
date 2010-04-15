/*
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
if (!Function.prototype.apply) {
    // Based on code from http://prototype.conio.net/
    Function.prototype.apply = function(object, parameters) {
        var parameterStrings = new Array();
        if (!object) {
            object = window;
        }
        if (!parameters) {
            parameters = new Array();
        }
        for (var i = 0; i < parameters.length; i++) {
            parameterStrings[i] = 'parameters[' + i + ']';
        }
        object.__apply__ = this;
        var result = eval('object.__apply__(' + parameterStrings.join(', ') + ')');
        object.__apply__ = null;
        return result;
    }
}

/*
 * Core validation object.
 */
var ValangValidator = function(name, installSelfWithForm, rules) {
    this.name = name;
    this.rules = rules;
    this.form = this._findForm(name);
    if (installSelfWithForm) {
        this._installSelfWithForm();
    }
};

ValangValidator.prototype = {
	globalErrorsId: 'global_errors',
	fieldErrorIdSuffix: '_error',
    validate: function() {
        return this._validateAndReturnFailedRules().length > 0;
    },
    validateAndShowFeedback: function() {
        var failedRules = this._validateAndReturnFailedRules();
        if (failedRules.length > 0) {
            this.showValidationFeedback(failedRules);
        }
        return failedRules.length === 0;
    },
    showValidationFeedback: function(failedRules) {

        // first putting all the valiation error in field propriatery error place
        // holders, basically elements with id <filed_name>_error.
        var globalRules = new Array();
        for (var i = 0; i < failedRules.length; i++) {
            var errorBoxId = failedRules[i].field + this.fieldErrorIdSuffix;
            var errorBox = document.getElementById(errorBoxId);
            if (errorBox != null) {
                errorBox.innerHTML = failedRules[i].getErrorMessage();
            } else {
                globalRules.push(failedRules[i]);
            }
        }

        // all those errors that weren't put in a field propriatery error place holders
        // will be grouped together as global errors, either in a global error place holder
        // or will be shown in an alert.
        var globalErrorsBox = document.getElementById(this.globalErrorsId);
        if (globalErrorsBox != null) {
            var ul = document.createElement('ul');
            for (var i = 0; i < globalRules.length; i++) {
                var li = document.createElement('li');
                li.innerHTML = globalRules[i].getErrorMessage();
                ul.appendChild(li);
            }
            globalErrorsBox.appendChild(ul);
        } else {
            var errors = '';
            for (var i = 0; i < globalRules.length; i++) {
                errors = errors + globalRules[i].getErrorMessage() + '\n';
            }
            // The following line is sometimes effected by Firefox Bug 236791. Please just ignore
            // the error or tell me how to fix it?
            // https://bugzilla.mozilla.org/show_bug.cgi?id=236791
            alert(errors);
        }

        // disabled, as it causes onBlur recursion
        //var fields = this.form.getFieldsWithName(failedRules[0].field);
        //if (fields.length > 0) {
        //    fields[0].activate();
       // }
    },
    _findForm: function(name) {
        var element = document.getElementById(name);
        if (!element || element.tagName.toLowerCase() != 'form') {
            console.log("no form");
            element = document.getElementById(name + 'ValangValidator');
        }
        if (!element || element.tagName.toLowerCase() != 'script') {
        	//throw 'unable to find form with ID \'' + name + '\' or script element with ID \'' + name + 'ValangValidator\''
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
    _installSelfWithForm: function() {
        var oldOnload = window.onload;
        var oldOnsubmit = this.form.formElement.onsubmit;
        var thisValidator = this;
        // delay install until the page is 
        // fully loaded so that we can be 
        // (fairly) sure of being the last 
        // thing that tries to handle the
        // onload event
        window.onload = function() {
            ValangValidator.Logger.log('Installing ValangValidator \'' + thisValidator.name + '\' as onsubmit handler');
            try {
                if (oldOnload) {
                    oldOnload();
                }
            } catch (err) {
            	ValangValidator.Logger.log('Error: ' + err);
            }
            	
        	// onblur for each field
        	var fields = thisValidator.form.getFields();
        	for ( var i = 0; i < fields.length; i++) {
				var thisField = fields[i];
				if(thisField.type == "submit") continue; //dont add to submit buttons
				
				thisField.fieldElement._oldBlur = thisField.fieldElement.blur;
        		thisField.fieldElement.onblur = function() {
        			// fire any old onblur
        			if(this._oldBlur) {
        	            ValangValidator.Logger.push('Calling OldBlur');
        				this._oldBlur();
        	            ValangValidator.Logger.pop('End OldBlur');
        			}
        			// check this field
        			var vField =  new ValangValidator.Field(this);
        			var fieldRuleCount = thisValidator._countRules(vField);
        			var valid = true;
        			if(fieldRuleCount > 0){
        				var failedRules = thisValidator._validateField(vField);
        				valid = (failedRules.length == 0);
        				if(!valid) thisValidator.showValidationFeedback(failedRules);
        			}

        			
        			if(thisValidator.fieldValidationCallback) {
        				thisValidator.fieldValidationCallback(this, valid, fieldRuleCount);
        			}
        		};
			}
        	
        	// on submit
            thisValidator.form.formElement.onsubmit = function() {
                if (!oldOnsubmit || oldOnsubmit()) {
                	var valid = thisValidator.validateAndShowFeedback();
                	var callback = true;
        			if(thisValidator.formValidationCallback) {
        				callback = thisValidator.formValidationCallback(this, valid);
        			}
        			return callback && valid;
                }
            }
        }
    },
    fieldValidationCallback: function(field, isValid, ruleCount) {
        ValangValidator.Logger.log('Stub callback: ' + field + " : " + isValid + " : " + ruleCount);
    },
    formValidationCallback: function(form, isValid) {
        ValangValidator.Logger.log('Stub callback: ' + form + " : " + isValid);
        return isValid;
    },
    _countRules: function(field) {
        var count = 0;
        for (var i = 0; i < this.rules.length; i++) {
            var rule = this.rules[i];
            if(rule.field == field.name) {
            	count++;
            }
        }
        return count;
    },
    _validateField: function(field) {
        var failedRules = new Array();
        ValangValidator.Logger.push('Evaluating ' + this.rules.length + ' rules');
        for (var i = 0; i < this.rules.length; i++) {
            var rule = this.rules[i];
            if(rule.field == field.name) {
	            ValangValidator.Logger.push('Evaluating rule for field [' + rule.field + ']');
	            this._clearErrorIfExists(rule.field);
	            rule.form = this.form;
	            var valid = false;
	            try {
	            	valid=rule.validate();
	            } catch(err) {
	            	ValangValidator.Logger.log("error: " + err);
	            }
	            if (!valid) {
	                ValangValidator.Logger.pop('Failed');
	                failedRules.push(rule);
	            } else {
	                ValangValidator.Logger.pop('Passed');
	            }
            }
        }
        ValangValidator.Logger.pop('Finished rules evaluation');
        return failedRules;
    },
    _validateAndReturnFailedRules: function() {
        this._clearGlobalErrors();
        ValangValidator.Logger.push('Starting validation');
        var failedRules = new Array();
        for (var i = 0; i < this.rules.length; i++) {
            var rule = this.rules[i];
            this._clearErrorIfExists(rule.field);

            ValangValidator.Logger.push('Evaluating rule for field [' + rule.field + ']');
            rule.form = this.form;
            var valid = false;
            try {
            	valid=rule.validate();
            } catch(err) {
            	ValangValidator.Logger.log("error: " + err);
            }
            if (!valid) {
                ValangValidator.Logger.pop('Failed');
                failedRules.push(rule);
            } else {
                ValangValidator.Logger.pop('Passed');
            }            
        }

        ValangValidator.Logger.pop('Finshed - ' + failedRules.length + ' failed rules');
        return this._giveRulesSameOrderAsFormFields(failedRules);
    },
    _clearErrorIfExists: function(field) {
        var errorBox = document.getElementById(field + this.fieldErrorIdSuffix);
        if (errorBox != null) {
            errorBox.innerHTML = '';
        }
    },
    _clearGlobalErrors: function() {
        var errorBox = document.getElementById(this.globalErrorsId);
        if (errorBox != null) {
            errorBox.innerHTML = '';
        }
    },
    _giveRulesSameOrderAsFormFields: function(failedRules) {
        var sortedFailedRules = new Array();
        var fields = this.form.getFields();
        for (var i = 0; i < fields.length; i++) {
            var fieldName = fields[i].name;
            for (var j = 0; j < failedRules.length; j++) {
                if (failedRules[j] && failedRules[j].field == fieldName) {
                    sortedFailedRules.push(failedRules[j]);
                    failedRules[j] = null;
                }
            }
        }
        for (var i = 0; i < failedRules.length; i++) {
            if (failedRules[i]) {
                sortedFailedRules.push(failedRules[i]);
            }
        }
        return sortedFailedRules;
    }
};

/*
 * Simple static logger implementation; by default attempts to log output
 * into a div with ID = 'valangLogDiv'.
 * If you wish to provide an alternative location for the log output you 
 * must overide the ValangValidator.Logger.log function.
 */
ValangValidator.Logger = {
	logId: 'valangLogDiv',
    log: function(msg) {
		if(! this._logAvailable()) return;
		var msgLi = document.createElement("li");
    	msgLi.appendChild(document.createTextNode(msg));
    	this._logElement.appendChild(msgLi);
    },
    push: function(msg) {
		if(! this._logAvailable()) return;
        this.log(msg);
		var logElem = document.createElement("ul");
		this._logElement.appendChild(logElem);
		this._logElement = logElem;
    },
    pop: function(msg) {
    	if(! this._logAvailable()) return;
    	var parent = this._logElement.parentNode;
    	if (parent && parent.nodeName == "UL"){ //check for over-pop!
    		this._logElement = parent;
    	}
        this.log(msg);
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
    	var logDiv = document.getElementById('valangLogDiv');
    	if(!logDiv) return; 
    	
    	if(this._logElement == null) {
    		var logElem = document.createElement("ul");
    		logDiv.appendChild(logElem);
    		this._logElement = logElem;
    	}
    },
    logFunctionCalls: function(object) {
        for (var elementName in object) {
            var theElement = object[elementName];
            if (typeof theElement == 'function') {
                object[elementName] = this._wrapFunctionCallWithLog(elementName, theElement);
            }
        }
    },
    _wrapFunctionCallWithLog: function(functionName, theFunction) {
        return function() {
            ValangValidator.Logger.push('calling ' + functionName + '(' + arguments[0] + ', ' + arguments[1] + ')');
            try {
                var result = theFunction.apply(this, arguments);
            } catch(ex) {
                ValangValidator.Logger.pop('threw ' + ex);
                throw ex;
            }
            ValangValidator.Logger.pop('result = ' + result);
            return result;
        }
    }
};

/*
 * Encapsulates a HTML form
 *
 * Based on code from http://prototype.conio.net/
 */
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

/*
 * Encapsulates a HTML form field
 *
 * Based on code from http://prototype.conio.net/
 */
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
        // The following line is sometimes effected by Firefox Bug 236791. Please just ignore
        // the error or tell me how to fix it?
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

/*
 * Represents a single valang validation rule and the functions needed
 * to evaluate that rule.
 */
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

// Property Accessor
    getPropertyValue: function(propertyName, expectedType) {
        return this.form.getValue(propertyName);
    },

// Assertions
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

// This function tries to convert the lhs into a type
// that are compatible with the rhs for the various
// JS compare operations. When there is a choice between
// converting to a string or a number; number is always
// favoured.
    _makeCompatible: function(lhs, rhs) {
        try {
            this._forceNumber(rhs);
            return this._forceNumber(lhs);
        } catch(ex) {
        }
        var lhsType = typeof lhs;
        var rhsType = typeof rhs;
        if (lhsType == rhsType) {
            return lhs;
        } else if (lhsType == 'number' || rhsType == 'number') {
            return this._forceNumber(lhs);
        } else {
            throw 'unable to convert [' + lhs + '] and [' + rhs + '] to compatible types';
        }
    },
    _forceNumber: function(value) {
        if (typeof value != 'number') {
            try {
                var newValue = eval(value.toString());
            } catch(ex) {
            }
            if (newValue && typeof newValue == 'number') {
                return newValue;
            }
            throw 'unable to convert value [' + value + '] to number';
        }
        return value;
    },

// Unary Operators
    lengthOf: function(value) {
        return (value != null) ? value.length : 0;
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
        lhs = this._makeCompatible(lhs, rhs);
        rhs = this._makeCompatible(rhs, lhs);
        return lhs < rhs;
    },
    lessThanOrEquals: function(lhs, rhs) {
        lhs = this._makeCompatible(lhs, rhs);
        rhs = this._makeCompatible(rhs, lhs);
        return lhs <= rhs;
    },
    moreThan: function(lhs, rhs) {
        lhs = this._makeCompatible(lhs, rhs);
        rhs = this._makeCompatible(rhs, lhs);
        return lhs > rhs;
    },
    moreThanOrEquals: function(lhs, rhs) {
        lhs = this._makeCompatible(lhs, rhs);
        rhs = this._makeCompatible(rhs, lhs);
        return lhs >= rhs;
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
    between: function(lhs, rhs) {
        this._assertLength(rhs, 2);
        lhs = this._makeCompatible(lhs, rhs[0]);
        rhs[0] = this._makeCompatible(rhs[0], lhs);
        rhs[1] = this._makeCompatible(rhs[1], lhs);
        return lhs >= rhs[0] && lhs <= rhs[1];
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
    parseDate: function(dateString, fieldName) {
    	var theDate = new Date(Date.parse(dateString));
    	ValangValidator.Logger.log("Using internal date parser: " + 
    			fieldName + ": " + dateString + " -> " + theDate);
    	return theDate;
    }
    
};
