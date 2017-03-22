<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>云存储</title>
    <%@include file="../frame/header.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/storage.css"/>
  	<script type="text/javascript" src="<%=path %>/js/storage/storage-quick.js"></script> 
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
                    <li class="active">快存储</li>
                </ol>
            </div>
            <div class="contentMain">
                <div class="row">
						<div class="col-sm-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>快存储
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
												<th>磁盘名称/磁盘ID</th>
	                                            <th>磁盘大小</th>
	                                            <th>磁盘状态</th>
	                                            <th>可卸载</th>
	                                            <th>磁盘属性</th>
	                                            <th>描述</th>
	                                            <th class="item-operation">操作</th>
											</tr>
										</thead>
										<tbody id="storageList">
											<tr>
												<td text-indent: 30px;"><input
													type="checkbox" autocomplete="off" class="chkAll"
													id="checkallbox" /></td>
												<td><span class="storageName">adbde-dffds</span></td>
	                                            <td>5G</td>
	                                            <td>待挂载</td>
	                                            <td>支持</td>
	                                            <td>数据盘</td>
	                                            <td>测试</td>
	                                            <td class="item-operation"><i class="fa fa-trash"></i>
	                                            	<ul class="moreFun" style="margin-bottom:0px;line-height:40px;">
														<li class="dropdown ">
															<a id="'+row.id+'_moreFun" class="dropdown-toggle a-live" data-toggle="dropdown" style="margin-left: 5px" title="更多配置">
															<i class="fa fa-gears"></i></a>
														</li>
														
													</ul>
	                                            </td>
											</tr>
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
                <span class="">磁盘大小：</span>
                <input type="radio" name="updateStorageSize" class="updateStorageSize" value="20480" id="size20"><label for="size20">20<span>G</span></label>
                <input type="radio" name="updateStorageSize" class="updateStorageSize" value="51200" id="size50"><label for="size50">50<span>G</span></label>
                <input type="radio" name="updateStorageSize" class="updateStorageSize" value="102400" id="size100"><label for="size100">100<span>G</span></label>
                <input type="radio" name="updateStorageSize" class="updateStorageSize" id="updatedefVolNum">
                	<label for="updatedefVolNum"><input type="number" id="updatedefVol" placeholder="自定义大小"><span>G</span></label>
                <div>
                <span style="color:#1E90FF; padding-left:84px">总量:<span id="totalVol">${userResource.vol_size}</span>G</span>
                <span style="color:#1E90FF; padding-left:15px">剩余:<span id="restVol">${userResource.vol_surplus_size }</span>G 可用</span></div>
            </div>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">可卸载：</span>
            <span><i class="fa fa-toggle-onORoff fa-on" id="unloadBtn" value="1"></i><span class="toggle-text">支持</span></span>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">磁盘属性：</span>
            <select class="form-control q-storage">
            	<option value="0">--请选择磁盘属性--</option>
            	<option value="1">数据盘</option>
            	<option value="2">系统盘</option>
            </select>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">磁盘描述：</span>
            <textarea type="text" row="3" class="form-control q-storage" id="storage-mark"></textarea>
        </li>
    </ul>
</div>
<script type="text/javascript">
	
</script>

</body>
</html>