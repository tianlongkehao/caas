var filesocket, fileSocketAlive, msgnum, endString, resultString;
resultString = "";
function dropdownFile(object) {
	$(".contentMain>div:not('.baseInfo')").addClass("hide");
	$(".containerFile").removeClass("hide");
	$("#terminal-container").html("");

	var nodeIP = object.getAttribute("dockerServerURL");
	$('#containerFileIp').val(nodeIP);
	var containerID = object.getAttribute("containerid").replace('docker://', '');
	$('#containerFileId').val(containerID);
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
					//连接后先取得"sh-4.2#"作为命令结束标志
					endString = Base64(object.content);
					sendChangeDirMsg("/");
				} else if (msgnum < 20) {
					//处理发送获取目录列表的msg后返回的msg
					//展示目录内容
					if (resultString.endsWith(endString)) {
						resultString = resultString.replace(endString, "").replace("ls -F", "").trim();
						showFileList(resultString);
					}
				} else if (msgnum < 30) {
					//处理发送切换目录的msg后返回的msg
					//切换目录后发送显示路径的请求
					if (resultString.endsWith(endString)) {
						sendPwdMsg();
					}
				} else if (msgnum < 40) {
					//处理发送获取当前路径的msg后返回的msg
					if (resultString.endsWith(endString)) {
						resultString = resultString.replace(endString, "").replace("pwd", "").trim();
						//显示路径
						showPath(resultString);
						//显示路径后发送显示目录内容的请求
						sendListFileMsg();
					}
				} else if (msgnum < 50) {
					//处理发送创建文件夹的msg后返回的msg
					//创建文件夹后发送显示路径的请求
					if (resultString.endsWith(endString)) {
						sendPwdMsg();
					}
				} else if (msgnum < 60) {
					//处理发送删除文件的msg后返回的msg
					//删除文件后发送显示路径的请求
					if (resultString.endsWith(endString)) {
						sendPwdMsg();
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

//处理返回的文件列表信息，展示文件列表
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
				fileHtml += '<td class="fileName" style="width: 45%;"><a ><i class="fa fa-file-o"></i>';
			} else if (fileName.endsWith("@")) {
				fileHtml += '<td class="fileName" style="width: 45%;"><a href="javascript:sendChangeDirMsg(\'' + fileName.substring(0, fileName.length - 1) + '\')"><i class="fa fa-share-square"></i>';
			} else {
				fileHtml += '<td class="fileName" style="width: 45%;"><a ><i class="fa fa-file"></i>';
			}
			var fileRealName;
			if (fileName.endsWith("/") || fileName.endsWith("@") || fileName.endsWith("*")) {
				fileRealName = fileName.substring(0, fileName.length - 1);
				fileHtml += '<span>' + fileRealName + '</span></a></td>';
			} else {
				fileRealName = fileName;
				fileHtml += '<span>' + fileRealName + '</span></a></td>';
			}
			if (fileName.endsWith("/") || fileName.endsWith("@")) {
				fileHtml += '<td style="width: 40%;"></td>' + '</tr>';
			} else {
				fileHtml += '<td style="width: 40%; text-indent:6px">' + '<a class="no-drop" href="javascript:downloadContainerFile(\'' + fileRealName + '\')" title="导出文件"><i class="fa fa-download"></i></a>' + '<a class="no-drop" href="javascript:sendRmFileMsg(\'' + fileRealName + '\')" title="删除"><i class="fa fa-trash"></i></a>' + '</td>' + '</tr>';
			}
		}
	}
	$("#fileBody").empty();
	$("#fileBody").append(fileHtml);
}

//处理返回的路径信息，修改显示的路径
function showPath(data) {
	$('#path').val(data);
}

//发送获取目录列表的msg
function sendListFileMsg() {
	resultString = "";
	msgnum = 10;
	//清空列表
	$("#fileBody").empty();

	var data = "ls -F\n";
	var stdinMsg = {
		"MsgType" : 2,
		"Content" : stringToByte(data),
	};
	filesocket.send(JSON.stringify(stdinMsg));
}

//发送切换目录的msg
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

//发送获取当前路径的msg
function sendPwdMsg() {
	resultString = "";
	msgnum = 30;
	//显示路径前清空列表
	$("#fileBody").empty();

	var data = "pwd\n";
	var stdinMsg = {
		"MsgType" : 2,
		"Content" : stringToByte(data),
	};
	filesocket.send(JSON.stringify(stdinMsg));
}

