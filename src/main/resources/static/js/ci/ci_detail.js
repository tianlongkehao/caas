 var count = 0;
 var editor_one = null;
$(document).ready(function(){
    $(".ci-tab").click(function(){
        $(".code-tabmain").children().addClass("hide");
        $(".code-tabmain").children().eq($(this).index()).removeClass("hide");
        $(".ci-tab").removeClass("active");
        $(this).addClass("active");
    });
    changeBaseImageVersion();
    registerCiRecordEvent();
    registerDeployEvent();
    registerConstructCiEvent();
    //修改事件
    registerCiEditEvent();
    //删除事件
    registerCiDelEvent($("#id").val());
    //删除hookcode
    registerHookCode($("#id").val());
    //加载构建日志
    // printLog();
    //停止构建执行
    stopCodeCi();
	//删除一个执行
    deleteCodeCi();

    //动态给版本信息赋宽度
    if(printLog()){
    	var btnVersionWidth = $(".btn-version").html().length*14;
        $(".btn-version").css("width",btnVersionWidth);
    }

    //是否选中hook
    if ($("#isHookCode").val() == 1) {
    	document.getElementById("HookCode").checked=true;
    }

    if ($("#imageName").val()) {
    	document.getElementById("imageInfo").checked=true;
    }
    else {
    	$(".imageInfoCon").toggle();
    }

    if ($("#isBaseImage").val() == 1) {
    	document.getElementById("baseImage").checked=true;
    }

    if ($("#imgType").val() == 1) {
    	document.getElementById("imageType").checked=true;
    }

	//codeType
	$(".git-config").hide();
    $("#codeType").change(function(){
    	changeCodeType();
    });
    changeCodeType();

    //git-higher
    $("#git-higher").click(function(){
    	$(".git-higher").show();
    	$("#git-higher").hide();
    });

    //加载invoke中的数据；
    loadInvokeData();

	//sonar配置
	if ($("#sources").val()) {
		$("#ci-sonarInfo").prop("checked",true);
		$("#sonarCheck").val("1");
		$(".sonarInfo").show();
    } else {
		$("#sonarCheck").val("0");
		$(".sonarInfo").hide();
    }

	$("#ci-sonarInfo").click(function() {
		if ($('#ci-sonarInfo').is(":checked")) {
			$("#sonarCheck").val("1");
			$(".sonarInfo").show();
		} else {
			$("#sonarCheck").val("0");
			$(".sonarInfo").hide();
			$("#sources").val("");
		}
	});

    //构建maven
    $("#maven").click(function(){
    	count++;
    	loadMaven(count);
    	//$("#sortable").append(maven);
    });

    //maven高级按钮选项
    $(document).on('click','.maven-higherBtn',function(){
    	$(this).parent().hide();
    	$(this).parent().next('.maven-higherCon').removeClass("hide");
    });

    //构建ant
    $("#ant").click(function(){
    	count++;
    	ant = loadAnt(count);
    	//$("#sortable").append(ant);
    });

    //ant高级按钮选项
    $(document).on('click','.ant-higherBtn',function(){
    	$(this).parent().hide();
    	$(this).parent().next('.ant-higherCon').removeClass("hide");
    });
    //显示和隐藏提示
	$(document).on('click','.questionInfoBtn',function(){
		$(this).parent().parent().parent().find("div.questionInfoCon").toggle();
	});
	$(document).on('click','.fa-questionBtn',function(){
		$(this).parent().next().toggle();
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
        var content = $(this).closest('div.ibox');
        content.parent().parent().remove();
    });

	//拖拽
	$( "#sortable" ).sortable({
		revert: true
	});

	//dockerfile路径&&dockerfile模板
    var dockerfilePathHtml = '<div class="row dockerfilePath">'+
							    '<div class="form-group1 col-md-12">'+
							    '<label class="c-project-tit">dockerfile路径</label>'+
							    '<textarea id="dockerFileLocation" name="dockerFileLocation" class="form-control c-project-con" type="text" required="" row="5"></textarea>'+
							'</div></div>';

    var dockerfileTempHtml = '<div class="row dockerfileTemp">'+
							    '<div class="form-group1 col-md-12">'+
									'<label class="c-project-tit" style="line-height:20px">编写dockerfile</label>'+
									'<span id="docImport-btn" class=" btn-info btn-sm" style="cursor: pointer">导入模板</span>'+
									'<span id="docExport-btn" class=" btn-info btn-sm" style="cursor: pointer;margin-left:5px;">另存为模板</span>'+
								'</div>'+
								'<div class="form-group col-md-12" id="dockerFiles2" style="width:98%;margin-left:10px">'+
									'<textarea id="dockerFileContentEdit" name="dockerFileContentEdit"></textarea>'+
								'</div>'+
							'</div>';
    //点击dockerfile构建方式-编写dockerfile
    $("#dockerfilePath").click(function(){
    	$(".changeDockerfileM").find("a").css("background-color","#fff");
    	$(this).css("background-color","#ddd");
    	$("#dockerfileMethod").empty();
    	$(".dockerfileTools").addClass("hide");
    	$("#dockerfileMethod").append(dockerfilePathHtml);
    });

    var dockerfileMethod = $("#ciMethod").val();

    //点击dockerfile构建方式-编写dockerfile
    $("#dockerfileTemp").click(function(){
    //$(document).on('click','#dockerfileTemp',function(){
    	$(".changeDockerfileM").find("a").css("background-color","#fff");
    	$(this).css("background-color","#ddd");
    	$("#dockerfileMethod").empty();
    	$(".dockerfileTools").removeClass("hide");
    	$("#dockerfileMethod").append(dockerfileTempHtml);

    	editor_one = CodeMirror.fromTextArea(document.getElementById("dockerFileContentEdit"), {
            lineNumbers: true,
            matchBrackets: true,
            styleActiveLine: true,
            theme: "ambiance"
        });
    	//loadCiToolsChecked();

    	//勾选工具的执行语句添加到dockerfile中
		editor_one.setValue('');
		var checkedTool = $(".toolChk:checked");
		var allToolCode = $("#basicImage").val()+ "\n";
		var allToolId = "";
		if(checkedTool.length == 0){
			allToolCode = "";
			allToolId = "";
			$("#ciTools").val(allToolId);
		}else{
			for(var j=0; j < checkedTool.length; j++){
				var checkedToolCode = checkedTool[j].attributes.toolcode.value;
				var checkedToolId = checkedTool[j].id;
				allToolCode += checkedToolCode + "\n";
				allToolId +=checkedToolId + ",";
			}
			$("#ciTools").val(allToolId.substring(0, allToolId.length-1));
		}
		editor_one.setValue(allToolCode);
    	//editor_one.setValue(dockerfileMethod);
    });

	$("#dockerfile").focus();
	$("#dockerfile-import").hide();
	$("#dockerfile-export").hide();

	$(".btn-imageType .btns").each(function() {
		$(this).click(function() {
			$(".btn-imageType .btns").removeClass("active");
			$(this).addClass("active");
		});
	});

	//点击基本设置tab时加载数据
	$(".create-set").click(function(){
		var dockerFileLocation = $("#ciLocation").val();

		if (dockerFileLocation != '' ) {
			$("#dockerfilePath").click();
			$("#dockerFileLocation").val(dockerFileLocation);
		}
		if (dockerfileMethod != '' && dockerfileMethod != undefined) {
			$("#dockerfileTemp").click();
			editor_one.setValue(dockerfileMethod);
		}
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
	$(document).on('click','#docImport-btn',function(){
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
		});
	});

	// 另存为模板
	$(document).on('click','#docExport-btn',function(){
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
					layer.tips('DockerFile不能为空', '#dockerFiles2', {
						tips : [ 1, '#3595CC' ]
					});
					$('#dockerFiles2').focus();
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
		});
	});

	//添加认证按钮
	$("#addCredentialsCon").hide();
	$(document).on('click','#addCredentialsBtn',function(){
		layer.open({
			type : 1,
			title : '添加认证',
			content : $("#addCredentialsCon"),
			area: ['500px'],
			btn : [ '添加', '取消' ],
			scrollbar:false,
			yes:function(index, layero){
				if (!judgeCredData()) {
					return;
				}
				var type = $("#CredentialsType").val();
				var codeType = $("#codeType").val();
				var username = $("#userNameCred").val();
				var password = $("#passwordCred").val();
				var privateKey = $("#privateKey").val();
				var remark = $("#keyRemark").val();
				var code = type==1?"HTTP":"SSH";
				$.ajax({
					url : ctx + "/secret/addCredential.do",
					data : {
						"type" : type,
						"codeType" : codeType,
						"userName" : username,
						"password" : password,
						"privateKey" : privateKey,
						"remark" : remark
					},
					success : function(data) {
						data = eval("(" + data + ")");
						if (data.status == "200") {
							if(type == 2){
								$("#sshPassword").val(data.sshKey);
								layer.open({
									type : 1,
									title : 'ssh密钥',
									content : $("#sshPwdInfo"),
									area : ['500px'],
									btn : ['确认'],
									scrollbar : false,
									yes : function(index, layero) {
										var html = "<option value='"+data.id+"'>"+username +" ("+code+") ("+remark+")"+"</option>";
										$("#codeCredentials").append(html);
										layer.closeAll();
									}
								});
							} else {
								var html = "<option value='"+data.id+"'>"+username +" ("+code+") ("+remark+")"+"</option>";
								$("#codeCredentials").append(html);
								layer.closeAll();
							}
						} else {
							layer.alert("代码认证导入失败");
						}
					}
				});
			}
		});
	});
