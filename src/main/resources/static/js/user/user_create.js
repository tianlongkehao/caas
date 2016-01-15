$(document).ready(function(){

    $(".next2").click(function(){
    	var flag = checkBasicInfo();
    	if(flag === false){
    		return;
    	}
  		$(".radius_step").removeClass("action").eq(1).addClass("action");
        $(".step-inner").css("left","-100%");

    });
    $(".next3").click(function(){
    	var flag = checkResourceQuota();
    	if(flag === false){
    		return;
    	}
        $(".radius_step").removeClass("action").eq(2).addClass("action");
        $(".step-inner").css("left","-200%");

    });
    $(".last_step").click(function(){
        if($(".radius_step").eq(1).hasClass("action")){
            $(".step-inner").css("left","0");
            $(".radius_step").removeClass("action").eq(0).addClass("action");
        }else if($(".radius_step").eq(2).hasClass("action")){
            $(".step-inner").css("left","-100%");
            $(".radius_step").removeClass("action").eq(1).addClass("action");
        }

    });

    $(".go_backs").click(function(){
        if($(".radius_step").eq(1).hasClass("action")){
            $(".step-inner").css("left","0");
            $(".radius_step").removeClass("action").eq(0).addClass("action");
        }else if($(".radius_step").eq(2).hasClass("action")){
            $(".step-inner").css("left","-100%");
            $(".radius_step").removeClass("action").eq(1).addClass("action");
        }

    });
    
    $("#user_save_finishBtn").click(function(){
    	var flag = checkRestriction();
    	if(flag == false){
    		return;
    	}
    	layer.open({
			 title: '用户信息',
			 content:'确定创建新用户？',
			 btn: ['确定', '取消'],
			 yes: function(index, layero){ //或者使用btn1
			 	//按钮【按钮一】的回调
				 layer.close(index);
				 $('#add_tenement').attr("action", '/user/save.do');
				 $('#add_tenement').submit();
		 	 },
			 cancel: function(index){ //或者使用btn2
			 	//按钮【按钮二】的回调
		 	 }
		 });
    	
    });
	/**
	 * 填写时用户基本信息 表单验证
	 */
	$('#userName').blur(function(event){
		var userName = $('#userName').val().trim();
		if(userName.length === 0){
			layer.tips('登陆名称不能为空', '#userName', {
				tips: [1, '#0FA6D8'] //还可配置颜色
			});
		} else if (userName.length < 4){
			layer.tips('登陆名称过短','#userName',{
				tips: [1, '#0FA6D8']
			});
		} else {
			var un = userName.toLowerCase();
			console.info(un);
			$("#userName").val(un);
			$.get(
				"/user/checkUsername/"+un,
				function(data,status){
					console.info("Data: " + data + "\nStatus: " + status);
					var data = eval("(" + data + ")");
					if(data.status=="400"){
						//layer.alert("登陆帐号已经被使用，请输入新的帐号！");
						layer.tips('登陆帐号已经被使用，请输入新的帐号！','#userName',{
						 tips: [1, '#0FA6D8']
						 });
					}
					if(data.status=="300"){
						layer.alert("k8s已经建立此名称的namespace，请输入新的帐号！");
						closeBtn(0);
					}
				});
		}
	});
	$('#user_realname').blur(function(event){
		var user_realname = $('#user_realname').val().trim();
		if(user_realname.length === 0){
			layer.tips('真实姓名不能为空', '#user_realname', {
				tips: [1, '#0FA6D8'] //还可配置颜色
			});
		}
	});
	$('#pwd').blur(function(event){
		var pwd = $('#pwd').val().trim();
		if(pwd.length === 0){
			layer.tips('登陆密码不能为空', '#pwd', {
				tips: [1, '#0FA6D8'] //还可配置颜色
			});
		}else if(pwd.length < 6 || checkStrong(password) < 2){
			layer.tips('密码过于简单，密码必须是数字、字母、特殊字符两种及以上的组合', '#pwd', {
				tips: [1, '#0FA6D8'] //还可配置颜色
			});
		}
	});
	$('#confirm_pwd').blur(function(event){
		var confirm_pwd = $('#confirm_pwd').val().trim();
		var pwd = $('#pwd').val().trim();
		if(confirm_pwd.length === 0){
			layer.tips('确认密码不能为空', '#confirm_pwd', {
				tips: [1, '#0FA6D8'] //还可配置颜色
			});
		}else if(confirm_pwd != pwd){
			layer.tips('两次密码输入不一致', '#confirm_pwd', {
				tips: [1, '#0FA6D8'] //还可配置颜色
			});
		}
	});
	$('#email').blur(function(event){
		var email = $('#email').val().trim();
		if (email.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) !== -1) {
			if (email.length > 50) {
				layer.tips('邮箱长度不能大于50', '#email',{
					tips: [1, '#0FA6D8']
				});
			}
		} else {
			if (email.length === 0) {
				layer.tips('邮箱不能为空', '#email',{
					tips: [1, '#0FA6D8']
				});
			} else {
				layer.tips('请输入合法的邮箱', '#email',{
					tips: [1, '#0FA6D8']
				});
			}
		}
	});

	/**
	 * 填写时资源配额 表单验证
	 */
	$('#cpu_account').blur(function(event){
		var cpu_account = $('#cpu_account').val().trim();
		if(cpu_account === ''){
			layer.tips('请填写CPU数量', '#cpu_account',{
				tips: [1, '#0FA6D8']
			});
		}
	});
	$('#ram').blur(function(event){
		var ram = $('#ram').val().trim();
		if(ram === ''){
			layer.tips('请填写内存', '#ram',{
				tips: [1, '#0FA6D8']
			});
		}
	});
	$('#pod_count').blur(function(event){
		var pod_count = $('#pod_count').val().trim();
		if(pod_count === ''){
			layer.tips('请填写Pod数量', '#pod_count',{
				tips: [1, '#0FA6D8']
			});
		}
	});
	$('#image_control').blur(function(event){
		var image_control = $('#image_control').val().trim();
		if(image_control === ''){
			layer.tips('请填写副本控制器', '#image_control',{
				tips: [1, '#0FA6D8']
			});
		}
	});
	$('#server_count').blur(function(event){
		var server_count = $('#server_count').val().trim();
		if(server_count === ''){
			layer.tips('请填写服务', '#server_count',{
				tips: [1, '#0FA6D8']
			});
		}
	});
	/**
	 * 填写时验证资源限制信息
	 */
	$('#pod_cpu_default').blur(function(event){
		var pod_cpu_default = $('#pod_cpu_default').val().trim();
		if(pod_cpu_default === ''){
			layer.tips('请填写 POD CPU默认值', '#pod_cpu_default',{
				tips: [1, '#0FA6D8']
			});
		}
	});
	$('#pod_memory_default').blur(function(event){
		var pod_memory_default = $('#pod_memory_default').val().trim();
		if(pod_memory_default === ''){
			layer.tips('请填写 POD内存 默认值', '#pod_memory_default',{
				tips: [1, '#0FA6D8']
			});
		}
	});
	$('#pod_cpu_max').blur(function(event){
		var pod_cpu_max = $('#pod_cpu_max').val().trim();
		if(pod_cpu_max === ''){
			layer.tips('请填写 POD CPU 上限', '#pod_cpu_max',{
				tips: [1, '#0FA6D8']
			});
		}
	});
	$('#pod_memory_max').blur(function(event){
		var pod_memory_max = $('#pod_memory_max').val().trim();
		if(pod_memory_max === ''){
			layer.tips('请填写 POD内存上限', '#pod_memory_max',{
				tips: [1, '#0FA6D8']
			});
		}
	});
	$('#pod_cpu_min').blur(function(event){
		var pod_cpu_min = $('#pod_cpu_min').val().trim();
		if(pod_cpu_min === ''){
			layer.tips('请填写POD CPU 下限', '#pod_cpu_min',{
				tips: [1, '#0FA6D8']
			});
		}
	});
	$('#pod_memory_min').blur(function(event){
		var pod_memory_min = $('#pod_memory_min').val().trim();
		if(pod_memory_min === ''){
			layer.tips('请填写POD内存下限', '#pod_memory_min',{
				tips: [1, '#0FA6D8']
			});
		}
	});
	$('#container_cpu_default').blur(function(event){
		var container_cpu_default = $('#container_cpu_default').val().trim();
		if(container_cpu_default === ''){
			layer.tips('请填写 容器CPU默认值', '#container_cpu_default',{
				tips: [1, '#0FA6D8']
			});
		}
	});
	$('#container_memory_default').blur(function(event){
		var container_memory_default = $('#container_memory_default').val().trim();
		if(container_memory_default === ''){
			layer.tips('请填写 容器内存 默认值', '#container_memory_default',{
				tips: [1, '#0FA6D8']
			});
		}
	});
	$('#container_cpu_max').blur(function(event){
		var container_cpu_max = $('#container_cpu_max').val().trim();
		if(container_cpu_max === ''){
			layer.tips('请填写容器 CPU 上限', '#container_cpu_max',{
				tips: [1, '#0FA6D8']
			});
		}
	});
	$('#container_memory_max').blur(function(event){
		var container_memory_max = $('#container_memory_max').val().trim();
		if(container_memory_max === ''){
			layer.tips('请填 容器内存上限', '#container_memory_max',{
				tips: [1, '#0FA6D8']
			});
		}
	});
	$('#container_cpu_min').blur(function(event){
		var container_cpu_min = $('#container_cpu_min').val().trim();
		if(container_cpu_min === ''){
			layer.tips('请填写容器 CPU 下限', '#container_cpu_min',{
				tips: [1, '#0FA6D8']
			});
		}
	});
	$('#container_memory_min').blur(function(event){
		var container_memory_min = $('#container_memory_min').val().trim();
		if(container_memory_min === ''){
			layer.tips('请填写容器内存下限', '#container_memory_min',{
				tips: [1, '#0FA6D8']
			});
		}
	});
});



