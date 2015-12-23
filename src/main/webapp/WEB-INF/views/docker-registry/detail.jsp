<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>镜像中心</title>
    <%@include file="../frame/header.jsp"%>
    <link rel="stylesheet" type="text/css" href="/css/mod/docker-registry.css"/>
    <script type="text/javascript" src="/js/registry/registry.js"></script>
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
                </div>
                <div class="contentMain">
                    <div class="mirror-head">
                        <section class="images">
                            <div class="img-icon">
                                <img class="imgIcon" src="/images/image-1.png" style="max-width:80%;">
                            </div>
                        </section>
                        <section class="type-info">
                             <span class="title" imagename="tenxcloud/mysql">
                                 <span style="width:90%;display:inline-block;overflow: hidden;text-overflow: ellipsis;">${image.name }</span>
                            </span>
                            <span class="list-content">${image.remark }</span>
                            <span id="collectImage" class="btn btn-link fork"  title="点击收藏" style="text-decoration: none;">
                                <i class="fa fa-star-o star-style"></i>&nbsp;<span id="collectTxt">收藏</span>
                            </span>
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
                                <div class="detail-contents">
                                    <p>
                                        ${image.remark }
                                    </p>
                                </div>
                            </section>
                            <section class="infoTags hide">
                                <div class="detail-contents">
                                    <p><i class="fa fa-tag"></i> ${image.version }</p>
                                    <p><i class="fa fa-tag"></i> ${image.imageType }</p>
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
                            <div class="btn-block" style="height: 50px;">
                                <a href="javascript:void(0);" id="deployImage" class="btn-pull-deploy btn-primary btn btn-long-deploy" imageversion="${image.version}" imagename="${image.name}">部署镜像</a>
                            </div>
                            <div class="btn-block" style="height: 50px;">
                                <a href="javascript:void(0);" id="deleteImage" class="btn-defaulted btn btn-long-deploy" imageversion="${image.version}" imagename="${image.name}">删除镜像</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </article>
    </div>
</body>
