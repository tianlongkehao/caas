$(document).ready(function(){
	GateOne.init({url: 'https://192.168.247.129',
		embedded: true
//			,theme: 'black',style: { 'background-color': 'black', 'box-shadow': '0 0 40px blueViolet'}
	},newTerminal);

});

var newTerminal = function() {
    // Introducing the superSandbox()!  Use it to wrap any code that you don't want to load until dependencies are met.
    // In this example we won't call newTerminal() until GateOne.Terminal and GateOne.Terminal.Input are loaded.
    GateOne.Base.superSandbox("NewExternalTerm", ["GateOne.Terminal", "GateOne.Terminal.Input"], function(window, undefined) {
        "use strict";
        var existingContainer = GateOne.Utils.getNode('#'+GateOne.prefs.prefix+'container');
    var container = GateOne.Utils.createElement('div', {
            'id': 'container', 'class': 'terminal', 'style': {'height': '100%', 'width': '100%'}
    });
    var gateone = GateOne.Utils.getNode('#gateone');
    // Don't actually submit the form
    if (!existingContainer) {
            gateone.appendChild(container);
    } else {
            container = existingContainer;
    }
    // Create the new terminal
    var termNum = GateOne.Terminal.newTerminal(null, null, container); 
    });
};
