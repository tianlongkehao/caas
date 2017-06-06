<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>云存储</title>
    <%@include file="../frame/header.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/storage.css"/>
  	<script type="text/javascript" src="<%=path %>/js/storage/storage-snap.js"></script> 
</head>
<body>
<jsp:include page="../frame/bcm-menu.jsp" flush="true">
    <jsp:param name="service" value=""/>
</jsp:include>

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                            id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active">云存储</li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active">快照</li>
                </ol>
            </div>
            <div class="contentMain">
                <div class="row">
						<div class="col-sm-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>快照
									</h5>

									<div class="ibox-tools">
										<a href="javascript:window.location.reload(true);"
											id="volReloadBtn" title="刷新"><i class="fa fa-repeat"></i></a>
									</div>
								</div>
								<div class="ibox-content">
									<table class="table table-stripped table-hover dataTables-example">
										<thead>
											<tr>
												<!-- <th style="width: 5%; text-indent: 30px;"><input
													type="checkbox" autocomplete="off" class="chkAll"
													id="checkallbox" /></th> -->
												<th style="width: 10%;text-indent:20px;">快照名称</th>
	                                            <th style="width: 8%">快照ID</th>
	                                            <th style="width: 10%">磁盘名称</th>
	                                            <th style="width: 8%">磁盘ID</th>
	                                            <th style="width: 10%;">磁盘大小</th>
	                                            <th style="width: 10%;">磁盘属性</th>
	                                            <th style="width: 10%;">创建时间</th>
	                                            <th style="width: 10%;">进度</th>
	                                            <th style="width: 8%;">状态</th>
	                                            <th style="width: 10%;">快照描述</th>
	                                            <th style="text-indent: 5px;" class="del-operation">操作</th>
											</tr>
										</thead>
										<tbody id="snapList">
											<tr>
												<td style="width: 10%;text-indent:20px;">快照名称</td>
	                                            <td style="width: 8%">快照ID</td>
	                                            <td style="width: 10%">磁盘名称</td>
	                                            <td style="width: 8%">磁盘ID</td>
	                                            <td style="width: 10%;">磁盘大小</td>
	                                            <td style="width: 10%;">磁盘属性</td>
	                                            <td style="width: 10%;">创建时间</td>
	                                            <td style="width: 10%;">进度</td>
	                                            <td style="width: 8%;">状态</td>
	                                            <td style="width: 10%;">快照描述</td>
	                                            <td style="text-indent: 5px;" class="del-operation"><a onclick="storageRollBack()" title="回滚磁盘"><i class="fa fa-history"></i></a></td>
											</tr>
										</tbody>
										<tfoot class="hide">
											<tr>
												<td colspan="11">
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
    </article>
</div>

<div id="storageRollBack" style="display:none">
    <ul class="popWin">
		<li class="s-first-li"><img src="<%=path %>/images/warning.png" style="width:66px"></li>
		<li class="s-second-li"><p>你确定把&nbsp;<strong>数据盘：{dfsfsfd}</strong>&nbsp;的数据回滚到&nbsp;<strong>2017-02-02 15:15</strong>&nbsp;时刻吗？</p>
		<p><strong>数据盘上该时刻之后的数据将被清除。请谨慎操作！</strong></p>
		<p><strong>只有停止中的实例和当前磁盘没有创建中没有创建中的快照才可以回滚磁盘。</strong></p>
		<div><label><input type="checkbox" vlaue="">回滚后立即启动实例</label></div>
		</li>        
    </ul>
</div>

<script type="text/javascript">
$('.dataTables-example').dataTable({
	"aoColumnDefs" : [ {
		"bSortable" : false,
		"aTargets" : [ 10 ]
	} ],
});
$("#checkallbox").parent().removeClass("sorting_asc");
</script>

</body>
</html>