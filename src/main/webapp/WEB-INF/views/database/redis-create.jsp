<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>redis</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/redis.css" />
<script type="text/javascript"
	src="<%=path%>/js/database/redis.js"></script>
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
						<li class=""><a href="<%=path %>/db/redis"><span id="nav2">数据库服务</span></a></li>
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
								action="<%=path%>/service/storage/build" method="post">
								<div style="margin: 25px 15px" class="createRedisStep1">
									<div class="infoCred">
										<span class="labelCred">集群名称：</span> <input type="text"
											class="form-control conCred" id="" name=""
											value="">
									</div>
									<div class="infoCred">
										<span class="labelCred">使用者：</span> <input type="text"
											class="form-control conCred" id="" name=""
											value="">
									</div>
								    <div class="infoCred">
						                <span class="labelCred">集群模式：</span> 
						                <select class="form-control conCred" id="clusterType" name="" onchange="changeClusterType(this)">
						                    <option value="1">集群</option>
						                    <option value="2">单机</option>
						                </select>
						            </div>
									<div class="infoCred">
										<p id="clusterTypeExplain" class="pExplain">集群版架构，扩展CPU及QPS能力，支持8节点及16节点的集群架构</p>
									</div>
									<div class="infoCred">   
										<span class="labelCred">redis版本：</span> 
										<select class="form-control conCred" id="" name="" >
											<option value="1">3.01</option>
											<option value="2">2.8</option>
										</select>
									</div>
									<div class="infoCred">   
										<span class="labelCred">redis内存(G)：</span>
										<div id="redisMemSec">
											<select class="form-control conCred" id="" name="" >
												<option value="16">16G 集群版 8个节点</option>
												<option value="32">32G 集群版 8个节点</option>
												<option value="64">64G 集群版 8个节点</option>
												<option value="128">128G 集群版 16个节点</option>
											</select>
										</div>
									</div>
									<div class="infoCred">   
										<span class="labelCred">存储卷大小(G)：</span><input type="number"
											class="form-control conCred" id="" name=""
											value="" placeholder="大于redis内存">
									</div>
									<div class="infoCred">
										<span class="labelCred">CPU(个)：</span> <input type="number"
											class="form-control conCred" id="" name=""
											value="2" placeholder="2" disabled>
									</div>
									<div class="infoCred normal">
										<span class="labelCred">数据库(个)：</span> <input type="number"
											class="form-control conCred" id="" name=""
											value="16" placeholder="16">
									</div>
									<div class="infoCred normal">
										<span class="labelCred">端口：</span> <input type="number"
											class="form-control conCred" id="" name=""
											value="6379" placeholder="6379">
									</div>
									<div class="infoCred">   
										<span class="labelCred">内存消除策略：</span> 
										<select class="form-control conCred" id="" name="" >
											<option value="1">allkeys_lru</option>
											<option value="2">Volatile_lruG</option>
											<option value="1">Volatile_random</option>
											<option value="2">allkeys_random</option>
											<option value="1">Volatile_ttl</option>
											<option value="2">noeviction</option>
										</select>
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
											class="form-control conCred" id="" name=""
											value="no" placeholder="" disabled>
									</div>
									<div class="infoCred hide">
										<p class="pExplain">是否开启守护进程，默认：no 不开启</p>
									</div>
									<div class="infoCred normal">
										<span class="labelCred">开启AOF：</span> <input type="text"
											class="form-control conCred" id="" name=""
											value="yes" placeholder="" disabled>
									</div>
									<div class="infoCred hide">
										<p class="pExplain">开启AOF</p>
									</div>
									<div class="infoCred normal">
										<span class="labelCred">同步AOF到磁盘：</span> 
										<select class="form-control conCred" id="" name="">
											<option value="1">everysec</option>
											<option value="2">always</option>
											<option value="3">no</option>
										</select>
									</div>
									<div class="infoCred hide">
										<p class="pExplain">同步AOF到磁盘</p>
									</div>
									<div class="infoCred normal">
										<span class="labelCred">客户端超时时间(s)：</span> <input type="number"
											class="form-control conCred" id="" name=""
											value="300" placeholder="">
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
											class="form-control conCred" id="" name=""
											value="/opt/redis" disabled>
									</div>
									<div class="infoCred hide">
										<p class="pExplain">数据库存放目录</p>
									</div>
									<div class="infoCred normal">
										<span class="labelCred">数据库文件名：</span> <input type="text"
											class="form-control conCred" id="" name=""
											value="dump.rdb" placeholder="" disabled>
									</div>
									<div class="infoCred hide">
										<p class="pExplain">数据库文件名</p>
									</div>
									<div class="infoCred normal">
										<span class="labelCred">节点互连超时时间(ms)：</span> <input type="text"
											class="form-control conCred" id="" name=""
											value="15000" placeholder="" disabled>
									</div>
									<div class="infoCred hide">
										<p class="pExplain">节点互连超时时间</p>
									</div>
									<div class="infoCred normal">
										<span class="labelCred">节点配置文件：</span> <input type="text"
											class="form-control conCred" id="" name=""
											value="nodes.conf" placeholder="" disabled>
									</div>
									<div class="infoCred hide">
										<p class="pExplain">节点配置文件</p>
									</div>
									<div class="infoCred"> 
										<input type="button" class="btn btn-danger btn-sm btn-nohigher" value="返回基本配置">
									</div>
								</div>
								<div class="container" style="width:90%">
									<div class="list-item-description">
                                        <a href="<%=path %>/db/redis"><span class="btn btn-default" style="margin-right: 30px;">返回</span></a>
                                        <span id="buildStorage" class="btn btn-primary btn-color pull-right">保存</span>
                                    </div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</article>
	</div>

</body>
</html>