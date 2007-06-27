var rules = {
    '#usernameInput' : function(element) {
        element.onkeyup = function() {
            XT.doAjaxAction('validateUsername', this, {'username' : this.value});
            return true;
        }
    },
    
    '#signup' : function(element) {
        element.onclick = function() {
            XT.doAjaxSubmit('validate', this);
            return false;
        }
    }
};

Behaviour.register(rules);