/**
 * 用户基本信息 表单验证
 * @returns {Boolean}
 */
function checkBasicInfo(){
	var username = $.trim($('#userName').val());
	var realname = $.trim($("#user_realname").val());
	var company = $.trim($("#company").val());
	var user_department = $.trim($("#user_department").val());
	var user_employee_id = $.trim($("#user_employee_id").val());
	var user_cellphone = $.trim($("#user_cellphone").val());
	var user_phone = $.trim($("#user_phone").val());
	var email = $.trim($('#email').val());

	var password = $.trim($('#pwd').val());
	var confirmNewPwd = $.trim($('#confirm_pwd').val());

    if (username == '') {
		layer.tips('登陆名称不能为空', '#userName', {
			tips: [1, '#0FA6D8'] //还可配置颜色
		});
		$('#userName').focus;
		return false;
    } else if(username.length < 4){
		layer.tips('登陆名称过短','#userName',{
			tips: [1, '#0FA6D8']
		});
		$('#userName').focus;
		return false;
	} else{
		var un = username.toLowerCase();
		console.info(un);
		$("#userName").val(un);
		$.get(
			"/user/checkUsername/"+un,
			function(data,status){
				console.info("Data: " + data + "\nStatus: " + status);
				var data = eval("(" + data + ")");
				if(data.status=="400"){
					layer.tips('登陆帐号已经被使用，请输入新的帐号！','#userName',{
						tips: [1, '#0FA6D8']
					});
					$('#userName').focus;
					return false;
				}
				if(data.status=="300"){
					layer.tips('k8s已经建立此名称的namespace，请输入新的帐号！','#userName',{
						tips: [1, '#0FA6D8']
					});
					$('#userName').focus;
					return false;
				}
			});
	}
    if (password == '') {
		layer.tips('密码不能为空', '#pwd', {
			tips: [1, '#0FA6D8'] //还可配置颜色
		});
		$('#pwd').focus;
		return false;
    } else if (password.length < 6 || checkStrong(password) < 2){
		layer.tips('密码过于简单，密码必须是数字、字母、特殊字符两种及以上的组合', '#pwd', {
			tips: [1, '#0FA6D8'] //还可配置颜色
		});
		$('#pwd').focus;
		return false;
	}
    /*if (password.length < 6 || username.length < 4 || username.length > 18) {
	   layer.msg('用户名或密码过短', {icon: 5});
	   return false;
    }*/
    if(password !== confirmNewPwd){
      layer.tips('两次密码不一致',$('#confirm_pwd'),{tips: [1, '#EF6578']});
      $('#confirm_pwd').focus();
      return false;
    }

    if(realname === ''){
		if(realname.length === 0){
			layer.tips('真实姓名不能为空', '#user_realname', {
				tips: [1, '#0FA6D8'] //还可配置颜色
			});
		}
    	$("#user_realname").focus();
        return false;
    }
    if (email.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) !== -1) {
        if (email.length > 50) {
			layer.tips('邮箱长度不能大于50', '#email',{
				tips: [1, '#0FA6D8']
			});
          $('#email').focus();
          return false;
        }
    } else {
        if (email === '') {
			layer.tips('邮箱不能为空', '#email',{
				tips: [1, '#0FA6D8']
			});
          $('#email').focus();
          return false;
        } else {
			layer.tips('请输入合法的邮箱', '#email',{
				tips: [1, '#0FA6D8']
			});
          $('#email').focus();
          return false;
        }
    }
    $("#userName").attr("value", userName);
    $("#user_realname").attr("value", user_realname);
    $("#company").attr("value", company);
    $("#user_department").attr("value", user_department);
    $("#user_employee_id").attr("value", user_employee_id);
    $("#user_cellphone").attr("value", user_cellphone);
    $("#user_phone").attr("value", user_phone);
    $("#email").attr("value", email);
}

