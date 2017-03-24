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
			area: ['500px'],
			content : $("#addCredentialsCon"),
			btn : [ '添加', '取消' ],
			scrollbar:false,
			yes:function(index, layero){
				var type = $("#CredentialsType").val();
				var codeType = $("#codeType").val();
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
							var html = "<option value='"+data.id+"'>"+username +" ("+code+") ("+remark+")"+"</option>";
							$("#codeCredentials").append(html);
							layer.alert("代码认证导入成功");
							layer.close(index);
						} else {
							layer.alert("代码认证导入失败");
						}
					}
				});
			}

		});
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

//加载ant表单数据
function loadAntData(count,invoke){
	var antVersion = invoke.antVersion;
	var antVersionData = '';
	if (antVersion == 'default') {
		antVersionData = '<option value="default">default</option><option value="ant">ant</option>';
	}
	else {
		antVersionData = '<option value="ant">ant</option><option value="default">default</option>';
	}
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
                    		antVersionData+
                    '</select>'+
                '</div>'+
                '<div class="form-group col-md-12">'+
                   '<label class="c-project-tit">目标</label>'+
                    '<input id="antTargets-'+count+'" name="antTargets" type="text" class="form-control c-project-con">'+
                '</div>'+
            '</div>'+
            '<div class="row ant-config">'+
            	'<button class="ant-higherBtn" type="button" style="float:right!important">高级...</button>'+
            '</div>'+
            '<div class="row ant-higherCon hide">'+
            	'<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">构建文件</label>'+
                    '<input id="antBuildFileLocation-'+count+'" name="antBuildFileLocation" type="text" class="form-control c-project-con">'+
                '</div>'+
                '<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">性能</label>'+
                    '<input id="antProperties-'+count+'" name="antProperties" type="text" class="form-control c-project-con" >'+
                '</div>'+
                '<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">java选项</label>'+
                    '<input id="antJavaOpts-'+count+'" name="antJavaOpts" type="text" class="form-control c-project-con">'+
                '</div></div></div></div></div></div>';

	$("#sortable").append(antHtml);
	$("#antTargets-"+count).val(invoke.antTargets);
	$("#antBuildFileLocation-"+count).val(invoke.antBuildFileLocation);
	$("#antProperties-"+count).val(invoke.antProperties);
	$("#antJavaOpts-"+count).val(invoke.antJavaOpts);
}

//添加maven表单
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
	var mavenVersionData = '';
	if (mavenVersion == 'default') {
		mavenVersionData = '<option value="default">default</option><option value="maven">maven</option>';
	}
	else {
		mavenVersionData = '<option value="maven">maven</option><option value="default">default</option>';
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
                    mavenVersionData +
                    '</select>'+
                '</div>'+
                '<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">目标</label>'+
                    '<input id="mavenGoals-'+count+'" name="mavenGoals" type="text" class="form-control c-project-con" value="'+invoke.mavenGoals+'">'+
                '</div>'+
                '<div class="row maven-config">'+
                	'<button class="maven-higherBtn" type="button" style="float:right!important">高级...</button>'+
                '</div>'+

                '<div class="maven-higherCon hide"><div class="form-group col-md-12">'+
                    '<label class="c-project-tit">POM</label>'+
                    '<input id="pomLocation-'+count+'" name="pomLocation" type="text" class="form-control c-project-con" value="'+invoke.pomLocation+'">'+
                '</div>'+
                '<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">属性</label>'+
                    '<input id="mavenProperty-'+count+'" name="mavenProperty" type="text" class="form-control c-project-con" value="'+invoke.mavenProperty+'">'+
                '</div>'+
                '<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">JVM选项</label>'+
                    '<input id="mavenJVMOptions-'+count+'" name="mavenJVMOptions" type="text" class="form-control c-project-con" value="'+invoke.mavenJVMOptions+'">'+
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
                    	mavenSet +
                    '</select>'+
                '</div>'+
                '<div class="form-group col-md-12">'+
                    '<label class="c-project-tit">全局配置文件</label>'+
                    '<select id="mavenGlobalSetFile-'+count+'" name="mavenGlobalSetFile" class="form-control c-project-con" >'+
                        mavenGlobalSet +
                    '</select>'+
                '</div>'+
                '</div></div></div></div></div></div>';
	$("#sortable").append(mavenHtml);
	$("#mavenGoals-"+count).val(invoke.mavenGoals);
	$("#pomLocation-"+count).val(invoke.pomLocation);
	$("#mavenProperty-"+count).val(invoke.mavenProperty);
	$("#mavenJVMOptions-"+count).val(invoke.mavenJVMOptions);
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
	var ciToolsVal = $("#ciToolsCheckedVal").val();
	var ciTools = ciToolsVal.split(",");
	var toolChkInput = $(".toolChk");
	toolChkInput.prop("checked") == true;
	//toolChkInput.attr("checked",false);
	for(var i=0; i<ciTools.length; i++){
		$("#"+ciTools[i].replace(/\//g, '\\/')).prop("checked",true);

	}
}

