<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>模板</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/tensorflow.css" />
<script type="text/javascript" src="<%=path%>/js/service/tensorflow.js"></script>
</head>
<body>
	<jsp:include page="../frame/bcm-menu.jsp" flush="true">
		<jsp:param name="service" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="<%=path %>/bcm/${cur_user.id }"><i
								class="fa fa-home"></i>&nbsp;&nbsp;<span id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active">TensorFlow</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="row">
						<div class="col-sm-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>TensorFlow
									</h5>
									<div class="ibox-tools">
										<a href="javascript:add()" title="新建"><i class="fa fa-plus"></i> </a>
										<a
											href="javascript:window.location.reload(true);"
											id="volReloadBtn" title="刷新"><i class="fa fa-repeat"></i>
										</a>
									</div>
								</div>
								<div class="ibox-content">
									<table
										class="table table-striped table-hover dataTables-example"
										data-filter=#filter>
										<thead>
											<tr>
												<th style="width: 5%; text-indent: 30px;"><input
													type="checkbox" autocomplete="off" class="chkAll"
													id="checkallbox" /></th>
												<th style="width: 10%;">名称</th>
												<th style="width: 10%;">Cpu</th>
												<th style="width: 10%;">内存(G)</th>
												<th style="width: 15%;">存储卷</th>
												<th style="width: 15%;">访问路径</th>
												<th style="width: 15%;">创建时间</th>
												<th style="width: 20%;" class="item-operation">操作</th>
											</tr>
										</thead>
										<tbody id="tensorflowList">

										</tbody>
										<tfoot class="hide">
											<tr>
												<td colspan="4">
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

    <div id="tensorflowAdd" style="display:none">
    <ul class="popWin">
        <li class="line-h-3">
            <span class="s-edit-name">服务名称：<font color="red">*</font></span><span class="s-edit-name">${username}-</span>
            <input id="tensorflowName" style="padding-top:8px;padding-bottom:5px;float:left;width:62%;" type="text" value="">
        </li>
        <li class="line-h-3">
            <div class="param-set">
                <span class="mem-edit">Cpu:<font color="red">*</font></span>
                <input type="radio" name="cpu" class="cpu" value="4" checked>4
                <input type="radio" name="cpu" class="cpu" value="8">8
                <input type="radio" name="cpu" class="cpu" value="16">16
                <span style="color:#1E90FF; padding-left:15px;float:right">剩余:<span id="restCpu">${leftcpu }</span></span>
            </div>
        </li>
        <li class="line-h-3">
            <div class="param-set">
                <span class="cpu-edit">内存:<font color="red">*</font></span>
                <input type="radio" name="memory" class="memory" value="8" checked>8<span>G</span>
                <input type="radio" name="memory" class="memory" value="16">16<span>G</span>
                <input type="radio" name="memory" class="memory" value="32">32<span>G</span>
                <span style="color:#1E90FF; padding-left:15px;float:right">剩余:<span id="restMem">${leftmemory }</span>G</span>
                </div>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">镜像:<font color="red">*</font></span>
            <select id="image" class="form-control q-storage">
            	<option value=0>--请选择镜像--</option>
            </select>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">存储:</span>
            <select id="storage" class="form-control q-storage">
            	<option value="0">--请选择块设备--</option>
            </select>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">密码:<font color="red">*</font></span>
            <input type="password" id="password" class="form-control q-storage" value=""/>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">确认密码:<font color="red">*</font></span>
            <input type="password" id="password2" class="form-control q-storage" value=""/>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">描述：</span>
            <textarea type="text" row="3" class="form-control q-storage" id="mark"></textarea>
        </li>
    </ul>
</div>

<div id="tensorflowDetail" style="display:none">
    <ul class="popWin">
        <li class="line-h-3">
            <span class="s-edit-name">服务名称：</span><span class="s-edit-name"></span>
        </li>
        <li class="line-h-3">
            <div class="param-set">
                <span class="mem-edit">Cpu:</span>
            </div>
        </li>
        <li class="line-h-3">
            <div class="param-set">
                <span class="cpu-edit">内存:</span>
            </div>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">镜像:</span>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">存储:</span>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">描述：</span>
            <textarea type="text" row="3" class="form-control q-storage" id="mark2" readonly></textarea>
        </li>
    </ul>
</div>

	<script type="text/javascript">
		$('.dataTables-example').dataTable({
			"aoColumnDefs" : [ {
				"bSortable" : false,
				"aTargets" : [ 0, 6 ]
			} ],
			"aaSorting" : [ [ 5, "desc" ] ]
		});
		$("#checkallbox").parent().removeClass("sorting_asc");
	</script>

</body>
</html>