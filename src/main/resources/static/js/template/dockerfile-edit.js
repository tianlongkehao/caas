$(document).ready(function(){
	var editor_one = CodeMirror.fromTextArea(document.getElementById("dockerFile"), {
        lineNumbers: true,
        matchBrackets: true,
        styleActiveLine: true,
        theme: "ambiance"
    });
	$("#dockerfile").focus();
	
	$("#buildStorage").click(function() {
		var dockerFile = editor_one.getValue();
		var dockerFileId = $("#dockerFileId").val();
		if (dockerFile == null || dockerFile == "") {
			layer.tips('DockerFile不能为空', '#dockerFile', {
				tips : [ 1, '#3595CC' ]
			});
			$('#dockerFile').focus();
			layer.close(index);
			return;
		}

		$.ajax({
			url : ctx + "/template/dockerfile/modify.do",
			type : "POST",
			data : {
				"dockerFileId" : dockerFileId,
				"dockerFile" : dockerFile
			},
			success : function(data) {
				data = eval("(" + data + ")");
				if (data.status == "200") {
					layer.alert("DockerFile模板修改成功");
					window.location.href=""+ctx+"/template/dockerfile";
				} else {
					layer.alert("DockerFile模板修改失败！");
				}
			}
		});
	})
	
});




