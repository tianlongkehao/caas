<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>镜像中心</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/docker-registry.css" />
<script type="text/javascript" src="<%=path%>/js/registry/registry.js"></script>
</head>
<body>
	<jsp:include page="../frame/bcm-menu.jsp" flush="true">
		<jsp:param name="registry" value="0" />
	</jsp:include>
	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li id="index" class="active" value = "${index}">${active }</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="row">
						<div class="col-md-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>${active }
									</h5>
									<div class="ibox-tools">
										<c:if test="${index == 1}">
											  <a id="deleteButton"
												    class="no-drop" href="javascript:void(0)" onclick = "delImages()" title="删除">
												<i class="fa fa-trash"></i></a>
										</c:if>
										<c:if test="${user.user_autority == 1}">
											<a href="javascript:syncImages();" id="volRefreshBtn" title="同步"><i
												class="fa fa-refresh"></i></a>
										</c:if>
										 <a href="javascript:window.location.reload(true);"
											id="volReloadBtn" title="刷新"><i class="fa fa-repeat"></i></a>
									</div>
								</div>
								<div class="ibox-content">
									<table class="table table-striped table-hover dataTables-example">
										<thead>
											<tr>
												<th style="text-indent: 30px;"><input
													type="checkbox" class="chkAll" id="checkallbox" /></th>
												<th style="text-indent: 10px;" class="sonarTh">代码质量</th>
												<th style="text-indent: 10px;">名称</th>
												<th style="text-indent: 8px;">版本</th>
												<th style="text-indent: 8px;">镜像信息</th>
												<th style="text-indent: 5px;">创建人</th>
												<th style="text-indent: 8px;">创建时间</th>
												<th style="text-indent: 10px;"
													class="del-operation">操作</th>
											</tr>
										</thead>
										<tbody id="imageList">
											<%-- <c:forEach items="${images}" var="image">

												<c:if test="${cur_image.id != image.id}">
													<tr class="userTr" id="${image.id }">
														<td style="width: 5%; text-indent: 22px;">
														<input type="checkbox" class="chkItem" name="ids"
															value="${image.id }"></td>
														<td style="width: 15%; text-indent: 22px;"><a
															href="<%=path %>/registry/detail/${image.id }"
															title="查看详细信息"
															onmousemove="style.textDecoration='underline'"
															onmouseout="style.textDecoration='none'">${image.name }</a>
														</td>
														<td style="width: 15%; text-indent: 15;">${image.version }</td>
														<td style="width: 15%; text-indent: 8;"
															id="user.user_province" name="user.user_province">${image.remark }</td>
														<td style="width: 15%; text-indent: 10;">${image.creatorName }<c:if
																test="${image.creatorName == ''||image.creatorName == null}">该创建人已被删除</c:if></td>
														<td style="width: 20%; text-indent: 10;"
															id="user.user_autority" name="user.user_autority">${image.createDate }</td>
														<td style="width: 10%; text-indent: 10;">

	                                                        <c:if test="${image.isDelete == 1 }">
	                                                            <c:if test="${image.currUserFavor==0 }">
	                                                                    <a class="no-drop a-oper forkquick" imageId="${image.id }">
	                                                                       <i class="fa fa-star-o star-style" style="color: #4280CB;margin-left:35px;"></i>
	                                                                    </a>
	                                                            </c:if>

	                                                            <c:if test="${image.currUserFavor==1 }">
	                                                                <a class="no-drop a-oper forkquick"  imageId="${image.id }">
	                                                                    <i class="fa fa-star star-style" style="color: #337ab7;margin-left:35px;"></i>
	                                                                </a>
	                                                            </c:if>
	                                                        </c:if>

														    <c:if test="${image.isDelete != 1 }">
																<a class="no-drop" href="<%=path %>/service/add?imageName=${image.name}&imageVersion=${image.version}&imgID=${image.id}&resourceName=${image.resourceName}"
																	 imageversion="${image.version}" imagename="${image.name}" title="部署">
																	 <i class="fa fa-wrench"></i>
																</a>
																<a class="no-drop a-oper downloadImage" imageversion="${image.version}" imagename="${image.name}" imgID="${image.id }" resourcename= "${image.resourceName}" title="导出">
																	<i class="fa fa-share-square-o"></i>
																</a>
																<c:if test="${image.currUserFavor==0 }">
																		<a class="no-drop a-oper forkquick" imageId="${image.id }"><i
																			class="fa fa-star-o star-style"
																			style="color: #4280CB"></i></a>
																</c:if>

																<c:if test="${image.currUserFavor==1 }">
																	<a class="no-drop a-oper forkquick" imageId="${image.id }">
																		<i class="fa fa-star star-style" style="color: #337ab7"></i>
																	</a>
																</c:if>
																<c:if test="${editImage==image.creator }">
																	<a class="no-drop a-oper" href="javascript:void(0)" onclick="deleteImage(this)"
																			title="删除" imageversion="${image.version}" imagename="${image.name}" imageid="${image.id}"> <i class="fa fa-trash"></i>
																	</a>
																</c:if>
															</c:if>
														</td>
													</tr>
												</c:if>
											</c:forEach> --%>
										</tbody>
										<tfoot class="hide">
											<tr>
												<td colspan="7">
													<ul class="pagination pull-right"></ul>
												</td>
											</tr>
										</tfoot>
									</table>
                                   <input type = "hidden" value = "${userId }" id = "userId">
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

		</article>
	</div>
	<div id="delItemcfg" style="display:none;min-height:200px;max-height:600px;">

		<div class="panel-group" id="namefilter">
			<div class="panel panel-success">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a data-toggle="collapse" data-parent="#accordion"
						   href="#collapseOne" onclick="filters(this)">
							<span class="">按名称筛选</span><span class="caret caret-filter"></span>
						</a>
					</h4>
				</div>
				<div id="collapseOne" class="panel-collapse collapse in">
					<div class="panel-body namefiltercon">
						<!-- <div class="panel-group filter" id="accordion1">
							<div class="panel panel-success">
								<div class="panel-heading">
									<h4 class="panel-title">
										<input type="checkbox" class="checkAllItem1">
										<a data-toggle="collapse" data-parent="#accordion1"
										   href="#collapseOne1" onclick="filters(this)">
											<span class="">demo1</span><span class="caret caret-filter"></span>
										</a>
									</h4>
								</div>
								<div id="collapseOne1" class="panel-collapse collapse in">
									<div class="panel-body">
										<ul class="image-version">
											<li><input type="checkbox" class="checkItem1"><span title="title">v19v1-555-55-7v1-555-55-7v1-555-55-7v1-555-55-7</span></li>
											<li><input type="checkbox" class="checkItem1"><span title="title">v1-555-55-7</span></li>
											<li title="title"><input type="checkbox" class="checkItem1"><span>v144444</span></li>
											<li title="title"><input type="checkbox" class="checkItem1"><span>v1-155-555-5</span></li>
										</ul>
									</div>
								</div>
							</div>
						</div> -->

					</div>
				</div>
			</div>
		</div>

		<div class="panel-group" id="timefilter">
			<div class="panel panel-info">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a data-toggle="collapse" data-parent="#timefilter"
						   href="#timeOne" onclick="filters(this)">
							<span class="">按时间筛选</span><span class="caret caret-filter"></span>
						</a>
					</h4>
				</div>
				<div id="timeOne" class="panel-collapse collapse in">
					<div class="panel-body timefiltercon">
						<!-- <div class="panel-group filter" id="timeAccordion1">
							<div class="panel panel-info">
								<div class="panel-heading">
									<h4 class="panel-title">
										<input type="checkbox">
										<a data-toggle="collapse" data-parent="#timeAccordion1"
										   href="#timecollapseOne1" onclick="filters(this)">
											<span class="">一个月前</span><span class="caret caret-filter"></span>
										</a>
									</h4>
								</div>
								<div id="timecollapseOne1" class="panel-collapse collapse in">
									<div class="panel-body">
										<ul class="image-version">
											<li><input type="checkbox"><span>name</span><br><span>nv19</span></li>
											<li><input type="checkbox"><span>name+v1-555-55-7</span></li>
											<li><input type="checkbox"><span>name+v144444</span></li>
											<li><input type="checkbox"><span>name+v1-155-555-5</span></li>
										</ul>
									</div>
								</div>
							</div>
						</div> -->
						<!-- <div class="panel-group filter" id="timeAccordion2">
							<div class="panel panel-info">
								<div class="panel-heading">
									<h4 class="panel-title">
										<input type="checkbox">
										<a data-toggle="collapse" data-parent="#timeAccordion2"
										   href="#timecollapseOne2" onclick="filters(this)">
											<span class="">两个月前</span><span class="caret caret-filter"></span>
										</a>
									</h4>
								</div>
								<div id="timecollapseOne2" class="panel-collapse collapse in">
									<div class="panel-body">
										<ul class="image-version">
											<li><input type="checkbox"><span>name+v19</span></li>
											<li><input type="checkbox"><span>name+v1-555-55-7</span></li>
											<li><input type="checkbox"><span>name+v144444</span></li>
											<li><input type="checkbox"><span>name+v1-155-555-5</span></li>
											<li><input type="checkbox"><span>name+v19</span></li>
											<li><input type="checkbox"><span>name+v1-555-55-7</span></li>
											<li><input type="checkbox"><span>name+v144444</span></li>
											<li><input type="checkbox"><span>name+v1-155-555-5</span></li>
											<li><input type="checkbox"><span>name+v19</span></li>
											<li><input type="checkbox"><span>name+v1-555-55-7</span></li>
											<li><input type="checkbox"><span>name+v144444</span></li>
											<li><input type="checkbox"><span>name+v1-155-555-5</span></li>
										</ul>
									</div>
								</div>
							</div>
						</div> -->
					</div>
				</div>
			</div>
		</div>



	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			/* $('.dataTables-example').dataTable( {
			    "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0 ,6] }]
			}); */
			$("#checkallbox").parent().removeClass("sorting_asc");
		})
	</script>

</body>
</html>
