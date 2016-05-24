<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html class=" js flexbox canvas canvastext webgl no-touch geolocation postmessage websqldatabase indexeddb hashchange history draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow textshadow opacity cssanimations csscolumns cssgradients cssreflections csstransforms csstransforms3d csstransitions fontface generatedcontent video audio localstorage sessionstorage webworkers applicationcache svg inlinesvg smil svgclippaths">
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <style type="text/css">@charset "UTF-8";
    [ng\:cloak], [ng-cloak], [data-ng-cloak], [x-ng-cloak], .ng-cloak, .x-ng-cloak, .ng-hide:not(.ng-hide-animate) {
        display: none !important;
    }

    ng\:form {
        display: block;
    }

    .ng-animate-shim {
        visibility: hidden;
    }

    .ng-anchor {
        position: absolute;
    }</style>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width">
    <meta name="theme-color" content="#000">

    <title>Grafana - Kubernetes Cluster</title>

    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/cluster.css"/>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/core/xcharts.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/grafana.css">

    <script type="text/javascript" src="<%=path %>/js/cluster/cluster.js"></script>
    <script type="text/javascript" src="<%=path %>/plugins/bower-angular-master/angular.js"></script>
    <script type="text/javascript" src="<%=path %>/plugins/chartjs-1.0.2/Chart.js"></script>
    <%--<script type="text/javascript" src="<%=path %>/js/plugins/xcharts.min.js"></script>
    <script type="text/javascript" src="<%=path %>/js/plugins/xcharts.js"></script>--%>
    <script type="text/javascript" src="<%=path %>/js/cluster/cluster.js"></script>
    <script type="text/javascript" src="<%=path %>/js/cluster/require.js"></script>
    <script type="text/javascript" src="<%=path %>/js/cluster/grafana.js"></script>

    <%--<link rel="icon" type="image/png" href="/public/img/fav32.png">--%>
    <base href="/">

</head>

