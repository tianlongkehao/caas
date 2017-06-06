<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>云存储</title>
    <%@include file="../frame/header.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/storage.css"/>
  	<script type="text/javascript" src="<%=path %>/js/storage/storage-snapStrategy.js"></script> 
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
                    <li class="active">快照策略</li>
                </ol>
            </div>
            <div class="contentMain">
                <div class="row">
						<div class="col-sm-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>快照策略
									</h5>

									<div class="ibox-tools">
										<a id="storageAddSnapStrategy" onclick="snapStrategyAdd()" title="创建存快照策略"><i
											class="fa fa-plus"></i></a>
										<a id="snapStrategyDel" title="删除快照策略"  onclick="delStorages()"><i
											class="fa fa-trash"></i></a>
										<a href="javascript:window.location.reload(true);"
											id="volReloadBtn" title="刷新"><i class="fa fa-repeat"></i></a>
									</div>
								</div>
								<div class="ibox-content">
									<table class="table table-stripped table-hover dataTables-example">
										<thead>
											<tr>
												<th style="width: 5%; text-indent: 30px;"><input
													type="checkbox" autocomplete="off" class="chkAll"
													id="checkallbox" /></th>
												<th style="width: 15%;">自动快照策略名称</th>
	                                            <th style="width: 15%;">自动快照策略ID</th>
	                                            <th style="width: 15%;">自动快照策略详情</th>
	                                            <th style="width: 15%;">关联磁盘数</th>
	                                            <th style="width: 15%;">创建时间</th>
	                                            <th style="width: 10%;" class="del-operation">操作</th>
											</tr>
										</thead>
										<tbody id="storageList">
											<tr>
												<td style="width: 5%; text-indent: 30px;"><input
													type="checkbox" autocomplete="off" class="chkItem"
													/></td>
												<td style="width: 15%;">自动快照策略名称</td>
	                                            <td style="width: 15%;">自动快照策略ID</td>
	                                            <td style="width: 15%;">自动快照策略详情</td>
	                                            <td style="width: 15%;">关联磁盘数</td>
	                                            <td style="width: 15%;">创建时间</td>
	                                            <td style="width: 10%;class="del-operation">
	                                            	<a onclick="snapStrategyEdit()" title="修改策略"><i class="fa fa-edit"></i></a>
	                                            	<a onclick="setStorage()" title="设置磁盘"><i class="fa fa-cog"></i></a>
	                                            	<a onclick="delOneStorage()" title="删除磁盘"><i class="fa fa-trash"></i></a>
	                                            </td>
											</tr>
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
    </article>
</div>

<!-- 创建策略 -->
<div id="snapStrategyInfo" style="display:none">
    <ul class="popWin">
        <li class="line-h-3">
            <span class="s-edit-name">策略名称：<font color="red">*</font></span>
            <input id="q-storageName" class="form-control q-storage" type="text" value="">
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">创建时间：<font color="red">*</font></span>
            <ul class="createTimeInfo">
            	<li><label><input type="checkbox">00:00</label></li>
            	<li><label><input type="checkbox">01:00</label></li>
            	<li><label><input type="checkbox">02:00</label></li>
            	<li><label><input type="checkbox">03:00</label></li>
            	<li><label><input type="checkbox">04:00</label></li>
            	<li><label><input type="checkbox">05:00</label></li>
            	<li><label><input type="checkbox">06:00</label></li>
            	<li><label><input type="checkbox">07:00</label></li>
            	<li><label><input type="checkbox">08:00</label></li>
            	<li><label><input type="checkbox">09:00</label></li>
            	<li><label><input type="checkbox">10:00</label></li>
            	<li><label><input type="checkbox">11:00</label></li>
            	<li><label><input type="checkbox">12:00</label></li>
            	<li><label><input type="checkbox">13:00</label></li>
            	<li><label><input type="checkbox">14:00</label></li>
            	<li><label><input type="checkbox">15:00</label></li>
            	<li><label><input type="checkbox">16:00</label></li>
            	<li><label><input type="checkbox">17:00</label></li>
            	<li><label><input type="checkbox">18:00</label></li>
            	<li><label><input type="checkbox">19:00</label></li>
            	<li><label><input type="checkbox">20:00</label></li>
            	<li><label><input type="checkbox">21:00</label></li>
            	<li><label><input type="checkbox">22:00</label></li>
            	<li><label><input type="checkbox">23:00</label></li>
            </ul>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">重复日期：<font color="red">*</font></span>
            <ul class="repeatDateInfo">
            	<li><label><input type="checkbox">周一</label></li>
            	<li><label><input type="checkbox">周二</label></li>
            	<li><label><input type="checkbox">周三</label></li>
            	<li><label><input type="checkbox">周四</label></li>
            	<li><label><input type="checkbox">周五</label></li>
            	<li><label><input type="checkbox">周六</label></li>
            	<li><label><input type="checkbox">周日</label></li>
            </ul>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">保留时间：<font color="red">*</font></span>
            <ul class="retainTimeInfo">
            	<li><label><input type="radio" name="retainTime" value="">自定义时长<input type="number" class="retainTimeInput" value="30">天</label><span>保留天数取值范围：1-35536</span></li>
            	<li><label><input type="radio" name="retainTime">永久保留</label></li>
            </ul>
        </li>
    </ul>
