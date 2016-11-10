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
		if(checkDockerfile()){
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
						setTimeout('window.location.href=""+ctx+"/template/dockerfile"',1500);
					} else {
						layer.alert("DockerFile模板修改失败！");
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




