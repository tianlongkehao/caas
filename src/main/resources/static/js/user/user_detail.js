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
    
    $("#user_update_finishBtn").click(function(){
    	var flag = checkRestriction();
    	if(flag === false){
    		return;
    	}
    	layer.open({
			 type:1,
			 title: '用户信息',
			 content:'确定修改用户信息？',
			 btn: ['确定', '取消'],
			 yes: function(index, layero){ //或者使用btn1
				 layer.close(index);
				 
				 $("#update_tenement").attr("action", '/user/update.do');
				 $("#update_tenement").submit();
		 	 },
			 cancel: function(index){ //或者使用btn2
		 	 }
		 	 
		 });
    });

});


$(function(){
    var ramSlider = $("#ramSlider").slider({
        formatter: function(value) {
            return value;
        }
    });

    ramSlider.on("slide", function(slideEvt) {
        $("#ram").val(slideEvt.value);
    }).on("change", function(slideEvt){
        $("#ram").val(slideEvt.value.newValue);
    });

    $("#ram").on("change",function(){
        var ramVal = Number($(this).val());
        console.log(ramVal);
        ramSlider.slider('setValue', ramVal);
    });
});


$(function(){
    var volSlider = $("#volSlider").slider({
        formatter: function(value) {
            return value;
        }
    });

    volSlider.on("slide", function(slideEvt) {
        $("#vol").val(slideEvt.value);
    }).on("change", function(slideEvt){
        $("#vol").val(slideEvt.value.newValue);
    });

    $("#vol").on("change",function(){
        var volVal = Number($(this).val());
        console.log(volVal);
        volSlider.slider('setValue', volVal);
    });
});

function checkBasicInfo(){

    var company = $.trim($("#company").val());
	var user_department = $.trim($("#user_department").val());
	var user_employee_id = $.trim($("#user_employee_id").val());
	var user_cellphone = $.trim($("#user_cellphone").val());
	var user_phone = $.trim($("#user_phone").val());
    var email = $.trim($('#email').val());
    
    var user_autority = '';
	$(":selected[name='option']").each(function(){
		user_autority = jQuery(this).val();
	});

	if(user_autority === ''){
		layer.msg('请选择权限', {icon: 5});
		$("#user_autority").focus();
		return false;
	}
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
    	layer.msg('请填写CPU数量', {icon: 5});
    	$("#cpu_account").focus();
        return false;
	 }
	 if(ram === ''){
		 layer.msg('请填写内存', {icon: 5});
		 $("#ram").focus();
		 return false;
	 }
	 if(pod_count === ''){
		 layer.msg('请填写Pod数量', {icon: 5});
		 $("#pod_count").focus();
		 return false;
	 }
	 if(image_control === ''){
		 layer.msg('请填写副本控制器', {icon: 5});
		 $("#image_control").focus();
		 return false;
	 }
	 if(server_count === ''){
		 layer.msg('请填写服务', {icon: 5});
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
	
	console.info(" pod_memory_default: "+pod_memory_default);
	
	if(pod_cpu_default === ''){
    	alert('请填写 POD CPU默认值');
    	$("#pod_cpu_default").focus();
        return false;
	}
	if(pod_memory_default === ''){
		alert('请填写 POD内存 默认值');
		$("#pod_memory_default").focus();
		return false;
	}
	if(pod_cpu_max === ''){
		alert('请填写 POD CPU 上限');
		$("#pod_cpu_max").focus();
		return false;
	}
	if(pod_memory_max === ''){
		alert('请填写 POD内存上限');
		$("#pod_memory_max").focus();
		return false;
	}
	if(pod_cpu_min === ''){
		alert('请填写POD CPU 下限');
		$("#pod_cpu_min").focus();
		return false;
	}
	if(pod_memory_min === ''){
		alert('请填写POD内存下限');
		$("#pod_memory_min").focus();
		return false;
	}
	
	if(container_cpu_default === ''){
		alert('请填写 容器CPU默认值');
		$("#container_cpu_default").focus();
		return false;
	}
	if(container_memory_default === ''){
		alert('请填写 容器内存 默认值');
		$("#container_memory_default").focus();
		return false;
	}
	if(container_cpu_max === ''){
		alert('请填写容器 CPU 上限');
		$("#container_cpu_max").focus();
		return false;
	}
	if(container_memory_max === ''){
		alert('请填 容器内存上限');
		$("#container_memory_max").focus();
		return false;
	}
	if(container_cpu_min === ''){
		alert('请填写容器 CPU 下限');
		$("#container_cpu_min").focus();
		return false;
	}
	if(container_memory_min === ''){
		alert('请填写容器内存下限');
		$("#container_memory_min").focus();
		return false;
	}

	$("#container_memory_default").attr("value", container_memory_default);
	$("#container_memory_max").attr("value", container_memory_max);
	$("#container_memory_min").attr("value", container_memory_min);
	$("#container_cpu_default").attr("value", container_cpu_default);
	$("#container_cpu_max").attr("value", container_cpu_max);
	$("#container_cpu_min").attr("value", container_cpu_min);
	
	$("#pod_memory_default").attr("value", pod_memory_default);
	$("#pod_memory_max").attr("value", pod_memory_max);
	$("#pod_memory_min").attr("value", pod_memory_min);
	$("#pod_cpu_default").attr("value", pod_cpu_default);
	$("#pod_cpu_max").attr("value", pod_cpu_max);
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




