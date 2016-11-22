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
			 title: '用户信息',
			 content:'确定修改用户信息？',
			 btn: ['确定', '取消'],
			 yes: function(index, layero){ //或者使用btn1
				 layer.close(index);
				 $("#update_tenement").attr("action", ctx+'/user/update.do');
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

	/**
	 * 填写时资源配额 表单验证
	 */
	$('#cpu_account').blur(function (event) {
		var cpu_account = $('#cpu_account').val().trim();
		if (cpu_account === '') {
			layer.tips('请填写CPU数量', '#cpu_account', {
				tips: [1, '#EF6578']
			});
		}
	});
	$('#ram').blur(function (event) {
		var ram = $('#ram').val().trim();
		if (ram === '') {
			layer.tips('请填写内存', '#ram', {
				tips: [1, '#EF6578']
			});
		}
	});
/*	$('#pod_count').blur(function (event) {
		var pod_count = $('#pod_count').val().trim();
		if (pod_count === '') {
			layer.tips('请填写Pod数量', '#pod_count', {
				tips: [1, '#EF6578']
			});
		}
	});
	$('#image_control').blur(function (event) {
		var image_control = $('#image_control').val().trim();
		if (image_control === '') {
			layer.tips('请填写副本控制器', '#image_control', {
				tips: [1, '#EF6578']
			});
		}
	});
	$('#server_count').blur(function (event) {
		var server_count = $('#server_count').val().trim();
		if (server_count === '') {
			layer.tips('请填写服务', '#server_count', {
				tips: [1, '#EF6578']
			});
		}
	});*/
	/**
	 * 填写时验证资源限制信息
	 */
	$('#pod_cpu_default').blur(function (event) {
		checkVal('#pod_cpu_default');
	});
	$('#pod_cpu_max').blur(function (event) {
		checkVal('#pod_cpu_max');
	});
	$('#pod_cpu_min').blur(function (event) {
		checkVal('#pod_cpu_min');
	});

	$('#pod_memory_default').blur(function (event) {
		checkVal('#pod_memory_default');
	});
	$('#pod_memory_max').blur(function (event) {
		checkVal('#pod_memory_max');
	});
	$('#pod_memory_min').blur(function (event) {
		checkVal('#pod_memory_min');
	});


	$('#container_cpu_default').blur(function (event) {
		checkVal('#container_cpu_default');
	});
	$('#container_memory_default').blur(function (event) {
		checkVal('#container_memory_default');
	});
	$('#container_cpu_max').blur(function (event) {
		checkVal('#container_cpu_max');
	});
	$('#container_memory_max').blur(function (event) {
		checkVal('#container_memory_max');
	});
	$('#container_cpu_min').blur(function (event) {
		checkVal('#container_cpu_min');
	});
	$('#container_memory_min').blur(function (event) {
		checkVal('#container_memory_min');
	});
});

function checkVal(id) {
	var idVal = $(id).val().trim();
	if (idVal === '') {
		showMsg('请填写数值', id)
	} else {
		var defaultVal;
		var maxVal;
		var minVal;
		if (id.indexOf("pod") != -1) {
			if (id.indexOf("cpu") != -1) {
				defaultVal = $('#pod_cpu_default').val().trim();
				maxVal = $('#pod_cpu_max').val().trim();
				minVal = $('#pod_cpu_min').val().trim();
			} else {
				defaultVal = $('#pod_memory_default').val().trim();
				maxVal = $('#pod_memory_max').val().trim();
				minVal = $('#pod_memory_min').val().trim();
			}
		} else {
			if (id.indexOf("cpu") != -1) {
				defaultVal = $('#container_cpu_default').val().trim();
				maxVal = $('#container_cpu_max').val().trim();
				minVal = $('#container_cpu_min').val().trim();
			} else {
				defaultVal = $('#container_memory_default').val().trim();
				maxVal = $('#container_memory_max').val().trim();
				minVal = $('#container_memory_min').val().trim();
			}
		}
		if (id.indexOf("default") != -1) {
			if (minVal > defaultVal) {
				showMsg('下限不应大于默认值', id)
			}
			if (defaultVal > maxVal) {
				showMsg('默认值不应大于上限', id)
			}
		}
		if (id.indexOf("min") != -1) {
			if (minVal > defaultVal) {
				showMsg('下限不应大于默认值', id)
			}
			if (minVal > maxVal) {
				showMsg('下限不应大于上限', id)
			}
		}
		if (id.indexOf("max") != -1) {

			if (defaultVal > maxVal) {
				showMsg('默认值不应大于上限', id)
			}
			if (minVal > maxVal) {
				showMsg('下限不应大于上限', id)
			}
		}

	}
}
function showMsg(msg, id) {
	layer.tips(msg, id, {
		tips: [1, '#EF6578']
	});
}


