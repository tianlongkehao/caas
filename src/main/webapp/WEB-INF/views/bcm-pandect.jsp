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

                    <div id="resourceinfo_wrap" class="tab_wrap">
                        <section class="container-count">
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
                                    <a href="<%=path %>/registry/0" data-permalink onclick="_permalink(this)">
                                          <span class="server-info-icon">
                                          	<img src="<%=path %>/images/applist3-gray.svg" class="pandect-icon" alt=""/>
                                            <span>镜像个数：</span>
                                          </span>
                                        <span class="pull-right big green"><span>${userResource.image_count}</span>&nbsp;个</span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                        </section>
                        
                        <section class="container-count logInfo">
                        <div class="padding">
                            <div class="row-title">最近操作</div>
                            <ul class="server-list">
                                <li>
                                    <span>创建了容器</span>
                                    <span class="right">10小时前</span>
                                </li>
                                <li>
                                    <span>删除了镜像</span>
                                    <span class="right">1天前</span>
                                </li>
                                <li>
                                    <span>更新了%%%</span>
                                    <span class="right">2017-01-06</span>
                                </li>
                            </ul>
                        </div>
                        </section>

                        <div class="detail-info">
                            <div class="info-list">
                                <table class="table" id="table-listing">
                                    <thead>
                                    <tr>
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
</script>>
</body>
</html>