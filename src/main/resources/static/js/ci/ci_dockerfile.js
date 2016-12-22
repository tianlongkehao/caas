$(document).ready(
		function() {
			
			var editor_one = CodeMirror.fromTextArea(document.getElementById("dockerFile"), {
                lineNumbers: true,
                matchBrackets: true,
                styleActiveLine: true,
                theme: "ambiance"
            });

			$("#dockerfile").focus();
			$("#dockerfile-import").hide();
			$("#dockerfile-export").hide();

			$("#buildBtn").click(function() {
				if (checkCiAdd(editor_one)) {
					$("#buildForm").submit();
					layer.load(0, {
						shade : [ 0.3, '#000' ]
					});
				}
				return false;
			});

			
			$(".btn-imageType .btns").each(function() {
				$(this).click(function() {
					$(".btn-imageType .btns").removeClass("active");
					$(this).addClass("active");
				});
			});

			// 导入模板文件选项对勾
			var dockerFile = null;
			$(document).on("click","#Path-table-doc tr", function () {
				$(this).parent().find("tr.focus").find("span.doc-tr").toggleClass("hide");
		        $(this).parent().find("tr.focus").toggleClass("focus");//取消原先选中行
		        //$("#Path-table>tbody>tr").parent().find("tr.focus").find("span.vals-path").removeClass("hide")
		        $(this).toggleClass("focus");//设定当前行为选中行
		        $(this).parent().find("tr.focus").find("span.doc-tr").toggleClass("hide");
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
				loadDockerFileTemplate();
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

			// 另存为模板
			$("#docExportBtn").click(function() {
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
							layer.tips('DockerFile不能为空', '#dockerFile', {
								tips : [ 1, '#3595CC' ]
							});
							$('#dockerFile').focus();
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
									layer.close(index);
								} else {
									layer.alert("DockerFile模板名称重复");
								}
							}
						});
					}
				})
			});
			
		});

	//多文件上传
	function selectFile(input) {
		var file_div = "";
		var obj_files = document.getElementById("sourceCode");
		var length = obj_files.files.length;
		for (var i = 0; i < obj_files.files.length; i++) {
			
			file_div += '<div class="alert alert-info alert-dismissable col-md-3" style="width: 176px; margin-left: 15px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; padding-top: 7px; padding-bottom: 7px;">'+
			'<button type="button" class="close" data-dismiss="alert" aria-hidden="true" onclick="delFile(this)">&times;</button><span>'+obj_files.files[i].name+'</span></div>';
		}
		$("#input-con").empty();
		$("#input-con").append(file_div);
	}
		
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
		                				"<input type='hidden' class='dockerFileTemplate' value='"+dockerFile.id+"' /></td>"+
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
	
	function checkCiAdd(editor_one){
        var imgNameLast = $("#imgNameLast").val().trim();
        var imgNameVersion = $("#imgNameVersion").val().trim();
        var projectName = $('#projectName').val().trim();
        var description = $('#description').val().trim();
        var dockerFile = editor_one.getValue();
    	var obj_files = document.getElementById("sourceCode");
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
        //验证镜像版本imgNameVersion
        if(imgNameVersion.length === 0){
            layer.tips('镜像版本不能为空', '#imgNameVersion', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#imgNameVersion').focus();
            return false;
        }
        if(imgNameVersion.search(/^[A-Za-z0-9-_]*$/) === -1){
            layer.tips('镜像版本只能由字母、数字、横线和下划线组成', '#imgNameVersion', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#imgNameVersion').focus();
            return false;
        }
        if(imgNameVersion.length > 128 || imgNameVersion.length < 1){
            layer.tips('镜像版本为1~128个字符', '#imgNameVersion', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#imgNameVersion').focus();
            return false;
        }
        
        
        // 验证填写的镜像名称是否重复
        var imageFlag = false;
        $.ajax({
    		url : ctx + "/ci/validciinfo.do",
    		async:false,
    		type: "POST",
    		data:{
    				/*"imgNameFirst":imgNameFirst,*/
    				"imgNameLast":imgNameLast,
    				"imgNameVersion":imgNameVersion
    		},
    		success : function(data) {
    			data = eval("(" + data + ")");
    			if (data.status=="400") {
    	            layer.tips('镜像版本重复', '#imgNameVersion', {
    	                tips: [1, '#0FA6D8'] //还可配置颜色
    	            });
    	            $('#imgNameVersion').focus();
    				imageFlag = true;
    			} 
    		}
    	});
        if (imageFlag) {
        	imageFlag = false;
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
        
        //验证是否上传程序包
        if (obj_files.files.length === 0) {
        	layer.tips('上传文件不能为空', '#sourceCode', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#sourceCode').focus();
            return false;
        }
        
        //验证dockerFile
        if (dockerFile.length === 0 ) {
        	layer.tips('dockerFile不能为空', '#dockerFiles',{
        		tips: [1,'##3595CC']
        	});
        	$('#dockerFiles').focus();
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
        
        return true;
    }

//删除已上传的某个文件
function delFile(obj){
	var thisText = $(obj).next().text();
	var obj_files = document.getElementById("sourceCode");
	var length = obj_files.files.length;
	for (var i = 0; i < length; i++) {
		if(thisText == obj_files.files[i].name){
			obj_files.onDelete(obj_files.files[i]);
		}
	}
/*	for(var p in obj_files.files){
		if (thisText == obj_files.files[p].name) {
			delete obj_files.files[p];
		}
	}*/
}