 $(document).ready(function () {
	 
	$(document).on('click','.no-drop',function(){
		  return false;
		});
	
	creatable();
	
	_refreshCreateTime(60000);
	//添加外部服务地址
	$("#importServiceBtn").click(function(){
		 $("#import-ser-name").removeAttr("disabled");
		 $("#import-ser-name").val("");
		 $("#import-ser-in").val("");
		 $("#import-ser-out").val("");
		 $("#import-ser-out-port").val("");
		 $("#import-ser-desc").val("");
		 layer.open({
		 	type:1,
	        title: '引入外部服务',
	        content: $("#import-service"),
	        btn: ['保存', '取消'],
	        yes: function(index, layero){
	        	$('#import-ser-name').focus();
	       	 var importSerName = $("#import-ser-name").val();
//	       	 var importSerIn = $("#import-ser-in").val();
	       	 var importSerIn = "default";
	       	 var importSerOut = $("#import-ser-out").val();
	       	 var importSerOutPort = $("#import-ser-out-port").val();
	       	 var importSerVis = $("#import-ser-visibility").val();
	       	 var importSerMode = $("#import-ser-mode").val();
	       	 var importSerDesc =$("#import-ser-desc").val();
	       	 var useNginx =$("#useProxyFlag").val();
	       	 if(!checkndCommit(importSerName,importSerIn,importSerOut,importSerOutPort,importSerVis,importSerMode,importSerDesc)){
	       		 return;
	       	 }
	       	var flag=0;
	        var un = importSerName.toLowerCase();
	        console.info(un);
	        $("#import-ser-name").val(un);
	        $.get(ctx +"/refservice/checkName.do?un=" + un,
	            function (data, status) {
	                console.info("Data: " + data + "\nStatus: " + status);
	                var data = eval("(" + data + ")");
	                if (data.status == "400") {
	                    layer.tips('服务名已经被使用，请输入新的服务名！', '#import-ser-name', {
	                        tips: [1, '#0FA6D8']
	                    });
	                    $('#import-ser-name').focus();
	                    return ;
	                }else {
		                layer.close(layer.index);
		               	$.ajax({
	   		         		url : ctx + "/refservice/add.do",
	   		         		type: "POST",
	   		         		data: {"serName":importSerName,"serAddress":importSerIn,
	   		         			   "refAddress":importSerOut,"refPort":importSerOutPort,
	   		         			   "viDomain":importSerVis,"importSerMode":importSerMode,
	   		         			   "refSerDesc":importSerDesc,"useNginx":useNginx
	   		         		},
	   		         		success: function(data) {
	   		         			creatable();
	   		         		}
	   		         	});
	                }
	            });
	        }
		})
	});
 });

	 

	function checkndCommit(importSerName,importSerIn,importSerOut,importSerOutPort,importSerVis,importSerMode,importSerDesc){
	 if (importSerName.length === 0) {
         layer.tips('服务名称不能为空', '#import-ser-name', {
             tips: [1, '#0FA6D8']
         });
         $('#import-ser-name').focus();
         return ;
     } 
	 
	 if(importSerName.search(/^[a-z0-9-]*$/) === -1){
		 layer.tips('服务名称只能由小写字母、数字及横线组成','#import-ser-name',{tips: [1, '#3595CC'],time: 3000});
		 $('#import-ser-name').focus();
		 return;
	     }
	
	 if (importSerName.length < 1) {
         layer.tips('服务名称过短', '#import-ser-name', {
             tips: [1, '#0FA6D8']
         });
         $('#import-ser-name').focus();
         return ;
     } 
     
     if (importSerIn.length === 0) {
         layer.tips('服务访问地址不能为空', '#import-ser-in', {
             tips: [1, '#0FA6D8']
         });
         $('#import-ser-in').focus();
         return ;
     }
     if (importSerOut.length === 0) {
         layer.tips('外部服务地址不能为空', '#import-ser-out', {
             tips: [1, '#0FA6D8']
         });
         $('#import-ser-out').focus();
         return ;
     }
     
	 if (importSerOut.search(/((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)/) === -1 ) {
         layer.tips('你的外部服务地址格式不是xxx.xxx.xxx.xxx', '#import-ser-out', {
             tips: [1, '#0FA6D8']
         });
         $('#import-ser-out').focus();
         return ;
     }
     
     if (importSerOutPort.length === 0) {
    	 layer.tips('外部服务端口不能为空', '#import-ser-out-port', {
             tips: [1, '#0FA6D8']
         });
         $('#import-ser-out-port').focus();
         return ;
     }
     if (importSerOutPort <=0 |importSerOutPort>=65535) {
    	 layer.tips('外部服务端口要在0～65535之间', '#import-ser-out-port', {
             tips: [1, '#0FA6D8']
         });
         $('#import-ser-out-port').focus();
         return ;
     }
     
     if (importSerOutPort.search(/^[0-9]*$/) === -1) {
    	 layer.tips('外部服务端口只能为数字', '#import-ser-out-port', {
             tips: [1, '#0FA6D8']
         });
         $('#import-ser-out-port').focus();
         return ;
     }
     if (importSerDesc.length >20 ) {
         layer.tips('不要超过20个字，谢谢', '#import-ser-desc', {
             tips: [1, '#0FA6D8']
         });
         $('#import-ser-desc').focus();
         return ;
     } 
    return true;
 }
 //展示列表
 function  creatable(){
 	var tr="";
 	var context =$('#importSerList');
 	context.empty();
 	$.ajax({
         type: "GET",
         url: ctx + "/refservice/list.do",
         success : function(data) {
         	var data = eval("("+data+")");
         	for (var i in data.data) {
            		var refservice = data.data[i];
            		tr+='<tr>'+
			     			'<td style="width: 5%; text-indent: 30px;">'+
			     			'<input type="checkbox" name="chkItem" class="chkItem" value='+refservice.id+' /></td>'+
					     		'<td style="width: 18%; padding-left: 5px;">'+refservice.serName+ '</td>'+
					     		'<td style="width: 20%; text-indent: 8px;">'+refservice.serAddress+'</td>'+
					     		'<td style="width: 20%;">'+refservice.refAddress+'</td>';
					     		if(undefined == refservice.useProxy){
					     			tr+='<td style="width: 20%;">'+"无"+'</td>';
					     		}else{
					     			tr+='<td style="width: 20%;">'+refservice.useProxy+'</td>';
					     			}
			     		if('1'==refservice.viDomain){
			     			tr+='<td style="width: 10%;">所有租户可见</td>';
			     		}else{
			     			tr+='<td style="width: 10%;">仅本租户可见</td>';
			     		}
			     		tr+='<td style="width: 14%;"><a class="deleteButton" href="javascript:void(0)" onclick="delImportSer(this,'+refservice.id+')"> <i class="fa fa-trash fa-lg"></i></a>'+
					     		'<a class="editButton" onclick="editImportSer(this,'+refservice.id+')" serName="'+refservice.serName+'" serIn="'+refservice.serAddress+'" rePort="'+refservice.refPort
					     		+'" serOut="'+refservice.refAddress+'" serDesc="'+refservice.refSerDesc+'" serVi="'+refservice.viDomain+'" usePxy="'+refservice.useProxy+'"><i class="fa fa-edit fa-lg"></i></a></td>'+
					     	'</tr>';
         	}
            $("#importSerList").append(tr);
            showDataTable();
        	
         }
 	
       })
 }
 function showDataTable(){
	 $('.dataTables-example').dataTable({
	     "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0 ,5] }]
		});
	 $("#checkallbox").parent().removeClass("sorting_asc");
	 
 }
 //删除某一行
 function delImportSer(obj,id){
	 
		layer.open({
		        title: '删除外部引入服务',
		        content: '确定删除外部引入服务？',
		        btn: ['确定', '取消'],
		        yes: function(index, layero){ 
		        	$(obj).parent().parent().remove();
		        	//refservice/delete.do
		        	$.ajax({
		        		type: "GET",
		                url: ctx + "/refservice/delete.do?ids="+id,
		                success : function(data) {
		                 	var data = eval("("+data+")");
		                 	if (data.status == 200) {
		                 		layer.msg("删除外部服务成功！",{icon: 6});
		                 		window.location.reload();
		                 	} 
		                 	else {
		                 		layer.alert("删除外部服务失败！请检查服务器连接");
		                 	}
		                }
		        	});
		        	layer.close(index);
		        }
	 })
	 
 }
 
 //修改
 function editImportSer(obj,id){
	 $("#import-ser-name").val($(obj).attr("serName"));
	 $("#import-ser-in").val($(obj).attr("serIn"));
	 $("#import-ser-out").val($(obj).attr("serOut"));
	 $("#import-ser-out-port").val($(obj).attr("rePort"));
	 $("#import-ser-visibility").val($(obj).attr("serVi"));
	 $("#import-ser-desc").val($(obj).attr("serDesc"));
	 $("#import-ser-name").attr("disabled","disabled");
	 var usePxy =$(obj).attr('usePxy');
	 if("undefined"==usePxy){ 
		 $("#useProxyFlag option[value=0]").attr("selected", true); 
	 }else{
		 $("#useProxyFlag option[value=1]").attr("selected", true); 
	 }
	 
	 layer.open({
		 	type: 1,
	        title: '修改外部引入服务',
	        content: $("#import-service"),
	        btn: ['确定', '取消'],
	        yes: function(index, layero){
	        	 var importSerName = $("#import-ser-name").val();
	        	 var importSerIn = "default";
	        	 var importSerOut = $("#import-ser-out").val();
	        	 var importSerOutPort = $("#import-ser-out-port").val();
	        	 var importSerVis = $("#import-ser-visibility").val();
	        	 var importSerMode = $("#import-ser-mode").val();
	        	 var importSerDesc =$("#import-ser-desc").val();
	        	 var useNginx =$("#useProxyFlag").val();
		       	 if(!checkndCommit(importSerName,importSerIn,importSerOut,importSerOutPort,importSerVis,importSerMode,importSerDesc)){
		       		 return;
		       	 }
	             layer.close(index);
	             $.ajax({
		         		url : ctx + "/refservice/edit.do",
		         		type: "POST",
		         		data: {"id":id,"serName":importSerName,"serAddress":importSerIn
		         			,"refAddress":importSerOut,"viDomain":importSerVis
		         			,"refPort":importSerOutPort,"importSerMode":importSerMode,"refSerDesc":importSerDesc,"useNginx":useNginx},
		         		success: function(data) {
		         			var data = eval("("+data+")");
		                 	if (data.status == 200) {
		                 		layer.msg("修改外部服务参数成功！",{icon: 6});
		                 		creatable();
		                 	} 
		                 	else {
		                 		layer.alert("修改外部服务参数失败！请检查服务器连接");
		                 	}
		         		}
		         	}); 

	        }
	 })
 }
 
 //批量删除
 function delImportSers(){
		obj = document.getElementsByName("chkItem");
		var ids = [];
	    for (k in obj) {
	        if (obj[k].checked) {
	        	ids.push(obj[k].value);
	        }
	    }
	    if(""==ids){
	    	layer.alert("请至少选中一个外部服务！",{icon:0});
	    	return;
	    }
	 layer.open({
	        title: '删除外部引入服务',
	        content: '确定批量删除外部引入服务？',
	        btn: ['确定', '取消'],
	        yes: function(index, layero){ 
	        	layer.close(index);
				$.ajax({
					url:""+ctx+"/refservice/delete.do?ids="+ids,
					success:function(data){
						data = eval("(" + data + ")");
						if(data.status=="200"){
							layer.alert("服务已删除");
							window.location.reload();
						}else{
							layer.alert("服务删除失败，请检查服务器连接");
						}
	
					}
				})
	        }
	 })
	 
 }
 
 function refresh(){
	 window.location.reload();//刷新当前页面.
 }
 
 function checkbox(){
	 $('input[name="chkItem"]').click(function(){
			if($(this).prop("checked")){
				$('#startContainer').removeClass('no-drop').addClass('a-live');
				$('#stopContainer').removeClass('no-drop').addClass('a-live');
				$('#scaleCluster').removeClass('no-drop').addClass('a-live');
				$('#upgradeCluster').removeClass('no-drop').addClass('a-live');
				$('#redeployContainer').removeClass('no-drop').addClass('a-live');
				$('#changeConfiguration').removeClass('no-drop').addClass('a-live');
				$('#deleteButton').removeClass('no-drop').addClass('a-live');
				var status = [];
				$('.clusterId').find("input[name='chkItem']").each(function(el) {
					if($(this).is(':checked')){
						status.push($(this).attr('status'));
					}
				});
				var statusStr = status.toString();
				var runIndex = statusStr.indexOf('3');
				var stopIndex = statusStr.indexOf('4');
				if(status.length > 1){
					$('#changeConfiguration').removeClass('a-live').addClass('no-drop');
					$('#scaleCluster').removeClass('a-live').addClass('no-drop');
					$('#upgradeCluster').removeClass('a-live').addClass('no-drop');
				}
				if(statusStr.indexOf('Initialization') > -1){
					$('#startContainer').removeClass('a-live').addClass('no-drop');
					$('#stopContainer').removeClass('a-live').addClass('no-drop');
					$('#changeConfiguration').removeClass('a-live').addClass('no-drop');
					$('#scaleCluster').removeClass('a-live').addClass('no-drop');
					$('#upgradeCluster').removeClass('a-live').addClass('no-drop');
				}
				if(runIndex > -1 && stopIndex < 0){
					$('#startContainer').removeClass('a-live').addClass('no-drop');
				}
				if(runIndex < 0 && stopIndex > -1){
					$('#stopContainer').removeClass('a-live').addClass('no-drop');
				}
				if(runIndex > -1 && stopIndex > -1){
					$('#startContainer').removeClass('a-live').addClass('no-drop');
					$('#stopContainer').removeClass('a-live').addClass('no-drop');
				}
			} else {
				$('#startContainer').removeClass('a-live').addClass('no-drop');
				$('#stopContainer').removeClass('a-live').addClass('no-drop');
				$('#scaleCluster').removeClass('a-live').addClass('no-drop');
				$('#upgradeCluster').removeClass('a-live').addClass('no-drop');
				$('#redeployContainer').removeClass('a-live').addClass('no-drop');
				$('#changeConfiguration').removeClass('a-live').addClass('no-drop');
				$('#deleteButton').removeClass('a-live').addClass('no-drop');
			}
	
	})
 }
 // Refresh create time
 function _refreshCreateTime(interval){
   setInterval(function(){
     $('.tdTimeStrap').each(function(){
       var self = this;
       var timebefore = $(self).find('span').text();
       if (timebefore.indexOf('秒') > -1 || timebefore.indexOf('分钟') > -1) {
         moment.locale('zh-cn');
         var creationTimestamp = $(self).find('.timeStrap').val();
         var timeFromNow = moment(creationTimestamp).fromNow();
         $(self).find('span').text(timeFromNow);
       }
     });
   }, interval);
 }
 
 function refresh1(id){
	
	var url = "service/" + Math.random();
	//create random number
	setTimeout(function() {
	$("#inst_"+id).load(url+id,"");
		  }, 500); //wait one second to run function
	
 }
 
 