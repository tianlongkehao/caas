<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>服务</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/storage.css"/>
   <%--  <script type="text/javascript" src="<%=path %>/js/storage/storage.js"></script> --%>
</head>
<body>
<jsp:include page="../frame/menu.jsp" flush="true">
    <jsp:param name="service" value=""/>
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
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active">${storage.storageName }</li>
                </ol>
                
            </div>
            <div class="contentMain">
				
				<aside class="aside-btn">
                    <div class="btns-group">
                        <span class="btn btn-defaults btn-white"><i class="icon-map-marker"></i><span class="ic_left">BONC PAAS</span></span>
                    </div>
                </aside>
				
				<section class="detail-succeed">
            <div class="icon-img"><div class="type-icon">
              <i class="fa fa-hdd-o"></i>
            </div></div>
            <ul class="succeed-content pull-left">
              <li >卷组名称：&nbsp;&nbsp;&nbsp;${storage.storageName }</li>
              
              <li>使用状态：&nbsp;&nbsp;&nbsp;
              			<c:if test="${storage.useType ==1 }">
                         	未使用
                        </c:if>
                        <c:if test="${storage.useType ==2 }">
                         	使用
                        </c:if>
              </li>
              
              <li>大&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;小：&nbsp;&nbsp;&nbsp;${storage.storageSize } M</li>
              <li>格&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;式：&nbsp;&nbsp;&nbsp;${storage.format }</li>
              <li>创建时间：&nbsp;&nbsp;&nbsp;${storage.createDate }</li>
            </ul>
            <div class="pull-right parameter-list">
               <canvas id="parameter" width="120px" height="120px"></canvas>
               <div class="param-text">
                 <p class="param-used"><span class="i"></span>已使用：<span id="sizeUsed">0</span>M</p>
                 <p class="param-sum"><span class="i"></span>总&nbsp;&nbsp;&nbsp;&nbsp;量：${storage.storageSize } M</p>
               </div>
               <input type="hidden" id="sizeUsedPersent" value="0.01">
               <input type="hidden" id="volumeSize" value="512">
            </div>
          </section>

            </div>
        </div>
    </article>
</div>



</body>
</html>