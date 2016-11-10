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
                    <span class="btn btn-defaulted" style="cursor:auto" data-toggle="tooltip" data-placement="top" imgId="${ci.imgId }" id="deploy" title="" data-original-title="构建成功后才能部署项目哦~">快速部署</span>
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
                                                    <i class='${statusClass}'></i>${statusName}
                                                </span>
                                                </div>
                                                <div class='time-line-time'>
                                                    <div class='event-sign'><i class='fa fa-angle-right fa_caret' style='transform: rotate(${transformClass});'></i></div>
                                                    <div class='datetimes'><i class='fa fa-calendar margin'></i><fmt:formatDate value="${ciRecord.constructDate}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
                                                    <div class='time-on-timeout'><i class='fa fa-time'></i><fmt:formatNumber type="number" value="${ciRecord.constructTime/1000}" maxFractionDigits="0"/>s</div>
                                                </div>
                                                <div class='time-line-message' style='display: ${displayClass};'>
                                                    <div class='buildForm' buildid='' containerid='' buildername='builder2' status='fail'>
                                                        <div style='clear:both;'></div>
                                                        <div class='buildStatus' style='margin:0px 0px 10px 0px'></div>
                                                        <div class='build-logs' style='max-height: 400px; overflow: auto;margin-top:10px;background-color:black;color: #37fc34'>
															<pre class='logs' style='background-color:black; overflow: hidden;color: #37fc34;border:0; float:left'><span class='printLogSpan' status='${ciRecord.constructResult}' ciRecordId='${ciRecord.id}'>${ciRecord.logPrint}</span>
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
				                                <div class="form-group">
				                                    <label class="col-2x control-label">项目名称：</label>
		                                            <div class="col-sm-9">
				                                    <input id="description" name="description" class="form-control" type="text"
				                                           required="">
				                                    </div>
				                                </div>
				                               
				                            </div>
				                            <div class="row">
				                                <div class="form-group">
				                                    <label class="col-2x control-label">描述：</label>
				                                    <div class="col-sm-9"><textarea id="description" name="description" class="form-control" type="text"
				                                           required="" row="5"></textarea></div>
				                                    
				                                </div>
				                            </div>
				                            <div class="row">
				                                <div class="form-group">
				                                    <label class="col-2x control-label">JDK：</label>
				                                    <div class="col-sm-9"><select id="jdk-version" name="jdk-version" class="form-control">
				                                    	<option value="jdk1.7">jdk1.7</option>
				                                    	<option value="jdk1.8">jdk1.8</option>
				                                    </select></div>
				                                </div>
				                            </div>
			                        	</section>
			                        	<hr>
			                            <h4 class="c-title">代码管理</h4>
			                            <section class="registryinfo">
			                            	<div class="row">
				                            	<div class="form-group">
					                                <label class="col-2x control-label">代码库类型：</label>
					                                <div class="col-sm-9"><select id="codeType" name="codeType" class="form-control" >
					                                	<option value="0">-none-</option>
					                                    <option value="1">git</option>
					                                </select></div>
					                            </div>
				                            </div>
				                            <div class="row git-config">
				                            	<div class="form-group">
					                                <label class="col-2x control-label">代码仓库地址：</label>
					                                <div class="col-sm-9"><input id="codeUrl" name="codeUrl" class="form-control" type="text"
			                                           placeholder="例如：https://github.com/tenxcloud/php-hello-world.git"></div>
					                            </div>
					                            <div class="form-group">
					                                <label class="col-2x control-label">认证方式：</label>
					                                <div class="col-sm-9"><select id="codeType" name="codeType" class="form-control">
					                                    <option value="0">-none-</option>
					                                    <option value="1">gitlab(SSH)(gitlab SSH方式认证)</option>
				                                      	<option value="2">root/**********(gitlab HTTPS方式认证)</option>
					                                </select></div>
					                            </div>
					                            <div class="form-group">
					                                <label class="col-2x control-label">创建分支：</label>
					                                <div class="col-sm-9"><input id="buildBranch" name="buildBranch" type="text" class="form-control"
			                                                   value="*/master"></div>
					                            </div>
				                            </div>
				                            <div class="row git-config git-higher">
				                            	<div class="form-group">
					                                <label class="col-2x control-label">代码库用户名：</label>
					                                <div class="col-sm-9"><input id="codeUsername" name="codeUsername" type="text" class="form-control"
			                                                   value=""></div>
					                            </div>
					                            <div class="form-group">
					                                <label class="col-2x control-label">代码库密码：</label>
					                                <div class="col-sm-9"><input id="codePassword" name="codePassword" type="password"
			                                                   class="form-control" value=""></div>
					                            </div>
			                                </div>
			                               
			                            </section>
			                            <hr>
			                            <h4 class="c-title">构建</h4>
			                            <section class="registryinfo">
			                            	<div class="row">
				                            	<div class="form-group">
					                                <label class="col-2x control-label">增加构建步骤：</label>
					                                <div class="col-sm-9"><select id="addciStep" name="addciStep" class="form-control" >
					                                	<option value="0">-none-</option>
					                                    <option value="1">maven</option>
					                                    <option value="2">ant</option>
					                                </select></div>
					                            </div>
				                            </div>
				                            <div class="row maven">
				                            	<div class="form-group">
					                                <label class="col-2x control-label">maven版本：</label>
					                                <div class="col-sm-9"><select id="mavenVis" name="mavenVis" class="form-control" >
					                                	<option value="0">default</option>
					                                    <option value="1">maven1111</option>
					                                </select></div>
					                            </div>
					                            <div class="form-group">
					                                <label class="col-2x control-label">目标：</label>
					                                <div class="col-sm-9"><input id="Goals" name="Goals" type="text" class="form-control"
			                                                   value=""></div>
					                            </div>
					                            <div class="form-group">
					                                <label class="col-2x control-label">POM：</label>
					                                <div class="col-sm-9"><input id="pom" name="pom" type="text" class="form-control"
			                                                   value=""></div>
					                            </div>
					                            <div class="form-group">
					                                <label class="col-2x control-label">属性：</label>
					                                <div class="col-sm-9"><input id="Properties" name="Properties" type="text" class="form-control"
			                                                   value=""></div>
					                            </div>
					                            <div class="form-group">
					                                <label class="col-2x control-label">JVM选项：</label>
					                                <div class="col-sm-9"><input id="JVMOptions" name="JVMOptions" type="text" class="form-control"
			                                                   value=""></div>
					                            </div>
					                            <div class="form-group">
					                                <label class="col-2x control-label">使用私人maven存储库：</label>
					                                <div class="col-sm-9" style="line-height:34px"><input type="checkbox"></div>
					                            </div>
					                            <div class="form-group">
					                                <label class="col-2x control-label">配置文件：</label>
					                                <div class="col-sm-9"><select id="Settings-file" name="Settings-file" class="form-control" >
					                                	<option value="0">use default maven setting</option>
					                                    <option value="1">settings file in filesystem</option>
					                                </select></div>
					                            </div>
					                            <div class="form-group">
					                                <label class="col-2x control-label">全局配置文件：</label>
					                                <div class="col-sm-9"><select id="GlobalSettingsfile" name="GlobalSettingsfile" class="form-control" >
					                                	<option value="0">use default maven global setting</option>
					                                    <option value="1">global settings file in filesystem</option>
					                                </select></div>
					                            </div>
				                            </div>
				                            <div class="row ant ant-config">
				                            	<div class="form-group">
					                                <label class="col-2x control-label">ant版本：</label>
					                                <div class="col-sm-9"><select id="antVis" name="antVis" class="form-control" >
					                                	<option value="0">default</option>
					                                    <option value="1">ant1111</option>
					                                </select></div>
					                            </div>
					                            <div class="form-group">
					                                <label class="col-2x control-label">目标：</label>
					                                <div class="col-sm-9"><input id="Targets" name="Targets" type="text" class="form-control"
			                                                   value=""></div>
					                            </div>
				                            </div>
				                            <div class="row ant ant-higher">
				                            	<div class="form-group">
					                                <label class="col-2x control-label">构建文件：</label>
					                                <div class="col-sm-9"><input id="antBuildFile" name="antBuildFile" type="text" class="form-control"
			                                                   value=""></div>
					                            </div>
					                            <div class="form-group">
					                                <label class="col-2x control-label">性能：</label>
					                                <div class="col-sm-9"><input id="antProperties" name="antProperties" type="text" class="form-control"
			                                                   value=""></div>
					                            </div>
					                            <div class="form-group">
					                                <label class="col-2x control-label">java选项：</label>
					                                <div class="col-sm-9"><input id="antJavaOptions" name="antJavaOptions" type="text" class="form-control"
			                                                   value=""></div>
					                            </div>
				                            </div>
				                         
			                            </section>
			                            <hr>
										<h4 class="c-title">构建后操作</h4>
			                            <section class="registryinfo">
			                            	<div class="row">
				                            	<div class="form-group">
					                                <label class="col-2x control-label" title="增加构建后操作步骤">增加构建后操作步骤：</label>
					                                <div class="col-sm-9"><select id="codeType" name="codeType" class="form-control" >
					                                	<option value="0">-none-</option>
					                                    
					                                </select></div>
					                            </div>
				                            </div>
				                        </section>
			                            <div class="form-group">
                                            <label class="col-2x control-label"></label>
                                            <div class="col-sm-9">
                                                <br><input type="button" id="editCiBtn" class="btn btn-primary pull-right" value="确认修改">
                                            </div>
                                        </div>
                                        <input type="hidden" id="id" name="id" value="${ci.id}">
			                           
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
                                            <div class="col-sm-9">
                                                <select id="baseImageName" name="baseImageName"  style="width:218px; height:48px">
                                                    <option type="text" value="${currentBaseImage.name }">${currentBaseImage.name }</option>
                                                    <c:forEach var = "image" items = "${baseImage }">
                                                        <c:if test = "${currentBaseImage.name ne image.name }">
                                                               <option type="text" value="${image.name }">${image.name }</option>
                                                        </c:if>
                                                    </c:forEach>
                                                </select> : 
                                                <select id="baseImageId" name="baseImageId" style="height:48px">
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
                <div id="dockerfile-import" style="display:none; max-height:170px;overflow-y:scroll;overflow-x:hidden;">
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
                </div>

            </div>
        </div>
    </article>
</div>
</body>
</html>