var rules = {
    'input#login' : function(element){
        element.onclick = function() {
            XT.doAjaxSubmit('validate', this);
            return false;
        }
    }
};

Behaviour.register(rules);

