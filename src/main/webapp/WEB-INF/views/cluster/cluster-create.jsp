<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<title>集群</title>
<%@include file="../frame/header.jsp" %>
<link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/cluster.css"/>
<script type="text/javascript" src="<%=path %>/js/cluster/cluster-create.js"></script>
</head>
<body>

<jsp:include page="../frame/bcm-menu.jsp" flush="true">
    <jsp:param name="user" value=""/>
</jsp:include>
<input type="hidden" id="checkedClusters">

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                            id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active"><a href="<%=path %>/cluster/management"><span id="nav2">集群管理</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active">创建节点</li>
                </ol>
            </div>
            <div class="contentMain">

                <div class="modalCrealApp">
                    <div class="steps-main">
                        <div class="progre">
                            <ul style="padding: 0 4rem;">
                                <li class="radius_step action"><span>1</span> 安装计划列表</li>
                                <li class="radius_step"><span>2</span> 提供SSH登陆凭证</li>
                                <li class="radius_step"><span>3</span> 检查报告</li>
                                <li class="radius_step"><span>4</span> 集群节点安装</li>
                                <!-- <li class="radius_step"><span>2</span> 提供SSH登陆凭证</li>
                                <li class="radius_step"><span>3</span> 配置集群节点</li>
                                <li class="radius_step"><span>4</span> 集群节点安装</li> -->
                            </ul>
                        </div>
                        <div class="step-inner" style="left: 0%;">
                            <%--选择集群节点--%>
                            <div class="host_step1">
                                <div class="blankapp" style="width: 90%; text-align: center">
                                    <div class="row" style="margin-left: 54px;">
                                        <div>
                                            <form class="search-group-inner"
                                                  style="width:100%;margin: 0 auto;position: relative;"
                                                  action="<%=path %>/cluster/getClusters" method="post">
                                                <input type="text" name="ipRange" class="form-control" placeholder="请输入要查找的IP"
                                                       style="width: 30%;display: inline" value="172.16.71.[10-22]">
                                                <span>~</span>
                                                <input type="text" name="ipRange" class="form-control"
                                                       placeholder="请输入要查找的IP"
                                                       style="width: 30%;display: inline;margin-right:10px" value="172.16.71.[171-175]">
                                                <button type="submit" class="btn btn-primary btn-color btn-send">查找</button>
                                            </form>
                                        </div>
                                        <div>
                                            <table class="table table-hover enabled" style="margin-top: 25px">
                                                <thead>
	                                                <tr style="text-align: center">
	                                                    <th style="width: 20%;text-indent:10px">IP</th>
	                                                    <th style="width: 15%;">Master</th>
	                                                    <th style="width: 15%;">Node</th>
	                                                    <th style="width: 15%;">Etcd</th>
	                                                    <th style="width: 15%;">yun源</th>
	                                                    <th style="width: 15%;">docker仓库</th>
	                                                </tr>
                                                </thead>
                                            </table>
                                            <div class="clusterNodesTable">
                                            	<table class="table table-hover enabled">
	                                                <tbody>
		                                                <tr>
		                                                    <td style="width: 20%;text-indent:10px">192.168.0.75</td>
		                                                    <td style="width: 15%;"><input type="checkbox"></td>
		                                                    <td style="width: 15%;"><input type="checkbox"></td>
		                                                    <td style="width: 15%;"><input type="checkbox"></td>
		                                                    <td style="width: 15%;"><input type="checkbox"></td>
		                                                    <td style="width: 15%;"><input type="checkbox"></td>
		                                                </tr>
		                                              
	                                                </tbody>
                                            </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="list-item-description" style="padding-top: 20px">
                                    <a href="<%=path %>/cluster/management"><button class="btn btn-default go_user"
                                                                  style="margin-right: 30px;">返回</button></a>
                                        <button class="nextTwo pull-right btn btn-primary btn-color pull_confirm"
                                              data-attr="tenxcloud/mysql">下一步</button>
                                </div>
                            </div>

                            <%--2.提供SSH登陆凭证--%>
                            <div class="host_step2">

                                <ul class="safeSet" style="margin-left: 100px">
                                    <li style="margin-bottom: 30px">
                                        <span>此安装需要对主机的根访问权限，此安装程序将通过SSH连接到您的主机，然后直接以root用户身份登录（登录所有主机，主机接收相同密码）。</span>
                                    </li>
                                    <li class="line-h-3">
                                        <div class="param-set">
                                            <span class="number-title">root：</span>
                                            <input type="text" class="form-control" placeholder="root" id="rootName" name="rootName" value="root"
                                                   style="width: 78%;display: inline">
                                        </div>
                                    </li>
                                    <li class="line-h-3">
                                        <div class="param-set">
                                            <span class="number-title">输入密码：</span>
                                            <input type="password" class="form-control" id="password" name="password"
                                                   style="width: 78%;display: inline">
                                        </div>
                                    </li>
                                    <li class="line-h-3">
                                        <div class="param-set">
                                            <span class="number-title">确认密码：</span>
                                            <input type="password" class="form-control" id="rePassword" name="rePassword"
                                                   style="width: 78%;display: inline">
                                        </div>
                                    </li>
                                    <li class="line-h-3">
                                        <div class="param-set">
                                            <span class="number-title">SSH端口：</span>
                                            <input type="text" class="form-control" id="port" name="port" placeholder="22" value="22"
                                                   style="width: 78%;display: inline">
                                        </div>
                                    </li>
                                </ul>
                                <div class="" style="padding-top: 100px">
                                    <button class="btn btn-default last_step" style="margin-right: 30px;">上一步</button>
                                        <button id="checkBtn"
                                              class="checkBtn pull-right btn btn-primary btn-color pull_confirm">下一步</button>
                                </div>
                            </div>

                            <%--3.配置集群节点--%>
                            <div class="host_step3" style="float:left">
                            	<div class="testResultInfo">
	                            	<span><strong>测试结果：未通过，无法安装，请完善信息后重新检测。</strong></span>
	                            	<input type="button" class="btn btn-danger" value="重新检测">
                            	</div>
								<div class="panel-group" id="accordion">
								    <div class="panel panel-default">
								        <div class="panel-heading">
								            <h4 class="panel-title">
								                <a data-toggle="collapse" data-parent="#accordion" 
								                href="#collapseOne">
								               1. SSH不可用主机
								                </a>
								            </h4>
								        </div>
								        <div id="collapseOne" class="panel-collapse collapse">
								            <div class="panel-body">
								                <input type="button" class="btn btn-info" value="192.168.0.75">
								                <input type="button" class="btn btn-info" value="192.168.0.77">
								            </div>
								        </div>
								    </div>
								    <div class="panel panel-default">
								        <div class="panel-heading">
								            <h4 class="panel-title">
								                <a data-toggle="collapse" data-parent="#accordion" 
								                href="#collapseTwo">
								               2. Master是高可用
								            </a>
								            </h4>
								        </div>
								        <div id="collapseTwo" class="panel-collapse collapse">
									        <div class="panel-body">
										        <div class="m-panel">
									           		<span class="noEditIp">192.168.0.2</span>
									           		<span class="noEditIp">192.168.0.2</span>
									           		<span class="noEditIp">192.168.0.2</span>
										        </div>
									            <div class="m-panel">
									           		<input type="text" class="form-control" style="width:200px;margin-top:10px" value="" placeholder="虚拟IP（必须填写）">
										        </div>
									        </div>
								        </div>
								    </div>
								    <div class="panel panel-default">
								        <div class="panel-heading">
								            <h4 class="panel-title">
								                <a data-toggle="collapse" data-parent="#accordion" 
								                href="#collapseThree">
								               3. etcd高可用（必须奇数）
								                </a>
								            </h4>
								        </div>
								        <div id="collapseThree" class="panel-collapse collapse">
								            <div class="panel-body">
								                <div class="etcdNodes float-left">
									                <span class="noEditIp">192.168.0.2<i class="fa fa-times-circle-o" onclick="deleteOneNode(this)"></i></span>
										           	<span class="noEditIp">192.168.0.2<i class="fa fa-times-circle-o" onclick="deleteOneNode(this)"></i></span>
										           	<span class="noEditIp">192.168.0.2<i class="fa fa-times-circle-o" onclick="deleteOneNode(this)"></i></span>
								                </div>
								                
								                <div class="btn-group addEtcdNodeBtn">
													<a type="button" class="btn btn-default dropdown-toggle" 
															data-toggle="dropdown">
														<i class="fa fa-plus"></i>添加	
														<span class="caret"></span>
													</a>
													<ul class="dropdown-menu" role="menu">
														<li><a onclick="addOneEtcdNode(this)" nodeIp="192.168.0.23">192.168.0.23</a></li>
														<li><a onclick="addOneEtcdNode(this)" nodeIp="192.168.0.24">192.168.0.24</a></li>
														<li><a onclick="addOneEtcdNode(this)" nodeIp="192.168.0.25">192.168.0.25</a></li>
													</ul>
												</div>
								            </div>
								        </div>
								    </div>
								    <div class="panel panel-default">
								        <div class="panel-heading">
								            <h4 class="panel-title">
								                <a data-toggle="collapse" data-parent="#accordion" 
								                href="#collapseFour">
								               4. YUM仓库未指定
								                </a>
								            </h4>
								        </div>
								        <div id="collapseFour" class="panel-collapse collapse ">
								            <div class="panel-body">
								                <div class="yumCon">
													<label>Name:</label>
													<input type="text" class="form-control" value="">
												</div>
												<div class="yumCon">
													<label>BaseURL:</label>
													<input type="text" class="form-control" value="">
												</div>
												<div class="yumCon">
													<label>Enabled:</label>
													<input type="text" class="form-control" value="">
												</div>
												<div class="yumCon">
													<label>GPGCheck:</label>
													<input type="text" class="form-control" value="">
												</div>
												<div class="yumCon">
													<label>GPGKey:</label>
													<input type="text" class="form-control" value="">
												</div>
								            </div>
								        </div>
								    </div>
								    <div class="panel panel-default">
								        <div class="panel-heading">
								            <h4 class="panel-title">
								                <a data-toggle="collapse" data-parent="#accordion" 
								                href="#collapseFive">
								               5. Docker仓库未指定
								            </a>
								            </h4>
								        </div>
								        <div id="collapseFive" class="panel-collapse collapse ">
									        <div class="panel-body">
									        	<input type="text" class="form-control" value="" placeholder="UserDockerRegistryURL（必须填写）">
									        </div>
								        </div>
								    </div>
								    <div class="panel panel-default">
								        <div class="panel-heading">
								            <h4 class="panel-title">
								                <a data-toggle="collapse" data-parent="#accordion" 
								                href="#collapseSix">
								               6. Node节点
								                </a>
								            </h4>
								        </div>
								        <div id="collapseSix" class="panel-collapse collapse ">
								            <div class="panel-body">
								                <span class="noEditIp">192.168.0.2</span>
									           	<span class="noEditIp">192.168.0.2</span>
									           	<span class="noEditIp">192.168.0.2</span> 
								            </div>
								        </div>
								    </div>
								</div>
								                            	
                                <div class="" style="padding-top: 30px">
                                    <button class="btn btn-default last_step" style="margin-right: 30px;">上一步</button>
                                        <button id="installBtn"
                                              class="installBtn pull-right btn btn-primary btn-color pull_confirm">安装</button>
                                </div>
                            </div>
                            <%--4.配置集群节点--%>
                            <div class="host_step4" style="height: auto;float: right">
                                <div>
                                    <table class="table table-hover enabled">
                                        <thead>
	                                        <tr>
	                                        	<th style="width: 15%;">步骤</th>
	                                            <th style="width: 50%;">安装进度</th>
	                                            <th style="width: 10%;">状态</th>
	                                            <th style="width: 26%;">操作</th>
	                                        </tr>
                                        </thead>
                                        <tbody id="divResult">
                                        	<tr>
	                                        	<td style="width: 15%;">1.安装YUM源</td>
	                                            <td style="width: 50%;" class="progressCon">
	                                            	<div class="progress progress-striped">
													    <div class="progress-bar progress-bar-success" role="progressbar"
													         aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
													         style="width: 90%;">
													        <span class="sr-only">90% 完成（成功）</span>
													    </div>
													</div>
	                                            </td>
	                                            <td style="width: 10%;"></td>
	                                            <td style="width: 26%;">
	                                            	<i>重置</i>
	                                            	<i>跳过</i>
	                                            	<i>日志</i>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                        	<td style="width: 15%;">2.安装必要RPM</td>
	                                            <td style="width: 50%;" class="progressCon">
	                                            	<div class="progress progress-striped">
													    <div class="progress-bar progress-bar-success" role="progressbar"
													         aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
													         style="width: 90%;">
													        <span class="sr-only">90% 完成（成功）</span>
													    </div>
													</div>
	                                            </td>
	                                            <td style="width: 10%;"></td>
	                                            <td style="width: 26%;">
	                                            	<i>重置</i>
	                                            	<i>跳过</i>
	                                            	<i>日志</i>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                        	<td style="width: 15%;">3.安装Docker仓库</td>
	                                            <td style="width: 50%;" class="progressCon">
	                                            	<div class="progress progress-striped">
													    <div class="progress-bar progress-bar-success" role="progressbar"
													         aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
													         style="width: 90%;">
													        <span class="sr-only">90% 完成（成功）</span>
													    </div>
													</div>
	                                            </td>
	                                            <td style="width: 10%;"></td>
	                                            <td style="width: 26%;">
	                                            	<i>重置</i>
	                                            	<i>跳过</i>
	                                            	<i>日志</i>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                        	<td style="width: 15%;">4.安装Etcd</td>
	                                            <td style="width: 50%;" class="progressCon">
	                                            	<div class="progress progress-striped">
													    <div class="progress-bar progress-bar-success" role="progressbar"
													         aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
													         style="width: 90%;">
													        <span class="sr-only">90% 完成（成功）</span>
													    </div>
													</div>
	                                            </td>
	                                            <td style="width: 10%;"></td>
	                                            <td style="width: 26%;">
	                                            	<i>重置</i>
	                                            	<i>跳过</i>
	                                            	<i>日志</i>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                        	<td style="width: 15%;">5.1 Master1安装</td>
	                                            <td style="width: 50%;" class="progressCon">
	                                            	<div class="progress progress-striped">
													    <div class="progress-bar progress-bar-success" role="progressbar"
													         aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
													         style="width: 90%;">
													        <span class="sr-only">90% 完成（成功）</span>
													    </div>
													</div>
	                                            </td>
	                                            <td style="width: 10%;"></td>
	                                            <td style="width: 26%;">
	                                            	<i>重置</i>
	                                            	<i>跳过</i>
	                                            	<i>日志</i>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                        	<td style="width: 15%;">5.2 Master2安装</td>
	                                            <td style="width: 50%;" class="progressCon">
	                                            	<div class="progress progress-striped">
													    <div class="progress-bar progress-bar-success" role="progressbar"
													         aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
													         style="width: 90%;">
													        <span class="sr-only">90% 完成（成功）</span>
													    </div>
													</div>
	                                            </td>
	                                            <td style="width: 10%;"></td>
	                                            <td style="width: 26%;">
	                                            	<i>重置</i>
	                                            	<i>跳过</i>
	                                            	<i>日志</i>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                        	<td style="width: 15%;">6.1 node1安装</td>
	                                            <td style="width: 50%;" class="progressCon">
	                                            	<div class="progress progress-striped">
													    <div class="progress-bar progress-bar-success" role="progressbar"
													         aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
													         style="width: 90%;">
													        <span class="sr-only">90% 完成（成功）</span>
													    </div>
													</div>
	                                            </td>
	                                            <td style="width: 10%;"></td>
	                                            <td style="width: 26%;">
	                                            	<i>重置</i>
	                                            	<i>跳过</i>
	                                            	<i>日志</i>
	                                            </td>
	                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="" style="padding-top: 100px">
                                    <button class="btn btn-default last_step" style="margin-right: 30px;">上一步</button>
                                    <a href="<%=path %>/cluster/management"><button id="finishBtn" class="pull-right btn btn-primary btn-color pull_confirm">完成</button></a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </article>
</div>

</body>
</html>
