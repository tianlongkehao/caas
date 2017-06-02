var jsonData = {

};

$(document).ready(function () {
  hasUsed();
  creatable(null,null,null);

 /**
  * 上传文件
  */
 $("#fileUpload").click(function(){
	 		$('#file').val("");
	    layer.open({
	    	type:1,
	    	content:$('#environment-templat'),
	    	title:'上传文件',
	    	btn:['保存','取消'],
	    	yes: function(index, layero){
	    			var path = $('#downfilepath').val();
	        	$('#path').val(path);
	        	var formData = new FormData($( "#form1" )[0]);
	        	var fileName = document.getElementById("file").value;
	        	var flag =0;
	         $("#mybody tr").each(function (index, domEle){
	    				$(domEle).find("span").each(function(index,data){
	    					if(fileName == $(data).html()){
	    						flag=1;
	    						}
							});
	         			});
	         if(flag==0){
//	        	  layer.closeAll();
	        	  up(formData,flag);
	         }else{
							layer.open({
		 						type:1,
		 						content:'卷组中存在同名的文件，您确定要覆盖吗？',
		 						title:'确认',
		 						btn:['确认','取消'],
		 						yes: function(index, layero){
		 							layer.close(index);
		 							up(formData,flag);
		 						}
							});
	         			}
	    			},
	      cancal: function (index, layero){
							cancleUploadFile();
				 }
	    	});
	});
	$("#storageReloadBtn").click(function(){
		window.location.reload();
	});
});

	function up(formData,flag){
        var url = ctx + '/upload'; // 接收上传文件的后台地址
        xhr = new XMLHttpRequest();  // XMLHttpRequest 对象
        xhr.open("post", url, true); //post方式，url为服务器请求地址，true 该参数规定请求是否异步处理。
        xhr.onload = uploadComplete; //请求完成
        xhr.onerror =  uploadFailed; //请求失败
        xhr.upload.onprogress = progressFunction;//【上传进度调用方法实现】
        xhr.upload.onloadstart = function(){//上传开始执行方法
            ot = new Date().getTime();   //设置上传开始时间
            oloaded = 0;//设置上传开始时，以上传的文件大小为0
        };
        xhr.send(formData); //开始上传，发送form数据
   } //

	//上传进度实现方法，上传过程中会频繁调用该方法
    function progressFunction(evt) {

         var progressBar = document.getElementById("progressBar");
         var percentageDiv = document.getElementById("percentage");
         // event.total是需要传输的总字节，event.loaded是已经传输的字节。如果event.lengthComputable不为真，则event.total等于0
         if (evt.lengthComputable) {//
             progressBar.max = evt.total;
             progressBar.value = evt.loaded;
             percentageDiv.innerHTML = Math.round(evt.loaded / evt.total * 100) + "%";
         }

        var time = document.getElementById("time");
        var nt = new Date().getTime();//获取当前时间
        var pertime = (nt-ot)/1000; //计算出上次调用该方法时到现在的时间差，单位为s
        ot = new Date().getTime(); //重新赋值时间，用于下次计算

        var perload = evt.loaded - oloaded; //计算该分段上传的文件大小，单位b
        oloaded = evt.loaded;//重新赋值已上传文件大小，用以下次计算

        //上传速度计算
        var speed = perload/pertime;//单位b/s
        var bspeed = speed;
        var units = 'b/s';//单位名称
        if(speed/1024>1){
            speed = speed/1024;
            units = 'k/s';
        }
        if(speed/1024>1){
            speed = speed/1024;
            units = 'M/s';
        }
        speed = speed.toFixed(1);
        //剩余时间
        var resttime = ((evt.total-evt.loaded)/bspeed).toFixed(1);
        time.innerHTML = '，速度：'+speed+units+'，剩余时间：'+resttime+'s';
           if(bspeed==0)
            time.innerHTML = '上传已取消';
    }
    //上传成功响应
    function uploadComplete(evt,flag) {
     //服务断接收完文件返回的结果
    	var data = eval("("+evt.target.responseText+")");
		if("200"==data.status && flag==0){
				$('#myModal').modal('hide');
				refreshtable();
				$('#hasUsed').html(data.used);
				layer.closeAll();
		}else if ("500"==data.status) {
			$('#myModal').modal('hide');
			layer.open({
				type:1,
				content:'上传失败，文件大小超过卷组可用大小！',
				title:'上传失败',
				btn:['确定'],
				yes: function(index, layero){
					refreshtable();
					layer.closeAll();
				}
			 });
	}else {
		$('#myModal').modal('hide');
		refreshtable();
		$('#hasUsed').html(data.used);
		layer.closeAll();
		$("#progressBar").empty();
		   $("#progressBar").val(0);
	     $("#time").empty();
	}
}
//上传失败
function uploadFailed(evt) {
	alert("上传失败！");
}

