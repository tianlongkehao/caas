<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>镜像中心</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="/css/mod/ci.css"/>
    <script type="text/javascript" src="/js/ci/ci_add.js"></script>
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
                    <li class="active" id="nav2">快速构建</li>
                </ol>
            </div>
            <div class="contentMain">

                <div class="item-obj">
                    <div class="container">
                        <h4>快速构建</h4>

                        <form id="buildForm" name="buildForm" action="/ci/addCi.do" method="post">
                            <div class="row depot-name">
                                <div class="form-group col-md-7">
                                    <label>镜像名称</label>

                                    <div class="">
                                        <span class="name-note">${username } /&nbsp;</span>
                                        <input name="imgNameFirst" type="hidden" value="${username }">
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
                                            <a case="public" class="btns active" onclick="javascript:$('#imgType').val(1)">公有</a>
                                            <a case="private" class="btns" onclick="javascript:$('#imgType').val(2)">私有</a>
                                        </span>
                                        <input type="hidden" name="imgType" id="imgType" value="1">
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
                                    <label for="codeUrl">代码仓库地址</label>
                                    <input id="codeUrl" name="codeUrl" class="form-control" type="text"
                                           placeholder="例如：https://github.com/tenxcloud/php-hello-world.git">
                                </div>
                            </div>
                            <section class="registryinfo">
                                <table class="table registry">
                                    <tbody>
                                    <tr>
                                        <td>
                                            <span class="reg-text">代码库类型</span>
                                <span class="btns-group">
                                  <select id="codeType" name="codeType" class="reg-input" style="width:189px;">
                                      <option value="2">git</option>
                                      <option value="1">svn</option>
                                  </select>
                                </span>
                                        </td>
                                        <td>
                                            <span class="reg-text">代码库用户名</span>
                                            <input id="codeUsername" name="codeUsername" type="text" class="reg-input"
                                                   value="">
                                        </td>
                                        <td>
                                            <span class="code-branch">代码库密码</span>
                                            <input id="codePassword" name="codePassword" type="password"
                                                   class="reg-input" value="">
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </section>

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
                                        <td>
                                            <span class="reg-text">Dockerfile位置</span>
                                            <input id="dockerFileLocation" name="dockerFileLocation" type="text"
                                                   class="reg-input" value="/">
                                        </td>
                                        <td>
                                            <span class="code-branch">代码分支</span>
                                            <span class="btns-group">
                                              <select id="codeBranch" name="codeBranch" class="reg-input" style="width:189px;">
                                                  <option value="master">master</option>
                                                  <!-- <option value="develop">develop</option> -->
                                              </select>
                                            </span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="reg-text">程序类型</span>
                                            <select id="codeProjectType" name="codeProjectType" class="reg-input"
                                                    placeholder="项目使用哪种语言编写" style="width:163px;">
                                                <option selected=""></option>
                                                <option value="1">java</option>
                                                <option value="2">php</option>
                                                <option value="3">go</option>
                                                <option value="4">Node.js</option>
                                                <option value="5">python</option>
                                                <option value="6">other</option>
                                            </select>
                                        </td>
                                        <!-- <td>
                                            <span class="reg-text">Docker的版本&nbsp;</span>
                                            <input id="dockerVersion" name="dockerVersion" type="text" class="reg-input" value="1.6" disabled="disabled">
                                        </td>
                                        <td>
                                            <span class="reg-text">构建节点 </span>
                                            <select id="constructionNode" name="constructionNode" class="reg-input" style="width:189px;">
                                                <option value="2">国际节点</option>
                                                <option value="1">国内节点</option>
                                            </select>
                                        </td> -->

                                    </tr>
                                    </tbody>
                                </table>
                            </section>
                            <br>

                            <div class="pull-right">
                                <span id="buildBtn" class="btn btn-primary">创建</span>
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