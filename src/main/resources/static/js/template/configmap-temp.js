// 响应每一行上的删除按钮
 function oneDeleteEnvTemplate(configmapId) {
	var ids = [];
	ids.push(configmapId);
	layer.open({
		title: '删除配置文件模板？',
		content: '确定删除配置文件模板？',
		btn: ['确定', '取消'],
		yes: function(index, layero){
			layer.close(index);
				$.ajax({
					url:""+ctx+"/configmap/deleteConfigMap?configmapId="+ids,
					success:function(data){
						data = eval("(" + data + ")");
						if(data.status=="200"){
							layer.msg("配置文件模板删除成功",{icon: 6})
							setTimeout('window.location.reload()',1500);
						}else{
							layer.alert("配置文件模板删除失败!");
						}
					}
				})
	        }
	 })
 }

 function delEnvTemplates(){
	 var ids = [];
	 $('input[name="chkItem"]:checked').each(function(){
		 var id = $(this).val();
		 ids.push(id);
	 })
	 if ("" == ids) {
		layer.alert("请选择至少一个模板", {icon:0});
		return;
	 }
	 layer.open({
			 title: '删除配置文件模板',
			 content: '确定删除配置文件模板？',
			 btn: ['确定', '取消'],
			 yes: function(index, layero){
				 layer.close(index);
				 $.ajax({
					 url:""+ctx+"/configmap/delconfigmaps.do?ids="+ids,
					 success:function(data){
						 data = eval("(" + data + ")");
						 if(data.status=="200"){
							 layer.msg("配置文件模板删除成功",{icon: 6})
							 setTimeout('window.location.reload()',1500);
						 }else{
							 layer.alert("配置文件模板删除失败，请检查服务器连接");
						 }

					 }
				})
			}
	})
 }
