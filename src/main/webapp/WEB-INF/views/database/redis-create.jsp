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
						            <div class="infoCred singlecfg">
										<span class="labelCred">主节点个数：</span> <input type="number"
											class="form-control conCred" id="masterNum" name=""
											value="3">
									</div>
									<div class="infoCred singlecfg">
										<p style="color:red; margin-left:18%">从节点个数与主节点个数一致</p>
									</div>
									<div class="infoCred">   
										<span class="labelCred">redis版本：</span> 
										<select class="form-control conCred" id="" name="" >
											<option value="1">3.01</option>
											<option value="2">2.8</option>
										</select>
									</div>
									<div class="infoCred">   
										<span class="labelCred">容器内存：</span><input type="number"
											class="form-control conCred" id="" name=""
											value="1" max="16" placeholder="容器内存小于16G，默认值1G">
									</div>
									<div class="infoCred">   
										<span class="labelCred">redis内存：</span><input type="number"
											class="form-control conCred" id="" name=""
											value="" placeholder="redis内存是容器内存的70%">
									</div>
									<div class="infoCred">
										<span class="labelCred">CPU个数：</span> <input type="number"
											class="form-control conCred" id="" name=""
											value="1" placeholder="1">
									</div>
									<div class="infoCred normal">
										<span class="labelCred">数据库个数：</span> <input type="number"
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
										<input type="button" class="btn btn-danger btn-sm btn-higher" value="高级">
									</div>
								</div>
								<div style="margin: 25px 15px;display:none" class="createRedisStep2">
									<div class="infoCred normal">
										<span class="labelCred">daemonSize：</span> <input type="number"
											class="form-control conCred" id="" name=""
											value="" placeholder="">
									</div>
									<div class="infoCred normal">
										<span class="labelCred">timeout：</span> <input type="number"
											class="form-control conCred" id="" name=""
											value="" placeholder="">
									</div>
									<div class="infoCred normal">
										<span class="labelCred">appendonly：</span> <input type="number"
											class="form-control conCred" id="" name=""
											value="" placeholder="">
									</div>
									<div class="infoCred normal">
										<span class="labelCred">aooebdstbc：</span> <input type="number"
											class="form-control conCred" id="" name=""
											value="" placeholder="">
									</div>
									<div class="infoCred normal">
										<span class="labelCred">cluster-enabled：</span> <input type="number"
											class="form-control conCred" id="" name=""
											value="" placeholder="">
									</div>
									<div class="infoCred normal">
										<span class="labelCred">dir：</span> <input type="number"
											class="form-control conCred" id="" name=""
											value="" placeholder="">
									</div>
									<div class="infoCred normal">
										<span class="labelCred">dbfilename：</span> <input type="number"
											class="form-control conCred" id="" name=""
											value="" placeholder="">
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