//	$(document).on('click','#addCredentialsBtn',function(){
//		layer.open({
//			type : 1,
//			title : '添加认证',
//			area: ['500px'],
//			content : $("#addCredentialsCon"),
//			btn : [ '添加', '取消' ],
//			scrollbar:false,
//			yes:function(index, layero){
//				var type = $("#CredentialsType").val();
//				var codeType = $("#codeType").val();
//				var username = $("#userNameCred").val();
//				var password = $("#passwordCred").val();
//
//				var remark = $("#keyRemark").val();
//				if (!username || username.length < 1) {
//			    	layer.tips('用户名不能为空','#userNameCred',{tips:[1,'#3595CC']});
//			    	$("#userNameCred").focus();
//			    	return;
//				}
//				var code = "";
//				if (type == 1 ) {
//					code = "HTTP";
//					if (!password || password.length < 1) {
//				    	layer.tips('密码不能为空','#passwordCred',{tips:[1,'#3595CC']});
//				    	$("#passwordCred").focus();
//				    	return;
//					}
//				}
//				if (type == 2 ) {
//					code = "SSH";
//					if (!privateKey || privateKey.length < 1) {
//						layer.tips('密钥不能为空','#SSHpasswordCred',{tips:[1,'#3595CC']});
//						$("#SSHpasswordCred").focus();
//						return;
//					}
//				}
//				if (!remark || remark.length < 1) {
//			    	layer.tips('描述信息不能为空','#keyRemark',{tips:[1,'#3595CC']});
//			    	$("#keyRemark").focus();
//			    	return;
//				}
//				$.ajax({
//					url : ctx + "/secret/addCredential.do",
//					data : {
//						"type" : type,
//						"codeType" : codeType,
//						"userName" : username,
//						"password" : password,
//						"privateKey" : privateKey,
//						"remark" : remark
//					},
//					success : function(data) {
//						data = eval("(" + data + ")");
//						if (data.status == "200") {
//							var html = "<option value='"+data.id+"'>"+username +" ("+code+") ("+remark+")"+"</option>";
//							$("#codeCredentials").append(html);
//							layer.alert("代码认证导入成功");
//							layer.close(index);
//						} else {
//							layer.alert("代码认证导入失败");
//						}
//					}
//				});
//			}
//
//		});
//	});
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
	//镜像信息
	$("#imageInfo").click(function(){
		$(".imageInfoCon").toggle();
	});
	//工具集同组单选
	$(".toolChk").click(function(){
		var checkedName = $(this).attr("name");
		var checkedName = "'"+checkedName+"'";
		var ccc = '.toolChk[name= '+checkedName+']';
		var sameNameCount = $(ccc).length;

		if($(this).is(":checked")){
			var allToolCode = "";
			var editorVal = editor_one.getValue();
			var toolCode = $(this).attr("toolCode");
			//勾选
			for(var i=0; i< sameNameCount; i++){
				  $(ccc).prop("checked",false);
			}
			$(this).prop('checked', true);
		}
		//勾选工具的执行语句添加到dockerfile中
		editor_one.setValue('');
		var checkedTool = $(".toolChk:checked");
		var allToolCode = $("#basicImage").val()+ "\n";
		var allToolId = "";
		if(checkedTool.length == 0){
			allToolCode = "";
			allToolId = "";
			$("#ciTools").val(allToolId);
		}else{
			for(var j=0; j < checkedTool.length; j++){
				var checkedToolCode = checkedTool[j].attributes.toolcode.value;
				var checkedToolId = checkedTool[j].id;
				allToolCode += checkedToolCode + "\n";
				allToolId +=checkedToolId + ",";
			}
			$("#ciTools").val(allToolId.substring(0, allToolId.length-1));
		}
		editor_one.setValue(allToolCode);
	});
	loadCiToolsChecked();
});/*ready*/

//停止一个构建执行
function stopCodeCi(){
	$("#stopCodeCi").click(function(){
		var projectName = $(this).attr("projectName");
		var executionId = $(this).attr("executionId");
		$.ajax({
			url : ctx + "/ci/stopCodeCi.do",
			type : "POST",
			data : {
				"projectName" : projectName,
				"executionId" : executionId
			},
			success : function(data) {
				data = eval("(" + data + ")");
				if (data.status == "200") {
					window.location.reload();
				} else {
					layer.alert("停止构建失败");
				}
			}
		});
	});
}

//删除一个构建执行
function deleteCodeCi(){
	$(".deleteCodeCi").click(function(){
		var projectName = $(this).attr("projectName");
		var executionId = $(this).attr("executionId");
		var ciRecordId = $(this).attr("ciRecordId");
		$.ajax({
			url : ctx + "/ci/deleteCodeCi.do",
			type : "POST",
			data : {
				"projectName" : projectName,
				"executionId" : executionId,
				"ciRecordId" :ciRecordId
			},
			success : function(data) {
				data = eval("(" + data + ")");
				if (data.status == "200") {
					window.location.reload();
				} else {
					layer.alert("删除当前构建失败");
				}
			}
		});
	});
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
	                			"</tr>";
                	}
	            }
	            if (html == "") {
	            	html += '<tr><td>没有保存的模板</td></tr>';
	            }
	            $("#dockerfile-body").empty();
	            $("#dockerfile-body").append(html);
  			}
	 });
}

//代码管理信息的展示
function changeCodeType() {
	var codeType = $("#codeType").val();
	if (codeType == 0 ) {
		$(".git-config").hide();
		$(".git-higher").hide();
	}
	else {
		loadCredentialData(codeType);
		$(".git-config").show();
		$("#git-higher").show();
		$(".git-higher").hide();
	}
}

//加载credential数据
var credentialId = 0;
function loadCredentialData (codeType) {
	if (codeType == 2 ) {
		$("#addHook").addClass("hide");
	}
	else {
		$("#addHook").removeClass("hide");
	}
	credentialId = $("#credentialId").val();
	$.ajax({
		url : ctx + "/secret/loadCredentialData.do?codeType="+codeType,
		success : function(data){
			data = eval("("+data+")");
			var html = "";
			if (data != null) {
				for (var i in data.data) {
					var credential = data.data[i];
					if (credential.id == credentialId ) {
						if (credential.type == 1) {
							html = '<option value="'+credential.id +'">'+ credential.userName +' (HTTP) (' + credential.remark +')</option>' + html;
						}
						else {
							html = '<option value="'+credential.id +'">'+ credential.userName +' (SSH) (' + credential.remark +')</option>' + html;
						}
					}
					else {
						if (credential.type == 1) {
							html += '<option value="'+credential.id +'">'+ credential.userName +' (HTTP) (' + credential.remark +')</option>';
						}
						else {
							html += '<option value="'+credential.id +'">'+ credential.userName +' (SSH) (' + credential.remark +')</option>';
						}
					}

				}
				$("#codeCredentials").html(html);
			}
		}
	});

}

