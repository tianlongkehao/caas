
<<<<<<< HEAD
var jsonData = {
		
}

$(document).ready(function () {
	 creatable(null,null,null);
	 $("#fileUpload").click(function(){
		    layer.open({
		    	type:1,
		    	content:$('#environment-templat'),
		    	title:'上传文件',
		    	btn:['保存','取消'],
		    	yes: function(index, layero){ 
		    			var path = $('#downfilepath').val();
		        	$('#path').val(path);
		        	var formData = new FormData($( "#form1" )[0]);
		        	$.ajax({
			        		type:'POST',
			        		url: ctx + '/upload',
			        		data: formData,
						 			async: false,  
			 	         cache: false,  
			 	         contentType: false,  
			 	         processData: false,  
			 	         success : function(data) {
		   			 				var data = eval("("+data+")");
		   			 				if("200"==data.status){
		   			 					layer.open({
		   			 						type:1,
		   			 						content:'上传成功！',
		   			 						title:'上传成功',
		   			 						btn:['确定'],
		   			 						yes: function(index, layero){ 
		   			 							creatable(null,null,null);
		   			 						layer.closeAll()
		   			 						   }
		   			 			});
		   			 		}
		   			 	}
		          			
		        	}); 
		        }
		    })
		});
	$("#storageReloadBtn").click(function(){
		window.location.reload();
	});
});

function  creatable(isDir,path,dirName){
	
	if(null==isDir ||"true"==isDir ){
		var tbody="";
		var context =$('#mybody');
		var param="";
		var storageName=$('#storageName').html();
		if(null==path||null==path){
		 param="path=&dirName=&storageName=/"+storageName+"/";
		}else{
			param="path="+path+"&dirName="+dirName+"/&storageName=/"+storageName+"/";
		}
		context.empty();

		$.ajax({
	        type: "GET",
	        url: ctx + "/listFile?"+param,
	        success : function(data) {
	        	//alert(data);
	        	var data = eval("("+data+")");
	        	if(data.status=="500"){
	        		alert("您没有权限浏览上一级内容");
	        		creatable(null,null,null);
	        		return;
	        			}
	        	for (i in data.fileList) {
	        		var	fileInfo =	JSON.stringify(data.fileList[i]);
	    				fileInfo = eval("(" + fileInfo + ")");
	    				//alert(fileInfo.day);
	    				if(fileInfo.fileName=='..'){
			    				tbody+='<tr class="vol_list" style="cursor:pointer">'+
									'<td style="text-indent: 14px;">'+
									'</td>'+
									'<td style="width: 40%;">'+
									'<a hrer=""><img src="/images/img-file.png" >'+
									'<span style="margin-left:5px" onclick=creatable("'+fileInfo.dir+'","'+fileInfo.path+'","'+fileInfo.fileName+'") >'+
									fileInfo.fileName+'</span>'+
									'</a>'+
									'</td>'+
									'<td style="width: 30%;">'+fileInfo.size+'</td>'+
									'<td style="width: 26%;"></td>'+
								'</tr>';
	    				}else{
			    				tbody+='<tr class="vol_list" style="cursor:pointer">'+
									'<td style="text-indent: 14px;">'+
									'<input type="checkbox" class="chkItem" name="downfiles" value="'+fileInfo.fileName+'" >'+
									'</td>'+
									'<td style="width: 40%;">'+
									'<a hrer=""><img src="/images/img-file.png" >'+
									'<span style="margin-left:5px" onclick=creatable("'+fileInfo.dir+'","'+fileInfo.path+'","'+fileInfo.fileName+'") >'+
									fileInfo.fileName+'</span>'+
									'</a>'+
									'</td>'+
									'<td style="width: 30%;">'+fileInfo.size+'KB</td>'+
									'<td style="width: 26%;">'+fileInfo.time+'</td>'+
								'</tr>';
	    				}
	           
	           $('#downfilepath').val(fileInfo.path);
	        	} 
	        	$('#mybody').html($(tbody)); 
	    		
	        }
	      })
	}
}

=======
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
        						+'</tr>'
        		})(i);
        	}
        	$('tbody #storageList').html(itemsHtml);
		}
	});
}*/
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
					$("#tbody-vol").find("img.imgSrc")[j].src = "/images/img-file.png";
				}else{
					$("#tbody-vol").find("img.imgSrc")[j].src = "/images/file-f.png";
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

function volList(){
	for (var i = 0; i < datas.length; i++) {
		var tableTr = '<tr class="vol_list">'
				+ '<td style="text-indent: 14px;"><input type="checkbox" class="chkItem" name="vol_chk" value="" ></td>'
				+ '<td style="width: 40%;"><a id="aaa" onclick="expand(this);"><img class="imgSrc" src="" ><span class="volName" style="margin-left:5px">'
				+ datas[i].filename + '</span></a></td>'
				+ '<td style="width: 30%;">' + datas[i].size
				+ '</td>' + '<td style="width: 26%;">'
				+ datas[i].modDate + '</td>' + '</tr>';
		$("#tbody-vol").append(tableTr);
		if(datas[i].imgtype == "box"){
			$("#tbody-vol").find("img.imgSrc")[i].src = "/images/img-file.png";
		}else{
			$("#tbody-vol").find("img.imgSrc")[i].src = "/images/file-f.png";
		}
	}
}

$(document)
		.ready(
				function() {
					/*
					 * $("#storageReloadBtn").click(function(){
					 * window.location.reload(); });
					 */
					volList();

				});
>>>>>>> refs/heads/develop
