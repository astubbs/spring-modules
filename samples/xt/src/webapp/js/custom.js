XT.defaultLoadingElementId = 'loading';

XT.defaultLoadingImage = '../images/loading.gif';

XT.defaultErrorHandler = function(ajaxRequest, exception) {
    alert(exception.message);
};