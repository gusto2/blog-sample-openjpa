
YUI.add('messenger', function(Y) {
    Y.Messenger = {
        message: function(node, message) {
            Y.one(node).setHTML(Y.Escape.html(message));
        }
    };
}, '0.0.1', {requires: ['node','escape']});