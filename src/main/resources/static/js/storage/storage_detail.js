
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

