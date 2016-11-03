$(document).ready(function(){
	creatable(null,null);
});
//$(window).load(function() {
//	alert("load");
//});

$(window).unload(function() {
	alert("unload");
});

function  creatable(isDir,dirName){
	
	if(null==isDir ||"true"==isDir ){
		var tbody="";
		var context =$('#mybody');
		var param="";
		if(null==dirName){dirName="";}
		param="dirName="+dirName;
		context.empty();

		$.ajax({
	        type: "GET",
	        url: ctx + "/service/listFile?"+param,
	        success : function(data) {
	        	//alert(data);
	        	var data = eval("("+data+")");
	        	if(data.status=="400"){
	        		failedMSG("没有找到相应的目录");
	        		return;
	        	}
	        	for (i in data.fileList) {
	        		var	fileInfo =	JSON.stringify(data.fileList[i]);
	    				fileInfo = eval("(" + fileInfo + ")");
    				if(fileInfo.fileName=='..'){
		    				tbody+='<tr class="vol_list" style="cursor:pointer">'+
								'<td style="width: 5%;text-indent: 14px;">'+
								'</td>'+
								'<td style="width: 25%; text-indent: 30px;"  onclick=creatable("'+fileInfo.dir+'","'+fileInfo.fileName+'") >'+
								'<a hrer="">'
								if(true==fileInfo.dir){
									tbody+='<img src="/images/img-file.png" >'
								}else{
									tbody+='<img src="/images/file-f.png" >'
								 }
								tbody+='<span style="margin-left:5px"  >'+
								fileInfo.fileName+'</span>'+
								'</a>'+
								'</td>'+
								'<td style="width: 20%;">'+fileInfo.size+'</td>'+
								'<td style="width: 25%;"></td>'+
								'<td style="width: 10%;text-indent: 56px;"></td>'+
							'</tr>';
    				}else{
		    				tbody+='<tr class="vol_list" style="cursor:pointer">'+
								'<td style="width: 5%;text-indent: 14px;">'+
								'<input type="checkbox" class="chkItem" name="downfiles" value="'+fileInfo.fileName+'" >'+
								'</td>'+
								'<td style="width: 25%;text-indent: 30px;" onclick=creatable("'+(fileInfo.dir || fileInfo.link)+'","'+fileInfo.fileName+'") >'+
								'<a hrer="">'
								if(true==fileInfo.dir){
									tbody+='<img src="/images/img-file.png" >'
								} else if(true== fileInfo.link){
									tbody+='<img src="/images/image-link.png" >'
								} else {
									tbody+='<img src="/images/file-f.png" >'
								}
		    				tbody+='<span style="margin-left:5px"  >'+
								fileInfo.fileName+'</span>'+
								'</a>'+
								'</td>'+
								'<td style="width: 20%;">'+fileInfo.size+'KB</td>'+
								'<td style="width: 25%;">'+fileInfo.modifiedTime+'</td>'+
								'<td style="width: 10%;text-indent: 56px;">'+'<a class="deleteButton" href="javascript:void(0)" onclick="delfile(this)"  fileName="'+fileInfo.fileName+'"> <i class="fa fa-trash fa-lg"></i></a>'+'</td>'+
							'</tr>';
    				}
           
    				$('#downfilepath').val(fileInfo.path);
	        	} 
	        	$('#mybody').html($(tbody)); 
	        }

		})

	}
}
//删除某一行
function delfile(obj){
	var fileName=$(obj).attr("fileName");
	layer.open({
		title: '删除文件',
		content: '确定删除该文件？',
		btn: ['确定', '取消'],
		yes: function(index, layero){ 
			$.ajax({
				type: "GET",
		        url: ctx + "/service/delFile.do?fileNames="+fileName,
		        success : function(data) {
		        	var data = eval("("+data+")");
		        	if(data.status=="400"){
		        		failedMSG("删除失败");
		        		return;
		        	}
		        	creatable(null,"./");
		        }
			});
			layer.close(index);
		}
	})
}

//批量删除
function delfiles(){
	var path =$('#downfilepath').val();
	obj = document.getElementsByName("downfiles");
	var fileNames = [];
    for (k in obj) {
        if (obj[k].checked) {
        	fileNames.push(obj[k].value);
        }
    }
	layer.open({
		title: '删除文件',
		content: '确定删除选中的文件？',
		btn: ['确定', '取消'],
		yes: function(index, layero){ 
			layer.close(index);
			if(""==fileNames){
				failedMSG("没有文件被选中");
				return;
			}
			$.ajax({
				url:""+ctx+"/service/delFile.do?fileNames="+fileNames,
				success:function(data){
		        	var data = eval("("+data+")");
		        	if(data.status=="400"){
		        		failedMSG("删除失败");
		        		return;
		        	}
		        	creatable(null,"./");
				}
			})
		}
	})
}

//新建文件夹
function createdir(){
	layer.open({
		type:1,
		content:$('#createdir-templat'),
		title:'新建文件夹',
		btn:['新建','取消'],
		yes: function(index, layero){
			layer.close(index);
			var dirName=$('#newdir').val();
			$.ajax({
        		type: "POST",
        		url: ctx + "/service/createFile.do?dirName="+dirName,
        		success:function(data){
		        	var data = eval("("+data+")");
		        	if(data.status=="500"){
		        		failedMSG("新建文件夹失败");
		        		return;
		        	}
		        	creatable(null,".");
				}
			});
		}
	})
}


//弹出失败消息
function failedMSG(title){
	layer.msg(title,{
		icon: 5,
		btn: ['確定'],
		time: 0, //不自动关闭
		yes: function(index){
			layer.close(index);
			//刷新页面
			creatable(null,".");
		}
	});
}
