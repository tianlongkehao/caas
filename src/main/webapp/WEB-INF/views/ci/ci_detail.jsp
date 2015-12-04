<%@ page contentType="text/html; charset=UTF-8" %>
<link rel="stylesheet" type="text/css" href="/css/mod/ci.css"/>
<script type="text/javascript" src="/js/ci/ci_detail.js"></script>
          <div class="ci-body">
              <div class="ci-head">
                  <span class="ci-name margin">test1</span>
                  <span class="btn btn-defaulted" style="cursor:auto" data-toggle="tooltip" data-placement="top" id="deploy" title="" data-original-title="构建成功后才能部署项目哦~">快速部署</span>
              </div>
              <div class="ci-content-tabmain">
                  <div class="create-log ci-tab active">构建日志</div>
                  <div class="create-detail ci-tab">项目描述</div>
                  <div class="create-set ci-tab">基本设置</div>
                  <div class="create-other ci-tab">操作</div>
                  <span class="btn btn-primary pull-right" id="btn-build">构建</span>

                  <div class="code-tabmain">

                      <div class="log-details">
                          <div class="event-line" repotype="" status="success">

                              <div class="event-status success">
                                  <i class="fa fa-check notes"></i>
                              </div>
                              <div class="time-line-content lives">
                                  <div class="time-line-reason event-title">
                                      <div class="title-name">
                                          <span class="event-names">
                                              test1&nbsp;
                                              <span class="btn-version">latest</span>
                                          </span>
                                          <span class="time-on-status">
                                              <i class="fa_run"></i>成功
                                          </span>
                                      </div>
                                      <div class="time-line-time">
                                          <div class="event-sign"><i class="fa fa-angle-right fa_caret" style="transform: rotate(90deg);"></i></div>
                                          <div class="datetimes"><i class="fa fa-calendar margin"></i>2 天前</div>
                                          <div class="time-on-timeout"><i class="fa fa-time"></i>45分11秒</div>
                                      </div>
                                      <div class="time-line-message" style="display: block;">
                                          <div class="buildForm" buildid="" containerid="" buildername="builder2" status="fail">
                                              <div style="clear:both;"></div>
                                              <div class="buildStatus" style="margin:0px 0px 10px 0px"></div>
                                              <div class="build-logs" style="max-height: 400px; overflow: auto;margin-top:10px;background-color:black;color: #37fc34">
                                                  <pre class="logs" style="background-color:black;color: #37fc34;border:0">
<span><font color="#ffc20e">[2015年11月30日 16:26:49]</font> =&gt; Starting docker
<font color="#ffc20e">[2015年11月30日 16:26:51]</font> =&gt; Checking docker daemon
<font color="#ffc20e">[2015年11月30日 16:26:51]</font> =&gt; Checking if the application already exists
<font color="#ffc20e">[2015年11月30日 16:26:51]</font> Cloning from https://git.oschina.net/peilan/node01.git into /app
<font color="#ffc20e">[2015年11月30日 17:11:59]</font> Username for 'https://git.oschina.net': </span>
                                                        </pre>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="event-line" repotype="" status="fail">

                                    <div class="event-status error">
                                        <i class="fa fa-times notes"></i>
                                    </div>
                                    <div class="time-line-content lives">
                                        <div class="time-line-reason event-title">
                                            <div class="title-name error">
                                                <span class="event-names">
                                                    test1&nbsp;
                                                    <span class="btn-version">latest</span>
                                                </span>
                                                <span class="time-on-status">
                                                    <i class="fa_stop"></i>失败
                                                </span>
                                            </div>
                                            <div class="time-line-time">
                                                <div class="event-sign"><i class="fa fa-angle-right fa_caret" style="transform: rotate(90deg);"></i></div>
                                          <div class="datetimes"><i class="fa fa-calendar margin"></i>2 天前</div>
                                          <div class="time-on-timeout"><i class="fa fa-time"></i>45分11秒</div>
                                      </div>
                                      <div class="time-line-message" style="display: block;">
                                          <div class="buildForm" buildid="" containerid="" buildername="builder2" status="fail">
                                              <div style="clear:both;"></div>
                                              <div class="buildStatus" style="margin:0px 0px 10px 0px"></div>
                                              <div class="build-logs" style="max-height: 400px; overflow: auto;margin-top:10px;background-color:black;color: #37fc34">
                                                  <pre class="logs" style="background-color:black;color: #37fc34;border:0">
