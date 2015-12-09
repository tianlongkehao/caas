<%@ page contentType="text/html; charset=UTF-8" %>
<link rel="stylesheet" type="text/css" href="/css/mod/ci.css"/>
<script src="http://malsup.github.io/jquery.form.js"></script>
<script type="text/javascript">
var id = "${id}";
</script>
<script type="text/javascript" src="/js/ci/ci_detail.js"></script>
          <div class="ci-body">
              <div class="ci-head">
                  <span class="ci-name margin" id="projectNameSpan"></span>
                  <span class="btn btn-defaulted" style="cursor:auto" data-toggle="tooltip" data-placement="top" id="deploy" title="" data-original-title="构建成功后才能部署项目哦~">快速部署</span>
              </div>
              <div class="ci-content-tabmain">
                  <div class="create-log ci-tab active">构建日志</div>
                  <div class="create-detail ci-tab">项目描述</div>
                  <div class="create-set ci-tab">基本设置</div>
                  <div class="create-other ci-tab">操作</div>
                  <span class="btn btn-primary pull-right" id="btn-build">构建</span>

                  <div class="code-tabmain">

                      <div class="log-details" id="ciRecordList">
                      </div>

                            <div class="project-details hide">
                                <div>
                                    代码仓库：
                                    <a target="_blank" class="btn btn-link" title="点击链接查看项目" href="javascript:void(0)" id="codeLocation"></a>
                                </div>
                            </div>
                            <div class="config-details hide">
                            <form id="editCiForm" class="form-horizontal" method="post" action="/ci/modifyCi.do" role="form" onsubmit="">
                              <br>
                              <div class="form-group">
                                  <label for="ciProjectName" class="col-2x control-label">项目名称：</label>
                                  <div class="col-sm-9">
                                      <input type="text" class="form-control" id="projectName" name="projectName" value="">
                                  </div>
                              </div>
                              <div class="form-group">
                                  <label for="ciSummary" class="col-2x control-label">简介：</label>
                                  <div class="col-sm-9">
                                      <input type="text" class="form-control" id="description" name="description" value="">
                                  </div>
                              </div>
                              <div class="form-group">
                                  <label for="dockerfileLocation" class="col-2x control-label">Dockerfile位置：</label>
                                  <div class="col-sm-9">
                                      <input type="text" class="form-control" id="dockerFileLocation" name="dockerFileLocation" value="/">
                                  </div>
                              </div>
                              <div class="form-group">
                                  <label for="defaultBranch" class="col-2x control-label">默认代码分支：</label>
                                  <div class="col-sm-9">
                                      <input type="text" class="form-control" id="codeBranch" name="codeBranch" value="master" disabled="">
                                  </div>
                              </div>
                              <div class="form-group">
                                  <label for="editConfig" class="col-2x control-label"></label>
                                  <div class="col-sm-9">
                                      <!-- <textarea class="form-control" id="ciDetail" name="ciDetail">...</textarea> -->
                                      <br><input type="button" id="editCiBtn" class="btn btn-primary pull-right" value="确认修改">
                                  </div>
                              </div>
                              <input type="hidden" id="id" name="id" value="">
                          </form>
                      </div>
                      <div class="other-details hide">
                          <div class="col-9x">
                              <a href="javascript:void(0)" id="delCiBtn" class="deletebutton btn btn-danger btn-deleteitem">删除项目</a>
                              <p class="other-hint">
                                  <span class="gray-radius">
                                      <i class="fa fa-warning"></i>
                                  </span>
                                  删除项目将清除数据以及镜像且不能找回，慎重选择哦
                              </p>
                          </div>
                      </div>
                  </div>
              </div>
          </div>

