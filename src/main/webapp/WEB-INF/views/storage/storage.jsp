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
                    <li class="active">存储和备份</li>
                </ol>
            </div>
            <div class="contentMain">
				
				<aside class="aside-btn">
                    <div class="btns-group">
                        <span class="btn btn-defaults btn-white"><i class="icon-map-marker"></i><span class="ic_left">BCM</span></span>
                    </div>
                </aside>
				
                <div class="caption clearfix">
                    <ul class="toolbox clearfix">
                        <li><a href="javascript:void(0);" id="storageReloadBtn"><i class="fa fa-repeat"></i></a></li>
                        <li><a href="<%=path %>/service/storage/add" id="storageAdd">创建</a></li>
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
                                            <th style="width: 15%;text-indent: 15px;">使用状态</th>
                                            <!-- <th style="width: 10%;text-indent: 20px;">格式</th> -->
                                            <th style="width: 15%;text-indent: 8px;">挂载点</th>
                                            <th style="width: 15%;text-indent: 10px;">大小</th>
                                            <th style="width: 12%;">创建时间</th>
                                            <th style="width: 20%;text-indent: 10px;">功能</th>
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
                                        <tbody id="storageList">
                                      <%--   <c:forEach items="${storages}" var="storage" >
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
                                                	<span>M</span>
                                                </td>
                                                <td style="width: 12%;">
                                                	${storage.createDate }
                                                </td>
                                                <td style="width:23%">	
                                                 <span class="btn btn-primary format formatStorage">格式化</span>
                                       			 <span class="btn btn-primary dilation dilatationStorage" storageId="${storage.id }" storageSize="${storage.storageSize }" storageName="${storage.storageName }">扩容</span>
                                       			 <span class="btn btn-primary deleteStorage deleteStorage" storageId="${storage.id }" >删除</span>
                                                </td>
                                            </tr>
                                        </c:forEach> --%>

                                        </tbody>
                                    </table>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
          		<div id="pagination"></div>
          		
            </div>
        </div>
    </article>
</div>

<div id="storageUpdate" style="display:none">
    <ul class="popWin">
        <li class="line-h-3">
            <span class="edit-name-c">名称：</span>
            <input id="upgradeStorageName" disabled="disabled" style="margin-top: 5px;width: 165px;" type="text" value="">
        </li>
        <li class="line-h-3">
            <div class="param-set">
                <span class="edit-name-c">存储大小：</span>
                <input type="radio" name="updateStorageSize" class="updateStorageSize" value="20480">20<span>G</span>
                <input type="radio" name="updateStorageSize" class="updateStorageSize" value="51200">50<span>G</span>
                <input type="radio" name="updateStorageSize" class="updateStorageSize" value="102400">100<span>G</span>
                <input type="radio" name="updateStorageSize" class="updateStorageSize" id="updatedefVolNum">
                	<input id="updatedefVol" style="width:50px; font-size:8px" placeholder="自定义大小"><span>G</span>
                <div>
                <span style="color:#1E90FF; padding-left:84px">总量:<span id="totalVol">${cur_user.vol_size}</span>G</span>
                <span style="color:#1E90FF; padding-left:15px">剩余:<span id="restVol">${leftstorage }</span>G 可用</span></div>
                <!-- <input id="storageSizeUpdateSlider" data-slider-id='storageSizeUpdateSliderData' type="text" data-slider-min="0" data-slider-max="1024" data-slider-step="1" />
                <input type="text" left="" value="250" id="storageSizeUpdateSlider_input" name="storageSize">
                <span>M</span> -->
                <!-- <span style="color: grey;">当前可用ram：<label id="leftram" ></label>M</span>-->
            </div>
        </li>
    </ul>
</div>

</body>
</html>