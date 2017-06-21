<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>云存储</title>
    <%@include file="../frame/header.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/storage.css"/>
  	<script type="text/javascript" src="<%=path %>/js/storage/storage-block.js"></script>
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
                    <li class="active">块存储</li>
                </ol>
            </div>
            <div class="contentMain">
                <div class="row">
						<div class="col-sm-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>块存储
									</h5>

									<div class="ibox-tools">
										<a id="storageQuickAdd" title="创建云盘"><i
											class="fa fa-plus"></i></a>
										<a id="storagedel" title="删除云盘"  onclick="delStorages()"><i
											class="fa fa-trash"></i></a>
										<a href="javascript:window.location.reload(true);"
											id="volReloadBtn" title="刷新"><i class="fa fa-repeat"></i></a>
									</div>
								</div>
								<div class="ibox-content">
									<table class="table table-stripped table-hover dataTables-example">
										<thead>
											<tr>
												<th text-indent: 30px;"><input
													type="checkbox" autocomplete="off" class="chkAll"
													id="checkallbox" /></th>
												<th>磁盘名称</th>
	                                            <th>磁盘大小(G)</th>
	                                            <th>磁盘状态</th>
	                                            <th>描述</th>
	                                            <th class="item-operation">操作</th>
											</tr>
										</thead>
										<tbody id="storageList">
										   <c:forEach items="${cephRbdInfos}" var="rbd">
											<tr>
												<td text-indent: 30px;"><input
													type="checkbox" autocomplete="off" class="chkAll"
													id="checkallbox" /></td>
												<td><span class="storageName">${rbd.name }</span></td>
	                                            <td>${rbd.size }</td>
	                                            <c:if test="${rbd.used ==true}">
	                                                <td>已挂载</td>
	                                            </c:if>
	                                            <c:if test="${rbd.used ==false}">
	                                                <td>未挂载</td>
	                                            </c:if>
	                                            <td>${rbd.detail }</td>
	                                            <td class="item-operation" rbdId=${rbd.id } rbd="${rbd.name }" size="${rbd.size }" detail="${rbd.detail }" release="${rbd.releaseWhenServiceDown }">
	                                            	<a onclick="createSnapshoot(this)"><i class="fa fa-camera fa-opr" title="创建快照" style="margin-left:0px"></i></a>
	                                            	<a onclick="createStrategy(this)"><i class="fa fa-tasks fa-opr" title="设置自动快照策略"></i></a>
	                                            	<a onclick="formatStrategy(this)"><i class="fa fa-eraser fa-opr" title="重新初始化磁盘"></i></a>
	                                            	<ul class="moreFun" style="margin-bottom:0px;line-height:40px;">
														<li class="dropdown ">
															<a class="dropdown-toggle a-live" data-toggle="dropdown">
															<i class="fa fa-gears fa-opr"></i></a>
															<ul class="dropdown-menu" style="margin-left:-20px;margin-top:40px">
																<li onclick="releaseStorage(this)"><a>释放</a></li>
																<li onclick="changeDescribe(this)"><a>修改磁盘描述</a></li>
																<li onclick="changeProperty(this)"><a>修改属性</a></li>
																<li onclick="changeStorageSize(this)"><a>磁盘扩容</a></li>
															</ul>
														</li>
													</ul>
	                                            </td>
											</tr>
											</c:forEach>
										</tbody>
										<tfoot class="hide">
											<tr>
												<td colspan="8">
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

<div id="storageQuickAddInfo" style="display:none">
    <ul class="popWin">
        <li class="line-h-3">
            <span class="s-edit-name">磁盘名称：</span>
            <input id="q-storageName" class="form-control q-storage" type="text" value="">
        </li>
        <li class="line-h-3">
            <div class="param-set">
                <span class="">磁盘大小(G)：</span>
                <input type="radio" name="updateStorageSize" class="updateStorageSize" value="20480" id="size20" checked="checked"><label for="size20">20<span>G</span></label>
                <input type="radio" name="updateStorageSize" class="updateStorageSize" value="51200" id="size50"><label for="size50">50<span>G</span></label>
                <input type="radio" name="updateStorageSize" class="updateStorageSize" value="102400" id="size100"><label for="size100">100<span>G</span></label>
                <input type="radio" name="updateStorageSize" class="updateStorageSize" value ="selfdefine" id="updatedefVolNum">
                	<label for="updatedefVolNum"><input type="number" id="updatedefVol" placeholder="自定义大小"><span>G</span></label>
                <div>
                <span style="color:#1E90FF; padding-left:84px">总量:<span id="totalVol">${userResource.vol_size}</span>G</span>
                <span style="color:#1E90FF; padding-left:15px">剩余:<span id="restVol">${userResource.vol_surplus_size }</span>G 可用</span></div>
            </div>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">磁盘描述：</span>
            <textarea row="3" class="form-control q-storage" id="storage-mark"></textarea>
        </li>
    </ul>
</div>

<!-- 修改磁盘属性 -->
<div id="changeProperty" style="display:none">
    <ul class="popWin">
    	<li class="line-h-3">
            <span class="s-edit-name">磁盘名称：</span>
            <input id="rbd" class="form-control q-storage" type="text" value="" disabled>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">磁盘大小(G)：</span>
            <input id="size" class="form-control q-storage" type="text" value="" disabled>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">释放行为：</span>
            <ul class="releaseStorageInfo">
            	<li><label><input id="release" type="checkbox"/>磁盘随实例释放</label></li>
            </ul>
        </li>
    </ul>
