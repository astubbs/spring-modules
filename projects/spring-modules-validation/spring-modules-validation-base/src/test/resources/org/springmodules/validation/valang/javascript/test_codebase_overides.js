/* 
 * This code adds browser object mocks and replacment functions to work in rhino 
 */

// Mock the document/window object
var document = {
		getElementById: function(id) { return null; }
}
var window = document;

// Mock alert function
var alert = function(msg){};

// overide the getValue function to use the form object injected with Rhino
ValangValidator.Form.prototype.getValue = function(propertyName) {

	var javaResult = formObject[propertyName];
	var result;
    
    if (javaResult === undefined || javaResult === null) return null; // allow "" which is falsy
    
    if (javaResult['getClass'] && javaResult.getClass() == new java.util.Date().getClass()) {
        // convert java Date to JavaScript Date
        result = eval('new Date(' + javaResult.getTime() + ')');

    } else {
        var toEval = javaResult.toString();

        // toEval is a JS string or a java.lang.String; convert to a javascript string.
        if (typeof(toEval) != 'string') {
            toEval = new String(toEval);
        }
        toEval = '\'' + toEval.replace('\'', '\\\'') + '\'';
        result = eval(toEval);
    }
    return result;

}

// return an array with a mock HTML element
ValangValidator.Form.prototype.getFieldsWithName = function(name) {
 return [new ValangValidator.Field({
			id: "testId"+name,
			name: name,
			type: "text",
			tagName: "input",
			addEventListener: function(){},
			removeEventListener: function(){},
			fieldElement: null   
 })];
}

// return Form with mock form HTML element
ValangValidator.prototype._findForm = function(name) {
    return new ValangValidator.Form({
    	elements:{}
    });
}