/**
 * 资源配额 表单验证
 * @returns {Boolean}
 */
function checkResourceQuota (){
	 var cpu_account = $.trim($("#cpu_account").val());
	 var ram = $.trim($("#ram").val());
	 var pod_count = $.trim($("#pod_count").val());
	 var image_control = $.trim($("#image_control").val());
	 var server_count = $.trim($("#server_count").val());
//	 var vol_count = $.trim($("#vol_count").val());
//	 var vol = $.trim($("#vol").val());
	 
	 if(cpu_account === ''){
		 layer.tips('请填写CPU数量', '#cpu_account',{
			 tips: [1, '#0FA6D8']
		 });
    	$("#cpu_account").focus();
        return false;
	 }
	 if(ram === ''){
		 layer.tips('请填写内存', '#ram',{
			 tips: [1, '#0FA6D8']
		 });
		 $("#ram").focus();
		 return false;
	 }
	 if(pod_count === ''){
		 layer.tips('请填写Pod数量', '#pod_count',{
			 tips: [1, '#0FA6D8']
		 });
		 $("#pod_count").focus();
		 return false;
	 }
	 if(image_control === ''){
		 layer.tips('请填写副本控制器', '#image_control',{
			 tips: [1, '#0FA6D8']
		 });
		 $("#image_control").focus();
		 return false;
	 }
	 if(server_count === ''){
		 layer.tips('请填写服务', '#server_count',{
			 tips: [1, '#0FA6D8']
		 });
		 $("#server_count").focus();
		 return false;
	 }
	 $("#ram").attr("value", ram);
}

