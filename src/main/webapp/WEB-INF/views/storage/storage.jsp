<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>服务</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/storage.css"/>
    <script type="text/javascript" src="<%=path %>/js/storage/storage.js"></script>
</head>
<body>
<jsp:include page="../frame/menu.jsp" flush="true">
    <jsp:param name="ci" value=""/>
</jsp:include>

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                            id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" id="nav2">服务</li>
                     <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" id="nav2">存储和备份</li>
                </ol>
            </div>
            <div class="contentMain">
				
				<aside class="aside-btn">
                    <div class="btns-group">
                        <span class="btn btn-defaults btn-white"><i class="icon-map-marker"></i><span class="ic_left">BONC PAAS</span></span>
                    </div>
                </aside>
				
                <div class="caption clearfix">
                    <ul class="toolbox clearfix">
                        <li><a href="javascript:void(0);" id="storageReloadBtn"><i class="fa fa-repeat"></i></a></li>
                        <li><a href="<%=path %>/service/storage/add" id="storageAdd">创建</a></li>
                        <%-- <li><a href="<%=path %>/ci/addSource" id="ciAddSourceBtn">删除</a></li> --%>
                        <!-- <li><a href="<%=path %>/file/downloadTemplate?fileName=Dockerfile">下载dockerfile示例</a></li> -->
                    </ul>
                </div>
                <div class="itemTable">
                    <table class="table ci-table">
                        <thead>
                        <tr>
                            <th>
                                <div class="table-head">
                                    <table class="table" style="margin:0px;">
                                        <thead>
                                        <tr style="height:40px;">
                                            <th style="width: 15%;text-indent:30px;">名称</th>
                                            <th style="width: 10%;text-indent: 15px;">使用状态</th>
                                            <th style="width: 12%;text-indent: 20px;">格式</th>
                                            <th style="width: 10%;text-indent: 8px;">挂载点</th>
                                            <th style="width: 15%;text-indent: 10px;">大小</th>
                                            <th style="width: 12%;">创建时间</th>
                                            <th style="width: 23%;text-indent: 10px;">功能</th>
                                        </tr>
                                        </thead>
                                    </table>
                                </div>
                            </th>
                        </tr>
                        </thead>
                        <tbody id="projectsBody">
                        <tr>
                            <td>
                                <div class="content-table">
                                    <table class="table tables">
                                        <tbody id="ciList">
                                        <c:forEach items="${storages}" var="storage" >
                                            <tr class="ci-listTr" style="cursor:auto">
                                                <td style="width: 15%; text-indent:22px;" id = "storageName">
                                                	${storage.storageName}
                                                </td>
                                                <td style="width: 12%;" class="cStatusColumn">
                                                	<c:if test="${storage.useType ==0 }">
                                                		&nbsp;未使用
                                                	</c:if>
                                                	<c:if test="${storage.useType ==1 }">
                                                		使用
                                                	</c:if>
                                                </td>
                                                <td style="width: 10%;">
                                                	${storage.format }
                                                </td>
                                                <td style="width: 10%;">
                                                	${storage.mountPoint  }
                                                </td>
                                                <td style="width: 15%;">
                                                	${storage.storageSize } 
                                                </td>
                                                <td style="width: 12%;">
                                                	${storage.createDate }
                                                </td>
                                                <td style="width:23%">	
                                                 <span class="btn btn-primary format" id="formatStorage">格式化</span>
                                       			 <span class="btn btn-primary dilation" id="dilatationStorage" storageId = "${storage.id }" storageSize = "${storage.storageSize }" storageName = "${storage.storageName }">扩容</span>
                                       			 <span class="btn btn-primary deleteStorage" id="deleteStorage" storageId = "${storage.id }">删除</span>
                                                </td>
                                            </tr>
                                        </c:forEach>

                                        </tbody>
                                    </table>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
          		
          		
            </div>
        </div>
    </article>
</div>

</body>
</html>