<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>代码构建</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/core/jquery-ui.min.css"/>
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

                        <form id="buildForm" name="buildForm" action="<%=path %>/ci/addCodeCi.do" method="post">
                        	<section class="registryinfo">
                        		<div class="row depot-name">
	                                <div class=" col-md-12">
	                                    <label class="c-project-tit">项目名称</label>
	                                    <input id="projectName" name="projectName" class="form-control c-project-con" type="text"
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
	                                    <select id="jdk-version" name="jdkVersion" class="form-control c-project-con">
	                                       <c:forEach items="${jdkList}" var="jdk">
	                                    	  <option value="${jdk.version }">${jdk.version }</option>
	                                       </c:forEach>
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
		                                <select id="codeCredentials" name="codeCredentials" class="form-control c-project-con">
		                                    <option value="">-none-</option>
		                                    <option value="gitlab(SSH)(gitlab SSH方式认证)">gitlab(SSH)(gitlab SSH方式认证)</option>
	                                      	<option value="root/**********(gitlab HTTPS方式认证)">root/**********(gitlab HTTPS方式认证)</option>
		                                </select>
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label class="c-project-tit">创建分支</label>
		                                <input id="codeBranch" name="codeBranch" type="text" class="form-control c-project-con"
                                                   value="*/master">
		                            </div>
	                            </div>
	                            <div class="row git-config">
	                            	<button id="git-higher" type="button" style="float:right!important">高级...</button>
	                            </div>
	                            <div class="row git-config git-higher">
	                            	<div class="form-group col-md-12">
		                                <label class="c-project-tit">Name</label>
		                                <input id="codeUsername" name="codeName" type="text" class="form-control c-project-con reg-input"
                                                   value="">
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label class="c-project-tit">Refspec</label>
		                                <input id="codePassword" name="codeRefspec" type="text"
                                                   class="form-control c-project-con reg-input" value="">
		                            </div>
                                </div>
                            </section>
                            <h4 class="c-title">构建</h4>
                            <section class="registryinfo">
		                                <ul class="nav nav-bar">
		                                	<li class="dropdown"><a type="button" id="dropdown-btn" class="dropdown-toggle btn btn-default" data-toggle="dropdown">增加构建步骤<span class="caret"></span></a>
		                                		<ul class="dropdown-menu">
			                                		<li><a id="maven">Maven</a></li>
			                                		<li><a id="ant">Ant</a></li>
			                                		<li><a id="shell">Execute shell</a></li>
			                                	</ul>
		                                	</li>
		                                	
		                                </ul>
	                            <div id="sortable">
	                            </div>
                            </section>
                            <h4 class="c-title">镜像信息</h4>
                            <section class="registryinfo">
	                            <div class="row">
	                                <div class="form-group col-md-12">
	                                    <label class="c-project-tit">镜像名称</label>
	                                    <input id="imageName" name="imgNameLast" type="text"
                                                   class="form-control c-project-con reg-input" value="">
		                            </div>
	                            </div>
	                            <ul class="nav nav-bar">
		                           <li class="dropdown"><a type="button" id="dropdown-btn" class="dropdown-toggle btn btn-default" data-toggle="dropdown">dockerfile构建方式<span class="caret"></span></a>
		                              <ul class="dropdown-menu">
			                             <li><a id="dockerfilePath">dockerfile路径</a></li>
			                             <li><a id="dockerfileTemp">编写dockerfiel</a></li>
			                          </ul>
		                            </li>
		                        </ul>
	                            <div class="row dockerfilePath hide">
	                                <div class="form-group col-md-12">
	                                    <label class="c-project-tit">dockerfile路径</label>
	                                    <textarea id="dockerFileLocation" name="dockerFileLocation" class="form-control c-project-con" type="text"
	                                           required="" row="5"></textarea>
	                                </div>
	                            </div>
	                            <div class="row dockerfileTemp hide">
	                                <div class="form-group col-md-12">
										<label class="c-project-tit" style="line-height:20px">编写dockerfile</label> 
										<span id="docImportBtn" class=" btn-info btn-sm" style="cursor: pointer">导入模板</span>
										<span id="docExportBtn" class=" btn-info btn-sm" style="cursor: pointer">另存为模板</span>
									</div>

									<div class="form-group col-md-12" id="dockerFiles" style="width:95%;margin-left:30px">
										<textarea id="dockerFile" name="dockerFileContent"></textarea>
									</div>
	                            </div>
                        	</section>
                            
                        	<input type = "hidden" id = "jsonData" name = "jsonData" value = "">
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
				<!--dockerfile导入模板 -->
                <div id="dockerfile-import" style="max-height:170px;overflow-y:scroll;overflow-x:hidden;">
                    <table class="table table-hover enabled" id="Path-table-doc"
                        style="width: 326px; margin: 5px 10px 5px 10px">
                        <tbody id="dockerfile-body">
                           
                        </tbody>
                    </table>
                </div>
                <!-- dockerfile另存为模板 -->
                <div id="dockerfile-export">
                    <div style="width: 345px; margin: 5px 10px 5px 10px">
                        <span>模板名称：</span><input type="text" id="dockerFileTemplateName"
                            style="width: 77%" autofocus="autofocus" />
                    </div>
                </div>
</body>
</html>