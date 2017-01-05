$(document).ready(function(){
	$("#dockerFile").focus();
	var editor_one = CodeMirror.fromTextArea(document.getElementById("dockerFile"), {
        lineNumbers: true,
        matchBrackets: true,
        styleActiveLine: true,
        theme: "ambiance"
    });
	
	
	
	// 导入模板文件选项对勾
	var dockerFile = null;
	$("#Path-table-doc>tbody>tr").on(
			"click",
			function() {
				$(this).parent().find("tr.focus").find("span.doc-tr")
						.toggleClass("hide");
				$(this).parent().find("tr.focus").toggleClass("focus");// 取消原先选中行
				// $("#Path-table>tbody>tr").parent().find("tr.focus").find("span.vals-path").removeClass("hide")
				$(this).toggleClass("focus");// 设定当前行为选中行
				$(this).parent().find("tr.focus").find("span.doc-tr")
						.toggleClass("hide");
				$.ajax({
					url : ctx + "/template/dockerfile/content",
					type : "GET",
					data : {
						"id":$(this).parent().find("tr.focus").find(".dockerFileTemplate").val()
					},
					success : function(data) {
						data = eval("(" + data + ")");
						if (data != null) {
							dockerFile = data.dockerFile;
						}
					}
				});
	});
	
	// 导入模板
	$("#docImportBtn").click(function() {
		layer.open({
			type : 1,
			title : 'dockerfile模板',
			content : $("#dockerfile-import"),
			btn : [ '导入', '取消' ],
			yes : function(index, layero) {
				editor_one.setValue(dockerFile);
				layer.close(index);
			}
		})
	});
	
	$("#buildStorage").click(function() {
		var templateName = $("#dockerFileTemplateName").val();
		if (templateName == null || templateName == "") {
			layer.tips('模板名称不能为空', '#dockerFileTemplateName', {
				tips : [ 1, '#3595CC' ]
			});
			$('#dockerFileTemplateName').focus();
			return;
		}
		if(checkDockerfile()){
				var dockerFile = editor_one.getValue();
				$.ajax({
					url : ctx + "/ci/saveDockerFileTemplate.do",
					type : "POST",
					data : {
						"templateName" : templateName,
						"dockerFile" : dockerFile
					},
					success : function(data) {
						data = eval("(" + data + ")");
						if (data.status == "200") {
							layer.alert("DockerFile模板保存成功");
							setTimeout('window.location.href=""+ctx+"/template/dockerfile"',1000);
						} else {
							layer.tips('模板名称重复，请您重新填写', '#dockerFileTemplateName', {
								tips : [ 1, '#3595CC' ]
							});
							$('#dockerFileTemplateName').focus();
						}
					}
				});
		 }
		
	})
	
	function checkDockerfile(){
		var dockerFile = editor_one.getValue();
		if (dockerFile == null || dockerFile == "") {
			layer.tips('DockerFile不能为空', '#dockerfiletext', {
				tips : [ 1, '#3595CC' ]
			});
			$('#dockerfiletext').focus();
			return false;
		}
		if (dockerFile.length > 0) {
        	var flag = true;
        	$.ajax({
        		async : false,
        		url : ctx+"/ci/judgeBaseImage.do",
        		type : "GET",
        		data : {"dockerFile":dockerFile},
        		success:function(data){
					data = eval("(" + data + ")");
					if(data.status=="400"){
						layer.msg("dockerFile首行命令不正确，必须是FROM 基础镜像:版本格式，请您检查是否正确", {icon: 5});
						flag = false;
					}else if (data.status == "500") {
						layer.msg("没有找到dockerFile的基础镜像，请您检查是否正确",{icon: 5});
						flag = false;
					}
				}	 
        	});
        	return flag;
        }
	}
});





