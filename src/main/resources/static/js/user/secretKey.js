$(function(){
	var user,host,proxy,ip,port,policy,identifyFile;
	//创建密钥
	$("#createKeyBtn").click(function(){
		var userAutority = $("#userAutority").val();
		if(userAutority == 1){
			$(".userLimit").hide();
			$(".adminLimit").show();
		}else{
			$(".userLimit").show();
			$(".adminLimit").hide();
		}
		
		delData();
		layer.open({
			type: 1,
			title: '创建',
			content: $("#createKeyCon"),
			area: ['450px'],
			scrollbar:false,
			btn: ['确定', '取消'],
			yes: function(index, layero){
//				if (!judgeCredData()) {
//					return;
//				}
				
				var type = $("#CredentialsType").val();
				var codeType = $("#codeType").val();
				var username = $("#userNameCred").val();
				var password = $("#passwordCred").val();
				var privateKey = $("#SSHpasswordCred").val();
				var remark = $("#keyRemark").val();
				var code = type==1?"HTTP":"SSH";
				
				var ciCodeCredential = {
						"type" : type,
						"codeType":codeType,
						"userName" : username,
						"password" : password,
						"privateKey" : privateKey,
						"remark" : remark
					};
				host = $("#host").val();
				ip = $("#ip").val();
				port = $("#port").val();
				policy = $("#policy").val();
				identifyFile = $("#identifyFile").val();
				var sshConfig = {
						"user":username,
						"host":host,
						"proxy":proxy,
						"ip":ip,
						"port":port,
						"policy":policy,
						"identifyFile":identifyFile
				};
				var addData = "";
				if(proxy == false){
					addData = {
						"ciCodeCredential":ciCodeCredential	
					}
				}else{
					addData = {
							"ciCodeCredential":ciCodeCredential,
							"sshConfig":sshConfig
						}
				}
				$.ajax({
					url : ctx + "/secret/addCredential.do",
					data : addData,
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
										layer.msg("创建成功！", {
											icon : 6
										});
										setTimeout('window.location.reload()', 1500);
									}
								});
							} else {
								var html = "<option value='"+data.id+"'>"+username +" ("+code+") ("+remark+")"+"</option>";
								$("#codeCredentials").append(html);
								layer.closeAll();
							}
						} else {
							layer.alert("代码认证导入失败",
								function(indexAlert){
									layer.closeAll();
							});
						}
					}
				});
			}
		});
	});
	//选择认证类型
	$(".ssh").hide();
//	$(document).on('change','#CredentialsType',function(){
//		var credentialsType = $("#CredentialsType").val();
//		if(credentialsType == 1){
//			$(".normal").show();
//			$(".ssh").hide();
//			$("#SSHpasswordCred").val("");
//		}else{
//			$(".normal").hide();
//			$(".ssh").show();
//			$("#passwordCred").val("");
//		}
//	});
	
	//代理
	$("#proxy").click(function(){
		var proxyVal = $("#proxy:checked").length;
		if(proxyVal == 1){
			proxy = true;
			$(".adminLimitproxy").show();
		}else if(proxyVal == 0){
			proxy = false;
			$(".adminLimitproxy").hide();
		}
	})

});

//批量删除密钥
function delSecretKey(){
	var credIds = [];
	$('input[name="ids"]:checked').each(function(){
		var id = $(this).val();
		credIds.push(id);
	});
	if ("" == credIds) {
		layer.alert("请选择至少一个密钥", {icon:0});
		return;
	}
	layer.open({
		title: '删除密钥',
		content: '确定删除密钥？',
		btn: ['确定', '取消'],
		yes: function(index, layero){
			layer.close(index);
			$.ajax({
				url :""+ ctx + "/secret/delCredentials.do?credentials="+credIds,
				success : function(data){
					data = eval("("+data+")");
					if (data.status == "200") {
						window.location.reload(true);
					}
					else {
						layer.alert("删除失败,请检出服务器连接");
					}
				}
			});
		}
	});
}

