<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <script type="text/javascript" src="<%=path %>/js/cluster/cluster-topo.js"></script>
    <script type="text/javascript" src="<%=path %>/plugins/echarts-2.2.7/build/dist/echarts.js"></script>
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
                    <li class="active">集群拓扑</li>
                </ol>
            </div>
            <div class="contentMain">
				<div class="row">
					<div class="col-md-12">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h5>
									<i class="fa fa-map-marker" style="margin-right: 6px;"></i>集群拓扑
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
                    <c:if test="${user == 'user' }">
	                    <div class="searchFun" style="text-align: center; float: right; position:absolute;right:35px;top:66px;z-index:1100"
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
                   
                   <c:if test="${user == 'root' }">
                        <div class="searchFun" style="text-align: center; float: right; position:absolute;right:35px;top:66px;z-index:1100"
                                     align="right">
                                <label style="line-height: 35px">服务:</label>
                                <select name="search_service" id="search_service" onchange="searchService(this)"
                                        style="height: 30px;display: inline; width: 140px; border-radius: 5px; ">
                                </select>
                        </div>
                        
                        <div class="searchFun" style="text-align: center; float: right; position:absolute;right:226px;top:66px;z-index:1100"
                                 align="right">
                            <label style="line-height: 35px">租户:</label>
                            <select name="search_user" id="search_user" onchange="searchUser(this)"
                                    style="height: 30px;display: inline; width: 140px; border-radius: 5px; ">
                                    <option value="">-----请选择-----</option>
                                    <option value="all">All</option>
                                <c:forEach items="${userList}" var="user">
                                    <option value="${user.namespace }">${user.namespace }</option>
                                </c:forEach>
                            </select>
                       </div>
                   </c:if>
                   <input type = "hidden" id = "userType" value = "${user }">
               
                <div>
                    <div id="clusterResource_wrap" class="tab_wrap">
                        <div class="detail-info">
                        	<div class="info-list" id="resourceImg">
                        		<div id="clusterTopo" style="height:800px;"></div>
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
