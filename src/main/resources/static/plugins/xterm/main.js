var term, protocol, socketURL, socket, pid, charWidth, charHeight;

var terminalContainer = document.getElementById('terminal-container');

function dropdownCMD(object) {
	$(".contentMain>div:not('.baseInfo')").addClass("hide");
	$(".containerCMD").removeClass("hide");
	$("#terminal-container").html("");
	var containerID = object.getAttribute("containerid").replace('docker://', '');
	var host = object.getAttribute("entryHost");
	var dockerServerURL = 'http://' + object.getAttribute("dockerServerURL") + ':' + object.getAttribute("dockerServerPort");

	var soc = "ws://" + host + "/enter?method=web&containerID=" + containerID + "&dockerServerURL=" + dockerServerURL;
	socket = new WebSocket(soc);
	//todo
	protocol = (location.protocol === 'https:') ? 'wss://' : 'ws://';
	socketURL = protocol + location.hostname + ((location.port) ? (':' + location.port) : '') + '/terminals/';
	term = new Terminal();
	term.open(terminalContainer);
	term.fit();
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
}

function runFakeTerminal() {
	if (term._initialized) {
		return;
	}

	term._initialized = true;

	//	var shellprompt = '$ ';

	//	term.prompt = function() {
	//		term.write('\r\n' + shellprompt);
	//	};

	term.writeln('Connection is closed.');
	term.writeln('');
	//	term.prompt();

	//	term.on('key', function(key, ev) {
	//		var printable = (!ev.altKey && !ev.altGraphKey && !ev.ctrlKey && !ev.metaKey
	//		);
	//
	//	if (ev.keyCode == 13) {
	//		term.prompt();
	//	} else if (ev.keyCode == 8) {
	//		// Do not delete the prompt
	//		if (term.x > 2) {
	//			term.write('\b \b');
	//		}
	//		} else if (printable) {
	//			term.write(key);
	//		}
	//	});
	//
	//	term.on('paste', function(data, ev) {
	//		term.write(data);
	//	});
}
