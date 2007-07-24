var rules = {
    'a.feedLink' : function(element){
        element.onclick = function() {
            XT.doAjaxAction('viewFeed', this, {'subscription' : this.getAttribute('subscription')});
            return false;
        };
    },
    
    'a[id^="entry"]' : function(element){
        element.onclick = function() {
            XT.doAjaxAction('toggleEntry', this, 
            {'subscription' : this.getAttribute('subscription'), 'entryIndex' : this.getAttribute('entryIndex'), 'status' : this.getAttribute('class')});
            return false;
        }
    }
};

Behaviour.register(rules);

function showEntryEffect(params) {
    var selector = new DOMSelector();
    var elements = selector.select(params.selector);
    if (elements.length == 1) {
        Effect.BlindDown(elements[0]);
    }
}

function hideEntryEffect(params) {
    var selector = new DOMSelector();
    var elements = selector.select(params.selector);
    if (elements.length == 1) {
        Effect.BlindUp(elements[0]);
    }
}