$(document).ready(function() {
	creatable(null, null);
});
function EnterPress(e) { // 传入 event
	var e = e || window.event;
	if (e.keyCode == 13) {
		var dictionary = document.getElementById("scp-path").value;
		creatable(true,dictionary);
	}
} 

function creatable(isDir, dirName) {
	var path = document.getElementById("path").value;
	var hostkey = document.getElementById("hostkey").value;
	$(".chkAll").prop('checked', $(this).is(":checked"));
	if (null == isDir || true == isDir) {
		var tbody = "";
		var context = $('#mybody');
		var param = "";
		if (null == dirName) {
			dirName = "";
		}
		param = "dirName=" + encodeURIComponent(dirName) + "&path=" + encodeURIComponent(path) + "&hostkey=" + encodeURIComponent(hostkey);
		context.empty();

		$.ajax({
			type : "GET",
			url : ctx + "/service/listFile?" + param,
			success : function(data) {
				// alert(data);
				var data = eval("(" + data + ")");
				if (data.status == "400") {
					failedMSG("没有找到相应的目录", true);
					return;
				}
				$("#scp-path").val(data.path);
				$("#path").val(data.path);
				for (i in data.fileList) {
					var fileInfo = JSON.stringify(data.fileList[i]);
					fileInfo = eval("(" + fileInfo + ")");
					if (fileInfo.fileName == '..') {
						tbody += '<tr class="vol_list" style="cursor:pointer">'
								+ '<td style="width: 5%;text-indent: 14px;">'
								+ '</td>'
								+ '<td style="width: 25%; text-indent: 22px;"  onclick=creatable('
								+ fileInfo.dir
								+ ',"'
								+ fileInfo.fileName
								+ '") >'
								+ '<a hrer="">'
						if (true == fileInfo.dir) {
							tbody += '<img src="/images/img-file.png" >'
						} else {
							tbody += '<img src="/images/file-f.png" >'
						}
						tbody += '<span style="margin-left:5px"  >'
								+ fileInfo.fileName
								+ '</span>'
								+ '</a>'
								+ '</td>'
								+ '<td style="width: 15%; text-indent: 61px;">'
								+ fileInfo.size
								+ '</td>'
								+ '<td style="width: 30%; text-indent: 152px;"></td>'
								+ '<td style="width: 15%;text-indent: 131px;"></td>'
								+ '</tr>';
					} else {
						tbody += '<tr class="vol_list" style="cursor:pointer">'
								+ '<td style="width: 5%;text-indent: 14px;">'
								+ '<input type="checkbox" class="chkItem" name="downfiles" value="'
								+ fileInfo.fileName
								+ '" >'
								+ '</td>'
								+ '<td style="width: 25%;text-indent: 22px;" onclick=creatable('
								+ (fileInfo.dir || fileInfo.link)
								+ ',"'
								+ fileInfo.fileName
								+ '") >'
								+ '<a hrer="">'
						if (true == fileInfo.dir) {
							tbody += '<img src="/images/img-file.png" >'
						} else if (true == fileInfo.link) {
							tbody += '<img src="/images/image-link.png" >'
						} else {
							tbody += '<img src="/images/file-f.png" >'
						}
						tbody += '<span style="margin-left:5px"  >'
								+ fileInfo.fileName
								+ '</span>'
								+ '</a>'
								+ '</td>'
								+ '<td style="width: 15%; text-indent: 56px;">'
								+ fileInfo.size
								+ 'KB</td>'
								+ '<td style="width: 30%; text-indent: 148px;">'
								+ fileInfo.modifiedTime
								+ '</td>'
								+ '<td style="width: 15%;text-indent: 123px;">'
								+ '<a class="deleteButton" style="text-indent: 0px;" href="javascript:void(0)" onclick="delfile(this)"  fileName="'
								+ fileInfo.fileName
								+ '"> <i class="fa fa-trash fa-lg fa-i"></i></a>'
						if (true == fileInfo.dir) {
						} else if (true == fileInfo.link) {
						} else {
							tbody += '<a class="downloadButton" style="text-indent: 1px;" href="javascript:void(0)" onclick="downloadFile(this)"  fileName="'
									+ fileInfo.fileName
									+ '"> <i class="fa fa-download fa-lg fa-i"></i></a>'
						}
						tbody += '</td>' + '</tr>';
					}

					$('#downfilepath').val(fileInfo.path);
				}
				$('#mybody').html($(tbody));
			}
		});
	}
}
// 删除某一行
function delfile(obj) {
	var hostkey = document.getElementById("hostkey").value;
	var fileName = $(obj).attr("fileName");
	layer.open({
		title : '删除文件',
		content : '确定删除该文件？',
		btn : [ '确定', '取消' ],
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
	})
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
		btn : [ '确定', '取消' ],
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
			})
		}
	})
}

