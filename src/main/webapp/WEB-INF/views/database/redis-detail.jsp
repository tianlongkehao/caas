<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>redis</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/redis.css" />
<script type="text/javascript" src="<%=path%>/js/database/redis-create.js"></script>
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
						<li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class=""><a href="<%=path %>/RedisController/redis"><span id="nav2">数据库服务</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" style="width: 110px">redis</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="modalCrealApp">
						<div class="steps-main">
							<div class="progre hide">
								<ul style="padding: 0 4rem;">
									<li class="radius_step action" style="width: 100%">基本信息</li>
								</ul>
							</div>
							<div class="progre">
								<ul style="padding: 0 4rem;">
									<li class="radius_step action step1Btn"><span>1</span> 基本配置</li>
									<li class="radius_step step2Btn"><span>2</span> 高级配置</li>
								</ul>
							</div>

							<%--基本信息--%>
							<form id="buildForm" name="buildForm"
								action="<%=path%>/RedisController/createRedisService.do" method="post">
								<div style="margin: 25px 15px" class="createRedisStep1">
									<div class="infoCred">
										<span class="labelCred">集群名称：</span> <input type="text"
											class="form-control conCred" id="name" name="name"
											value="${redis.name }">
									</div>
									<div class="infoCred">
										<span class="labelCred">使用者：</span> <input type="text"
											class="form-control conCred" id="user" name="user"
											value="${redis.user }">
									</div>
									<div class="infoCred">
										<span class="labelCred">集群模式：</span>
										<select class="form-control conCred" id="clusterType" onchange="changeClusterType(this)">
											<c:if test = "${redis.nodeNum == 1 }">
												<option value="1">集群</option>
												<option value="2" selected>单机</option>
											</c:if>
											<c:if test = "${redis.nodeNum > 1 }">
												<option value="1" selected>集群</option>
												<option value="2">单机</option>
											</c:if>
										</select>
										<input type="hidden" id = "id" name ="id" value = "${redis.id }"/>
										<input type="hidden" id = "nodeNum" name ="nodeNum" value = "${redis.nodeNum }"/>
										<input type="hidden" id = "ram" name ="ram" value = "${redis.ram }"/>
									</div>
									<div class="infoCred">
										<c:if test = "${redis.nodeNum == 1 }">
											<p id="clusterTypeExplain" class="pExplain">单机版架构，CPU处理能力为单核</p>
										</c:if>
										<c:if test = "${redis.nodeNum > 1 }">
											<p id="clusterTypeExplain" class="pExplain">集群版架构，扩展CPU及QPS能力，支持8节点及16节点的集群架构</p>
										</c:if>
									</div>
									<div class="infoCred">
										<span class="labelCred">redis版本：</span>
										<select class="form-control conCred" id="version" name="version" >
											<option value="3.2.8-rb">3.2.8-rb</option>
										</select>
									</div>
									<div class="infoCred">
										<span class="labelCred">redis内存(G)：</span>
										<div id="redisMemSec">
											<select class="form-control conCred" id="nodeRam"  onchange="javascript:changeRamType(this)" select=2>
												<c:if test = "${redis.nodeNum == 1 }">
													<option value="1,1">1G 单机版</option>
													<option value="2,1">2G 单机版</option>
													<option value="4,1">4G 单机版</option>
													<option value="8,1">8G 单机版</option>
													<option value="16,1">16G 单机版</option>
													<option value="32,1">32G 单机版</option>
												</c:if>
												<c:if test = "${redis.nodeNum > 1 }">
													<option value="16,8">16G 集群版 8个节点</option>
													<option value="32,8">32G 集群版 8个节点</option>
													<option value="64,8">64G 集群版 8个节点</option>
													<option value="128,16">128G 集群版 16个节点</option>
												</c:if>
											</select>
										</div>
									</div>
									<div class="infoCred">
										<span class="labelCred">存储卷大小(G)：</span><input type="number"
											class="form-control conCred" id="storage" name="storage"
											value="${redis.storage }" placeholder="大于redis内存" min="0">
									</div>
									<div class="infoCred">
										<span class="labelCred">CPU(个)：</span> <input type="number"
											class="form-control conCred"
											value="2" placeholder="2" disabled>
										<input type="hidden" id = "cpu" name ="cpu" value = "2"/>
									</div>
									<div class="infoCred normal">
										<span class="labelCred">数据库(个)：</span> <input type="number"
											class="form-control conCred" id="databaseNum" name="databaseNum"
											value="${redis.databaseNum }" placeholder="16" min="0">
									</div>
									<div class="infoCred normal">
										<span class="labelCred">端口：</span> <input type="number"
											class="form-control conCred" id="port" name="port"
											value="6379" placeholder="${redis.port }">
									</div>
									<div class="infoCred">
										<span class="labelCred">内存消除策略：</span>
										<select class="form-control conCred" id="memoryPolicy" name="memoryPolicy">
											<option value="allkeys_lru">allkeys_lru</option>
											<option value="Volatile_lruG">Volatile_lruG</option>
											<option value="Volatile_random">Volatile_random</option>
											<option value="allkeys_random">allkeys_random</option>
											<option value="Volatile_ttl">Volatile_ttl</option>
											<option value="noeviction">noeviction</option>
										</select>
										<input type="hidden" id="memoryPolicyValue"  value="${redis.memoryPolicy }">
									</div>
									<div class="infoCred">
										<p class="pExplain">内存消除策略，默认allkeys-lru(优先移除主键中最近未使用的key), noeviction(不清除，超出最大内存报错),</p>
										<p class="pExplain">volatile-lru(优先移除过期键中最近未使用的key),allkeys-random(随机移除主键中某个key),</p>
										<p class="pExplain">volatile-random(随机移除过期键中某个key),volatile-ttl(过期键中，优先移除过期时间更早的key)</p>
									</div>
									<div class="infoCred">
										<input type="button" class="btn btn-danger btn-sm btn-higher" value="高级">
									</div>
								</div>
								<div style="margin: 25px 15px;display:none" class="createRedisStep2">
									<div class="infoCred normal">
										<span class="labelCred">是否开启守护进程：</span> <input type="text"
											class="form-control conCred" id="daemon" name="daemon"
											value="no" placeholder="" disabled>
										<input type="hidden" id = "daemon" name ="daemon" value = "0"/>
									</div>
									<div class="infoCred hide">
										<p class="pExplain">是否开启守护进程，默认：no 不开启</p>
									</div>
									<div class="infoCred normal">
										<span class="labelCred">开启AOF：</span> <input type="text"
											class="form-control conCred" id="aof" name="aof"
											value="yes" placeholder="" disabled>
										<input type="hidden" id = "aof" name ="aof" value = "1"/>
									</div>
									<div class="infoCred hide">
										<p class="pExplain">开启AOF</p>
									</div>
									<div class="infoCred normal">
										<span class="labelCred">同步AOF到磁盘：</span>
										<select class="form-control conCred" id="aofSync" name="aofSync">
											<option value="everysec">everysec</option>
											<option value="always">always</option>
											<option value="no">no</option>
										</select>
										<input type="hidden" id = "aofSyncValue" value = "${redis.aofSync }"/>
									</div>
									<div class="infoCred hide">
										<p class="pExplain">同步AOF到磁盘</p>
									</div>
									<div class="infoCred normal">
										<span class="labelCred">客户端超时时间(s)：</span> <input type="number"
											class="form-control conCred" id="clientTimeout" name="clientTimeout"
											value="${redis.clientTimeout }" placeholder="300" min="0">
									</div>
									<div class="infoCred hide">
										<p class="pExplain">客户端超时时间，单位：秒</p>
									</div>
									<div class="infoCred normal">
										<span class="labelCred">是否使用集群：</span>
										<select class="form-control conCred" id="clusterEnabled" name="" disabled>
											<option value="1">yes</option>
											<option value="2">no</option>
										</select>
									</div>
									<div class="infoCred hide">
										<p class="pExplain">是否使用集群，yes为集群版，no为单机版</p>
									</div>
									<div class="infoCred normal">
										<span class="labelCred">数据库存放目录：</span> <input type="text"
											class="form-control conCred" id="databaseDir" name="databaseDir"
											value="/opt/redis" disabled>
										<input type="hidden" id = "databaseDir" name ="databaseDir" value = "/opt/redis"/>
									</div>
									<div class="infoCred hide">
										<p class="pExplain">数据库存放目录</p>
									</div>
									<div class="infoCred normal">
										<span class="labelCred">数据库文件名：</span> <input type="text"
											class="form-control conCred" id="databaseFileName" name="databaseFileName"
											value="dump.rdb" placeholder="" disabled>
										<input type="hidden" id = "databaseFileName" name ="databaseFileName" value = "dump.rdb"/>
									</div>
									<div class="infoCred hide">
										<p class="pExplain">数据库文件名</p>
									</div>
									<div class="infoCred normal">
										<span class="labelCred">节点互连超时时间(ms)：</span> <input type="text"
											class="form-control conCred" id="nodeTimeout" name="nodeTimeout"
											value="15000" placeholder="" disabled>
										<input type="hidden" id = "nodeTimeout" name ="nodeTimeout" value = "15000"/>
									</div>
									<div class="infoCred hide">
										<p class="pExplain">节点互连超时时间</p>
									</div>
									<div class="infoCred normal">
										<span class="labelCred">节点配置文件：</span> <input type="text"
											class="form-control conCred" id="nodeConfigFile" name="nodeConfigFile"
											value="nodes.conf" placeholder="" disabled>
										<input type="hidden" id = "nodeConfigFile" name ="nodeConfigFile" value = "nodes.conf"/>
									</div>
									<div class="infoCred hide">
										<p class="pExplain">节点配置文件</p>
									</div>
									<div class="infoCred">
										<input type="button" class="btn btn-danger btn-sm btn-nohigher" value="返回基本配置">
									</div>
								</div>
								<c:if test="${redis.status==0 }">
									<div class="container" style="width:90%">
										<div class="list-item-description">
											<a href="<%=path %>/RedisController/redis"><span class="btn btn-default" style="margin-right: 30px;">返回</span></a>
											<span id="saveBtn" class="btn btn-primary btn-color pull-right">保存</span>
										</div>
									</div>
								</c:if>
							</form>
						</div>
					</div>
				</div>
			</div>
		</article>
	</div>