//取消上传
function cancleUploadFile() {
	xhr.abort();
}


/**
 * 总用量
 */
function hasUsed() {
	var storageName = $('#storageName').html();
	var totalSize = $('#totalSize').html();
	$.ajax({
		type : "GET",
		url : ctx + "/hasUsed.do?storageName=/" + storageName + "&totalSize=" + totalSize,
		success : function(data) {
			data = eval('(' + data + ')');
			if ("400" != data.status) {
				var hasUsed = data.length;
				$('#hasUsed').html(hasUsed);
			}
		}
	});
}

/**
 * 刷新ceph列表
 */
function refreshtable(){
	var path=$('#downfilepath').val();
	creatable(null,path,null);
	hasUsed();
}

/**
 * 生成ceph的文件列表
 * @param isDir
 * @param path
 * @param dirName
 */
function creatable(isDir, path, dirName) {

	if (null == isDir || "true" == isDir) {
		var tbody = "";
		var context = $('#mybody');
		var param = "";
		var storageName = $('#storageName').html();
		if (null == path || null == path) {
			param = "path=&dirName=&storageName=/" + storageName + "/";
		} else {
			if (null == dirName) {
				dirName = "";
			} else {
				dirName += "/";
			}
			param = "path=" + path + "&dirName=" + dirName + "&storageName=/" + storageName + "/";
		}
		context.empty();

		$.ajax({
			type : "GET",
			url : ctx + "/listFile?" + param,
			success : function(data) {
				//alert(data);
				var data = eval("(" + data + ")");
				if (data.status == "500") {
					layer.alert("您没有权限浏览上一级内容");
					creatable(null, null, null);
					return;
				}
				if (data.status == "400") {
					layer.alert("对不起，没有找到相应的目录");
					return;
				}
				for (i in data.fileList) {
					var fileInfo = JSON.stringify(data.fileList[i]);
					fileInfo = eval("(" + fileInfo + ")");
					$('#downfilepath').val(fileInfo.path);
					fileInfo.path = encodeURI(fileInfo.path);
					fileInfo.fileNameEnc = encodeURI(fileInfo.fileName);
					//alert(fileInfo.day);
					if (fileInfo.fileName == '..') {
						tbody += '<tr class="vol_list" style="cursor:pointer">' + '<td style="width: 5%;text-indent: 14px;">' + '</td>' + '<td style="width: 25%; text-indent: 30px;"  onclick=creatable("' + fileInfo.dir + '","' + fileInfo.path + '","' + fileInfo.fileNameEnc + '") >' + '<a hrer="">';
						if (true == fileInfo.dir) {
							tbody += '<img src="' + ctx + '/images/img-file.png ">';
						} else {
							tbody += '<img src="' + ctx + '/images/file-f.png ">';
							;
						}
						tbody += '<span style="margin-left:5px"  >' + fileInfo.fileName + '</span>' + '</a>' + '</td>' + '<td style="width: 20%;">' + fileInfo.size + '</td>' + '<td style="width: 25%;"></td>' + '<td style="width: 10%;text-indent: 56px;"></td>' + '</tr>';
					} else {
						tbody += '<tr class="vol_list" style="cursor:pointer">' + '<td style="width: 5%;text-indent: 14px;">' + '<input type="checkbox" class="chkItem" name="downfiles" value="' + fileInfo.fileName + '" >' + '</td>' + '<td style="width: 25%;text-indent: 30px;" onclick=creatable("' + fileInfo.dir + '","' + fileInfo.path + '","' + fileInfo.fileNameEnc + '") >' + '<a hrer="">';
						if (true == fileInfo.dir) {
							tbody += '<img src="' + ctx + '/images/img-file.png ">';
						} else {
							tbody += '<img src="' + ctx + '/images/file-f.png ">';
							;
						}
						tbody += '<span style="margin-left:5px"  >' + fileInfo.fileName + '</span>' + '</a>' + '</td>' + '<td style="width: 20%;">' + fileInfo.size + 'KB</td>' + '<td style="width: 25%;">' + fileInfo.modifiedTime + '</td>' + '<td style="width: 10%;text-indent: 56px;">' + '<a class="deleteButton" href="javascript:void(0)" onclick="delfile(this)"  fileName="' + fileInfo.fileName + '"> <i class="fa fa-trash fa-lg"></i></a>' + '</td>' + '</tr>';
					}

				}
				$('#mybody').html($(tbody));
				//showDataTable();
			}
		});

	}
}


