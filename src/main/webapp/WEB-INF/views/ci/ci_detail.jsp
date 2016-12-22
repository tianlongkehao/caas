<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>构建信息</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/ci.css"/>
    <script type="text/javascript" src="<%=path %>/js/ci/ci_detail.js"></script>
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
                    <li class="active" id="nav2">构建信息</li>
                </ol>
            </div>
            <div class="contentMain">
            <div class="ci-body">
                <div class="ci-head">
                    <span class="ci-name margin" id="projectNameSpan">${ci.projectName}</span>
                    <span class="btn btn-defaulted" style="cursor:pointer" data-toggle="tooltip" data-placement="top" imgId="${ci.imgId }" id="deploy" title="" data-original-title="构建成功后才能部署项目哦~">快速部署</span>
                    <span class="btn btn-defaulted" style="cursor:pointer" data-toggle="tooltip" data-placement="top" ciId="${ci.id }" id="replayci" title="" data-original-title="构建成功后才能部署项目哦~">重新构建</span>
                </div>
                <div class="ci-content-tabmain">
                    <div class="create-log ci-tab active">构建日志</div>
                    <div class="create-set ci-tab">基本设置</div>
                    <div class="create-other ci-tab">操作</div>
                    <%-- <span class="btn btn-primary pull-right" id="buildBtn" ciId="${ci.id }" constructionStatus="${ci.constructionStatus }">构建</span> --%>

                        <div class="code-tabmain">

                            <%-- 构建日志 --%>
                            <div class="log-details" id="ciRecordList">
                                <c:forEach items="${ciRecordList}" var="ciRecord" varStatus="status">
                                    <c:choose>
                                        <c:when test="${ciRecord.constructResult == 1}">
                                            <c:set var="statusClass" value="fa_run"></c:set>
                                            <c:set var="eventStatusClass" value="fa-check"></c:set>
                                            <c:set var="statusName" value="成功"></c:set>
                                            <c:set var="eventStatus" value="success"></c:set>
                                        </c:when>
                                        <c:when test="${ciRecord.constructResult == 2}">
                                            <c:set var="statusClass" value="fa_stop"></c:set>
                                            <c:set var="eventStatusClass" value="fa-times"></c:set>
                                            <c:set var="statusName" value="失败"></c:set>
                                            <c:set var="eventStatus" value="error"></c:set>
                                        </c:when>
                                        <c:when test="${ciRecord.constructResult == 3}">
                                            <c:set var="statusClass" value="fa_run"></c:set>
                                            <c:set var="eventStatusClass" value="fa-check"></c:set>
                                            <c:set var="statusName" value="构建中"></c:set>
                                            <c:set var="eventStatus" value=""></c:set>
                                        </c:when>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${status.index==0 }">
                                            <c:set var="liveClass" value="lives"></c:set>
                                            <c:set var="transformClass" value="90deg"></c:set>
                                            <c:set var="displayClass" value="block"></c:set>
                                        </c:when>
                                        <c:otherwise>
                                        	<c:set var="liveClass" value=""></c:set>
                                            <c:set var="transformClass" value="0deg"></c:set>
                                            <c:set var="displayClass" value="none"></c:set>
                                        </c:otherwise>
                                    </c:choose>
                                    <div class='event-line ${liveClass}' repotype='' status='${eventStatus}'>
                                        <div class='event-status ${eventStatus}'>
                                            <i class='fa ${eventStatusClass} notes'></i>
                                        </div>
                                        <div class='time-line-content'>
                                            <div class='time-line-reason event-title'>
                                                <div class='title-name ${eventStatus}'>
                                                <span class='event-names'>
                                                    ${ci.projectName}
                                                <span class='btn-version'>${ciRecord.ciVersion}</span>
                                                </span>
                                                <span class='time-on-status'>
                                                    <i class='${statusClass}'></i><span class="ciStatus" value="${statusName}">${statusName}</span>
                                                </span>
                                                </div>
                                                <div class='time-line-time'>
                                                    <div class='event-sign'><i class='fa fa-angle-right fa_caret' style='transform: rotate(${transformClass});'></i></div>
                                                    <c:if test="${ci.type == 1}">
	                                                    <c:if test="${ciRecord.constructResult == 1 || ciRecord.constructResult == 2}">
	                                                        <div class='event-del' title="删除当前执行"><span id = "deleteCodeCi"  ciRecordId = "${ciRecord.id }" projectName = "${ci.projectName }" executionId = "${ciRecord.executionId }"><i class='fa fa-trash'></i></span></div>
	                                                    </c:if>
	                                                    <c:if test="${ciRecord.constructResult == 3}">
	                                                        <div class='event-stop' title="停止构建"><span id = "stopCodeCi" projectName = "${ci.projectName }" executionId = "${ciRecord.executionId }" ><i class='fa fa-power-off'></i></span></div>
	                                                    </c:if>
                                                    </c:if>
                                                    <div class='datetimes'><i class='fa fa-calendar margin'></i><fmt:formatDate value="${ciRecord.constructDate}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
                                                    <div class='time-on-timeout'><i class='fa fa-time'></i><fmt:formatNumber type="number" value="${ciRecord.constructTime/1000}" maxFractionDigits="0"/>s</div>
                                                </div>
                                                <div class='time-line-message' style='display: ${displayClass};'>
                                                    <div class='buildForm' buildid='' containerid='' buildername='builder2' status='fail'>
                                                        <div style='clear:both;'></div>
                                                        <div class='buildStatus' style='margin:0px 0px 10px 0px'></div>
                                                        <div class='build-logs' style='max-height: 400px; overflow: auto;margin-top:10px;background-color:black;color: #37fc34'>
															<pre class='logs' style='background-color:black; overflow: hidden;color: #37fc34;border:0; float:left'>
															       <span class='printLogSpan' status='${ciRecord.constructResult}' ciRecordId='${ciRecord.id}'>${ciRecord.logPrint}</span>
															</pre>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>

                            <%-- 基本设置 --%>
                            <div class="config-details hide">
                                <c:if test="${ci.type == 1}"> 
                                	<form id="editCiForm" class="form-horizontal" method="post" action="" role="form">
                                		<section class="registryinfo">
			                        		<div class="row depot-name">
				                                <div class=" col-md-12">
				                                    <label class="c-project-tit">项目名称</label>
				                                    <input id="projectName" name="projectName" class="form-control c-project-con" type="text"
				                                           required="" value = "${ci.projectName }">
				                                </div>
				                            </div>
				                            <div class="row" style="margin-bottom: 12px;">
				                                <div class=" col-md-12">
				                                    <label class="c-project-tit">描述</label>
				                                    <textarea id="description" name="description" class="form-control c-project-con" type="text"
				                                           required="" row="5" value = "${ci.description }">${ci.description }</textarea>
				                                </div>
				                            </div>
				                            <div class="row">
				                                <div class=" col-md-12">
				                                    <label class="c-project-tit">JDK</label>
				                                    <select id="jdk-version" name="jdkVersion" class="form-control c-project-con">
                                                        <option value="${ci.jdkVersion }">${empty ci.jdkVersion  ? "system" : ci.jdkVersion}</option>
    			                                        <c:forEach items="${jdkList}" var="jdk">
					                                        <c:if test="${ci.jdkVersion != jdk.version }">
                                                                <option value="${jdk.version }">${jdk.version }</option>
	                                                        </c:if>
                                                        </c:forEach>
				                                    </select>
				                                </div>
				                            </div>
			                        	</section>
			                        	
			                        	<hr>
			                            <h4 class="c-title">代码管理</h4>
			                            <section class="registryinfo">
			                            	<div class="row">
				                            	<div class="form-group1 col-md-12">
					                                <label class="c-project-tit">代码库类型</label>
					                                <select id="codeType" name="codeType" class="form-control c-project-con" >
					                                    <c:if test="${ci.codeType == 0 }">
                                                           <option value="0">-none-</option>
                                                           <option value="1">git</option>
                                                        </c:if>
					                                	<c:if test="${ci.codeType == 1 }">
                                                           <option value="1">git</option>
                                                           <option value="0">-none-</option>
                                                        </c:if>
					                                </select>
					                            </div>
				                            </div>
				                            <div class="row git-config">
				                            	<div class="form-group1 col-md-12">
					                                <label class="c-project-tit">代码仓库地址</label>
					                                <input id="codeUrl" name="codeUrl" class="form-control c-project-con" type="text"
			                                           placeholder="例如：https://github.com/tenxcloud/php-hello-world.git" value = "${ci.codeUrl }">
					                            </div>
					                            <div class="form-group1 col-md-12">
					                            	<label class="c-project-tit">认证方式</label>
					                            	<select id="codeCredentials" name="codeCredentials" class="form-control c-project-con" style="width:50%;float:left;">
					                            	  <c:forEach items="${ciCodeList}" var="ciCode" >
					                            	      <c:if test="${ciCode.id == ci.codeCredentials }">
	                                                          <c:if test="${ciCode.type == 1 }">
	                                                              <option value="${ciCode.id }">${ciCode.userName }(HTTP)</option>
	                                                          </c:if>
	                                                          <c:if test="${ciCode.type == 2 }">
	                                                              <option value="${ciCode.id }">${ciCode.userName }(SSH)</option>
	                                                          </c:if>
					                            	      </c:if>
                                                      </c:forEach>
					                            	  <c:forEach items="${ciCodeList}" var="ciCode" >
					                            	      <c:if test="${ciCode.id != ci.codeCredentials }">
				                                              <c:if test="${ciCode.type == 1 }">
				                                                  <option value="${ciCode.id }">${ciCode.userName }(HTTP)</option>
				                                              </c:if>
				                                              <c:if test="${ciCode.type == 2 }">
				                                                  <option value="${ciCode.id }">${ciCode.userName }(SSH)</option>
				                                              </c:if>
					                            	      </c:if>
			                                          </c:forEach>
					                                </select>
					                                <c:if test="${userAutority ==1 || userAutority ==2 }">
						                                <button type="button" id="addCredentialsBtn" class="addCredentialsBtn" value="添加证书"><i class="fa fa-key"></i>&nbsp添加证书</button>
			                                        </c:if>
					                            </div>
					                            <div class="form-group1 col-md-12">
					                                <label class="c-project-tit">创建分支</label>
					                                <input id="codeBranch" name="codeBranch" type="text" class="form-control c-project-con"
			                                                   value="${ci.codeBranch }">
					                            </div>
					                            <div class="form-group1 col-md-12">
					                                <label class="c-project-tit">HookCode</label>
					                               <input type="checkbox" id="HookCode" class="c-project-checkbox" >
                                                   <input type="hidden" id = "isHookCode" name ="isHookCode" value = "${ci.isHookCode }"/>
					                            </div>
				                            </div>
				                            <div class="row git-config">
				                            	<button id="git-higher" type="button" style="float:right!important">高级...</button>
				                            </div>
				                            <div class="row git-config git-higher">
				                            	<div class="form-group1 col-md-12">
					                                <label class="c-project-tit">Name</label>
					                                <input id="codeUsername" name="codeName" type="text" class="form-control c-project-con reg-input"
			                                                   value="${ci.codeName }">
					                            </div>
					                            <div class="form-group1 col-md-12">
					                                <label class="c-project-tit">Refspec</label>
					                                <input id="codePassword" name="codeRefspec" type="text"
			                                                   class="form-control c-project-con reg-input" value="${ci.codeRefspec }">
					                            </div>
			                                </div>
			                            </section>
			                            <hr>
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
			                            <hr>
										<h4 class="c-title">镜像信息<input type="checkbox" id="imageInfo"></h4>
			                            <section class="registryinfo imageInfoCon">
				                            <div class="row">
				                                <div class="form-group1 col-md-12">
				                                    <label class="c-project-tit">镜像名称</label>
				                                    <input id="imageName" name="imgNameLast" type="text"
			                                                   class="form-control c-project-con reg-input imgInput" value="${ci.imgNameLast }">
					                            </div>
				                            </div>
				                            <div class="row">
					                            <div class="form-group1 col-md-12">
				                                    <label class="c-project-tit">镜像版本</label>
				                                    <input id="imgNameVersion" name="imgNameVersion" type="text" placeholder = "填写之后重新构建镜像不会进行迭代，而是覆盖"
			                                                   class="form-control c-project-con reg-input imgInput" value="${ci.imgNameVersion }">
					                            </div>
				                            </div>
				                            
				                            <div class="row">
			                                    <div class="form-group1 col-md-12">
			                                        <label class="c-project-tit">是否为基础镜像</label>
			                                        <input id="baseImage" name="baseImage" type="checkbox"
			                                                   style="height:26px" value="">
			                                        <input type = "hidden" id = "isBaseImage" name = "isBaseImage" value = "${ci.isBaseImage }">
			                                    </div>
			                                    <div class="form-group1 col-md-12">
			                                        <label class="c-project-tit">是否为公有镜像</label>
			                                        <input id="imageType" name="imageType" type="checkbox"
			                                                   style="height:26px" value="">
			                                        <input type = "hidden" id = "imgType" name = "imgType" value = "${ci.imgType }">
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
					                        <input type="hidden" id="ciLocation" value="${ci.dockerFileLocation}">
                                            <input type = "hidden" id = "ciMethod" value = '${dockerFileContent }'>
				                            <div id="dockerfileMethod">
	                                               
	                            			</div>
			                        	</section>
			                            <div class="form-group">
                                            <label class="col-2x control-label"></label>
                                            <div class="col-sm-10">
                                                <br><input type="button" id="editCiBtn" class="btn btn-primary pull-right" value="确认修改">
                                            </div>
                                        </div>
                                        <input type="hidden" id="id" name="id" value="${ci.id}">
                                        <input type = "hidden" id = "jsonData" name = "jsonData" value = "">
			                        </form>
                                </c:if> 

                                <c:if test="${ci.type == 2}">
                                    <form id="editCiUploadForm" class="form-horizontal" method="post" action="" role="form" enctype="multipart/form-data">
                                        <br>
                                        <div class="form-group">
                                            <label class="col-2x control-label">项目名称：</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control" id="projectName" name="projectName" value="${ci.projectName}">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-2x control-label">简介：</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control" id="description" name="description" value="${ci.description}">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-2x control-label">重新上传代码文件：</label>
                                            <div class="col-sm-9">
                                                <input type="file" class="" id="sourceCode" name="sourceCode" style="margin:6px 0;">
                                            </div>
                                        </div>
                                         <div class="form-group">
                                            <label class="col-2x control-label">重新上传Dockfile：</label>
                                            <div class="col-sm-9">
                                            <span id="docImport-btn" class=" btn-info btn-sm" style="cursor: pointer; ">导入模板</span>
                                            <textarea id="dockerFile" name = "dockerFile"
                                                style="background-color: black; color: #37fc34; border: 0; width: 100%; height: 230px; margin-top:10px">${dockerFileTxt }</textarea>   
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-2x control-label"></label>
                                            <div class="col-sm-9">
                                                <!-- <textarea class="form-control" id="ciDetail" name="ciDetail">...</textarea> -->
                                                <br><input type="button" id="editCiUploadBtn" class="btn btn-primary pull-right" value="确认修改">
                                            </div>
                                        </div>
                                        <input type="hidden" id="id" name="id" value="${ci.id}">
                                    </form>
                                </c:if>
                                <c:if test="${ci.type == 3}">
                                    <form id="editCiUploadCodeForm" class="form-horizontal" method="post" action="" role="form" enctype="multipart/form-data">
                                        <br>
                                        <div class="form-group">
                                            <label class="col-2x control-label">项目名称：</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control" id="projectName" name="projectName" value="${ci.projectName}">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-2x control-label">简介：</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control" id="description" name="description" value="${ci.description}">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-2x control-label">重新上传代码文件：</label>
                                            <div class="col-sm-9">
                                                <input type="file" class="" id="sourceCode" name="sourceCode" style="margin:6px 0;">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-2x control-label">基础镜像：</label>
                                            <div class="col-sm-9 ci-quick-baseImage">
                                                <select id="baseImageName" name="baseImageName">
                                                    <option type="text" value="${currentBaseImage.name }">${currentBaseImage.name }</option>
                                                    <c:forEach var = "image" items = "${baseImage }">
                                                        <c:if test = "${currentBaseImage.name ne image.name }">
                                                               <option type="text" value="${image.name }">${image.name }</option>
                                                        </c:if>
                                                    </c:forEach>
                                                </select> : 
                                                <select id="baseImageId" name="baseImageId">
                                                    <option type="text" id = "ownBase" value="${ci.baseImageId }">${ci.baseImageVersion }</option>
                                                </select>
                                            
                                               <%--  <input id="baseImageName" name="baseImageName" type="text" 
                                                          value="${ci.baseImageName}" style="width:218px"> :
                                                <input id="baseImageVersion" name="baseImageVersion" type="text" value="${ci.baseImageVersion}">
                                                 --%>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-2x control-label"></label>
                                            <div class="col-sm-9">
                                                <!-- <textarea class="form-control" id="ciDetail" name="ciDetail">...</textarea> -->
                                                <br><input type="button" id="editCiUploadCodeBtn" class="btn btn-primary pull-right" value="确认修改">
                                            </div>
                                        </div>
                                        <input type="hidden" id="id" name="id" value="${ci.id}">
                                    </form>
                                </c:if>
                            </div>

                            <%-- 操作 --%>
                            <div class="other-details hide">
                                <div class="col-9x">
                                    <c:if test="${ci.isHookCode == 1 }">
                                    	<a href="javascript:void(0);" id="delHookBtn" class="deletebutton btn btn-primary btn-deleteHook">删除Hook</a>
                                    </c:if>
                                    <a href="javascript:void(0);" id="delCiBtn" class="deletebutton btn btn-danger btn-deleteitem">删除项目</a>
                                    <p class="other-hint">
                                        <span class="gray-radius">
                                            <i class="fa fa-warning"></i>
                                        </span>
                                        删除项目将清除数据以及镜像且不能找回，慎重选择哦
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                 
                <!--dockerfile导入模板 -->
                <div id="dockerfile-import" style="display:none;max-height:170px;overflow-y:scroll;overflow-x:hidden;">
                    <table class="table table-hover enabled" id="Path-table-doc"
                        style="width: 326px; margin: 5px 10px 5px 10px">
                        <tbody id="dockerfile-body">
                           
                        </tbody>
                    </table>
                </div>
                <%-- <div id="dockerfile-import" style="display:none; max-height:170px;overflow-y:scroll;overflow-x:hidden;">
                    <table class="table table-hover enabled" id="Path-table-doc"
                        style="width: 326px; margin: 5px 10px 5px 10px">
                        <tbody id="dockerfile-body">
                            <c:if test="${empty dockerFiles }">
                                <tr>
                                    <td>没有保存的模板</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty dockerFiles }">
                                <c:forEach var = "dockerFile" items = "${dockerFiles }">
                                    <tr>
                                        <td class="vals vals-doc">${dockerFile.templateName }
                                          <span class="doc-tr hide"><i class="fa fa-check"></i></span>
                                           <input type="hidden" class="dockerFileTemplate" value='${dockerFile.dockerFile }' />
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </tbody>
                    </table>
                </div> --%>
                <!-- dockerfile另存为模板 -->
                <div id="dockerfile-export" style="display:none;">
                    <div style="width: 345px; margin: 5px 10px 5px 10px">
                        <span>模板名称：</span><input type="text" id="dockerFileTemplateName"
                            style="width: 77%" autofocus="autofocus" />
                    </div>
                </div>
				<!-- 添加证书 -->
                <div id="addCredentialsCon">
                    <div style="width: 345px; margin: 5px 10px 5px 10px">
                        <div class="infoCred">
                        	<span class="labelCred">类型：</span>
	                        <select class="form-control conCred" id="CredentialsType" name = "type">
	                        	<option value="1">用户名和密码</option>
	                        	<option value="2">SSH用户名和密钥</option>
	                        </select></div>
                        <div class="infoCred">
	                        <span class="labelCred">用户名：</span>
	                        <input type="text" class="form-control conCred" id="userNameCred" name="userName" value=""></div>
                        <div class="infoCred normal">
	                        <span class="labelCred">密码：</span>
	                        <input type="password" class="form-control conCred" id="passwordCred" name="password" value=""></div>
                        <div class="infoCred ssh">
	                        <span class="labelCred">密钥：</span>
	                        <textarea type="text" class="form-control conCred" id="SSHpasswordCred" name="privateKey" row="5" value="" ></textarea></div>
                    </div>
                </div>
            </div>
        </div>
    </article>
</div>
</body>
</html>