<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>总览</title>
    <%@include file="frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/bcm-pandect.css"/>
</head>
<body>

<jsp:include page="frame/bcm-menu.jsp" flush="true">
    <jsp:param name="user" value=""/>
</jsp:include>
<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="<%=path %>/home"><i class="fa fa-home"></i><span id="nav1">&nbsp;&nbsp;控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active">总览</li>
                </ol>
            </div>
            <div class="contentMain">
                
                <div class="account_table" userID="${user.id }">
                	<c:if test="${cur_user.user_autority != 1}">
					<div class="serviceDetailInfo hide">
	                    <ul>
		                    <li class="serviceNumInfo blue">
		                      <a href="<%=path %>/service" data-permalink onclick="_permalink(this)">
		                    	<span class="server-info-icon">
                                     <img src="<%=path %>/images/service.svg" class="pandect-icon1" alt=""/>
                                     <span class="pandect-icon1Text">服务个数</span>
                                 </span><br>
                                 <span class="pull-right numberInfos"><span class="numberInfo">${usedServiceNum}</span>&nbsp;个</span>  
                               </a>  			
		                    </li>
		                    <li class="serviceNumInfo yellow">
		                      <a href="<%=path %>/service" data-permalink onclick="permalink(this)">
		                    	<span class="server-info-icon">
                                     <img src="<%=path %>/images/example.png" class="pandect-icon1" alt=""/>
                                     <span class="pandect-icon1Text">实例个数</span>
                                 </span><br>
                                 <span class="pull-right numberInfos"><span class="numberInfo">${usedPodNum}</span>&nbsp;个</span>  
                               </a>  			
		                    </li>
		                    <li class="serviceNumInfo litblue">
		                      <a href="<%=path %>/registry/1" data-permalink onclick="_permalink(this)">
		                    	<span class="server-info-icon">
                                     <img src="<%=path %>/images/applist3.svg" class="pandect-icon1" alt=""/>
                                     <span class="pandect-icon1Text">镜像个数</span>
                                 </span><br>
                                 <span class="pull-right numberInfos"><span class="numberInfo">${imageCount}</span>&nbsp;个</span>  
                               </a>  			
		                    </li>
	                    </ul>
                    </div>
                    <div id="resourceinfo_wrap" class="tab_wrap">
                        <section class="container-count ">
                        <div class="padding">
                            <div class="row-title">服务详情</div>
                            <ul class="server-list">
                                <li>
                                    <a href="<%=path %>/service" data-permalink onclick="_permalink(this)">
                                      <span class="server-info-icon">
                                        <img src="<%=path %>/images/service-gray.svg" class="pandect-icon" alt=""/>
                                        <span>服务个数：</span>
                                      </span>
                                      <span class="pull-right big yellow"><span>${usedServiceNum}</span>
                                      <%-- /<span>上限未限制${servServiceNum}</span> --%>
                                      &nbsp;个</span>
                                      	<%--<c:choose>
                                      		<c:when test="${servServiceNum==''}">
	                                      		<span class="pull-right big green"><span id="clusterNum"></span>&nbsp;个</span>
                                      		</c:when>
                                      		<c:otherwise>
		                                        <span class="pull-right big green"><span id="clusterNum1">${servServiceNum }</span>&nbsp;个</span>
                                      		</c:otherwise>
                                      	</c:choose>--%>
                                    </a>
                                </li>
                                <li>
                                    <a href="<%=path %>/service" data-permalink onclick="permalink(this)">
                                          <span class="server-info-icon"><i class="fa fa-puzzle-piece pandect-icon"></i>
                                            <span>实例个数：</span>
                                          </span>
                                        <span class="pull-right big blue"><span>${usedPodNum}</span>
                                        <%-- /<span>上限未限制${servPodNum}</span> --%>
                                        &nbsp;个</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="<%=path %>/registry/1" data-permalink onclick="_permalink(this)">
                                          <span class="server-info-icon">
                                          	<img src="<%=path %>/images/applist3-gray.svg" class="pandect-icon" alt=""/>
                                            <span>镜像个数：</span>
                                          </span>
                                        <span class="pull-right big litblue"><span>${imageCount}</span>&nbsp;个</span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                        </section>
                        
                        <section class="container-count logInfo">
                        <div class="padding">
                            <div class="row-title">最近操作</div>
                            <ul class="server-list">
	                  		  	<c:forEach items = "${list_Data}" var = "oprationRecord">
