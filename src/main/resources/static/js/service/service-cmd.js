var term, socket, alive;

var terminalContainer = document.getElementById('terminal-container');
$(document).ready(function() {
	dropdownCMD();
	var windowHeight = window.innerHeight-44;
	$(".xterm-viewport").css("height",windowHeight);
	window.onresize = function(){
		
//		var windowHeight = window.innerHeight-44;
//		$(".xterm-viewport").css("height",windowHeight);
	}
})
function dropdownCMD() {
	$("#terminal-container").html("");
	var container = $("#containerid").val();
	var nodeIP = $("#nodeIP").val();
	var dockerServerPort = $("#dockerIOPort").val();
	var containerID = container.replace('docker://', '');
	var host = $("#entryHost").val();
	var dockerServerURL = 'http://' + nodeIP + ':' + dockerServerPort;
	var soc = "ws://" + host + "/enter?method=web&containerID=" + containerID + "&dockerServerURL=" + dockerServerURL;
	socket = new WebSocket(soc);
	term = new Terminal();
	term.open(terminalContainer);
	socket.onopen = runRealTerminal;
	socket.onclose = runFakeTerminal;
	socket.onerror = runFakeTerminal;
}

function stringToByte(str) {
	var bytes = new Array();
	var len, c;
	len = str.length;
	for (var i = 0; i < len; i++) {
		c = str.charCodeAt(i);
		if (c >= 0x010000 && c <= 0x10FFFF) {
			bytes.push(((c >> 18) & 0x07) | 0xF0);
			bytes.push(((c >> 12) & 0x3F) | 0x80);
			bytes.push(((c >> 6) & 0x3F) | 0x80);
			bytes.push((c & 0x3F) | 0x80);
		} else if (c >= 0x000800 && c <= 0x00FFFF) {
			bytes.push(((c >> 12) & 0x0F) | 0xE0);
			bytes.push(((c >> 6) & 0x3F) | 0x80);
			bytes.push((c & 0x3F) | 0x80);
		} else if (c >= 0x000080 && c <= 0x0007FF) {
			bytes.push(((c >> 6) & 0x1F) | 0xC0);
			bytes.push((c & 0x3F) | 0x80);
		} else {
			bytes.push(c & 0xFF);
		}
	}
	return bytes;
}

function runRealTerminal() {
	term.attach(socket);
	term._initialized = true;
	term.fit();
	alive = setInterval("keepAlive()",3000);
}

function keepAlive() {
	var keepAliveMsg = {
		"MsgType" : 3,
		"Content" : stringToByte("js---keepalive"),
	};
	socket.send(JSON.stringify(keepAliveMsg));
}

$(window).resize(function() {
	if(alive != null)
	term.fit();
});

function runFakeTerminal() {
	if(alive != null)clearInterval(alive);
	if (term._initialized) {
		return;
	}
	term._initialized = true;
	term.writeln('Connection is closed.');
	term.writeln('');
}