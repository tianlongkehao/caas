var term, socket, alive;

var terminalContainer = document.getElementById('terminal-container');
$(document).ready(function() {
	dropdownCMD();
	var windowHeight = window.innerHeight-44;
	$(".xterm-viewport").css("height",windowHeight);
	window.onresize = function(){

//		var windowHeight = window.innerHeight-44;
//		$(".xterm-viewport").css("height",windowHeight);
	};
	//保存镜像
	$("#saveImage").click(function() {
		layer.open({
			type : 1,
			title : '保存镜像',
			content : $("#saveImageCon"),
			btn : ['确定', '取消'],
			yes : function(index, layero) {
				var imageName = $('#imageName').val().trim();
				if (imageName.length == 0) {
					layer.tips('对不起，镜像版本名不能为空！', '#imageName', {
						tips : [1, '#3595CC']
					});
					$('#imageName').focus();
					return;
				}
				if (imageName.length > 128) {
					layer.tips('对不起，镜像版本名过长！', '#imageName', {
						tips : [1, '#3595CC']
					});
					$('#imageName').focus();
					return;
				}
				reg = /^[a-z][a-z0-9-_]*$/;
				if (!reg.test(imageName)) {
					layer.tips('镜像名称只能由字母、数字、横线和下划线组成,且首字母只能为字母', '#imageName', {
						tips : [1, '#3595CC']
					});
					$('#imageName').focus();
					return;
				}
				var version = $('#version').val().trim();
				if (version.length == 0) {
					layer.tips('对不起，镜像版本名不能为空！', '#version', {
						tips : [1, '#3595CC']
					});
					$('#version').focus();
					return;
				}
				if (version.length > 128) {
					layer.tips('对不起，镜像版本名过长！', '#version', {
						tips : [1, '#3595CC']
					});
					$('#version').focus();
					return;
				}
				reg = /^[a-zA-Z0-9_.-]+$/;
				if (!reg.test(version)) {
					layer.tips('对不起，您输入的镜像版本格式不正确！', '#version', {
						tips : [1, '#3595CC']
					});
					$('#version').focus();
					return;
				}
				var cmdString = $('#cmdString').val().replace(/\s+/g, ' ').trim();
				if (cmdString.length == 0) {
					layer.tips('对不起，启动命令不能为空！', '#cmdString', {
						tips : [1, '#3595CC']
					});
					$('#cmdString').focus();
					return;
				}
				if (cmdString.length > 128) {
					layer.tips('对不起，启动命令过长！', '#cmdString', {
						tips : [1, '#3595CC']
					});
					$('#cmdString').focus();
					return;
				}
				var containerId = $('#containerId').val();
				var nodeIP = $('#containerIp').val();
				saveAsImage(containerId, nodeIP, cmdString);
				layer.close(index);
			}
		});
	});
});
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
//导出container为image
function saveAsImage(containerId, nodeIP, cmdString) {
	$('.progress-bar-info').text("容器保存中...");
	$('#myModal').modal('show');
	$.when(getdata(containerId, nodeIP, cmdString)).done(function(data) {
		$('#myModal').modal('hide');
	});
}

function getdata(containerId, nodeIP, cmdString) {
	var defer = $.Deferred();
	var imageName = $('#imageName').val();
	var version = $('#version').val().trim();
	$.ajax({
		url : ctx + "/service/saveAsImage",
		type : "POST",
		data : {
			"containerId" : containerId,
			"nodeIP" : nodeIP,
			"imageName" : imageName,
			"version" : version,
			"cmdString" : cmdString
		},
		success : function(data) {
			defer.resolve(data);
			data = eval("(" + data + ")");
			if (data.status == "200") {
				layer.msg('容器已成功保存为镜像', {
					icon : 1,
					btn : ['確定'],
					time : 0, // 不自动关闭
					yes : function(index) {
						layer.closeAll();
					}
				});
			} else if (data.status == "400") {
				failedMSG("对不起，仓库中已存在该版本名！");
			} else if (data.status == "401") {
				failedMSG("对不起，container保存为本地镜像失败！");
			} else if (data.status == "402") {
				failedMSG("对不起，本地镜像推送到仓库时失败！");
			} else if (data.status == "403") {
				failedMSG("对不起，获取数据库中原始镜像的信息失败！");
			}
		}
	});
	return defer.promise();
}

// 弹出失败消息
function failedMSG(title) {
	layer.msg(title, {
		icon : 5,
		btn : ['確定'],
		time : 0, // 不自动关闭
		yes : function(index) {
			layer.close(index);
		}
	});
}
