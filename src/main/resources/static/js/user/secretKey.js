$(function(){
	//创建密钥
	$("#createKeyBtn").click(function(){
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
				var username = $("#userNameCred").val();
				var password = $("#passwordCred").val();
				var privateKey = $("#SSHpasswordCred").val();
				var remark = $("#keyRemark").val();
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
							window.location.reload(true);
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
	
});

//批量删除密钥
function delSecretKey(){
	obj = document.getElementsByName("ids");
	var credIds = [];
    for (k in obj) {
        if (obj[k].checked) {
        	credIds.push(obj[k].value);
        }
    }
    if (credIds.length <=0) {
    	layer.msg( "请选择需要删除的镜像", {icon: 2 });
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
    })
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
    })
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
			var username = $("#userNameCred").val();
			var password = $("#passwordCred").val();
			var privateKey = $("#SSHpasswordCred").val();
			var remark = $("#keyRemark").val();
			$.ajax({
				url : ctx + "/secret/updateCredential.do",
				data : {
					"id" : id,
					"type" : type,
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
	})
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




