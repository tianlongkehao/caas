<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>zookeeper</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/zookeeper.css" />
<script type="text/javascript" src="<%=path%>/js/market/zookeeper.js"></script>
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
						<li class="active">Zookeeper</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="row">
						<div class="col-sm-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>Zookeeper
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
												<th style="width: 15%;">集群名称</th>
												<th style="width: 20%;">运行状态</th>
												<th style="width: 20%;">节点数</th>
												<th style="width: 20%;">创建时间</th>
												<th style="width: 20%;" class="item-operation">操作</th>
											</tr>
										</thead>
										<tbody id="zookeeperList">

										    <c:forEach items="${zookeepers}" var="zookeeper">
                                                <tr>
                                                	<td style="width: 5%; text-indent: 30px;">
                                                		<input type="checkbox" class="chkItem" name="chkItem"/>
                                                	</td>
                                                	<td style="width: 15%;">
                                                	  <c:choose>
                                                	  	<c:when test="${zookeeper.status == 0}">
                                                	  		${zookeeper.name }
                                                	  	</c:when>
                                                	  	<c:otherwise>
                                                        	<a href="javascript:detail(${zookeeper.id })" title="查看详细信息">${zookeeper.name }</a>
                                                	  	</c:otherwise>
                                                	  </c:choose>
                                                	</td>
                                                	<td id="status_${zookeeper.id}" style="width: 20%;">
                                                         <c:choose>
                                                			<c:when test="${zookeeper.status == 0}">
                                                				停止
                                                			</c:when>
                                                			<c:otherwise>
                                                				运行中
                                                			</c:otherwise>
                                                		</c:choose>
                                                	</td>
                                                	<td style="width: 20%;">
                                                         3
                                                	</td>
                                                	<td style="width: 20%;">
                                                         ${zookeeper.createDate }
                                                	</td>
                                                	<td style="width: 20%;">
                                                		<c:choose>
                                                			<c:when test="${zookeeper.status == 0}">
                                                				<a class="a-live" href="javascript:start(${zookeeper.id })" style="margin-left: 5px" title="开始"><i class="fa fa-play self_a"></i></a>
																<a class="no-drop" style="margin-left: 5px" title="停止" ><i class="fa fa-power-off"></i></a>
																<a class="a-live" href="javascript:update(${zookeeper.id })" style="margin-left: 5px" title="修改"><i class="fa fa-cog"></i></a>
                                                			</c:when>
                                                			<c:otherwise>
                                                				<a class="no-drop" style="margin-left: 5px" title="开始"><i class="fa fa-play self_a"></i></a>
																<a class="a-live" href="javascript:stop(${zookeeper.id })" style="margin-left: 5px" title="停止" ><i class="fa fa-power-off"></i></a>
                                                			    <a class="no-drop" style="margin-left: 5px" title="修改"><i class="fa fa-cog"></i></a>
                                                			</c:otherwise>
                                                		</c:choose>
	 													<a href="javascript:remove(${zookeeper.id })" style="margin-left: 5px" title="删除" ><i class="fa fa-trash"></i></a>
													</td>
                                                </tr>
										    </c:forEach>

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

    <div id="zookeeperAdd" style="display:none">
    <ul class="popWin">
        <li class="line-h-3">
            <span class="s-edit-name">集群名称：<font color="red">*</font></span>
            <input type="text" id="zookeeperName" class="form-control q-storage" value=""/>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">副本数：</span>3
        </li>
        <li class="line-h-3">
            <div class="param-set">
                <span class="mem-edit">Cpu × Memory<font color="red">*</font></span>
                <input type="radio" name="cpu×memeory" class="cpu" value=1 checked>1 × 2
                <input type="radio" name="cpu×memeory" class="cpu" value=2>2 × 4
                <input type="radio" name="cpu×memeory" class="cpu" value=3>2 × 8
                <span style="color:#1E90FF; padding-left:15px;float:right">内存剩余:<span id="restMem">${leftmemory }</span>G</span>
                <span style="color:#1E90FF; padding-left:15px;float:right">Cpu剩余:<span id="restCpu">${leftcpu }</span></span>
            </div>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">镜像:<font color="red">*</font></span>
            <select id="image" class="form-control q-storage">
            	<option value=0>--请选择镜像--</option>
            	<c:forEach items="${images}" var="image">
            	    <option value=${image.id }>${image.name }:${image.version }</option>
            	</c:forEach>
            </select>
        </li>
        <li class="line-h-3">
            <div class="param-set">
                <span class="mem-edit">存储:<font color="red">*</font></span>
                <input type="radio" name="storage" class="cpu" value=5 checked>5G
                <input type="radio" name="storage" class="cpu" value=20>20G
                <input type="radio" name="storage" class="cpu" value=50>50G
                <span style="color:#1E90FF; padding-left:15px;float:right">剩余:<span id="restStorage">${leftstorage }</span>G</span>
            </div>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">描述：</span>
            <textarea type="text" row="3" class="form-control q-storage" id="mark"></textarea>
        </li>

        <label>高级<input type="checkbox" id="advanced2"></label>
        			<section class="advanced2">
	                            <div class="row">
	                                <div class="form-group col-md-12">
	                                    <label class="c-project-tit">客户端端口：2181</label>
		                            </div>
	                            </div>
	                            <div class="row">
		                            <div class="form-group col-md-12">
	                                    <label class="c-project-tit">文件存储目录：/var/lib/zookeeper</label>
		                            </div>
	                            </div>
	                            <div class="row">
		                            <div class="form-group col-md-12">
	                                    <span class="s-edit-name">超时时间下限(ms)：</span>
	                                    <input type="number"
											class="form-control q-storage" id="timeout_end" name="databaseNum"
											value="2000" placeholder="2000" min="0">
		                            </div>
	                            </div>
	                            <div class="row">
		                            <div class="form-group col-md-12">
	                                    <span class="s-edit-name">初始超时限制(tick)：</span>
		                            <input type="number"
											class="form-control q-storage" id="timeout_start" name="databaseNum"
											value="10" placeholder="10" min="0">
		                            </div>
	                            </div>
	                            <div class="row">
		                            <div class="form-group col-md-12">
	                                    <span class="s-edit-name">同步超时限制(tick)：</span>
		                           <input type="number"
											class="form-control q-storage" id="timeout_syn" name="databaseNum"
											value="2000" placeholder="2000" min="0">
		                            </div>
	                            </div>
	                            <div class="row">
		                            <div class="form-group col-md-12">
	                                    <span class="s-edit-name">节点最大并发数：</span>
		                           <input type="number"
											class="form-control q-storage" id="max_node" name="databaseNum"
											value="60" placeholder="60" min="0">
		                            </div>
	                            </div>
	                            <div class="row">
		                            <div class="form-group col-md-12">
	                                    <span class="s-edit-name">待处理请求最大值：</span>
		                           <input type="number"
											class="form-control q-storage" id="max_request" name="databaseNum"
											value="1000" placeholder="1000" min="0">
		                            </div>
	                            </div>
                       </section>
    </ul>
