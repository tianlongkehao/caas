<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>nginx</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/nginx.css" />
<script type="text/javascript" src="<%=path%>/js/nginx/k8snginxcfg.js"></script>
<style type="text/css">

</style>
</head>
<body>
	<jsp:include page="../frame/menu.jsp" flush="true">
		<jsp:param name="service" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2">nginx</li>
					</ol>
				</div>
				<div class="contentMain">
					<aside class="aside-btn">
	                    <div class="btns-group">
	                        <a id="clusterResource" class="Record action"><span class="btn btn-defaults btn-white"><span
	                                class="ic_left">代理配置</span></span></a>
	                    </div>
	                    
	                    <c:if test="${user == 'user' }">
		                    <div class="searchFun" style="float: left; text-align: center; margin: 0px 10px; margin-top:20px; float: right"
		                                 align="right">
		                            <label style="line-height: 35px">服务:</label>
		                            <select name="search_service" id="search_service" onchange="searchService(this)"
		                                    style="height: 30px;display: inline; width: 140px; border-radius: 5px; ">
		                                    <option value="">-----请选择-----</option>
		                                    <option value="all">All</option>
		                                <c:forEach items="${serviceTopo}" var="service">
		                                    <option value="${service.serviceName }">${service.serviceName }</option>
		                                </c:forEach>
		                            </select>
		                    </div>
	                   </c:if>
	                   
	                   <%-- <c:if test="${user == 'root' }"> --%>
	                        <div class="searchFun" style="float: left; text-align: center; margin: 0px 10px; margin-top:20px; float: right"
	                                     align="right">
	                                <label style="line-height: 35px">服务:</label>
	                                <select name="search_service" id="search_service" onchange="searchService(this)"
	                                        style="height: 30px;display: inline; width: 140px; border-radius: 5px; ">
	                                </select>
	                        </div>
	                        
	                        <div class="searchFun" style="float: left; text-align: center; margin: 0px 10px; margin-top:20px; float: right"
	                                 align="right">
	                            <label style="line-height: 35px">用户:</label>
	                            <select name="search_user" id="search_user" onchange="searchUser(this)"
	                                    style="height: 30px;display: inline; width: 140px; border-radius: 5px; ">
	                                    <option value="all">All</option>
	                                <c:forEach items="${userList}" var="user">
	                                    <option value="${user.namespace }">${user.namespace }</option>
	                                </c:forEach>
	                            </select>
	                       </div>
	                   <%-- </c:if> --%>
	                   <input type = "hidden" id = "userType" value = "${user }">
	                </aside>
					<div class="item-obj">
						<div class="addNginxCfg">
							<div class="row addNginxStepRow ">
									<div class="col-sm-12">
										<div class="ibox float-e-margins">
											<div class="ibox-title">
												<h5>{namespace}+{serviceName}</h5>
												<div class="ibox-tools">
													<a class="">
						                                <i class="fa fa-save"></i>
						                            </a>
													<a class="collapse-link">
						                                <i class="fa fa-chevron-up"></i>
						                            </a>
												</div>
											</div>
											<div class="ibox-content">
												<form class="nginxForm">
													<div class="nginx-label">
														<span>upstream</span><input type="text" class="appNameAndNamespace" name="appNameAndNamespace" value="demo-testbonc" disabled>{
													</div>
													<div class="nginx-label col-md-offset-1">
														<select id="IsUpstreamIPHash" name="IsUpstreamIPHash">
															<option value="0">none</option>
															<option value="1">ip_hash</option>
														</select>;
													</div>
													<div id="nginx-sers"><div class="nginx-label col-md-offset-1">
														<span>server</span><input type="text" class="ipAndUpstreamPort" name="ipAndUpstreamPort" value="192.168.0.81:31586">;<i class="fa fa-plus addSerBtn"></i><i class="fa fa-minus delSerBtn"></i>
													</div></div>
													<div class="nginx-label">
														<span>}</span>
													</div>
													<div class="nginx-label">
														<span>server</span>{
													</div>
													<div class="nginx-label col-md-offset-1">
														<span>listen</span><input type="text" id="ListenPort" name="ListenPort" value="80">;
													</div>
													<div class="nginx-label col-md-offset-1">
														<span>server_name</span><input type="text" id="serverName"  name="serverName" value="192.168.252.133">;
													</div>
													<div class="nginx-label col-md-offset-1">
														<span>location</span><input type="text" id="Location" name="Location" value="/testbonc/demo">{
													</div>
													<div class="nginx-label col-md-offset-2">
														<span>proxy_pass</span><input type="text" id="proxy_pass" name="" value="" disabled><input type="text" id="" name="" value="/demo/">;
													</div>
													<div class="nginx-label col-md-offset-2">
														<span>proxy_set_header Host $host:</span><input type="text" class="sameToListenPort" name="" value="" disabled>;
													</div>
													<div class="nginx-label col-md-offset-2">
														<span>proxy_set_header X-Real-IP $remote_addr:</span><input type="text" class="sameToListenPort" id="" name="" value="" disabled>;
													</div>
													<div class="nginx-label col-md-offset-2">
														<span>proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for</span>;
													</div>
													<div class="nginx-label col-md-offset-2">
														<span>proxy_redirect</span><input type="text" id="ProxyRedirectSrcPath" name="ProxyRedirectSrcPath" value="http://demo:8080/demo">
														<input type="text" id="ProxyRedirectDestPath" name="ProxyRedirectDestPath" value="/testbonc/demo">;
													</div>
													<div class="nginx-label col-md-offset-1">
														<span>}</span>
													</div>
													<div class="nginx-label">
														<span>}</span>
													</div>
												</form>
											</div><!-- ibox-content -->
										</div><!-- ibox -->
									</div>
								</div><!-- addNginxStepRow -->
							
							<div class="nginxBtns hide">
                                 <a href="<%=path %>/nginx/list"><span class="btn btn-default go_namespaceList" style="margin-right: 30px;">返回</span></a>
                                 <span class="saveInfo pull-right btn btn-primary pull_confirm" id="saveNginxInfoBtn">保存</span>
                            </div>
						</div>
                    </div>
				</div>
			</div>
		</article>
	</div>
	
	<script type="text/javascript">
	$(document).ready(function(){
		
	})
	
	</script>
	
</body>
</html> 