</div>
<!-- 设置磁盘 -->
<div id="setStrategyInfo" style="display:none;width:96%;margin:0 auto;">
    
	<ul id="myTab" class="navTab nav-tabs">
	    <li class="active">
	        <a href="#notSet" data-toggle="tab">未设置策略磁盘</a>
	    </li>
	    <li><a href="#haveSet" data-toggle="tab">已设置策略磁盘</a></li>
	</ul>
	<div id="myTabContent" class="tab-content">
	    <div class="tab-pane fade in active" id="notSet">
	        <table class="table table-stripped table-hover dataTables-notset">
	        	<thead>
	        		<tr>
	        			<th style="width: 5%; text-indent: 10px;"><input
							type="checkbox" autocomplete="off" class="chkAll checkallboxset"
							id="checkallnotset" /></th>
	        			<th>磁盘名称</th>
	        			<th>磁盘ID</th>
	        			<th>磁盘大小</th>
	        			<th>磁盘属性</th>
	        			<th>操作</th>
	        		</tr>
	        	</thead>
	        	<tbody>
	        		<tr>
	        			<td style="width: 5%; text-indent: 10px;"><input
							type="checkbox" autocomplete="off" class="chkItem"/></td>
	        			<td>磁盘名称</td>
	        			<td>磁盘ID</td>
	        			<td>磁盘大小</td>
	        			<td>磁盘属性</td>
	        			<td><a class="textBtn">执行快照策略</a></td>
	        		</tr>
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
	    <div class="tab-pane fade" id="haveSet">
	        <table class="table table-stripped table-hover dataTables-haveset">
	        	<thead>
	        		<tr>
	        			<th style="width: 5%; text-indent: 10px;"><input
							type="checkbox" autocomplete="off" class="chkAll checkallboxset"
							/></th>
	        			<th>磁盘名称</th>
	        			<th>磁盘ID</th>
	        			<th>磁盘大小</th>
	        			<th>磁盘属性</th>
	        			<th>操作</th>
	        		</tr>
	        	</thead>
	        	<tbody>
	        		<tr>
	        			<td style="width: 5%; text-indent: 10px;"><input
							type="checkbox" autocomplete="off" class="chkItem"/></td>
	        			<td>磁盘名称</td>
	        			<td>磁盘ID</td>
	        			<td>磁盘大小</td>
	        			<td>磁盘属性</td>
	        			<td><a class="textBtn">执行快照策略</a></td>
	        		</tr>
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
	    
	</div>
    
</div>
<script type="text/javascript">
$('.dataTables-example').dataTable({
	"aoColumnDefs" : [ {
		"bSortable" : false,
		"aTargets" : [ 0,6 ]
	} ],
});
$("#checkallbox").parent().removeClass("sorting_asc");

$('.dataTables-notset').dataTable({
	"aoColumnDefs" : [ {
		"bSortable" : false,
		"aTargets" : [ 0,5 ]
	} ],
});

$('.dataTables-haveset').dataTable({
	"aoColumnDefs" : [ {
		"bSortable" : false,
		"aTargets" : [ 0,5 ]
	} ],
});
$(".checkallboxset").parent().removeClass("sorting_asc");
</script>

</body>
</html>