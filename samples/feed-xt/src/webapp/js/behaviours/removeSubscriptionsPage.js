var rules = {
    'input#removeSubscriptions' : function(element){
        element.onclick = function() {
            var ok = window.confirm(this.getAttribute("confirmMessage"));
            if (ok) {
                XT.doAjaxSubmit('validate', this);
            }
            return false;
        }
    }
};

Behaviour.register(rules);

