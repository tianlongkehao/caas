<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>服务</title>
    <%@include file="../frame/header.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/storage.css"/>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/core/footable/footable.core.css"/>
  	<script type="text/javascript" src="<%=path %>/js/storage/storage.js"></script> 
</head>
<body>
<jsp:include page="../frame/menu.jsp" flush="true">
    <jsp:param name="service" value=""/>
</jsp:include>

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                            id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" id="nav2">服务</li>
                     <li style="margin-left: -44px;"><i class="fa fa-angle-right"></i></li>
                    <li class="active">存储和备份</li>
                </ol>
            </div>
            <div class="contentMain">
                <div class="row">
						<div class="col-sm-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>存储卷
									</h5>

									<div class="ibox-tools">
										<a href="<%=path %>/service/storage/add" id="storageAdd" title="创建存储卷"><i
											class="fa fa-plus"></i></a>
										<a href="javascript:window.location.reload(true);"
											id="volReloadBtn" title="刷新"><i class="fa fa-repeat"></i></a>
									</div>
								</div>
								<div class="ibox-content">
									<table class="table table-stripped table-hover dataTables-example" 
										data-filter=#filter>
										<thead>
											<tr>
												<th style="width: 15%;text-indent:30px;">名称</th>
	                                            <th style="width: 15%;text-indent: 15px;">使用状态</th>
	                                            <!-- <th style="width: 10%;text-indent: 20px;">格式</th> -->
	                                            <th style="width: 15%;text-indent: 8px;">挂载点</th>
	                                            <th style="width: 15%;text-indent: 10px;">大小</th>
	                                            <th style="width: 20%;">创建时间</th>
	                                            <th style="width: 10%;text-indent: 10px;" class="del-operation">操作</th>
											</tr>
										</thead>
										<tbody id="storageList">
											
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
				</div>
          		<!-- <div id="pagination"></div> -->
            </div>
        </div>
    </article>
</div>

<div id="storageUpdate" style="display:none">
    <ul class="popWin">
        <li class="line-h-3">
            <span class="edit-name-c">名称：</span>
            <input id="upgradeStorageName" disabled="disabled" style="margin-top: 5px;width: 165px;" type="text" value="">
        </li>
        <li class="line-h-3">
            <div class="param-set">
                <span class="edit-name-c">存储大小：</span>
                <input type="radio" name="updateStorageSize" class="updateStorageSize" value="20480">20<span>G</span>
                <input type="radio" name="updateStorageSize" class="updateStorageSize" value="51200">50<span>G</span>
                <input type="radio" name="updateStorageSize" class="updateStorageSize" value="102400">100<span>G</span>
                <input type="radio" name="updateStorageSize" class="updateStorageSize" id="updatedefVolNum">
                	<input id="updatedefVol" style="width:50px; font-size:8px" placeholder="自定义大小"><span>G</span>
                <div>
                <span style="color:#1E90FF; padding-left:84px">总量:<span id="totalVol">${cur_user.vol_size}</span>G</span>
                <span style="color:#1E90FF; padding-left:15px">剩余:<span id="restVol">${leftstorage }</span>G 可用</span></div>
                <!-- <input id="storageSizeUpdateSlider" data-slider-id='storageSizeUpdateSliderData' type="text" data-slider-min="0" data-slider-max="1024" data-slider-step="1" />
                <input type="text" left="" value="250" id="storageSizeUpdateSlider_input" name="storageSize">
                <span>M</span> -->
                <!-- <span style="color: grey;">当前可用ram：<label id="leftram" ></label>M</span>-->
            </div>
        </li>
    </ul>
</div>
<script type="text/javascript">
	$('.dataTables-example').dataTable({
        "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 5 ] }]
	});
	//折叠ibox
	$('.collapse-link').click(function () {
	    var ibox = $(this).closest('div.ibox');
	    var button = $(this).find('i');
	    var content = ibox.find('div.ibox-content');
	    content.slideToggle(200);
	    button.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
	    ibox.toggleClass('').toggleClass('border-bottom');
	    setTimeout(function () {
	        ibox.resize();
	        ibox.find('[id^=map-]').resize();
	    }, 50);
	});
</script>

</body>
</html>