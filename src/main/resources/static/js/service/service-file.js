var filesocket, fileSocketAlive, msgnum, endString, resultString, replaceString;
resultString = "";
function dropdownFile(object) {
	$(".contentMain>div:not('.baseInfo')").addClass("hide");
	$(".containerFile").removeClass("hide");
	$("#terminal-container").html("");
	var containerID = object.getAttribute("containerid").replace('docker://', '');
	var host = object.getAttribute("entryHost");
	var dockerServerURL = 'http://' + object.getAttribute("dockerServerURL") + ':' + object.getAttribute("dockerServerPort");

	var soc = "ws://" + host + "/enter?method=web&containerID=" + containerID + "&dockerServerURL=" + dockerServerURL;
	filesocket = new WebSocket(soc);

	filesocket.onerror = function() {
		layer.msg("filesocket连接发生错误", {
			icon : 1
		});
	};

	//连接成功建立的回调方法
	filesocket.onopen = function() {
		reresultString = "";
		msgnum = 0;
		fileSocketAlive = setInterval("keepFileSocketAlive()", 3000);
	};

	//接收到消息的回调方法
	filesocket.onmessage = function(event) {
		//读取返回的数据
		var reader = new FileReader();
		reader.readAsBinaryString(event.data);
		//blob
		reader.onloadend = function() {
			var object = eval('(' + reader.result + ')');
			//判断是文件处理用的的数据
			if (object.msgType == 5) {
				msgnum = msgnum + 1;
				var responseMsg = Base64(object.content);
				resultString += responseMsg;
				console.log(msgnum);
				console.log(responseMsg);
				/*
				 * 链接socket时会返回一个sh-4.2#
				 * 输入ls后会返回：1.ls 2.文件列表 3.sh-4.2#
				 */
				if (msgnum == 1) {
					endString = Base64(object.content);
					sendChangeDirMsg("/");
				} else if (msgnum < 20) {
					//展示目录内容
					if (resultString.endsWith(endString)) {
						resultString = resultString.replace(endString, "").replace("ls -F", "").trim();
						showFileList(resultString);
					}
				} else if (msgnum < 30) {
					//切换目录后发送显示路径的请求
					if (resultString.endsWith(endString)) {
						sendPwdMsg();
					}
				} else if (msgnum < 40) {
					if (resultString.endsWith(endString)) {
						resultString = resultString.replace(endString, "").replace("pwd", "").trim();
						//显示路径
						showPath(resultString);
						//显示路径后发送显示目录内容的请求
						sendListFileMsg();
					}
				}
			}
		};
	};

	//连接关闭的回调方法
	filesocket.onclose = function() {
	};

	//监听窗口关闭事件，当窗口关闭时，主动去关闭filesocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
	window.onbeforeunload = function() {
		if (fileSocketAlive != null) {
			clearInterval(fileSocketAlive);
		}
		closefilesocket();
	};

}

//关闭WebSocket连接
function closefilesocket() {
	filesocket.close();
}

//展示文件列表
function showFileList(data) {
	var fileObjArray = data.replace(/\s+/g, ',').split(",");
	var fileHtml = "";
	for (var i = 0; i < fileObjArray.length; i++) {
		if (fileObjArray[i] == "" || fileObjArray[i] == null) {
			fileHtml += '';
		} else {
			var fileName = fileObjArray[i];
			fileHtml += '<tr>' + '<td style="width: 5%;text-indent: 14px;"><input type="checkbox"></td>';
			if (fileName.endsWith("/")) {
				fileHtml += '<td class="fileName" style="width: 45%;"><a href="javascript:sendChangeDirMsg(\'' + fileName.substring(0, fileName.length - 1) + '\')"><i class="fa fa-folder"></i>';
			} else if (fileName.endsWith("*")) {
				fileHtml += '<td class="fileName" style="width: 45%;"><a ><i class="fa fa-file-code-o"></i>';
			} else if (fileName.endsWith("@")) {
				fileHtml += '<td class="fileName" style="width: 45%;"><a href="javascript:sendChangeDirMsg(\'' + fileName.substring(0, fileName.length - 1) + '\')"><i class="fa fa-share-square"></i>';
			} else {
				fileHtml += '<td class="fileName" style="width: 45%;"><a ><i class="fa fa-file"></i>';
			}
			if (fileName.endsWith("/") || fileName.endsWith("@") || fileName.endsWith("*")) {
				fileHtml += fileName.substring(0, fileName.length - 1) + '</a></td>';
			} else {
				fileHtml += fileName + '</a></td>';
			}
			if (fileName.endsWith("/") || fileName.endsWith("@")) {
				fileHtml += '<td style="width: 40%;"></td>' + '</tr>';
			} else {
				fileHtml += '<td style="width: 40%; text-indent:6px">' + '<a onclick="uploadOneFile()" title="上传文件"><i class="fa fa-upload"></i></a>' + '<a onclick="downloadOneFile()" title="导出文件"><i class="fa fa-download"></i>' + '<input hidden="true" value="" id="downfilepath"/>' + '</a>' + '<a id="deleteButton" class="no-drop"' + 'href="javascript:delOneFile()" title="删除">' + '<i id="deleteButtonfile" class="fa fa-trash"></i>' + '</a></td>' + '</tr>';
			}
		}
	}
	$("#fileBody").empty();
	$("#fileBody").append(fileHtml);
}

//修改显示的路径
function showPath(data) {
	$('#path').val(data);
}

//获取目录
function sendListFileMsg() {
	resultString = "";
	msgnum = 10;
	var data = "ls -F\n";
	var stdinMsg = {
		"MsgType" : 2,
		"Content" : stringToByte(data),
	};
	filesocket.send(JSON.stringify(stdinMsg));
}

//切换目录
function sendChangeDirMsg(dir) {
	resultString = "";
	msgnum = 20;
	//切换目录前清空列表
	$("#fileBody").empty();

	var data = "cd " + dir + "\n";
	var stdinMsg = {
		"MsgType" : 2,
		"Content" : stringToByte(data),
	};
	filesocket.send(JSON.stringify(stdinMsg));
}

//获取完整路径
function sendPwdMsg() {
	resultString = "";
	msgnum = 30;
	var data = "pwd\n";
	var stdinMsg = {
		"MsgType" : 2,
		"Content" : stringToByte(data),
	};
	filesocket.send(JSON.stringify(stdinMsg));
}

function keepFileSocketAlive() {
	var keepAliveMsg = {
		"MsgType" : 3,
		"Content" : stringToByte("js---keepalive"),
	};
	filesocket.send(JSON.stringify(keepAliveMsg));
}
