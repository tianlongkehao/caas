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
	                                    <option value="">-----请选择-----</option>
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
						<div class="container">
							<form id="nginxForm" class="">
								<div class="nginx-label">
									<span>upstream</span><input type="text" id="appNameAndNamespace" name="appNameAndNamespace" value="demo-testbonc" disabled>{
								</div>
								<div class="nginx-label col-md-offset-1">
									<select id="IsUpstreamIPHash" name="IsUpstreamIPHash">
										<option value="0">none</option>
										<option value="1">ip_hash</option>
									</select>;
								</div>
								<div id="nginx-sers"><div class="nginx-label col-md-offset-1">
									<span>server</span><input type="text" id="ipAndUpstreamPort" name="ipAndUpstreamPort" value="192.168.0.81:31586">;<i class="fa fa-plus addSerBtn"></i><i class="fa fa-minus delSerBtn"></i>
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
								
								
								<div class="k8s hide">
									<div class="nginxCon"><span>ServerName:</span><input type="text"  class="form-control"></div>
									<div class="nginxCon"><span>ListenPort:</span><input type="text"  class="form-control"></div>
									<div class="nginxCon"><span>RealServerPath:</span><input type="text" id="RealServerPath" name="RealServerPath" class="form-control"></div>
									<div class="nginxCon"><span>Namespace:</span><input type="text" id="Namespace" name="Namespace" class="form-control"></div>
									<div class="nginxCon"><span>AppName:</span><input type="text" id="AppName" name="AppName" class="form-control"></div>
									<div class="nginxCon"><span>Location:</span><input type="text"  class="form-control"></div>
									<div class="nginxCon"><span>ProxyRedirectSrcPath:</span><input type="text"  class="form-control"></div>
									<div class="nginxCon"><span>ProxyRedirectDestPath:</span><input type="text" id="ProxyRedirectDestPath" name="ProxyRedirectDestPath" class="form-control"></div>
									<div class="nginxCon"><span>UpstreamIPs:</span><input type="text" id="UpstreamIPs" name="UpstreamIPs" class="form-control"></div>
									<div class="nginxCon"><span>UpstreamPort:</span><input type="text" id="UpstreamPort" name="UpstreamPort" class="form-control"></div>
									
								</div>
								<div class="extrn hide">
									<div class="nginxCon"><span>ExternNginxServerName:</span><input type="text" id="ExternNginxServerName" name="ExternNginxServerName" class="form-control"></div>
									<div class="nginxCon"><span>ExternNginxListenPort:</span><input type="text" id="ExternNginxListenPort" name="ExternNginxListenPort" class="form-control"></div>
									<div class="nginxCon"><span>ExternNginxRealServerPath:</span><input type="text" id="ExternNginxRealServerPath" name="ExternNginxRealServerPath" class="form-control"></div>
									<div class="nginxCon"><span>ExternNginxNamespace:</span><input type="text" id="ExternNginxNamespace" name="ExternNginxNamespace" class="form-control"></div>
									<div class="nginxCon"><span>ExternNginxAppName:</span><input type="text" id="ExternNginxAppName" name="ExternNginxAppName" class="form-control"></div>
									<div class="nginxCon"><span>ExternNginxLocation:</span><input type="text" id="ExternNginxLocation" name="ExternNginxLocation" class="form-control"></div>
									<div class="nginxCon"><span>ExternNginxProxyRedirectSrcPath:</span><input type="text" id="ExternNginxProxyRedirectSrcPath" name="ExternNginxProxyRedirectSrcPath" class="form-control"></div>
									<div class="nginxCon"><span>ExternNginxProxyRedirectDestPath:</span><input type="text" id="ExternNginxProxyRedirectDestPath" name="ExternNginxProxyRedirectDestPath" class="form-control"></div>
									<div class="nginxCon"><span>ExternNginxUpstreamIPs:</span><input type="text" id="ExternNginxUpstreamIPs" name="ExternNginxUpstreamIPs" class="form-control"></div>
									
								</div>
								<div class="ishash hide"><div class="nginxCon"><span>IsUpstreamIPHash:</span><input type="text" id="IsUpstreamIPHash" name="IsUpstreamIPHash" class="form-control"></div></div>
								<div class="testTool hide">
									<div class="nginxCon"><span>NginxCmd:</span><input type="text" id="NginxCmd" name="NginxCmd" class="form-control"></div>
									
								</div>
							</form>
							<div class="nginxBtns">
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