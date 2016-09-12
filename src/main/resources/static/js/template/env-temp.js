 // 响应每一行上的删除按钮
 function oneDeleteEnvTemplate(id) {
	var envTemplateIDs = [];
	envTemplateIDs.push(id);
	layer.open({
		title: '删除环境变量？',
		content: '确定删除环境变量？',
		btn: ['确定', '取消'],
		yes: function(index, layero){ 
			layer.close(index);
				$.ajax({
					url:""+ctx+"/template/delEnvTemplates.do?envTemplateIDs="+envTemplateIDs,
					success:function(data){
						data = eval("(" + data + ")");
						if(data.status=="200"){
							
							layer.msg("环境变量删除成功",{btn: ['确定'],icon: 6
							  ,yes: function(index2){
									window.location.reload();
							  }});
						}else{
							layer.alert("环境变量删除失败，请检查服务器连接");
						}
					}
				})
	        }
	 })
 }
 
 function delEnvTemplates(){
	 var serviceIDs = [];
	 $('input[name="chkItem"]:checked').each(function(){
	      var containerId = $(this).val();
	      serviceIDs.push(containerId);
		 layer.open({
		        title: '删除服务',
		        content: '确定删除服务？',
		        btn: ['确定', '取消'],
		        yes: function(index, layero){ 
		        	layer.close(index);
		        				$.ajax({
		        					url:""+ctx+"/service/delServices.do?serviceIDs="+serviceIDs,
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
		 
	 })
	 
 }