<body ng-controller="GrafanaCtrl" ng-class="{'sidemenu-open': contextSrv.sidemenu}" class="ng-scope sidemenu-open">
<div class="sidemenu-canvas">

    <!-- ngIf: contextSrv.sidemenu -->
    <aside class="sidemenu-wrapper ng-scope" ng-if="contextSrv.sidemenu">
        <!-- ngInclude: 'app/partials/sidemenu.html' -->
        <div ng-include="'app/partials/sidemenu.html'" class="ng-scope">
            <div ng-controller="SideMenuCtrl" ng-init="init()" class="ng-scope">

                <ul class="sidemenu sidemenu-main">
                    <li style="margin-bottom: 15px;">
                        <a class="pointer sidemenu-top-btn" ng-click="contextSrv.toggleSideMenu()">
                            <%--<img class="logo-icon" src="img/fav32.png">--%>
                            <i class="pull-right fa fa-angle-left"></i>
                        </a>
                    </li>

                    <!-- ngIf: systemSection -->

                    <!-- ngRepeat: item in mainLinks -->
                    <li ng-repeat="item in mainLinks" class="ng-scope">
                        <a href="/" class="sidemenu-item" target="">
                            <span class="icon-circle sidemenu-icon"><i class="fa fa-fw fa-th-large"></i></span>
                            <span class="sidemenu-item-text ng-binding">Dashboards</span>
                        </a>
                    </li>
                    <!-- end ngRepeat: item in mainLinks -->
                    <li ng-repeat="item in mainLinks" class="ng-scope">
                        <a href="/datasources" class="sidemenu-item" target="">
                            <span class="icon-circle sidemenu-icon"><i class="fa fa-fw fa-database"></i></span>
                            <span class="sidemenu-item-text ng-binding">Data Sources</span>
                        </a>
                    </li>
                    <!-- end ngRepeat: item in mainLinks -->
                </ul>

                <!-- ngIf: !systemSection -->
                <ul class="sidemenu sidemenu-small ng-scope" style="margin-top:50px" ng-if="!systemSection">

                    <!-- ngIf: contextSrv.user.isSignedIn -->

                    <li class="dropdown">
                        <a class="sidemenu-item pointer" data-toggle="dropdown" ng-click="loadOrgs()" tabindex="0">
                            <span class="icon-circle sidemenu-icon"><i class="fa fa-fw fa-users"></i></span>
                            <span class="sidemenu-item-text ng-binding">Main Org.</span>
                            <i class="fa fa-caret-down small"></i>
                        </a>
                        <ul class="dropdown-menu" role="menu" style="left: 65px">
                            <!-- ngRepeat: menuItem in orgMenu -->
                        </ul>
                    </li>

                    <!-- ngIf: contextSrv.isGrafanaAdmin -->

                    <!-- ngIf: contextSrv.isSignedIn -->

                    <!-- ngIf: !contextSrv.isSignedIn -->
                    <li ng-if="!contextSrv.isSignedIn" class="ng-scope">
                        <a href="login" class="sidemenu-item" target="_self">
                            <span class="icon-circle sidemenu-icon"><i class="fa fa-fw fa-sign-in"></i></span>
                            <span class="sidemenu-item-text">Sign in</span>
                        </a>
                    </li>
                    <!-- end ngIf: !contextSrv.isSignedIn -->
                </ul>
                <!-- end ngIf: !systemSection -->

                <!-- ngIf: systemSection -->

            </div>
        </div>
    </aside>
    <!-- end ngIf: contextSrv.sidemenu -->

    <div class="page-alert-list">
        <!-- ngRepeat: alert in dashAlerts.list -->
    </div>

    <!-- ngView:  -->
    <div ng-view="" class="main-view ng-scope">
        <div body-class="" class="dashboard ng-scope">

            <!-- ngInclude:  -->
            <div ng-include="" src="topNavPartial" class="ng-scope">
                <div class="navbar navbar-static-top ng-scope" ng-controller="DashboardNavCtrl" ng-init="init()">
                    <div class="navbar-inner">
                        <div class="container-fluid">

                            <div class="top-nav">
                                <!-- ngIf: !contextSrv.sidemenu -->

                                <div class="top-nav-dashboards-btn">
                                    <a class="pointer" ng-click="openSearch()">
                                        <i class="fa fa-th-large"></i>
                                        <span class="dashboard-title ng-binding">Kubernetes Cluster</span>
                                        <i class="fa fa-caret-down"></i>
                                    </a>
                                </div>
                            </div>

                            <ul class="nav pull-left top-nav-dash-actions">
                                <li ng-show="dashboardMeta.canStar" class="ng-hide">
                                    <a class="pointer" ng-click="starDashboard()">
                                        <i class="fa fa-star-o"
                                           ng-class="{'fa-star-o': !dashboardMeta.isStarred, 'fa-star': dashboardMeta.isStarred}"
                                           style="color: orange;"></i>
                                    </a>
                                </li>
                                <li ng-show="dashboardMeta.canShare" class="">
                                    <a class="pointer ng-scope" ng-click="shareDashboard()"
                                       bs-tooltip="'Share dashboard'" data-placement="bottom" data-original-title=""
                                       title=""><i class="fa fa-share-square-o"></i></a>
                                </li>
                                <li ng-show="dashboardMeta.canSave" class="">
                                    <a ng-click="saveDashboard()" bs-tooltip="'Save dashboard'" data-placement="bottom"
                                       class="ng-scope" data-original-title="" title=""><i class="fa fa-save"></i></a>
                                </li>
                                <li class="dropdown">
                                    <a class="pointer ng-scope" ng-click="hideTooltip($event)"
                                       bs-tooltip="'Manage dashboard'" data-placement="bottom" data-toggle="dropdown"
                                       data-original-title="" title=""><i class="fa fa-cog"></i></a>
                                    <ul class="dropdown-menu">
                                        <!-- ngIf: dashboardMeta.canEdit -->
                                        <li ng-if="dashboardMeta.canEdit" class="ng-scope"><a class="pointer"
                                                                                              ng-click="openEditView('settings');">Settings</a>
                                        </li>
                                        <!-- end ngIf: dashboardMeta.canEdit -->
                                        <!-- ngIf: dashboardMeta.canEdit -->
                                        <li ng-if="dashboardMeta.canEdit" class="ng-scope"><a class="pointer"
                                                                                              ng-click="openEditView('annotations');">Annotations</a>
                                        </li>
                                        <!-- end ngIf: dashboardMeta.canEdit -->
                                        <!-- ngIf: dashboardMeta.canEdit -->
                                        <li ng-if="dashboardMeta.canEdit" class="ng-scope"><a class="pointer"
                                                                                              ng-click="openEditView('templating');">Templating</a>
                                        </li>
                                        <!-- end ngIf: dashboardMeta.canEdit -->
                                        <li><a class="pointer" ng-click="exportDashboard();">Export</a></li>
                                        <li><a class="pointer" ng-click="editJson();">View JSON</a></li>
                                        <!-- ngIf: contextSrv.isEditor -->
                                        <li ng-if="contextSrv.isEditor" class="ng-scope"><a class="pointer"
                                                                                            ng-click="saveDashboardAs();">Save
                                            As...</a></li>
                                        <!-- end ngIf: contextSrv.isEditor -->
                                        <!-- ngIf: dashboardMeta.canSave -->
                                        <li ng-if="dashboardMeta.canSave" class="ng-scope"><a class="pointer"
                                                                                              ng-click="deleteDashboard();">Delete
                                            dashboard</a></li>
                                        <!-- end ngIf: dashboardMeta.canSave -->
                                    </ul>
                                </li>
                            </ul>

                            <!-- ngIf: playlistSrv -->

                            <ul class="nav pull-right">
                                <li ng-show="dashboardViewState.fullscreen" class="back-to-dashboard-link ng-hide">
                                    <a ng-click="exitFullscreen()">
                                        Back to dashboard
                                    </a>
                                </li>
                                <!-- ngIf: dashboard -->
                                <li ng-if="dashboard" class="ng-scope">
                                    <gf-time-picker dashboard="dashboard" class="ng-isolate-scope">
                                        <ul class="nav gf-timepicker-nav">

                                            <li class="grafana-menu-zoom-out">
                                                <a class="small" ng-click="ctrl.zoom(2)">
                                                    Zoom Out
                                                </a>
                                            </li>

                                            <li ng-class="{'gf-timepicker-open': ctrl.isOpen}">
                                                <a bs-tooltip="ctrl.tooltip" data-placement="bottom"
                                                   ng-click="ctrl.openDropdown()" class="ng-scope"
                                                   data-original-title="" title="">
                                                    <i class="fa fa-clock-o"></i>
                                                    <span ng-bind="ctrl.rangeString"
                                                          class="ng-binding">Last 30 minutes</span>
                                                    <span ng-show="ctrl.isUtc" class="gf-timepicker-utc ng-hide">
                                                        UTC
                                                    </span>

                                                    <span ng-show="ctrl.dashboard.refresh"
                                                          class="text-warning ng-binding ng-hide">
                                                        &nbsp;
                                                        Refresh every
                                                    </span>
                                                </a>
                                            </li>

                                            <li class="grafana-menu-refresh">
                                                <a ng-click="ctrl.timeSrv.refreshDashboard()">
                                                    <i class="fa fa-refresh"></i>
                                                </a>
                                            </li>

                                        </ul>
                                    </gf-time-picker>
                                </li>
                                <!-- end ngIf: dashboard -->
                            </ul>
                        </div>
                    </div>
                </div>

            </div>

            <div dash-editor-view=""></div>
            <div dash-search-view=""></div>
            <div class="clearfix"></div>

            <!-- ngIf: submenuEnabled --><!-- ngInclude: 'app/partials/submenu.html' -->
            <div ng-if="submenuEnabled" ng-include="'app/partials/submenu.html'" class="ng-scope">
                <div class="submenu-controls ng-scope" ng-controller="SubmenuCtrl">
                    <div class="tight-form borderless">

                        <!-- ngIf: dashboard.templating.list.length > 0 -->
                        <ul class="tight-form-list ng-scope" ng-if="dashboard.templating.list.length > 0">
                            <!-- ngRepeat: variable in variables -->
                            <li ng-repeat="variable in variables" class="submenu-item ng-scope">
				<span class="template-variable tight-form-item ng-binding" ng-show="!variable.hideLabel"
                      style="padding-right: 5px">
					Node:
				</span>
                                <value-select-dropdown variable="variable" on-updated="variableUpdated(variable)"
                                                       get-values-for-tag="getValuesForTag(variable, tagKey)"
                                                       class="ng-isolate-scope">
                                    <div class="variable-link-wrapper">
                                        <a ng-click="vm.show()" class="variable-value-link tight-form-item ng-binding"
                                           style="display: inline-block;">
                                            All
                                            <!-- ngRepeat: tag in vm.selectedTags -->
                                            <i class="fa fa-caret-down"></i>
                                        </a>

                                        <input type="text"
                                               class="tight-form-clear-input input-small ng-pristine ng-valid ng-touched"
                                               style="width: 67px; display: none;" ng-keydown="vm.keyDown($event)"
                                               ng-model="vm.search.query" ng-change="vm.queryChanged()">

                                        <!-- ngIf: vm.dropdownVisible -->
                                    </div>
                                </value-select-dropdown>
                            </li>
                            <!-- end ngRepeat: variable in variables -->
                        </ul>
                        <!-- end ngIf: dashboard.templating.list.length > 0 -->

                        <!-- ngIf: dashboard.annotations.list.length > 0 -->

                        <!-- ngIf: dashboard.links.length > 0 -->

                        <div class="clearfix"></div>
                    </div>
                </div>
            </div>
            <!-- end ngIf: submenuEnabled -->

            <div class="clearfix"></div>

            <div class="main-view-container">
                <!-- ngRepeat: (row_name, row) in dashboard.rows -->
                <div class="grafana-row ng-scope" ng-controller="RowCtrl" ng-repeat="(row_name, row) in dashboard.rows"
                     row-height="" style="min-height: 5px;">
                    <div class="row-control">
                        <div class="row-control-inner">
                            <div class="row-close" ng-show="row.collapse" data-placement="bottom">
                                <div class="row-close-buttons">
							<span class="row-button bgPrimary" ng-click="toggleRow(row)">
								<i bs-tooltip="'Expand row'" data-placement="right"
                                   class="fa fa-caret-left pointer ng-scope" data-original-title="" title=""></i>
							</span>
                                </div>
                                <div class="row-text pointer ng-binding" ng-click="toggleRow(row)"
                                     ng-bind="row.title | interpolateTemplateVars:this">Documentation
                                </div>
                            </div>
                            <div class="row-open ng-hide" ng-show="!row.collapse">
                                <div class="row-tab bgSuccess dropdown" ng-show="dashboardMeta.canEdit"
                                     ng-hide="dashboardViewState.fullscreen">
							<span class="row-tab-button dropdown-toggle" data-toggle="dropdown">
								<i class="fa fa-bars"></i>
							</span>
                                    <ul class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="drop1">
                                        <li>
                                            <a ng-click="toggleRow(row)">Collapse row</a>
                                        </li>
                                        <li class="dropdown-submenu">
                                            <a href="javascript:void(0);">Add Panel</a>
                                            <ul class="dropdown-menu">
                                                <!-- ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Graph</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Table</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Single
                                                        stat</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Text</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Dashboard
                                                        list</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                            </ul>
                                        </li>
                                        <li class="dropdown-submenu">
                                            <a href="javascript:void(0);">Set height</a>
                                            <ul class="dropdown-menu">
                                                <li><a ng-click="setHeight('25px')">25 px</a></li>
                                                <li><a ng-click="setHeight('100px')">100 px</a></li>
                                                <li><a ng-click="setHeight('150px')">150 px</a></li>
                                                <li><a ng-click="setHeight('200px')">200 px</a></li>
                                                <li><a ng-click="setHeight('250px')">250 px</a></li>
                                                <li><a ng-click="setHeight('300px')">300 px</a></li>
                                                <li><a ng-click="setHeight('350px')">350 px</a></li>
                                                <li><a ng-click="setHeight('450px')">450 px</a></li>
                                                <li><a ng-click="setHeight('500px')">500 px</a></li>
                                                <li><a ng-click="setHeight('600px')">600 px</a></li>
                                                <li><a ng-click="setHeight('700px')">700 px</a></li>
                                            </ul>
                                        </li>
                                        <li class="dropdown-submenu">
                                            <a href="javascript:void(0);">Move</a>
                                            <ul class="dropdown-menu">
                                                <li><a ng-click="moveRow('up')">Up</a></li>
                                                <li><a ng-click="moveRow('down')">Down</a></li>
                                                <li><a ng-click="moveRow('top')">To top</a></li>
                                                <li><a ng-click="moveRow('bottom')">To Bottom</a></li>
                                            </ul>
                                        </li>
                                        <li>
                                            <a dash-editor-link="app/partials/roweditor.html">Row editor</a>
                                        </li>
                                        <li>
                                            <a ng-click="deleteRow()">Delete row</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <!-- ngIf: !row.collapse -->
                    </div>
                </div>
                <!-- end ngRepeat: (row_name, row) in dashboard.rows -->
                <div class="grafana-row ng-scope" ng-controller="RowCtrl" ng-repeat="(row_name, row) in dashboard.rows"
                     row-height="" style="min-height: 250px;">
                    <div class="row-control">
                        <div class="row-control-inner">
                            <div class="row-close ng-hide" ng-show="row.collapse" data-placement="bottom">
                                <div class="row-close-buttons">
							<span class="row-button bgPrimary" ng-click="toggleRow(row)">
								<i bs-tooltip="'Expand row'" data-placement="right"
                                   class="fa fa-caret-left pointer ng-scope" data-original-title="" title=""></i>
							</span>
                                </div>
                                <div class="row-text pointer ng-binding" ng-click="toggleRow(row)"
                                     ng-bind="row.title | interpolateTemplateVars:this">Overall Cluster Memory Usage
                                </div>
                            </div>
                            <div class="row-open" ng-show="!row.collapse">
                                <div class="row-tab bgSuccess dropdown" ng-show="dashboardMeta.canEdit"
                                     ng-hide="dashboardViewState.fullscreen">
							<span class="row-tab-button dropdown-toggle" data-toggle="dropdown">
								<i class="fa fa-bars"></i>
							</span>
                                    <ul class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="drop1">
                                        <li>
                                            <a ng-click="toggleRow(row)">Collapse row</a>
                                        </li>
                                        <li class="dropdown-submenu">
                                            <a href="javascript:void(0);">Add Panel</a>
                                            <ul class="dropdown-menu">
                                                <!-- ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Graph</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Table</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Single
                                                        stat</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Text</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Dashboard
                                                        list</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                            </ul>
                                        </li>
                                        <li class="dropdown-submenu">
                                            <a href="javascript:void(0);">Set height</a>
                                            <ul class="dropdown-menu">
                                                <li><a ng-click="setHeight('25px')">25 px</a></li>
                                                <li><a ng-click="setHeight('100px')">100 px</a></li>
                                                <li><a ng-click="setHeight('150px')">150 px</a></li>
                                                <li><a ng-click="setHeight('200px')">200 px</a></li>
                                                <li><a ng-click="setHeight('250px')">250 px</a></li>
                                                <li><a ng-click="setHeight('300px')">300 px</a></li>
                                                <li><a ng-click="setHeight('350px')">350 px</a></li>
                                                <li><a ng-click="setHeight('450px')">450 px</a></li>
                                                <li><a ng-click="setHeight('500px')">500 px</a></li>
                                                <li><a ng-click="setHeight('600px')">600 px</a></li>
                                                <li><a ng-click="setHeight('700px')">700 px</a></li>
                                            </ul>
                                        </li>
                                        <li class="dropdown-submenu">
                                            <a href="javascript:void(0);">Move</a>
                                            <ul class="dropdown-menu">
                                                <li><a ng-click="moveRow('up')">Up</a></li>
                                                <li><a ng-click="moveRow('down')">Down</a></li>
                                                <li><a ng-click="moveRow('top')">To top</a></li>
                                                <li><a ng-click="moveRow('bottom')">To Bottom</a></li>
                                            </ul>
                                        </li>
                                        <li>
                                            <a dash-editor-link="app/partials/roweditor.html">Row editor</a>
                                        </li>
                                        <li>
                                            <a ng-click="deleteRow()">Delete row</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <!-- ngIf: !row.collapse -->
                        <div class="panels-wrapper ng-scope" ng-if="!row.collapse">
                            <!-- ngIf: row.showTitle -->
                            <div class="row-text pointer ng-binding ng-scope" ng-click="toggleRow(row)"
                                 ng-if="row.showTitle" ng-bind="row.title | interpolateTemplateVars:this">Overall
                                Cluster Memory Usage
                            </div>
                            <!-- end ngIf: row.showTitle -->

                            <!-- ngRepeat: (name, panel) in row.panels track by panel.id -->
                            <div ng-repeat="(name, panel) in row.panels track by panel.id" class="panel ng-scope"
                                 ui-draggable="!dashboardViewState.fullscreen" drag="panel.id"
                                 ui-on-drop="onDrop($data, row, panel)" drag-handle-class="drag-handle" panel-width=""
                                 draggable="true" style="width: 100%;">
                                <panel-loader type="panel.type" class="panel-margin">
                                    <grafana-panel-graph class="ng-scope">
                                        <grafana-panel>
                                            <div class="panel-container"
                                                 ng-class="{'panel-transparent': panel.transparent}"
                                                 style="min-height: 250px; display: block;">
                                                <div class="panel-header">
                                                    <!-- ngIf: panelMeta.error -->

		<span class="panel-loading ng-hide" ng-show="panelMeta.loading">
			<i class="fa fa-spinner fa-spin"></i>
		</span>

                                                    <div class="panel-title-container drag-handle" panel-menu=""><span
                                                            class="panel-title drag-handle pointer ng-scope"><span
                                                            class="panel-title-text drag-handle ng-binding"></span><span
                                                            class="panel-links-btn" style="display: none;"><i
                                                            class="fa fa-external-link"></i></span><span
                                                            class="panel-time-info ng-binding ng-hide"
                                                            ng-show="panelMeta.timeInfo"><i class="fa fa-clock-o"></i> </span></span>
                                                    </div>
                                                </div>

                                                <div class="panel-content">
                                                    <ng-transclude>

                                                        <div class="graph-wrapper ng-scope"
                                                             ng-class="{'graph-legend-rightside': panel.legend.rightSide}">
                                                            <div class="graph-canvas-wrapper">

                                                                <!-- ngIf: datapointsWarning -->

                                                                <div grafana-graph="" class="histogram-chart"
                                                                     style="height: 210px; padding: 0px;">
                                                                    <canvas class="flot-base" width="1115" height="210"
                                                                            style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 1115px; height: 210px;"></canvas>
                                                                    <div class="flot-text"
                                                                         style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; font-size: smaller; color: rgb(84, 84, 84);">
                                                                        <div class="flot-x-axis flot-x1-axis xAxis x1Axis"
                                                                             style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; display: block;">
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; max-width: 65px; top: 190px; left: 102px; text-align: center;">
                                                                                11:26
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; max-width: 65px; top: 190px; left: 172px; text-align: center;">
                                                                                11:28
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; max-width: 65px; top: 190px; left: 242px; text-align: center;">
                                                                                11:30
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; max-width: 65px; top: 190px; left: 312px; text-align: center;">
                                                                                11:32
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; max-width: 65px; top: 190px; left: 382px; text-align: center;">
                                                                                11:34
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; max-width: 65px; top: 190px; left: 452px; text-align: center;">
                                                                                11:36
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; max-width: 65px; top: 190px; left: 522px; text-align: center;">
                                                                                11:38
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; max-width: 65px; top: 190px; left: 592px; text-align: center;">
                                                                                11:40
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; max-width: 65px; top: 190px; left: 662px; text-align: center;">
                                                                                11:42
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; max-width: 65px; top: 190px; left: 732px; text-align: center;">
                                                                                11:44
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; max-width: 65px; top: 190px; left: 802px; text-align: center;">
                                                                                11:46
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; max-width: 65px; top: 190px; left: 872px; text-align: center;">
                                                                                11:48
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; max-width: 65px; top: 190px; left: 942px; text-align: center;">
                                                                                11:50
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; max-width: 65px; top: 190px; left: 1012px; text-align: center;">
                                                                                11:52
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; max-width: 65px; top: 190px; left: 1082px; text-align: center;">
                                                                                11:54
                                                                            </div>
                                                                        </div>
                                                                        <div class="flot-y-axis flot-y1-axis yAxis y1Axis"
                                                                             style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; display: block;">
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; top: 175px; left: 1px; text-align: right;">
                                                                                186 GiB
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; top: 146px; left: 1px; text-align: right;">
                                                                                373 GiB
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; top: 117px; left: 1px; text-align: right;">
                                                                                559 GiB
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; top: 87px; left: 1px; text-align: right;">
                                                                                745 GiB
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; top: 58px; left: 1px; text-align: right;">
                                                                                931 GiB
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; top: 29px; left: 6px; text-align: right;">
                                                                                1.1 TiB
                                                                            </div>
                                                                            <div class="flot-tick-label tickLabel"
                                                                                 style="position: absolute; top: 0px; left: 6px; text-align: right;">
                                                                                1.3 TiB
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <canvas class="flot-overlay" width="1115"
                                                                            height="210"
                                                                            style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 1115px; height: 210px;"></canvas>
                                                                </div>

                                                            </div>

                                                            <!-- ngIf: panel.legend.show -->
                                                            <div class="graph-legend-wrapper ng-scope"
                                                                 ng-if="panel.legend.show" graph-legend="">
                                                                <section class="graph-legend">
                                                                    <div class="graph-legend-series"
                                                                         data-series-index="0">
                                                                        <div class="graph-legend-icon"><i
                                                                                class="fa fa-minus pointer"
                                                                                style="color:#7EB26D"></i></div>
                                                                        <div class="graph-legend-alias"><a>Limit</a>
                                                                        </div>
                                                                        <div class="graph-legend-value current">1.03
                                                                            TiB
                                                                        </div>
                                                                    </div>
                                                                    <div class="graph-legend-series"
                                                                         data-series-index="1">
                                                                        <div class="graph-legend-icon"><i
                                                                                class="fa fa-minus pointer"
                                                                                style="color:#EAB839"></i></div>
                                                                        <div class="graph-legend-alias"><a>Usage</a>
                                                                        </div>
                                                                        <div class="graph-legend-value current">359.65
                                                                            GiB
                                                                        </div>
                                                                    </div>
                                                                    <div class="graph-legend-series"
                                                                         data-series-index="2">
                                                                        <div class="graph-legend-icon"><i
                                                                                class="fa fa-minus pointer"
                                                                                style="color:#6ED0E0"></i></div>
                                                                        <div class="graph-legend-alias"><a>Working
                                                                            Set</a></div>
                                                                        <div class="graph-legend-value current">249.47
                                                                            GiB
                                                                        </div>
                                                                    </div>
                                                                </section>
                                                            </div>
                                                            <!-- end ngIf: panel.legend.show -->
                                                        </div>

                                                        <div class="clearfix ng-scope"></div>

                                                    </ng-transclude>
                                                </div>
                                                <panel-resizer><span class="resize-panel-handle"></span></panel-resizer>
                                            </div>

                                            <!-- ngIf: editMode -->

                                        </grafana-panel>


                                    </grafana-panel-graph>
                                </panel-loader>
                            </div>
                            <!-- end ngRepeat: (name, panel) in row.panels track by panel.id -->

                            <div panel-drop-zone="" class="panel panel-drop-zone" ui-on-drop="onDrop($data, row)"
                                 data-drop="true">
                                <div class="panel-container" style="background: transparent">
                                    <div style="text-align: center">
                                        <em>Drop here</em>
                                    </div>
                                </div>
                            </div>

                            <div class="clearfix"></div>
                        </div>
                        <!-- end ngIf: !row.collapse -->
                    </div>
                </div>
                <!-- end ngRepeat: (row_name, row) in dashboard.rows -->
                <div class="grafana-row ng-scope" ng-controller="RowCtrl" ng-repeat="(row_name, row) in dashboard.rows"
                     row-height="" style="min-height: 250px;">
                    <div class="row-control">
                        <div class="row-control-inner">
                            <div class="row-close ng-hide" ng-show="row.collapse" data-placement="bottom">
                                <div class="row-close-buttons">
							<span class="row-button bgPrimary" ng-click="toggleRow(row)">
								<i bs-tooltip="'Expand row'" data-placement="right"
                                   class="fa fa-caret-left pointer ng-scope" data-original-title="" title=""></i>
							</span>
                                </div>
                                <div class="row-text pointer ng-binding" ng-click="toggleRow(row)"
                                     ng-bind="row.title | interpolateTemplateVars:this">Memory Usage Group By Node
                                </div>
                            </div>
                            <div class="row-open" ng-show="!row.collapse">
                                <div class="row-tab bgSuccess dropdown" ng-show="dashboardMeta.canEdit"
                                     ng-hide="dashboardViewState.fullscreen">
							<span class="row-tab-button dropdown-toggle" data-toggle="dropdown">
								<i class="fa fa-bars"></i>
							</span>
                                    <ul class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="drop1">
                                        <li>
                                            <a ng-click="toggleRow(row)">Collapse row</a>
                                        </li>
                                        <li class="dropdown-submenu">
                                            <a href="javascript:void(0);">Add Panel</a>
                                            <ul class="dropdown-menu">
                                                <!-- ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Graph</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Table</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Single
                                                        stat</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Text</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Dashboard
                                                        list</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                            </ul>
                                        </li>
                                        <li class="dropdown-submenu">
                                            <a href="javascript:void(0);">Set height</a>
                                            <ul class="dropdown-menu">
                                                <li><a ng-click="setHeight('25px')">25 px</a></li>
                                                <li><a ng-click="setHeight('100px')">100 px</a></li>
                                                <li><a ng-click="setHeight('150px')">150 px</a></li>
                                                <li><a ng-click="setHeight('200px')">200 px</a></li>
                                                <li><a ng-click="setHeight('250px')">250 px</a></li>
                                                <li><a ng-click="setHeight('300px')">300 px</a></li>
                                                <li><a ng-click="setHeight('350px')">350 px</a></li>
                                                <li><a ng-click="setHeight('450px')">450 px</a></li>
                                                <li><a ng-click="setHeight('500px')">500 px</a></li>
                                                <li><a ng-click="setHeight('600px')">600 px</a></li>
                                                <li><a ng-click="setHeight('700px')">700 px</a></li>
                                            </ul>
                                        </li>
                                        <li class="dropdown-submenu">
                                            <a href="javascript:void(0);">Move</a>
                                            <ul class="dropdown-menu">
                                                <li><a ng-click="moveRow('up')">Up</a></li>
                                                <li><a ng-click="moveRow('down')">Down</a></li>
                                                <li><a ng-click="moveRow('top')">To top</a></li>
                                                <li><a ng-click="moveRow('bottom')">To Bottom</a></li>
                                            </ul>
                                        </li>
                                        <li>
                                            <a dash-editor-link="app/partials/roweditor.html">Row editor</a>
                                        </li>
                                        <li>
                                            <a ng-click="deleteRow()">Delete row</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <!-- ngIf: !row.collapse -->
                        <div class="panels-wrapper ng-scope" ng-if="!row.collapse">
                            <!-- ngIf: row.showTitle -->
                            <div class="row-text pointer ng-binding ng-scope" ng-click="toggleRow(row)"
                                 ng-if="row.showTitle" ng-bind="row.title | interpolateTemplateVars:this">Memory Usage
                                Group By Node
                            </div>
                            <!-- end ngIf: row.showTitle -->

                            <!-- ngRepeat: (name, panel) in row.panels track by panel.id -->
                            <div ng-repeat="(name, panel) in row.panels track by panel.id" class="panel ng-scope"
                                 ui-draggable="!dashboardViewState.fullscreen" drag="panel.id"
                                 ui-on-drop="onDrop($data, row, panel)" drag-handle-class="drag-handle" panel-width=""
                                 draggable="true" style="width: 100%;">
                                <panel-loader type="panel.type" class="panel-margin">
                                    <grafana-panel-graph class="ng-scope">
                                        <grafana-panel>
                                            <div class="panel-container"
                                                 ng-class="{'panel-transparent': panel.transparent}"
                                                 style="min-height: 250px; display: block;">
                                                <div class="panel-header">
                                                    <!-- ngIf: panelMeta.error -->

		<span class="panel-loading ng-hide" ng-show="panelMeta.loading">
			<i class="fa fa-spinner fa-spin"></i>
		</span>

                                                    <div class="panel-title-container drag-handle" panel-menu=""><span
                                                            class="panel-title drag-handle pointer ng-scope"><span
                                                            class="panel-title-text drag-handle ng-binding"></span><span
                                                            class="panel-links-btn" style="display: none;"><i
                                                            class="fa fa-external-link"></i></span><span
                                                            class="panel-time-info ng-binding ng-hide"
                                                            ng-show="panelMeta.timeInfo"><i class="fa fa-clock-o"></i> </span></span>
                                                    </div>
                                                </div>

                                                <div class="panel-content">
                                                    <ng-transclude>

                                                        <div class="graph-wrapper ng-scope"
                                                             ng-class="{'graph-legend-rightside': panel.legend.rightSide}">
                                                            <div class="graph-canvas-wrapper">

                                                                <!-- ngIf: datapointsWarning -->

                                                                <div grafana-graph="" class="histogram-chart"
                                                                     style="height: 210px; padding: 0px;">
                                                                    <canvas class="flot-base" width="1115" height="210"
                                                                            style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 1115px; height: 210px;"></canvas>

                                                                    <canvas class="flot-overlay" width="1115"
                                                                            height="210"
                                                                            style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 1115px; height: 210px;"></canvas>
                                                                </div>

                                                            </div>

                                                            <!-- ngIf: panel.legend.show -->
                                                            <div class="graph-legend-wrapper ng-scope"
                                                                 ng-if="panel.legend.show" graph-legend="">
                                                                <section class="graph-legend">
                                                                    <div class="graph-legend-series"
                                                                         data-series-index="0">
                                                                        <div class="graph-legend-icon"><i
                                                                                class="fa fa-minus pointer"
                                                                                style="color:#7EB26D"></i></div>
                                                                        <div class="graph-legend-alias"><a>Limit
                                                                            {minion1}</a></div>
                                                                        <div class="graph-legend-value current">15.51
                                                                            GiB
                                                                        </div>
                                                                    </div>
                                                                    <div class="graph-legend-series"
                                                                         data-series-index="1">
                                                                        <div class="graph-legend-icon"><i
                                                                                class="fa fa-minus pointer"
                                                                                style="color:#EAB839"></i></div>
                                                                        <div class="graph-legend-alias"><a>Limit
                                                                            {minion202}</a></div>
                                                                        <div class="graph-legend-value current">15.51
                                                                            GiB
                                                                        </div>
                                                                    </div>
                                                                    <div class="graph-legend-series"
                                                                         data-series-index="2">
                                                                        <div class="graph-legend-icon"><i
                                                                                class="fa fa-minus pointer"
                                                                                style="color:#6ED0E0"></i></div>
                                                                        <div class="graph-legend-alias"><a>Usage
                                                                            {minion1}</a></div>
                                                                        <div class="graph-legend-value current">5.15
                                                                            GiB
                                                                        </div>
                                                                    </div>
                                                                    <div class="graph-legend-series"
                                                                         data-series-index="3">
                                                                        <div class="graph-legend-icon"><i
                                                                                class="fa fa-minus pointer"
                                                                                style="color:#EF843C"></i></div>
                                                                        <div class="graph-legend-alias"><a>Usage
                                                                            {minion202}</a></div>
                                                                        <div class="graph-legend-value current">2.19
                                                                            GiB
                                                                        </div>
                                                                    </div>
                                                                </section>
                                                            </div>
                                                            <!-- end ngIf: panel.legend.show -->
                                                        </div>

                                                        <div class="clearfix ng-scope"></div>

                                                    </ng-transclude>
                                                </div>
                                                <panel-resizer><span class="resize-panel-handle"></span></panel-resizer>
                                            </div>

                                            <!-- ngIf: editMode -->

                                        </grafana-panel>


                                    </grafana-panel-graph>
                                </panel-loader>
                            </div>
                            <!-- end ngRepeat: (name, panel) in row.panels track by panel.id -->

                            <div panel-drop-zone="" class="panel panel-drop-zone" ui-on-drop="onDrop($data, row)"
                                 data-drop="true">
                                <div class="panel-container" style="background: transparent">
                                    <div style="text-align: center">
                                        <em>Drop here</em>
                                    </div>
                                </div>
                            </div>

                            <div class="clearfix"></div>
                        </div>
                        <!-- end ngIf: !row.collapse -->
                    </div>
                </div>
                <!-- end ngRepeat: (row_name, row) in dashboard.rows -->
                <div class="grafana-row ng-scope" ng-controller="RowCtrl" ng-repeat="(row_name, row) in dashboard.rows"
                     row-height="" style="min-height: 250px;">
                    <div class="row-control">
                        <div class="row-control-inner">
                            <div class="row-close ng-hide" ng-show="row.collapse" data-placement="bottom">
                                <div class="row-close-buttons">
							<span class="row-button bgPrimary" ng-click="toggleRow(row)">
								<i bs-tooltip="'Expand row'" data-placement="right"
                                   class="fa fa-caret-left pointer ng-scope" data-original-title="" title=""></i>
							</span>
                                </div>
                                <div class="row-text pointer ng-binding" ng-click="toggleRow(row)"
                                     ng-bind="row.title | interpolateTemplateVars:this">Individual Node Memory Usage
                                </div>
                            </div>
                            <div class="row-open" ng-show="!row.collapse">
                                <div class="row-tab bgSuccess dropdown" ng-show="dashboardMeta.canEdit"
                                     ng-hide="dashboardViewState.fullscreen">
							<span class="row-tab-button dropdown-toggle" data-toggle="dropdown">
								<i class="fa fa-bars"></i>
							</span>
                                    <ul class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="drop1">
                                        <li>
                                            <a ng-click="toggleRow(row)">Collapse row</a>
                                        </li>
                                        <li class="dropdown-submenu">
                                            <a href="javascript:void(0);">Add Panel</a>
                                            <ul class="dropdown-menu">
                                                <!-- ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Graph</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Table</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Single
                                                        stat</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Text</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                                <li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">
                                                    <a ng-click="addPanelDefault(key)" bo-text="value.name">Dashboard
                                                        list</a>
                                                </li>
                                                <!-- end ngRepeat: (key, value) in panels -->
                                            </ul>
                                        </li>
                                        <li class="dropdown-submenu">
                                            <a href="javascript:void(0);">Set height</a>
                                            <ul class="dropdown-menu">
                                                <li><a ng-click="setHeight('25px')">25 px</a></li>
                                                <li><a ng-click="setHeight('100px')">100 px</a></li>
                                                <li><a ng-click="setHeight('150px')">150 px</a></li>
                                                <li><a ng-click="setHeight('200px')">200 px</a></li>
                                                <li><a ng-click="setHeight('250px')">250 px</a></li>
                                                <li><a ng-click="setHeight('300px')">300 px</a></li>
                                                <li><a ng-click="setHeight('350px')">350 px</a></li>
                                                <li><a ng-click="setHeight('450px')">450 px</a></li>
                                                <li><a ng-click="setHeight('500px')">500 px</a></li>
                                                <li><a ng-click="setHeight('600px')">600 px</a></li>
                                                <li><a ng-click="setHeight('700px')">700 px</a></li>
                                            </ul>
                                        </li>
                                        <li class="dropdown-submenu">
                                            <a href="javascript:void(0);">Move</a>
                                            <ul class="dropdown-menu">
                                                <li><a ng-click="moveRow('up')">Up</a></li>
                                                <li><a ng-click="moveRow('down')">Down</a></li>
                                                <li><a ng-click="moveRow('top')">To top</a></li>
                                                <li><a ng-click="moveRow('bottom')">To Bottom</a></li>
                                            </ul>
                                        </li>
                                        <li>
                                            <a dash-editor-link="app/partials/roweditor.html">Row editor</a>
                                        </li>
                                        <li>
                                            <a ng-click="deleteRow()">Delete row</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <!-- ngIf: !row.collapse -->
                        <div class="panels-wrapper ng-scope" ng-if="!row.collapse">
                            <!-- ngIf: row.showTitle -->
                            <div class="row-text pointer ng-binding ng-scope" ng-click="toggleRow(row)"
                                 ng-if="row.showTitle" ng-bind="row.title | interpolateTemplateVars:this">Individual
                                Node Memory Usage
                            </div>
                            <!-- end ngIf: row.showTitle -->

                            <!-- ngRepeat: (name, panel) in row.panels track by panel.id -->
                            <div ng-repeat="(name, panel) in row.panels track by panel.id" class="panel ng-scope"
                                 ui-draggable="!dashboardViewState.fullscreen" drag="panel.id"
                                 ui-on-drop="onDrop($data, row, panel)" drag-handle-class="drag-handle" panel-width=""
                                 draggable="true" style="width: 50%;">
                                <panel-loader type="panel.type" class="panel-margin">
                                    <grafana-panel-graph class="ng-scope">
                                        <grafana-panel>
                                            <div class="panel-container"
                                                 ng-class="{'panel-transparent': panel.transparent}"
                                                 style="min-height: 250px; display: block;">
                                                <div class="panel-header">
                                                    <!-- ngIf: panelMeta.error -->

		<span class="panel-loading ng-hide" ng-show="panelMeta.loading">
			<i class="fa fa-spinner fa-spin"></i>
		</span>

                                                    <div class="panel-title-container drag-handle" panel-menu=""><span
                                                            class="panel-title drag-handle pointer ng-scope"><span
                                                            class="panel-title-text drag-handle ng-binding">minion1 Memory Usage</span><span
                                                            class="panel-links-btn" style="display: none;"><i
                                                            class="fa fa-external-link"></i></span><span
                                                            class="panel-time-info ng-binding ng-hide"
                                                            ng-show="panelMeta.timeInfo"><i class="fa fa-clock-o"></i> </span></span>
                                                    </div>
                                                </div>

                                                <div class="panel-content">
                                                    <ng-transclude>

                                                        <div class="graph-wrapper ng-scope"
                                                             ng-class="{'graph-legend-rightside': panel.legend.rightSide}">
                                                            <div class="graph-canvas-wrapper">

                                                                <!-- ngIf: datapointsWarning -->

                                                                <div grafana-graph="" class="histogram-chart"
                                                                     style="height: 195px; padding: 0px;">
                                                                    <canvas class="flot-base" width="542" height="195"
                                                                            style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 542px; height: 195px;"></canvas>

                                                                    <canvas class="flot-overlay" width="542"
                                                                            height="195"
                                                                            style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 542px; height: 195px;"></canvas>
                                                                </div>

                                                            </div>

                                                            <!-- ngIf: panel.legend.show -->
                                                            <div class="graph-legend-wrapper ng-scope"
                                                                 ng-if="panel.legend.show" graph-legend="">
                                                                <section class="graph-legend">
                                                                    <div class="graph-legend-series"
                                                                         data-series-index="0">
                                                                        <div class="graph-legend-icon"><i
                                                                                class="fa fa-minus pointer"
                                                                                style="color:#7EB26D"></i></div>
                                                                        <div class="graph-legend-alias"><a>Limit</a>
                                                                        </div>
                                                                        <div class="graph-legend-value current">15.51
                                                                            GiB
                                                                        </div>
                                                                    </div>
                                                                    <div class="graph-legend-series"
                                                                         data-series-index="1">
                                                                        <div class="graph-legend-icon"><i
                                                                                class="fa fa-minus pointer"
                                                                                style="color:#EAB839"></i></div>
                                                                        <div class="graph-legend-alias"><a>Usage</a>
                                                                        </div>
                                                                        <div class="graph-legend-value current">7.45
                                                                            GiB
                                                                        </div>
                                                                    </div>
                                                                    <div class="graph-legend-series"
                                                                         data-series-index="2">
                                                                        <div class="graph-legend-icon"><i
                                                                                class="fa fa-minus pointer"
                                                                                style="color:#6ED0E0"></i></div>
                                                                        <div class="graph-legend-alias"><a>Working
                                                                            Set</a></div>
                                                                        <div class="graph-legend-value current">5.15
                                                                            GiB
                                                                        </div>
                                                                    </div>
                                                                </section>
                                                            </div>
                                                            <!-- end ngIf: panel.legend.show -->
                                                        </div>

                                                        <div class="clearfix ng-scope"></div>

                                                    </ng-transclude>
                                                </div>
                                                <panel-resizer><span class="resize-panel-handle"></span></panel-resizer>
                                            </div>

                                            <!-- ngIf: editMode -->

                                        </grafana-panel>


                                    </grafana-panel-graph>
                                </panel-loader>
                            </div>
                            <!-- end ngRepeat: (name, panel) in row.panels track by panel.id -->
                            <div ng-repeat="(name, panel) in row.panels track by panel.id" class="panel ng-scope"
                                 ui-draggable="!dashboardViewState.fullscreen" drag="panel.id"
                                 ui-on-drop="onDrop($data, row, panel)" drag-handle-class="drag-handle" panel-width=""
                                 draggable="true" style="width: 50%;">
                                <panel-loader type="panel.type" class="panel-margin">
                                    <grafana-panel-graph class="ng-scope">
                                        <grafana-panel>
                                            <div class="panel-container"
                                                 ng-class="{'panel-transparent': panel.transparent}"
                                                 style="min-height: 250px; display: block;">
                                                <div class="panel-header">
                                                    <!-- ngIf: panelMeta.error -->

		<span class="panel-loading ng-hide" ng-show="panelMeta.loading">
			<i class="fa fa-spinner fa-spin"></i>
		</span>

                                                    <div class="panel-title-container drag-handle" panel-menu=""><span
                                                            class="panel-title drag-handle pointer ng-scope"><span
                                                            class="panel-title-text drag-handle ng-binding">minion202 Memory Usage</span><span
                                                            class="panel-links-btn" style="display: none;"><i
                                                            class="fa fa-external-link"></i></span><span
                                                            class="panel-time-info ng-binding ng-hide"
                                                            ng-show="panelMeta.timeInfo"><i class="fa fa-clock-o"></i> </span></span>
                                                    </div>
                                                </div>

                                                <div class="panel-content">
                                                    <ng-transclude>

                                                        <div class="graph-wrapper ng-scope"
                                                             ng-class="{'graph-legend-rightside': panel.legend.rightSide}">
                                                            <div class="graph-canvas-wrapper">

                                                                <!-- ngIf: datapointsWarning -->

                                                                <div grafana-graph="" class="histogram-chart"
                                                                     style="height: 195px; padding: 0px;">
                                                                    <canvas class="flot-base" width="542" height="195"
                                                                            style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 542px; height: 195px;"></canvas>

                                                                    <canvas class="flot-overlay" width="542"
                                                                            height="195"
                                                                            style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 542px; height: 195px;"></canvas>
                                                                </div>

                                                            </div>

                                                            <!-- ngIf: panel.legend.show -->
                                                            <div class="graph-legend-wrapper ng-scope"
                                                                 ng-if="panel.legend.show" graph-legend="">
                                                                <section class="graph-legend">
                                                                    <div class="graph-legend-series"
                                                                         data-series-index="0">
                                                                        <div class="graph-legend-icon"><i
                                                                                class="fa fa-minus pointer"
                                                                                style="color:#7EB26D"></i></div>
                                                                        <div class="graph-legend-alias"><a>Limit</a>
                                                                        </div>
                                                                        <div class="graph-legend-value current">15.51
                                                                            GiB
                                                                        </div>
                                                                    </div>
                                                                    <div class="graph-legend-series"
                                                                         data-series-index="1">
                                                                        <div class="graph-legend-icon"><i
                                                                                class="fa fa-minus pointer"
                                                                                style="color:#EAB839"></i></div>
                                                                        <div class="graph-legend-alias"><a>Usage</a>
                                                                        </div>
                                                                        <div class="graph-legend-value current">3.12
                                                                            GiB
                                                                        </div>
                                                                    </div>
                                                                    <div class="graph-legend-series"
                                                                         data-series-index="2">
                                                                        <div class="graph-legend-icon"><i
                                                                                class="fa fa-minus pointer"
                                                                                style="color:#6ED0E0"></i></div>
                                                                        <div class="graph-legend-alias"><a>Working
                                                                            Set</a></div>
                                                                        <div class="graph-legend-value current">2.19
                                                                            GiB
                                                                        </div>
                                                                    </div>
                                                                </section>
                                                            </div>
                                                            <!-- end ngIf: panel.legend.show -->
                                                        </div>

                                                        <div class="clearfix ng-scope"></div>

                                                    </ng-transclude>
                                                </div>
                                                <panel-resizer><span class="resize-panel-handle"></span></panel-resizer>
                                            </div>

                                            <!-- ngIf: editMode -->

                                        </grafana-panel>


                                    </grafana-panel-graph>
                                </panel-loader>
                            </div>
                            <!-- end ngRepeat: (name, panel) in row.panels track by panel.id -->

                            <div panel-drop-zone="" class="panel panel-drop-zone" ui-on-drop="onDrop($data, row)"
                                 data-drop="true">
                                <div class="panel-container" style="background: transparent">
                                    <div style="text-align: center">
                                        <em>Drop here</em>
                                    </div>
                                </div>
                            </div>

                            <div class="clearfix"></div>
                        </div>
                        <!-- end ngIf: !row.collapse -->
                    </div>
                </div>
                <!-- end ngRepeat: (row_name, row) in dashboard.rows -->
                <%--<div class="grafana-row ng-scope" ng-controller="RowCtrl" ng-repeat="(row_name, row) in dashboard.rows"--%>
                <%--row-height="" style="min-height: 250px;">--%>
                <%--<div class="row-control">--%>
                <%--<div class="row-control-inner">--%>
                <%--<div class="row-close ng-hide" ng-show="row.collapse" data-placement="bottom">--%>
                <%--<div class="row-close-buttons">--%>
                <%--<span class="row-button bgPrimary" ng-click="toggleRow(row)">--%>
                <%--<i bs-tooltip="'Expand row'" data-placement="right"--%>
                <%--class="fa fa-caret-left pointer ng-scope" data-original-title="" title=""></i>--%>
                <%--</span>--%>
                <%--</div>--%>
                <%--<div class="row-text pointer ng-binding" ng-click="toggleRow(row)"--%>
                <%--ng-bind="row.title | interpolateTemplateVars:this">CPU Usage Group By Node--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="row-open" ng-show="!row.collapse">--%>
                <%--<div class="row-tab bgSuccess dropdown" ng-show="dashboardMeta.canEdit"--%>
                <%--ng-hide="dashboardViewState.fullscreen">--%>
                <%--<span class="row-tab-button dropdown-toggle" data-toggle="dropdown">--%>
                <%--<i class="fa fa-bars"></i>--%>
                <%--</span>--%>
                <%--<ul class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="drop1">--%>
                <%--<li>--%>
                <%--<a ng-click="toggleRow(row)">Collapse row</a>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Add Panel</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<!-- ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Graph</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Table</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Single--%>
                <%--stat</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Text</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Dashboard--%>
                <%--list</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Set height</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<li><a ng-click="setHeight('25px')">25 px</a></li>--%>
                <%--<li><a ng-click="setHeight('100px')">100 px</a></li>--%>
                <%--<li><a ng-click="setHeight('150px')">150 px</a></li>--%>
                <%--<li><a ng-click="setHeight('200px')">200 px</a></li>--%>
                <%--<li><a ng-click="setHeight('250px')">250 px</a></li>--%>
                <%--<li><a ng-click="setHeight('300px')">300 px</a></li>--%>
                <%--<li><a ng-click="setHeight('350px')">350 px</a></li>--%>
                <%--<li><a ng-click="setHeight('450px')">450 px</a></li>--%>
                <%--<li><a ng-click="setHeight('500px')">500 px</a></li>--%>
                <%--<li><a ng-click="setHeight('600px')">600 px</a></li>--%>
                <%--<li><a ng-click="setHeight('700px')">700 px</a></li>--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Move</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<li><a ng-click="moveRow('up')">Up</a></li>--%>
                <%--<li><a ng-click="moveRow('down')">Down</a></li>--%>
                <%--<li><a ng-click="moveRow('top')">To top</a></li>--%>
                <%--<li><a ng-click="moveRow('bottom')">To Bottom</a></li>--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li>--%>
                <%--<a dash-editor-link="app/partials/roweditor.html">Row editor</a>--%>
                <%--</li>--%>
                <%--<li>--%>
                <%--<a ng-click="deleteRow()">Delete row</a>--%>
                <%--</li>--%>
                <%--</ul>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<!-- ngIf: !row.collapse -->--%>
                <%--<div class="panels-wrapper ng-scope" ng-if="!row.collapse">--%>
                <%--<!-- ngIf: row.showTitle -->--%>
                <%--<div class="row-text pointer ng-binding ng-scope" ng-click="toggleRow(row)"--%>
                <%--ng-if="row.showTitle" ng-bind="row.title | interpolateTemplateVars:this">CPU Usage--%>
                <%--Group By Node--%>
                <%--</div>--%>
                <%--<!-- end ngIf: row.showTitle -->--%>

                <%--<!-- ngRepeat: (name, panel) in row.panels track by panel.id -->--%>
                <%--<div ng-repeat="(name, panel) in row.panels track by panel.id" class="panel ng-scope"--%>
                <%--ui-draggable="!dashboardViewState.fullscreen" drag="panel.id"--%>
                <%--ui-on-drop="onDrop($data, row, panel)" drag-handle-class="drag-handle" panel-width=""--%>
                <%--draggable="true" style="width: 100%;">--%>
                <%--<panel-loader type="panel.type" class="panel-margin">--%>
                <%--<grafana-panel-graph class="ng-scope">--%>
                <%--<grafana-panel>--%>
                <%--<div class="panel-container"--%>
                <%--ng-class="{'panel-transparent': panel.transparent}"--%>
                <%--style="min-height: 250px; display: block;">--%>
                <%--<div class="panel-header">--%>
                <%--<!-- ngIf: panelMeta.error -->--%>

                <%--<span class="panel-loading ng-hide" ng-show="panelMeta.loading">--%>
                <%--<i class="fa fa-spinner fa-spin"></i>--%>
                <%--</span>--%>

                <%--<div class="panel-title-container drag-handle" panel-menu=""><span--%>
                <%--class="panel-title drag-handle pointer ng-scope"><span--%>
                <%--class="panel-title-text drag-handle ng-binding"></span><span--%>
                <%--class="panel-links-btn" style="display: none;"><i--%>
                <%--class="fa fa-external-link"></i></span><span--%>
                <%--class="panel-time-info ng-binding ng-hide"--%>
                <%--ng-show="panelMeta.timeInfo"><i class="fa fa-clock-o"></i> </span></span>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<div class="panel-content">--%>
                <%--<ng-transclude>--%>

                <%--<div class="graph-wrapper ng-scope"--%>
                <%--ng-class="{'graph-legend-rightside': panel.legend.rightSide}">--%>
                <%--<div class="graph-canvas-wrapper">--%>

                <%--<!-- ngIf: datapointsWarning -->--%>

                <%--<div grafana-graph="" class="histogram-chart"--%>
                <%--style="height: 210px; padding: 0px;">--%>
                <%--<canvas class="flot-base" width="1115" height="210"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 1115px; height: 210px;"></canvas>--%>
                <%----%>
                <%--<canvas class="flot-overlay" width="1115"--%>
                <%--height="210"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 1115px; height: 210px;"></canvas>--%>
                <%--</div>--%>

                <%--</div>--%>

                <%--<!-- ngIf: panel.legend.show -->--%>
                <%--<div class="graph-legend-wrapper ng-scope"--%>
                <%--ng-if="panel.legend.show" graph-legend="">--%>
                <%--<section class="graph-legend">--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="0">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#7EB26D"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Limit--%>
                <%--{minion1}</a></div>--%>
                <%--<div class="graph-legend-value current">4.00 s--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="1">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#EAB839"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Limit--%>
                <%--{minion202}</a></div>--%>
                <%--<div class="graph-legend-value current">4.00 s--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="2">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#6ED0E0"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Usage--%>
                <%--{minion1}</a></div>--%>
                <%--<div class="graph-legend-value current">144.20--%>
                <%--ms--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="3">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#EF843C"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Usage--%>
                <%--{minion202}</a></div>--%>
                <%--<div class="graph-legend-value current">86.43--%>
                <%--ms--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</section>--%>
                <%--</div>--%>
                <%--<!-- end ngIf: panel.legend.show -->--%>
                <%--</div>--%>

                <%--<div class="clearfix ng-scope"></div>--%>

                <%--</ng-transclude>--%>
                <%--</div>--%>
                <%--<panel-resizer><span class="resize-panel-handle"></span></panel-resizer>--%>
                <%--</div>--%>

                <%--<!-- ngIf: editMode -->--%>

                <%--</grafana-panel>--%>


                <%--</grafana-panel-graph>--%>
                <%--</panel-loader>--%>
                <%--</div>--%>
                <%--<!-- end ngRepeat: (name, panel) in row.panels track by panel.id -->--%>

                <%--<div panel-drop-zone="" class="panel panel-drop-zone" ui-on-drop="onDrop($data, row)"--%>
                <%--data-drop="true">--%>
                <%--<div class="panel-container" style="background: transparent">--%>
                <%--<div style="text-align: center">--%>
                <%--<em>Drop here</em>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<div class="clearfix"></div>--%>
                <%--</div>--%>
                <%--<!-- end ngIf: !row.collapse -->--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<!-- end ngRepeat: (row_name, row) in dashboard.rows -->--%>
                <%--<div class="grafana-row ng-scope" ng-controller="RowCtrl" ng-repeat="(row_name, row) in dashboard.rows"--%>
                <%--row-height="" style="min-height: 250px;">--%>
                <%--<div class="row-control">--%>
                <%--<div class="row-control-inner">--%>
                <%--<div class="row-close ng-hide" ng-show="row.collapse" data-placement="bottom">--%>
                <%--<div class="row-close-buttons">--%>
                <%--<span class="row-button bgPrimary" ng-click="toggleRow(row)">--%>
                <%--<i bs-tooltip="'Expand row'" data-placement="right"--%>
                <%--class="fa fa-caret-left pointer ng-scope" data-original-title="" title=""></i>--%>
                <%--</span>--%>
                <%--</div>--%>
                <%--<div class="row-text pointer ng-binding" ng-click="toggleRow(row)"--%>
                <%--ng-bind="row.title | interpolateTemplateVars:this">Individual Node CPU Usage--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="row-open" ng-show="!row.collapse">--%>
                <%--<div class="row-tab bgSuccess dropdown" ng-show="dashboardMeta.canEdit"--%>
                <%--ng-hide="dashboardViewState.fullscreen">--%>
                <%--<span class="row-tab-button dropdown-toggle" data-toggle="dropdown">--%>
                <%--<i class="fa fa-bars"></i>--%>
                <%--</span>--%>
                <%--<ul class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="drop1">--%>
                <%--<li>--%>
                <%--<a ng-click="toggleRow(row)">Collapse row</a>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Add Panel</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<!-- ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Graph</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Table</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Single--%>
                <%--stat</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Text</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Dashboard--%>
                <%--list</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Set height</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<li><a ng-click="setHeight('25px')">25 px</a></li>--%>
                <%--<li><a ng-click="setHeight('100px')">100 px</a></li>--%>
                <%--<li><a ng-click="setHeight('150px')">150 px</a></li>--%>
                <%--<li><a ng-click="setHeight('200px')">200 px</a></li>--%>
                <%--<li><a ng-click="setHeight('250px')">250 px</a></li>--%>
                <%--<li><a ng-click="setHeight('300px')">300 px</a></li>--%>
                <%--<li><a ng-click="setHeight('350px')">350 px</a></li>--%>
                <%--<li><a ng-click="setHeight('450px')">450 px</a></li>--%>
                <%--<li><a ng-click="setHeight('500px')">500 px</a></li>--%>
                <%--<li><a ng-click="setHeight('600px')">600 px</a></li>--%>
                <%--<li><a ng-click="setHeight('700px')">700 px</a></li>--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Move</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<li><a ng-click="moveRow('up')">Up</a></li>--%>
                <%--<li><a ng-click="moveRow('down')">Down</a></li>--%>
                <%--<li><a ng-click="moveRow('top')">To top</a></li>--%>
                <%--<li><a ng-click="moveRow('bottom')">To Bottom</a></li>--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li>--%>
                <%--<a dash-editor-link="app/partials/roweditor.html">Row editor</a>--%>
                <%--</li>--%>
                <%--<li>--%>
                <%--<a ng-click="deleteRow()">Delete row</a>--%>
                <%--</li>--%>
                <%--</ul>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<!-- ngIf: !row.collapse -->--%>
                <%--<div class="panels-wrapper ng-scope" ng-if="!row.collapse">--%>
                <%--<!-- ngIf: row.showTitle -->--%>
                <%--<div class="row-text pointer ng-binding ng-scope" ng-click="toggleRow(row)"--%>
                <%--ng-if="row.showTitle" ng-bind="row.title | interpolateTemplateVars:this">Individual--%>
                <%--Node CPU Usage--%>
                <%--</div>--%>
                <%--<!-- end ngIf: row.showTitle -->--%>

                <%--<!-- ngRepeat: (name, panel) in row.panels track by panel.id -->--%>
                <%--<div ng-repeat="(name, panel) in row.panels track by panel.id" class="panel ng-scope"--%>
                <%--ui-draggable="!dashboardViewState.fullscreen" drag="panel.id"--%>
                <%--ui-on-drop="onDrop($data, row, panel)" drag-handle-class="drag-handle" panel-width=""--%>
                <%--draggable="true" style="width: 50%;">--%>
                <%--<panel-loader type="panel.type" class="panel-margin">--%>
                <%--<grafana-panel-graph class="ng-scope">--%>
                <%--<grafana-panel>--%>
                <%--<div class="panel-container"--%>
                <%--ng-class="{'panel-transparent': panel.transparent}"--%>
                <%--style="min-height: 250px; display: block;">--%>
                <%--<div class="panel-header">--%>
                <%--<!-- ngIf: panelMeta.error -->--%>

                <%--<span class="panel-loading ng-hide" ng-show="panelMeta.loading">--%>
                <%--<i class="fa fa-spinner fa-spin"></i>--%>
                <%--</span>--%>

                <%--<div class="panel-title-container drag-handle" panel-menu=""><span--%>
                <%--class="panel-title drag-handle pointer ng-scope"><span--%>
                <%--class="panel-title-text drag-handle ng-binding">minion1 CPU Usage</span><span--%>
                <%--class="panel-links-btn" style="display: none;"><i--%>
                <%--class="fa fa-external-link"></i></span><span--%>
                <%--class="panel-time-info ng-binding ng-hide"--%>
                <%--ng-show="panelMeta.timeInfo"><i class="fa fa-clock-o"></i> </span></span>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<div class="panel-content">--%>
                <%--<ng-transclude>--%>

                <%--<div class="graph-wrapper ng-scope"--%>
                <%--ng-class="{'graph-legend-rightside': panel.legend.rightSide}">--%>
                <%--<div class="graph-canvas-wrapper">--%>

                <%--<!-- ngIf: datapointsWarning -->--%>

                <%--<div grafana-graph="" class="histogram-chart"--%>
                <%--style="height: 195px; padding: 0px;">--%>
                <%--<canvas class="flot-base" width="542" height="195"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 542px; height: 195px;"></canvas>--%>
                <%----%>
                <%--<canvas class="flot-overlay" width="542"--%>
                <%--height="195"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 542px; height: 195px;"></canvas>--%>
                <%--</div>--%>

                <%--</div>--%>

                <%--<!-- ngIf: panel.legend.show -->--%>
                <%--<div class="graph-legend-wrapper ng-scope"--%>
                <%--ng-if="panel.legend.show" graph-legend="">--%>
                <%--<section class="graph-legend">--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="0">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#7EB26D"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Limit</a>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-value current">4.00 s--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="1">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#EAB839"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Usage</a>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-value current">144.20--%>
                <%--ms--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</section>--%>
                <%--</div>--%>
                <%--<!-- end ngIf: panel.legend.show -->--%>
                <%--</div>--%>

                <%--<div class="clearfix ng-scope"></div>--%>

                <%--</ng-transclude>--%>
                <%--</div>--%>
                <%--<panel-resizer><span class="resize-panel-handle"></span></panel-resizer>--%>
                <%--</div>--%>

                <%--<!-- ngIf: editMode -->--%>

                <%--</grafana-panel>--%>


                <%--</grafana-panel-graph>--%>
                <%--</panel-loader>--%>
                <%--</div>--%>
                <%--<!-- end ngRepeat: (name, panel) in row.panels track by panel.id -->--%>
                <%--<div ng-repeat="(name, panel) in row.panels track by panel.id" class="panel ng-scope"--%>
                <%--ui-draggable="!dashboardViewState.fullscreen" drag="panel.id"--%>
                <%--ui-on-drop="onDrop($data, row, panel)" drag-handle-class="drag-handle" panel-width=""--%>
                <%--draggable="true" style="width: 50%;">--%>
                <%--<panel-loader type="panel.type" class="panel-margin">--%>
                <%--<grafana-panel-graph class="ng-scope">--%>
                <%--<grafana-panel>--%>
                <%--<div class="panel-container"--%>
                <%--ng-class="{'panel-transparent': panel.transparent}"--%>
                <%--style="min-height: 250px; display: block;">--%>
                <%--<div class="panel-header">--%>
                <%--<!-- ngIf: panelMeta.error -->--%>

                <%--<span class="panel-loading ng-hide" ng-show="panelMeta.loading">--%>
                <%--<i class="fa fa-spinner fa-spin"></i>--%>
                <%--</span>--%>

                <%--<div class="panel-title-container drag-handle" panel-menu=""><span--%>
                <%--class="panel-title drag-handle pointer ng-scope"><span--%>
                <%--class="panel-title-text drag-handle ng-binding">minion202 CPU Usage</span><span--%>
                <%--class="panel-links-btn" style="display: none;"><i--%>
                <%--class="fa fa-external-link"></i></span><span--%>
                <%--class="panel-time-info ng-binding ng-hide"--%>
                <%--ng-show="panelMeta.timeInfo"><i class="fa fa-clock-o"></i> </span></span>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<div class="panel-content">--%>
                <%--<ng-transclude>--%>

                <%--<div class="graph-wrapper ng-scope"--%>
                <%--ng-class="{'graph-legend-rightside': panel.legend.rightSide}">--%>
                <%--<div class="graph-canvas-wrapper">--%>

                <%--<!-- ngIf: datapointsWarning -->--%>

                <%--<div grafana-graph="" class="histogram-chart"--%>
                <%--style="height: 195px; padding: 0px;">--%>
                <%--<canvas class="flot-base" width="542" height="195"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 542px; height: 195px;"></canvas>--%>
                <%--&lt;%&ndash;<div class="flot-text"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; font-size: smaller; color: rgb(84, 84, 84);">--%>
                <%--<div class="flot-x-axis flot-x1-axis xAxis x1Axis"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; display: block;">--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 17px; text-align: center;">--%>
                <%--10:15--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 100px; text-align: center;">--%>
                <%--10:20--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 182px; text-align: center;">--%>
                <%--10:25--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 264px; text-align: center;">--%>
                <%--10:30--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 347px; text-align: center;">--%>
                <%--10:35--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 429px; text-align: center;">--%>
                <%--10:40--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="flot-y-axis flot-y1-axis yAxis y1Axis"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; display: block;">--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 160px; left: 1px; text-align: right;">--%>
                <%--0 ms--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 128px; left: 1px; text-align: right;">--%>
                <%--1.0 s--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 96px; left: 1px; text-align: right;">--%>
                <%--2.0 s--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 64px; left: 1px; text-align: right;">--%>
                <%--3.0 s--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 32px; left: 1px; text-align: right;">--%>
                <%--4.0 s--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 0px; left: 1px; text-align: right;">--%>
                <%--5.0 s--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>&ndash;%&gt;--%>
                <%--<canvas class="flot-overlay" width="542"--%>
                <%--height="195"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 542px; height: 195px;"></canvas>--%>
                <%--</div>--%>

                <%--</div>--%>

                <%--<!-- ngIf: panel.legend.show -->--%>
                <%--<div class="graph-legend-wrapper ng-scope"--%>
                <%--ng-if="panel.legend.show" graph-legend="">--%>
                <%--<section class="graph-legend">--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="0">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#7EB26D"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Limit</a>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-value current">4.00 s--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="1">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#EAB839"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Usage</a>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-value current">86.43--%>
                <%--ms--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</section>--%>
                <%--</div>--%>
                <%--<!-- end ngIf: panel.legend.show -->--%>
                <%--</div>--%>

                <%--<div class="clearfix ng-scope"></div>--%>

                <%--</ng-transclude>--%>
                <%--</div>--%>
                <%--<panel-resizer><span class="resize-panel-handle"></span></panel-resizer>--%>
                <%--</div>--%>

                <%--<!-- ngIf: editMode -->--%>

                <%--</grafana-panel>--%>


                <%--</grafana-panel-graph>--%>
                <%--</panel-loader>--%>
                <%--</div>--%>
                <%--<!-- end ngRepeat: (name, panel) in row.panels track by panel.id -->--%>

                <%--<div panel-drop-zone="" class="panel panel-drop-zone" ui-on-drop="onDrop($data, row)"--%>
                <%--data-drop="true">--%>
                <%--<div class="panel-container" style="background: transparent">--%>
                <%--<div style="text-align: center">--%>
                <%--<em>Drop here</em>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<div class="clearfix"></div>--%>
                <%--</div>--%>
                <%--<!-- end ngIf: !row.collapse -->--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<!-- end ngRepeat: (row_name, row) in dashboard.rows -->--%>
                <%--<div class="grafana-row ng-scope" ng-controller="RowCtrl" ng-repeat="(row_name, row) in dashboard.rows"--%>
                <%--row-height="" style="min-height: 250px;">--%>
                <%--<div class="row-control">--%>
                <%--<div class="row-control-inner">--%>
                <%--<div class="row-close ng-hide" ng-show="row.collapse" data-placement="bottom">--%>
                <%--<div class="row-close-buttons">--%>
                <%--<span class="row-button bgPrimary" ng-click="toggleRow(row)">--%>
                <%--<i bs-tooltip="'Expand row'" data-placement="right"--%>
                <%--class="fa fa-caret-left pointer ng-scope" data-original-title="" title=""></i>--%>
                <%--</span>--%>
                <%--</div>--%>
                <%--<div class="row-text pointer ng-binding" ng-click="toggleRow(row)"--%>
                <%--ng-bind="row.title | interpolateTemplateVars:this">Overall Cluster Disk Usage--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="row-open" ng-show="!row.collapse">--%>
                <%--<div class="row-tab bgSuccess dropdown" ng-show="dashboardMeta.canEdit"--%>
                <%--ng-hide="dashboardViewState.fullscreen">--%>
                <%--<span class="row-tab-button dropdown-toggle" data-toggle="dropdown">--%>
                <%--<i class="fa fa-bars"></i>--%>
                <%--</span>--%>
                <%--<ul class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="drop1">--%>
                <%--<li>--%>
                <%--<a ng-click="toggleRow(row)">Collapse row</a>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Add Panel</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<!-- ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Graph</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Table</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Single--%>
                <%--stat</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Text</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Dashboard--%>
                <%--list</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Set height</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<li><a ng-click="setHeight('25px')">25 px</a></li>--%>
                <%--<li><a ng-click="setHeight('100px')">100 px</a></li>--%>
                <%--<li><a ng-click="setHeight('150px')">150 px</a></li>--%>
                <%--<li><a ng-click="setHeight('200px')">200 px</a></li>--%>
                <%--<li><a ng-click="setHeight('250px')">250 px</a></li>--%>
                <%--<li><a ng-click="setHeight('300px')">300 px</a></li>--%>
                <%--<li><a ng-click="setHeight('350px')">350 px</a></li>--%>
                <%--<li><a ng-click="setHeight('450px')">450 px</a></li>--%>
                <%--<li><a ng-click="setHeight('500px')">500 px</a></li>--%>
                <%--<li><a ng-click="setHeight('600px')">600 px</a></li>--%>
                <%--<li><a ng-click="setHeight('700px')">700 px</a></li>--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Move</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<li><a ng-click="moveRow('up')">Up</a></li>--%>
                <%--<li><a ng-click="moveRow('down')">Down</a></li>--%>
                <%--<li><a ng-click="moveRow('top')">To top</a></li>--%>
                <%--<li><a ng-click="moveRow('bottom')">To Bottom</a></li>--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li>--%>
                <%--<a dash-editor-link="app/partials/roweditor.html">Row editor</a>--%>
                <%--</li>--%>
                <%--<li>--%>
                <%--<a ng-click="deleteRow()">Delete row</a>--%>
                <%--</li>--%>
                <%--</ul>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<!-- ngIf: !row.collapse -->--%>
                <%--<div class="panels-wrapper ng-scope" ng-if="!row.collapse">--%>
                <%--<!-- ngIf: row.showTitle -->--%>
                <%--<div class="row-text pointer ng-binding ng-scope" ng-click="toggleRow(row)"--%>
                <%--ng-if="row.showTitle" ng-bind="row.title | interpolateTemplateVars:this">Overall--%>
                <%--Cluster Disk Usage--%>
                <%--</div>--%>
                <%--<!-- end ngIf: row.showTitle -->--%>

                <%--<!-- ngRepeat: (name, panel) in row.panels track by panel.id -->--%>
                <%--<div ng-repeat="(name, panel) in row.panels track by panel.id" class="panel ng-scope"--%>
                <%--ui-draggable="!dashboardViewState.fullscreen" drag="panel.id"--%>
                <%--ui-on-drop="onDrop($data, row, panel)" drag-handle-class="drag-handle" panel-width=""--%>
                <%--draggable="true" style="width: 100%;">--%>
                <%--<panel-loader type="panel.type" class="panel-margin">--%>
                <%--<grafana-panel-graph class="ng-scope">--%>
                <%--<grafana-panel>--%>
                <%--<div class="panel-container"--%>
                <%--ng-class="{'panel-transparent': panel.transparent}"--%>
                <%--style="min-height: 250px; display: block;">--%>
                <%--<div class="panel-header">--%>
                <%--<!-- ngIf: panelMeta.error -->--%>

                <%--<span class="panel-loading ng-hide" ng-show="panelMeta.loading">--%>
                <%--<i class="fa fa-spinner fa-spin"></i>--%>
                <%--</span>--%>

                <%--<div class="panel-title-container drag-handle" panel-menu=""><span--%>
                <%--class="panel-title drag-handle pointer ng-scope"><span--%>
                <%--class="panel-title-text drag-handle ng-binding"></span><span--%>
                <%--class="panel-links-btn" style="display: none;"><i--%>
                <%--class="fa fa-external-link"></i></span><span--%>
                <%--class="panel-time-info ng-binding ng-hide"--%>
                <%--ng-show="panelMeta.timeInfo"><i class="fa fa-clock-o"></i> </span></span>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<div class="panel-content">--%>
                <%--<ng-transclude>--%>

                <%--<div class="graph-wrapper ng-scope"--%>
                <%--ng-class="{'graph-legend-rightside': panel.legend.rightSide}">--%>
                <%--<div class="graph-canvas-wrapper">--%>

                <%--<!-- ngIf: datapointsWarning -->--%>

                <%--<div grafana-graph="" class="histogram-chart"--%>
                <%--style="height: 210px; padding: 0px;">--%>
                <%--<canvas class="flot-base" width="1115" height="210"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 1115px; height: 210px;"></canvas>--%>
                <%----%>
                <%--<canvas class="flot-overlay" width="1115"--%>
                <%--height="210"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 1115px; height: 210px;"></canvas>--%>
                <%--</div>--%>

                <%--</div>--%>

                <%--<!-- ngIf: panel.legend.show -->--%>
                <%--<div class="graph-legend-wrapper ng-scope"--%>
                <%--ng-if="panel.legend.show" graph-legend="">--%>
                <%--<section class="graph-legend">--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="0">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#7EB26D"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Limit</a>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-value current">1.13--%>
                <%--TiB--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="1">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#EAB839"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Usage</a>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-value current">136.55--%>
                <%--GiB--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</section>--%>
                <%--</div>--%>
                <%--<!-- end ngIf: panel.legend.show -->--%>
                <%--</div>--%>

                <%--<div class="clearfix ng-scope"></div>--%>

                <%--</ng-transclude>--%>
                <%--</div>--%>
                <%--<panel-resizer><span class="resize-panel-handle"></span></panel-resizer>--%>
                <%--</div>--%>

                <%--<!-- ngIf: editMode -->--%>

                <%--</grafana-panel>--%>


                <%--</grafana-panel-graph>--%>
                <%--</panel-loader>--%>
                <%--</div>--%>
                <%--<!-- end ngRepeat: (name, panel) in row.panels track by panel.id -->--%>

                <%--<div panel-drop-zone="" class="panel panel-drop-zone" ui-on-drop="onDrop($data, row)"--%>
                <%--data-drop="true">--%>
                <%--<div class="panel-container" style="background: transparent">--%>
                <%--<div style="text-align: center">--%>
                <%--<em>Drop here</em>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<div class="clearfix"></div>--%>
                <%--</div>--%>
                <%--<!-- end ngIf: !row.collapse -->--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<!-- end ngRepeat: (row_name, row) in dashboard.rows -->--%>
                <%--<div class="grafana-row ng-scope" ng-controller="RowCtrl" ng-repeat="(row_name, row) in dashboard.rows"--%>
                <%--row-height="" style="min-height: 250px;">--%>
                <%--<div class="row-control">--%>
                <%--<div class="row-control-inner">--%>
                <%--<div class="row-close ng-hide" ng-show="row.collapse" data-placement="bottom">--%>
                <%--<div class="row-close-buttons">--%>
                <%--<span class="row-button bgPrimary" ng-click="toggleRow(row)">--%>
                <%--<i bs-tooltip="'Expand row'" data-placement="right"--%>
                <%--class="fa fa-caret-left pointer ng-scope" data-original-title="" title=""></i>--%>
                <%--</span>--%>
                <%--</div>--%>
                <%--<div class="row-text pointer ng-binding" ng-click="toggleRow(row)"--%>
                <%--ng-bind="row.title | interpolateTemplateVars:this">Disk Usage Group By Node--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="row-open" ng-show="!row.collapse">--%>
                <%--<div class="row-tab bgSuccess dropdown" ng-show="dashboardMeta.canEdit"--%>
                <%--ng-hide="dashboardViewState.fullscreen">--%>
                <%--<span class="row-tab-button dropdown-toggle" data-toggle="dropdown">--%>
                <%--<i class="fa fa-bars"></i>--%>
                <%--</span>--%>
                <%--<ul class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="drop1">--%>
                <%--<li>--%>
                <%--<a ng-click="toggleRow(row)">Collapse row</a>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Add Panel</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<!-- ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Graph</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Table</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Single--%>
                <%--stat</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Text</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Dashboard--%>
                <%--list</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Set height</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<li><a ng-click="setHeight('25px')">25 px</a></li>--%>
                <%--<li><a ng-click="setHeight('100px')">100 px</a></li>--%>
                <%--<li><a ng-click="setHeight('150px')">150 px</a></li>--%>
                <%--<li><a ng-click="setHeight('200px')">200 px</a></li>--%>
                <%--<li><a ng-click="setHeight('250px')">250 px</a></li>--%>
                <%--<li><a ng-click="setHeight('300px')">300 px</a></li>--%>
                <%--<li><a ng-click="setHeight('350px')">350 px</a></li>--%>
                <%--<li><a ng-click="setHeight('450px')">450 px</a></li>--%>
                <%--<li><a ng-click="setHeight('500px')">500 px</a></li>--%>
                <%--<li><a ng-click="setHeight('600px')">600 px</a></li>--%>
                <%--<li><a ng-click="setHeight('700px')">700 px</a></li>--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Move</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<li><a ng-click="moveRow('up')">Up</a></li>--%>
                <%--<li><a ng-click="moveRow('down')">Down</a></li>--%>
                <%--<li><a ng-click="moveRow('top')">To top</a></li>--%>
                <%--<li><a ng-click="moveRow('bottom')">To Bottom</a></li>--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li>--%>
                <%--<a dash-editor-link="app/partials/roweditor.html">Row editor</a>--%>
                <%--</li>--%>
                <%--<li>--%>
                <%--<a ng-click="deleteRow()">Delete row</a>--%>
                <%--</li>--%>
                <%--</ul>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<!-- ngIf: !row.collapse -->--%>
                <%--<div class="panels-wrapper ng-scope" ng-if="!row.collapse">--%>
                <%--<!-- ngIf: row.showTitle -->--%>
                <%--<div class="row-text pointer ng-binding ng-scope" ng-click="toggleRow(row)"--%>
                <%--ng-if="row.showTitle" ng-bind="row.title | interpolateTemplateVars:this">Disk Usage--%>
                <%--Group By Node--%>
                <%--</div>--%>
                <%--<!-- end ngIf: row.showTitle -->--%>

                <%--<!-- ngRepeat: (name, panel) in row.panels track by panel.id -->--%>
                <%--<div ng-repeat="(name, panel) in row.panels track by panel.id" class="panel ng-scope"--%>
                <%--ui-draggable="!dashboardViewState.fullscreen" drag="panel.id"--%>
                <%--ui-on-drop="onDrop($data, row, panel)" drag-handle-class="drag-handle" panel-width=""--%>
                <%--draggable="true" style="width: 100%;">--%>
                <%--<panel-loader type="panel.type" class="panel-margin">--%>
                <%--<grafana-panel-graph class="ng-scope">--%>
                <%--<grafana-panel>--%>
                <%--<div class="panel-container"--%>
                <%--ng-class="{'panel-transparent': panel.transparent}"--%>
                <%--style="min-height: 250px; display: block;">--%>
                <%--<div class="panel-header">--%>
                <%--<!-- ngIf: panelMeta.error -->--%>

                <%--<span class="panel-loading ng-hide" ng-show="panelMeta.loading">--%>
                <%--<i class="fa fa-spinner fa-spin"></i>--%>
                <%--</span>--%>

                <%--<div class="panel-title-container drag-handle" panel-menu=""><span--%>
                <%--class="panel-title drag-handle pointer ng-scope"><span--%>
                <%--class="panel-title-text drag-handle ng-binding"></span><span--%>
                <%--class="panel-links-btn" style="display: none;"><i--%>
                <%--class="fa fa-external-link"></i></span><span--%>
                <%--class="panel-time-info ng-binding ng-hide"--%>
                <%--ng-show="panelMeta.timeInfo"><i class="fa fa-clock-o"></i> </span></span>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<div class="panel-content">--%>
                <%--<ng-transclude>--%>

                <%--<div class="graph-wrapper ng-scope"--%>
                <%--ng-class="{'graph-legend-rightside': panel.legend.rightSide}">--%>
                <%--<div class="graph-canvas-wrapper">--%>

                <%--<!-- ngIf: datapointsWarning -->--%>

                <%--<div grafana-graph="" class="histogram-chart"--%>
                <%--style="height: 210px; padding: 0px;">--%>
                <%--<canvas class="flot-base" width="1115" height="210"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 1115px; height: 210px;"></canvas>--%>
                <%--<div class="flot-text"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; font-size: smaller; color: rgb(84, 84, 84);">--%>
                <%--<div class="flot-x-axis flot-x1-axis xAxis x1Axis"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; display: block;">--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 71px; text-align: center;">--%>
                <%--10:16--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 141px; text-align: center;">--%>
                <%--10:18--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 211px; text-align: center;">--%>
                <%--10:20--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 281px; text-align: center;">--%>
                <%--10:22--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 351px; text-align: center;">--%>
                <%--10:24--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 421px; text-align: center;">--%>
                <%--10:26--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 491px; text-align: center;">--%>
                <%--10:28--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 561px; text-align: center;">--%>
                <%--10:30--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 631px; text-align: center;">--%>
                <%--10:32--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 701px; text-align: center;">--%>
                <%--10:34--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 771px; text-align: center;">--%>
                <%--10:36--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 841px; text-align: center;">--%>
                <%--10:38--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 911px; text-align: center;">--%>
                <%--10:40--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 981px; text-align: center;">--%>
                <%--10:42--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 1051px; text-align: center;">--%>
                <%--10:44--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="flot-y-axis flot-y1-axis yAxis y1Axis"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; display: block;">--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 175px; left: 26px; text-align: right;">--%>
                <%--0 B--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 140px; left: 7px; text-align: right;">--%>
                <%--23 GiB--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 105px; left: 7px; text-align: right;">--%>
                <%--47 GiB--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 70px; left: 7px; text-align: right;">--%>
                <%--70 GiB--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 35px; left: 7px; text-align: right;">--%>
                <%--93 GiB--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 0px; left: 1px; text-align: right;">--%>
                <%--116 GiB--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<canvas class="flot-overlay" width="1115"--%>
                <%--height="210"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 1115px; height: 210px;"></canvas>--%>
                <%--</div>--%>

                <%--</div>--%>

                <%--<!-- ngIf: panel.legend.show -->--%>
                <%--<div class="graph-legend-wrapper ng-scope"--%>
                <%--ng-if="panel.legend.show" graph-legend="">--%>
                <%--<section class="graph-legend">--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="0">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#7EB26D"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Limit--%>
                <%--{minion1}</a></div>--%>
                <%--<div class="graph-legend-value current">100.00--%>
                <%--GiB--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="1">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#EAB839"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Limit--%>
                <%--{minion202}</a></div>--%>
                <%--<div class="graph-legend-value current">100.00--%>
                <%--GiB--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="2">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#6ED0E0"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Usage--%>
                <%--{minion1}</a></div>--%>
                <%--<div class="graph-legend-value current">12.20--%>
                <%--GiB--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="3">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#EF843C"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Usage--%>
                <%--{minion202}</a></div>--%>
                <%--<div class="graph-legend-value current">9.19--%>
                <%--GiB--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</section>--%>
                <%--</div>--%>
                <%--<!-- end ngIf: panel.legend.show -->--%>
                <%--</div>--%>

                <%--<div class="clearfix ng-scope"></div>--%>

                <%--</ng-transclude>--%>
                <%--</div>--%>
                <%--<panel-resizer><span class="resize-panel-handle"></span></panel-resizer>--%>
                <%--</div>--%>

                <%--<!-- ngIf: editMode -->--%>

                <%--</grafana-panel>--%>


                <%--</grafana-panel-graph>--%>
                <%--</panel-loader>--%>
                <%--</div>--%>
                <%--<!-- end ngRepeat: (name, panel) in row.panels track by panel.id -->--%>

                <%--<div panel-drop-zone="" class="panel panel-drop-zone" ui-on-drop="onDrop($data, row)"--%>
                <%--data-drop="true">--%>
                <%--<div class="panel-container" style="background: transparent">--%>
                <%--<div style="text-align: center">--%>
                <%--<em>Drop here</em>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<div class="clearfix"></div>--%>
                <%--</div>--%>
                <%--<!-- end ngIf: !row.collapse -->--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<!-- end ngRepeat: (row_name, row) in dashboard.rows -->--%>
                <%--<div class="grafana-row ng-scope" ng-controller="RowCtrl" ng-repeat="(row_name, row) in dashboard.rows"--%>
                <%--row-height="" style="min-height: 250px;">--%>
                <%--<div class="row-control">--%>
                <%--<div class="row-control-inner">--%>
                <%--<div class="row-close ng-hide" ng-show="row.collapse" data-placement="bottom">--%>
                <%--<div class="row-close-buttons">--%>
                <%--<span class="row-button bgPrimary" ng-click="toggleRow(row)">--%>
                <%--<i bs-tooltip="'Expand row'" data-placement="right"--%>
                <%--class="fa fa-caret-left pointer ng-scope" data-original-title="" title=""></i>--%>
                <%--</span>--%>
                <%--</div>--%>
                <%--<div class="row-text pointer ng-binding" ng-click="toggleRow(row)"--%>
                <%--ng-bind="row.title | interpolateTemplateVars:this">Individual Node Disk Usage--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="row-open" ng-show="!row.collapse">--%>
                <%--<div class="row-tab bgSuccess dropdown" ng-show="dashboardMeta.canEdit"--%>
                <%--ng-hide="dashboardViewState.fullscreen">--%>
                <%--<span class="row-tab-button dropdown-toggle" data-toggle="dropdown">--%>
                <%--<i class="fa fa-bars"></i>--%>
                <%--</span>--%>
                <%--<ul class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="drop1">--%>
                <%--<li>--%>
                <%--<a ng-click="toggleRow(row)">Collapse row</a>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Add Panel</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<!-- ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Graph</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Table</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Single--%>
                <%--stat</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Text</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Dashboard--%>
                <%--list</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Set height</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<li><a ng-click="setHeight('25px')">25 px</a></li>--%>
                <%--<li><a ng-click="setHeight('100px')">100 px</a></li>--%>
                <%--<li><a ng-click="setHeight('150px')">150 px</a></li>--%>
                <%--<li><a ng-click="setHeight('200px')">200 px</a></li>--%>
                <%--<li><a ng-click="setHeight('250px')">250 px</a></li>--%>
                <%--<li><a ng-click="setHeight('300px')">300 px</a></li>--%>
                <%--<li><a ng-click="setHeight('350px')">350 px</a></li>--%>
                <%--<li><a ng-click="setHeight('450px')">450 px</a></li>--%>
                <%--<li><a ng-click="setHeight('500px')">500 px</a></li>--%>
                <%--<li><a ng-click="setHeight('600px')">600 px</a></li>--%>
                <%--<li><a ng-click="setHeight('700px')">700 px</a></li>--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Move</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<li><a ng-click="moveRow('up')">Up</a></li>--%>
                <%--<li><a ng-click="moveRow('down')">Down</a></li>--%>
                <%--<li><a ng-click="moveRow('top')">To top</a></li>--%>
                <%--<li><a ng-click="moveRow('bottom')">To Bottom</a></li>--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li>--%>
                <%--<a dash-editor-link="app/partials/roweditor.html">Row editor</a>--%>
                <%--</li>--%>
                <%--<li>--%>
                <%--<a ng-click="deleteRow()">Delete row</a>--%>
                <%--</li>--%>
                <%--</ul>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<!-- ngIf: !row.collapse -->--%>
                <%--<div class="panels-wrapper ng-scope" ng-if="!row.collapse">--%>
                <%--<!-- ngIf: row.showTitle -->--%>
                <%--<div class="row-text pointer ng-binding ng-scope" ng-click="toggleRow(row)"--%>
                <%--ng-if="row.showTitle" ng-bind="row.title | interpolateTemplateVars:this">Individual--%>
                <%--Node Disk Usage--%>
                <%--</div>--%>
                <%--<!-- end ngIf: row.showTitle -->--%>

                <%--<!-- ngRepeat: (name, panel) in row.panels track by panel.id -->--%>
                <%--<div ng-repeat="(name, panel) in row.panels track by panel.id" class="panel ng-scope"--%>
                <%--ui-draggable="!dashboardViewState.fullscreen" drag="panel.id"--%>
                <%--ui-on-drop="onDrop($data, row, panel)" drag-handle-class="drag-handle" panel-width=""--%>
                <%--draggable="true" style="width: 50%;">--%>
                <%--<panel-loader type="panel.type" class="panel-margin">--%>
                <%--<grafana-panel-graph class="ng-scope">--%>
                <%--<grafana-panel>--%>
                <%--<div class="panel-container"--%>
                <%--ng-class="{'panel-transparent': panel.transparent}"--%>
                <%--style="min-height: 250px; display: block;">--%>
                <%--<div class="panel-header">--%>
                <%--<!-- ngIf: panelMeta.error -->--%>

                <%--<span class="panel-loading ng-hide" ng-show="panelMeta.loading">--%>
                <%--<i class="fa fa-spinner fa-spin"></i>--%>
                <%--</span>--%>

                <%--<div class="panel-title-container drag-handle" panel-menu=""><span--%>
                <%--class="panel-title drag-handle pointer ng-scope"><span--%>
                <%--class="panel-title-text drag-handle ng-binding">minion1 Disk Usage</span><span--%>
                <%--class="panel-links-btn" style="display: none;"><i--%>
                <%--class="fa fa-external-link"></i></span><span--%>
                <%--class="panel-time-info ng-binding ng-hide"--%>
                <%--ng-show="panelMeta.timeInfo"><i class="fa fa-clock-o"></i> </span></span>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<div class="panel-content">--%>
                <%--<ng-transclude>--%>

                <%--<div class="graph-wrapper ng-scope"--%>
                <%--ng-class="{'graph-legend-rightside': panel.legend.rightSide}">--%>
                <%--<div class="graph-canvas-wrapper">--%>

                <%--<!-- ngIf: datapointsWarning -->--%>

                <%--<div grafana-graph="" class="histogram-chart"--%>
                <%--style="height: 195px; padding: 0px;">--%>
                <%--<canvas class="flot-base" width="542" height="195"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 542px; height: 195px;"></canvas>--%>
                <%--<div class="flot-text"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; font-size: smaller; color: rgb(84, 84, 84);">--%>
                <%--<div class="flot-x-axis flot-x1-axis xAxis x1Axis"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; display: block;">--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 35px; text-align: center;">--%>
                <%--10:15--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 114px; text-align: center;">--%>
                <%--10:20--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 194px; text-align: center;">--%>
                <%--10:25--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 273px; text-align: center;">--%>
                <%--10:30--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 353px; text-align: center;">--%>
                <%--10:35--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 432px; text-align: center;">--%>
                <%--10:40--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="flot-y-axis flot-y1-axis yAxis y1Axis"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; display: block;">--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 160px; left: 26px; text-align: right;">--%>
                <%--0 B--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 128px; left: 7px; text-align: right;">--%>
                <%--23 GiB--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 96px; left: 7px; text-align: right;">--%>
                <%--47 GiB--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 64px; left: 7px; text-align: right;">--%>
                <%--70 GiB--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 32px; left: 7px; text-align: right;">--%>
                <%--93 GiB--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 0px; left: 1px; text-align: right;">--%>
                <%--116 GiB--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<canvas class="flot-overlay" width="542"--%>
                <%--height="195"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 542px; height: 195px;"></canvas>--%>
                <%--</div>--%>

                <%--</div>--%>

                <%--<!-- ngIf: panel.legend.show -->--%>
                <%--<div class="graph-legend-wrapper ng-scope"--%>
                <%--ng-if="panel.legend.show" graph-legend="">--%>
                <%--<section class="graph-legend">--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="0">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#7EB26D"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Limit</a>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-value current">100.00--%>
                <%--GiB--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="1">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#EAB839"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Usage</a>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-value current">12.20--%>
                <%--GiB--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</section>--%>
                <%--</div>--%>
                <%--<!-- end ngIf: panel.legend.show -->--%>
                <%--</div>--%>

                <%--<div class="clearfix ng-scope"></div>--%>

                <%--</ng-transclude>--%>
                <%--</div>--%>
                <%--<panel-resizer><span class="resize-panel-handle"></span></panel-resizer>--%>
                <%--</div>--%>

                <%--<!-- ngIf: editMode -->--%>

                <%--</grafana-panel>--%>


                <%--</grafana-panel-graph>--%>
                <%--</panel-loader>--%>
                <%--</div>--%>
                <%--<!-- end ngRepeat: (name, panel) in row.panels track by panel.id -->--%>
                <%--<div ng-repeat="(name, panel) in row.panels track by panel.id" class="panel ng-scope"--%>
                <%--ui-draggable="!dashboardViewState.fullscreen" drag="panel.id"--%>
                <%--ui-on-drop="onDrop($data, row, panel)" drag-handle-class="drag-handle" panel-width=""--%>
                <%--draggable="true" style="width: 50%;">--%>
                <%--<panel-loader type="panel.type" class="panel-margin">--%>
                <%--<grafana-panel-graph class="ng-scope">--%>
                <%--<grafana-panel>--%>
                <%--<div class="panel-container"--%>
                <%--ng-class="{'panel-transparent': panel.transparent}"--%>
                <%--style="min-height: 250px; display: block;">--%>
                <%--<div class="panel-header">--%>
                <%--<!-- ngIf: panelMeta.error -->--%>

                <%--<span class="panel-loading ng-hide" ng-show="panelMeta.loading">--%>
                <%--<i class="fa fa-spinner fa-spin"></i>--%>
                <%--</span>--%>

                <%--<div class="panel-title-container drag-handle" panel-menu=""><span--%>
                <%--class="panel-title drag-handle pointer ng-scope"><span--%>
                <%--class="panel-title-text drag-handle ng-binding">minion202 Disk Usage</span><span--%>
                <%--class="panel-links-btn" style="display: none;"><i--%>
                <%--class="fa fa-external-link"></i></span><span--%>
                <%--class="panel-time-info ng-binding ng-hide"--%>
                <%--ng-show="panelMeta.timeInfo"><i class="fa fa-clock-o"></i> </span></span>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<div class="panel-content">--%>
                <%--<ng-transclude>--%>

                <%--<div class="graph-wrapper ng-scope"--%>
                <%--ng-class="{'graph-legend-rightside': panel.legend.rightSide}">--%>
                <%--<div class="graph-canvas-wrapper">--%>

                <%--<!-- ngIf: datapointsWarning -->--%>

                <%--<div grafana-graph="" class="histogram-chart"--%>
                <%--style="height: 195px; padding: 0px;">--%>
                <%--<canvas class="flot-base" width="542" height="195"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 542px; height: 195px;"></canvas>--%>
                <%--<div class="flot-text"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; font-size: smaller; color: rgb(84, 84, 84);">--%>
                <%--<div class="flot-x-axis flot-x1-axis xAxis x1Axis"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; display: block;">--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 34px; text-align: center;">--%>
                <%--10:15--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 114px; text-align: center;">--%>
                <%--10:20--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 193px; text-align: center;">--%>
                <%--10:25--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 273px; text-align: center;">--%>
                <%--10:30--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 352px; text-align: center;">--%>
                <%--10:35--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 432px; text-align: center;">--%>
                <%--10:40--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="flot-y-axis flot-y1-axis yAxis y1Axis"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; display: block;">--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 160px; left: 26px; text-align: right;">--%>
                <%--0 B--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 128px; left: 7px; text-align: right;">--%>
                <%--23 GiB--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 96px; left: 7px; text-align: right;">--%>
                <%--47 GiB--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 64px; left: 7px; text-align: right;">--%>
                <%--70 GiB--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 32px; left: 7px; text-align: right;">--%>
                <%--93 GiB--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 0px; left: 1px; text-align: right;">--%>
                <%--116 GiB--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<canvas class="flot-overlay" width="542"--%>
                <%--height="195"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 542px; height: 195px;"></canvas>--%>
                <%--</div>--%>

                <%--</div>--%>

                <%--<!-- ngIf: panel.legend.show -->--%>
                <%--<div class="graph-legend-wrapper ng-scope"--%>
                <%--ng-if="panel.legend.show" graph-legend="">--%>
                <%--<section class="graph-legend">--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="0">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#7EB26D"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Limit</a>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-value current">100.00--%>
                <%--GiB--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="1">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#EAB839"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Usage</a>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-value current">9.19--%>
                <%--GiB--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</section>--%>
                <%--</div>--%>
                <%--<!-- end ngIf: panel.legend.show -->--%>
                <%--</div>--%>

                <%--<div class="clearfix ng-scope"></div>--%>

                <%--</ng-transclude>--%>
                <%--</div>--%>
                <%--<panel-resizer><span class="resize-panel-handle"></span></panel-resizer>--%>
                <%--</div>--%>

                <%--<!-- ngIf: editMode -->--%>

                <%--</grafana-panel>--%>


                <%--</grafana-panel-graph>--%>
                <%--</panel-loader>--%>
                <%--</div>--%>
                <%--<!-- end ngRepeat: (name, panel) in row.panels track by panel.id -->--%>

                <%--<div panel-drop-zone="" class="panel panel-drop-zone" ui-on-drop="onDrop($data, row)"--%>
                <%--data-drop="true">--%>
                <%--<div class="panel-container" style="background: transparent">--%>
                <%--<div style="text-align: center">--%>
                <%--<em>Drop here</em>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<div class="clearfix"></div>--%>
                <%--</div>--%>
                <%--<!-- end ngIf: !row.collapse -->--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<!-- end ngRepeat: (row_name, row) in dashboard.rows -->--%>
                <%--<div class="grafana-row ng-scope" ng-controller="RowCtrl" ng-repeat="(row_name, row) in dashboard.rows"--%>
                <%--row-height="" style="min-height: 250px;">--%>
                <%--<div class="row-control">--%>
                <%--<div class="row-control-inner">--%>
                <%--<div class="row-close ng-hide" ng-show="row.collapse" data-placement="bottom">--%>
                <%--<div class="row-close-buttons">--%>
                <%--<span class="row-button bgPrimary" ng-click="toggleRow(row)">--%>
                <%--<i bs-tooltip="'Expand row'" data-placement="right"--%>
                <%--class="fa fa-caret-left pointer ng-scope" data-original-title="" title=""></i>--%>
                <%--</span>--%>
                <%--</div>--%>
                <%--<div class="row-text pointer ng-binding" ng-click="toggleRow(row)"--%>
                <%--ng-bind="row.title | interpolateTemplateVars:this">Network Usage Group By Node--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="row-open" ng-show="!row.collapse">--%>
                <%--<div class="row-tab bgSuccess dropdown" ng-show="dashboardMeta.canEdit"--%>
                <%--ng-hide="dashboardViewState.fullscreen">--%>
                <%--<span class="row-tab-button dropdown-toggle" data-toggle="dropdown">--%>
                <%--<i class="fa fa-bars"></i>--%>
                <%--</span>--%>
                <%--<ul class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="drop1">--%>
                <%--<li>--%>
                <%--<a ng-click="toggleRow(row)">Collapse row</a>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Add Panel</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<!-- ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Graph</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Table</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Single--%>
                <%--stat</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Text</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Dashboard--%>
                <%--list</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Set height</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<li><a ng-click="setHeight('25px')">25 px</a></li>--%>
                <%--<li><a ng-click="setHeight('100px')">100 px</a></li>--%>
                <%--<li><a ng-click="setHeight('150px')">150 px</a></li>--%>
                <%--<li><a ng-click="setHeight('200px')">200 px</a></li>--%>
                <%--<li><a ng-click="setHeight('250px')">250 px</a></li>--%>
                <%--<li><a ng-click="setHeight('300px')">300 px</a></li>--%>
                <%--<li><a ng-click="setHeight('350px')">350 px</a></li>--%>
                <%--<li><a ng-click="setHeight('450px')">450 px</a></li>--%>
                <%--<li><a ng-click="setHeight('500px')">500 px</a></li>--%>
                <%--<li><a ng-click="setHeight('600px')">600 px</a></li>--%>
                <%--<li><a ng-click="setHeight('700px')">700 px</a></li>--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Move</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<li><a ng-click="moveRow('up')">Up</a></li>--%>
                <%--<li><a ng-click="moveRow('down')">Down</a></li>--%>
                <%--<li><a ng-click="moveRow('top')">To top</a></li>--%>
                <%--<li><a ng-click="moveRow('bottom')">To Bottom</a></li>--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li>--%>
                <%--<a dash-editor-link="app/partials/roweditor.html">Row editor</a>--%>
                <%--</li>--%>
                <%--<li>--%>
                <%--<a ng-click="deleteRow()">Delete row</a>--%>
                <%--</li>--%>
                <%--</ul>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<!-- ngIf: !row.collapse -->--%>
                <%--<div class="panels-wrapper ng-scope" ng-if="!row.collapse">--%>
                <%--<!-- ngIf: row.showTitle -->--%>
                <%--<div class="row-text pointer ng-binding ng-scope" ng-click="toggleRow(row)"--%>
                <%--ng-if="row.showTitle" ng-bind="row.title | interpolateTemplateVars:this">Network Usage--%>
                <%--Group By Node--%>
                <%--</div>--%>
                <%--<!-- end ngIf: row.showTitle -->--%>

                <%--<!-- ngRepeat: (name, panel) in row.panels track by panel.id -->--%>
                <%--<div ng-repeat="(name, panel) in row.panels track by panel.id" class="panel ng-scope"--%>
                <%--ui-draggable="!dashboardViewState.fullscreen" drag="panel.id"--%>
                <%--ui-on-drop="onDrop($data, row, panel)" drag-handle-class="drag-handle" panel-width=""--%>
                <%--draggable="true" style="width: 100%;">--%>
                <%--<panel-loader type="panel.type" class="panel-margin">--%>
                <%--<grafana-panel-graph class="ng-scope">--%>
                <%--<grafana-panel>--%>
                <%--<div class="panel-container"--%>
                <%--ng-class="{'panel-transparent': panel.transparent}"--%>
                <%--style="min-height: 250px; display: block;">--%>
                <%--<div class="panel-header">--%>
                <%--<!-- ngIf: panelMeta.error -->--%>

                <%--<span class="panel-loading ng-hide" ng-show="panelMeta.loading">--%>
                <%--<i class="fa fa-spinner fa-spin"></i>--%>
                <%--</span>--%>

                <%--<div class="panel-title-container drag-handle" panel-menu=""><span--%>
                <%--class="panel-title drag-handle pointer ng-scope"><span--%>
                <%--class="panel-title-text drag-handle ng-binding"></span><span--%>
                <%--class="panel-links-btn" style="display: none;"><i--%>
                <%--class="fa fa-external-link"></i></span><span--%>
                <%--class="panel-time-info ng-binding ng-hide"--%>
                <%--ng-show="panelMeta.timeInfo"><i class="fa fa-clock-o"></i> </span></span>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<div class="panel-content">--%>
                <%--<ng-transclude>--%>

                <%--<div class="graph-wrapper ng-scope"--%>
                <%--ng-class="{'graph-legend-rightside': panel.legend.rightSide}">--%>
                <%--<div class="graph-canvas-wrapper">--%>

                <%--<!-- ngIf: datapointsWarning -->--%>

                <%--<div grafana-graph="" class="histogram-chart"--%>
                <%--style="height: 210px; padding: 0px;">--%>
                <%--<canvas class="flot-base" width="1115" height="210"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 1115px; height: 210px;"></canvas>--%>
                <%--<div class="flot-text"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; font-size: smaller; color: rgb(84, 84, 84);">--%>
                <%--<div class="flot-x-axis flot-x1-axis xAxis x1Axis"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; display: block;">--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 81px; text-align: center;">--%>
                <%--10:16--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 150px; text-align: center;">--%>
                <%--10:18--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 220px; text-align: center;">--%>
                <%--10:20--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 289px; text-align: center;">--%>
                <%--10:22--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 358px; text-align: center;">--%>
                <%--10:24--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 427px; text-align: center;">--%>
                <%--10:26--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 497px; text-align: center;">--%>
                <%--10:28--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 566px; text-align: center;">--%>
                <%--10:30--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 635px; text-align: center;">--%>
                <%--10:32--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 705px; text-align: center;">--%>
                <%--10:34--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 774px; text-align: center;">--%>
                <%--10:36--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 843px; text-align: center;">--%>
                <%--10:38--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 912px; text-align: center;">--%>
                <%--10:40--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 982px; text-align: center;">--%>
                <%--10:42--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 65px; top: 190px; left: 1051px; text-align: center;">--%>
                <%--10:44--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="flot-y-axis flot-y1-axis yAxis y1Axis"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; display: block;">--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 175px; left: 1px; text-align: right;">--%>
                <%---150 kBps--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 146px; left: 1px; text-align: right;">--%>
                <%---100 kBps--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 117px; left: 8px; text-align: right;">--%>
                <%---50 kBps--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 88px; left: 24px; text-align: right;">--%>
                <%--0 Bps--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 58px; left: 12px; text-align: right;">--%>
                <%--50 kBps--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 29px; left: 5px; text-align: right;">--%>
                <%--100 kBps--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 0px; left: 5px; text-align: right;">--%>
                <%--150 kBps--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<canvas class="flot-overlay" width="1115"--%>
                <%--height="210"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 1115px; height: 210px;"></canvas>--%>
                <%--</div>--%>

                <%--</div>--%>

                <%--<!-- ngIf: panel.legend.show -->--%>
                <%--<div class="graph-legend-wrapper ng-scope"--%>
                <%--ng-if="panel.legend.show" graph-legend="">--%>
                <%--<section class="graph-legend">--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="0">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#7EB26D"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Tx--%>
                <%--{minion1}</a></div>--%>
                <%--<div class="graph-legend-value current">1.15--%>
                <%--kBps--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="1">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#EAB839"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Tx--%>
                <%--{minion202}</a></div>--%>
                <%--<div class="graph-legend-value current">67.91--%>
                <%--kBps--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="2">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#6ED0E0"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Rx--%>
                <%--{minion1}</a></div>--%>
                <%--<div class="graph-legend-value current">72.46--%>
                <%--kBps--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="3">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#EF843C"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Rx--%>
                <%--{minion202}</a></div>--%>
                <%--<div class="graph-legend-value current">4.88--%>
                <%--kBps--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</section>--%>
                <%--</div>--%>
                <%--<!-- end ngIf: panel.legend.show -->--%>
                <%--</div>--%>

                <%--<div class="clearfix ng-scope"></div>--%>

                <%--</ng-transclude>--%>
                <%--</div>--%>
                <%--<panel-resizer><span class="resize-panel-handle"></span></panel-resizer>--%>
                <%--</div>--%>

                <%--<!-- ngIf: editMode -->--%>

                <%--</grafana-panel>--%>


                <%--</grafana-panel-graph>--%>
                <%--</panel-loader>--%>
                <%--</div>--%>
                <%--<!-- end ngRepeat: (name, panel) in row.panels track by panel.id -->--%>

                <%--<div panel-drop-zone="" class="panel panel-drop-zone" ui-on-drop="onDrop($data, row)"--%>
                <%--data-drop="true">--%>
                <%--<div class="panel-container" style="background: transparent">--%>
                <%--<div style="text-align: center">--%>
                <%--<em>Drop here</em>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<div class="clearfix"></div>--%>
                <%--</div>--%>
                <%--<!-- end ngIf: !row.collapse -->--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<!-- end ngRepeat: (row_name, row) in dashboard.rows -->--%>
                <%--<div class="grafana-row ng-scope" ng-controller="RowCtrl" ng-repeat="(row_name, row) in dashboard.rows"--%>
                <%--row-height="" style="min-height: 250px;">--%>
                <%--<div class="row-control">--%>
                <%--<div class="row-control-inner">--%>
                <%--<div class="row-close ng-hide" ng-show="row.collapse" data-placement="bottom">--%>
                <%--<div class="row-close-buttons">--%>
                <%--<span class="row-button bgPrimary" ng-click="toggleRow(row)">--%>
                <%--<i bs-tooltip="'Expand row'" data-placement="right"--%>
                <%--class="fa fa-caret-left pointer ng-scope" data-original-title="" title=""></i>--%>
                <%--</span>--%>
                <%--</div>--%>
                <%--<div class="row-text pointer ng-binding" ng-click="toggleRow(row)"--%>
                <%--ng-bind="row.title | interpolateTemplateVars:this">Individual Node Network Usage--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="row-open" ng-show="!row.collapse">--%>
                <%--<div class="row-tab bgSuccess dropdown" ng-show="dashboardMeta.canEdit"--%>
                <%--ng-hide="dashboardViewState.fullscreen">--%>
                <%--<span class="row-tab-button dropdown-toggle" data-toggle="dropdown">--%>
                <%--<i class="fa fa-bars"></i>--%>
                <%--</span>--%>
                <%--<ul class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="drop1">--%>
                <%--<li>--%>
                <%--<a ng-click="toggleRow(row)">Collapse row</a>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Add Panel</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<!-- ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Graph</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Table</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Single--%>
                <%--stat</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Text</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--<li bindonce="" ng-repeat="(key, value) in panels" class="ng-scope">--%>
                <%--<a ng-click="addPanelDefault(key)" bo-text="value.name">Dashboard--%>
                <%--list</a>--%>
                <%--</li>--%>
                <%--<!-- end ngRepeat: (key, value) in panels -->--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Set height</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<li><a ng-click="setHeight('25px')">25 px</a></li>--%>
                <%--<li><a ng-click="setHeight('100px')">100 px</a></li>--%>
                <%--<li><a ng-click="setHeight('150px')">150 px</a></li>--%>
                <%--<li><a ng-click="setHeight('200px')">200 px</a></li>--%>
                <%--<li><a ng-click="setHeight('250px')">250 px</a></li>--%>
                <%--<li><a ng-click="setHeight('300px')">300 px</a></li>--%>
                <%--<li><a ng-click="setHeight('350px')">350 px</a></li>--%>
                <%--<li><a ng-click="setHeight('450px')">450 px</a></li>--%>
                <%--<li><a ng-click="setHeight('500px')">500 px</a></li>--%>
                <%--<li><a ng-click="setHeight('600px')">600 px</a></li>--%>
                <%--<li><a ng-click="setHeight('700px')">700 px</a></li>--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li class="dropdown-submenu">--%>
                <%--<a href="javascript:void(0);">Move</a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<li><a ng-click="moveRow('up')">Up</a></li>--%>
                <%--<li><a ng-click="moveRow('down')">Down</a></li>--%>
                <%--<li><a ng-click="moveRow('top')">To top</a></li>--%>
                <%--<li><a ng-click="moveRow('bottom')">To Bottom</a></li>--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li>--%>
                <%--<a dash-editor-link="app/partials/roweditor.html">Row editor</a>--%>
                <%--</li>--%>
                <%--<li>--%>
                <%--<a ng-click="deleteRow()">Delete row</a>--%>
                <%--</li>--%>
                <%--</ul>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<!-- ngIf: !row.collapse -->--%>
                <%--<div class="panels-wrapper ng-scope" ng-if="!row.collapse">--%>
                <%--<!-- ngIf: row.showTitle -->--%>
                <%--<div class="row-text pointer ng-binding ng-scope" ng-click="toggleRow(row)"--%>
                <%--ng-if="row.showTitle" ng-bind="row.title | interpolateTemplateVars:this">Individual--%>
                <%--Node Network Usage--%>
                <%--</div>--%>
                <%--<!-- end ngIf: row.showTitle -->--%>

                <%--<!-- ngRepeat: (name, panel) in row.panels track by panel.id -->--%>
                <%--<div ng-repeat="(name, panel) in row.panels track by panel.id" class="panel ng-scope"--%>
                <%--ui-draggable="!dashboardViewState.fullscreen" drag="panel.id"--%>
                <%--ui-on-drop="onDrop($data, row, panel)" drag-handle-class="drag-handle" panel-width=""--%>
                <%--draggable="true" style="width: 50%;">--%>
                <%--<panel-loader type="panel.type" class="panel-margin">--%>
                <%--<grafana-panel-graph class="ng-scope">--%>
                <%--<grafana-panel>--%>
                <%--<div class="panel-container"--%>
                <%--ng-class="{'panel-transparent': panel.transparent}"--%>
                <%--style="min-height: 250px; display: block;">--%>
                <%--<div class="panel-header">--%>
                <%--<!-- ngIf: panelMeta.error -->--%>

                <%--<span class="panel-loading ng-hide" ng-show="panelMeta.loading">--%>
                <%--<i class="fa fa-spinner fa-spin"></i>--%>
                <%--</span>--%>

                <%--<div class="panel-title-container drag-handle" panel-menu=""><span--%>
                <%--class="panel-title drag-handle pointer ng-scope"><span--%>
                <%--class="panel-title-text drag-handle ng-binding">minion1 Network Usage</span><span--%>
                <%--class="panel-links-btn" style="display: none;"><i--%>
                <%--class="fa fa-external-link"></i></span><span--%>
                <%--class="panel-time-info ng-binding ng-hide"--%>
                <%--ng-show="panelMeta.timeInfo"><i class="fa fa-clock-o"></i> </span></span>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<div class="panel-content">--%>
                <%--<ng-transclude>--%>

                <%--<div class="graph-wrapper ng-scope"--%>
                <%--ng-class="{'graph-legend-rightside': panel.legend.rightSide}">--%>
                <%--<div class="graph-canvas-wrapper">--%>

                <%--<!-- ngIf: datapointsWarning -->--%>

                <%--<div grafana-graph="" class="histogram-chart"--%>
                <%--style="height: 195px; padding: 0px;">--%>
                <%--<canvas class="flot-base" width="542" height="195"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 542px; height: 195px;"></canvas>--%>
                <%--<div class="flot-text"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; font-size: smaller; color: rgb(84, 84, 84);">--%>
                <%--<div class="flot-x-axis flot-x1-axis xAxis x1Axis"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; display: block;">--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 42px; text-align: center;">--%>
                <%--10:15--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 120px; text-align: center;">--%>
                <%--10:20--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 198px; text-align: center;">--%>
                <%--10:25--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 277px; text-align: center;">--%>
                <%--10:30--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 355px; text-align: center;">--%>
                <%--10:35--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 433px; text-align: center;">--%>
                <%--10:40--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="flot-y-axis flot-y1-axis yAxis y1Axis"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; display: block;">--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 160px; left: 20px; text-align: right;">--%>
                <%--0 Bps--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 128px; left: 8px; text-align: right;">--%>
                <%--25 kBps--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 96px; left: 8px; text-align: right;">--%>
                <%--50 kBps--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 64px; left: 8px; text-align: right;">--%>
                <%--75 kBps--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 32px; left: 1px; text-align: right;">--%>
                <%--100 kBps--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 0px; left: 1px; text-align: right;">--%>
                <%--125 kBps--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<canvas class="flot-overlay" width="542"--%>
                <%--height="195"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 542px; height: 195px;"></canvas>--%>
                <%--</div>--%>

                <%--</div>--%>

                <%--<!-- ngIf: panel.legend.show -->--%>
                <%--<div class="graph-legend-wrapper ng-scope"--%>
                <%--ng-if="panel.legend.show" graph-legend="">--%>
                <%--<section class="graph-legend">--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="0">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#7EB26D"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Tx</a></div>--%>
                <%--<div class="graph-legend-value current">1.15--%>
                <%--kBps--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="1">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#EAB839"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Rx</a></div>--%>
                <%--<div class="graph-legend-value current">72.46--%>
                <%--kBps--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</section>--%>
                <%--</div>--%>
                <%--<!-- end ngIf: panel.legend.show -->--%>
                <%--</div>--%>

                <%--<div class="clearfix ng-scope"></div>--%>

                <%--</ng-transclude>--%>
                <%--</div>--%>
                <%--<panel-resizer><span class="resize-panel-handle"></span></panel-resizer>--%>
                <%--</div>--%>

                <%--<!-- ngIf: editMode -->--%>

                <%--</grafana-panel>--%>


                <%--</grafana-panel-graph>--%>
                <%--</panel-loader>--%>
                <%--</div>--%>
                <%--<!-- end ngRepeat: (name, panel) in row.panels track by panel.id -->--%>
                <%--<div ng-repeat="(name, panel) in row.panels track by panel.id" class="panel ng-scope"--%>
                <%--ui-draggable="!dashboardViewState.fullscreen" drag="panel.id"--%>
                <%--ui-on-drop="onDrop($data, row, panel)" drag-handle-class="drag-handle" panel-width=""--%>
                <%--draggable="true" style="width: 50%;">--%>
                <%--<panel-loader type="panel.type" class="panel-margin">--%>
                <%--<grafana-panel-graph class="ng-scope">--%>
                <%--<grafana-panel>--%>
                <%--<div class="panel-container"--%>
                <%--ng-class="{'panel-transparent': panel.transparent}"--%>
                <%--style="min-height: 250px; display: block;">--%>
                <%--<div class="panel-header">--%>
                <%--<!-- ngIf: panelMeta.error -->--%>

                <%--<span class="panel-loading ng-hide" ng-show="panelMeta.loading">--%>
                <%--<i class="fa fa-spinner fa-spin"></i>--%>
                <%--</span>--%>

                <%--<div class="panel-title-container drag-handle" panel-menu=""><span--%>
                <%--class="panel-title drag-handle pointer ng-scope"><span--%>
                <%--class="panel-title-text drag-handle ng-binding">minion202 Network Usage</span><span--%>
                <%--class="panel-links-btn" style="display: none;"><i--%>
                <%--class="fa fa-external-link"></i></span><span--%>
                <%--class="panel-time-info ng-binding ng-hide"--%>
                <%--ng-show="panelMeta.timeInfo"><i class="fa fa-clock-o"></i> </span></span>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<div class="panel-content">--%>
                <%--<ng-transclude>--%>

                <%--<div class="graph-wrapper ng-scope"--%>
                <%--ng-class="{'graph-legend-rightside': panel.legend.rightSide}">--%>
                <%--<div class="graph-canvas-wrapper">--%>

                <%--<!-- ngIf: datapointsWarning -->--%>

                <%--<div grafana-graph="" class="histogram-chart"--%>
                <%--style="height: 195px; padding: 0px;">--%>
                <%--<canvas class="flot-base" width="542" height="195"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 542px; height: 195px;"></canvas>--%>
                <%--<div class="flot-text"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; font-size: smaller; color: rgb(84, 84, 84);">--%>
                <%--<div class="flot-x-axis flot-x1-axis xAxis x1Axis"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; display: block;">--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 41px; text-align: center;">--%>
                <%--10:15--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 120px; text-align: center;">--%>
                <%--10:20--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 198px; text-align: center;">--%>
                <%--10:25--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 276px; text-align: center;">--%>
                <%--10:30--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 355px; text-align: center;">--%>
                <%--10:35--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; max-width: 67px; top: 175px; left: 433px; text-align: center;">--%>
                <%--10:40--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="flot-y-axis flot-y1-axis yAxis y1Axis"--%>
                <%--style="position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px; display: block;">--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 160px; left: 8px; text-align: right;">--%>
                <%--60 kBps--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 133px; left: 8px; text-align: right;">--%>
                <%--70 kBps--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 107px; left: 8px; text-align: right;">--%>
                <%--80 kBps--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 80px; left: 8px; text-align: right;">--%>
                <%--90 kBps--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 53px; left: 1px; text-align: right;">--%>
                <%--100 kBps--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 27px; left: 1px; text-align: right;">--%>
                <%--110 kBps--%>
                <%--</div>--%>
                <%--<div class="flot-tick-label tickLabel"--%>
                <%--style="position: absolute; top: 0px; left: 1px; text-align: right;">--%>
                <%--120 kBps--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<canvas class="flot-overlay" width="542"--%>
                <%--height="195"--%>
                <%--style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 542px; height: 195px;"></canvas>--%>
                <%--</div>--%>

                <%--</div>--%>

                <%--<!-- ngIf: panel.legend.show -->--%>
                <%--<div class="graph-legend-wrapper ng-scope"--%>
                <%--ng-if="panel.legend.show" graph-legend="">--%>
                <%--<section class="graph-legend">--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="0">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#7EB26D"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Tx</a></div>--%>
                <%--<div class="graph-legend-value current">67.91--%>
                <%--kBps--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="graph-legend-series"--%>
                <%--data-series-index="1">--%>
                <%--<div class="graph-legend-icon"><i--%>
                <%--class="fa fa-minus pointer"--%>
                <%--style="color:#EAB839"></i></div>--%>
                <%--<div class="graph-legend-alias"><a>Rx</a></div>--%>
                <%--<div class="graph-legend-value current">4.88--%>
                <%--kBps--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</section>--%>
                <%--</div>--%>
                <%--<!-- end ngIf: panel.legend.show -->--%>
                <%--</div>--%>

                <%--<div class="clearfix ng-scope"></div>--%>

                <%--</ng-transclude>--%>
                <%--</div>--%>
                <%--<panel-resizer><span class="resize-panel-handle"></span></panel-resizer>--%>
                <%--</div>--%>

                <%--<!-- ngIf: editMode -->--%>

                <%--</grafana-panel>--%>


                <%--</grafana-panel-graph>--%>
                <%--</panel-loader>--%>
                <%--</div>--%>
                <%--<!-- end ngRepeat: (name, panel) in row.panels track by panel.id -->--%>

                <%--<div panel-drop-zone="" class="panel panel-drop-zone" ui-on-drop="onDrop($data, row)"--%>
                <%--data-drop="true">--%>
                <%--<div class="panel-container" style="background: transparent">--%>
                <%--<div style="text-align: center">--%>
                <%--<em>Drop here</em>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--<div class="clearfix"></div>--%>
                <%--</div>--%>
                <%--<!-- end ngIf: !row.collapse -->--%>
                <%--</div>--%>
                <%--</div>--%>
                <!-- end ngRepeat: (row_name, row) in dashboard.rows -->

                <div ng-show="dashboardMeta.canEdit" class="row-fluid add-row-panel-hint"
                     ng-hide="dashboardViewState.fullscreen">
                    <div class="span12" style="text-align:right;">
				<span style="margin-right: 10px;" ng-click="addRowDefault()" class="pointer btn btn-info btn-small">
					<span><i class="fa fa-plus"></i> ADD ROW</span>
				</span>
                    </div>
                </div>

            </div>

            <!-- ngIf: consoleEnabled -->

        </div>
    </div>