/*$(function(){
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
});*/

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


/**
 * 资源配额 表单验证
 * @returns {Boolean}
 */
function checkResourceQuota (){
	 var cpu_account = $.trim($("#cpu_account").val());
	 var ram = $.trim($("#ram").val());
	 var vol = $.trim($("#vol").val());
	 var image_count = $.trim($("#image_count").val());
	 //var pod_count = $.trim($("#pod_count").val());
	 //var image_control = $.trim($("#image_control").val());
	 //var server_count = $.trim($("#server_count").val());
//	 var vol_count = $.trim($("#vol_count").val());

	if(cpu_account === ''){
		layer.tips('请填写CPU数量', '#cpu_account',{
			tips: [1, '#EF6578']
		});
		$("#cpu_account").focus();
		return false;
	}
	if(ram === ''){
		layer.tips('请填写内存', '#ram',{
			tips: [1, '#EF6578']
		});
		$("#ram").focus();
		return false;
	}
/*	if(pod_count === ''){
		layer.tips('请填写Pod数量', '#pod_count',{
			tips: [1, '#EF6578']
		});
		$("#pod_count").focus();
		return false;
	}
	if(image_control === ''){
		layer.tips('请填写副本控制器', '#image_control',{
			tips: [1, '#EF6578']
		});
		$("#image_control").focus();
		return false;
	}
	if(server_count === ''){
		layer.tips('请填写服务', '#server_count',{
			tips: [1, '#EF6578']
		});
		$("#server_count").focus();
		return false;
	}
	 $("#ram").attr("value", ram);*/
}


/**
 * 验证资源限制信息
 * @returns {Boolean}
 */
function checkRestriction() {
	var aaa = document.getElementsByClassName("restrictionVal");
	for (var i = 0; i < aaa.length; i++) {
		var id = aaa[i].id;
		var idVal = $.trim($('#' + id).val());
		if (idVal == '') {
			layer.tips('请填写数值', '#' + id, {
				tips: [1, '#EF6578']
			});
			$('#' + id).focus();
			return false;
		}
	}
	for (var i = 0; i < aaa.length; i++) {
		var id = aaa[i].id;
		var idVal = $.trim($('#' + id).val());
		var defaultVal;
		var maxVal;
		var minVal;
		if (id.indexOf("pod") != -1) {
			if (id.indexOf("cpu") != -1) {
				defaultVal = $('#pod_cpu_default').val().trim();
				maxVal = $('#pod_cpu_max').val().trim();
				minVal = $('#pod_cpu_min').val().trim();
			} else {
				defaultVal = $('#pod_memory_default').val().trim();
				maxVal = $('#pod_memory_max').val().trim();
				minVal = $('#pod_memory_min').val().trim();
			}
		} else {
			if (id.indexOf("cpu") != -1) {
				defaultVal = $('#container_cpu_default').val().trim();
				maxVal = $('#container_cpu_max').val().trim();
				minVal = $('#container_cpu_min').val().trim();
			} else {
				defaultVal = $('#container_memory_default').val().trim();
				maxVal = $('#container_memory_max').val().trim();
				minVal = $('#container_memory_min').val().trim();
			}
		}
		if (id.indexOf("default") != -1) {
			if (minVal > defaultVal) {
				layer.tips('下限不应大于默认值', '#' + id, {
					tips: [1, '#EF6578']
				});
				$('#' + id).focus();
				return false;
			}
			if (defaultVal > maxVal) {
				layer.tips('默认值不应大于上限', '#' + id, {
					tips: [1, '#EF6578']
				});
				$('#' + id).focus();
				return false;
			}
		}
		if (id.indexOf("min") != -1) {
			if (minVal > defaultVal) {
				layer.tips('下限不应大于默认值', '#' + id, {
					tips: [1, '#EF6578']
				});
				$('#' + id).focus();
				return false;
			}
			if (minVal > maxVal) {
				layer.tips('下限不应大于上限', '#' + id, {
					tips: [1, '#EF6578']
				});
				$('#' + id).focus();
				return false;
			}
		}
		if (id.indexOf("max") != -1) {
			if (defaultVal > maxVal) {
				layer.tips('默认值不应大于上限', '#' + id, {
					tips: [1, '#EF6578']
				});
				$('#' + id).focus();
				return false;
			}
			if (minVal > maxVal) {
				layer.tips('下限不应大于上限', '#' + id, {
					tips: [1, '#EF6578']
				});
				$('#' + id).focus();
				return false;
			}
		}

	}
	$('#' + id).attr("value", idVal);
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




