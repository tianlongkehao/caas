<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>用户</title>
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
                    <li><a href="#"><i class="fa fa-home"></i>&nbsp;&nbsp;控制台</a></li>
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
                                    <a href="<%=path %>/containers?0" data-permalink onclick="_permalink(this)">
                                      <span class="server-info-icon"><i class="fa_icon server_icon_1"></i>
                                        <span>服务个数：</span>
                                      </span>
                                      <span class="pull-right big yellow"><span>${usedServiceNum}</span>/<span>未限制${servServiceNum}</span>&nbsp;个</span>
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
                                    <a href="<%=path %>/ci?0" data-permalink onclick="permalink(this)">
                                          <span class="server-info-icon"><i class="fa_icon server_icon_2"></i>
                                            <span>实例个数：</span>
                                          </span>
                                        <span class="pull-right big blue"><span>${usedPodNum}</span>/<span>未限制${servPodNum}</span>&nbsp;个</span>
                                    </a>
                                </li>
                                <%--<li>
                                    <a href="<%=path %>/docker-registry?0" data-permalink onclick="_permalink(this)">
                                          <span class="server-info-icon"><i class="fa_icon server_icon_3"></i>
                                            <span>副本控制器：</span>
                                          </span>
                                        <span class="pull-right big yellow"><span>${usedControllerNum}</span>/<span>未限制${servControllerNum}</span>&nbsp;个</span>
                                    </a>
                                </li>--%>
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
                                            <span id="detailVolume">${usedstorage}</span>/<span id="totalVolume">${cur_user.vol_size}</span>（G）
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

</body>
</html>