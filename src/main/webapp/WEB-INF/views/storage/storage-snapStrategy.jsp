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
										<a href="javascript:window.location.reload(true);"
											id="volReloadBtn" title="刷新"><i class="fa fa-repeat"></i></a>
									</div>
								</div>
								<div class="ibox-content">
									<table class="table table-stripped table-hover dataTables-example">
										<thead>
											<tr>
											    <th style="width:5%;text-indent:30px">
												<input type="checkbox" class="chkAll" id="checkallbox" />
												</th>
												<th style="width: 18%;">自动快照策略名称</th>
	                                            <th style="width: 18%;">关联磁盘数</th>
	                                            <th style="width: 18%;">执行磁盘数</th>
	                                            <th style="width: 18%;">创建时间</th>
	                                            <th style="width: 18%;">截止时间</th>
	                                            <th style="width: 10%;" class="item-operation">操作</th>
											</tr>
										</thead>
										<tbody id="strategyList">
										  <%-- <c:forEach items="${snapStrategies}" var="strategy">
											<tr strategyId=${strategy.id } strategyName="${strategy.name }" bindCount="${strategy.bindCount }"
											     keep=${strategy.keep } time="${strategy.time }" week="${strategy.week }">
												<td style="width: 18%;">${strategy.name }</td>
	                                            <td style="width: 18%;">${strategy.bindCount }</td>
	                                            <th style="width: 18%;">${strategy.excutingCount }</th>
	                                            <td style="width: 18%;">${strategy.createDate }</td>
	                                            <th style="width: 18%;">${strategy.endData }</th>
	                                            <td style="width: 10%;class="del-operation">
	                                            	<a onclick="snapStrategyEdit(this)" title="修改策略"><i class="fa fa-edit"></i></a>
	                                            	<a onclick="setStorage(this)" title="设置磁盘"><i class="fa fa-cog"></i></a>
	                                            	<a onclick="delOneStorage(this)" title="删除磁盘"><i class="fa fa-trash"></i></a>
	                                            </td>
											</tr>
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
            <input id="strategyname" class="form-control q-storage" type="text" value="">
        </li>
        <li class="line-h-3">
            <span class="s-edit-name">创建时间：<font color="red">*</font></span>
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
            <span class="s-edit-name">重复日期：<font color="red">*</font></span>
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
            <span class="s-edit-name">保留时间：<font color="red">*</font></span>
            <ul class="retainTimeInfo">
            	<li><label><input type="radio" name="retainTime" value="1">自定义时长<input id="keep" type="number" class="retainTimeInput" value="30">天</label><span>保留天数取值范围：1-35536</span></li>
            	<li><label><input type="radio" name="retainTime" value="2">永久保留</label></li>
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
	        			<th style="width: 25%;">磁盘名称</th>
	        			<th style="width: 25%;">磁盘大小(G)</th>
	        			<th style="width: 25%;">磁盘描述</th>
	        			<th style="width: 25%;">操作</th>
	        		</tr>
	        	</thead>
	        	<tbody id="not">
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
	        			<th style="width: 25%;">磁盘名称</th>
	        			<th style="width: 25%;">磁盘大小(G)</th>
	        			<th style="width: 25%;">磁盘描述</th>
	        			<th style="width: 25%;">操作</th>
	        		</tr>
	        	</thead>
	        	<tbody id="have">
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

</body>
</html>