function showDataTable(){
	$('.dataTables-example').dataTable({
		"aoColumnDefs" : [ {
			"bSortable" : false,
			"aTargets" : [ 0, 4 ]
		} ],
		"aaSorting": [[ 3, "desc" ]]
	});
	$("#checkallbox").parent().removeClass("sorting_asc");

}
//解压文件
function unzipFile(){
	var path =$('#downfilepath').val();
	layer.open({
		type:1,
		content:'确定解压吗？',
		title:'解压文件',
		btn:['确定','取消'],
		yes: function(index, layero){
			var dirName=$('#newdir').val();
			var fileNames = [];
			obj = document.getElementsByName("downfiles");
		    for (k in obj) {
		        if (obj[k].checked) {
		        	fileNames.push(obj[k].value);
		        }
		    }

			$.ajax({
        		type: "GET",
           url: ctx + "/storage/unZipFile.do?path="+path+"&fileName="+fileNames+"&storageName=/"+storageName,
        	});
        	layer.close(index);
        	refreshtable();
		}
	});
}
//新建文件夹
function createdir() {
	var storageName = $('#storageName').html();
	var path = $('#downfilepath').val();
	layer.open({
		type : 1,
		content : $('#createdir-templat'),
		title : '新建文件夹',
		btn : ['新建', '取消'],
		yes : function(index, layero) {
			var dirName = $('#newdir').val();
			$.ajax({
				type : "POST",
				url : ctx + "/storage/createFile.do?path=" + path + "&dirName=" + dirName + "&storageName=/" + storageName,
				success : function(data) {
					var data = eval("(" + data + ")");
					if (data.status == "200") {
						layer.close(index);
						refreshtable();
					} else {
						layer.alert("创建失败");
						return;
					}
				}
			});
		}
	});
}



