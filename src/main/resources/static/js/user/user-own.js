/**
 * Created by cuidong on 15-12-24.
 */


$(function(){

    var p=window.location.href;
    var reg = new RegExp("b", "");
    if (reg.test(p)){
            $("#pwd").addClass("active");
            $("#pwd_wrap").removeClass("hide");

            $("#baseinfo").removeClass("active");
            $("#baseinfo_wrap").addClass("hide");
        }











    $(".Record").click(function(){

        $(".Record").removeClass("active");
        $(this).addClass("active");

        $(".tab_wrap").addClass("hide");
        $("#"+$(this).attr("id")+"_wrap").removeClass("hide");


    });

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
    	$.ajax({
			url:"/user/userModifyBasic.do?id="+id
										+"&company="+company
										+"&user_department="+user_department
										+"&user_employee_id="+user_employee_id
										+"&user_cellphone="+user_cellphone
										+"&user_phone="+user_phone
										+"&email="+email ,
			success:function(data){
				data = eval("(" + data + ")");
				if(data.status === '200'){
					alert("信息修改成功");
				}else{
					alert("信息修改失败");
				}
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
    	$.ajax({
			url:"/user/userModifyPsw.do?id="+id+"&password="+pwd+"&newpwd="+newpwd,
			success:function(data){
				data = eval("(" + data + ")");
				if(data.status === '200'){
					alert("密码修改成功");
				}else{
					alert("密码修改失败");
				}
			}
    	});
    });
    
   function checkpwd (){
	   
	   var pwd = $.trim($("#originalPwd").val());
	   var newpwd = $.trim($("#newPwd").val());
	   var confirmpwd = $.trim($("#confirmNewPwd").val());
	   
	   if(pwd === ''){
		   alert("请输入原密码");
		   return false;
	   }
	   if(newpwd !== confirmpwd){
		   alert("新密码与确认密码不一致，请重新输入");
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

    var data = [
        {
            value: 30,
            color:"#F7464A"
        },
        {
            value : 50,
            color : "#E2EAE9"
        },
        {
            value : 100,
            color : "#D4CCC5"
        },
        {
            value : 40,
            color : "#949FB1"
        },
        {
            value : 120,
            color : "#4D5360"
        }

    ];
//    var piectx = $(".pieChart").getContext("2d");
//    new Chart(piectx).Doughnut(data);
//
//    //容器运行状态的环状图下图注
//    var stoppedctx = $("#redPie").getContext("2d");
//    stoppedctx.fillStyle = "#F7464A";
//    stoppedctx.fillRect(0,0,15,15);
//    var runningctx = $("#greenPie").getContext("2d");
//    runningctx.fillStyle = "green";
//    runningctx.fillRect(0,0,15,15);
//    var grostctx = document.getElementById("grayPie").getContext("2d");
//    grostctx.fillStyle = "#D4CCC5";
//    grostctx.fillRect(0,0,15,15);

});




/*
var runningPie = 0;
var stoppedPie = 0;
var ghostPie = 0;
data.forEach(function(value){
    if (value.status == 1){
        $scope.containers.push(value);
        $scope.changePage("init");
        runningPie += 1
    }else if (value.status == 2){
        stoppedPie += 1
    }else{
        ghostPie += 1
    }
});
var piedata  = [
    {
        value: runningPie,
        color: "green"
    },
    {
        value : stoppedPie,
        color : "#F7464A"
    },
    {
        value : ghostPie,
        color : "#D4CCC5"
    }
];
var piectx = document.getElementById("pieChart").getContext("2d");
new Chart(piectx).Doughnut(piedata);*/
