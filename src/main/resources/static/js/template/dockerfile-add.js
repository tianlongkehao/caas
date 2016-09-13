$(document).ready(function(){
	var editor_one = CodeMirror.fromTextArea(document.getElementById("dockerFile"), {
        lineNumbers: true,
        matchBrackets: true,
        styleActiveLine: true,
        theme: "ambiance"
    });
	$("#dockerfile").focus();
	
	
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
				dockerFile = $(this).parent().find("tr.focus").find(
						".dockerFileTemplate").val();
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
		layer.open({
			type : 1,
			title : '另存为模板',
			content : $("#dockerfile-export"),
			btn : [ '保存', '取消' ],
			yes : function(index, layero) {
				var templateName = $("#dockerFileTemplateName").val();
				if (templateName == null || templateName == "") {
					layer.tips('模板名称不能为空', '#dockerFileTemplateName', {
						tips : [ 1, '#3595CC' ]
					});
					$('#dockerFileTemplateName').focus();
					return;
				}
				var dockerFile = editor_one.getValue();
				if (dockerFile == null || dockerFile == "") {
					layer.tips('DockerFile不能为空', '#dockerfiletext', {
						tips : [ 1, '#3595CC' ]
					});
					$('#dockerfiletext').focus();
					layer.close(index);
					return;
				}

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
							layer.alert("DockerFile模板导入成功");
							window.location.href=""+ctx+"/template/dockerfile";
						} else {
							layer.alert("DockerFile模板名称重复");
						}
					}
				});
			}
		})
	})
	
});




