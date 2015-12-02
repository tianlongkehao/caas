<%@ page contentType="text/html; charset=UTF-8" %>
<link rel="stylesheet" type="text/css" href="/css/mod/ci.css"/>
<script src="http://malsup.github.io/jquery.form.js"></script>
<script type="text/javascript" src="/js/ci/ci_add.js"></script>
<div class="item-obj">
    <div class="container">
        <h4>快速构建</h4>
        <form id="buildForm" name="buildForm" action="ci/addCi.do" method="post">
            <div class="row depot-name">
                <div class="form-group col-md-7">
                    <label for="repoName">镜像名称</label>
                    <div class="">
                        <span class="name-note">test /&nbsp;</span>
                        <input name="imgNameFisrt" type="hidden" value="test">
                        <input name="imgNameLast" type="text" class="name-input" value=""> :
                        <input name="imgNameVersion" type="text" value="latest" class="name-input" disabled="disabled">
                    </div>
                </div>
                <div class="form-group col-md-5">
                    <label for="isPrivate">性质</label>
                    <div class="">
                        <span class="btn-groups">
                            <span case="public" class="btns btn-public">公有</span>
                            <span case="private" class="btns btn-private">私有</span>
                        </span>
                        <input type="hidden" name="imgType" value="1">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group col-md-12">
                    <label for="ciSummary">简介</label>
                    <input name="description" class="form-control" type="text" required="">
                </div>
            </div>
            <div class="row">
                <div class="form-group col-md-12">
                    <label for="ciSummary">代码仓库地址</label>
                    <input name="codeUrl" class="form-control"  type="text" placeholder="例如：https://github.com/tenxcloud/php-hello-world.git">
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
                                <input id="projectName" name="projectName" type="text" value="" class="reg-input">
                            </td>
                            <td>
                                <span class="reg-text">Dockerfile位置</span>
                                <input id="dockerFileLocation" name="dockerFileLocation" type="text" class="reg-input" value="/">
                            </td>
                            <td>
                                <span class="code-branch">代码分支</span>
                                <span class="btns-group">
                                  <select id="codeBranch" name="codeBranch" class="reg-input" style="width:189px;">
                                      <option value="master">master</option>
                                  </select>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <span class="reg-text">程序类型</span>
                                <select id="codeProjectType" name="codeProjectType" class="reg-input" placeholder="项目使用哪种语言编写" style="width:163px;">
                                    <option selected=""></option>
                                    <option value="1">java</option>
                                    <option value="2">php</option>
                                    <option value="3">go</option>
                                    <option value="4">Node.js</option>
                                    <option value="5">python</option>
                                    <option value="6">other</option>
                                </select>
                            </td>
                            <td>
                                <span class="reg-text">Docker的版本&nbsp;</span>
                                <input id="dockerVersion" name="dockerVersion" type="text" class="reg-input" value="1.6" disabled="disabled">
                            </td>
                            <td>
                                <span class="reg-text">构建节点 </span>
                                <select id="constructionNode" name="constructionNode" class="reg-input" style="width:189px;">
                                    <option value="2">国际节点</option>
                                    <option value="1">国内节点</option>
                                </select>
                            </td>

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
