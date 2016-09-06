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
	<jsp:include page="../frame/menu.jsp" flush="true" >
        <jsp:param name="registry" value="0" />
    </jsp:include>
    
	 <div class="page-container">
        <article>
            <div class="page-main">
                <div class="contentTitle">
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-home"></i>&nbsp;&nbsp;控制台</a></li>
                        <li><i class="fa fa-angle-right"></i></li>
                        <li class="active">镜像服务</li>
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
                            <span class="list-content">${image.summary }</span>

                            <span class="list-content hide" id="contentArea">
                                <textarea id="desTextarea" name="description" style="padding:5px;">${image.summary }</textarea>
                                <span class="pull-right" id="desEditSpan">
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
                    </div>
                    <div class="row" style="padding-bottom: 50px;">
                        <div class="col-md-8">
                            <div class="table_list">
                                <div class="list_info INFO active">信息</div>
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
                                        <span class="btn btn-primary" id="cancelEdit"><i class="fa fa-times"></i> 取消</span>
                                        <span class="btn btn-primary" id="saveContent"><i class="fa fa-save"></i> 保存</span>
                                    </div>
                                    <br>
                                </div>
                            </section>
                            <section class="infoTags hide">
                                <div class="detail-contents">
                                    <p><i class="fa fa-tag"></i> ${image.version }</p>
                                </div>
                            </section>
                            <!-- <section class="infoInterface hide">
                                <div class="detail-contents">
                                    <p><b>容器端口：<span>3306/tcp</span></b></p>
                                    <p><b>数据存储卷：<span>/var/lib/mysql</span></b></p>
                                    <p><b><b>所需环境变量：</b></b></p>
                                    <table class="table table-hover" id="editTable" style="border:1px solid #eee;">
                                        <tbody>
                                        <tr style="background:#F5F6F6">
                                            <th>变量名</th>
                                            <th>默认值</th>
                                        </tr>
                                        <tr class="envobj-tr">
                                            <td>PATH</td>
                                            <td>/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin</td>
                                        </tr>
                                        <tr class="envobj-tr">
                                            <td>DEBIAN_FRONTEND</td>
                                            <td>noninteractive</td>
                                        </tr>
                                        <tr class="envobj-tr">
                                            <td>MYSQL_USER</td>
                                            <td>admin</td>
                                        </tr>
                                        <tr class="envobj-tr">
                                            <td>MYSQL_PASS</td>
                                            <td>**Random**</td>
                                        </tr>
                                        <tr class="envobj-tr">
                                            <td>REPLICATION_MASTER</td>
                                            <td>**False**</td>
                                        </tr>
                                        <tr class="envobj-tr">
                                            <td>REPLICATION_SLAVE</td>
                                            <td>**False**</td>
                                        </tr>
                                        <tr class="envobj-tr">
                                            <td>REPLICATION_USER</td>
                                            <td>replica</td>
                                        </tr>
                                        <tr class="envobj-tr">
                                            <td>REPLICATION_PASS</td>
                                            <td>replica</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </section>
                            <section class="infoDockerfile hide">
                                <div class="detail-contents">
                                </div>
                            </section> -->
                           
                        </div>
                        <div class="col-md-4">
                            <ul class="registry-attr">
                                <li class="li-row"><i class="fa_attr_s"></i>属性</li>
                                <li class="li-row" style="line-height: 35px;">
                                    <p><i class="fa_shuxinger"></i>贡献者:&nbsp;&nbsp;&nbsp;&nbsp; ${creator }</p>
                                    <p><i class="fa fa-star-o"></i><span>收藏数:&nbsp;&nbsp;&nbsp;&nbsp; ${favorUser } 个人收藏了该镜像</span></p>
                                    <p><i class="fa_datetime"></i><span>创建时间: ${image.createTime }</span></p>
                                </li>
                            </ul>
                            <div class="btn-block" style="height: 50px;" id="deployImage">
                                <a href="<%=path %>/service/add?imageName=${image.name}&imageVersion=${image.version}&imgID=${image.id}&resourceName=${image.resourceName}" class="btn-primary btn btn-long-deploy" imageversion="${image.version}" imagename="${image.name}" >部署镜像</a>
                            </div>
                            <div class="btn-block" style="height: 50px;" id="downloadImage">
                                <a href="<%=path %>/registry/downloadImage?imageName=${image.name}&imageVersion=${image.version}&imgID=${image.id}&resourceName=${image.resourceName}" id="doloadImage" class="btn-primary btn btn-long-deploy" imageversion="${image.version}" imagename="${image.name}" >导出镜像</a>
                            </div>
                            <c:if test="${editImage==1 }">
	                            <div class="btn-block " style="height: 50px; " id="deleteImage">
	                                <a href="javascript:void(0);"  class="btn btn-dangered btn-long-deploy" imageversion="${image.version}" imagename="${image.name}">删除镜像</a>
	                            </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </article>
    </div>
</body>
