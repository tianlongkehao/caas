$(document).ready(function(){
	creatable(null,null,null);
//	GateOne.init({url: 'https://192.168.247.129',
//		embedded: true
////			,theme: 'black',style: { 'background-color': 'black', 'box-shadow': '0 0 40px blueViolet'}
//	},newTerminal);
//});
//
//var newTerminal = function() {
//    // Introducing the superSandbox()!  Use it to wrap any code that you don't want to load until dependencies are met.
//    // In this example we won't call newTerminal() until GateOne.Terminal and GateOne.Terminal.Input are loaded.
//    GateOne.Base.superSandbox("NewExternalTerm", ["GateOne.Terminal", "GateOne.Terminal.Input"], function(window, undefined) {
//        "use strict";
//        var existingContainer = GateOne.Utils.getNode('#'+GateOne.prefs.prefix+'container');
//        var container = GateOne.Utils.createElement('div', {
//        	'id': 'container', 'class': 'terminal', 'style': {'height': '100%', 'width': '100%'}
//        });
//        var gateone = GateOne.Utils.getNode('#gateone');
//        // Don't actually submit the form
//        if (!existingContainer) {
//        	gateone.appendChild(container);
//        } else {
//        	container = existingContainer;
//        }
//        // Create the new terminal
//        var termNum = GateOne.Terminal.newTerminal(null, null, container); 
//    });
});
$(window).load(function() {
	alert(load);
});

$(window).unload(function() {
	alert(unload);
});

function  creatable(isDir,path,dirName){
	
	if(null==isDir ||"true"==isDir ){
		var tbody="";
		var context =$('#mybody');
		var param="";
		if(null==path||null==path){
		 param="path=&dirName=&storageName=/";
		}else{
			if(null==dirName){dirName="";}
			param="path="+path+"&dirName="+dirName+"/&storageName=/";
		}
		context.empty();

		$.ajax({
	        type: "GET",
	        url: ctx + "/service/listFile?"+param,
	        success : function(data) {
	        	//alert(data);
	        	var data = eval("("+data+")");
	        	if(data.status=="500"){
	        		alert("您没有权限浏览上一级内容");
	        		creatable(null,null,null);
	        		return;
	        			}
	        	if(data.status=="400"){
	        		alert("对不起，没有找到相应的目录");
	        		creatable(null,null,null);
	        		return;
	        			}
	        	for (i in data.fileList) {
	        		var	fileInfo =	JSON.stringify(data.fileList[i]);
	    				fileInfo = eval("(" + fileInfo + ")");
	    				//alert(fileInfo.day);
	    				if(fileInfo.fileName=='..'){
			    				tbody+='<tr class="vol_list" style="cursor:pointer">'+
									'<td style="width: 5%;text-indent: 14px;">'+
									'</td>'+
									'<td style="width: 25%; text-indent: 30px;"  onclick=creatable("'+fileInfo.dir+'","'+fileInfo.path+'","'+fileInfo.fileName+'") >'+
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
									'<td style="width: 25%;text-indent: 30px;" onclick=creatable("'+(fileInfo.dir || fileInfo.link)+'","'+fileInfo.path+'","'+fileInfo.fileName+'") >'+
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
	  	      //showDataTable();
	        }
		
	      })

	}
}
//删除某一行
function delfile(obj){
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
		                url: ctx + "/storage/delFile.do?path="+path+"&fileNames="+fileName+"&storageName=/",
		        	});
		        	layer.close(index);
		        }
	 })
	 
}
