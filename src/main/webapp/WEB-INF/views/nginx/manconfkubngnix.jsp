<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>nginx</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/service.css" />
<script type="text/javascript" src="<%=path%>/js/service/service.js"></script>
<style type="text/css">
.nginxCon{
	margin-top:10px;
}
.nginxCon span{
	float:left;
	line-height:34px;
	width:20%;
}
.nginxCon input{
	width:50%;
}	
.nginxSubBtn{
	margin-top:20px;
}
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
				<form id="nginxForm">
					<div>
						<select id="nginxType" class="form-control" style="width:50%">
							<option value="0">---请选择---</option>
							<option value="1">K8s</option>
							<option value="2">Extern</option>
							<option value="3">TestTool</option>
						</select>
					</div>
					<div class="k8s">
						<div class="nginxCon"><span>ServerName:</span><input type="text" id="serverName" name="serverName" class="form-control"></div>
						<div class="nginxCon"><span>ListenPort:</span><input type="text" id="ListenPort" name="ListenPort" class="form-control"></div>
						<div class="nginxCon"><span>RealServerPath:</span><input type="text" id="RealServerPath" name="RealServerPath" class="form-control"></div>
						<div class="nginxCon"><span>Namespace:</span><input type="text" id="Namespace" name="Namespace" class="form-control"></div>
						<div class="nginxCon"><span>AppName:</span><input type="text" id="AppName" name="AppName" class="form-control"></div>
						<div class="nginxCon"><span>Location:</span><input type="text" id="Location" name="Location" class="form-control"></div>
						<div class="nginxCon"><span>ProxyRedirectSrcPath:</span><input type="text" id="ProxyRedirectSrcPath" name="ProxyRedirectSrcPath" class="form-control"></div>
						<div class="nginxCon"><span>ProxyRedirectDestPath:</span><input type="text" id="ProxyRedirectDestPath" name="ProxyRedirectDestPath" class="form-control"></div>
						<div class="nginxCon"><span>UpstreamIPs:</span><input type="text" id="UpstreamIPs" name="UpstreamIPs" class="form-control"></div>
						<div class="nginxCon"><span>UpstreamPort:</span><input type="text" id="UpstreamPort" name="UpstreamPort" class="form-control"></div>
						
					</div>
					<div class="extrn">
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
					<div class="ishash"><div class="nginxCon"><span>IsUpstreamIPHash:</span><input type="text" id="IsUpstreamIPHash" name="IsUpstreamIPHash" class="form-control"></div></div>
					<div class="testTool">
						<div class="nginxCon"><span>NginxCmd:</span><input type="text" id="NginxCmd" name="NginxCmd" class="form-control"></div>
						
					</div>
				</form>
				<div class="nginxSubBtn"><input type="button" value="提交" class="btn btn-default"><div>
				
				</div>
			</div>
		</article>
	</div>
	
	<script type="text/javascript">
	$(document).ready(function(){
		$(".k8s").hide();
		$(".extrn").hide();
		$(".testTool").hide();
		$(".ishash").hide();
		$("#nginxType").change(function(){
			var nginxType = $("#nginxType").val();
			if(nginxType == 1){
				$(".k8s").show();
				$(".ishash").show();
				$(".extrn").hide();
				$(".testTool").hide();
			}else if(nginxType == 2){
				$(".k8s").hide();
				$(".ishash").show();
				$(".extrn").show();
				$(".testTool").hide();
			}else{
				$(".k8s").hide();
				$(".ishash").hide();
				$(".extrn").hide();
				$(".testTool").show();
			}
		});
	})
	
	</script>
	
</body>
</html> 