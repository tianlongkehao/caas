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
            $("#baseinfo").removeClass("active");
            $("#baseinfo_wrap").addClass("hide");
        }

    $(".Record").click(function(){

        $(".Record").removeClass("active");
        $(this).addClass("active");

        $(".tab_wrap").addClass("hide");
        $("#"+$(this).attr("id")+"_wrap").removeClass("hide");


    });

    var userCpuPer = $("#detailCpu")[0].textContent/$("#totalCpu")[0].textContent*100+"%";
    $("#usedCpu")[0].style.width = userCpuPer;
    var userMemPer = $("#detailMemory")[0].textContent/$("#totalMemory")[0].textContent*100+"%";
    $("#usedMemory")[0].style.width = userMemPer;
    var userVolPer = $("#detailVolume")[0].textContent/$("#totalVolume")[0].textContent*100+"%";
    $("#usedVolume")[0].style.width = userVolPer;

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

});