//删除某一行
function delfile(obj){
		var storageName=$('#storageName').html();
		var fileName=$(obj).attr("fileName");
		var path =$('#downfilepath').val();
		layer.open({
		        title: '删除卷组中的文件',
		        content: '确定删除卷组中的文件？',
		        btn: ['确定', '取消'],
		        yes: function(index, layero){
		        	$(obj).parent().parent().remove();
		        	//refservice/delete.do
		        	$.ajax({
		        		type: "GET",
		                url: ctx + "/storage/delFile.do?path="+path+"&fileNames="+fileName+"&storageName=/"+storageName,
		        	});
		        	layer.close(index);
		       	 hasUsed();
		        }
	 });
}
//批量删除
function delfiles(){
		var storageName=$('#storageName').html();
		var path =$('#downfilepath').val();
		obj = document.getElementsByName("downfiles");
		var fileNames = [];
	    for (k in obj) {
	        if (obj[k].checked) {
	        	fileNames.push(obj[k].value);
	        }
	    }
	 layer.open({
		 			 title: '删除卷组中的文件',
	        content: '确定删除卷组中的文件？',
	        btn: ['确定', '取消'],
	        yes: function(index, layero){
	        	layer.close(index);
	        	if(""==fileNames){
	        		alert("你总要选一个呀");
	        		return;
	        	}
				$.ajax({
					url:""+ctx+"/storage/delFile.do?path="+path+"&fileNames="+fileNames+"&storageName=/"+storageName,
					success:function(data){
						data = eval("(" + data + ")");
						if(data.status=="200"){
							layer.alert("服务已删除");
							refreshtable();
						}else{
							layer.alert("服务删除失败，请检查服务器连接");
						}

					}
				});
	        }
	 });

}

function loadStorageList(){
	var url = ""+ctx+"/service/storageList";
	var json = {pageable:"pageable"};

	jqList.query(url,json, function(data){
		if(data.status == 200) {
        	var itemsHtml = '';
        	var len = data.storages.length;
        	if(len == 0){
        		if(searchFlag) {
        			itemsHtml = '<tr><td colspan="4">未找到匹配的数据...</td></tr>';
        		}else {
        			itemsHtml = '<tr><td colspan="4">无数据...</td></tr>';
        		}
        	}
        	for(var i=0; i<len; i++){
        		(function(){
        			var storage = data.storages[i];
        			var useType = storage.useType ==1 ? "未使用" : "使用" ;

        			if(storage.mountPoint == null || storage.mountPoint == ""){
        				storage.mountPoint = "未挂载";
        			}
        			itemsHtml += ' <tr class="ci-listTr" style="cursor:auto">'+
        							' <td style="width: 15%; text-indent:22px;" id = "storageName">'+
        							'<a href="'+ctx+'/service/storage/detail/'+storage.id+'" title="查看详细信息">'+storage.storageName +'</a>'+
        							'</td>'+
        							' <td style="width: 15%;" class="cStatusColumn">' +
        								useType +
        							' </td>'+
        							//' <td style="width: 10%;">' + storage.format + '</td>'+
        							' <td style="width: 15%;word-wrap:break-word;word-break:break-all;">' + storage.mountPoint + '</td>'+
        							' <td style="width: 12%;">' + storage.storageSize + ' M</td>'+
        							' <td style="width: 15%;">' + storage.createDate + '</td>'+
        							' <td style="width: 20%;">' +
        								' <span class="btn btn-primary format formatStorage"> 格式化 </span>'+
        								' <span class="btn btn-primary dilation dilatationStorage" storageId="'+storage.id +'" storageSize="'+ storage.storageSize +'" storageName="' + storage.storageName +'">扩容</span>'+
        								' <span class="btn btn-primary delete deleteStorage" storageId="'+storage.id +'"> 删除 </span>'+
        							'</td>'+
        						+'</tr>';
        		})(i);
        	}
        	$('tbody #storageList').html(itemsHtml);
		}
	});
}
var datas = [ {
	level:"1",
	imgtype : "box",
	filename : "vol1",
	size : "10M",
	modDate : "2016-12-10",
	val : [ {
		level:"2",
		imgtype : "box",
		filename : "1",
		size : "10M",
		modDate : "2016-12-10",
		val:[
		{
			level:"3",
			imgtype : "file",
			filename : "1",
			size : "10M",
			modDate : "2016-12-10"}
		     ]
	}, {
		level:"2",
		imgtype : "file",
		filename : "2",
		size : "30M",
		modDate : "2016-12-10"
	}, {
		level:"2",
		imgtype : "file",
		filename : "3",
		size : "50M",
		modDate : "2016-12-10"
	} ]
}, {
	level:"1",
	imgtype : "box",
	filename : "vol2",
	size : "20M",
	modDate : "2016-12-10",
	val : [ {
		level:"2",
		imgtype : "file",
		filename : "4",
		size : "20M",
		modDate : "2016-12-10"
	}, {
		level:"2",
		imgtype : "file",
		filename : "5",
		size : "30M",
		modDate : "2016-12-10"
	}, {
		level:"2",
		imgtype : "file",
		filename : "6",
		size : "50M",
		modDate : "2016-12-10"
	} ]
},

];
/* 存储卷 */
function expand(obj) {

	var vol_name = $(obj).find("span.volName")[0].innerHTML;

	var _table = document.getElementById('table-vol');
	var _tbody = _table.getElementsByTagName('tbody')[0];
	_tbody.innerHTML = "";

	var _tr = "";
	for (var i = 0; i < datas.length; i++) {
		if (datas[i].filename == vol_name) {
			for (var j = 0; j < datas[i].val.length; j++) {
				_tr = '<tr class="vol_list">'
						+ '<td style="text-indent: 14px;"><input type="checkbox" class="chkItem" name="vol_chk" value="" ></td>'
						+ '<td style="width: 40%;"><a href="" onclick="expand();"><img class="imgSrc" src=""><span style="margin-left:5px">'
						+ datas[i].val[j].filename + '</span></a></td>'
						+ '<td style="width: 30%;">' + datas[i].val[j].size
						+ '</td>' + '<td style="width: 26%;">'
						+ datas[i].val[j].modDate + '</td></tr>';
				$("#tbody-vol").append(_tr);
				if(datas[i].val[j].imgtype == "box"){
					$("#tbody-vol").find("img.imgSrc")[j].src = ctx +"/images/img-file.png";
				}else{
					$("#tbody-vol").find("img.imgSrc")[j].src = ctx +"/images/file-f.png";
				}
			}



			$("#allboxs").addClass("hide");

			var valPath = '<div class="" style="line-height:40px; id="nextboxs">'
					+ '<ul style="margin:0px">'
					+ '<li style="float:left"><a href="#">'
					+ '返回上一级'
					+ '</a><span style="padding:5px">'
					+ '|'
					+ '</span><a href="">'
					+ '全部文件'
					+ '</a><span style="padding:5px">&gt;</span></li>'
					+ '<li style="float:left">'
					+ datas[i].filename
					+ '</li>'
					+ '</ul>' + '</div>';

			$("#val-path").append(valPath);
		}
	}

}