//发送创建文件夹的msg
function sendMkdirMsg() {
	$('#newdir').val("");
	layer.open({
		type : 1,
		content : $('#createdir-templat'),
		title : '新建文件夹',
		btn : ['新建', '取消'],
		yes : function(index, layero) {
			layer.close(index);
			var dirName = $('#newdir').val();
			resultString = "";
			msgnum = 40;
			//创建文件夹前清空列表
			$("#fileBody").empty();
			var data = "mkdir " + dirName + "\n";
			var stdinMsg = {
				"MsgType" : 2,
				"Content" : stringToByte(data),
			};
			filesocket.send(JSON.stringify(stdinMsg));
		}
	});

}

//发送删除文件的msg
function sendRmFileMsg(fileName) {
	$('#newdir').val("");
	layer.open({
		type : 1,
		title : '确认删除文件',
		btn : ['确认', '取消'],
		yes : function(index, layero) {
			layer.close(index);
			resultString = "";
			msgnum = 50;
			//创建文件夹前清空列表
			$("#fileBody").empty();
			var data = "rm -f " + fileName + "\n";
			var stdinMsg = {
				"MsgType" : 2,
				"Content" : stringToByte(data),
			};
			filesocket.send(JSON.stringify(stdinMsg));
		}
	});

}

//发送保持连接的msg
function keepFileSocketAlive() {
	var keepAliveMsg = {
		"MsgType" : 3,
		"Content" : stringToByte("js---keepalive"),
	};
	filesocket.send(JSON.stringify(keepAliveMsg));
}

//下载容器内的文件
function downloadContainerFile(fileName) {
	var path = $('#path').val();
	var nodeIp = $('#containerFileIp').val();
	var containerId = $('#containerFileId').val();
	location.href = ctx + "/service/downloadFile?" + "nodeIp=" + nodeIp + "&containerId=" + containerId + "&path=" + path + "&fileName=" + encodeURIComponent(fileName);
}

//上传文件到容器中
function fileUpload() {
	layer.open({
		type : 1,
		content : $('#upload-template'),
		title : '上传文件',
		btn : ['上传', '取消'],
		yes : function(index, layero) {
			var formData = new FormData($("#form1")[0]);
			var fileName = document.getElementById("file").value;
			var arr = fileName.split("\\");
			fileName = arr[arr.length - 1];
			var flag = 0;
			$("#fileBody tr").each(function(index, domEle) {
				$(domEle).find("span").each(function(index, data) {
					if (fileName == $(data).html()) {
						flag = 1;
					}
				});
			});
			if (flag == 0) {
				layer.closeAll();
				$('.progress-bar-info').text("文件上传中...");
				$('#myModal').modal('show');
				UpladFile();
			} else {
				layer.msg('存在同名的文件！', {
					icon : 7,
					btn : ['確定', '取消'],
					time : 0, // 不自动关闭
					yes : function(index) {
						layer.closeAll();
					}
				});
			}
		}
	});
};
function UpladFile() {
	var path = $('#path').val();
	var nodeIp = $('#containerFileIp').val();
	var containerId = $('#containerFileId').val();

	$('#currentContainerId').val(containerId);
	$('#currentContainerIp').val(nodeIp);
	$('#currentFilePath').val(path);

	var fileObj = document.getElementById("file").files[0];
	// js 获取文件对象
	var FileController = ctx + '/service/uploadFile';
	// 接收上传文件的后台地址

	// FormData 对象
	var form = new FormData($("#form1")[0]);
	// XMLHttpRequest 对象
	var xhr = new XMLHttpRequest();
	xhr.open("post", FileController, true);
	xhr.onload = function() {
	};
	xhr.onreadystatechange = function() {//服务器返回值的处理函数，此处使用匿名函数进行实现
		if (xhr.readyState == 4 && xhr.status == 200) {//
			var responseText = xhr.responseText;
			$('#myModal').modal('hide');
			sendPwdMsg();
		}
	};
	xhr.upload.addEventListener("progress", progressFunction, false);
	xhr.send(form);
}

function progressFunction(evt) {
	if (evt.lengthComputable) {
		var progress = Math.round(evt.loaded / evt.total * 90) + "%";
		if (progress == "100%") {
			$('#myModal').modal('hide');
		} else {
			$('.progress-bar-info').text("文件上传中" + progress + "...");
		}
	}
}
