<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>创建用户</title>
    <%@include file="../frame/header.jsp" %> 
    <link rel="stylesheet" type="text/css" href="/css/mod/user.css"/>
    <script type="text/javascript" src="/js/user/user_create.js"></script>
</head>
<body>

<jsp:include page="../frame/menu.jsp" flush="true">
    <jsp:param name="user" value=""/>
</jsp:include>

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" id="nav2">创建用户</li>
                </ol>
            </div>
            <div class="contentMain">


                <div class="modalCrealApp">
                        <div class="steps-main">
                            <div class="progre">
                                <ul style="padding: 0 4rem;">
                                    <li class="radius_step action" style="width:100%">基本信息</li>
                                </ul>
                            </div>
		                    <form id="add_tenement" name="add_tenement" action="" method="post" >
                           		<div class="step-inner" style="left: 0%;">

                                <%--基本信息--%>
                                    <div class="host_step1" >
                                        <div class="blankapp" style="text-align: center">
                                            <div class="row">
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;line-height: 35px" >登录账号:</label>
                                                    <label style="width: 2%; float: left;"><font color="red">*</font></label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="userName" name="userName">
                                                </div>
                                               	<div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;line-height: 35px" >姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</label>
                                                    <label style="width: 2%; float: left;"><font color="red">*</font></label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="user_realname" name="user_realname">
                                                </div>
                                               	<div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;line-height: 35px" >权限选择:</label>
                                                    <label style="width: 2%; float: left;"><font color="red">*</font></label>
                                                    <select class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    			id="user_autority" name="user_autority">
                                                        <option name="user_autority" value="3">普通用户</option>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="row" style="margin-top: 15px">
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;line-height: 35px" >省&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;份:</label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;"
                                                           id="province" name="province">
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;line-height: 35px" >公&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;司:</label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="company" name="company">
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;line-height: 35px" >所属部门:</label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="user_department" name="user_department">
                                                </div>
                                            </div>
                                            <div class="row" style="margin-top: 15px">
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;line-height: 35px" >工&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号:</label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;"
                                                           id="user_employee_id" name="user_employee_id">
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;line-height: 35px" >手机号码:</label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="user_cellphone" name="user_cellphone">
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;line-height: 35px" >固定电话:</label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="user_phone" name="user_phone">
                                                </div>
                                            </div>

                                            <div class="row" style="margin-top: 15px">
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;line-height: 35px" >电子邮箱:</label>
                                                    <label style="width: 2%; float: left ;"><font color="red">*</font></label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;"
                                                           id="email" name="email">
                                                </div>
                                                 <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;line-height: 35px" >登陆密码:</label>
                                                    <label style="width: 2%; float: left;"><font color="red">*</font></label>
                                                    <input type="password" class="form-control" style="width: 75%;display: inline; float: right;"
                                                    		id="pwd" name="password">
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;line-height: 35px" >确认密码:</label>
                                                    <label style="width: 2%; float: left;"><font color="red">*</font></label>
                                                    <input type="password" class="form-control" style="width: 75%;display: inline; float: right;"
                                                    		id="confirm_pwd">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="list-item-description" style="padding-top: 100px;">
                                            <a href="/user/manage/list"><span class="btn btn-default go_user" style="margin-right: 30px;">返回</span></a>
                                            <a href="/user/manage/list"><span class="userSave pull-right btn btn-primary pull_confirm" data-attr="tenxcloud/mysql" id="user_create_save">保存</span></a>
                                        </div>
                                    </div>


                            </div>
                            </form>

                        </div>

                </div>

            </div>
        </div>
    </article>
</div>
<%--<script type="text/javascript" >
	$(document).ready(function(){
		$("#userName").blur(function(){
			var username = $.trim($("#userName").val());
				var un = username.toLowerCase();
				console.info(un);
				$("#userName").val(un);
				//console.info("name: "+username);
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
//                            $("#userName").focus();
//							layer.alert("登陆帐号已经被使用，请输入新的帐号！");

					 	}
						if(data.status=="300"){
							layer.alert("k8s已经建立此名称的namespace，请输入新的帐号！");
                            closeBtn(0);
							$("#userName").focus();
					 	}
					});

		});
	});
</script>--%>
</body>
</html>
