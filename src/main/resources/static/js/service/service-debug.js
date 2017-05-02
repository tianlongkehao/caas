// 删除某一行
function delfile(obj) {
	var hostkey = document.getElementById("hostkey").value;
	var fileName = $(obj).attr("fileName");
	layer.open({
		title : '删除文件',
		content : '确定删除该文件？',
		btn : ['确定', '取消'],
		yes : function(index, layero) {
			$.ajax({
				type : "GET",
				url : ctx + "/service/delFile.do?fileNames=" + encodeURIComponent(fileName) + "&hostkey=" + encodeURIComponent(hostkey),
				success : function(data) {
					var data = eval("(" + data + ")");
					if (data.status == "400") {
						failedMSG("删除失败", true);
						return;
					}
					creatable(null, "./");
				}
			});
			layer.close(index);
		}
	});
}

// 批量删除
function delfiles() {
	var hostkey = document.getElementById("hostkey").value;
	var path = $('#downfilepath').val();
	obj = document.getElementsByName("downfiles");
	var fileNames = [];
	for (k in obj) {
		if (obj[k].checked) {
			fileNames.push(obj[k].value);
		}
	}
	layer.open({
		title : '删除文件',
		content : '确定删除选中的文件？',
		btn : ['确定', '取消'],
		yes : function(index, layero) {
			layer.close(index);
			if ("" == fileNames) {
				failedMSG("没有文件被选中", true);
				return;
			}
			$.ajax({
				url : "" + ctx + "/service/delFile.do?fileNames=" + encodeURIComponent(fileNames) + "&hostkey=" + encodeURIComponent(hostkey),
				success : function(data) {
					var data = eval("(" + data + ")");
					if (data.status == "400") {
						failedMSG("删除失败", true);
						return;
					}
					creatable(null, "./");
				}
			});
		}
	});
}

// 新建文件夹
function createdir() {
	var hostkey = document.getElementById("hostkey").value;
	layer.open({
		type : 1,
		content : $('#createdir-templat'),
		title : '新建文件夹',
		btn : ['新建', '取消'],
		yes : function(index, layero) {
			layer.close(index);
			var dirName = $('#newdir').val();
			$.ajax({
				type : "POST",
				url : ctx + "/service/createFile.do?dirName=" + dirName + "&hostkey=" + encodeURIComponent(hostkey),
				success : function(data) {
					var data = eval("(" + data + ")");
					if (data.status == "500") {
						failedMSG("新建文件夹失败", true);
						return;
					}
					creatable(null, ".");
				}
			});
		}
	});
}

/**
 * 批量下载文件
 */
function downloadFiles() {
	obj = document.getElementsByName("downfiles");
	var hostkey = document.getElementById("hostkey").value;
	var fileNames = [];
	for (k in obj) {
		if (obj[k].checked) {
			fileNames.push(obj[k].value);
		}
	}
	if ("" == fileNames) {
		failedMSG("请选择需要下载的文件", false);
		return;
	}
	location.href = ctx + "/service/downloadFile?downfiles=" + fileNames + "&hostkey=" + encodeURIComponent(hostkey);
}

/**
 * 下载文件
 */
function downloadFile(obj) {
	var hostkey = document.getElementById("hostkey").value;
	var fileName = $(obj).attr("fileName");
	window.open(ctx + "/service/downloadFile?downfiles=" + fileName + "&hostkey=" + encodeURIComponent(hostkey));
}

/**
 * 上传文件选择窗口
 */
function fileUpload() {
	$('#file').val("");
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
			$("#mybody tr").each(function(index, domEle) {
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
				//				upload(formData);
				//				getUploadProgress();
				UpladFile();
			} else {
				layer.msg('存在同名的文件，确定要覆盖吗？', {
					icon : 7,
					btn : ['確定', '取消'],
					time : 0, // 不自动关闭
					yes : function(index) {
						layer.closeAll();
						$('.progress-bar-info').text("文件上传中...");
						$('#myModal').modal('show');
						//						upload(formData);
						//						getUploadProgress();
						UpladFile();
					}
				});
			}
		}
	});
};

//上传文件的方法
function upload(formData) {
	$.when(uploadAjax(formData)).done(function(data) {
		var data = eval("(" + data + ")");
		if ("200" == data.status) {
			//        	$('#myModal').modal('hide');
			//			creatable(null, ".");
			//			layer.closeAll();
		} else {
			$('#myModal').modal('hide');
			failedMSG("文件上传失败！", true);
		}
	});
}

//上传文件的ajax异步请求
function uploadAjax(formData) {
	var defer = $.Deferred();
	$.ajax({
		type : 'POST',
		url : ctx + '/service/uploadFile',
		async : true,
		data : formData,
		cache : false,
		contentType : false,
		processData : false,
		success : function(data) {
			defer.resolve(data);
		}
	});
	return defer.promise();
}

//获取文件上传的进度
function getUploadProgress() {
	$('.progress-bar-info').text("文件转送至容器中...");
	$.when(getUploadProgressAjax()).done(function(data) {
	});
}

//获取文件上传的进度的ajax请求
function getUploadProgressAjax() {
	var defer = $.Deferred();
	var uuid = $('#uuid').val();
	$.ajax({
		type : 'GET',
		async : true,
		url : ctx + '/service/getUploadProgress?uuid=' + uuid,
		success : function(data) {
			var data = eval("(" + data + ")");
			if ("200" == data.status) {
				$('.progress-bar-info').text("文件转送至容器中" + data.progress + "...");
				if (data.progress != "100%") {
					setTimeout("getUploadProgressAjax()", 500);
				} else {
					layer.closeAll();
					$('#myModal').modal('hide');
					creatable(null, ".");
					//					defer.resolve(data);
				}
			} else {
				$('#myModal').modal('hide');
				failedMSG("文件转送进度获取失败！", true);
			}

		}
	});
	return defer.promise();

}



function generateUUID() {
	var d = new Date().getTime();
	var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		var r = (d + Math.random() * 16) % 16 | 0;
		d = Math.floor(d / 16);
		return (c == 'x' ? r : (r & 0x7 | 0x8)).toString(16);
	});
	return uuid;
};

function UpladFile() {
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
		// alert("上传完成!");
	};

	xhr.upload.addEventListener("progress", progressFunction, false);
	xhr.send(form);
}

function progressFunction(evt) {
	//	var progressBar = document.getElementById("progressBar");
	//	var percentageDiv = document.getElementById("percentage");
	if (evt.lengthComputable) {
		//		progressBar.max = evt.total;
		//		progressBar.value = evt.loaded;
		var progress = Math.round(evt.loaded / evt.total * 100) + "%";
		$('.progress-bar-info').text("文件上传中" + progress + "...");
		if (evt.loaded == evt.total) {
			setTimeout("getUploadProgress()", 1000);
		}
	}
}

