<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title></title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="/css/mod/ci.css"/>
    <script type="text/javascript" src="/js/ci/ci_detail.js"></script>
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
                    <span class="btn btn-primary pull-right" id="buildBtn" ciId="${ci.id }" constructionStatus="${ci.constructionStatus }">构建</span>

                        <div class="code-tabmain">

                            <%-- 构建日志 --%>
                            <div class="log-details" id="ciRecordList">

                                <c:forEach items="${ciRecordList}" var="ciRecord">

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

                                    <div class='event-line' repotype='' status='${eventStatus}'>
                                        <div class='event-status ${eventStatus}'>
                                            <i class='fa ${eventStatusClass} notes'></i>
                                        </div>
                                        <div class='time-line-content lives'>
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
                                                    <div class='event-sign'><i class='fa fa-angle-right fa_caret' style='transform: rotate(90deg);'></i></div>
                                                    <div class='datetimes'><i class='fa fa-calendar margin'></i>${ciRecord.constructDate}</div>
                                                    <div class='time-on-timeout'><i class='fa fa-time'></i>${ciRecord.constructTime}</div>
                                                </div>
                                                <div class='time-line-message' style='display: block;'>
                                                    <div class='buildForm' buildid='' containerid='' buildername='builder2' status='fail'>
                                                        <div style='clear:both;'></div>
                                                        <div class='buildStatus' style='margin:0px 0px 10px 0px'></div>
                                                        <div class='build-logs' style='max-height: 400px; overflow: auto;margin-top:10px;background-color:black;color: #37fc34'>
<pre class='logs' style='background-color:black;color: #37fc34;border:0'>
<span>
</span>
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

            </div>
        </div>
    </article>
</div>
</body>
</html>