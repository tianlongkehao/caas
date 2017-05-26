<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>云存储</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href= "<%=path%>/plugins/datetimepicker/css/jquery-ui.css"/>

<link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/storage.css"/>
<script type="text/javascript" src="<%=path %>/js/storage/storage-block.js"></script>
</head>
<body>

	<jsp:include page="../frame/bcm-menu.jsp" flush="true">
		<jsp:param name="ci" value="" />
	</jsp:include>
	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i><span id="nav1">&nbsp;&nbsp;控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li><a href="<%=path %>/service"><span id="nav2">块存储</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" style="width:200px">storage-quickName</li>
					</ol>
					<input hidden="true" value="${service.id }" id="serId"/>
					<input hidden="true" value="${service.serviceName }" id="serName"/>
					<input hidden="true" value="${service.status }" id="serStatus"/>
			        <input hidden="true" value="${service.serviceType }" id="serType"/>
				</div>
				<div class="contentMain">

					<div class="baseInfo center-style">
						<ul class="nav navbar-nav">
							<li><a class="BASE btn-prim serDetail">基本信息</a></li>
							<li><a class="INSTANCES serDetail">磁盘监控</a></li>
						</ul>
					</div>
         			<!-- Dlag -->
					<div class="containerInfo">
					<form id="BaseSerForm" name="BaseSerForm"
					 action="<%=path%>/service/detail/editBaseSerForm.do" >
					  <input hidden="true" value="${service.id }"  name="id"/>
						<table class="table w50">
							<thead>
								<tr>
									<th>基本信息</th>
									<th>
										<div class="hide" style="float:right; margin-right:30px; color:#337ab7; font-size:19px">
											<c:if test="${service.status==1 or service.status==4}">
												<i class="fa fa-edit" id="editSerBtn" name="editSerBtn" title="修改"></i>
											</c:if>
											<i class="fa fa-reply" id="restSerBtn" name="restSerBtn" title="还原"></i>
											<i class="fa fa-save" id="saveSerBtn" name="saveSerBtn" title="保存"></i>
											<i class="fa fa-times" id="canclSerBtn" name="canclSerBtn" title="取消"></i>
										</div>
									</th>
								</tr>
							</thead>
							<tbody class="BORDER">
								<tr>
									<td>磁盘名称：deft-dee</td>
									<td>磁盘ID：123456</td>
								</tr>
								<tr>
									<td>磁盘大小：50G</td>
									<td>磁盘状态：未挂载</td>
								</tr>
								<tr>
									<td>磁盘属性：系统盘</td>
									<td>创建时间：2017-03-28</td>
								</tr>
								<tr>
									<td>创建人：小二</td>
									<td>本地磁盘快照：3个</td>
								</tr>
								<tr>
									<td colspan="2">磁盘描述：wwwwwwwwwwwwwwwwwwwwwwwwwwww</td>
									
								</tr>
							</tbody>
						</table>
						<table class="table basicInfo w50">
							<thead>
								<tr>
									<th>磁盘挂载信息</th>
									<th>&nbsp;</th>
								</tr>
							</thead>
							<tbody class="BORDER">
								<tr>
									<td>所挂载的实例：wwwwwwww</td>
									<td>挂载路径：/src</td>
								</tr>
								<tr>
									<td>磁盘是否随实例释放：是</td>
									<td>自动快照是否随磁盘释放：否</td>
								</tr>
								
							</tbody>
						</table>
						</form>
					</div>

					<div class="containerInstances hide" style="min-height: 300px;">
						<div id="IOPSInfo" style="width:100%;height:200px;background-color:#fff;border:1px solid #ddd">IOPS（个）无数据</div>
						<div id="capacityInfo" style="width:100%;height:200px;background-color:#fff;border:1px solid #ddd;margin-top:10px">吞吐量（MBps）无数据</div>
					</div>
					
				</div>
			</div>
		</article>
	</div>
</body>
</html>
