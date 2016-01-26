$(document).ready(function(){

    var user_province_val = document.getElementById("user_province_hidden").value;
    var province_options = document.getElementById("user_province").options;
    for(var i = 0; i < province_options.length; i++){
        if (province_options[i].value == user_province_val){
            province_options.selectedIndex = i;
            break;
        }
    }

    $("#saveInfo_btn").click(function(){

        var flag = checkBasicInfo();
        if(flag == false){
            return;
        }

        layer.open({
            title: '用户信息',
            content: '确定创建新用户吗？',
            btn: ['确定', '取消'],
            yes: function(index, layero){ //或者使用btn1
                //按钮【按钮一】的回调

                layer.close(index);
                $('#add_tenement').attr("action", ctx+'/user/savemanage.do');
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
        }else if(pwd.length < 6){
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



    /*$(".chkAll").click(function(){
        $(".chkItem").prop('checked',$(".chkAll").is(":checked"));
    });

    $(".chkItem").each(function(){

        $(this).click(function(){

            if($(this).is(":checked")){
                if ($(".chkItem:checked").length == $(".chkItem").length) {
                    $(".chkAll").prop("checked", "checked");
                }
            }else{
                $(".chkAll").prop('checked', $(this).is(":checked"));
            }

        });


    });
    
    $("#userAdd").click(function(){
		$(".contentMain").load("/user/add");
	});*/


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
        $("#userName").focus();
        return false;
    } else if(username.length < 4){
        layer.tips('登陆名称过短','#userName',{
            tips: [1, '#0FA6D8']
        });
        $('#userName').focus();
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
                    $('#userName').focus();
                    return false;
                }
                if(data.status=="300"){
                    layer.tips('k8s已经建立此名称的namespace，请输入新的帐号！','#userName',{
                        tips: [1, '#0FA6D8']
                    });
                    $('#userName').focus();
                    return false;
                }
            });
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
    if (password == '') {
        layer.tips('密码不能为空', '#pwd', {
            tips: [1, '#0FA6D8'] //还可配置颜色
        });
        $('#pwd').focus();
        return false;
    } else if (password.length < 6){
        layer.tips('密码过于简单，密码必须是数字、字母、特殊字符两种及以上的组合', '#pwd', {
            tips: [1, '#0FA6D8'] //还可配置颜色
        });
        $('#pwd').focus();
        return false;
    }
    /*if (password.length < 6 || username.length < 4 || username.length > 18) {
     layer.msg('用户名或密码过短', {icon: 5});
     return false;
     }*/
    if(password !== confirmNewPwd){
        layer.tips('两次密码不一致','#confirm_pwd',{
            tips: [1, '#0FA6D8']
        });
        $('#confirm_pwd').focus();
        return false;
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