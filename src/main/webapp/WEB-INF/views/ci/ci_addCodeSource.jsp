<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>快速构建</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/ci.css"/>
    <script type="text/javascript" src="<%=path %>/js/ci/ci_quick.js"></script>
</head>
<body>

<jsp:include page="../frame/bcm-menu.jsp" flush="true">
    <jsp:param name="ci" value=""/>
</jsp:include>

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                            id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active"><a href="<%=path %>/ci"><span id="nav2">镜像构建</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active">快速构建</li>
                </ol>
            </div>
            <div class="contentMain">

                <div class="item-obj">
                    <div class="container">
                        <h4>快速构建</h4>

                        <form id="buildForm" name="buildForm" action="<%=path %>/ci/addCodeResourceCi.do" method="post" enctype="multipart/form-data">
                            <div class="row depot-name">
								<div class="form-group col-md-7">
									<label>基础镜像</label>
									<div class="ci-quick-baseImage">
										<select id="baseImageName" name="baseImageName">
											<c:forEach var = "image" items = "${baseImage }">
												<option type="text" value="${image.name }">${image.name }</option>
											</c:forEach>
										</select> : 
										<select id="baseImageId" name="baseImageId">
										
										</select>
										<%-- <input id="baseImageName" name="baseImageName" type="text" 
                                               value="${docker_regisgtry_address }/bonc/tomcat" style="width:218px"> :
                                        <input id="baseImageVersion" name="baseImageVersion" type="text" value="8-jre7"> --%> 
									</div>
								</div>
								<div class="form-group col-md-7">
                            		<label>镜像名称</label>

                                    <div class="">
                                        <span class="name-note">${username } /&nbsp;</span>
                                        <input name="imgNameFirst" id = "imgNameFirst" type="hidden" value="${username }">
                                        <input id="imgNameLast" name="imgNameLast" type="text" class="name-input"
                                               value=""> :
                                        <input id="imgNameVersion" name="imgNameVersion" type="text" value="latest"
                                               class="name-input">
                                    </div>
                                </div>
                                <div class="form-group col-md-5">
                                    <label>性质</label>

                                    <div>
                                        <span class="btn-groups btn-imageType">
                                            <a case="public" class="btns" onclick="javascript:$('#imgType').val(1)">公有</a>
                                            <a case="private" class="btns active" onclick="javascript:$('#imgType').val(2)">私有</a>
                                        </span>
                                        <input type="hidden" name="imgType" id="imgType" value="2">
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group col-md-12">
                                    <label>简介</label>
                                    <input id="description" name="description" class="form-control" type="text"
                                           required="">
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group col-md-12">
                                    <label for="sourceCode">上传代码</label>
                                    <input type="file" name="sourceCode" id="sourceCode">
                                </div>
                            </div>
                            <section class="registryinfo">
                                <table class="table registry">
                                    <tbody>
                                    <tr>
                                        <td class="first-info"><b style="font-weight: 500;">基本信息</b></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="reg-text">项目名称</span>
                                            <input id="projectName" name="projectName" type="text" value=""
                                                   class="reg-input">
                                        </td>
                                       <!--  <td>
                                            <span class="reg-text">上传DockerFile</span> &nbsp;
                                            <input id="dockerFile" name="dockerFile" type="file" class="dockerfileinput">
                                        </td> -->
                                    </tr>
                                    </tbody>
                                </table>
                            </section>
                            <br>

                            <div class="pull-right">
                                <span id="buildBtn" class="btn btn-primary btn-color">创建</span>
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