// 响应每一行上的删除按钮
 function oneDeleteEnvTemplate(templateName) {
	var templateNames = [];
	templateNames.push(templateName);
	layer.open({
		title: '删除环境变量？',
		content: '确定删除环境变量？',
		btn: ['确定', '取消'],
		yes: function(index, layero){ 
			layer.close(index);
				$.ajax({
					url:""+ctx+"/template/delEnvTemplates.do?templateNames="+templateNames,
					success:function(data){
						data = eval("(" + data + ")");
						if(data.status=="200"){
							layer.msg("环境变量删除成功",{icon: 6})
							setTimeout('window.location.reload()',1500);
						}else{
							layer.alert("环境变量删除失败，请检查环境变量器连接");
						}
					}
				})
	        }
	 })
 }
 
 function delEnvTemplates(){
	 var templateNames = [];
	 $('input[name="chkItem"]:checked').each(function(){
		 var envTemplate = $(this).val();
		 templateNames.push(envTemplate);
		 layer.open({
			 title: '删除环境变量',
			 content: '确定删除环境变量？',
			 btn: ['确定', '取消'],
			 yes: function(index, layero){ 
				 layer.close(index);
				 $.ajax({
					 url:""+ctx+"/template/delEnvTemplates.do?templateNames="+templateNames,
					 success:function(data){
						 data = eval("(" + data + ")");
						 if(data.status=="200"){
							 layer.msg("环境变量删除成功",{icon: 6})
							 setTimeout('window.location.reload()',1500);
						 }else{
							 layer.alert("环境变量删除失败，请检查服务器连接");
						 }
			
					 }
				 })
			 }
		 })
	 })
	 
 }