<%-- 									<tr>
										
		                                         <td style="width: 15%;height:40px;">
		                                              <fmt:formatDate value="${logService.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
		                                            </td>
									</tr> --%>
									<li style="height: 45px">
                                    	<span>${oprationRecord.value}</span>
                                    	<span class="right">
                                    		 <fmt:formatDate value="${oprationRecord.key}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    	</span>
                               		</li>
						  		</c:forEach>
                            </ul>
                        </div>
                        </section>

                        <div class="detail-info">
                            <div class="info-list">
                                <table class="table" id="table-listing">
                                    <thead>
                                    <tr class="table-title">
                                        <th colspan="6" class="detail-rows">资源使用情况</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr style="height:20px"></tr>
                                    <tr>
                                        <td style="width:15%">CPU（个）</td>
                                        <td style="width:25%">
                                            <div class="slider_bj">
                                                <div class="slider_block detailCpu" id="usedCpu"></div>
                                            </div>
                                        </td>
                                        <td style="width:10%">
                                            <span id="detailCpu">${usedCpuNum}</span>/<span id="totalCpu">${servCpuNum}</span>（个）
                                        	<%--<c:choose>
                                        		<c:when test="${usedCpuNum==''}">
			                                        <span id="detailCpu">&nbsp;</span>/<span id="totalCpu">&nbsp;</span>（个）
                                        		</c:when>
                                        		<c:otherwise>
			                                        <span id="detailCpu">${usedCpuNum}</span>/<span id="totalCpu">${servCpuNum}</span>（个）
                                        		</c:otherwise>
                                        	</c:choose>--%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>内存（G）</td>
                                        <td>
                                            <div class="slider_bj">
                                                <div class="slider_block detailMemory" id="usedMemory"></div>
                                            </div>
                                        </td>
                                        <td>
                                            <span id="detailMemory">${usedMemoryNum}</span>/<span id="totalMemory">${servMemoryNum}</span>（G）
                                        	<%--<c:choose>
                                        		<c:when test="${usedMemoryNum==''}">
			                                        <span id="detailMemory">&nbsp;</span>/<span id="totalMemory">&nbsp;</span>（G）
                                        		</c:when>
                                        		<c:otherwise>
			                                        <span id="detailMemory">${usedMemoryNum}</span>/<span id="totalMemory">${servMemoryNum}</span>（G）
                                        		</c:otherwise>
                                        	</c:choose>--%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>卷组容量（G）</td>
                                        <td>
                                            <div class="slider_bj">
                                                <div class="slider_block detailVolume" id="usedVolume"></div>
                                            </div>
                                        </td>
                                        <td>
                                            <span id="detailVolume">${usedstorage}</span>/<span id="totalVolume">${userResource.vol_size}</span>（G）
                                        </td>
                                    </tr>
                                    <%--<tr>
                                        <td>集群（个）</td>
                                        <td>
                                            <div class="slider_bj">
                                                <div class="slider_block detailHostingClusterNum"></div>
                                            </div>
                                        </td>
                                        <td><span id="detailHostingClusterNum">3</span>（个）</td>
                                        <td style="width:15%">主机（个）</td>
                                        <td style="width:25%">
                                            <div class="slider_bj">
                                                <div class="slider_block detailHostingNum"></div>
                                            </div>
                                        </td>
                                        <td style="width:10%"><span id="detailHostingNum">-</span>/5（个）</td>
                                    </tr>--%>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    </c:if>
                    
                    <c:if test="${cur_user.user_autority == 1}">
                    	
                        	<div class="detail-info">
	                            <div class="info-list">
	                                <table class="table" id="table-listing">
	                                    <thead>
	                                    <tr class="table-title">
	                                        <th colspan="6" class="detail-rows">集群资源使用情况</th>
	                                    </tr>
	                                    </thead>
	                                    <tbody>
	                                    <tr style="height:20px"></tr>
	                                    <tr>
	                                        <td style="width:15%">CPU（个）</td>
	                                        <td style="width:25%">
	                                            <div class="slider_bj">
	                                                <div class="slider_block detailCpu" id="usedCpu"></div>
	                                            </div>
	                                        </td>
	                                        <td style="width:10%">
	                                            <span id="detailCpu">${usedCpuNum}</span>/<span id="totalCpu">${servCpuNum}</span>（个）
	                                        </td>
	                                    </tr>
	                                    <tr>
	                                        <td>内存（G）</td>
	                                        <td>
	                                            <div class="slider_bj">
	                                                <div class="slider_block detailMemory" id="usedMemory"></div>
	                                            </div>
	                                        </td>
	                                        <td>
	                                            <span id="detailMemory">${usedMemoryNum}</span>/<span id="totalMemory">${servMemoryNum}</span>（G）
	                                        </td>
	                                    </tr>
	                                    <tr>
	                                        <td>卷组容量（G）</td>
	                                        <td>
	                                            <div class="slider_bj">
	                                                <div class="slider_block detailVolume" id="usedVolume"></div>
	                                            </div>
	                                        </td>
	                                        <td>
	                                            <span id="detailVolume">${usedstorage}</span>/<span id="totalVolume">${userResource.vol_size}</span>（G）
	                                        </td>
	                                    </tr>
	                                    </tbody>
	                                </table>
	                            </div>
	                        </div>
	                        
	                        <section class="container-count usersResourceInfo">
		                        <div class="padding">
		                            <div class="row-title">租户信息</div>
		                            <table class="table table-hover">
	                                    <thead>
	                                    	<tr class="usersInfoTit u-line">
	                                    		<th>租户名称</th>
	                                    		<th>实例个数</th>
	                                    		<th>服务个数</th>
	                                    		<th>使用情况</th>
	                                    		<th>CPU(个)</th>
	                                    		<th>内存(GB)</th>
	                                    		<th>存储(GB)</th>
	                                    	</tr>
	                                    </thead>
	                                    <tbody>
	                                    	<tr>
	                                    		<td rowspan="3">租户1</td>
	                                    		<td rowspan="3">22</td>
	                                    		<td rowspan="3">33</td>
	                                    		<td>总量</td>
	                                    		<td>33</td>
	                                    		<td>55</td>
	                                    		<td>77</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>未使用</td>
	                                    		<td>44</td>
	                                    		<td>33</td>
	                                    		<td>55</td>
	                                    	</tr>
	                                    	<tr class="u-line">
	                                    		<td>已使用</td>
	                                    		<td>44</td>
	                                    		<td>33</td>
	                                    		<td>55</td>
	                                    	</tr>
	                                    	
	                                    	<tr>
	                                    		<td rowspan="3">租户1</td>
	                                    		<td rowspan="3">22</td>
	                                    		<td rowspan="3">33</td>
	                                    		<td>总量</td>
	                                    		<td>33</td>
	                                    		<td>55</td>
	                                    		<td>77</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>未使用</td>
	                                    		<td>44</td>
	                                    		<td>33</td>
	                                    		<td>55</td>
	                                    	</tr>
	                                    	<tr class="u-line">
	                                    		<td>已使用</td>
	                                    		<td>44</td>
	                                    		<td>33</td>
	                                    		<td>55</td>
	                                    	</tr>
	                                    
	                                    </tbody>
	                                </table>
		                        </div>
		                    </section>
	                        
                        </c:if>
                </div>

            </div>
        </div>
    </article>
</div>
<script type="text/javascript">
$(function(){
	var userCpuPer = $("#detailCpu")[0].textContent/$("#totalCpu")[0].textContent*100+"%";
    $("#usedCpu")[0].style.width = userCpuPer;
    var userMemPer = $("#detailMemory")[0].textContent/$("#totalMemory")[0].textContent*100+"%";
    $("#usedMemory")[0].style.width = userMemPer;
    var userVolPer = $("#detailVolume")[0].textContent/$("#totalVolume")[0].textContent*100+"%";
    $("#usedVolume")[0].style.width = userVolPer;
})
</script>
</body>
</html>