// 新建文件夹
function createdir() {
	var hostkey = document.getElementById("hostkey").value;
	layer.open({
		type : 1,
		content : $('#createdir-templat'),
		title : '新建文件夹',
		btn : [ '新建', '取消' ],
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
	})
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
 * 上传文件
 */
function fileUpload() {
	$('#file').val("");
	layer.open({
		type : 1,
		content : $('#upload-template'),
		title : '上传文件',
		btn : [ '上传', '取消' ],
		yes : function(index, layero) {
			var formData = new FormData($("#form1")[0]);
			var fileName = document.getElementById("file").value;
			var arr = fileName.split("\\");
			fileName = arr[arr.length-1];
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
		        $.when(up(formData, flag)).done(function(data){
		        	$('#myModal').modal('hide');
		        });
			} else {
				layer.msg('存在同名的文件，确定要覆盖吗？', {
					icon : 7,
					btn : [ '確定', '取消' ],
					time : 0, // 不自动关闭
					yes : function(index) {
						layer.closeAll();
						$('.progress-bar-info').text("文件上传中...");
						$('#myModal').modal('show');
				        $.when(up(formData, flag)).done(function(data){
				        	$('#myModal').modal('hide');
				        });
					}
				});
			}
		}
	})
};

function up(formData, flag) {
	var defer = $.Deferred();
	$.ajax({
		type : 'POST',
		url : ctx + '/service/uploadFile',
		data : formData,
//		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(data) {
			defer.resolve(data);
			var data = eval("(" + data + ")");
			if ("200" == data.status) {
				creatable(null, ".");
				$('#hasUsed').html(data.used);
				layer.closeAll();
			} else {
				failedMSG("文件上传失败！", true);
			}
		}
	});
	return defer.promise();
}
//导出container为image
function saveAsImage(containerId, nodeName) {
    var version = $('#version').val().trim();
    if(version.length == 0){
		layer.tips('对不起，镜像版本名不能为空！','#version',{tips: [1, '#3595CC']});
		$('#version').focus();
    	return;
    }
    if(version.length > 128){
		layer.tips('对不起，镜像版本名过长！','#version',{tips: [1, '#3595CC']});
		$('#version').focus();
    	return;
    }
    reg=/^[a-zA-Z0-9_.-]+$/;     
    if(!reg.test(version)){    
		layer.tips('对不起，您输入的镜像版本格式不正确！','#version',{tips: [1, '#3595CC']});
		$('#version').focus();
		return;
    }
    $('.progress-bar-info').text("容器保存中...");
	$('#myModal').modal('show');
    $.when(getdata(containerId, nodeName)).done(function(data){
    	$('#myModal').modal('hide');
    });
}

function getdata(containerId, nodeName){
	var defer = $.Deferred();
    var imageName = $('#imageName').val();
    var version = $('#version').val().trim();
    $.ajax({
		url : ctx + "/service/saveAsImage",
		type: "POST",
		data:{"containerId":containerId,"nodeName":nodeName,"imageName":imageName,"version":version},
		success : function(data) {
			defer.resolve(data);
			data = eval("(" + data + ")");
			if (data.status=="200") {
				layer.msg('容器已成功保存为镜像', {
					icon : 1,
					btn : [ '確定' ],
					time : 0, // 不自动关闭
					yes : function(index) {
						layer.closeAll();
					}
				});
			}else if(data.status=="400") {
				failedMSG("对不起，仓库中已存在该版本名！", false);
			} else if(data.status=="401") {
				failedMSG("对不起，container保存为本地镜像失败！", false);
			} else if(data.status=="402") {
				failedMSG("对不起，本地镜像推送到仓库时失败！", false);
			} else if(data.status=="403") {
				failedMSG("对不起，获取数据库中原始镜像的信息失败！", false);
			}
		}
	});
	return defer.promise();
}



// 弹出失败消息
function failedMSG(title, flag) {
	layer.msg(title, {
		icon : 5,
		btn : [ '確定' ],
		time : 0, // 不自动关闭
		yes : function(index) {
			layer.close(index);
			if (flag == true) {
				// 刷新页面
				creatable(null, "/");
			}
		}
	});
}
