/**
 * Created by cuidong on 15-12-24.
 */


$(function(){

    var user_autority_val = document.getElementById("user_autority_hidden").value;
    var autority_options = document.getElementById("user_autority").options;
    for(var i = 0; i < autority_options.length; i++){
        if (autority_options[i].value == user_autority_val){
            autority_options.selectedIndex = i;
            break;
        }
    }

    var user_province_val = document.getElementById("user_province_hidden").value;
    var province_options = document.getElementById("user_province").options;
    for(var i = 0; i < province_options.length; i++){
        if (province_options[i].value == user_province_val){
            province_options.selectedIndex = i;
            break;
        }
    }


    var p=window.location.href;
    var reg = new RegExp("b", "");
    if (reg.test(p)){
            $("#pwd").removeClass("hide");
            $("#pwd").addClass("active");
            $("#pwd_wrap").removeClass("hide");

            $("#baseinfo").addClass("hide");
            $("#resourceinfo").addClass("hide");
            $("#preferenceinfo").addClass("hide");
            $("#baseinfo").removeClass("active");
            $("#baseinfo_wrap").addClass("hide");
        }

    $(".Record").click(function(){

        $(".Record").removeClass("active");
        $(this).addClass("active");

        $(".tab_wrap").addClass("hide");
        $("#"+$(this).attr("id")+"_wrap").removeClass("hide");


    });

//    var userCpuPer = $("#detailCpu")[0].textContent/$("#totalCpu")[0].textContent*100+"%";
//    $("#usedCpu")[0].style.width = userCpuPer;
//    var userMemPer = $("#detailMemory")[0].textContent/$("#totalMemory")[0].textContent*100+"%";
//    $("#usedMemory")[0].style.width = userMemPer;
//    var userVolPer = $("#detailVolume")[0].textContent/$("#totalVolume")[0].textContent*100+"%";
//    $("#usedVolume")[0].style.width = userVolPer;

    $("#basicInfo").click(function(){
    	var id = $("#user_id").val();
    	var company = $.trim($("#company").val());
    	var user_department = $.trim($("#user_department").val());
    	var user_employee_id = $.trim($("#user_employee_id").val());
    	var user_cellphone = $.trim($("#user_cellphone").val());
    	var user_phone = $.trim($("#user_phone").val());
        var email = $.trim($("#email").val());

    	var flag = checkBasicInfo();
    	if(flag === false){
    		return;
    	}
        layer.open({
            title: '修改信息',
            content: '确定修改基本信息吗？',
            btn: ['确定', '取消'],
            yes: function(index, layero){ //或者使用btn1
                //按钮【按钮一】的回调
                layer.close(index);
                $.ajax({
                    url:ctx+"/user/userModifyBasic.do?id="+id
                    +"&company="+company
                    +"&user_department="+user_department
                    +"&user_employee_id="+user_employee_id
                    +"&user_cellphone="+user_cellphone
                    +"&user_phone="+user_phone
                    +"&email="+email ,
                    success:function(data){
                        data = eval("(" + data + ")");
                        if(data.status === '200'){
                            layer.alert("信息修改成功");
                        }else{
                            layer.alert("信息修改失败");
                        }
                    }
                });
            },
            cancel: function(index){ //或者使用btn2
                //按钮【按钮二】的回调
            }
        });

    });

    $("#modifyPwd").click(function(){
    	var id = $("#user_id").val();
    	var pwd = $.trim($("#originalPwd").val());
    	var newpwd = $.trim($("#newPwd").val());

    	var flag = checkpwd();
    	if(flag === false){
    		return;
    	}
        layer.open({
            title: '修改密码',
            content: '确定修改密码吗？',
            btn: ['确定', '取消'],
            yes: function(index, layero){ //或者使用btn1
                //按钮【按钮一】的回调
                layer.close(index);
                $.ajax({
                    url:ctx+"/user/userModifyPsw.do?id="+id+"&password="+pwd+"&newpwd="+newpwd,
                    success:function(data){
                        data = eval("(" + data + ")");
                        if(data.status === '200'){
                            layer.msg("密码修改成功",{icon: 6});
                            setTimeout('window.location.href = ctx + "/login"',2000);

                        }else{
                            layer.alert("密码修改失败");
                        }
                    }
                });
            },
            cancel: function(index){ //或者使用btn2
                //按钮【按钮二】的回调
            }
        });
    });

   function checkpwd (){

	   var pwd = $.trim($("#originalPwd").val());
	   var newpwd = $.trim($("#newPwd").val());
	   var confirmpwd = $.trim($("#confirmNewPwd").val());

	   if(pwd === ''){
		   layer.alert("请输入原密码");
		   return false;
	   }
	   if(newpwd !== confirmpwd){
		   layer.alert("新密码与确认密码不一致，请重新输入");
		   $("#newPwd").focus();
		   return false;
	   }

    };

    function checkBasicInfo(){
        var email = $.trim($("#email").val());

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
    }

    //保存监控的偏好设置
    $("#preferSave").click(function(){
    	var monitor = 1;
    	if ($("#PinpointChk").prop("checked")==true){
    		monitor = 1;
    	} else {
    		monitor = 0;
    	}
    	$.ajax({
    		type : "POST",
    		url : ctx + "/user/userFavorUpdate.do?monitor="+monitor,
    		success : function(data) {
    			var data = eval("(" + data + ")");
    			if (data.status == "200") {
                    layer.alert("修改成功");
    				return;
    			}
    		}
    	});
    });

//    $("#sonarYesOrNo").click(function(){
//    	if($("#sonarYesOrNoChecked").is(":checked")){
//    		$(".sonarTwoStep").removeClass("hide");
//    	}else{
//    		$(".sonarTwoStep").addClass("hide");
//    	}
//    });
    //sonar同步,设置阈值
    $("#sonarSyn").click(function(){
    	sonarPrograssCheck();
    });
    //sonar异步
    $("#sonarAsyn").click(function(){
    	sonarPrograssCheck();
    });
    //增加一条白名单
    $("#addOneWhiteList").click(function(){
    	var whiteListHtml = '<div>'+
				    		'<input type="text" class="whiteList form-control" placeholder="git地址">'+
					    	'<input type="text" class="branchList form-control" placeholder="分支">'+
					    	'<select class="sonarValue form-control" name="sonarValue">'+
    						'<option value="a">A</option>'+
    						'<option value="b">B</option>'+
    						'<option value="c">C</option>'+
    						'<option value="d">D</option>'+
    						'<option value="e">E</option>'+
    						'</select>'+
					    	'<i class="fa fa-trash" onclick="delOneWhiteList(this)"></i>'+
					    	'</div>';
    	$(".whiteLists").append(whiteListHtml);
    });

  //是否进行sonar质量检查 检查true 不检查false
 	$("#sonarYesOrNoBtn").click(function(){
 		var $this = $(this);
 		var changeVal =  $this.attr("value");
 		if(changeVal == "true"){
 			$this.removeClass("fa-on").addClass("fa-off");
 			$this.next().empty().html("不检查");
 			$this.attr("value","false");
 			$(".sonarTwoStep").addClass("hide");
 		}else{
 			$this.removeClass("fa-off").addClass("fa-on");
 			$this.next().empty().html("检查");
 			$this.attr("value","true");
 			$(".sonarTwoStep").removeClass("hide");
 		}
	});
 	//是否显示检查结果   显示true 不显示false
 	$("#showSonarYesOrNoBtn").click(function(){
 		var $this = $(this);
 		var changeVal =  $this.attr("value");
 		if(changeVal == "true"){
			$this.removeClass("fa-off").addClass("fa-on");
 			$this.next().empty().html("显示检查结果");
 			$this.attr("value","false");
 		}else{
  			$this.removeClass("fa-on").addClass("fa-off");
 			$this.next().empty().html("不显示检查结果");
 			$this.attr("value","true");
 		}
	});
 	//保存质量检查      偏好设置设置
    $("#sonarSave").click(function(){
		var enabled = $("#sonarYesOrNoBtn").attr("value");
		var hidden = $("#showSonarYesOrNoBtn").attr("value");
		var mandatory = $(".sonarCheck:checked").attr("value");
		var threshold = "";
		var breakable = "";
		if ($("#sonarAsyn").find("input").prop("checked")) {
			threshold = 6;
			breakable = false;
		} else {
			threshold = $("#sonarThreshold").val();
			breakable = true;
		}
		var token = $("#tokenValue").val();
		var url = $("#urlValue").val();
		$.ajax({
			type : "POST",
			url : ctx + "/user/updateSonarConfig.do",
			data : {
				"enabled" : enabled,
				"hidden" : hidden,
				"mandatory" : mandatory,
				"threshold" : threshold,
				"breakable" : breakable,
				"token" : token,
				"url" : url
			},
			success : function(data) {
				var data = eval("(" + data + ")");
				if (data.status == "200") {
					layer.alert("修改成功");
					return;
				} else if (data.status == "500"){
					layer.alert("没有shera环境，无法保存");
					return;
				} else {
					layer.alert("修改失败");
					return;
				}
			}
		});
    });

    var SonarConfigVal = $("#SonarConfigVal").val();
    if(SonarConfigVal == "true"){
    	$(".sonarTwoStep").removeClass("hide");
    }else{
    	$(".sonarTwoStep").addClass("hide");
    }

    var threshold = $("#thresholdVal").val();
    if(threshold != 6){
    	$(".sonarInfo").removeClass("hide");
    }else{
    	$(".sonarInfo").addClass("hide");
    }
    $("#sonarThreshold").val(threshold);


});/*ready*/
$(function () { $('#collapseTwo').collapse('show')});
$(function () { $('#collapseOne').collapse('show')});
$(function () { $('#collapseThree').collapse('show')});

function delOneWhiteList(obj){
	$(obj).parent().remove();
}
function sonarPrograssCheck(){
	if($("#sonarSynChecked").is(":checked")){
		$(".sonarInfo").removeClass("hide");
	}else{
		$(".sonarInfo").addClass("hide");
	}
}


