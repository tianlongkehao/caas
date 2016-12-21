$(document).ready(function () {
	//codeType
	$(".git-config").hide();
    $("#codeType").change(function(){
    	var codeType = $("#codeType").val();
    	if(codeType == 1){
    		$(".git-config").show();
    		$("#git-higher").show();
    		$(".git-higher").hide();
    	}else{
    		$(".git-config").hide();
    		$(".git-higher").hide();
    	}
    });
    //git-higher
    $("#git-higher").click(function(){
    	$(".git-higher").show();
    	$("#git-higher").hide();
    });
    
    var count = 0;
    //构建maven
    $("#maven").click(function(){
    	count++;
    	var maven = loadMaven(count);
    	$("#sortable").append(maven);
    });
    
    //maven高级按钮选项
    $(document).on('click','.maven-higherBtn',function(){
    	$(this).parent().hide();
    	$(this).parent().next('.maven-higherCon').removeClass("hide");
    });
    
    //构建ant
    $("#ant").click(function(){
    	count++;
    	var ant = loadAnt(count);
    	$("#sortable").append(ant);
    });
    
    //ant高级按钮选项
    $(document).on('click','.ant-higherBtn',function(){
    	$(this).parent().hide();
    	$(this).parent().next('.ant-higherCon').removeClass("hide");
    });
    
    //构建shell
    $("#shell").click(function(){
    	count++;
    	var shell = loadShell(count);
    	$("#sortable").append(shell);
    });
    
    //折叠ibox
    $(document).on('click','.collapse-link',function(){
        var ibox = $(this).closest('div.ibox');
        var button = $(this).find('i');
        var content = ibox.find('div.ibox-content');
        content.slideToggle(200);
        button.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
        ibox.toggleClass('').toggleClass('border-bottom');
        setTimeout(function () {
            ibox.resize();
            ibox.find('[id^=map-]').resize();
        }, 50);
        ibox.css("border-bottom","1px solid #dadada");
    });

    //关闭ibox
    $(document).on('click','.close-link',function(){
        var content = $(this).closest('div.addCiStepRow');
        content.remove();
    });
    
    //拖拽
    $( "#sortable" ).sortable({
        revert: true
    });
    //dockerfile路径&&dockerfile模板
    var dockerfilePathHtml = '<div class="row dockerfilePath">'+
							    '<div class="form-group col-md-12">'+
							    '<label class="c-project-tit">dockerfile路径</label>'+
							    '<textarea id="dockerFileLocation" name="dockerFileLocation" class="form-control c-project-con" type="text" required="" row="5"></textarea>'+
							'</div></div>';
    
    var dockerfileTempHtml = '<div class="row dockerfileTemp">'+
							    '<div class="form-group col-md-12">'+
									'<label class="c-project-tit" style="line-height:20px">编写dockerfile</label>'+
									'<span id="docImportBtn" class=" btn-info btn-sm" style="cursor: pointer">导入模板</span>'+
									'<span id="docExportBtn" class=" btn-info btn-sm" style="cursor: pointer;margin-left:5px;">另存为模板</span>'+
								'</div>'+
								'<div class="form-group col-md-12" id="dockerFiles" style="width:98%;margin-left:10px">'+
									'<textarea id="dockerFile" name="dockerFileContent"></textarea>'+
								'</div>'+
							'</div>';
    //生成dockerfile路径输入框
    $("#dockerfilePath").click(function(){
    	$("#dockerfileMethod").empty();
    	$("#dockerfileMethod").append(dockerfilePathHtml);
    });
    //生成dockerfile模板输入框
    var editor_one = null;
    $(document).on('click','#dockerfileTemp',function(){
    	$("#dockerfileMethod").empty();
    	$("#dockerfileMethod").append(dockerfileTempHtml);
    	
    	editor_one = CodeMirror.fromTextArea(document.getElementById("dockerFile"), {
            lineNumbers: true,
            matchBrackets: true,
            styleActiveLine: true,
            theme: "ambiance"
        });
    	$("#dockerfile").focus();
    });
    $("#dockerfileMethod").append(dockerfilePathHtml);
	$("#dockerfile-import").hide();
	$("#dockerfile-export").hide();

	
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
	$(document).on('click','#docImportBtn',function(){
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
	$(document).on('click','#docExportBtn',function(){
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
					layer.tips('DockerFile不能为空', '#dockerFiles', {
						tips : [ 1, '#3595CC' ]
					});
					$('#dockerFiles').focus();
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
	
	//添加认证按钮
	$("#addCredentialsCon").hide();
	$(document).on('click','#addCredentialsBtn',function(){
		delData();
		layer.open({
			type : 1,
			title : '添加认证',
			content : $("#addCredentialsCon"),
			btn : [ '添加', '取消' ],
			scrollbar:false,
			yes:function(index, layero){
				if (!judgeCredData()) {
	        		return;
	        	}
	        	var type = $("#CredentialsType").val();
				var username = $("#userNameCred").val();
				var password = $("#passwordCred").val();
				var privateKey = $("#SSHpasswordCred").val();
				var remark = $("#keyRemark").val();
				var code = type==1?"HTTP":"SSH";
				$.ajax({
					url : ctx + "/secret/addCredential.do",
					data : {
						"type" : type,
						"userName" : username,
						"password" : password,
						"privateKey" : privateKey,
						"remark" : remark
					},
					success : function(data) {
						data = eval("(" + data + ")");
						if (data.status == "200") {
							var html = "<option value='"+data.id+"'>"+username +" ("+code+") ("+remark+")"+"</option>"
							$("#codeCredentials").append(html);
							layer.alert("代码认证导入成功");
							layer.close(index);
						} else {
							layer.alert("代码认证导入失败");
						}
					}
				});
			}
		})
	});
	//选择认证类型
	$(".ssh").hide();
	$(document).on('change','#CredentialsType',function(){
		var credentialsType = $("#CredentialsType").val();
		if(credentialsType == 1){
			$(".normal").show();
			$(".ssh").hide();
			$("#SSHpasswordCred").val("");
		}else{
			$(".normal").hide();
			$(".ssh").show();
			$("#passwordCred").val("");
		}
	});
	
	//提交表单
	$("#buildBtn").click(function(){
        if(checkCodeCiAdd(editor_one)) {
        	$("#buildForm").submit();
        	layer.load(0, {shade: [0.3, '#000']});
        }
        return false;
    });
	
	//镜像信息
	$(".imageInfoCon").hide()
	
	$("#imageInfo").click(function(){
		$(".imageInfoCon").toggle();
	})
		
	
    
});/*ready*/
		
			
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

function loadAnt(count){
	var antHtml = '<div class="row addCiStepRow ant" count ='+count+' invoke ="ant">'+
	'<div class="col-sm-12">'+
	'<div class="ibox float-e-margins">'+
		'<div class="ibox-title">'+
			'<h5>ant</h5>'+
			'<div class="ibox-tools">'+
				'<a class="collapse-link">'+
                    '<i class="fa fa-chevron-up"></i>'+
                '</a>'+
				'<a class="close-link">'+
                    '<i class="fa fa-times"></i>'+
                '</a>'+
			'</div>'+
		'</div>'+
		'<div class="ibox-content">'+
			'<div class="row ant-config">'+
            	'<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">ant版本</label>'+
                    '<select id="antVersion-'+count+'" name="antVersion" class="form-control c-project-con" >'+
                    	'<option value="default">default</option>'+
                        '<option value="ant">ant</option>'+
                    '</select>'+
                '</div>'+
                '<div class="form-group col-md-12">'+
                   '<label class="c-project-tit">目标</label>'+
                    '<input id="antTargets-'+count+'" name="antTargets" type="text" class="form-control c-project-con" value="">'+
                '</div>'+
            '</div>'+
            '<div class="row ant-config">'+
            	'<button class="ant-higherBtn" type="button" style="float:right!important">高级...</button>'+
            '</div>'+
            '<div class="row ant-higherCon hide">'+
            	'<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">构建文件</label>'+
                    '<input id="antBuildFileLocation-'+count+'" name="antBuildFileLocation" type="text" class="form-control c-project-con" value="">'+
                '</div>'+
                '<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">性能</label>'+
                    '<input id="antProperties-'+count+'" name="antProperties" type="text" class="form-control c-project-con" value="">'+
                '</div>'+
                '<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">java选项</label>'+
                    '<input id="antJavaOpts-'+count+'" name="antJavaOpts" type="text" class="form-control c-project-con" value="">'+
                '</div></div></div></div></div></div>';
	return antHtml;
}

function loadMaven(count){
	var mavenHtml = '<div class="row addCiStepRow maven" count = '+count+' invoke = "maven">'+
	'<div class="col-sm-12">'+
	'<div class="ibox float-e-margins">'+
		'<div class="ibox-title">'+
			'<h5>maven</h5>'+
			'<div class="ibox-tools">'+
				'<a class="collapse-link">'+
                    '<i class="fa fa-chevron-up"></i>'+
                '</a>'+
				'<a class="close-link">'+
                    '<i class="fa fa-times"></i>'+
                '</a>'+
			'</div>'+
		'</div>'+
		'<div class="ibox-content">'+
			'<div class="row">'+
            	'<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">maven版本</label>'+
                    '<select id="mavenVersion-'+count+'" name="mavenVersion" class="form-control c-project-con" >'+
                    	'<option value="default">default</option>'+
                        '<option value="maven">maven</option>'+
                    '</select>'+
                '</div>'+
                '<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">目标</label>'+
                    '<input id="mavenGoals-'+count+'" name="mavenGoals" type="text" class="form-control c-project-con" value="">'+
                '</div>'+
                '<div class="row maven-config">'+
                	'<button class="maven-higherBtn" type="button" style="float:right!important">高级...</button>'+
                '</div>'+
                
                '<div class="maven-higherCon hide"><div class="form-group col-md-12">'+
                    '<label class="c-project-tit">POM</label>'+
                    '<input id="pomLocation-'+count+'" name="pomLocation" type="text" class="form-control c-project-con" value="">'+
                '</div>'+
                '<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">属性</label>'+
                    '<input id="mavenProperty-'+count+'" name="mavenProperty" type="text" class="form-control c-project-con" value="">'+
                '</div>'+
                '<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">JVM选项</label>'+
                    '<input id="mavenJVMOptions-'+count+'" name="mavenJVMOptions" type="text" class="form-control c-project-con" value="">'+
                '</div>'+
                '<div class="form-group col-md-12">'+
                    '<div style="line-height:34px">'+
                        '<label class="c-project-tit" title="使用私人maven存储库">使用私人maven存储库</label>'+
                        '<input type="checkbox" id = "isUserPrivateRegistry-'+count+'" name = "isUserPrivateRegistry" value = "0">'+
                    '</div>'+
                '</div>'+
                 '<div class="form-group col-md-12">'+
                    '<div style="line-height:34px">'+
                        '<label class="c-project-tit">注入建立变量</label>'+
                        '<input type="checkbox"  id="injectBuildVariables-'+count+'" name="injectBuildVariables" value="0">'+
                    '</div>'+
                '</div>'+
                '<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">配置文件</label>'+
                    '<select id="mavenSetFile-'+count+'" name="mavenSetFile" class="form-control c-project-con" >'+
                    	'<option value="use default maven setting">use default maven setting</option>'+
                        '<option value="settings file in filesystem">settings file in filesystem</option>'+
                    '</select>'+
                '</div>'+
                '<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">全局配置文件</label>'+
                    '<select id="mavenGlobalSetFile-'+count+'" name="mavenGlobalSetFile" class="form-control c-project-con" >'+
                    	'<option value="use default maven global setting">use default maven global setting</option>'+
                        '<option value="global settings file in filesystem">global settings file in filesystem</option>'+
                    '</select>'+
                '</div>'+
                '</div></div></div></div></div></div>';
	return mavenHtml;
}


function loadShell(count){
	shellHtml = '<div class="row addCiStepRow shell" count = '+count+' invoke = "shell">'+
	'<div class="col-sm-12">'+
	'<div class="ibox float-e-margins">'+
		'<div class="ibox-title">'+
			'<h5>Execute shell</h5>'+
			'<div class="ibox-tools">'+
				'<a class="collapse-link">'+
                    '<i class="fa fa-chevron-up"></i>'+
                '</a>'+
				'<a class="close-link">'+
                    '<i class="fa fa-times"></i>'+
                '</a>'+
			'</div>'+
		'</div>'+
		'<div class="ibox-content">'+
			'<div class="row">'+
                '<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">命令</label>'+
                    '<textarea id="executeShell-'+count+'" name="executeShell" class="form-control c-project-con" type="text" row="5"></textarea>'+
                '</div></div></div></div></div></div>';
	return shellHtml;
}

function checkCodeCiAdd(editor_one){
	
	//项目名称的判断
	var projectName = $("#projectName").val();
	var flagName = false;
	if(!projectName || projectName.length < 1){
      layer.tips('项目名称不能为空','#projectName',{tips: [1, '#3595CC']});
      $('#projectName').focus();
      return;
    }
    if(projectName.search(/^[a-z][a-z0-9-_]*$/) === -1){
      layer.tips('项目名称只能由小写字母、数字及横线下划线组成，且首字母只能为字母。','#projectName',{tips: [1, '#3595CC'],time: 3000});
      $('#projectName').focus();
      return;
    } 
    else {
    	$.ajax({
    		url : ctx + "/ci/judgeCodeCiName.do",
    		async:false,
    		type: "POST",
    		data:{"projectName":projectName},
    		success : function(data) {
    			data = eval("(" + data + ")");
    			if (data.status=="400") {
    	            layer.tips('项目名称重复', '#projectName', {
    	                tips: [1, '#0FA6D8'] 
    	            });
    	            $('#projectName').focus();
    	            flagName = true;
    			} 
    		}
    	});
    }
    if (flagName) {
    	flagName = false;
    	return false;
    }
    
    
    //项目描述的判断
    var description = $("#description").val();
    if (!description || description.length < 1) {
    	layer.tips('项目简介不能为空','#description',{tips:[1,'#3595CC']});
    	$("#description").focus();
    	return;
    }
    
    //判断代码库类型
    var codeType = $("#codeType").val();
    if (codeType == 1) {
    	//判断代码仓库地址
    	var codeUrl = $("#codeUrl").val();
    	if(!codeUrl || codeUrl.length < 1){
    		layer.tips('代码仓库地址不能为空','#codeUrl',{tips: [1, '#3595CC']});
    		$('#codeUrl').focus();
    		return;
	    }
    	
    	//代码地址通过代码验证是否可以通过验证
    	var codeCredentialId = $("#codeCredentials").val();
    	var flagUrl = false;
    	$.ajax({
    		url : ctx + "/ci/judgeCodeUrl.do",
    		async:false,
    		type: "POST",
    		data:{"codeUrl":codeUrl,"codeCredentialId":codeCredentialId},
    		success : function(data) {
    			data = eval("(" + data + ")");
    			if (data.status=="400") {
    	            layer.tips('代码仓库地址验证失败，请您检查是否有误', '#codeUrl', {
    	                tips: [1, '#0FA6D8'] 
    	            });
    	            $('#codeUrl').focus();
    	            flagUrl = true;
    			} 
    		}
    	});
    	if (flagUrl) {
    		flagUrl = false;
        	return false;
        }
    	
    	//判断代码分支
    	var codeBranch = $("#codeBranch").val();
    	if(!codeBranch || codeBranch.length < 1){
    		layer.tips('代码分支不能为空','#codeBranch',{tips: [1, '#3595CC']});
    		$('#codeBranch').focus();
    		return;
	    }
    	if ($("#HookCode").prop("checked")==true) {
    		$("#isHookCode").val("1");
    	}
    }
    if (codeType == 0) {
    	$("#codeUrl").val("");
    	$("#codeBranch").val("");
    	$("#codeCredentials").val("");
    	$("#codeUsername").val("");
    	$("#codePassword").val("");
    }
    
    //判断构建
    var flag = false;
    var json = "";
    $("#sortable >div").each(function(){
    	debugger;
    	if (flag) {
    		return false;
    	}
        var count = $(this).attr("count");
        var invoke = $(this).attr("invoke");
        //maven构建的判断以及数据的封装；
        if (invoke == "maven") {
        	//maven目标判断
        	var goals = "#mavenGoals-" + count;
        	var mavenGoals = $(goals).val();
        	if(!mavenGoals || mavenGoals.length < 1){
        		layer.tips('目标不能为空',goals,{tips: [1, '#3595CC']});
        		$(goals).focus();
        		flag = true;
        		return;
    	    }
        	var mvnVerId = "#mavenVersion-" + count;
        	var pomId = "#pomLocation-" + count;
        	var mvnProId = "#mavenProperty-" + count;
        	var mvnJvmId = "#mavenJVMOptions-" + count;
        	var isUPRId = "#isUserPrivateRegistry-" + count;
        	var ijBVId = "#injectBuildVariables-" + count;
        	var mvnSFId = "#mavenSetFile-" + count;
        	var MVnGSFID = "#mavenGlobalSetFile-" + count;
        	
        	var invokeType = 2;
        	var mavenVersion = $(mvnVerId).val();
        	var pomLocation = $(pomId).val();
        	var mavenProperty = $(mvnProId).val();
        	var mavenJVMOptions = $(mvnJvmId).val();
        	var isUserPrivateRegistry = 0 ;
        	var injectBuildVariables = 0 ; 
        	var mavenSetFile = $(mvnSFId).val();
        	var mavenGlobalSetFile = $(MVnGSFID).val();
        	//maven存储库的判断
        	if($(isUPRId).prop("checked")==true){
        		isUserPrivateRegistry = 1;
            }
        	//注入建立变量的判断
        	if($(ijBVId).prop("checked")==true){
        		injectBuildVariables = 1;
            }
        	var jsonData= '{ "invokeType": "'+invokeType+'","mavenGoals":"'+mavenGoals+'", "mavenVersion":"'+mavenVersion+'", "pomLocation": "'+pomLocation+'",'+
        					' "mavenProperty":"'+mavenProperty+'","mavenJVMOptions": "'+mavenJVMOptions+'","isUserPrivateRegistry":"'+isUserPrivateRegistry+'",'+
        					' "injectBuildVariables":"'+injectBuildVariables+'","mavenSetFile":"'+mavenSetFile+'","mavenGlobalSetFile":"'+mavenGlobalSetFile+'"},';
        	json += jsonData;
        }
        
        if (invoke == "ant") {
        	//ant目标判断
        	var target = "#antTargets-" + count;
        	var antTargets = $(target).val();
        	if(!antTargets || antTargets.length < 1){
        		layer.tips('ant目标不能为空',target,{tips: [1, '#3595CC']});
        		$(target).focus();
        		flag = true;
        		return;
    	    }
        	
        	var antVId = "#antVersion-" + count;
        	var antBFLId = "#antBuildFileLocation-" + count;
        	var antPId = "#antProperties-" + count;
        	var antJOId = "#antJavaOpts-" + count;
        	
        	var invokeType = 1;
        	var antVersion = $(antVId).val();
        	var antBuildFileLocation = $(antBFLId).val();
        	var antProperties = $(antPId).val();
        	var antJavaOpts = $(antJOId).val();
        	
        	var jsonData= '{ "invokeType": "'+invokeType+'", "antTargets":"'+antTargets+'", "antVersion":"'+antVersion+'", "antBuildFileLocation": "'+antBuildFileLocation+'",'+
							' "antProperties":"'+antProperties+'","antJavaOpts":"'+antJavaOpts+'"},';
        	json += jsonData;
        }
        
        if (invoke == "shell") {
        	//shell脚本的判断
        	var shellId = "#executeShell-" + count ;
        	var executeShell = $(shellId).val();
        	if (!executeShell || executeShell.length < 1) {
        		layer.tips('shell脚本不能为空',shellId,{tips: [1, '#3595CC']});
        		$(shellId).focus();
        		flag = true;
        		return;
        	}
        	var invokeType = 3;
        	var jsonData= '{ "invokeType": "'+invokeType+'", "shellCommand":"'+executeShell+'"},';
			json += jsonData;
        }
    });
    
    if (flag) {
    	return false;
    }
    
    json = json.substring(0, json.length-1);
    json = '[' + json + ']';
    $("#jsonData").val(json);
    
    if ($("#imageInfo").prop("checked")) {
    	var imageName = $("#imageName").val();
		if(!imageName || imageName.length < 1){
			layer.tips('镜像名称不能为空','#imageName',{tips: [1, '#3595CC']});
			$('#imageName').focus();
			return;
	    }
		var imgNameVersion = $("#imgNameVersion").val();
		if (imgNameVersion || imageName.length > 0) {
			if(imgNameVersion.search(/^[A-Za-z0-9-_]*$/) === -1){
				layer.tips('镜像版本只能由字母、数字、横线和下划线组成', '#imgNameVersion', {
					tips: [1, '#0FA6D8'] //还可配置颜色
				});
				$('#imgNameVersion').focus();
				return false;
			}
		}
		
		//判断是否为基础镜像
	    if ($("#baseImage").prop("checked")) {
	    	$("#isBaseImage").val(1);
	    }
	    else{
	    	$("#isBaseImage").val(2);
	    }
	    //判断是否为公有镜像
	    if ($("#imageType").prop("checked")) {
	    	$("#imgType").val(1);
	    }
	    else {
	    	$("#imgType").val(2);
	    }
	    
	    //doackerFile文件路径的判断
		var dockerFileLocation = $("#dockerFileLocation").val();
		if (dockerFileLocation != undefined) {
			if(!dockerFileLocation || dockerFileLocation.length < 1){
				layer.tips('dockerfile文件路径不能为空','#dockerFileLocation',{tips: [1, '#3595CC']});
				$('#dockerFileLocation').focus();
				return;
			}
		}
		if (editor_one != null) {
			var dockerFile = editor_one.getValue();
			if (dockerFile != undefined) {
				if(!dockerFile || dockerFile.length < 1){
					layer.tips('dockerfile模板不能为空','#dockerFiles',{tips: [1, '#3595CC']});
					$('#dockerFiles').focus();
					return;
				}
			}
		}
    }
    else {
    	$("#imageName").val("");
    	$("#imgNameVersion").val("");
    	$("#dockerFileLocation").val("");
    	if (editor_one != null) {
    		editor_one.setValue("");
    	}
    }
    
    return true;
}

//数据格式判断
function judgeCredData(){
	var type = $("#CredentialsType").val();
	var username = $("#userNameCred").val();
	var password = $("#passwordCred").val();
	var privateKey = $("#SSHpasswordCred").val();
	var remark = $("#keyRemark").val();
	if (!username || username.length < 1) {
    	layer.tips('用户名不能为空','#userNameCred',{tips:[1,'#3595CC']});
    	$("#userNameCred").focus();
    	return;
	}
	var code = "";
	if (type == 1 ) {
		code = "HTTP";
		if (!password || password.length < 1) {
	    	layer.tips('密码不能为空','#passwordCred',{tips:[1,'#3595CC']});
	    	$("#passwordCred").focus();
	    	return;
		}
	}
	if (type == 2 ) {
		code = "SSH";
		if (!privateKey || privateKey.length < 1) {
			layer.tips('密钥不能为空','#SSHpasswordCred',{tips:[1,'#3595CC']});
			$("#SSHpasswordCred").focus();
			return;
		}
	}
	if (!remark || remark.length < 1) {
    	layer.tips('描述信息不能为空','#keyRemark',{tips:[1,'#3595CC']});
    	$("#keyRemark").focus();
    	return;
	}
	return true;
}

function delData(){
	$("#CredentialsType").val(1);
	$(".normal").show();
	$(".ssh").hide();
	$("#userNameCred").val("");
	$("#keyRemark").val("");
	$("#passwordCred").val("");
	$("#SSHpasswordCred").val("");
}


