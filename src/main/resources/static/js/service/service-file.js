var filesocket, alive;
function dropdownFile(object) {
	var num = 0;
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
		alive = setInterval("keepFIleSocketAlive()",3000);
		listFile("/");
	};

	//接收到消息的回调方法
	filesocket.onmessage = function(event) {
		num = num + 1;
		if (num % 3 == 0) {
			var reader = new FileReader();
			reader.readAsBinaryString(event.data);
			//blob
			reader.onloadend = function() {
				var object = eval('(' + reader.result + ')');
				/*layer.msg(Base64(object.content), {
					icon : 1
				});*/
				var fileObj = Base64(object.content);
				var fileObjArray = fileObj.replace(/\s+/g, ',').split(",");
				var fileHtml = "";
				for(var i=0;i<fileObjArray.length;i++){
					if(fileObjArray[i]=="" || fileObjArray[i] == null){
						fileHtml +='';
					}else{
						var fileName = fileObjArray[i];
						fileHtml += '<tr>'
							+'<td style="width: 5%;text-indent: 14px;"><input type="checkbox"></td>'
							+'<td class="fileName" style="width: 45%;">';
						if(fileName.indexOf("/") != -1){
							fileHtml += '<i class="fa fa-folder"></i>';
						}else if(fileName.indexOf("*") != -1){
							fileHtml += '<i class="fa fa-file-code-o"></i>';
						}else if(fileName.indexOf("@") != -1){
							fileHtml += '<i class="fa fa-share-square"></i>';
						}else{
							fileHtml += '<i class="fa fa-file"></i>';
						}
						fileHtml +=fileName+'</td>';
						if(fileName.indexOf("/") != -1){
							fileHtml +='<td style="width: 40%;"></td>'
								+'</tr>';
						}else{
							fileHtml +='<td style="width: 40%; text-indent:6px">'
								+'<a onclick="uploadOneFile()" title="上传文件"><i class="fa fa-upload"></i></a>' 
								+'<a onclick="downloadOneFile()" title="导出文件"><i class="fa fa-download"></i>'
								+'<input hidden="true" value="" id="downfilepath"/>'
								+'</a>'
								+'<a id="deleteButton" class="no-drop"'
								+'href="javascript:delOneFile()" title="删除">'
								+'<i id="deleteButtonfile" class="fa fa-trash"></i>'
								+'</a></td>'
								+'</tr>';
						}
							
					}
				}
				$("#fileBody").append(fileHtml);
				console.log(Base64(object.content));
			};
		}
	};

	//连接关闭的回调方法
	filesocket.onclose = function() {
		layer.msg("filesocket连接关闭", {
			icon : 1
		});
	};

	//监听窗口关闭事件，当窗口关闭时，主动去关闭filesocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
	window.onbeforeunload = function() {
		if(alive != null)clearInterval(alive);
		closefilesocket();
	};

}

//发送获取列表message
function listFile(path) {
	var data = "ls -F " + path + "\n";
	var stdinMsg = {
		"MsgType" : 0,
		"Content" : stringToByte(data),
	};
	filesocket.send(JSON.stringify(stdinMsg));
}

function keepFIleSocketAlive() {
	var keepAliveMsg = {
		"MsgType" : 3,
		"Content" : stringToByte("js---keepalive"),
	};
	filesocket.send(JSON.stringify(keepAliveMsg));
}
