<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>快速构建</title>
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
                    <li class="active" id="nav2">创建备份</li>
                </ol>
            </div>
            <div class="contentMain">

                <div class="item-obj">
                    <div class="container">
                        <h4>创建存储卷</h4>

                        <form id="buildForm" name="buildForm" action="<%=path %>/service/storage/build" method="post">
                     
                            <div class="row">
                                <div class="form-group col-md-12">
                                    <label>名称：</label>
                                    <input id="storageName" name="storageName" class="form-control" type="text"
                                           required="" placeholder="3到5个字符，可由字母，数字，下划线组成">
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group col-md-12">
                                    <label for="codeUrl">存储大小</label>
                                    <input id="storageSize" name="storageSize" class="form-control" type="text">
                                </div>
                            </div>
                            <section class="registryinfo">
                                
                                <span class="reg-text">存储格式</span>
                               <span class="btns-group">
                                 <select id="format" name="format" class="reg-input" style="width:189px;">
                                 	  <option value="3">ext4</option>
                                     <option value="2">reiserfs</option>
                                     <option value="1">xfs</option>
                                 </select>
                               </span>
                            </section>

                            <br>

                            <div class="pull-right">
                                <span id="buildStorage" class="btn btn-primary">创建</span>
                            </div>
                        </form>
                    </div>
                </div>

            </div>
        </div>
    </article>
</div>

</body>
</html>