//加载构建invoke的原始数据
function loadInvokeData() {
	var id = $("#id").val();
	$.ajax({
		url:ctx+"/ci/invokeData.do",
		async:false,
		type: "POST",
		data:{"id":id},
		success:function(data){
			data = eval("(" + data + ")");
			var invokeList = data.data;
			if (invokeList.length >0) {
				for (var i = 0; i<invokeList.length;i++ ) {
					var invoke = invokeList[i];
					var order = invoke.jobOrderId;
					if (invoke.invokeType == 2) {
						//加载是maven类型的构建数据
						loadMavenData(order,invoke);
				    	//判断checkbox选择框是否选中
				    	if (invoke.isUserPrivateRegistry == 1 ) {
				    		var isUPR = "isUserPrivateRegistry-"+order;
				    		document.getElementById(isUPR).checked = true;
				    	}
				    	if (invoke.injectBuildVariables == 1) {
				    		var inBV = "injectBuildVariables-"+order;
				    		document.getElementById(inBV).checked = true;
				    	}
					}
					if (invoke.invokeType == 1) {
						//加载ant类型的构建数据
						loadAntData(order,invoke);
					}
					if (invoke.invokeType == 3) {
						//加载shell类型的构建数据
						loadShellData(order,invoke);
					}
					count = order;
				}
			}
		}
	});
}

function registerDeployEvent(){
	$("#deploy").unbind("click").click(function(){
		imgId = $(this).attr("imgId");
		if(imgId!=null&&imgId>0){
			window.open(ctx+"/registry/detail/"+imgId);
		}
	});

	$("#replayci").unbind("click").click(function(){
		ciId = $(this).attr("ciId");
		var ciList = $("#ciRecordList").children().length;
		if(ciList == 0){
			replayciEvent(ciId);
		}else {
			var ciStatus = $(".ciStatus")[0].innerHTML;
			if(ciStatus == "构建中"){
		    	$("#replayci").css("cursor","no-drop");
			}else{
				replayciEvent(ciId);
			}
	    }
	});
}

function replayciEvent(ciId){
	$.ajax({
		url:ctx+"/ci/constructCi.do?id="+ciId,
		async:true,
		success:function(data){
			data = eval("(" + data + ")");
			if(data.status=="200"){
				window.location.reload();
			}else{
				layer.alert(data.msg);
			}
			setTimeout('window.location.reload()',2000);
		}
	});
	setTimeout('window.location.reload()',2000);
}

function registerConstructCiEvent(){
	$("#buildBtn").unbind("click").click(function(){
		if($(this).attr("constructionStatus")=="2"){
			return;
		}
		var $this = $(this);
		var id = $this.attr("ciId");
		layer.open({
	        title: '快速构建',
	        content: '确定构建镜像？',
	        btn: ['确定', '取消'],
	        yes: function(index, layero){
	        	$(this).unbind("click");
	        	layer.close(index);
	        	$.ajax({
	        		url:ctx+"/ci/constructCi.do?id="+id,
	        		success:function(data){
	        			data = eval("(" + data + ")");
	       			 	if(data.status=="200"){
	       			 		layer.alert("构建成功");
	       			 	}else{
	       			 		layer.alert(data.msg);
	       			 	}
	       			 	window.location.reload();
	        		},
	        		error:function(){
	        			layer.alert("系统错误，请联系管理员");
	        		}
	        	});
	        	window.location.reload();
	        },
	        cancel: function(index){
	        }
	    });
	});
}


function registerCiEditEvent(){
	$("#editCiBtn").click(function(){
		if (checkCodeCiAdd()) {
			var load = layer.load(0, {shade: [0.3, '#000']});
			$("#editCiForm").ajaxSubmit({
				url: ctx+"/ci/modifyCodeCi.do",
				type: "post",
				success: function (data) {
					data = eval("(" + data + ")");
					if (data.status == "200") {
						layer.alert("修改成功");
						$("#projectNameSpan").text($("#projectName").val());
					} else {
						layer.alert("修改失败"+data.msg);
					}
					layer.close(load);
				},
				error: function (e) {
					layer.alert("请求出错");
				}
			});
		}
    });
	$("#editCiUploadBtn").click(function(){
		if (checkQuickCiAdd()) {
			var index = layer.load(0, {shade: [0.3, '#000']});
			$("#editCiUploadForm").ajaxSubmit({
				url: ctx+"/ci/modifyDockerFileCi.do",
				type: "post",
				success: function (data) {
					layer.close(index);
					data = eval("(" + data + ")");
					if (data.status == "200") {
						layer.alert("修改成功");
						$("#projectNameSpan").text($("#projectName").val());
					} else {
						layer.alert("修改失败"+data.msg);
					}
				},
				error: function (e) {
					layer.close(index);
					layer.alert("请求出错");
				}
			});
		}
    });

	$("#editCiUploadCodeBtn").click(function(){
		if (checkQuickCiAdd()) {
			var index = layer.load(0, {shade: [0.3, '#000']});
			$("#editCiUploadCodeForm").ajaxSubmit({
				url: ctx+"/ci/modifyCodeResourceCi.do",
				type: "post",
				success: function (data) {
					layer.close(index);
					data = eval("(" + data + ")");
					if (data.status == "200") {
						layer.alert("修改成功");
						$("#projectNameSpan").text($("#projectName").val());
					} else {
						layer.alert("修改失败"+data.msg);
					}
				},
				error: function (e) {
					layer.close(index);
					layer.alert("请求出错");
				}
			});
		}
    });

	$("#baseImageName").change(function(){
		changeBaseImageVersion();
	});
}

function changeBaseImageVersion () {
	var baseImageName = $("#baseImageName").val();
	var baseVersion = $("#ownBase").html();
	$.ajax({
		url:""+ctx+"/ci/findBaseImageVersion.do",
		type:"post",
		data:{"baseImageName":baseImageName},
		success: function (data) {
            data = eval("(" + data + ")");
            var html = "";
            if (data != null) {
            	if (data['data'].length > 0) {
            		for (var i in data.data) {
            			var image = data.data[i];
            			if (baseVersion == image.version) {
            				html = "<option type='text' value='"+image.id+"'>"+image.version+"</option>" + html;
            			}else {
            				html += "<option type='text' value='"+image.id+"'>"+image.version+"</option>";
            			}
            		}
            	}
            }
            $("#baseImageId").html(html);
		}
	});
}

function registerCiDelEvent(id){
	 $("#delCiBtn").click(function(){
		 layer.open({
	        title: '删除构建',
	        content: '确定删除构建？',
	        btn: ['确定', '取消'],
	        yes: function(index, layero){ //或者使用btn1
	        	$.ajax({
                    type:"post",
	        		url:ctx+"/ci/delCi.do",
                    data: {"id" : id},
	        		success:function(data){
	        			data = eval("(" + data + ")");
	        			 if(data.status=="200"){
	        				 if(data.type == "1"){
	        					 window.location.href = ctx+"/ci?code";
	        				 }else{
	        					 window.location.href = ctx+"/ci";
	        				 }

	                     } else {
	                         layer.alert("删除构建失败");
	                     }
	        		},
                    error: function(e) {
                        layer.alert("请求出错");
                    }
	        	});
	        },
	        cancel: function(index){ //或者使用btn2
	        }
	    });
    });
}

function registerHookCode(ciId){
	 $("#delHookBtn").click(function(){
		 layer.open({
	        title: '删除HookCode',
	        content: '确定删除HookCode？',
	        btn: ['确定', '取消'],
	        yes: function(index, layero){ //或者使用btn1
	        	$.ajax({
                    type:"post",
	        		url:ctx+"/ci/delCodeHook.do",
                    data: {"ciId" : ciId},
	        		success:function(data){
	        			data = eval("(" + data + ")");
	        			 if(data.status=="200"){
	        				 window.location.reload();
	                     } else {
	                         layer.alert("修改失败"+data.msg);
	                     }
	        		},
                    error: function(e) {
                        layer.alert("请求出错");
                    }
	        	});
	        },
	        cancel: function(index){ //或者使用btn2
	        }
	    });
    });
}