<span><font color="#ffc20e">[2015年11月30日 16:26:49]</font> =&gt; Starting docker
<font color="#ffc20e">[2015年11月30日 16:26:51]</font> =&gt; Checking docker daemon
<font color="#ffc20e">[2015年11月30日 16:26:51]</font> =&gt; Checking if the application already exists
<font color="#ffc20e">[2015年11月30日 16:26:51]</font> Cloning from https://git.oschina.net/peilan/node01.git into /app
<font color="#ffc20e">[2015年11月30日 17:11:59]</font> Username for 'https://git.oschina.net': </span>
                                                        </pre>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="event-line" repotype="" status="fail">

                                    <div class="event-status error">
                                        <i class="fa fa-times notes"></i>
                                    </div>
                                    <div class="time-line-content lives">
                                        <div class="time-line-reason event-title">
                                            <div class="title-name error">
                                                <span class="event-names">
                                                    test1&nbsp;
                                                    <span class="btn-version">latest</span>
                                                </span>
                                                <span class="time-on-status">
                                                    <i class="fa_stop"></i>失败
                                                </span>
                                            </div>
                                            <div class="time-line-time">
                                                <div class="event-sign"><i class="fa fa-angle-right fa_caret" style="transform: rotate(90deg);"></i></div>
                                          <div class="datetimes"><i class="fa fa-calendar margin"></i>2 天前</div>
                                          <div class="time-on-timeout"><i class="fa fa-time"></i>45分11秒</div>
                                      </div>
                                      <div class="time-line-message" style="display: block;">
                                          <div class="buildForm" buildid="" containerid="" buildername="builder2" status="fail">
                                              <div style="clear:both;"></div>
                                              <div class="buildStatus" style="margin:0px 0px 10px 0px"></div>
                                              <div class="build-logs" style="max-height: 400px; overflow: auto;margin-top:10px;background-color:black;color: #37fc34">
                                                  <pre class="logs" style="background-color:black;color: #37fc34;border:0">
<span><font color="#ffc20e">[2015年11月30日 16:26:49]</font> =&gt; Starting docker
<font color="#ffc20e">[2015年11月30日 16:26:51]</font> =&gt; Checking docker daemon
<font color="#ffc20e">[2015年11月30日 16:26:51]</font> =&gt; Checking if the application already exists
<font color="#ffc20e">[2015年11月30日 16:26:51]</font> Cloning from https://git.oschina.net/peilan/node01.git into /app
<font color="#ffc20e">[2015年11月30日 17:11:59]</font> Username for 'https://git.oschina.net': </span>
                                                        </pre>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>


                            </div>

                            <div class="project-details hide">
                                <div>
                                    代码仓库：
                                    <a target="_blank" class="btn btn-link" title="点击链接查看项目" href="">/testProject/test1</a>
                                </div>
                            </div>
                            <div class="config-details hide">
                                <form id="configForm" class="form-horizontal" method="post" action="" role="form" onsubmit="">
                              <br>
                              <div class="form-group">
                                  <label for="ciProjectName" class="col-2x control-label">项目名称：</label>
                                  <div class="col-sm-9">
                                      <input type="text" class="form-control" id="ciProjectName" name="ciProjectName" value="node01">
                                  </div>
                              </div>
                              <div class="form-group">
                                  <label for="ciSummary" class="col-2x control-label">简介：</label>
                                  <div class="col-sm-9">
                                      <input type="text" class="form-control" id="ciSummary" name="ciSummary" value="testnode">
                                  </div>
                              </div>
                              <div class="form-group">
                                  <label for="dockerfileLocation" class="col-2x control-label">Dockerfile位置：</label>
                                  <div class="col-sm-9">
                                      <input type="text" class="form-control" id="dockerfileLocation" name="dockerfileLocation" value="/">
                                  </div>
                              </div>
                              <div class="form-group">
                                  <label for="defaultBranch" class="col-2x control-label">默认代码分支：</label>
                                  <div class="col-sm-9">
                                      <input type="text" class="form-control" id="defaultBranch" name="defaultBranch" value="master" disabled="">
                                  </div>
                              </div>
                              <div class="form-group">
                                  <label for="editConfig" class="col-2x control-label"></label>
                                  <div class="col-sm-9">
                                      <!-- <textarea class="form-control" id="ciDetail" name="ciDetail">...</textarea> -->
                                      <br><input type="submit" id="editConfig" class="btn btn-primary pull-right" value="确认修改">
                                  </div>
                              </div>

                              <div class="form-group hide">
                                  <label for="" class="col-2x control-label">其他操作：</label>
                                  <div class="col-sm-9">
                                      <a href="javascript:void(0)" id="editConfig" class="deletebutton btn btn-danger btn-deleteitem">删除项目</a>
                                      <p class="other-hint">
                                          <span class="gray-radius">
                                              <i class="fa fa-warning"></i>
                                          </span>
                                          删除项目将清除数据且不能找回，慎重选择哦
                                      </p>
                                  </div>
                              </div>
                              <input type="hidden" id="projectId" name="projectId" value="">
                          </form>
                      </div>
                      <div class="other-details hide">
                          <div class="col-9x">
                              <a href="javascript:void(0)" id="editConfig" class="deletebutton btn btn-danger btn-deleteitem">删除项目</a>
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

