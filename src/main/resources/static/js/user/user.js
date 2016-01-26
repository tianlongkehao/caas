$(function(){

	for(var j=0; j<document.getElementsByClassName("userTr").length; j++){
		var user_autority_val = document.getElementById("user_autority_hidden").value;
		var autority_options = document.getElementById("user_autority").options;
		for(var i = 0; i < autority_options.length; i++){
			if (autority_options[i].value == user_autority_val){
				document.getElementsByName("user.user_autority")[j].textContent = autority_options[i].textContent;
				break;
			}
		}

		var user_province_val = document.getElementById("user_province_hidden").value;
		var province_options = document.getElementById("user_province").options;
		for(var i = 0; i < province_options.length; i++){
			if (province_options[i].value == user_province_val){
				document.getElementsByName("user.user_province")[j].textContent = province_options[i].textContent;
				break;
			}
		}
	}

    $(".Record").click(function(){

        $(".Record").removeClass("active");
        $(this).addClass("active");

        $(".tab_wrap").addClass("hide");
        $("#"+$(this).attr("id")+"_wrap").removeClass("hide");

    });
    
    $('#userSave').click(function(){
    	var username = $('#userName').val().trim();
        var email = $('#email').val().trim();
        var company = $('#company').val().trim();
        var password = $('#password').val();
        var confirmNewPwd = $('#confirmNewPwd').val().trim();
        //alert(username);
        if (username === '' || password === '') {
            layer.msg('用户名或密码不能为空', {icon: 5});
            return;
          }
          if (password.length < 6 || username.length < 5 || username.length > 18) {
            layer.msg('用户名或密码过短', {icon: 5});
            return;
          }
          if (email.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) !== -1) {
            if (email.length > 50) {
             layer.tips('邮箱长度不能大于50', $('#email'),{tips: [1, '#EF6578']});
              $('#email').focus();
              return;
            }
          } else {
            if (email === '') {
              layer.tips('邮箱不能为空', $('#email'),{tips: [1, '#EF6578']});
              $('#email').focus();
              return;
            } else {
              layer.tips('请输入合法的邮箱', $('#email'),{tips: [1, '#EF6578']});
              $('#email').focus();
              return;
            }
          }
          if (checkStrong(password) < 2) {
              layer.tips('密码过于简单，密码必须是数字、字母、特殊字符两种及以上的组合',$('#password'),{tips: [1, '#EF6578']});
              $('#password').focus();
              return;
            }
            if(password !== confirmNewPwd){
              layer.tips('两次密码不一致',$('#confirmNewPwd'),{tips: [1, '#EF6578']});
              $('#confirmNewPwd').focus();
              return;
            }

    	layer.open({
			 type:1,
			 title: '用户信息',
			 content:'确定新增用户信息？',
			 btn: ['确定', '取消'],
			 yes: function(index, layero){ //或者使用btn1
			 	//按钮【按钮一】的回调
				 $('#addUser').submit();
		 	 },
			 cancel: function(index){ //或者使用btn2
			 	//按钮【按钮二】的回调
		 	 }
		 });
    	
    })

    $('#passwordInfo').click(function(){
    	var id = $('.account_table').attr('userID');
    	var password = $('#originalPwd').val();
    	var newPwd  = $('#newPwd').val();
    	var confirmNewPwd= $('#confirmNewPwd').val();
    	
    	if (checkStrong(newPwd) < 2) {
            layer.tips('密码过于简单，密码必须是数字、字母、特殊字符两种及以上的组合',$('#password'),{tips: [1, '#EF6578']});
            $('#password').focus();
            return;
          }
          if(newPwd !== confirmNewPwd){
            layer.tips('两次密码不一致',$('#confirmNewPwd'),{tips: [1, '#EF6578']});
            $('#confirmNewPwd').focus();
            return;
          }
    	layer.open({
			 type:1,
			 title: '密码修改',
			 content:'确定修改用户密码？',
			 btn: ['确定', '取消'],
			 yes: function(index, layero){ //或者使用btn1
			 	//按钮【按钮一】的回调
				 layer.close(index);
				 $.ajax({
						url:ctx+"../userModifyPsw.do?id="+id+"&password="+password+"&newPwd="+newPwd,
						success:function(data){
							data = eval("(" + data + ")");
							if(data.status=="200"){
								alert("密码保存成功");
							}else{
								alert("密码保存失败，请检查服务器连接");
							}

						}
					})
		 	 },
			 cancel: function(index){ //或者使用btn2
			 	//按钮【按钮二】的回调
		 	 }
		 });
    })

    $("#basicInfo").click(function(){
    	var id = $('.account_table').attr('userID');
    	var email = $('#email').val();
    	var company = $('#company').val();
    	 if (email.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) !== -1) {
             if (email.length > 50) {
              layer.tips('邮箱长度不能大于50', $('#email'),{tips: [1, '#EF6578']});
               $('#email').focus();
               return;
             }
           } else {
             if (email === '') {
               layer.tips('邮箱不能为空', $('#email'),{tips: [1, '#EF6578']});
               $('#email').focus();
               return;
             } else {
               layer.tips('请输入合法的邮箱', $('#email'),{tips: [1, '#EF6578']});
               $('#email').focus();
               return;
             }
           }
    	layer.open({
			 type:1,
			 title: '用户信息修改',
			 content:'确定修改用户信息？',
			 btn: ['确定', '取消'],
			 yes: function(index, layero){ //或者使用btn1
			 	//按钮【按钮一】的回调
				 layer.close(index);
				 $.ajax({
						url:ctx+"../userModifyBasic.do?id="+id+"&email="+email+"&company="+company,
						success:function(data){
							data = eval("(" + data + ")");
							if(data.status=="200"){
								alert("用户信息保存成功");
							}else{
								alert("用户信息保存失败，请检查服务器连接");
							}

						}
					})
		 	 },
			 cancel: function(index){ //或者使用btn2
			 	//按钮【按钮二】的回调
		 	 }
		 });
    });
    
    // 用户名验证
    $('#userName').blur(function (event) {
      var username = $('#userName').val();
      if (!username || username.trim() === '') {
        layer.tips('用户名不能为空',$(this),{tips: [1, '#EF6578']});
        return;
      }
      username = username.trim();
      if (username.search(/^[a-zA-Z0-9][a-zA-Z0-9_]*$/) !== -1) {
        if (username.length < 5 || username.length > 18) {
          layer.tips('用户名长度为5~18位',$(this),{tips: [1, '#EF6578']});
        }
        return;
      }
      layer.tips('用户名只能由字母、数字以及下划线组成，且首字母不能为下划线',$(this),{tips: [1, '#EF6578']});
    });

});

