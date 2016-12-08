$(function(){
	//创建密钥
	$("#createKeyBtn").click(function(){
		
		layer.open({
			type: 1,
	        title: '创建',
	        content: $("#createKeyCon"),
	        scrollbar:false,
	        btn: ['确定', '取消'],
	        yes: function(index, layero){ 
	        	
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

//detail&修改
function keyDetail(obj){
	var userNameCred = $(obj).attr("keyName");
	var keyType = $(obj).parent().next().find("input#key-type-hidden").val();
	
	if(keyType == "http"){
		$(".normal").show();
		$(".ssh").hide();
	}else{
		$(".normal").hide();
		$(".ssh").show();
	}
	$("#userNameCred").val(userNameCred);
	
	layer.open({
		type: 1,
        title: '创建',
        content: $("#createKeyCon"),
        scrollbar:false,
        btn: ['修改', '取消'],
        yes: function(index, layero){ 
        	
        }
	})
}





