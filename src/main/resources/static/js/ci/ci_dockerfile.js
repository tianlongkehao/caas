$(document).ready(function () {

 	$("#dockerfile").focus();
 	$("#dockerfile-import").hide();
 	$("#dockerfile-export").hide();
 	
 	//导入模板
	$("#docImportBtn").click(function(){
		layer.open({
		 	type:1,
	        title: 'dockerfile模板',
	        content: $("#dockerfile-import"),
	        btn: ['导入', '取消'],
	        yes: function(index, layero){ 
	        	
	        	layer.close(index);
				/*$.ajax({
					url:""+ctx+"service/stratServices.do?serviceIDs="+serviceIDs,
					success:function(data){
						data = eval("(" + data + ")");
						if(data.status=="200"){
							layer.alert("环境变量模板导入成功");
							window.location.reload();
						}else{
							layer.alert("环境变量模板导入失败");
						}
					}	
				})*/
	        }
	 })
	});
	
	//环境变量另存为模板
	$("#docExportBtn").click(function(){
		layer.open({
		 	type:1,
	        title: '另存为模板',
	        content: $("#dockerfile-export"),
	        btn: ['保存', '取消'],
	        yes: function(index, layero){ 
	        	
	        	layer.close(index);
				/*$.ajax({
					url:""+ctx+"service/stratServices.do?serviceIDs="+serviceIDs,
					success:function(data){
						data = eval("(" + data + ")");
						if(data.status=="200"){
							layer.alert("环境变量模板导入成功");
							window.location.reload();
						}else{
							layer.alert("环境变量模板导入失败");
						}
					}	
				})*/
	        }
	 })
	});
	
	//导入模板文件选项对勾
	$("#Path-table-doc>tbody>tr").on("click", function () {
		$(this).parent().find("tr.focus").find("span.doc-tr").toggleClass("hide");
        $(this).parent().find("tr.focus").toggleClass("focus");//取消原先选中行
        //$("#Path-table>tbody>tr").parent().find("tr.focus").find("span.vals-path").removeClass("hide")
        $(this).toggleClass("focus");//设定当前行为选中行
        $(this).parent().find("tr.focus").find("span.doc-tr").toggleClass("hide");
    });
	
	
   
});