/**
 * 删除选中租户
 */
function delTenement (){
	var id = "";
	$(":checked[name='ids']").each(function(){
		id = id + jQuery(this).val() + ",";
	});
	if ("" == id) {
		alert("请选择至少一个用户");
		return;
	}
	else {
		id = id.substring(0, id.length - 1);
		layer.open({
			 title: '删除用户',
			 content:'确定删除多个用户吗？',
			 btn: ['确定', '取消'],
			 yes: function(index, layero){ //或者使用btn1
				 layer.close(index);
				 $.ajax({
						url:ctx+"/user/delMul.do?ids="+id,
						success:function(data){
							data = eval("(" + data + ")");
							if(data.status=="200"){
								layer.alert("用户信息删除成功");
							}else{
								layer.alert("用户信息删除失败，请检查服务器连接");
							}
							//location.href = ctx+"redirect:/user/list";
							location.reload(true);
						}
				 })
				 
		 	 },
			 cancel: function(index){ //或者使用btn2
			 	//按钮【按钮二】的回调
		 	 }
		 });
	}
}


//function tenement_detail() {
//	var id = "";
//	var count = 0;
//	$(":checked[name='ids']").each(function(){
//		id = jQuery(this).val();
//		count = count + 1;
//	});
//	if ("" == id) {
//		alert("请选择一个用户");
//		return;
//	}
//	if(count > 1){
//		alert("只能选择一个用户");
//		return;
//	}
//	location.href = ctx+"user/detail/"+id;
//}


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
//////////////////////////////////////////////////////////////////////
//检测密码强度 end
