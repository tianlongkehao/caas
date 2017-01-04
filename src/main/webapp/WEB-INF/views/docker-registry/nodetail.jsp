<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>镜像中心</title>
    <%@include file="../frame/header.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/docker-registry.css"/>
    <script type="text/javascript" src="<%=path %>/js/registry/registry.js"></script>
</head>
<body>
	<jsp:include page="../frame/menu.jsp" flush="true" >
        <jsp:param name="registry" value="0" />
    </jsp:include>
    
	 <div class="page-container">
        <article>
            <div class="page-main">
                <div class="contentTitle">
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-home"></i>&nbsp;&nbsp;控制台</a></li>
                        <li><i class="fa fa-angle-right"></i></li>
                        <li class="active">镜像服务</li>
                    </ol>
                </div>
                <div class="contentMain">
                    <label>镜像不存在，可能已经被创建者删除！</label>
                    <img src="<%=path%>/images/noimage.png">
                	
                </div>
            </div>
        </article>
    </div>
</body>
