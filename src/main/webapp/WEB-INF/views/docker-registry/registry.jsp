<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>镜像中心</title>
    <%@include file="../frame/header.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/docker-registry.css"/>
    <script type="text/javascript" src="<%=path %>/js/registry/registry.js"></script>
</head>
<body>
    <jsp:include page="../frame/menu.jsp" flush="true" >
        <jsp:param name="registry" value="0" />
    </jsp:include>

    <div class="page-container">
        <article>
            <div class="page-main">
                <div class="contentTitle">
                    <ol class="breadcrumb">
                        <li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span id="nav1">控制台</span></a></li>
                        <li><i class="fa fa-angle-right"></i></li>
                        <li class="active" id="nav2">${active }</li>
                    </ol>
                </div>
                <div class="contentMain">
					<div class="row">
						<div class="col-sm-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>${active }
									</h5>

									<div class="ibox-tools">
										<a href="javascript:window.location.reload(true);"
											id="volReloadBtn" title="刷新"><i class="fa fa-repeat"></i></a> 
										 
										<a id="exportBtn" title="导出"><i
											class="fa fa-share-square-o"></i></a> 
										<a id="SearchBtn" title="搜索"><i
											class="fa fa-search"></i></a> 
										<a id="deleteButton" class="no-drop"
											href="javascript:delImages()" title="删除"> <i
											class="fa fa-trash"></i>
										</a>
									</div>
								</div>
								<div class="ibox-content">
									<input type="text" class="form-control input-sm m-b-xs"
										id="filter" placeholder="搜索表格...">

									<table class="footable table table-stripped fixed-table-header" data-page-size="9"
										data-filter=#filter>
										<thead>
											<tr>
												<th style="width: 5%; text-indent: 30px;">
													<input type="checkbox" class="chkAll" id="checkallbox" /></th>
												<th style="width: 15%;text-indent:30px;">名称</th>
	                                            <th style="width: 15%;text-indent: 5px;">版本</th>
	                                            <th style="width: 15%;text-indent: 8px;">镜像信息</th>
	                                            <th style="width: 15%;text-indent: 5px;">创建人</th>
	                                            <th style="width: 20%;text-indent: 10px;">创建时间</th>
	                                            <th style="width: 10%;text-indent: 10px;" class="del-operation">操作</th>
											</tr>
										</thead>
										<tbody id="imageList">
											<c:forEach items="${images}" var="image">
												
												<c:if test="${cur_image.id != image.id}">
													<tr class="userTr" id="${image.id }">
														<td style="width: 5%; text-indent: 22px;"><input
															type="checkbox" class="chkItem" name="ids"
															value="${image.id }"></td>
														<td style="width: 15%; text-indent:22px;"><a
															href="<%=path %>/registry/detail/${image.id }" title="查看详细信息"
															onmousemove="style.textDecoration='underline'"
															onmouseout="style.textDecoration='none'">${image.name }</a>
														</td>
														<td style="width: 15%; text-indent: 15;">${image.version }</td>
														<td style="width: 15%; text-indent: 8;"
															id="user.user_province" name="user.user_province">${image.remark }</td>
														<td style="width: 15%; text-indent: 10;">${image.creator }</td>
														<td style="width: 20%; text-indent: 10;"
															id="user.user_autority" name="user.user_autority">${image.createTime }</td>
														<td style="width: 10%; text-indent: 10;"><a
															class="no-drop" href="<%=path %>/service/add?imageName=${image.name}&imageVersion=${image.version}&imgID=${image.id}&resourceName=${image.resourceName}"
															title="部署"> <i class="fa fa-wrench"></i>
														</a><a
															class="no-drop" href="javascript:delOneImage()"
															style="margin-left: 10px" title="导出"> <i class="fa fa-share-square-o"></i>
														</a><a
															class="no-drop" href="<%=path %>/registry/downloadImage?imageName=${image.name}&imageVersion=${image.version}&imgID=${image.id}&resourceName=${image.resourceName}"
															style="margin-left: 10px" title="删除"> <i class="fa fa-trash"></i>
														</a></td>
													</tr>
												</c:if>
											</c:forEach>
										</tbody>
										<tfoot>
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
                    <div class="content hide">
                        <div class="search">
                            <form class="search-group-inner" style="width:60%;margin: 0 auto;position: relative;" action="<%=path %>/registry/${index }" method = "post">
                                <input name="imageName" class="search-img" placeholder="搜索镜像" type="text"><button type="submit" class="btn btn-primary btn-send">搜索</button>
                            </form>
                        </div>
                        <div class="images-layout">
                            <ul id="imageList">

                                <c:forEach items="${images}" var="image" >

                                    <li class="images-panel">
                                        <div class="select-img">
	                                        <c:if test="${image.currUserFavor==0 }">
	                                            <i class="fa fa-star-o star-style" style="color:#4280CB"></i>
	                                        </c:if>
	                                        <c:if test="${image.currUserFavor==1 }">
	                                            <i class="fa fa-star star-style" style="color:#efa421"></i>
	                                        </c:if>
                                            <div class="mir-img ">
                                                <img src="<%=path %>/images/image-1.png">
                                            </div>
                                        </div>
                                        <div class="select-info">
                                            <div class="pull-right-text">${image.name}
                                             	<c:if test="${image.imageType==2 }">
	                                            	<span class="btn btn-link lock">
	                                            		<i class="fa fa-lock"></i>
	                               					</span>
                               					</c:if>
                                            </div>
                                            <div>
                                            	<i class="fa fa-tag"></i> ${image.version }
                                                <div class="pull-right">
                                                    <a href="<%=path %>/service/add?imageName=${image.name}&imageVersion=${image.version}&imgID=${image.id}&resourceName=${image.resourceName}" class="btn-pull-deploy btn" imageversion="${image.version}" imagename="${image.name}" >部署</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="create-item">
                                            <a href="<%=path %>/registry/detail/${image.id }">
                                                <span class="note-text" > ${image.summary } </span>
                                            </a>
                                        </div>
                                    </li>

                                </c:forEach>

                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </article>
    </div>
    <script type="text/javascript">
    $(document).ready(function(){
		$(".footable").footable();
		$("#checkallbox").next().addClass("hide");
		$(".del-operation").children("span").addClass("hide");
	})
    </script>

</body>
</html>