/**
 * 验证资源限制信息
 * @returns {Boolean}
 */
function checkRestriction() {
	var pod_cpu_default = $.trim($("#pod_cpu_default").val());
	var pod_cpu_max = $.trim($("#pod_cpu_max").val());
	var pod_cpu_min = $.trim($("#pod_cpu_min").val());
	var pod_memory_default = $.trim($("#pod_memory_default").val());
	var pod_memory_max = $.trim($("#pod_memory_max").val());
	var pod_memory_min = $.trim($("#pod_memory_min").val());
	
	var container_cpu_default = $.trim($("#container_cpu_default").val());
	var container_cpu_max = $.trim($("#container_cpu_max").val());
	var container_cpu_min = $.trim($("#container_cpu_min").val());
	var container_memory_default = $.trim($("#container_memory_default").val());
	var container_memory_max = $.trim($("#container_memory_max").val());
	var container_memory_min = $.trim($("#container_memory_min").val());
	
	if(pod_cpu_default === ''){
		layer.tips('请填写 POD CPU默认值', '#pod_cpu_default',{
			tips: [1, '#0FA6D8']
		});
    	$("#pod_cpu_default").focus();
        return false;
	}
	if(pod_memory_default === ''){
		layer.tips('请填写 POD内存 默认值', '#pod_memory_default',{
			tips: [1, '#0FA6D8']
		});
		$("#pod_memory_default").focus();
		return false;
	}
	if(pod_cpu_max === ''){
		layer.tips('请填写 POD CPU 上限', '#pod_cpu_max',{
			tips: [1, '#0FA6D8']
		});
		$("#pod_cpu_max").focus();
		return false;
	}
	if(pod_memory_max === ''){
		layer.tips('请填写 POD内存上限', '#pod_memory_max',{
			tips: [1, '#0FA6D8']
		});
		$("#pod_memory_max").focus();
		return false;
	}
	if(pod_cpu_min === ''){
		layer.tips('请填写POD CPU 下限', '#pod_cpu_min',{
			tips: [1, '#0FA6D8']
		});
		$("#pod_cpu_min").focus();
		return false;
	}
	if(pod_memory_min === ''){
		layer.tips('请填写POD内存下限', '#pod_memory_min',{
			tips: [1, '#0FA6D8']
		});
		$("#pod_memory_min").focus();
		return false;
	}
	
	if(container_cpu_default === ''){
		layer.tips('请填写 容器CPU默认值', '#container_cpu_default',{
			tips: [1, '#0FA6D8']
		});
		$("#container_cpu_default").focus();
		return false;
	}
	if(container_memory_default === ''){
		layer.tips('请填写 容器内存 默认值', '#container_memory_default',{
			tips: [1, '#0FA6D8']
		});
		$("#container_memory_default").focus();
		return false;
	}
	if(container_cpu_max === ''){
		layer.tips('请填写容器 CPU 上限', '#container_cpu_max',{
			tips: [1, '#0FA6D8']
		});
		$("#container_cpu_max").focus();
		return false;
	}
	if(container_memory_max === ''){
		layer.tips('请填 容器内存上限', '#container_memory_max',{
			tips: [1, '#0FA6D8']
		});
		$("#container_memory_max").focus();
		return false;
	}
	if(container_cpu_min === ''){
		layer.tips('请填写容器 CPU 下限', '#container_cpu_min',{
			tips: [1, '#0FA6D8']
		});
		$("#container_cpu_min").focus();
		return false;
	}
	if(container_memory_min === ''){
		layer.tips('请填写容器内存下限', '#container_memory_min',{
			tips: [1, '#0FA6D8']
		});
		$("#container_memory_min").focus();
		return false;
	}
	
	$("#container_memory_default").attr("value", container_memory_default);
	$("#container_cpu_default").attr("value", container_cpu_default);
	$("#container_memory_max").attr("value", container_memory_max);
	$("#container_cpu_max").attr("value", container_cpu_max);
	$("#container_memory_min").attr("value", container_memory_min);
	$("#container_cpu_min").attr("value", container_cpu_min);
	
	$("#pod_memory_default").attr("value", pod_memory_default);
	$("#pod_cpu_default").attr("value", pod_cpu_default);
	$("#pod_memory_max").attr("value", pod_memory_max);
	$("#pod_cpu_max").attr("value", pod_cpu_max);
	$("#pod_memory_min").attr("value", pod_memory_min);
	$("#pod_cpu_min").attr("value", pod_cpu_min);
	
}

//检测密码强度
//////////////////////////////////////////////////////////////////////
function checkStrong(sValue) {
	var modes = 0;
	//正则表达式验证符合要求的
	if (sValue.length < 1) return modes;
	if (/\d/.test(sValue)) modes++; //数字
	if (/[a-z]/.test(sValue)) modes++; //小写
	if (/[A-Z]/.test(sValue)) modes++; //大写
	if (/\W/.test(sValue)) modes++; //特殊字符

	//逻辑处理
	switch (modes) {
		case 1:
			return 1;
			break;
		case 2:
			return 2;
		case 3:
		case 4:
			return sValue.length < 12 ? 3 : 4
					break;
	}
}