//删除一个密钥
function delOneSecretKey(id){
	layer.open({
		title: '删除密钥',
		content: '确定删除密钥？',
		btn: ['确定', '取消'],
		yes: function(index, layero){
			layer.close(index);
			$.ajax({
				url :""+ ctx + "/secret/delCredential.do?id="+id,
				success : function(data){
					data = eval("("+data+")");
					if (data.status == "200") {
						window.location.reload(true);
					}
					else {
						layer.alert("删除失败,请检出服务器连接");
					}
				}
			});
		}
	});
}

//清除数据
function delData(){
	$("#CredentialsType").val(1);
	$(".normal").show();
	$(".ssh").hide();
	$("#userNameCred").val("");
	$("#keyRemark").val("");
	$("#passwordCred").val("");
	$("#SSHpasswordCred").val("");
}

//detail&修改 
function keyDetail(obj){
	var id = $(obj).attr("id");
	findCredData(id);
	layer.open({
		type: 1,
		title: '密钥详情',
		content: $("#secretKeyDetail"),
		area:['450px'],
		scrollbar:false,
		btn: ['关闭'],
		yes: function(index, layero){
			layer.close(index);
//			if (!judgeCredData()) {
//				return;
//			}
//			var type = $("#CredentialsType").val();
//			var codeType = $("#codeType").val();
//			var username = $("#userNameCred").val();
//			var password = $("#passwordCred").val();
//			var privateKey = $("#SSHpasswordCred").val();
//			var remark = $("#keyRemark").val();
//			$.ajax({
//				url : ctx + "/secret/updateCredential.do",
//				data : {
//					"id" : id,
//					"type" : type,
//					"codeType" : codeType,
//					"userName" : username,
//					"password" : password,
//					"privateKey" : privateKey,
//					"remark" : remark
//				},
//				success : function(data) {
//					data = eval("(" + data + ")");
//					if (data.status=="200") {
//						window.location.reload();
//					} else if (data.status=="400") {
//						layer.alert("代码认证修改失败，请您检查服务路径");
//					}
//				}
//			});
		}
	});
}

//查询加载数据
function findCredData(id){
	$.ajax({
		url : ""+ctx+"/secret/detailCredential.do?id="+id,
		async:false,
		success : function (data) {
			data = eval("("+data+")");
			var cred = data.credential;
			$("#userNameCredDetail").val(cred.userName);
			$("#keyRemarkDetail").val(cred.remark);
			$("#CredentialsTypeDetail").val(cred.type);
			$("#codeTypeDetail").val(cred.codeType);
			$("#SSHpasswordCredDetail").val(cred.privateKey);
			if(cred.type == 1){
				//type=1 normal  type=2 ssh
				$(".normal").show();
				$(".ssh").hide();
				$("#passwordCredDetail").val(cred.password);
			}else{
				$(".normal").hide();
				$(".ssh").show();
				$("#SSHpasswordCredDetail").val(cred.privateKey);
			}
		}
	});
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
//	if (type == 2 ) {
//		code = "SSH";
//		if (!privateKey || privateKey.length < 1) {
//			layer.tips('密钥不能为空','#SSHpasswordCred',{tips:[1,'#3595CC']});
//			$("#SSHpasswordCred").focus();
//			return;
//		}
//	}
	if (!remark || remark.length < 1) {
		layer.tips('描述信息不能为空','#keyRemark',{tips:[1,'#3595CC']});
		$("#keyRemark").focus();
		return;
	}
	return true;
}

//复制密钥按钮
function copySshPwd(obj){
	//var sshPassword=document.getElementById("sshPassword");
	var sshPassword = $(obj).parent().next("textarea");
	sshPassword.select(); // 选择对象
	document.execCommand("Copy"); // 执行浏览器复制命令
}




