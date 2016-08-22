$(document).ready(function () {

 	$("#dockerfile").focus();
 	$("#dockerfile-import").hide();
 	$("#dockerfile-export").hide();
 	

 	$("#buildBtn").click(function(){
        if(checkCiAdd()) {
        	$("#buildForm").submit();
        	layer.load(0, {shade: [0.3, '#000']});
        }
        return false;
    });
 	
 	$(".btn-imageType .btns").each(function(){
        $(this).click(function(){
            $(".btn-imageType .btns").removeClass("active");
            $(this).addClass("active");
        });
    });
 	
 	//导入模板文件选项对勾
	var dockerFile = null;
	$(document).on("click","#Path-table-doc tr", function () {
		$(this).parent().find("tr.focus").find("span.doc-tr").toggleClass("hide");
        $(this).parent().find("tr.focus").toggleClass("focus");//取消原先选中行
        //$("#Path-table>tbody>tr").parent().find("tr.focus").find("span.vals-path").removeClass("hide")
        $(this).toggleClass("focus");//设定当前行为选中行
        $(this).parent().find("tr.focus").find("span.doc-tr").toggleClass("hide");
        dockerFile = $(this).parent().find("tr.focus").find(".dockerFileTemplate").val();
    });
	
	
//	$(document).on("click","#Path-table tr", function () {
//		$(this).parent().find("tr.focus").find("span.vals-path").toggleClass("hide");
//		$(this).parent().find("tr.focus").toggleClass("focus");//取消原先选中行
//		//$("#Path-table>tbody>tr").parent().find("tr.focus").find("span.vals-path").removeClass("hide")
//		$(this).toggleClass("focus");//设定当前行为选中行
//		$(this).parent().find("tr.focus").find("span.vals-path").toggleClass("hide");
//		templateName = $(this).parent().find("tr.focus").find(".templateName").val();
//		console.log(templateName);
//	});
	
 	
 	//导入模板
	$("#docImportBtn").click(function(){
		loadDockerFileTemplate();
		layer.open({
		 	type:1,
	        title: 'dockerfile模板',
	        content: $("#dockerfile-import"),
	        btn: ['导入', '取消'],
	        yes: function(index, layero){ 
	        	$("#dockerFile").val(dockerFile);
	        	layer.close(index);
	        }
	 })
	});
	
	//另存为模板
	$("#docExportBtn").click(function(){
		layer.open({
		 	type:1,
	        title: '另存为模板',
	        content: $("#dockerfile-export"),
	        btn: ['保存', '取消'],
	        yes: function(index, layero){ 
	        	var templateName = $("#dockerFileTemplateName").val();
	        	if (templateName == null || templateName == "") {
	        		layer.tips('模板名称不能为空','#dockerFileTemplateName',{tips: [1, '#3595CC']});
					$('#dockerFileTemplateName').focus();
					return;
	        	}
	        	var dockerFile = $("#dockerFile").val();
	        	if (dockerFile == null || dockerFile == "") {
	        		layer.tips('DockerFile不能为空','#dockerFile',{tips: [1, '#3595CC']});
					$('#dockerFile').focus();
					layer.close(index);
					return;
	        	}
	        	$.ajax({
					url:ctx+"/ci/saveDockerFileTemplate.do",
					type: "POST",
	         		data:{"templateName":templateName,"dockerFile":dockerFile},
					success:function(data){
						data = eval("(" + data + ")");
						if(data.status=="400"){
							layer.msg("DockerFile模板名称重复",{icon: 5});
						}else{
							layer.msg("DockerFile模板导入成功",{icon: 6});
							layer.close(index);
						}
					}	
	        	});
	        }
	 })
	});
	
	//单击导入模板，加载模板数据
	function loadDockerFileTemplate(){
		 $.ajax({
	  		url : ctx + "/ci/loadDockerFileTemplate.do",
	  		success : function(data) {
	  			data = eval("(" + data + ")");
	  			var html = "";
		            if (data != null) {
	                	for (var i in data.data) {
	                		var dockerFile = data.data[i];
	                		html += "<tr>"+
	                				"<td class='vals vals-doc'>"+dockerFile.templateName+"<span class='doc-tr hide'><i class='fa fa-check'></i></span>"+
	                				"<input type='hidden' class='dockerFileTemplate' value='"+dockerFile.dockerFile+"' /></td>"+
	                			"</tr>"
	                	}
		            } 
		            if (html == "") {
		            	html += '<tr><td>没有保存的模板</td></tr>'	
		            }
		            $("#dockerfile-body").empty();
		            $("#dockerfile-body").append(html);
	  			}
		 });
	}

	
	function checkCiAdd(){
        var imgNameLast = $("#imgNameLast").val().trim();
        var projectName = $('#projectName').val().trim();
        var description = $('#description').val().trim();
        var dockerFile = $('#dockerFile').val().trim();
        
        // 验证仓库名称
        imgNameLast = imgNameLast.toLowerCase();
        $('#imgNameLast').val(imgNameLast);
        if(imgNameLast.length === 0){
            layer.tips('镜像名称不能为空', '#imgNameLast', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#imgNameLast').focus();
            return false;
        }
        
        if(imgNameLast.search(/^[a-z][a-z0-9-_]*$/) === -1){
            layer.tips('镜像名称只能由字母、数字、横线及下划线组成，且首字母不能为数字、横线及下划线。', '#imgNameLast', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#imgNameLast').focus();
            return false;
        }
        
        if(imgNameLast.length > 50 || imgNameLast.length < 3){
            layer.tips('镜像名称为3~50个字符', '#imgNameLast', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#imgNameLast').focus();
            return false;
        }
        
        // 验证简介
        if(description.length === 0){
            layer.tips('项目简介不能为空', '#description', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#description').focus();
            return false;
        }
        //验证dockerFile
        
        if (dockerFile.length === 0 ) {
        	layer.tips('dockerFile不能为空', '#dockerFile',{
        		tips: [1,'##3595CC']
        	});
        	$('#dockerFile').focus();
        	return false;
        }
        
        // 验证项目名称
        if(projectName.length === 0){
            layer.tips('项目名称不能为空', '#projectName', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#projectName').focus();
            return false;
        }
        if(projectName.length > 20 || projectName.length < 3){
            layer.tips('项目名称为3~20个字符', '#projectName', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#projectName').focus();
            return false;
        }

        return true;
    }

	
   
});