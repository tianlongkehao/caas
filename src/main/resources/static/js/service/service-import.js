 $(document).ready(function () {
	 
	$(document).on('click','.no-drop',function(){
		  return false;
		});
	
	

	_refreshCreateTime(60000);
	
	checkbox();
	
	//添加外部服务地址
	$("#importServiceBtn").click(function(){
		layer.open({
		 	type:1,
	        title: '引入外部服务',
	        content: $("#import-service"),
	        btn: ['保存', '取消'],
	        yes: function(index, layero){
	        	 var importSerName = $("#improt-ser-name").val();
	        	 var importSerIn = $("#improt-ser-in").val();
	        	 var importSerOut = $("#improt-ser-out").val();
	        	 var importSerVis = $("#improt-ser-visibility").val();
	        	 
	        	 /*$.ajax({
		         		url : ctx + "/service/importSer.do",
		         		type: "POST",
		         		data:{"templateName":templateName},
		         		success : function(data) {
		         			
		         		}
		         	});*/
	        	 
	        	 if(importSerName != ""){
	     			var tr = '<tr>'+
				     			'<td style="width: 5%; text-indent: 30px;">'+
				     			'<input type="checkbox" name="chkItem" /></td>'+
						     		'<td style="width: 18%; padding-left: 5px;">'+importSerName+'</td>'+
						     		'<td style="width: 20%; text-indent: 8px;">'+importSerIn+'</td>'+
						     		'<td style="width: 20%;">'+importSerOut+'</td>'+
						     		'<td style="width: 14%;">'+importSerVis+'</td>'+
						     		'<td style="width: 10%;"><a class="deleteButton" href="javascript:void(0)" onclick="delImportSer(this)"> <i class="fa fa-trash fa-lg"></i></a>'+
						     		'<a class="editButton" href="javascript:editImportSer(this)"><i class="fa fa-edit fa-lg"></i></a></td>'+
						     	'</tr>';
	     		$("#importSerList").append(tr);
	        	 }
	        	 layer.close(index);
	        }
		})
	});
	
	


 });
 
 //删除某一行
 function delImportSer(obj){
	 
		layer.open({
		        title: '删除外部引入服务',
		        content: '确定删除外部引入服务？',
		        btn: ['确定', '取消'],
		        yes: function(index, layero){ 
		        	$(obj).parent().parent().remove();
		    	   
		        	layer.close(index);
		        }
	 })
	 
 }
 //修改
 function editImportSer(obj){
	 
	 layer.open({
		 	type: 1,
	        title: '修改外部引入服务',
	        content: $("#import-service"),
	        btn: ['确定', '取消'],
	        yes: function(index, layero){ 
	        	
	        	layer.close(index);
	        }
	 })
 }
 
 //批量删除
 function delImportSers(){
	 var importSerIDs = [];
	 $('input[name="chkItem"]:checked').each(function(){
	      var importSerId = $(this).val();
	      importSerIDs.push(importSerId);
		 layer.open({
		        title: '删除外部引入服务',
		        content: '确定批量删除外部引入服务？',
		        btn: ['确定', '取消'],
		        yes: function(index, layero){ 
		        	layer.close(index);
		        				/*$.ajax({
		        					url:""+ctx+"service/delServices.do?serviceIDs="+serviceIDs,
		        					success:function(data){
		        						data = eval("(" + data + ")");
		        						if(data.status=="200"){
		        							layer.alert("服务已删除");
		        							window.location.reload();
		        						}else{
		        							layer.alert("服务删除失败，请检查服务器连接");
		        						}
	        		
		        					}
		        				})*/
		        }
		 })
		 
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
 
 