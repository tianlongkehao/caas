<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/cluster.css"/>
    <script type="text/javascript" src="<%=path %>/js/cluster/cluster.js"></script>
    <script type="text/javascript" src="<%=path %>/js/cluster/cluster-topo.js"></script>
    <script type="text/javascript" src="<%=path %>/plugins/echarts-2.2.7/build/dist/echarts.js"></script>
</head>

<body>
<jsp:include page="../frame/menu.jsp" flush="true">
    <jsp:param name="cluster" value=""/>
</jsp:include>
<input type="hidden" id="checkedHosts">

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                            id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" id="nav2">集群资源</li>
                </ol>
            </div>
            <div class="contentMain">

                <aside class="aside-btn">
                    <div class="btns-group">
                        <a id="clusterResource" class="Record action"><span class="btn btn-defaults btn-white"><span
                                class="ic_left">集群拓扑</span></span></a>
                    </div>
                </aside>
                <div class="caption clearfix" style="padding-bottom: 0px">
                    <ul class="toolbox clearfix hide">
                        <li><a id="updateCluster" style="cursor:pointer"><i
                                class="fa fa-repeat"></i></a></li>
                    </ul>
                    <form id="search_form" class="form-inline" action="<%=path %>/user/searchByCondition" method="post">
                        <div class="searchFun" style="float: left; text-align: center; margin: 0px 10px; float: right"
                             align="right">
                            <label style="line-height: 35px">租户:</label>
                            <select name="search_time" id="search_time" onchange="searchTime()"
                                    style="height: 30px;display: inline; width: 140px; border-radius: 5px; ">
                                <option value="0">ALL</option>
                                <option value="1">北京</option>
                                
                            </select>
                        </div>

                    </form>
                </div>
                <div>
                    <div id="clusterResource_wrap" class="tab_wrap">
                        <div class="detail-info">
                        	<div class="info-list" id="resourceImg">
                        		<div id="clusterTopo" style="height:500px;"></div>
                        		<!-- <div id ="can" style="width:100%; border:1px dashed #cccccc">
                        			<canvas id="canvas" width="1248px" height="450px"></canvas>
                        		</div> -->		
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
