$(document).ready(function(){

	var user_autority_val = document.getElementById("user_autority_hidden").value;
	var autority_options = document.getElementById("user_autority").options;
	for(var i = 0; i < autority_options.length; i++){
		if (autority_options[i].value == user_autority_val){
			autority_options.selectedIndex = i;
			//document.getElementById("user_autority_hidden").value = autority_options[i].textContent;
			break;
		}
	}

	var user_province_val = document.getElementById("user_province_hidden").value;
	var province_options = document.getElementById("user_province").options;
	for(var i = 0; i < province_options.length; i++){
		if (province_options[i].value == user_province_val){
			province_options.selectedIndex = i;
			//document.getElementById("user_province_hidden").value = province_options[i].textContent;
			break;
		}
	}

    $(".next2").click(function(){
    	var flag = checkBasicInfo();
    	if(flag === false){
    		return;
    	}
    	$(".radius_step").removeClass("action").eq(1).addClass("action");
        $(".step-inner").css("left","-100%");

    });


    $("#user_create_next2").click(function(){
    	layer.open({
			 title: '用户信息',
			 content:'确定修改用户信息？',
			 btn: ['确定', '取消'],
			 yes: function(index, layero){ //或者使用btn1
				 layer.close(index);
				 $("#update_tenement").attr("action", ctx+'/user/update_management.do');
				 debugger
				 $("#update_tenement").submit();
		 	 },
			 cancel: function(index){ //或者使用btn2
		 	 }
		 	 
		 });
    });


	/**
	 * 填写时用户基本信息 表单验证
	 */
	$('#email').blur(function (event) {
		var email = $('#email').val().trim();
		if (email.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) !== -1) {
			if (email.length > 50) {
				layer.tips('邮箱长度不能大于50', '#email', {
					tips: [1, '#EF6578']
				});
			}
		} else {
			if (email.length === 0) {
				layer.tips('邮箱不能为空', '#email', {
					tips: [1, '#EF6578']
				});
			} else {
				layer.tips('请输入合法的邮箱', '#email', {
					tips: [1, '#EF6578']
				});
			}
		}
	});
})

function checkBasicInfo(){

    var company = $.trim($("#company").val());
	var user_department = $.trim($("#user_department").val());
	var user_employee_id = $.trim($("#user_employee_id").val());
	var user_cellphone = $.trim($("#user_cellphone").val());
	var user_phone = $.trim($("#user_phone").val());
    var email = $.trim($('#email').val());

    if (email.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) !== -1) {
        if (email.length > 50) {
         layer.tips('邮箱长度不能大于50', $('#email'),{tips: [1, '#EF6578']});
          $('#email').focus();
          return false;
        }
      } else {
        if (email === '') {
          layer.tips('邮箱不能为空', $('#email'),{tips: [1, '#EF6578']});
          $('#email').focus();
          return false;
        } else {
          layer.tips('请输入合法的邮箱', $('#email'),{tips: [1, '#EF6578']});
          $('#email').focus();
          return false;
        }
      }
    
    $("#company").attr("value", company);
	$("#user_department").attr("value", user_department);
	$("#user_employee_id").attr("value", user_employee_id);
	$("#user_cellphone").attr("value", user_cellphone);
	$("#user_phone").attr("value", user_phone);
	$("#email").attr("value", email);
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




