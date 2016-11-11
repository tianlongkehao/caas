<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>构建</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/ci.css"/>
    <script type="text/javascript" src="<%=path %>/js/ci/ci.js"></script>
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

                <div class="caption clearfix hide">
                    <ul class="toolbox clearfix">
                        <li><a href="javascript:void(0);" id="ciReloadBtn"><i class="fa fa-repeat"></i></a></li>
                        <li><a href="<%=path %>/ci/add" id="ciAddBtn">代码构建</a></li>
                        <li><a href="<%=path %>/ci/addCodeSource" id="ciAddCodeSourceBtn">快速构建</a></li>
                        <li><a href="<%=path %>/ci/uploadImage" id="ciAddSourceBtn">上传镜像</a></li>
                        <li><a href="<%=path %>/ci/dockerfile">Dockerfile构建</a></li>
                    </ul>
                </div>
                <div class="itemTable">
                	<div class="row">
						<div class="col-md-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5 class="ci-h5 active" id="ciTab"><i class="fa fa-map-marker" style="margin-right: 6px;"></i>构建</h5>
									<h5 class="ci-h5" id="ciCodeTab"><i class="fa fa-map-marker" style="margin-right: 6px;"></i>代码构建</h5>

									<div class="ibox-tools">
										<a href="<%=path %>/ci/add" id="ciAddBtn"><img src="<%=path %>/images/code.png" class="fa-img" alt="代码构建" title="代码构建"></a>
										<a href="<%=path %>/ci/addCodeSource" id="ciAddCodeSourceBtn"><img src="<%=path %>/images/quick.png" class="fa-img" alt="快速构建" title="快速构建"></a>
										<a href="<%=path %>/ci/uploadImage" id="ciAddSourceBtn"><img src="<%=path %>/images/upload.png" class="fa-img" alt="上传镜像" title="上传镜像"></a>
										<a href="<%=path %>/ci/dockerfile"><img src="<%=path %>/images/dockerfile.png" class="fa-img" alt="Dockerfile构建" title="Dockerfile构建"></a>
										<a href="javascript:window.location.reload(true);" id="ciReloadBtn" title="刷新">
											<i class="fa fa-repeat"></i>
										</a>
									</div>
								</div>
								<div class="ibox-content ci-content">
									<table class="table table-stripped table-hover dataTables-example1">
											<thead>
												<tr style="height: 40px;">
													<th style="width: 15%; text-indent: 30px;">项目名称</th>
													<th style="width: 12%; text-indent: 15px;">构建状态</th>
													<th style="width: 20%;">上次构建时间</th>
													<th style="width: 10%; text-indent: 8px;">持续时间</th>
													<th style="width: 20%; text-indent: 10px;">镜像</th>
													<th style="width: 10%; text-indent: 10px;">功能</th>
												</tr>
											</thead>

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
                                                	 <c:set var="hrefValue"  value="href='/registry/detail/${ci.imgId }'"></c:set>
                                                </c:otherwise>
                                            </c:choose>

                                            <tr class="ci-listTr" style="cursor:auto">
                                                <td style="width: 15%; text-indent:30px;">
                                                    <a href="<%=path %>/ci/detail/${ci.id}" title="查看详细信息">${ci.projectName}</a>
                                                </td>
                                                <td style="width: 12%; text-indent:15px;" class="cStatusColumn">
                                                    <i class="${statusClassName}"></i> ${statusName}
                                                    <img src="<%=path %>/images/loading4.gif" alt="" class="${loadingImgShowClass}" />
                                                </td>
                                                <td style="width: 20%;"><fmt:formatDate value="${ci.constructionDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                <td style="width: 10%; text-indent:8px;"><fmt:formatNumber type="number" value="${ci.constructionTime/1000}" maxFractionDigits="0"/>s</td>
                                                <td style="width: 20%; text-indent:10px;">
                                                    <a target="_blank" title="" class="${cursorClass}"  ${hrefValue}>${ci.imgNameFirst}/${ci.imgNameLast}:${ci.imgNameVersion}</a>
                                                </td>
                                                <td style="width:10%; text-indent:10px;">
                                                    <span class="bj-green ${btnCursorClass}" data-toggle="tooltip" data-placement="right" title="构建" data-original-title="重新构建" constructionStatus="${ci.constructionStatus}"  ciId="${ci.id}"><i class="fa fa-arrow-circle-right"></i></span>
                                                </td>
                                            </tr>

                                        </c:forEach>
												
										</tbody>
										<tfoot class="hide">
											<tr>
												<td colspan="6">
													<ul class="pagination pull-right"></ul>
												</td>
											</tr>
										</tfoot>
									</table>
								</div>
								<div class="ibox-content ci-code-content">
									<table class="table table-stripped table-hover dataTables-example">
											<thead>
												<tr style="height: 40px;">
													<th style="width: 15%; text-indent: 30px;">项目名称</th>
													<th style="width: 12%; ">上次构建状态</th>
													<th style="width: 15%; ">上次成功时间</th>
													<th style="width: 15%;">上次失败时间</th>
													<th style="width: 10%; ">上次持续时间</th>
													<th style="width: 18%; ">镜像</th>
													<th style="width: 8%; text-indent: 10px;">功能</th>
												</tr>
											</thead>

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
                                                    <c:set var="statusName" value="成功"></c:set>
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
                                                	 <c:set var="hrefValue"  value="href='/registry/detail/${ci.imgId }'"></c:set>
                                                </c:otherwise>
                                            </c:choose>

                                            <tr class="ci-listTr" style="cursor:auto">
                                                <td style="width: 15%; text-indent:30px;">
                                                    <a href="<%=path %>/ci/detail/${ci.id}" title="查看详细信息">${ci.projectName}</a>
                                                </td>
                                                <td style="width: 12%; " class="cStatusColumn">
                                                    <i class="${statusClassName}"></i> ${statusName}
                                                    <img src="<%=path %>/images/loading4.gif" alt="" class="${loadingImgShowClass}" />
                                                </td>
                                                <td style="width: 15%; ">
                                                	<c:choose>
		                                                <c:when test="${ci.type == 2||ci.type == 3}">
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
                                                <td style="width: 15%;"><fmt:formatDate value="${ci.constructionDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                <td style="width: 10%; "><fmt:formatNumber type="number" value="${ci.constructionTime/1000}" maxFractionDigits="0"/>s</td>
                                                <td style="width: 18%; ">
                                                    <a target="_blank" title="" class="${cursorClass}"  ${hrefValue}>${ci.imgNameFirst}/${ci.imgNameLast}:${ci.imgNameVersion}</a>
                                                </td>
                                                <td style="width:8%; ">
                                                    <span class="bj-green ${btnCursorClass}" data-toggle="tooltip" data-placement="right" title="构建" data-original-title="重新构建" constructionStatus="${ci.constructionStatus}"  ciId="${ci.id}"><i class="fa fa-arrow-circle-right"></i></span>
                                                </td>
                                            </tr>

                                        </c:forEach>
												
										</tbody>
										<tfoot class="hide">
											<tr>
												<td colspan="7">
													<ul class="pagination pull-right"></ul>
												</td>
											</tr>
										</tfoot>
									</table>
								</div>
							</div>
						</div>
					</div>
                </div>

            </div>
        </div>
    </article>
</div>
	<!-- <script type="text/javascript">
		 $('.dataTables-example').dataTable({
			"aoColumnDefs" : [ {
				"bSortable" : false,
				"aTargets" : [ 6 ]
			} ],
			"aaSorting": [[ 3, "desc" ]]
		});
	</script> -->
</body>
</html>