$(function(){
	//创建密钥
	$("#createKeyBtn").click(function(){
		//判断租户是否添加了shera环境
		var flag = false;
//		$.ajax({
//			url:ctx + "/ci/judgeShera.do",
//			async : false,
//			success : function(data){
//				data = eval("("+data+")");
//				if (data.status == "400") {
//					layer.open({
//						title: '提示',
//						content: '当前租户没有添加shera环境，请您联系管理员',
//						btn: ['确定', '取消']
//					});
//					flag = true;
//				}
//			}
//		})
		if (flag) {
			return;
		}

		delData();
		layer.open({
			type: 1,
			title: '创建',
			content: $("#createKeyCon"),
			scrollbar:false,
			btn: ['确定', '取消'],
			yes: function(index, layero){
				if (!judgeCredData()) {
					return;
				}
				var type = $("#CredentialsType").val();
				var codeType = $("#codeType").val();
				var username = $("#userNameCred").val();
				var password = $("#passwordCred").val();
				var privateKey = $("#SSHpasswordCred").val();
				var remark = $("#keyRemark").val();
				var code = type==1?"HTTP":"SSH";
				$.ajax({
					url : ctx + "/secret/addCredential.do",
					data : {
						"type" : type,
						"codeType":codeType,
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
							}
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
		title: '修改',
		content: $("#createKeyCon"),
		scrollbar:false,
		btn: ['修改', '取消'],
		yes: function(index, layero){
			if (!judgeCredData()) {
				return;
			}
			var type = $("#CredentialsType").val();
			var codeType = $("#codeType").val();
			var username = $("#userNameCred").val();
			var password = $("#passwordCred").val();
			var privateKey = $("#SSHpasswordCred").val();
			var remark = $("#keyRemark").val();
			$.ajax({
				url : ctx + "/secret/updateCredential.do",
				data : {
					"id" : id,
					"type" : type,
					"codeType" : codeType,
					"userName" : username,
					"password" : password,
					"privateKey" : privateKey,
					"remark" : remark
				},
				success : function(data) {
					data = eval("(" + data + ")");
					if (data.status=="200") {
						window.location.reload();
					} else if (data.status=="400") {
						layer.alert("代码认证修改失败，请您检查服务路径");
					}
				}
			});
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
			$("#userNameCred").val(cred.userName);
			$("#keyRemark").val(cred.remark);
			$("#CredentialsType").val(cred.type);
			$("#codeType").val(cred.codeType);
			if(cred.type == 1){
				$(".normal").show();
				$(".ssh").hide();
				$("#passwordCred").val(cred.password);
			}else{
				$(".normal").hide();
				$(".ssh").show();
				$("#SSHpasswordCred").val(cred.privateKey);
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
function copySshPwd(){
	var sshPassword=document.getElementById("sshPassword");
	sshPassword.select(); // 选择对象
	document.execCommand("Copy"); // 执行浏览器复制命令
}




