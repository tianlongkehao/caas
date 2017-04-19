<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/cluster.css"/>
   
    <script type="text/javascript" src="<%=path %>/plugins/echarts-2.2.7/build/dist/echarts.js"></script>
     <script type="text/javascript" src="<%=path %>/js/cluster/cluster-rate.js"></script>
</head>

<body>
<jsp:include page="../frame/bcm-menu.jsp" flush="true">
    <jsp:param name="cluster" value=""/>
</jsp:include>
<input type="hidden" id="checkedHosts">

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                            id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active">集群资源</li>
                </ol>
            </div>
            <div class="contentMain">
				<div class="row">
					<div class="col-md-12">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h5>
									<i class="fa fa-map-marker" style="margin-right: 6px;"></i>集群资源
								</h5>
								<div class="ibox-tools">
									<a href="javascript:window.location.reload(true);"
											title="刷新"><i class="fa fa-repeat"></i></a>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="ibox-content" style="padding:0px"></div>
				    <div class="searchFun" style="text-align: center; float: right; position:absolute;right:35px;top:66px;z-index:1100"
	                       align="right">
	                   <label style="line-height: 35px"></label>
	                   <select name="rateType" id="rateType" onchange="changeRateType(this)"
	                        style="height: 30px;display: inline; width: 160px; border-radius: 5px; ">
	                       <option value="day">天（资源使用情况）</option>
	                       <option value="week">周（资源使用情况）</option>
	                       <option value="month">月（资源使用情况）</option>
	                                
	                   </select>
	                </div>
                    <div id="mainDay" style="width: 98%;height:450px;margin-top:80px"></div>
                    <!-- <div id="mainWeek" style="width: 98%;height:400px;margin-top:20px"></div>
                    <div id="mainYear" style="width: 98%;height:400px;margin-top:20px"></div> -->
               
                <div>
                   
                </div>
            </div>
        </div>
    </article>
</div>
</body>
</html>