</div>


<script>
    window.grafanaBootData = {
        user: {
            "isSignedIn": false,
            "id": 0,
            "login": "",
            "email": "",
            "name": "",
            "lightTheme": false,
            "orgId": 1,
            "orgName": "Main Org.",
            "orgRole": "Admin",
            "isGrafanaAdmin": false,
            "gravatarUrl": ""
        },
        settings: {
            "allowOrgCreate": false,
            "appSubUrl": "",
            "buildInfo": {"buildstamp": 1450102681, "commit": "v2.6.0", "version": "2.6.0"},
            "datasources": {
                "-- Grafana --": {
                    "meta": {
                        "builtIn": true,
                        "metrics": true,
                        "module": "app/plugins/datasource/grafana/datasource",
                        "name": "Grafana",
                        "pluginType": "datasource",
                        "serviceName": "GrafanaDatasource",
                        "type": "grafana"
                    }, "type": "grafana"
                },
                "-- Mixed --": {
                    "meta": {
                        "builtIn": true,
                        "metrics": true,
                        "mixed": true,
                        "module": "app/plugins/datasource/mixed/datasource",
                        "name": "Mixed datasource",
                        "pluginType": "datasource",
                        "serviceName": "MixedDatasource",
                        "type": "mixed"
                    }, "type": "mixed"
                },
                "influxdb-datasource": {
                    "meta": {
                        "annotations": true,
                        "defaultMatchFormat": "regex values",
                        "metrics": true,
                        "module": "app/plugins/datasource/influxdb/datasource",
                        "name": "InfluxDB 0.9.x",
                        "partials": {"config": "app/plugins/datasource/influxdb/partials/config.html"},
                        "pluginType": "datasource",
                        "serviceName": "InfluxDatasource",
                        "type": "influxdb"
                    }, "name": "influxdb-datasource", "type": "influxdb", "url": "/api/datasources/proxy/1"
                }
            },
            "defaultDatasource": "influxdb-datasource"
        },
    };

    require(['app/app'], function (app) {
        app.boot();
    })
</script>


</body>
</html>