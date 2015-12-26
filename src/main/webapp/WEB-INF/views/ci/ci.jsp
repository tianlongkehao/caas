<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title></title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="/css/mod/ci.css"/>
    <script type="text/javascript" src="/js/ci/ci.js"></script>
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
                    <li class="active" id="nav2">构建</li>
                </ol>
            </div>
            <div class="contentMain">

                <div class="caption clearfix">
                    <ul class="toolbox clearfix">
                        <li><a href="javascript:void(0);" id="ciReloadBtn"><i class="fa fa-repeat"></i></a></li>
                        <!-- <li><a href="javascript:void(0);">添加源代码</a></li> -->
                        <li><a href="/ci/add" id="ciAddBtn">快速构建</a></li>
                        <li><a href="/ci/addSource" id="ciAddSourceBtn">代码上传构建</a></li>
                    </ul>
                </div>
                <div class="itemTable">
                    <table class="table ci-table">
                        <thead>
                        <tr>
                            <th>
                                <div class="table-head">
                                    <table class="table" style="margin:0px;">
                                        <thead>
                                        <tr style="height:40px;">
                                            <th style="width: 15%;text-indent:30px;">项目名称</th>
                                            <th style="width: 12%;text-indent: 15px;">构建状态</th>
                                            <th style="width: 15%;text-indent: 20px;">代码源</th>
                                            <th style="width: 12%;">上次构建时间</th>
                                            <th style="width: 10%;text-indent: 8px;">持续时间</th>
                                            <th style="width: 15%;text-indent: 10px;">镜像</th>
                                            <th style="width: 18%;text-indent: 10px;">功能</th>
                                        </tr>
                                        </thead>
                                    </table>
                                </div>
                            </th>
                        </tr>
                        </thead>
                        <tbody id="projectsBody">
                        <tr>
                            <td>
                                <div class="content-table">
                                    <table class="table tables">
                                        <tbody id="ciList">
                                        <c:forEach items="${ciList}" var="ci" >

                                            <c:choose>
                                                <c:when test="${ci.constructionStatus == 1}">
                                                    <c:set var="statusName" value="未构建"></c:set>
                                                    <c:set var="statusClassName" value="fa_stop"></c:set>
                                                    <c:set var="loadingImgShowClass" value="hide"></c:set>
                                                </c:when>
                                                <c:when test="${ci.constructionStatus == 2}">
                                                    <c:set var="statusName" value="构建中"></c:set>
                                                    <c:set var="statusClassName" value="fa_success"></c:set>
                                                    <c:set var="loadingImgShowClass" value=""></c:set>
                                                    <c:set var="btnCursorClass" value="cursor-no-drop"></c:set>
                                                </c:when>
                                                <c:when test="${ci.constructionStatus == 3}">
                                                    <c:set var="statusName" value="完成"></c:set>
                                                    <c:set var="statusClassName" value="fa_success"></c:set>
                                                    <c:set var="loadingImgShowClass" value="hide"></c:set>
                                                </c:when>
                                                <c:when test="${ci.constructionStatus == 4}">
                                                    <c:set var="statusName" value="失败"></c:set>
                                                    <c:set var="statusClassName" value="fa_stop"></c:set>
                                                    <c:set var="loadingImgShowClass" value="hide"></c:set>
                                                </c:when>
                                            </c:choose>

                                            <c:choose>
                                                <c:when test="${ci.codeType == 1}">
                                                    <c:set var="codeTypeName" value="svn"></c:set>
                                                </c:when>
                                                <c:when test="${ci.codeType == 2}">
                                                    <c:set var="codeTypeName" value="git"></c:set>
                                                </c:when>
                                            </c:choose>
                                            
                                            <c:choose>
                                                <c:when test="${ci.imgId == null || ci.imgId == 0}">
                                                     <c:set var="cursorClass" value="cursor-no-drop"></c:set>
                                                     <c:set var="hrefValue" value=""></c:set>
                                                </c:when>
                                                <c:otherwise>
                                                	<c:set var="cursorClass" value=""></c:set>
                                                	 <c:set var="hrefValue" value="href='/registry/detail/${ci.imgId }'"></c:set>
                                                </c:otherwise>
                                            </c:choose>

                                            <tr class="ci-listTr" style="cursor:auto">
                                                <td style="width: 15%; text-indent:22px;">
                                                    <a href="/ci/detail/${ci.id}" title="查看详细信息">${ci.projectName}</a>
                                                </td>
                                                <td style="width: 12%;" class="cStatusColumn">
                                                    <i class="${statusClassName}"></i> ${statusName}
                                                    <img src="/images/loading4.gif" alt="" class="${loadingImgShowClass}" />
                                                </td>
                                                <td style="width: 15%;">
                                                	<c:choose>
		                                                <c:when test="${ci.type == 2}">
		                                                </c:when>
		                                                <c:otherwise>
		                                                	 <a data-toggle="tooltip" data-placement="left" title="" target="_blank" href="${ci.codeUrl}" data-original-title="查看源代码">
		                                                        <span class="bj-code-source"><i class="fa fa-lg"></i>
		                                                            ${codeTypeName}
		                                                        </span>
		                                                    </a>
		                                                </c:otherwise>
		                                            </c:choose>
                                                </td>
                                                <td style="width: 12%;">${ci.constructionDate}</td>
                                                <td style="width: 10%;">${ci.constructionTime}</td>
                                                <td style="width: 15%;">
                                                    <a target="_blank" title="" class="${cursorClass}" ${hrefValue }>${ci.imgNameFirst}/${ci.imgNameLast}</a>
                                                </td>
                                                <td style="width:18%">
                                                    <span class="bj-green ${btnCursorClass}" data-toggle="tooltip" data-placement="right" title="" data-original-title="重新构建" constructionStatus="${ci.constructionStatus}"  ciId="${ci.id}">构建&nbsp;&nbsp;<i class="fa fa-arrow-circle-right"></i></span>
                                                </td>
                                            </tr>

                                        </c:forEach>

                                        </tbody>
                                    </table>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </article>
</div>

</body>
</html>