$(document).ready(function() {
	creatable(null, null);
	var oFrm = document.getElementById('shellinabox');
	oFrm.onreadystatechange = function() {
		if ( this.readyState == 'complete') {
			onComplete();
		}
	}
	$('#scp-path').bind('keypress',function(event){
		if (event.keyCode == "13") {
			var dirName = $('#scp-path').val();
//			var dirNameArr = $('#scp-path').val().split("/");
//			var dirName = dirNameArr[dirNameArr.length-1];
			creatable(true, dirName);
        }
    });
});
function creatable(){
	alert("test")
}
function creatable(isDir, dirName) {
	$(".chkAll").prop('checked', $(this).is(":checked"));
	if (null == isDir || true == isDir) {
		var tbody = "";
		var context = $('#mybody');
		var param = "";
		if (null == dirName) {
			dirName = "";
		}
		param = "dirName=" + encodeURIComponent(dirName);
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
				for (i in data.fileList) {
					var fileInfo = JSON.stringify(data.fileList[i]);
					fileInfo = eval("(" + fileInfo + ")");
					if (fileInfo.fileName == '..') {
						tbody += '<tr class="vol_list" style="cursor:pointer">'
								+ '<td style="width: 5%;text-indent: 14px;">'
								+ '</td>'
								+ '<td style="width: 25%; text-indent: 13px;"  onclick=creatable('
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
								+ '<td style="width: 15%; text-indent: 2px;">'
								+ fileInfo.size
								+ '</td>'
								+ '<td style="width: 30%; text-indent: 0px;"></td>'
								+ '<td style="width: 15%;text-indent: 0px;"></td>'
								+ '</tr>';
					} else {
						tbody += '<tr class="vol_list" style="cursor:pointer">'
								+ '<td style="width: 5%;text-indent: 14px;">'
								+ '<input type="checkbox" class="chkItem" name="downfiles" value="'
								+ fileInfo.fileName
								+ '" >'
								+ '</td>'
								+ '<td style="width: 25%;text-indent: 13px;" onclick=creatable('
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
								+ '<td style="width: 15%; text-indent: 8px;">'
								+ fileInfo.size
								+ 'KB</td>'
								+ '<td style="width: 30%; text-indent: 0px;">'
								+ fileInfo.modifiedTime
								+ '</td>'
								+ '<td style="width: 15%;text-indent: 30px;">'
								+ '<a class="deleteButton" style="text-indent: 0px;" href="javascript:void(0)" onclick="delfile(this)"  fileName="'
								+ fileInfo.fileName
								+ '"> <i class="fa fa-trash fa-lg"></i></a>'
						if (true == fileInfo.dir) {
						} else if (true == fileInfo.link) {
						} else {
							tbody += '<a class="downloadButton" style="text-indent: 1px;" href="javascript:void(0)" onclick="downloadFile(this)"  fileName="'
									+ fileInfo.fileName
									+ '"> <i class="fa fa-download fa-lg"></i></a>'
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
	var fileName = $(obj).attr("fileName");
	layer.open({
		title : '删除文件',
		content : '确定删除该文件？',
		btn : [ '确定', '取消' ],
		yes : function(index, layero) {
			$.ajax({
				type : "GET",
				url : ctx + "/service/delFile.do?fileNames=" + fileName,
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
				url : "" + ctx + "/service/delFile.do?fileNames=" + fileNames,
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
				url : ctx + "/service/createFile.do?dirName=" + dirName,
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
	location.href = ctx + "/service/downloadFile?downfiles=" + fileNames;
}

/**
 * 下载文件
 */
function downloadFile(obj) {
	var fileName = $(obj).attr("fileName");
	location.href = ctx + "/service/downloadFile?downfiles=" + fileName;
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
				up(formData, flag);
			} else {
				layer.open({
					type : 1,
					content : '存在同名的文件，确定要覆盖吗？',
					title : '确认',
					btn : [ '确认', '取消' ],
					yes : function(index, layero) {
						layer.closeAll();
						up(formData, flag);
					}
				});
			}
		}
	})
};

function up(formData, flag) {
	$('#myModal').modal('show');
	$.ajax({
		type : 'POST',
		url : ctx + '/service/uploadFile',
		data : formData,
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(data) {
			var data = eval("(" + data + ")");
			if ("200" == data.status && flag == 0) {
				$('#myModal').modal('hide');
				creatable(null, ".");
				$('#hasUsed').html(data.used);
				layer.closeAll();
			} else if ("500" == data.status) {
				$('#myModal').modal('hide');
				layer.open({
					type : 1,
					content : '上传失败，文件大小超过卷组可用大小！',
					title : '上传失败',
					btn : [ '确定' ],
					yes : function(index, layero) {
						refreshtable();
						layer.closeAll();
					}
				});
			} else {
				$('#myModal').modal('hide');
				refreshtable();
				$('#hasUsed').html(data.used);
				layer.closeAll();
			}
		}
	});
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