/*function volList(){
	for (var i = 0; i < datas.length; i++) {
		var tableTr = '<tr class="vol_list">'
				+ '<td style="text-indent: 14px;"><input type="checkbox" class="chkItem" name="vol_chk" value="" ></td>'
				+ '<td style="width: 40%;"><a id="aaa" onclick="expand(this);">';
		if(datas[i].imgtype == "box"){
			tableTr +='<img class="img" class="imgSrc" src='+ctx+'/images/file-f.png >'
		}else{
			tableTr +='<img class="img" class="imgSrc" src='+ctx+'/images/img-file.png >'
		}
		tableTr +='<span class="volName" style="margin-left:5px">'
				+ datas[i].filename + '</span></a></td>'
				+ '<td style="width: 30%;">' + datas[i].size
				+ '</td>' + '<td style="width: 26%;">'
				+ datas[i].modDate + '</td>' + '</tr>';
		$("#tbody-vol").append(tableTr);
		if(datas[i].imgtype == "box"){
			$("#tbody-vol").find("img.imgSrc")[i].attr('src',ctx +'\/images\/img-file.png');
		}else{
			$("#tbody-vol").find("img.imgSrc")[i].src = ctx +"/images/file-f.png";
		}
	}
}*/

$(document)
		.ready(
				function() {
					/*
					 * $("#storageReloadBtn").click(function(){
					 * window.location.reload(); });
					 */
					//volList();
				});