function registerCiRecordEvent(){
	$(".event-sign").unbind("click").click(function(){
        if($(this).hasClass("lives")){
        	$(this).parent().parent().children(".time-line-message").css("display","none");
            $(this).children(".fa_caret").css("transform","rotate(0deg)");
            $(this).removeClass("lives");
        }else{
        	$(this).parent().parent().children(".time-line-message").css("display","block");
            $(this).children(".fa_caret").css("transform","rotate(90deg)");
            $(this).addClass("lives");
        }
    });
}

//打印日志
function printLog(){
	$(".printLogSpan[status=3]").each(function(){
		var $this = $(this);
		var timer = setInterval(function(){
			$.ajax({
				url:ctx+"/ci/printCiRecordLog.do?id="+$this.attr("ciRecordId"),
				success:function(data){
					data = eval("(" + data + ")");
					 if(data.data.constructResult!="3"){
						 clearInterval(timer);
						 window.location.reload();
					 }
					 $this.html("<br>"+data.data.logPrint);
					$this.parent(".logs").parent(".build-logs").scrollTop($this.parent().parent(".build-logs")[0].scrollHeight);

				}
			});
		}, 5000);
	});
}

//添加ant表单
function loadAnt(count){
	var antHtml = "";
	$.ajax({
		url : ctx + "/shera/getSheraAntConfig.do",
		success : function(data){
			data = eval("("+data+")");
			antHtml = '<div class="row addCiStepRow ant" count ='+count+' invoke ="ant">'+
			'<div class="col-sm-12">'+
			'<div class="ibox float-e-margins">'+
				'<div class="ibox-title">'+
					'<h5>ant</h5>'+
					'<div class="ibox-tools">'+
						'<a class="questionInfoBtn" title="注解">'+
							'<i class="fa fa-question-circle"></i>'+
						'</a>'+
						'<a class="collapse-link" title="缩放">'+
							'<i class="fa fa-chevron-up"></i>'+
						'</a>'+
						'<a class="close-link" title="关闭">'+
							'<i class="fa fa-times"></i>'+
						'</a>'+
					'</div>'+
				'</div>'+
				'<div class="ibox-content">'+
					'<div class="row ant-config">'+
						'<div class="form-group col-md-12 questionInfoCon"><p>项目使用Ant构建系统.这将导致Jenkins去调用Ant目标和选项.任何非零返回值都会导致 Jenkins构建失败.'+
						'Jenkins提供一些环境变量可以用在构建脚本中.</p></div>'+
						'<div class="form-group col-md-12">'+
							'<label class="c-project-tit">ant版本</label>'+
							'<select id="antVersion-'+count+'" name="antVersion" class="form-control c-project-con" >';
			if (data != null && data.status == "200") {
				for (var i in data.antList) {
					antHtml += '<option value="' + data.antList[i].version + '">' + data.antList[i].version + '</option>';
				}
			}
			antHtml +=
							'</select>'+
						'</div>'+
						'<div class="form-group col-md-12">'+
						   '<label class="c-project-tit">目标</label>'+
							'<input id="antTargets-'+count+'" name="antTargets" type="text" class="form-control c-project-con" value=""><i class="fa fa-question-circle fa-questionBtn"></i>'+
						'</div>'+
						'<div class="form-group col-md-12 fa-questionCon">'+
							'<p>指定要调用的Ant目标的列表（以空格分隔），或将其留空以调用构建脚本中指定的默认Ant目标。 此外，您还可以使用此字段指定其他Ant选项。</p>'+
						 '</div>'+
					'</div>'+
					'<div class="row ant-config">'+
						'<button class="ant-higherBtn" type="button" style="float:right!important">高级...</button>'+
					'</div>'+
					'<div class="row ant-higherCon hide">'+
						'<div class="form-group col-md-12">'+
							'<label class="c-project-tit">构建文件</label>'+
							'<input id="antBuildFileLocation-'+count+'" name="antBuildFileLocation" type="text" class="form-control c-project-con" value=""><i class="fa fa-question-circle fa-questionBtn"></i>'+
						'</div>'+
						'<div class="form-group col-md-12 fa-questionCon">'+
							'<p>如果您的构建需要自定义-buildfile，在这里指定。 默认情况下Ant将使用根目录中的build.xml; 此选项可用于使用具有不同名称或子目录中的构建文件。</p>'+
						 '</div>'+
						'<div class="form-group col-md-12">'+
							'<label class="c-project-tit">属性</label>'+
							'<input id="antProperties-'+count+'" name="antProperties" type="text" class="form-control c-project-con" value=""><i class="fa fa-question-circle fa-questionBtn"></i>'+
						'</div>'+
						'<div class="form-group col-md-12 fa-questionCon">'+
						'<p>您可以在此处指定您的ant构建所需的属性（以标准属性文件格式）：</p>'+
						'<p>＃comment</p>'+
						'<p>name1 = value1</p>'+
						'<p>name2 = $ VAR2</p>'+
						'<p>这些被传递给Ant像“-Dname1 = value1 -Dname2 = value2”。 始终'+
						'使用$ VAR样式（甚至在Windows上）引用Jenkins定义的环境变量。 '+
						'在Windows上，％VAR％样式引用可用于存在于Jenkins之外的环境变量。 '+
						'反斜杠用于转义，因此对于单个反斜杠使用\\。 应该避免双引号（“），因为ant on * nix将引号'+
						'中的参数封装在引号中，并通过eval运行它们，而且Windows也有自己的转义问题，在任何一种情况下，'+
						'使用引号都可能导致构建失败。 属性，只需写入varname =</p>'+
					 '</div>'+
						'<div class="form-group col-md-12">'+
							'<label class="c-project-tit">java选项</label>'+
							'<input id="antJavaOpts-'+count+'" name="antJavaOpts" type="text" class="form-control c-project-con" value=""><i class="fa fa-question-circle fa-questionBtn"></i>'+
						'</div>'+
						'<div class="form-group col-md-12 fa-questionCon">'+
							'<p>如果您的构建需要自定义ANT_OPTS，请在此处指定。 通常，这可以用于指定要使用的java内存限制，例如-Xmx512m。 请注意，其他Ant选项（如-lib）应转到“Ant targets”字段。</p>'+
						'</div>'+
					'</div></div></div></div></div>';
			$("#sortable").append(antHtml);
		}
	});
}

