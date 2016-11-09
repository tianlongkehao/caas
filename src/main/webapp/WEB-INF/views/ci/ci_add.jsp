<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>代码构建</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/ci.css"/>
    <script type="text/javascript" src="<%=path %>/js/ci/ci_add.js"></script>
    
</head>
<body>

<jsp:include page="../frame/menu.jsp" flush="true">
    <jsp:param name="ci" value=""/>
</jsp:include>

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                            id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" id="nav2">代码构建</li>
                </ol>
            </div>
            <div class="contentMain">
                <div class="item-obj">
                    <div class="container">
                        <h4>代码构建</h4>

                        <form id="buildForm" name="buildForm" action="<%=path %>/ci/addCi.do" method="post">
                        	<section class="registryinfo">
                        		<div class="row depot-name">
	                                <div class=" col-md-12">
	                                    <label class="c-project-tit">项目名称</label>
	
	                                    <input id="description" name="description" class="form-control c-project-con" type="text"
	                                           required="">
	                                </div>
	                               
	                            </div>
	                            <div class="row">
	                                <div class="form-group col-md-12">
	                                    <label class="c-project-tit">描述</label>
	                                    <textarea id="description" name="description" class="form-control c-project-con" type="text"
	                                           required="" row="5"></textarea>
	                                    
	                                </div>
	                            </div>
	                            <div class="row">
	                                <div class="form-group col-md-12">
	                                    <label class="c-project-tit">JDK</label>
	                                    <select id="jdk-version" name="jdk-version" class="form-control c-project-con">
	                                    	<option value="jdk1.7">jdk1.7</option>
	                                    	<option value="jdk1.8">jdk1.8</option>
	                                    </select>
	                                </div>
	                            </div>
                        	</section>
                        	
                            <h4 class="c-title">代码管理</h4>
                            <section class="registryinfo">
                            	<div class="row">
	                            	<div class="form-group col-md-12">
		                                <label class="c-project-tit">代码库类型</label>
		                                <select id="codeType" name="codeType" class="form-control c-project-con" >
		                                	<option value="0">-none-</option>
		                                    <option value="1">git</option>
		                                </select>
		                            </div>
	                            </div>
	                            <div class="row git-config">
	                            	<div class="form-group col-md-12">
		                                <label class="c-project-tit">代码仓库地址</label>
		                                <input id="codeUrl" name="codeUrl" class="form-control c-project-con" type="text"
                                           placeholder="例如：https://github.com/tenxcloud/php-hello-world.git">
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label class="c-project-tit">认证方式</label>
		                                <select id="codeType" name="codeType" class="form-control c-project-con">
		                                    <option value="0">-none-</option>
		                                    <option value="1">gitlab(SSH)(gitlab SSH方式认证)</option>
	                                      	<option value="2">root/**********(gitlab HTTPS方式认证)</option>
		                                </select>
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label class="c-project-tit">创建分支</label>
		                                <input id="buildBranch" name="buildBranch" type="text" class="form-control c-project-con"
                                                   value="*/master">
		                            </div>
	                            </div>
	                            <div class="row git-config">
	                            	<button id="git-higher" type="button" style="float:right!important">高级...</button>
	                            </div>
	                            <div class="row git-config git-higher">
	                            	<div class="form-group col-md-12">
		                                <label class="c-project-tit">代码库用户名</label>
		                                <input id="codeUsername" name="codeUsername" type="text" class="form-control c-project-con reg-input"
                                                   value="">
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label class="c-project-tit">代码库密码</label>
		                                <input id="codePassword" name="codePassword" type="password"
                                                   class="form-control c-project-con reg-input" value="">
		                            </div>
                                </div>
                               
                            </section>
                            <h4 class="c-title">构建</h4>
                            <section class="registryinfo">
                            	<div class="row">
	                            	<div class="form-group col-md-12">
		                                <label class="c-project-tit">增加构建步骤</label>
		                                <select id="addciStep" name="addciStep" class="form-control c-project-con" >
		                                	<option value="0">-none-</option>
		                                    <option value="1">maven</option>
		                                    <option value="2">ant</option>
		                                </select>
		                            </div>
	                            </div>
	                            <div class="row maven">
	                            	<div class="form-group col-md-12">
		                                <label class="c-project-tit">maven版本</label>
		                                <select id="mavenVis" name="mavenVis" class="form-control c-project-con" >
		                                	<option value="0">default</option>
		                                    <option value="1">maven1111</option>
		                                </select>
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label class="c-project-tit">目标</label>
		                                <input id="Goals" name="Goals" type="text" class="form-control c-project-con"
                                                   value="">
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label class="c-project-tit">POM</label>
		                                <input id="pom" name="pom" type="text" class="form-control c-project-con"
                                                   value="">
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label class="c-project-tit">属性</label>
		                                <input id="Properties" name="Properties" type="text" class="form-control c-project-con"
                                                   value="">
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label class="c-project-tit">JVM选项</label>
		                                <input id="JVMOptions" name="JVMOptions" type="text" class="form-control c-project-con"
                                                   value="">
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label style="margin-left:10px">使用私人maven存储库</label>
		                                <input type="checkbox">
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label class="c-project-tit">配置文件</label>
		                                <select id="Settings-file" name="Settings-file" class="form-control c-project-con" >
		                                	<option value="0">use default maven setting</option>
		                                    <option value="1">settings file in filesystem</option>
		                                </select>
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label class="c-project-tit">全局配置文件</label>
		                                <select id="GlobalSettingsfile" name="GlobalSettingsfile" class="form-control c-project-con" >
		                                	<option value="0">use default maven global setting</option>
		                                    <option value="1">global settings file in filesystem</option>
		                                </select>
		                            </div>
	                            </div>
	                            <div class="row ant ant-config">
	                            	<div class="form-group col-md-12">
		                                <label class="c-project-tit">ant版本</label>
		                                <select id="antVis" name="antVis" class="form-control c-project-con" >
		                                	<option value="0">default</option>
		                                    <option value="1">ant1111</option>
		                                </select>
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label class="c-project-tit">目标</label>
		                                <input id="Targets" name="Targets" type="text" class="form-control c-project-con"
                                                   value="">
		                            </div>
	                            </div>
	                            <div class="row ant ant-config ant-higher">
	                            	<button id="ant-higher" type="button" style="float:right!important">高级...</button>
	                            </div>
	                            <div class="row ant ant-higher">
	                            	<div class="form-group col-md-12">
		                                <label class="c-project-tit">构建文件</label>
		                                <input id="antBuildFile" name="antBuildFile" type="text" class="form-control c-project-con"
                                                   value="">
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label class="c-project-tit">性能</label>
		                                <input id="antProperties" name="antProperties" type="text" class="form-control c-project-con"
                                                   value="">
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label class="c-project-tit">java选项</label>
		                                <input id="antJavaOptions" name="antJavaOptions" type="text" class="form-control c-project-con"
                                                   value="">
		                            </div>
	                            </div>
	                         
                            </section>
							<h4 class="c-title">构建后操作</h4>
                            <section class="registryinfo">
                            	<div class="row">
	                            	<div class="form-group col-md-12">
		                                <label class="c-project-tit" title="增加构建后操作步骤">增加构建后操作步骤</label>
		                                <select id="codeType" name="codeType" class="form-control c-project-con" >
		                                	<option value="0">-none-</option>
		                                    
		                                </select>
		                            </div>
	                            </div>
	                        </section>
                            
                            <br>

                            <div class="pull-right">
                                <span id="buildBtn" class="btn btn-primary">创建</span>
                            </div>
                        </form>
                    </div>
                </div>

            </div>
        </div>
    </article>
</div>
<script type="text/javascript">
	$('.dataTables-example').dataTable({
        "aoColumnDefs": [ { "bSortable": false, "aTargets": [0,1,3,4,5, 6] }],
        "aaSorting": [[ 2, "desc" ]]
	});
	$("#checkallbox").parent().removeClass("sorting_asc");
</script>
</body>
</html>