<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>租户信息</title>
    <%@include file="../frame/header.jsp" %> 
    <link rel="stylesheet" type="text/css" href="/css/mod/user.css"/>
    <script type="text/javascript" src="/js/user/user_detail.js"></script>
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
                    <li class="active" id="nav2">租户详情</li>
                </ol>
            </div>
            <div class="contentMain">
                <div class="modalCrealApp">
                        <div class="steps-main">
                            <div class="progre">
                                <ul style="padding: 0 4rem;">
                                    <li class="radius_step action" style="width: 100%"> 基本信息</li>
                                </ul>
                            </div>
                            <div class="step-inner" style="left: 0%;">

                                <%--基本信息--%>
			                    <form id="update_tenement" name="update_tenement" action="" method="post" >
                                    <div class="host_step1" >
                                        <div class="blankapp" style=" text-align: center">
                                        
                                            <div class="row">
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left ;" >登录账号:</label>
                                                    <label style="width: 2%; float: left ;"><font color="red">*</font></label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="userName" name="userName" value="${user.userName }" readonly="readonly">
													<input type="hidden" id="user_id" name="id" value="${user.id}">                                                   
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left ;" >姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</label>
                                                    <label style="width: 2%; float: left ;"><font color="red">*</font></label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="user_realname" name="user_realname" value="${user.user_realname }" readonly="readonly">
													                                                    
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left ;" >权&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;限:</label>
                                                    <label style="width: 2%; float: left ;"><font color="red">*</font></label>
                                                    <input type="hidden" id="user_autority_hidden" value="${user.user_autority}">
                                                    <select class="form-control" style="width: 75%;display: inline; float: right;"
                                                    			name="user_autority" id="user_autority" disabled>
                                                        <option name="option" value="2" >租户</option>
                                                        <option name="option" value="1" >管理员</option>
                                                        <option name="option" value="3" >普通用户</option>
                                                    </select>
                                                </div>
                                       
                                            </div>

                                            <div class="row" style="margin-top: 15px">
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left ;" >省&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;份:</label>
                                                    <label style="width: 2%; float: left ;"><font color="red">*</font></label>
                                                    <input type="hidden" id="user_province_hidden" value="${user.user_province}">
                                                    <select class="form-control" style="width: 75%;display: inline; float: right;"
                                                            name="province" id="user_province" disabled>
                                                        <option name="user_province" value="1">北京</option>
                                                        <option name="user_province" value="2">天津</option>
                                                        <option name="user_province" value="3">上海</option>
                                                        <option name="user_province" value="4">河北</option>
                                                        <option name="user_province" value="5">河南</option>
                                                        <option name="user_province" value="6">山西</option>
                                                        <option name="user_province" value="7">内蒙古</option>
                                                        <option name="user_province" value="8">辽宁</option>
                                                        <option name="user_province" value="9">吉林</option>
                                                        <option name="user_province" value="10">黑龙江</option>
                                                        <option name="user_province" value="11">江苏</option>
                                                        <option name="user_province" value="12">浙江</option>
                                                        <option name="user_province" value="13">安徽</option>
                                                        <option name="user_province" value="14">福建</option>
                                                        <option name="user_province" value="15">江西</option>
                                                        <option name="user_province" value="16">山东</option>
                                                        <option name="user_province" value="17">湖南</option>
                                                        <option name="user_province" value="18">湖北</option>
                                                        <option name="user_province" value="19">广东</option>
                                                        <option name="user_province" value="20">广西</option>
                                                        <option name="user_province" value="21">海南</option>
                                                        <option name="user_province" value="22">重庆</option>
                                                        <option name="user_province" value="23">四川</option>
                                                        <option name="user_province" value="24">贵州</option>
                                                        <option name="user_province" value="25">云南</option>
                                                        <option name="user_province" value="26">西藏</option>
                                                        <option name="user_province" value="27">陕西</option>
                                                        <option name="user_province" value="28">甘肃</option>
                                                        <option name="user_province" value="29">青海</option>
                                                        <option name="user_province" value="30">宁夏</option>
                                                        <option name="user_province" value="31">新疆</option>
                                                    </select>
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left ;" >公&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;司:</label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="company" name="company" value="${user.company }">
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;" >所属部门:</label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="user_department" name="user_department" value="${user.user_department }">
                                                </div>
                                            </div>
                                            <div class="row" style="margin-top: 15px">
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;" >工&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号:</label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;"
                                                           id="user_employee_id" name="user_employee_id" value="${user.user_employee_id }">
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;" >手机号码:</label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="user_cellphone" name="user_cellphone" value="${user.user_cellphone }">
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;" >固定电话:</label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="user_phone" name="user_phone" value="${user.user_phone }">
                                                </div>
                                            </div>
                                            <div class="row" style="margin-top: 15px">
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;" >电子邮箱:</label>
                                                    <label style="width: 2%; float: left ;"><font color="red">*</font></label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;"
                                                           id="email" name="email" value="${user.email }">
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 20%; float: left;" >密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:</label>
                                                    <label style="width: 2%; float: left ;"><font color="red">*</font></label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="pwd" name="password"  value="${user.password}" readonly="readonly">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="list-item-description" style="padding-top: 100px">
                                            <a href="/user/list"><span class="btn btn-default go_user" style="margin-right: 30px;">返回</span></a>
                                            <span class="next2 pull-right btn btn-primary pull_confirm" data-attr="tenxcloud/mysql" id="user_create_next2">修改</span>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                </div>
			</div>
		</div>
	</article>
</div>	

</body>
</html>