//加载ant表单数据
function loadAntData(count,invoke){
	var antVersion = invoke.antVersion;
	var antVersionData = '';
	$.ajax({
		url : ctx + "/shera/getSheraAntConfig.do",
		success : function(data){
			data = eval("("+data+")");
			if (data != null && data.status == "200") {
				for (var i in data.antList) {
					if(antVersion == data.antList[i].version){
						antVersionData += '<option value="' + data.antList[i].version + '" selected="selected">' + data.antList[i].version + '</option>';
					}else{
						antVersionData += '<option value="' + data.antList[i].version + '">' + data.antList[i].version + '</option>';
					}
				}
			}
			var antHtml = '<div class="row addCiStepRow ant" count ='+count+' invoke ="ant">'+
			'<div class="col-sm-12">'+
			'<div class="ibox float-e-margins">'+
				'<div class="ibox-title">'+
					'<h5>ant</h5>'+
					'<div class="ibox-tools">'+
						'<a class="questionInfoBtn" title="注解">'+
							'<i class="fa fa-question-circle"></i>'+
						'</a>'+
						'<a class="collapse-link" title="缩放">'+
		                    '<i class="fa fa-chevron-up"></i>'+
		                '</a>'+
						'<a class="close-link" title="关闭">'+
		                    '<i class="fa fa-times"></i>'+
		                '</a>'+
					'</div>'+
				'</div>'+
				'<div class="ibox-content">'+
					'<div class="row ant-config">'+
						'<div class="form-group col-md-12 questionInfoCon"><p>项目使用Ant构建系统.这将导致Jenkins去调用Ant目标和选项.任何非零返回值都会导致 Jenkins构建失败.'+
						'Jenkins提供一些环境变量可以用在构建脚本中.</p></div>'+
		            	'<div class="form-group col-md-12">'+
		                    '<label class="c-project-tit">ant版本</label>'+
		                    '<select id="antVersion-'+count+'" name="antVersion" class="form-control c-project-con" >'+
		                    		antVersionData+
		                    '</select>'+
		                '</div>'+
		                '<div class="form-group col-md-12">'+
		                   '<label class="c-project-tit">目标</label>'+
		                    '<input id="antTargets-'+count+'" name="antTargets" type="text" class="form-control c-project-con"><i class="fa fa-question-circle fa-questionBtn"></i>'+
		                '</div>'+
		                '<div class="form-group col-md-12 fa-questionCon">'+
							'<p>指定要调用的Ant目标的列表（以空格分隔），或将其留空以调用构建脚本中指定的默认Ant目标。 此外，您还可以使用此字段指定其他Ant选项。</p>'+
						 '</div>'+
		            '</div>'+
		            '<div class="row ant-config">'+
		            	'<button class="ant-higherBtn" type="button" style="float:right!important">高级...</button>'+
		            '</div>'+
		            '<div class="row ant-higherCon hide">'+
		            	'<div class="form-group col-md-12">'+
		                    '<label class="c-project-tit">构建文件</label>'+
		                    '<input id="antBuildFileLocation-'+count+'" name="antBuildFileLocation" type="text" class="form-control c-project-con"><i class="fa fa-question-circle fa-questionBtn"></i>'+
		                '</div>'+
		                '<div class="form-group col-md-12 fa-questionCon">'+
							'<p>如果您的构建需要自定义-buildfile，在这里指定。 默认情况下Ant将使用根目录中的build.xml; 此选项可用于使用具有不同名称或子目录中的构建文件。</p>'+
						 '</div>'+
		                '<div class="form-group col-md-12">'+
		                    '<label class="c-project-tit">属性</label>'+
		                    '<input id="antProperties-'+count+'" name="antProperties" type="text" class="form-control c-project-con" ><i class="fa fa-question-circle fa-questionBtn"></i>'+
		                '</div>'+
		                '<div class="form-group col-md-12 fa-questionCon">'+
							'<p>您可以在此处指定您的ant构建所需的属性（以标准属性文件格式）：</p>'+
							'<p>＃comment</p>'+
							'<p>name1 = value1</p>'+
							'<p>name2 = $ VAR2</p>'+
							'<p>这些被传递给Ant像“-Dname1 = value1 -Dname2 = value2”。 始终'+
							'使用$ VAR样式（甚至在Windows上）引用Jenkins定义的环境变量。 '+
							'在Windows上，％VAR％样式引用可用于存在于Jenkins之外的环境变量。 '+
							'反斜杠用于转义，因此对于单个反斜杠使用\\。 应该避免双引号（“），因为ant on * nix将引号'+
							'中的参数封装在引号中，并通过eval运行它们，而且Windows也有自己的转义问题，在任何一种情况下，'+
							'使用引号都可能导致构建失败。 属性，只需写入varname =</p>'+
						 '</div>'+
		                '<div class="form-group col-md-12">'+
		                    '<label class="c-project-tit">java选项</label>'+
		                    '<input id="antJavaOpts-'+count+'" name="antJavaOpts" type="text" class="form-control c-project-con"><i class="fa fa-question-circle fa-questionBtn"></i>'+
		                '</div>'+
		                '<div class="form-group col-md-12 fa-questionCon">'+
							'<p>如果您的构建需要自定义ANT_OPTS，请在此处指定。 通常，这可以用于指定要使用的java内存限制，例如-Xmx512m。 请注意，其他Ant选项（如-lib）应转到“Ant targets”字段。</p>'+
						'</div>'+
		                '</div></div></div></div></div>';

			$("#sortable").append(antHtml);
			$("#antTargets-"+count).val(invoke.antTargets);
			$("#antBuildFileLocation-"+count).val(invoke.antBuildFileLocation);
			$("#antProperties-"+count).val(invoke.antProperties);
			$("#antJavaOpts-"+count).val(invoke.antJavaOpts);
		}
	});
	
}