<script  type = "text/javascript" >
	if($("#nodeNum").val() == "1"){
		document.getElementById("clusterEnabled")[1].selected=true;
		switch($("#ram").val())
		{
		case "1":
			document.getElementById("nodeRam")[0].selected=true;
			break;
		case "2":
			document.getElementById("nodeRam")[1].selected=true;
			break;
		case "4":
			document.getElementById("nodeRam")[2].selected=true;
			break;
		case "8":
			document.getElementById("nodeRam")[3].selected=true;
			break;
		case "16":
			document.getElementById("nodeRam")[4].selected=true;
		  break;
		case "32":
			document.getElementById("nodeRam")[5].selected=true;
			break;
		}
	} else {
		document.getElementById("clusterEnabled")[0].selected=true;
		switch($("#ram").val())
		{
		case "16":
			document.getElementById("nodeRam")[0].selected=true;
			break;
		case "32":
			document.getElementById("nodeRam")[1].selected=true;
			break;
		case "64":
			document.getElementById("nodeRam")[2].selected=true;
			break;
		case "128":
			document.getElementById("nodeRam")[3].selected=true;
			break;
		}
	}
	switch($("#memoryPolicyValue").val()){
	case "allkeys_lru":
		document.getElementById("memoryPolicy")[0].selected=true;
		break;
	case "Volatile_lruG":
		document.getElementById("memoryPolicy")[1].selected=true;
		break;
	case "Volatile_random":
		document.getElementById("memoryPolicy")[2].selected=true;
		break;
	case "allkeys_random":
		document.getElementById("memoryPolicy")[3].selected=true;
		break;
	case "Volatile_ttl":
		document.getElementById("memoryPolicy")[4].selected=true;
		break;
	case "noeviction":
		document.getElementById("memoryPolicy")[5].selected=true;
		break;
	}
	switch($("#aofSyncValue").val()){
	case "everysec":
		document.getElementById("aofSync")[0].selected=true;
		break;
	case "always":
		document.getElementById("aofSync")[1].selected=true;
		break;
	case "no":
		document.getElementById("aofSync")[2].selected=true;
		break;
	}
</script>
</body>
</html>
