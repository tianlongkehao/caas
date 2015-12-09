<%@ page contentType="text/html; charset=UTF-8" %>
<link rel="stylesheet" type="text/css" href="/css/mod/ci.css"/>
<script type="text/javascript" src="/js/ci/ci.js"></script>

<div class="caption clearfix">
    <ul class="toolbox clearfix">
        <li><a href="javascript:void(0);" id="ciReloadBtn"><i class="fa fa-repeat"></i></a></li>
        <!-- <li><a href="javascript:void(0);">添加源代码</a></li> -->
        <li><a href="javascript:void(0);" id="ciAddBtn">快速构建</a></li>
    </ul>
</div>
<div class="itemTable">
    <table class="table ci-table">
        <thead>
            <tr>
                <th>
                    <div class="table-head">
                        <table class="table" style="margin:0px;">
                          <thead>
                              <tr style="height:40px;">
                                  <th style="width: 15%;text-indent:30px;">项目名称</th>
                                  <th style="width: 12%;text-indent: 15px;">构建状态</th>
                                  <th style="width: 15%;text-indent: 20px;">代码源</th>
                                  <th style="width: 12%;">上次构建时间</th>
                                  <th style="width: 10%;text-indent: 8px;">持续时间</th>
                                  <th style="width: 15%;text-indent: 10px;">镜像</th>
                                  <th style="width: 18%;text-indent: 10px;">功能</th>
                              </tr>
                          </thead>
                      </table>
                  </div>
              </th>
          </tr>
      </thead>
      <tbody  id="projectsBody">
          <tr>
              <td>
                  <div class="content-table">
                      <table class="table tables">
                          <tbody id="ciList">
                            </tbody>
                        </table>
                    </div>
                </td>
            </tr>
        </tbody>
    </table>
</div>