//添加maven表单
function loadMaven(count){
	$.ajax({
		url : ctx + "/shera/getSheraMavenConfig.do",
		success : function(data){
			data = eval("("+data+")");
			var mavenHtml = '<div class="row addCiStepRow maven" count = '+count+' invoke = "maven">'+
				'<div class="col-sm-12">'+
				'<div class="ibox float-e-margins">'+
					'<div class="ibox-title">'+
						'<h5>maven</h5>'+
						'<div class="ibox-tools">'+
							'<a class="questionInfoBtn" title="注解">'+
								'<i class="fa fa-question-circle"></i>'+
							'</a>'+
							'<a class="collapse-link" title="缩放">'+
								'<i class="fa fa-chevron-up"></i>'+
							'</a>'+
							'<a class="close-link" title="关闭">'+
								'<i class="fa fa-times"></i>'+
							'</a>'+
						'</div>'+
					'</div>'+
					'<div class="ibox-content">'+
						'<div class="row">'+
							'<div class="form-group col-md-12 questionInfoCon">'+
								'<p>对于使用Maven作为构建系统的项目。 这导致Jenkins调用Maven与给定的目标和选项。 Maven的非零'+
								'退出代码使Jenkins将构建标记为失败。 一些Maven版本有一个错误，它不能正确返回退出代码。</p>'+
								'<p>Jenkins将各种环境变量传递给Maven，您可以从Maven作为“$ {env.VARIABLENAME}”访问。</p>'+
								'<p>相同的变量可以在命令行参数中使用（就好像您正在从shell调用它）。 例如，您可以指定-DresultsFile = $ {WORKSPACE} / $ {BUILD_TAG} .results.txt</p>'+
							'</div>'+
							'<div class="form-group col-md-12">'+
								'<label class="c-project-tit">maven版本</label>'+
								'<select id="mavenVersion-'+count+'" name="mavenVersion" class="form-control c-project-con" >';

			if (data != null && data.status == "200") {
				for (var i in data.mavenList) {
					mavenHtml += '<option value="' + data.mavenList[i].version + '">' + data.mavenList[i].version + '</option>';
				}
			}
			mavenHtml +=
					'</select>'+
				'</div>'+
				'<div class="form-group col-md-12">'+
					'<label class="c-project-tit">目标</label>'+
					'<input id="mavenGoals-'+count+'" name="mavenGoals" type="text" class="form-control c-project-con" value=""><i class="fa fa-question-circle fa-questionBtn"></i>'+
				'</div>'+
				'<div class="form-group col-md-12 fa-questionCon">'+
					'<p>指定要执行的目标，例如“clean install”或“deploy”。 此字段还可以接受Maven的任何其他命令行选项，例如“-e”或“-DskipTests = true”。</p>'+
				 '</div>'+
				'<div class="row maven-config">'+
					'<button class="maven-higherBtn" type="button" style="float:right!important">高级...</button>'+
				'</div>'+

				'<div class="maven-higherCon hide"><div class="form-group col-md-12">'+
					'<label class="c-project-tit">POM</label>'+
					'<input id="pomLocation-'+count+'" name="pomLocation" type="text" class="form-control c-project-con" value=""><i class="fa fa-question-circle fa-questionBtn"></i>'+
				'</div>'+
				'<div class="form-group col-md-12 fa-questionCon">'+
					'<p>如果工作空间的顶层pom.xml位于除第一个模块的根目录之外的位置，请在此处指定路径（相对于模块根目录），例如parent/pom.xml。</p>'+
					'<p>如果为空，则默认为pom.xml</p>'+
				 '</div>'+
				'<div class="form-group col-md-12">'+
					'<label class="c-project-tit">属性</label>'+
					'<input id="mavenProperty-'+count+'" name="mavenProperty" type="text" class="form-control c-project-con" value=""><i class="fa fa-question-circle fa-questionBtn"></i>'+
				'</div>'+
				'<div class="form-group col-md-12 fa-questionCon">'+
					'<p>您可以在此处指定Maven构建所需的属性（以标准属性文件格式）：</p>'+
					'<p>＃comment</p>'+
					'<p>name1 = value1</p>'+
					'<p>name2 = value2</p>'+
					'<p>这些被传递给Maven像“-Dname1 = value1 -Dname2 = value2”</p>'+
				 '</div>'+
				'<div class="form-group col-md-12">'+
					'<label class="c-project-tit">JVM选项</label>'+
					'<input id="mavenJVMOptions-'+count+'" name="mavenJVMOptions" type="text" class="form-control c-project-con" value=""><i class="fa fa-question-circle fa-questionBtn"></i>'+
				'</div>'+
				 '<div class="form-group col-md-12 fa-questionCon">'+
					'<p>在启动Maven时指定需要的JVM选项. 可以参阅MAVEN_OPTS文档 (尽管这是Maven1.x文档,但是同样适用于Maven2.x)</p>'+
					'<p>要想在这里使用Shell环境变量,使用语法${VARIABLE}.</p>'+
				 '</div>'+
				'<div class="form-group col-md-12">'+
					'<div style="line-height:34px;float:left;width:97%">'+
						'<label class="c-project-tit" title="使用私有Maven存储库" style="width:154px">使用私有Maven存储库</label>'+
						'<input type="checkbox" id = "isUserPrivateRegistry-'+count+'" name = "isUserPrivateRegistry" value = "0">'+
					'</div><i class="fa fa-question-circle fa-questionBtn"></i>'+
				'</div>'+
				'<div class="form-group col-md-12 fa-questionCon">'+
					'<p>通常，Jenkins使用Maven确定的本地Maven仓库 - 确切的过程似乎是未记录的，但它是〜/.m2/repository，并且可以通过〜/.m2/settings.xml中设置<localRepository>覆盖</p>'+
					'<p>这通常意味着在同一节点上执行的所有作业都共享一个Maven存储库。这样做的好处是你可以节省磁盘空间，但这样做的缺点是，有时这些构建可能会互相干扰。例如，您可能最终会遇到构建不正确的成功，只是因为您具有本地存储库中的所有依赖关系，尽管事实上POM中的任何存储库都可能没有。</p>'+
					'<p>还有一些报告的问题，有关并发Maven进程尝试使用相同的本地存储库。</p>'+
					'<p>当选中此选项时，Jenkins将告诉Maven使用$WORKSPACE/.repository作为本地Maven存储库。这意味着每个作业将获得自己独立的Maven存储库，仅供自己使用。它修复了上述问题，以牺牲额外的磁盘空间消耗为代价。</p>'+
					'<p>当使用此选项时，请考虑设置一个Maven工件管理器，以便不必太频繁地访问远程Maven存储库。</p>'+
				 '</div>'+
				'<div class="form-group col-md-12">'+
					'<label class="c-project-tit">配置文件</label>'+
					'<select id="mavenSetFile-'+count+'" name="mavenSetFile" class="form-control c-project-con" onchange="changeMavenSetting(this)">'+
						'<option value="use default maven setting">use default maven setting</option>'+
						'<option value="settings file in filesystem">settings file in filesystem</option>'+
					'</select><i class="fa fa-question-circle fa-questionBtn"></i>'+
					'<div class="form-group col-md-12 settingfilePath hide"><label class="settingfilePath-tit">文件路径</label><input type="text" name="" class="form-control settingfilePath-con"><i class="fa fa-question-circle fa-questionBtn"></i></div>'+
					'<div class="form-group col-md-12 fa-questionCon" style="width:77%;margin-left:18%">'+
						'<p>相对于项目工作空间或绝对路径（支持变量）的settings.xml文件的路径。</p>'+
					 '</div>'+
				'</div>'+
				'<div class="form-group col-md-12 fa-questionCon">'+
					'<p>settings.xml文件中的settings元素包含用于定义以各种方式配置Maven执行的元素的元素，如pom.xml，但不应捆绑到任何特定项目或分发给受众。 这些包括本地存储库位置，备用远程存储库服务器和身份验证信息等值。</p>'+
					'<p>有两个位置，每个默认的settings.xml文件可能会存在：</p>'+
					'<p>Maven安装 - 默认：$ M2_HOME/conf/settings.xml</p>'+
					'<p>用户的安装 - 默认：$ {user.home}/.m2/settings.xml</p>'+
					'<p>前面的settings.xml也称为全局设置，后面的settings.xml被称为用户设置。 如果两个文件都存在，则它们的内容被合并，用户特定的settings.xml是主要的。</p>'+
				 '</div>'+
				'<div class="form-group col-md-12">'+
					'<label class="c-project-tit">全局配置文件</label>'+
					'<select id="mavenGlobalSetFile-'+count+'" name="mavenGlobalSetFile" class="form-control c-project-con" onchange="changeMavenSetting(this)">'+
						'<option value="use default maven global setting">use default maven global setting</option>'+
						'<option value="global settings file in filesystem">global settings file in filesystem</option>'+
					'</select><i class="fa fa-question-circle fa-questionBtn"></i>'+
					'<div class="form-group col-md-12 settingfilePath hide"><label class="settingfilePath-tit">文件路径</label><input type="text" name="" class="form-control settingfilePath-con"><i class="fa fa-question-circle fa-questionBtn"></i></div>'+
					'<div class="form-group col-md-12 fa-questionCon" style="width:77%;margin-left:18%">'+
						'<p>相对于项目工作空间或绝对路径（支持变量）的settings.xml文件的路径。</p>'+
					'</div>'+
				'</div>'+
				'<div class="form-group col-md-12 fa-questionCon">'+
					'<p>settings.xml文件中的settings元素包含用于定义以各种方式配置Maven执行的元素的元素，如pom.xml，但不应捆绑到任何特定项目或分发给受众。 这些包括本地存储库位置，备用远程存储库服务器和身份验证信息等值。</p>'+
					'<p>有两个位置，每个默认的settings.xml文件可能会存在：</p>'+
					'<p>Maven安装 - 默认：$ M2_HOME/conf/settings.xml</p>'+
					'<p>用户的安装 - 默认：$ {user.home}/.m2/settings.xml</p>'+
					'<p>前面的settings.xml也称为全局设置，后面的settings.xml被称为用户设置。 如果两个文件都存在，则它们的内容被合并，用户特定的settings.xml是主要的。</p>'+
				 '</div>'+
				'</div></div></div></div></div></div>';
			$("#sortable").append(mavenHtml);
		}
	});
}