</div>

<div id="zookeeperUpdate" style="display:none">
    <ul class="popWin">
        <li class="line-h-3">
            <span class="s-edit-name">集群名称：<font color="red">*</font></span>
            <span id="zookeeperName2"></span>
            <input type="hidden" id="zookeeperId2">
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">副本数：</span>3
        </li>
        <li class="line-h-3">
            <div class="param-set">
                <span class="mem-edit">Cpu × Memory<font color="red">*</font></span>
                <input type="radio" name="cpu×memeory2" class="cpu" value=1 checked>1 × 2
                <input type="radio" name="cpu×memeory2" class="cpu" value=2>2 × 4
                <input type="radio" name="cpu×memeory2" class="cpu" value=3>2 × 8
                <span style="color:#1E90FF; padding-left:15px;float:right">内容剩余:<span id="restMem2">${leftmemory }</span>G</span>
                <span style="color:#1E90FF; padding-left:15px;float:right">Cpu剩余:<span id="restCpu2">${leftcpu }</span></span>
                <input type="hidden" id="lastCpu" />
                <input type="hidden" id="lastMem" />
            </div>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">镜像:<font color="red">*</font></span>
            <span id="image2"></span>
        </li>
        <li class="line-h-3">
            <div class="param-set">
                <span class="mem-edit">存储:<font color="red">*</font></span>
                <span id="storage2"></span>
            </div>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">描述:</span>
            <textarea type="text" row="3" class="form-control q-storage" id="mark2"></textarea>
        </li>

        <label>高级<input type="checkbox" id="advanced"></label>
        			<section class="advanced">
	                            <div class="row">
	                                <div class="form-group col-md-12">
	                                    <label class="c-project-tit">客户端端口：2181</label>
		                            </div>
	                            </div>
	                            <div class="row">
		                            <div class="form-group col-md-12">
	                                    <label class="c-project-tit">文件存储目录：/var/lib/zookeeper</label>
		                            </div>
	                            </div>
	                            <div class="row">
		                            <div class="form-group col-md-12">
	                                    <span class="s-edit-name">超时时间下限(ms)：</span>
	                                    <input type="number"
											class="form-control q-storage" id="timeout_end2"
											value="2000" placeholder="2000" min="0">
		                            </div>
	                            </div>
	                            <div class="row">
		                            <div class="form-group col-md-12">
	                                    <span class="s-edit-name">初始超时限制(tick)：</span>
		                            <input type="number"
											class="form-control q-storage" id="timeout_start2"
											value="10" placeholder="10" min="0">
		                            </div>
	                            </div>
	                            <div class="row">
		                            <div class="form-group col-md-12">
	                                    <span class="s-edit-name">同步超时限制(tick)：</span>
		                           <input type="number"
											class="form-control q-storage" id="timeout_syn2"
											value="2000" placeholder="2000" min="0">
		                            </div>
	                            </div>
	                            <div class="row">
		                            <div class="form-group col-md-12">
	                                    <span class="s-edit-name">节点最大并发数：</span>
		                           <input type="number"
											class="form-control q-storage" id="max_node2"
											value="60" placeholder="60" min="0">
		                            </div>
	                            </div>
	                            <div class="row">
		                            <div class="form-group col-md-12">
	                                    <span class="s-edit-name">待处理请求最大值：</span>
		                           <input type="number"
											class="form-control q-storage" id="max_request2"
											value="1000" placeholder="1000" min="0">
		                            </div>
	                            </div>
                       </section>
    </ul>
</div>

<div id="zookeeperDetail" style="display:none">
    <ul class="popWin">
        <li class="line-h-3">
            <span class="s-edit-name">服务名称：</span><span id="name2"></span>
        </li>
        <li class="line-h-3">
            <div class="param-set">
                <span class="mem-edit">Cpu:</span><span id="cpu2"></span>
            </div>
        </li>
        <li class="line-h-3">
            <div class="param-set">
                <span class="cpu-edit">内存(G):</span><span id="memory2"></span>
            </div>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">镜像:</span><span id="image2"></span>
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">存储:</span><span id="storage2"></span>
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
				"aTargets" : [ 0, 5 ]
			} ],
			"aaSorting" : [ [ 4, "desc" ] ]
		});
		$("#checkallbox").parent().removeClass("sorting_asc");
	</script>

</body>
</html>