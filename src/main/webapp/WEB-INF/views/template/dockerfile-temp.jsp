<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>模板</title>
    <%@include file="../frame/header.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/template.css"/>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/core/footable/footable.core.css"/>
    <script type="text/javascript" src="<%=path%>/js/template/dockerfile-temp.js"></script>
</head>
<body>
<jsp:include page="../frame/bcm-menu.jsp" flush="true">
    <jsp:param name="template" value=""/>
</jsp:include>

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="<%=path %>/home"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                            id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" style="width:110px">dockerfile模板</li>
                </ol>
            </div>
            <div class="contentMain">
					<div class="row">
						<div class="col-sm-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>dockerfile模板
									</h5>
									<div class="ibox-tools">
										<a href="<%=path %>/template/dockerfile/add" id="dockerfileAdd" title="新建dockerfile"><i
											class="fa fa-plus"></i></a>
										<a id="deleteButton" class="no-drop" href="javascript:delDockerfiles()" title="删除">
											<i id = "delBtn" class="fa fa-trash self_a"></i>
										</a>
										<a href="javascript:window.location.reload(true);"
											id="volReloadBtn" title="刷新"><i class="fa fa-repeat"></i></a>
									</div>
								</div>
								<div class="ibox-content">
									<table class="table table-striped table-hover dataTables-example" data-page-size="8"
										data-filter=#filter>
										<thead>
											<tr>
												<th style="width: 5%; text-indent: 30px;">
													<input type="checkbox" autocomplete="off" class="chkAll" id="checkallbox"/>
												</th>
												<th style="width: 30%;text-indent:30px;">名称</th>
	                                            <th style="width: 30%;">创建时间</th>
	                                            <th style="width: 10%;text-indent: 10px;" class="del-operation">操作</th>
											</tr>
										</thead>
										<tbody id="storageList">
											  <c:forEach items = "${dockerFileList }" var = "dockerfile">
													<tr>
														<td style="width: 5%; text-indent: 30px;">
															<input type="checkbox" class="chkItem" name="chkItem"
																autocomplete="off" id="checkboxID" value="${dockerfile.id }" />
														</td>
														<td class="textOverflow" style="width: 670px;text-indent:30px;cursor: pointer;">
														<a href="<%=path %>/template/dockerfile/detail/${dockerfile.id }"
                                                            title="查看${dockerfile.templateName }详细信息" 
                                                            onmousemove="style.textDecoration='underline'"
                                                            onmouseout="style.textDecoration='none'">${dockerfile.templateName }</a>
														</td>
			                                            <td style="width: 30%;">
			                                                 <fmt:formatDate value="${dockerfile.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
		                                                </td>
			                                            <td style="width: 10%;text-indent: 10px;" >
			                                                 <a title="删除" href="javascript:void(0)" onclick="deletedockerfile(this)" dockerfileId = "${dockerfile.id }">
			                                                     <i class="fa fa-trash"></i>
			                                                 </a>
			                                            </td>
													</tr>
											  </c:forEach>
										</tbody>
										<tfoot class="hide">
											<tr>
												<td colspan="3">
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
        "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0,3] }],
        "aaSorting": [[ 2, "desc" ]]
	});
	$("#checkallbox").parent().removeClass("sorting_asc");
</script>

</body>
</html>