//加载maven表单数据
function loadMavenData(count,invoke){
	var mavenSet = '';
	var mavenSetFile = invoke.mavenSetFile;
	if (mavenSetFile == 'use default maven setting') {
		mavenSet = '<option value="use default maven setting">use default maven setting</option>'+
					'<option value="settings file in filesystem">settings file in filesystem</option>';
	}else {
		mavenSet = '<option value="settings file in filesystem">settings file in filesystem</option>'+
				    '<option value="use default maven setting">use default maven setting</option>';
	}
	var mavenVersion = invoke.mavenVersion;
	var mavenVersionData = "";
	$.ajax({
		url : ctx + "/shera/getSheraMavenConfig.do",
		success : function(data){
			var data = eval("("+data+")");
			if (data != null && data.status == "200") {
				for (var i in data.mavenList) {
					if(mavenVersion == data.mavenList[i].version){
						mavenVersionData += '<option value="' + data.mavenList[i].version + '" selected="selected">' + data.mavenList[i].version + '</option>';
					}else{
						mavenVersionData += '<option value="' + data.mavenList[i].version + '">' + data.mavenList[i].version + '</option>';
					}
					
				}
			}
			var mavenGlobalSet = '';
			var mavenGlobalSetFile = invoke.mavenGlobalSetFile;
			if (mavenGlobalSetFile == 'use default maven global setting') {
				mavenGlobalSet = '<option value="use default maven global setting">use default maven global setting</option>'+
		        			'<option value="global settings file in filesystem">global settings file in filesystem</option>';
			}else {
				mavenGlobalSet ='<option value="global settings file in filesystem">global settings file in filesystem</option>'+
							'<option value="use default maven global setting">use default maven global setting</option>';
			}
			

			var mavenHtml = '<div class="row addCiStepRow maven" count = '+count+' invoke = "maven">'+
			'<div class="col-sm-12">'+
			'<div class="ibox float-e-margins">'+
				'<div class="ibox-title">'+
					'<h5>maven</h5>'+
					'<div class="ibox-tools">'+
						'<a class="questionInfoBtn" title="注解">'+
							'<i class="fa fa-question-circle"></i>'+
						'</a>'+
						'<a class="collapse-link" title="缩放">'+
		                    '<i class="fa fa-chevron-up"></i>'+
		                '</a>'+
						'<a class="close-link" title="关闭">'+
		                    '<i class="fa fa-times"></i>'+
		                '</a>'+
					'</div>'+
				'</div>'+
				'<div class="ibox-content">'+
					'<div class="row">'+
						'<div class="form-group col-md-12 questionInfoCon">'+
							'<p>对于使用Maven作为构建系统的项目。 这导致Jenkins调用Maven与给定的目标和选项。 Maven的非零'+
							'退出代码使Jenkins将构建标记为失败。 一些Maven版本有一个错误，它不能正确返回退出代码。</p>'+
							'<p>Jenkins将各种环境变量传递给Maven，您可以从Maven作为“$ {env.VARIABLENAME}”访问。</p>'+
							'<p>相同的变量可以在命令行参数中使用（就好像您正在从shell调用它）。 例如，您可以指定-DresultsFile = $ {WORKSPACE} / $ {BUILD_TAG} .results.txt</p>'+
						'</div>'+
		            	'<div class="form-group col-md-12">'+
		                    '<label class="c-project-tit">maven版本</label>'+
		                    '<select id="mavenVersion-'+count+'" name="mavenVersion" class="form-control c-project-con" >'+
		                    mavenVersionData +
		                    '</select>'+
		                '</div>'+
		                '<div class="form-group col-md-12">'+
		                    '<label class="c-project-tit">目标</label>'+
		                    '<input id="mavenGoals-'+count+'" name="mavenGoals" type="text" class="form-control c-project-con" value="'+invoke.mavenGoals+'"><i class="fa fa-question-circle fa-questionBtn"></i>'+
		                '</div>'+
		                '<div class="form-group col-md-12 fa-questionCon">'+
							'<p>指定要执行的目标，例如“clean install”或“deploy”。 此字段还可以接受Maven的任何其他命令行选项，例如“-e”或“-DskipTests = true”。</p>'+
						'</div>'+
		                '<div class="row maven-config">'+
		                	'<button class="maven-higherBtn" type="button" style="float:right!important">高级...</button>'+
		                '</div>'+

		                '<div class="maven-higherCon hide"><div class="form-group col-md-12">'+
		                    '<label class="c-project-tit">POM</label>'+
		                    '<input id="pomLocation-'+count+'" name="pomLocation" type="text" class="form-control c-project-con" value="'+invoke.pomLocation+'"><i class="fa fa-question-circle fa-questionBtn"></i>'+
		                '</div>'+
		                '<div class="form-group col-md-12 fa-questionCon">'+
							'<p>如果工作空间的顶层pom.xml位于除第一个模块的根目录之外的位置，请在此处指定路径（相对于模块根目录），例如parent/pom.xml。</p>'+
							'<p>如果为空，则默认为pom.xml</p>'+
						'</div>'+
		                '<div class="form-group col-md-12">'+
		                    '<label class="c-project-tit">属性</label>'+
		                    '<input id="mavenProperty-'+count+'" name="mavenProperty" type="text" class="form-control c-project-con" value="'+invoke.mavenProperty+'"><i class="fa fa-question-circle fa-questionBtn"></i>'+
		                '</div>'+
		                '<div class="form-group col-md-12 fa-questionCon">'+
							'<p>您可以在此处指定Maven构建所需的属性（以标准属性文件格式）：</p>'+
							'<p>＃comment</p>'+
							'<p>name1 = value1</p>'+
							'<p>name2 = value2</p>'+
							'<p>这些被传递给Maven像“-Dname1 = value1 -Dname2 = value2”</p>'+
						 '</div>'+
		                '<div class="form-group col-md-12">'+
		                    '<label class="c-project-tit">JVM选项</label>'+
		                    '<input id="mavenJVMOptions-'+count+'" name="mavenJVMOptions" type="text" class="form-control c-project-con" value="'+invoke.mavenJVMOptions+'"><i class="fa fa-question-circle fa-questionBtn"></i>'+
		                '</div>'+
		                '<div class="form-group col-md-12 fa-questionCon">'+
							'<p>在启动Maven时指定需要的JVM选项. 可以参阅MAVEN_OPTS文档 (尽管这是Maven1.x文档,但是同样适用于Maven2.x)</p>'+
							'<p>要想在这里使用Shell环境变量,使用语法${VARIABLE}.</p>'+
						 '</div>'+
		                '<div class="form-group col-md-12">'+
		                    '<div style="line-height:34px">'+
		                        '<label class="c-project-tit" title="使用私人maven存储库">使用私人maven存储库</label>'+
		                        '<input type="checkbox" id = "isUserPrivateRegistry-'+count+'" name = "isUserPrivateRegistry" value = "0">'+
		                    '</div><i class="fa fa-question-circle fa-questionBtn"></i>'+
		                '</div>'+
		                '<div class="form-group col-md-12 fa-questionCon">'+
							'<p>通常，Jenkins使用Maven确定的本地Maven仓库 - 确切的过程似乎是未记录的，但它是〜/.m2/repository，并且可以通过〜/.m2/settings.xml中设置<localRepository>覆盖</p>'+
							'<p>这通常意味着在同一节点上执行的所有作业都共享一个Maven存储库。这样做的好处是你可以节省磁盘空间，但这样做的缺点是，有时这些构建可能会互相干扰。例如，您可能最终会遇到构建不正确的成功，只是因为您具有本地存储库中的所有依赖关系，尽管事实上POM中的任何存储库都可能没有。</p>'+
							'<p>还有一些报告的问题，有关并发Maven进程尝试使用相同的本地存储库。</p>'+
							'<p>当选中此选项时，Jenkins将告诉Maven使用$WORKSPACE/.repository作为本地Maven存储库。这意味着每个作业将获得自己独立的Maven存储库，仅供自己使用。它修复了上述问题，以牺牲额外的磁盘空间消耗为代价。</p>'+
							'<p>当使用此选项时，请考虑设置一个Maven工件管理器，以便不必太频繁地访问远程Maven存储库。</p>'+
						 '</div>'+
		                '<div class="form-group col-md-12">'+
		                    '<label class="c-project-tit">配置文件</label>'+
		                    '<select id="mavenSetFile-'+count+'" name="mavenSetFile" class="form-control c-project-con" onchange="changeMavenSetting(this)">'+
		                    	mavenSet +
							'</select><i class="fa fa-question-circle fa-questionBtn"></i>'+
							'<div class="form-group col-md-12 settingfilePath hide"><label class="settingfilePath-tit">文件路径</label><input type="text" name="" class="form-control settingfilePath-con"><i class="fa fa-question-circle fa-questionBtn"></i></div>'+
							'<div class="form-group col-md-12 fa-questionCon" style="width:77%;margin-left:18%">'+
								'<p>相对于项目工作空间或绝对路径（支持变量）的settings.xml文件的路径。</p>'+
							 '</div>'+
		                '</div>'+
		                '<div class="form-group col-md-12 fa-questionCon">'+
							'<p>settings.xml文件中的settings元素包含用于定义以各种方式配置Maven执行的元素的元素，如pom.xml，但不应捆绑到任何特定项目或分发给受众。 这些包括本地存储库位置，备用远程存储库服务器和身份验证信息等值。</p>'+
							'<p>有两个位置，每个默认的settings.xml文件可能会存在：</p>'+
							'<p>Maven安装 - 默认：$ M2_HOME/conf/settings.xml</p>'+
							'<p>用户的安装 - 默认：$ {user.home}/.m2/settings.xml</p>'+
							'<p>前面的settings.xml也称为全局设置，后面的settings.xml被称为用户设置。 如果两个文件都存在，则它们的内容被合并，用户特定的settings.xml是主要的。</p>'+
						 '</div>'+
		                '<div class="form-group col-md-12">'+
		                    '<label class="c-project-tit">全局配置文件</label>'+
		                    '<select id="mavenGlobalSetFile-'+count+'" name="mavenGlobalSetFile" class="form-control c-project-con" onchange="changeMavenSetting(this)">'+
		                    mavenGlobalSet+
							'</select><i class="fa fa-question-circle fa-questionBtn"></i>'+
							'<div class="form-group col-md-12 settingfilePath hide"><label class="settingfilePath-tit">文件路径</label><input type="text" name="" class="form-control settingfilePath-con"><i class="fa fa-question-circle fa-questionBtn"></i></div>'+
							'<div class="form-group col-md-12 fa-questionCon" style="width:77%;margin-left:18%">'+
								'<p>相对于项目工作空间或绝对路径（支持变量）的settings.xml文件的路径。</p>'+
							'</div>'+
		                '</div>'+
		                '<div class="form-group col-md-12 fa-questionCon">'+
							'<p>settings.xml文件中的settings元素包含用于定义以各种方式配置Maven执行的元素的元素，如pom.xml，但不应捆绑到任何特定项目或分发给受众。 这些包括本地存储库位置，备用远程存储库服务器和身份验证信息等值。</p>'+
							'<p>有两个位置，每个默认的settings.xml文件可能会存在：</p>'+
							'<p>Maven安装 - 默认：$ M2_HOME/conf/settings.xml</p>'+
							'<p>用户的安装 - 默认：$ {user.home}/.m2/settings.xml</p>'+
							'<p>前面的settings.xml也称为全局设置，后面的settings.xml被称为用户设置。 如果两个文件都存在，则它们的内容被合并，用户特定的settings.xml是主要的。</p>'+
						 '</div>'+
		                '</div></div></div></div></div></div>';
			$("#sortable").append(mavenHtml);
			$("#mavenGoals-"+count).val(invoke.mavenGoals);
			$("#pomLocation-"+count).val(invoke.pomLocation);
			$("#mavenProperty-"+count).val(invoke.mavenProperty);
			$("#mavenJVMOptions-"+count).val(invoke.mavenJVMOptions);
		}
	});
	
	
}