</div>

<!-- 修改磁盘描述 -->
<div id="changeDescribe" style="display:none">
    <ul class="popWin">
        <li class="line-h-3">
            <span class="s-edit-name">磁盘名称：<font color="red"></font></span>
            <input id="rbd2" class="form-control q-storage" type="text" value="" disabled>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">磁盘描述：<font color="red">*</font></span>
            <textarea  row="3" class="form-control q-storage" id="update-detail"></textarea>
        </li>
    </ul>
</div>
<!-- 创建快照 -->
<div id="createSnapshoot" style="display:none">
    <ul class="popWin">
        <li class="line-h-3">
            <span class="s-edit-name">磁盘名称：</span>
            <input id="rbd-snap" class="form-control q-storage" type="text" style="background-color:#ddd" value="" readonly>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">快照名称：<font color="red">*</font></span>
            <input id="snapshootName" class="form-control q-storage" type="text" value="">
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">快照描述：<font color="red">*</font></span>
            <textarea row="3" class="form-control q-storage" id="snap-mark"></textarea>
        </li>
    </ul>
</div>
<!-- 创建策略 -->
<div id="createStrategy" style="display:none">
    <ul class="popWin">
        <li class="line-h-3">
            <span class="s-edit-name">策略名称：</span>
            <input id="strategyname" class="form-control q-storage" type="text" value="" style="background-color:#ddd" readonly>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">创建时间：</span>
            <ul class="createTimeInfo">
            	<li><label><input type="checkbox" name="time" value="0">00:00</label></li>
            	<li><label><input type="checkbox" name="time" value="1">01:00</label></li>
            	<li><label><input type="checkbox" name="time" value="2">02:00</label></li>
            	<li><label><input type="checkbox" name="time" value="3">03:00</label></li>
            	<li><label><input type="checkbox" name="time" value="4">04:00</label></li>
            	<li><label><input type="checkbox" name="time" value="5">05:00</label></li>
            	<li><label><input type="checkbox" name="time" value="6">06:00</label></li>
            	<li><label><input type="checkbox" name="time" value="7">07:00</label></li>
            	<li><label><input type="checkbox" name="time" value="8">08:00</label></li>
            	<li><label><input type="checkbox" name="time" value="9">09:00</label></li>
            	<li><label><input type="checkbox" name="time" value="10">10:00</label></li>
            	<li><label><input type="checkbox" name="time" value="11">11:00</label></li>
            	<li><label><input type="checkbox" name="time" value="12">12:00</label></li>
            	<li><label><input type="checkbox" name="time" value="13">13:00</label></li>
            	<li><label><input type="checkbox" name="time" value="14">14:00</label></li>
            	<li><label><input type="checkbox" name="time" value="15">15:00</label></li>
            	<li><label><input type="checkbox" name="time" value="16">16:00</label></li>
            	<li><label><input type="checkbox" name="time" value="17">17:00</label></li>
            	<li><label><input type="checkbox" name="time" value="18">18:00</label></li>
            	<li><label><input type="checkbox" name="time" value="19">19:00</label></li>
            	<li><label><input type="checkbox" name="time" value="20">20:00</label></li>
            	<li><label><input type="checkbox" name="time" value="21">21:00</label></li>
            	<li><label><input type="checkbox" name="time" value="22">22:00</label></li>
            	<li><label><input type="checkbox" name="time" value="23">23:00</label></li>
            </ul>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">重复日期：</span>
            <ul class="repeatDateInfo">
            	<li><label><input type="checkbox" name="week" value="1">周一</label></li>
            	<li><label><input type="checkbox" name="week" value="2">周二</label></li>
            	<li><label><input type="checkbox" name="week" value="3">周三</label></li>
            	<li><label><input type="checkbox" name="week" value="4">周四</label></li>
            	<li><label><input type="checkbox" name="week" value="5">周五</label></li>
            	<li><label><input type="checkbox" name="week" value="6">周六</label></li>
            	<li><label><input type="checkbox" name="week" value="0">周日</label></li>
            </ul>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">保留时间：</span>
            <ul class="retainTimeInfo">
            	<li><label><input type="radio" name="retainTime" value="1">自定义时长<input id="keep" type="number" class="retainTimeInput" style="background-color:#ddd" readonly>天</label><span>保留天数取值范围：1-35536</span></li>
            	<li><label><input type="radio" name="retainTime" value="2">永久保留</label></li>
            </ul>
        </li>
        <li class="line-h-3">
            <ul id="operationBtn">
            </ul>
        </li>
    </ul>
</div>
<!-- 磁盘扩容 -->
<div id="changeStorageSize" style="display:none">
    <ul class="popWin">
    	<li class="line-h-3">
            <span class="s-edit-name">磁盘名称：</span>
            <input id="rbd3" class="form-control q-storage" type="text" value="" disabled>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">磁盘大小(G)：</span>
            <input id="size3" class="form-control q-storage" type="text" value="" disabled>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">磁盘扩容：<font color="red">*</font></span>
            <input id="extendsize" class="form-control q-storage" type="number" value=""><span class="storageUnit">G</span>
        </li>
    </ul>
</div>

<script type="text/javascript">

</script>

</body>
</html>