<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>服务</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="/css/mod/service.css"/>
    <script type="text/javascript" src="/js/mod/service/service-create.js"></script>
    <script type="text/javascript">
        $(function(){
            // 判断是否从镜像点击部署按钮跳转
            var isDepoly = '${isDepoly}';
            var imageName = '${imageName}';
            var imageVersion = '${imageVersion}';
            if(isDepoly == 'deploy'){
                deploy(imageName,imageVersion);
            }
        });
    </script>
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
                    <li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" id="nav2">快速构建</li>
                </ol>
            </div>
            <div class="contentMain">


                <div class="modalCrealApp">
                    <form id="buildService" name="buildService" action="/service/constructContainer.do" method="post">
                        <div class="steps-main">
                        <div class="progre">
                            <ul style="padding: 0 4rem;">
                                <li class="radius_step action"><span>1</span> 镜像来源</li>
                                <li class="radius_step"><span>2</span> 容器配置</li>
                                <li class="radius_step"><span>3</span> 高级设置</li>
                            </ul>
                        </div>
                        <div class="step-inner" style="left: 0%;">

                        <%-- 镜像来源 --%>
                        <div class="host_step1">
                            <div class="content">
                                <div class="search">
                                    <form class="search-group-inner"
                                          style="width: 60%; margin: 0 auto; position: relative;"
                                          action="/registry/0">
                                        <input name="imageName" class="search-img" placeholder="搜索镜像"
                                               type="text">
                                        <button type="submit" class="btn btn-primary btn-send">搜索</button>
                                    </form>
                                </div>
                            </div>

                            <div class="blankapp">
                                <ul class="blankapp-list">
                                    <li class="list-wrapper">
                                        <ul id="imageList">
                                            <li class="image-item"><span class="img_icon span2">
                                                                <img src="/images/image-1.png">
                                                        </span> <span class="span5 type" type="database">
                                                                <div class="list-item-description">
                                                                    <div class="name h4">
                                                                        tenxcloud/mysql <a title="点击查看镜像详情" target="_blank" href="">
                                                                        <i class="fa fa-external-link-square"></i>
                                                                    </a>
                                                                    </div>
                                                                    <span class="span2"> <i class="cloud_download"></i> <span
                                                                            class="number">12214</span>
                                                                    </span> <i class="fa fa-star-o"></i> <span class="star">46</span>
                                                                </div>
                                                        </span> <span class="span3">
                                                                <div class="list-item-description">
                                                                    <span class="id h5" title="latest,5.6" value="latest,5.6">版本:
                                                                        latest,5.6</span> <span class="pull-deploy btn btn-primary"
                                                                                                data-attr="tenxcloud/mysql"> 部署 <i
                                                                        class="fa fa-arrow-circle-o-right margin fa-lg"></i>
                                                                    </span>
                                                                </div>
                                                        </span></li>
                                        </ul>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <%-- 容器配置 --%>
                        <div class="host_step2">
                            <ul class="safeSet">
                                <li class="line-h-3">
                                    <span class="ve_top">镜像名称：</span>
                                    <input type="text" value="" class="in_style" id="imageName" name="imageName" readOnly="readOnly">
                                </li>
                                <li class="line-h-3">
                                    <span class="ve_top">镜像版本：</span>
                                    <input type="text" value="" class="in_style" id="imgVersion" name="imgVersion" readOnly="readOnly">
                                </li>
                                <li class="line-h-3">
                                    <span class="ve_top">服务名称：</span>
                                    <input type="text" value="" class="in_style" id="containerName" name="containerName">
                                </li>
                                <!--<li class="line-h-3"><span class="ve_top">选择集群：</span>
                                    <div class="select-versions" data-toggle="dropdown">
                                        <span class="clusterText" data="tenx_district2">北京二区</span>
                                        <div class="sign">
                                            <i class="fa fa-angle-right"></i>
                                        </div>
                                    </div>
                                    <ul id="clusterList" class="dropdown-menu dropdown-position">
                                        <li data="default">北京一区</li>
                                        <li data="tenx_district2">北京二区</li>
                                    </ul></li>
                                 <li class="line-h-3" style="min-height: 60px; padding-top: 10px;">
                                    <span class="ve_top display-block">容器配置：</span>

                                </li> -->
                                <li class="line-h-3" id="instsize">
                                    <div class="param-set">
                                        <span class="number-title">实例数量：</span>
                                        <input type="number" value="1" class="number" min="1" autocomplete="off"
                                               placeholder="1" id="serviceNum" name="serviceNum"> <span class="unit">个</span>
                                    </div>
                                </li>
                                <li class="line-h-3">
                                    <div class="param-set">
                                        <span class="number-title">CPU数量：</span>
                                        <input type="number" value="1" class="number" min="1" autocomplete="off"
                                               placeholder="1" id="cpuNum" name="cpuNum"> <span class="unit">个</span>
                                    </div>
                                </li>
                                <li class="line-h-3">
                                    <div class="param-set">
                                        <span class="number-title">内存：</span>
                                        <input id="ramSlider" data-slider-id='ramSlider' type="text" data-slider-min="0" data-slider-max="2024" data-slider-step="1" data-slider-value="256"/>
                                        <input type="text" value="256" id="ram" name="ram">
                                        <span>M</span>
                                    </div>
                                </li>


                                <!--  <li id="service_type"><span class="ve_top">服务类型：</span> <span
                                    class="update-mi"><input type="checkbox" id="state_service"
                                        stateless="0"> <label for="state_service"><font
                                        color="blue">有状态服务</font></label><span class="mountTips"></span></span></li>-->
                                <li class="hide-set" id="save_roll_dev" style="display:none">
                                    <ul id="mountPathList">
                                        <li class="mount line-h-3"><span class="ve_top">&nbsp;</span>
                                            <table class="pull-left">
                                                <tbody>
                                                <tr>
                                                    <!-- <input type='text' disabled/> -->
                                                    <td><span class="ve_top" style="width: 150px">/var/lib/mysql</span>
                                                    </td>
                                                    <td><select class="selectVolume"
                                                                style="height: 30px; width: 230px;">
                                                        <option value="0">选择一个存储卷</option>
                                                        <option uid="" value="test">test ext4 200M</option>
                                                    </select></td>
                                                    <td>
                                                        <ins class="ins">
                                                            <input type="checkbox" id="readOnlyOp1"
                                                                   class="isVolumeReadonly"> <label for="readOnlyOp1">只读</label>
                                                        </ins>
                                                        <a class="refreshVolume" title="刷新"
                                                           href="javascript:void(0)"> <i
                                                                class="fa fa-refresh fa-lg" style="font-size: 16px"></i>
                                                        </a> <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> <a
                                                            class="delVolume" title="删除" href="javascript:void(0)">
                                                        <i class="fa fa-trash-o" style="font-size: 16px"></i>
                                                    </a></td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </li>
                                        <li class="line-h-3"><span class="ve_top">镜像设置：</span> <span
                                                class="update-mi"><input type="checkbox" id="pullPolicy"
                                                                         name="pullPolicy" checked=""> <label
                                                for="pullPolicy">更新镜像</label></span>
                                        </li>
                                    </ul>
                                    <div style="height: 80px !important;"></div>
                                </li>
                            </ul>
                        </div>

                        <%-- 高级设置 --%>
                        <div class="host_step3" style="height: auto;">
                            <ul class="advanced">
                                <li class="hide-set"><span class="ve_top">链接服务：</span>
                                    <ol class="pull-left" style="width: 100%;">
                                        <li class="hide-select"><select class="col col-4"
                                                                        id="linkcontainers" style="min-width: 100px; height: 28px;">
                                            <option>mysql</option>
                                        </select>
                                            <input id="containernickname" type="text" placeholder="value">
                                            <a id="addcontainer" class="cursor">添加</a></li>

                                        <li>
                                            <table id="linkcontainertb" class="table table-hover enabled"
                                                   style="display: none">
                                                <thead>
                                                <tr>
                                                    <th style="">服务</th>
                                                    <th style="width: 40%">名称</th>
                                                    <th style="width: 20%">操作</th>
                                                </tr>
                                                </thead>
                                                <tbody id="linkcontainerbody"></tbody>
                                            </table>
                                        </li>
                                    </ol>
                                </li>
                                <li class="hide-set"><span class="ve_top">环境变量：</span>
                                    <ol class="pull-left" style="width: 100%;">
                                        <li class="hide-select"><input type="text"
                                                                       placeholder="name" id="Name"> <input type="text"
                                                                                                            placeholder="value" id="Value"> <a
                                                id="cratePATH">添加</a>
                                        </li>
                                        <li>
                                            <table class="table table-hover enabled" id="Path">
                                                <thead>
                                                <tr>
                                                    <th style="width: 45%">键</th>
                                                    <th style="width: 45%">值</th>
                                                    <th style="width: 10%">操作</th>
                                                </tr>
                                                </thead>
                                                <tbody id="Path-oper">
                                                <tr>
                                                    <td class="keys"><input type="text" style="width: 98%"
                                                                            disabled="" value="PATH"></td>
                                                    <td class="vals"><input type="text" placeholder="value"
                                                                            value="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin">
                                                    </td>
                                                    <td class="func"><a href="javascript:void(0)"
                                                                        onclick="deleteRow(this)" class="gray"><i
                                                            class="fa fa-trash-o fa-lg"></i></a><input type="hidden"
                                                                                                       class="oldValue"
                                                                                                       value="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin">
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="keys"><input type="text" style="width: 98%"
                                                                            disabled="" value="DEBIAN_FRONTEND"></td>
                                                    <td class="vals"><input type="text" placeholder="value"
                                                                            value="noninteractive"></td>
                                                    <td class="func"><a href="javascript:void(0)"
                                                                        onclick="deleteRow(this)" class="gray"><i
                                                            class="fa fa-trash-o fa-lg"></i></a><input type="hidden"
                                                                                                       class="oldValue" value="noninteractive">
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="keys"><input type="text" style="width: 98%"
                                                                            disabled="" value="MYSQL_USER"></td>
                                                    <td class="vals"><input type="text" placeholder="value"
                                                                            value="admin"></td>
                                                    <td class="func"><a href="javascript:void(0)"
                                                                        onclick="deleteRow(this)" class="gray"><i
                                                            class="fa fa-trash-o fa-lg"></i></a><input type="hidden"
                                                                                                       class="oldValue" value="admin"></td>
                                                </tr>
                                                <tr>
                                                    <td class="keys"><input type="text" style="width: 98%"
                                                                            disabled="" value="MYSQL_PASS"></td>
                                                    <td class="vals"><input type="text" placeholder="value"
                                                                            value="**Random**"></td>
                                                    <td class="func"><a href="javascript:void(0)"
                                                                        onclick="deleteRow(this)" class="gray"><i
                                                            class="fa fa-trash-o fa-lg"></i></a><input type="hidden"
                                                                                                       class="oldValue" value="**Random**"></td>
                                                </tr>
                                                <tr>
                                                    <td class="keys"><input type="text" style="width: 98%"
                                                                            disabled="" value="REPLICATION_MASTER"></td>
                                                    <td class="vals"><input type="text" placeholder="value"
                                                                            value="**False**"></td>
                                                    <td class="func"><a href="javascript:void(0)"
                                                                        onclick="deleteRow(this)" class="gray"><i
                                                            class="fa fa-trash-o fa-lg"></i></a><input type="hidden"
                                                                                                       class="oldValue" value="**False**"></td>
                                                </tr>
                                                <tr>
                                                    <td class="keys"><input type="text" style="width: 98%"
                                                                            disabled="" value="REPLICATION_SLAVE"></td>
                                                    <td class="vals"><input type="text" placeholder="value"
                                                                            value="**False**"></td>
                                                    <td class="func"><a href="javascript:void(0)"
                                                                        onclick="deleteRow(this)" class="gray"><i
                                                            class="fa fa-trash-o fa-lg"></i></a><input type="hidden"
                                                                                                       class="oldValue" value="**False**"></td>
                                                </tr>
                                                <tr>
                                                    <td class="keys"><input type="text" style="width: 98%"
                                                                            disabled="" value="REPLICATION_USER"></td>
                                                    <td class="vals"><input type="text" placeholder="value"
                                                                            value="replica"></td>
                                                    <td class="func"><a href="javascript:void(0)"
                                                                        onclick="deleteRow(this)" class="gray"><i
                                                            class="fa fa-trash-o fa-lg"></i></a><input type="hidden"
                                                                                                       class="oldValue" value="replica"></td>
                                                </tr>
                                                <tr>
                                                    <td class="keys"><input type="text" style="width: 98%"
                                                                            disabled="" value="REPLICATION_PASS"></td>
                                                    <td class="vals"><input type="text" placeholder="value"
                                                                            value="replica"></td>
                                                    <td class="func"><a href="javascript:void(0)"
                                                                        onclick="deleteRow(this)" class="gray"><i
                                                            class="fa fa-trash-o fa-lg"></i></a><input type="hidden"
                                                                                                       class="oldValue" value="replica"></td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </li>
                                    </ol>
                                </li>
                                <li class="hide-set"><span class="ve_top">端口配置：</span>
                                    <table class="table enabled">
                                        <thead style="background: #fafafa">
                                        <tr>
                                            <th style="width: 35%">容器端口</th>
                                            <th style="width: 35%">协议</th>
                                            <th style="width: 15%">映射端口</th>
                                            <th style="vertical-align: middle; width: 8.9%">操作</th>
                                        </tr>
                                        </thead>
                                        <tbody class="BORDER" id="pushPrptpcol">
                                        <tr class="plus-row">
                                            <td><input class="port" type="text" disabled=""
                                                       value="3306"></td>
                                            <td><select class="T-http">
                                                <option>TCP</option>
                                                <option>HTTP</option>
                                            </select></td>
                                            <td><i>动态生成</i></td>
                                            <td><a href="javascript:void(0)"
                                                   onclick="_deletePortRow(this)" class="gray"> <i
                                                    class="fa fa-trash-o fa-lg"></i>
                                            </a></td>
                                        </tr>
                                        <tr class="plus-row">
                                            <td><input class="port" type="text"></td>
                                            <td><select class="T-http">
                                                <option>TCP</option>
                                                <option>HTTP</option>
                                            </select></td>
                                            <td><i>动态生成</i></td>
                                            <td><a href="javascript:void(0)"
                                                   onclick="_deletePortRow(this)" class="gray"> <i
                                                    class="fa fa-trash-o fa-lg"></i>
                                            </a></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <div class="createPort">
                                        <span><i class="fa fa-plus margin"></i></span>
                                    </div>
                                </li>
                            </ul>
                            <div style="height: 80px !important;"></div>
                        </div>
                        </div>

                        </div>
                        <div class="createPadding hide">
                            <button class="btn btn-default go_backs" style="margin-right: 30px;">上一步</button>
                            <button class="btn btn-success two_step hide">高级设置</button>
                            <button id="createButton"
                                    class="pull-right btn btn-primary pull_confirm">创建
                            </button>
                        </div>
                    </form>
                </div>

            </div>
        </div>
    </article>
</div>

</body>
</html>