//添加shell脚本表单
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

//加载shell脚本表单数据
function loadShellData(count,invoke){
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

	$("#sortable").append(shellHtml);
	$("#executeShell-"+count).val(invoke.shellCommand);
}

//代码构建数据的判断以及数据的封装
function checkCodeCiAdd(){
	//项目名称的判断
	var projectName = $("#projectName").val();
	var id = $("#id").val();
	var falgName = false;
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
    		url : ctx + "/ci/judgeProjectName.do",
    		async:false,
    		type: "POST",
    		data:{"projectName":projectName,"id":id},
    		success : function(data) {
    			data = eval("(" + data + ")");
    			if (data.status=="400") {
    	            layer.tips('项目名称重复', '#projectName', {
    	                tips: [1, '#0FA6D8'] //还可配置颜色
    	            });
    	            $('#projectName').focus();
    	            falgName = true;
    			}
    		}
    	});
    }
    if (falgName) {
    	falgName = false;
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
    if (codeType != 0 ) {
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
    	} else {
    		$("#isHookCode").val("0");
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
    	flag = false;
    	return false;
    }

    json = json.substring(0, json.length-1);
    json = '[' + json + ']';
    $("#jsonData").val(json);


    if ($("#imageInfo").prop("checked")) {
    	var imgNameFirst = $("#imgNameFirst").val();
    	var imageName = $("#imageName").val();
		if(!imageName || imageName.length < 1){
			layer.tips('镜像名称不能为空','#imageName',{tips: [1, '#3595CC']});
			$('#imageName').focus();
			return;
	    }
		if(imageName.search(/^[a-z0-9-_]*$/) === -1){
			layer.tips('镜像名称只能由小写字母、数字、横线和下划线组成', '#imageName', {
				tips: [1, '#0FA6D8']
			});
			$('#imageName').focus();
			return false;
		}
		var imgNameVersion = $("#imgNameVersion").val();
		if (imgNameVersion != "" || imgNameVersion.length > 0) {
			if(imgNameVersion.search(/^[a-z0-9-_]*$/) === -1){
				layer.tips('镜像版本只能由小写字母、数字、横线和下划线组成', '#imgNameVersion', {
					tips: [1, '#0FA6D8']
				});
				$('#imgNameVersion').focus();
				return false;
			}

			$.ajax({
	    		url : ctx + "/ci/validCiDetailVersion.do",
	    		async:false,
	    		type: "POST",
	    		data:{
	    				"id" : id,
	    				"imgNameFirst":imgNameFirst,
	    				"imgNameLast":imageName,
	    				"imgNameVersion":imgNameVersion
	    		},
	    		success : function(data) {
	    			data = eval("(" + data + ")");
	    			if (data.status=="400") {
	    	            layer.tips('镜像版本重复', '#imgNameVersion', {
	    	                tips: [1, '#0FA6D8'] //还可配置颜色
	    	            });
	    	            $('#imgNameVersion').focus();
	    	            flag = true;
	    			}
	    		}
	    	});
	        if (flag) {
	        	flag = false;
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

	    var dockerFileLocation = $("#dockerFileLocation").val();
		if (dockerFileLocation != undefined) {
			if(!dockerFileLocation || dockerFileLocation.length < 1){
				layer.tips('dockerfile文件路径不能为空','#dockerFileLocation',{tips: [1, '#3595CC']});
				$('#dockerFileLocation').focus();
				return;
			}
		}

		if (editor_one !=null) {
			var dockerFile = editor_one.getValue();
			if (dockerFile != undefined) {
				if(!dockerFile || dockerFile.length < 1){
					layer.tips('dockerfile模板不能为空','#dockerFiles2',{tips: [1, '#3595CC']});
					$('#dockerFiles2').focus();
					return;
				}else {
					$("#dockerFileContentEdit").val(dockerFile);
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

function checkQuickCiAdd() {
	//项目名称的判断
	var projectName = $("#projectName").val();
	var id = $("#id").val();
	var falgName = false;
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
    		url : ctx + "/ci/judgeProjectName.do",
    		async:false,
    		type: "POST",
    		data:{"projectName":projectName,"id":id},
    		success : function(data) {
    			data = eval("(" + data + ")");
    			if (data.status=="400") {
    	            layer.tips('项目名称重复', '#projectName', {
    	                tips: [1, '#0FA6D8'] //还可配置颜色
    	            });
    	            $('#projectName').focus();
    	            falgName = true;
    			}
    		}
    	});
    }
    if (falgName) {
    	falgName = false;
    	return false;
    }
    return true;
}

//加载工具集的勾选项
function loadCiToolsChecked(){
	if($("#ciToolsCheckedVal").val()){
		var ciToolsVal = $("#ciToolsCheckedVal").val();
		var ciTools = ciToolsVal.split(",");
		var toolChkInput = $(".toolChk");
		toolChkInput.prop("checked") == true;
		//toolChkInput.attr("checked",false);
		for(var i=0; i<ciTools.length; i++){
			$("#"+ciTools[i].replace(/\//g, '\\/')).prop("checked",true);
		}
	}
}

//数据格式判断
function judgeCredData(){
	var type = $("#CredentialsType").val();
	var username = $("#userNameCred").val();
	var password = $("#passwordCred").val();
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
	if (!remark || remark.length < 1) {
		layer.tips('描述信息不能为空','#keyRemark',{tips:[1,'#3595CC']});
		$("#keyRemark").focus();
		return;
	}
	return true;
}

//复制密钥按钮
function copySshPwd(){
	var sshPassword=document.getElementById("sshPassword");
	sshPassword.select(); // 选择对象
	document.execCommand("Copy"); // 执行浏览器复制命令
}
function changeMavenSetting(obj){
	var selectVal = $(obj).val();
	if(selectVal == "settings file in filesystem" || selectVal == "global settings file in filesystem"){
		$(obj).parent().find("div.settingfilePath").removeClass("hide");
	}else{
		$(obj).parent().find("div.settingfilePath").addClass("hide");
		$(obj).parent().find("div.fa-questionCon").css("display","none");

	}
}
