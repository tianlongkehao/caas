<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>代码构建</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/core/jquery-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/ci.css"/>
    <script type="text/javascript" src="<%=path %>/js/ci/ci_add.js"></script>
    
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
                    <li class="active">代码构建</li>
                </ol>
            </div>
            <div class="contentMain">
                <div class="item-obj">
                    <div class="container">
                        <h4>代码构建</h4>

                        <form id="buildForm" name="buildForm" action="<%=path %>/ci/addCodeCi.do" method="post">
                        	<section class="registryinfo">
                        		<div class="row depot-name">
	                                <div class=" col-md-12">
	                                    <label class="c-project-tit">项目名称</label>
	                                    <input id="projectName" autocomplete = 'off' name="projectName" class="form-control c-project-con" type="text"
	                                           required="">
	                                </div>
	                            </div>
	                            <div class="row">
	                                <div class="form-group col-md-12">
	                                    <label class="c-project-tit">描述</label>
	                                    <textarea id="description" autocomplete = 'off' name="description" class="form-control c-project-con" type="text"
	                                           required="" row="5"></textarea>
	                                </div>
	                            </div>
	                            <div class="row">
	                                <div class="form-group col-md-12">
	                                    <label class="c-project-tit">JDK</label>
	                                    <select id="jdk-version" autocomplete = 'off' name="jdkVersion" class="form-control c-project-con">
	                                       <option value="">system</option>
	                                       <c:forEach items="${jdkList}" var="jdk">
	                                    	  <option value="${jdk.version }">${jdk.version }</option>
	                                       </c:forEach>
	                                    </select>
	                                </div>
	                            </div>
                        	</section>
                        	
                            <h4 class="c-title">代码管理</h4>
                            <section class="registryinfo">
                            	<div class="row">
	                            	<div class="form-group col-md-12">
		                                <label class="c-project-tit">代码库类型</label>
		                                <select id="codeType" autocomplete = 'off' name="codeType" class="form-control c-project-con" >
		                                	<option value="0" >-none-</option>
		                                    <option value="1">git</option>
		                                    <option value="2">svn</option>
		                                </select>
		                            </div>
	                            </div>
	                            <div class="row git-config">
	                            	<div class="form-group col-md-12">
		                                <label class="c-project-tit">代码仓库地址</label>
		                                <input id="codeUrl" name="codeUrl" class="form-control c-project-con" type="text"
                                           placeholder="例如：https://github.com/tenxcloud/php-hello-world.git">
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label class="c-project-tit">认证方式</label>
		                                <select id="codeCredentials" name="codeCredentials" class="form-control c-project-con" style="width:50%;float:left;">
		                                  <%-- <c:forEach items="${ciCodeList}" var="ciCode" >
		                                      <c:if test="${ciCode.type == 1 }">
		                                          <option value="${ciCode.id }">${ciCode.userName } (HTTP) (${ciCode.remark })</option>
		                                      </c:if>
		                                      <c:if test="${ciCode.type == 2 }">
                                                  <option value="${ciCode.id }">${ciCode.userName } (SSH) (${ciCode.remark })</option>
                                              </c:if>
		                                  </c:forEach> --%>
		                                </select>
		                                <c:if test="${userAutority ==1 || userAutority ==2 }">
			                                <button type="button" id="addCredentialsBtn" class="addCredentialsBtn" value="添加证书"><i class="fa fa-key"></i>&nbsp添加证书</button>
		                                </c:if>
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label class="c-project-tit">创建分支</label>
		                                <input id="codeBranch" name="codeBranch" type="text" class="form-control c-project-con"
                                                   value="master">
		                            </div>
		                            <div class="form-group col-md-12" id = "addHook">
		                                <label class="c-project-tit">HookCode</label>
		                                <input type="checkbox" id="HookCode" class="c-project-checkbox" >
                                        <input type="hidden" id = "isHookCode" name ="isHookCode" value = "0"/>
		                            </div>
	                            </div>
	                            <div class="row git-config">
	                            	<button id="git-higher" type="button" style="float:right!important">高级...</button>
	                            </div>
	                            <div class="row git-config git-higher">
	                            	<div class="form-group col-md-12">
		                                <label class="c-project-tit">Name</label>
		                                <input id="codeUsername" name="codeName" type="text" class="form-control c-project-con reg-input"
                                                   value="">
		                            </div>
		                            <div class="form-group col-md-12">
		                                <label class="c-project-tit">Refspec</label>
		                                <input id="codePassword" name="codeRefspec" type="text"
                                                   class="form-control c-project-con reg-input" value="">
		                            </div>
                                </div>
                            </section>
                            <!--   质量管理 -->
                            <h4 class="c-title hide">质量管理</h4>
                            <section class="registryinfo hide">
                        		<div class="row depot-name">
	                                <div class="form-group col-md-12">
	                                    <label class="c-project-tit">项目主键</label>
	                                    <input id="sonarProjectKey" autocomplete = 'off' name="sonarProjectKey" class="form-control c-project-con" type="text"
	                                           required="">
	                                </div>
	                            </div>
	                            <div class="row">
	                                <div class="form-group col-md-12">
	                                    <label class="c-project-tit">项目名称</label>
	                                    <input id="sonarProjectName" autocomplete = 'off' name="sonarProjectName" class="form-control c-project-con" type="text"
	                                           required="">
	                                </div>
	                            </div>
	                            <div class="row">
	                                <div class="form-group col-md-12">
	                                    <label class="c-project-tit">项目描述</label>
	                                    <input id="sonarProjectDescription" autocomplete = 'off' name="sonarProjectDescription" class="form-control c-project-con" type="text"
	                                           required="">
	                                </div>
	                            </div>
	                            <div class="row">
	                                <div class="form-group col-md-12">
	                                    <label class="c-project-tit">项目版本号</label>
	                                    <input id="sonarProjectVersion" autocomplete = 'off' name="sonarProjectVersion" class="form-control c-project-con" type="text"
	                                           required="">
	                                </div>
	                            </div>
	                            <div class="row">
	                                <div class="form-group col-md-12">
	                                    <label class="c-project-tit">源码所在目录</label>
	                                    <input id="sonarProjectSources" autocomplete = 'off' name="sonarProjectSources" class="form-control c-project-con" type="text"
	                                           required="">
	                                </div>
	                            </div>
	                            <div class="row">
	                                <div class="form-group col-md-12">
	                                    <label class="c-project-tit">源码编码类型</label>
	                                    <input id="sonarProjectSourceEncoding" autocomplete = 'off' name="sonarProjectSourceEncoding" class="form-control c-project-con" type="text"
	                                           required="">
	                                </div>
	                            </div>
	                            <div class="row">
	                                <div class="form-group col-md-12">
	                                    <label class="c-project-tit">源码分支</label>
	                                    <input id="sonarProjectBranch" autocomplete = 'off' name="sonarProjectBranch" class="form-control c-project-con" type="text"
	                                           required="">
	                                </div>
	                            </div>
	                            <div class="row">
	                                <div class="form-group col-md-12">
	                                    <label class="c-project-tit">项目源码链接</label>
	                                    <input id="sonarProjectLinkScm" autocomplete = 'off' name="sonarProjectLinkScm" class="form-control c-project-con" type="text"
	                                           required="">
	                                </div>
	                            </div>
                        	</section>
                            <h4 class="c-title">构建</h4>
                            <section class="registryinfo">
		                                <ul class="nav nav-bar">
		                                	<li class="dropdown"><a type="button" id="dropdown-btn" class="dropdown-toggle btn btn-default" data-toggle="dropdown">增加构建步骤<span class="caret"></span></a>
		                                		<ul class="dropdown-menu">
			                                		<li><a id="maven">Maven</a></li>
			                                		<li><a id="ant">Ant</a></li>
			                                		<li><a id="shell">Execute shell</a></li>
			                                	</ul>
		                                	</li>
		                                	
		                                </ul>
	                            <div id="sortable">
	                            </div>
                            </section>
                            <h4 class="c-title"><label><strong>镜像信息</strong><input type="checkbox" id="imageInfo"></label></h4>
                            <section class="registryinfo imageInfoCon">
	                            <div class="row">
	                                <div class="form-group col-md-12">
	                                    <label class="c-project-tit">镜像名称</label>
	                                    <input name="imgNameFirst" id = "imgNameFirst" type="hidden" value="${username }">
	                                    <input id="imageName" name="imgNameLast" type="text"
                                                   class="form-control c-project-con reg-input imgInput" value="">
		                            </div>
	                            </div>
	                            <div class="row">
		                            <div class="form-group col-md-12">
	                                    <label class="c-project-tit">镜像版本</label>
	                                    <input id="imgNameVersion" name="imgNameVersion" type="text" placeholder = "填写之后重新构建镜像不会进行迭代，而是覆盖"
                                                   class="form-control c-project-con reg-input imgInput" value="">
		                            </div>
	                            </div>
	                            <div class="row">
		                            <div class="form-group col-md-12">
	                                    <label class="c-project-tit">是否为基础镜像</label>
	                                    <input id="baseImage" name="baseImage" type="checkbox"
                                                   style="height:26px" value="">
                                        <input type = "hidden" id = "isBaseImage" name = "isBaseImage" value = "">
		                            </div>
		                            <div class="form-group col-md-12">
                                        <label class="c-project-tit">是否为公有镜像</label>
                                        <input id="imageType" name="imageType" type="checkbox"
                                                   style="height:26px" value="">
                                        <input type = "hidden" id = "imgType" name = "imgType" value = "">
                                    </div>
	                            </div>
	                            <ul class="nav nav-bar">
		                           <li class="dropdown"><a type="button" id="dropdown-btn" class="dropdown-toggle btn btn-default" data-toggle="dropdown">dockerfile构建方式<span class="caret"></span></a>
		                              <ul class="dropdown-menu">
			                             <li><a id="dockerfilePath">dockerfile路径</a></li>
			                             <li><a id="dockerfileTemp">编写dockerfile</a></li>
			                          </ul>
		                            </li>
		                        </ul>
		                        <div id="dockerfileMethods">
		                        	<div class="row dockerfileTemp dockerfileTools hide">
		                        		<div class="panel-group" id="accordion">
											<div class="panel panel-default">
												<div class="panel-heading">
													<h4 class="panel-title">
														<a data-toggle="collapse" data-parent="#accordion" 
														   href="#collapseOne">工具集
														</a>
													</h4>
												</div>
												<div id="collapseOne" class="panel-collapse collapse in">
													<div class="panel-body">
															<c:forEach items="${toolGroups}" var="tools" >
																 <div class="toolItems">
																 	<ul class="dftools">
																		<li class="dftools-firstli">${tools.groupName } :</li>
																		<c:forEach items="${tools.tools}" var="tool" >
																			<li class="dftools-li"><label><input type="checkbox" name="${tool.toolGroup }" class="toolChk" toolCode="${tool.toolCode }">${tool.name }</label></li>
																		</c:forEach>
																	</ul>
																</div> 
															</c:forEach>
													</div>
												</div>
											</div>
		                        		</div>
		                        	</div>
		                        </div>
		                        <div id="dockerfileMethod"></div>
                        	</section>
                            
                        	<input type = "hidden" id = "jsonData" name = "jsonData" value = "">
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
				<!--dockerfile导入模板 -->
                <div id="dockerfile-import" style="max-height:170px;overflow-y:scroll;overflow-x:hidden;">
                    <table class="table table-hover enabled" id="Path-table-doc"
                        style="width: 326px; margin: 5px 10px 5px 10px">
                        <tbody id="dockerfile-body">
                           
                        </tbody>
                    </table>
                </div>
                <!-- dockerfile另存为模板 -->
                <div id="dockerfile-export">
                    <div style="width: 345px; margin: 5px 10px 5px 10px">
                        <span>模板名称：</span><input type="text" id="dockerFileTemplateName"
                            style="width: 77%" autofocus="autofocus" />
                    </div>
                </div>
                <!-- 添加证书 -->
			     <div id="addCredentialsCon">           
			        <div style="margin: 15px 15px">
			            <div class="infoCred">
			                <span class="labelCred">仓库：</span> 
			                <select class="form-control conCred" id="codeType" name="codeType">
			                    <option value="1">Git</option>
			                    <option value="2">SVN</option>
			                </select>
			            </div>
			            <div class="infoCred">
			                <span class="labelCred">认证：</span> <select
			                    class="form-control conCred" id="CredentialsType" name="type">
			                    <option value="1">用户名和密码</option>
			                    <option value="2">SSH用户名和密钥</option>
			                </select>
			            </div>
			            <div class="infoCred">
			                <span class="labelCred">用户名：</span> <input type="text"
			                    class="form-control conCred" id="userNameCred" name="userName"
			                    value="">
			            </div>
			            <div class="infoCred normal">
			                <span class="labelCred">密码：</span> <input type="password"
			                    class="form-control conCred" id="passwordCred" name="password"
			                    value="">
			            </div>
			            <div class="infoCred ssh">
			                <span class="labelCred">密钥：</span>
			                <textarea type="text" class="form-control conCred" style="height:100px"
			                    id="SSHpasswordCred" name="privateKey" row="8" value=""></textarea>
			            </div>
			            <div class="infoCred">
			                <span class="labelCred">描述：</span>
			                <textarea type="text" class="form-control conCred" style="height:100px"
			                    id="keyRemark" name="keyRemark" row="8" value=""></textarea>
			            </div>
			        </div>
                 </div>
               <!--  <div id="addCredentialsCon">
                    <div style="width: 345px; margin: 5px 10px 5px 10px">
                        <div class="infoCred">
                        	<span class="labelCred">类型：</span>
	                        <select class="form-control conCred" id="CredentialsType" name = "type">
	                        	<option value="1">用户名和密码</option>
	                        	<option value="2">SSH用户名和密钥</option>
	                        </select></div>
                        <div class="infoCred">
	                        <span class="labelCred">用户名：</span>
	                        <input type="text" class="form-control conCred" id="userNameCred" name="userName" value=""></div>
                        <div class="infoCred normal">
	                        <span class="labelCred">密码：</span>
	                        <input type="password" class="form-control conCred" id="passwordCred" name="password" value=""></div>
                        <div class="infoCred ssh">
	                        <span class="labelCred">密钥：</span>
	                        <textarea type="text" class="form-control conCred" id="SSHpasswordCred" name="privateKey" row="8" value="" ></textarea></div>
                    </div>
                </div> -->
</body>
</html>