<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>服务</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/ci.css" />
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/service.css" />
<script type="text/javascript"
	src="<%=path%>/js/service/service-detail.js"></script>
<script type="text/javascript"
	src="<%=path%>/js/service/laydate/laydate.js"></script>
<!-- <script src="https://192.168.110.141/static/gateone.js"></script> -->

</head>
<body>

	<jsp:include page="../frame/menu.jsp" flush="true">
		<jsp:param name="ci" value="" />
	</jsp:include>
	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="#"><i class="fa fa-home"></i>&nbsp;&nbsp;控制台</a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active">服务</li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active">${service.serviceName }</li>
					</ol>
				</div>
				<div class="contentMain">
<!-- 					<div id="gateone_container" style="position: relative; width: 60em; height: 30em;">
						<div id="gateone"></div>
					</div>
 -->
					<iframe src="https://192.168.247.129:4200" width="800px" height="500px"></iframe>
				</div>
			</div>
					
		</article>
	</div>
</body>
<!-- <script>
	window.onload = function() {
		// Initialize Gate One:
		GateOne.init({url: 'https://192.168.110.141'});
	}
</script>
 -->
</html>