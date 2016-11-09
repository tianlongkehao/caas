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
                    <div class="create-detail ci-tab">项目描述</div>
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

                            <%-- 项目描述 --%>
                            <div class="project-details hide">
                                <div>
                                    代码仓库：
                                    <a target="_blank" class="btn btn-link" title="点击链接查看项目" href="${ci.codeUrl}" id="codeLocation">${ci.codeUrl}</a>
                                </div>
                            </div>

                            <%-- 基本设置 --%>
                            <div class="config-details hide">
                                <c:if test="${ci.type == 1}">
                                    <form id="editCiForm" class="form-horizontal" method="post" action="" role="form">
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
                                            <label for="dockerfileLocation" class="col-2x control-label">Dockerfile位置：</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control" id="dockerFileLocation" name="dockerFileLocation" value="/">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-2x control-label">默认代码分支：</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control" id="codeBranch" name="codeBranch" value="master" disabled="">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-2x control-label"></label>
                                            <div class="col-sm-9">
                                                <!-- <textarea class="form-control" id="ciDetail" name="ciDetail">...</textarea> -->
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