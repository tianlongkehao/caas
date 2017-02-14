<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>镜像中心</title>
    <%@include file="../frame/header.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/docker-registry.css"/>
    <script type="text/javascript" src="<%=path %>/js/registry/registry.js"></script>
</head>
<body>
	<jsp:include page="../frame/bcm-menu.jsp" flush="true" >
        <jsp:param name="registry" value="0" />
    </jsp:include>

	 <div class="page-container">
        <article>
            <div class="page-main">
                <div class="contentTitle">
                    <ol class="breadcrumb">
                        <li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i><span id="nav1">&nbsp;&nbsp;控制台</span></a></li>
                        <li><i class="fa fa-angle-right"></i></li>
                        <li class="active">镜像</li>
                        <li><i class="fa fa-angle-right"></i></li>
                        <li class="active">${image.name} </li>
                    </ol>
                    <input type="hidden" id = "imageId" value = "${image.id }" name = "imageId">
                    <input type="hidden" id = "editImage" value = "${editImage }" name = "editImage">
                </div>
                <div class="contentMain">

                    <div class="mirror-head">
                        <section class="images">
                            <div class="img-icon">
                                <img class="imgIcon" src="<%=path %>/images/image-1.png" style="max-width:80%;">
                            </div>
                        </section>
                        <section class="type-info">
                             <span class="title" imagename="${image.name }">
                                 <span style="width:90%;display:inline-block;overflow: hidden;text-overflow: ellipsis;">${image.name }</span>
                                 <c:if test="${editImage==1 }">
                                 	<a id="desEdit" class="btn btn-link" style="font-size: 14px; display: block;"><i class="fa fa-edit"></i> 编辑</a>
                           		 </c:if>
                            </span>
                            <span class="list-content" id="showContent">${image.summary }</span>

                            <span class="list-content hide" id="contentArea">
                                <textarea id="desTextarea" name="description" style="padding:5px;height: 50px">${image.summary }</textarea>
                                <span class="pull-right" id="desEditSpan" style="height: 30px">
                                    <a id="desEditCancel" class="btn btn-link"><i class="fa fa-close"></i> 取消</a>
                                    <a id="desEditSave" class="btn btn-link"><i class="fa fa-save"></i> 保存</a>
                                </span>
                            </span>
							<c:if test="${whetherFavor==0 }">
	                            <span id="collectImage" class="btn btn-link fork"  title="点击收藏" style="text-decoration: none;">
	                                <i class="fa fa-star-o star-style"></i>&nbsp;<span id="collectTxt">收藏</span>
	                            </span>
                            </c:if>
                            <c:if test="${whetherFavor==1 }">
	                            <span id="collectImage" class="btn btn-link fork live"  title="点击收藏" style="text-decoration: none;">
	                                <i class="fa fa-star star-style"></i>&nbsp;<span id="collectTxt">已收藏</span>
	                            </span>
                            </c:if>
                            <c:if test="${image.imageType==2 }">
	                            <span class="btn btn-link lock">
	                                <i class="fa fa-lock"></i>
	                            </span>
	                        </c:if>
                        </section>
                        <section class="buttons-info">
                             <div class="" style="height: 50px;float:left;margin-left:10px" id="deployImage">
                                <a href="<%=path %>/service/add?imageName=${image.name}&imageVersion=${image.version}&imgID=${image.id}&resourceName=${image.resourceName}" class="btn-primary btn btn-color" imageversion="${image.version}" imagename="${image.name}" >部署镜像</a>
                            </div>
                            <div class="" style="height: 50px;float:left;margin-left:10px">
                               <a class="btn-primary btn btn-color downloadImage" imageversion="${image.version}" imagename="${image.name}" imgID="${image.id }" resourcename= "${image.resourceName}" id="doloadImage" >导出镜像</a>
                            </div>
                            <c:if test="${editImage==1 }">
	                            <div class="" style="height: 50px; float:left;margin-left:10px" id="deleteImage">
	                                <a href="javascript:void(0)" onclick="deleteImage(this)"  class="btn btn-dangered btn-color" imageid = "${image.id }" imageversion="${image.version}" imagename="${image.name}">删除镜像</a>
	                            </div>
                            </c:if>
                        </section>
                    </div>
                    <div class="row" style="padding-bottom: 50px;">
                        <div class="col-md-8">
                            <div class="table_list">
                                <div class="list_info INFO active">信息</div>
                                <div class="list_info ISERVICE">服务接口</div>
                                <div class="list_info IDOCKERFILE">Dockerfile</div>
                                <div class="list_info IHISTORY">构建历史</div>
                                <div class="list_info TAGS">版本</div>
                               <!--  <div id="serviceInfo" class="list_info INTERFACE">服务接口</div>
                                <div class="list_info Dockerfile">Dockerfile</div> -->
                            </div>
                            <section class="infoLog " style="margin-bottom: 30px">
                            	<c:if test="${editImage==1 }">
                                	<span id="detailEditSave" class="editSave-bottom btn btn-link"><i class="fa fa-edit"></i> 编辑</span>
                                </c:if>
                                <input type="hidden" id="remark" value="${image.remark }">
                                <div class="detail-contents">
                                    <script type="text/javascript">
                                    	$(function(){
		                                     var remark = $("#remark").val();
		                                     var mdContent = marked(remark);
		                                     $('.infoLog .detail-contents').html(mdContent);
                                    	});
                                    </script>
                                </div>
                                <div class="hide" id="contentEditor" style="text-indent: 0px;padding:0px 10px;">
                                    <textarea id="editor" style="display: none;">${image.remark }</textarea>
                                    <div align="right" style="margin-right:5px">
                                        <span class="btn btn-primary btn-color" id="cancelEdit"><i class="fa fa-times"></i> 取消</span>
                                        <span class="btn btn-primary btn-color" id="saveContent"><i class="fa fa-save"></i> 保存</span>
                                    </div>
                                    <br>
                                </div>
                            </section>

                            <section class="infoInterface hide">
                                <div class="detail-contents">
                                    <p><b>容器端口：</b></p>
                                    <table class="table table-hover" id="portTable" style="border:1px solid #eee;">
                                        <tbody>
                                        <tr style="background:#F5F6F6">
                                            <th>端口</th>
                                            <th>协议</th>
                                        </tr>
                                        <c:forEach items="${portList }" var = "port">
                                        <tr class="envobj-tr">
                                            <td>${port.containerPort }</td>
                                            <td>${port.protocol }</td>
                                        </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                    <!-- <p><b>数据存储卷：<span>/var/lib/mysql</span></b></p> -->
                                    <p><b><b>所需环境变量：</b></b></p>
                                    <table class="table table-hover" id="envTable" style="border:1px solid #eee;">
                                        <tbody>
                                        <tr style="background:#F5F6F6">
                                            <th>变量名</th>
                                            <th>默认值</th>
                                        </tr>
                                        <c:forEach items="${envList}" var="env">
                                        <tr class="envobj-tr">
                                            <td>${env.envKey }</td>
                                            <td class="envTableValue">${env.envValue }</td>
                                        </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </section>
                            <section class="infoDockerfile hide">
                                <div class="detail-contents">
                                	<c:if test="${dockerFileContent == null || dockerFileContent == ''}">
                                		 这个镜像不提供Dockerfile。
                                	</c:if>
                               		<c:if test="${dockerFileContent != null && dockerFileContent != ''}">
                                		 <textarea id="dockerFileContent">${dockerFileContent}</textarea>
                                	</c:if>
                                </div>
                            </section>
                            <section class="infoHistory hide">
                                <div class="detail-contents">
                                	<c:if test="${fn:length(ciHistory) == 0}">
                                		 此镜像无构建历史。
                                	</c:if>
                               		<c:if test="${fn:length(ciHistory) != 0}">
                                	<table class="table table-hover" id="historyTable" style="border:1px solid #eee;">
                                        <tbody>
                                        <tr style="background:#F5F6F6">
                                            <th>构建名称</th>
                                            <th>构建版本</th>
                                            <th>构建结果</th>
                                            <th>构建时长</th>
                                            <th>创建者</th>
                                            <th>构建时间</th>
                                        </tr>
                                        <c:forEach items="${ciHistory}" var="ciHistory">
                                        <tr class="envobj-tr">
                                            <td>${ciHistory.ciName }</td>
                                            <td>${ciHistory.ciVersion }</td>
                                            <c:if test="${ciHistory.constructResult ==1 }"><td>成功</td></c:if>
                                            <c:if test="${ciHistory.constructResult ==2 }"><td>失败</td></c:if>
                                            <c:if test="${ciHistory.constructResult ==3 }"><td>构建中</td></c:if>
                                            <td>${ciHistory.constructTime }s</td>
                                            <td>${ciHistory.creatorName }</td>
                                            <td>${ciHistory.constructDate }</td>
                                        </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                    </c:if>
                                </div>
                            </section>
                            <section class="infoTags hide">
                                <div class="detail-contents">
                                    <p><i class="fa fa-tag"></i> ${image.version }</p>
                                </div>
                            </section>

                        </div>
                        <div class="col-md-4">
                            <ul class="registry-attr">
                                <li class="li-row"><i class="fa_shuxinger"></i>贡献者:&nbsp;&nbsp;&nbsp;&nbsp; ${creator }</li>
                                <li class="li-row" style="line-height: 35px;">
                                	<c:if test="${image.imageType==1 }">
                                		<p><i class="fa_attr_s"></i><span>属性:&nbsp;&nbsp;&nbsp;&nbsp; 公有</span></p>
                                	</c:if>
                                	<c:if test="${image.imageType==2 }">
                                		<p><i class="fa_attr_s"></i><span>属性:&nbsp;&nbsp;&nbsp;&nbsp; 私有</span></p>
                                	</c:if>
                                    <p><i class="fa_attr_size"></i><span>大小:&nbsp;&nbsp;&nbsp;&nbsp; 112M</span></p>
                                    <p><i class="fa fa-star-o"></i><span>收藏数:&nbsp;&nbsp;&nbsp;&nbsp; ${favorUser } 个人收藏了该镜像</span></p>
                                    <c:if test="${image.exportCount == null || image.exportCount == ''}">
                                    	<p><i class="fa_attr_download"></i><span>导出数:&nbsp;&nbsp;&nbsp;&nbsp; 0 次导出了该镜像</span></p>
                                    </c:if>
                                    <c:if test="${image.exportCount != null && image.exportCount != ''}">
                                    	<p><i class="fa_attr_download"></i><span>导出数:&nbsp;&nbsp;&nbsp;&nbsp; ${image.exportCount } 次导出了该镜像</span></p>
                                    </c:if>
                                    <p><i class="fa_datetime"></i><span>创建时间: ${image.createDate }</span></p>
                                </li>
                            </ul>
                            <%-- <div class="btn-block" style="height: 50px;" id="deployImage">
                                <a href="<%=path %>/service/add?imageName=${image.name}&imageVersion=${image.version}&imgID=${image.id}&resourceName=${image.resourceName}" class="btn-primary btn btn-long-deploy" imageversion="${image.version}" imagename="${image.name}" >部署镜像</a>
                            </div>
                            <div class="btn-block" style="height: 50px;">
                               <a class="btn-primary btn btn-long-deploy downloadImage" imageversion="${image.version}" imagename="${image.name}" imgID="${image.id }" resourcename= "${image.resourceName}" id="doloadImage" >导出镜像</a>
                            </div>
                            <c:if test="${editImage==1 }">
	                            <div class="btn-block " style="height: 50px; " id="deleteImage">
	                                <a href="javascript:void(0)" onclick="deleteImage(this)"  class="btn btn-dangered btn-long-deploy" imageid = "${image.id }" imageversion="${image.version}" imagename="${image.name}">删除镜像</a>
	                            </div>
                            </c:if> --%>
                        </div>
                    </div>
                </div>
            </div>
        </article>
    </div>
</body>
