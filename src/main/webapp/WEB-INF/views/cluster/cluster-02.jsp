<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/cluster.css"/>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/core/xcharts.min.css"/>
    <script type="text/javascript" src="<%=path %>/js/cluster/cluster.js"></script>
    <script type="text/javascript" src="<%=path %>/plugins/bower-angular-master/angular.js"></script>
    <script type="text/javascript" src="<%=path %>/plugins/chartjs-1.0.2/Chart.js"></script>
    <script type="text/javascript" src="<%=path %>/js/plugins/xcharts.min.js"></script>
    <script type="text/javascript" src="<%=path %>/js/plugins/xcharts.js"></script>
</head>

<body>
<jsp:include page="../frame/menu.jsp" flush="true">
    <jsp:param name="cluster" value=""/>
</jsp:include>
<input type="hidden" id="checkedHosts">

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                            id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" id="nav2">集群资源</li>
                </ol>
            </div>
            <div class="contentMain">

                <aside class="aside-btn">
                    <div class="btns-group">
                        <a id="clusterResource" class="Record action"><span class="btn btn-defaults btn-white"><span
                                class="ic_left">集群资源</span></span></a>
                    </div>
                </aside>

                <div>

                    <div id="clusterResource_wrap" class="tab_wrap">
                        <div class="detail-info">
                            <div class="info-list">
                                    <div ng-repeat="(name, panel) in row.panels track by panel.id"
                                         class="panel ng-scope" ui-draggable="true" drag="panel.id"
                                         ui-on-drop="onDrop($data, row, panel)" drag-handle-class="drag-handle"
                                         panel-width="" draggable="true" style="width: 100%;">
                                        <panel-loader type="panel.type" class="panel-margin">
                                            <grafana-panel-graph class="ng-scope">
                                                <grafana-panel>
                                                    <div class="panel-container"
                                                         ng-class="{'panel-transparent': panel.transparent}"
                                                         style="min-height: 250px; display: block;">
                                                        <div class="panel-content">
                                                            <ng-transclude>
                                                                <div class="graph-wrapper ng-scope">
                                                                    <%--<div class="graph-canvas-wrapper">
                                                                        <!-- ngIf: datapointsWarning -->
                                                                        <div grafana-graph="" class="histogram-chart"
                                                                             style="height: 210px; padding: 0px;">
                                                                            <!-- all cluster memory usage-->
                                                                            <figure class="flot-base" id="chartAll"
                                                                                    style="direction: ltr; position: relative; left: 0px; top: 0px; width: 1115px; height: 210px; margin-top: 20px">
                                                                            </figure>

                                                                        </div>
                                                                    </div>--%>
                                                                    <!-- ngIf: panel.legend.show -->
                                                                    <div class="table-lists ">OverAll Cluster Memory USAGE</div>
                                                                    <figure class="flot-base" id="chartAll"
                                                                            style="direction: ltr; position: relative; left: 0px; top: 0px; width: 1115px; height: 210px; margin-top: 20px">
                                                                    </figure>
                                                                    <div class="graph-legend-wrapper ng-scope"
                                                                         style="height: 30px">
                                                                        <section class="graph-legend">
                                                                            <div class="graph-legend-series"
                                                                                 data-series-index="0">
                                                                                <div class="graph-legend-icon"><i
                                                                                        class="fa fa-minus pointer" ></i></div>
                                                                                <div class="graph-legend-alias">
                                                                                    <a>Limit Current:</a></div>
                                                                                <div class="graph-legend-value current">
                                                                                    62.06 GiB
                                                                                </div>
                                                                            </div>
                                                                            <div class="graph-legend-series"
                                                                                 data-series-index="1">
                                                                                <div class="graph-legend-icon"><i
                                                                                        class="fa fa-minus pointer"></i></div>
                                                                                <div class="graph-legend-alias">
                                                                                    <a>Usage Current:</a></div>
                                                                                <div class="graph-legend-value current">
                                                                                    10.13 GiB
                                                                                </div>
                                                                            </div>
                                                                            <div class="graph-legend-series"
                                                                                 data-series-index="2">
                                                                                <div class="graph-legend-icon"><i
                                                                                        class="fa fa-minus pointer"></i></div>
                                                                                <div class="graph-legend-alias"><a>Working
                                                                                    Set Current:</a></div>
                                                                                <div class="graph-legend-value current">
                                                                                    5.00 GiB
                                                                                </div>
                                                                            </div>
                                                                        </section>
                                                                    </div>
                                                                </div>
                                                                    <!-- memory usage group by node -->
                                                                <div class="graph-wrapper ng-scope">
                                                                    <div class="table-lists ">Memory USAGE Group By Node</div>
                                                                    <figure class="flot-base" id="chartNode"
                                                                            style="direction: ltr; position: relative; left: 0px; top: 0px; width: 1115px; height: 210px; margin-top: 20px">
                                                                    </figure>

                                                                    <div class="graph-legend-wrapper ng-scope"
                                                                         style="height: 60px">
                                                                        <section class="graph-legend">
                                                                            <div class="graph-legend-series"
                                                                                 data-series-index="0">
                                                                                <div class="graph-legend-icon"><i
                                                                                        class="fa fa-minus pointer"></i></div>
                                                                                <div class="graph-legend-alias"><a>memory/limit_bytes_gauge.mean
                                                                                    {hostname: minion01} Current:</a></div>
                                                                                <div class="graph-legend-value current">
                                                                                    15.51 GiB
                                                                                </div>
                                                                            </div>
                                                                            <div class="graph-legend-series"
                                                                                 data-series-index="1">
                                                                                <div class="graph-legend-icon"><i
                                                                                        class="fa fa-minus pointer"></i></div>
                                                                                <div class="graph-legend-alias"><a>memory/limit_bytes_gauge.mean
                                                                                    {hostname: minion02} Current:</a></div>
                                                                                <div class="graph-legend-value current">
                                                                                    15.51 GiB
                                                                                </div>
                                                                            </div>
                                                                            <div class="graph-legend-series"
                                                                                 data-series-index="2">
                                                                                <div class="graph-legend-icon"><i
                                                                                        class="fa fa-minus pointer"></i></div>
                                                                                <div class="graph-legend-alias"><a>memory/working_set_bytes_gauge.mean
                                                                                    {hostname: minion01} Current:</a></div>
                                                                                <div class="graph-legend-value current">
                                                                                    2.04 GiB
                                                                                </div>
                                                                            </div>
                                                                            <div class="graph-legend-series"
                                                                                 data-series-index="3">
                                                                                <div class="graph-legend-icon"><i
                                                                                        class="fa fa-minus pointer"></i></div>
                                                                                <div class="graph-legend-alias"><a>memory/working_set_bytes_gauge.mean
                                                                                    {hostname: minion02} Current:</a></div>
                                                                                <div class="graph-legend-value current">
                                                                                    1.84 GiB
                                                                                </div>
                                                                            </div>
                                                                        </section>
                                                                    </div>
                                                                </div>


                                                                <div class="table-lists " style="width: 48%; float: left">Minion01 Memory Usage</div>
                                                                <div class="table-lists " style="width: 48%; float: right">Minion02 Memory Usage</div>
                                                                <div style="float: left">
                                                                    <figure class="flot-base" id="chartMinion"
                                                                            style="direction: ltr; position: relative; left: 0px; top: 0px; width: 542px; height: 210px; margin-top: 20px">
                                                                    </figure>

                                                                    <div class="graph-legend-wrapper ng-scope"
                                                                         style="height: 30px">
                                                                        <section class="graph-legend">
                                                                            <div class="graph-legend-series"
                                                                                 data-series-index="0">
                                                                                <div class="graph-legend-icon"><i
                                                                                        class="fa fa-minus pointer" ></i></div>
                                                                                <div class="graph-legend-alias">
                                                                                    <a>Limit Current:</a></div>
                                                                                <div class="graph-legend-value current">
                                                                                    62.06 GiB
                                                                                </div>
                                                                            </div>
                                                                            <div class="graph-legend-series"
                                                                                 data-series-index="1">
                                                                                <div class="graph-legend-icon"><i
                                                                                        class="fa fa-minus pointer"></i></div>
                                                                                <div class="graph-legend-alias">
                                                                                    <a>Usage Current:</a></div>
                                                                                <div class="graph-legend-value current">
                                                                                    10.13 GiB
                                                                                </div>
                                                                            </div>
                                                                            <div class="graph-legend-series"
                                                                                 data-series-index="2">
                                                                                <div class="graph-legend-icon"><i
                                                                                        class="fa fa-minus pointer"></i></div>
                                                                                <div class="graph-legend-alias"><a>Working
                                                                                    Set Current:</a></div>
                                                                                <div class="graph-legend-value current">
                                                                                    5.00 GiB
                                                                                </div>
                                                                            </div>
                                                                        </section>
                                                                    </div>
                                                                </div>
                                                                <div style="float: right">
                                                                <figure class="flot-base" id="chartMinion02"
                                                                        style="direction: ltr; position: relative; left: 0px; top: 0px; width: 542px; height: 210px; margin-top: 20px">
                                                                </figure>

                                                                <div class="graph-legend-wrapper ng-scope"
                                                                     style="height: 30px">
                                                                    <section class="graph-legend">
                                                                        <div class="graph-legend-series"
                                                                             data-series-index="0">
                                                                            <div class="graph-legend-icon"><i
                                                                                    class="fa fa-minus pointer" ></i></div>
                                                                            <div class="graph-legend-alias">
                                                                                <a>Limit Current:</a></div>
                                                                            <div class="graph-legend-value current">
                                                                                62.06 GiB
                                                                            </div>
                                                                        </div>
                                                                        <div class="graph-legend-series"
                                                                             data-series-index="1">
                                                                            <div class="graph-legend-icon"><i
                                                                                    class="fa fa-minus pointer"></i></div>
                                                                            <div class="graph-legend-alias">
                                                                                <a>Usage Current:</a></div>
                                                                            <div class="graph-legend-value current">
                                                                                10.13 GiB
                                                                            </div>
                                                                        </div>
                                                                        <div class="graph-legend-series"
                                                                             data-series-index="2">
                                                                            <div class="graph-legend-icon"><i
                                                                                    class="fa fa-minus pointer"></i></div>
                                                                            <div class="graph-legend-alias"><a>Working
                                                                                Set Current:</a></div>
                                                                            <div class="graph-legend-value current">
                                                                                5.00 GiB
                                                                            </div>
                                                                        </div>
                                                                    </section>
                                                                </div>
                                                                </div>

                                                                    <!-- end ngIf: panel.legend.show -->
                                                                <div class="clearfix ng-scope"></div>
                                                            </ng-transclude>
                                                        </div>
                                                    </div>
                                                    <!-- ngIf: editMode -->
                                                </grafana-panel>
                                            </grafana-panel-graph>
                                        </panel-loader>
                                    </div>

                                <table class="table" id="cluster-listing" style="margin-top: 20px">
                                    <thead>
                                    <tr>
                                        <th class="detail-rows" style="width:13%">集群主机资源一览</th>
                                        <th class="detail-rows" style="text-align: center;width:33%">CPU</th>
                                        <th class="detail-rows" style="text-align: center;width:33%">内存</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${lstClusters}" var="clusterUse">
                                        <%--<c:if test="${clusterUse.host == null || clusterUse.host == '0'}">
                                            <c:set var="cursorClass" value="cursor-no-drop"></c:set>
                                        </c:if>--%>
                                        <c:if test="${cur_cluster.host != clusterUse.host}">
                                            <tr name="clusterUseInfo">
                                                <td>${clusterUse.host}</td>
                                                <td style="border-right: 1px solid #ddd;border-left: 1px solid #ddd;">
                                                    <div class="slider_bj"
                                                         style="width:50%; float: left;margin-left: 40px">
                                                        <div class="slider_block detailCpu clusterCpuUse"
                                                             style="width: 0%"></div>
                                                    </div>
                                                    <div><span id="clusterDetailCpu"
                                                               class="clusterDetCpuUse">${clusterUse.cpuUse}</span>/<span
                                                            id="clusterTotalCup"
                                                            class="clusterTotCpuLimit">${clusterUse.cpuLimit}</span>（ms）
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="slider_bj"
                                                         style="width:50%; float: left;margin-left: 40px">
                                                        <div class="slider_block detailMemory clusterMemUse"
                                                             style="width: 0%"></div>

                                                    </div>
                                                    <div><span id="clusterDetailMemory"
                                                               class="clusterDetMemUse">${clusterUse.memUse}</span>/<span
                                                            id="clustertotalMemory"
                                                            class="clusterTotMemLimit">${clusterUse.memLimit}</span>（M）
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </c:forEach>

                                    </tbody>
                                </table>
                            </div>
                        </div>

                    </div>
                </div>


            </div>
        </div>
    </article>
</div>

<script type="text/javascript">

    (function () {
        var data = [
            {
                "xScale": "ordinal", "comp": [],
                "main": [{
                    "className": ".main.l1",
                    "data": [{"y": 15, "x": "2014-11-19"},
                        {"y": 11, "x": "2014-11-20"}, {"y": 8, "x": "2014-11-21"},
                        {"y": 10, "x": "2014-11-22"}, {"y": 1, "x": "2014-11-23"},
                        {"y": 6, "x": "2014-11-24"}, {"y": 8, "x": "2014-11-25"}, {"y": 28, "x": "2014-11-26"}]
                },
                    {
                        "className": ".main.l2",
                        "data": [{"y": 29, "x": "2014-11-19"},
                            {"y": 33, "x": "2014-11-20"}, {"y": 13, "x": "2014-11-21"},
                            {"y": 16, "x": "2014-11-22"}, {"y": 7, "x": "2014-11-23"},
                            {"y": 18, "x": "2014-11-24"}, {"y": 8, "x": "2014-11-25"}, {"y": 48, "x": "2014-11-26"}]
                    },
                    {
                        "className": ".main.l3",
                        "data": [{"y": 10, "x": "2014-11-19"},
                            {"y": 12, "x": "2014-11-20"}, {"y": 13, "x": "2014-11-21"},
                            {"y": 14, "x": "2014-11-22"}, {"y": 15, "x": "2014-11-23"},
                            {"y": 16, "x": "2014-11-24"}, {"y": 28, "x": "2014-11-25"}, {"y": 22, "x": "2014-11-26"}]
                    }],
                "type": "line-dotted", "yScale": "linear"
            },
            {
                "xScale": "ordinal", "comp": [],
                "main": [{
                    "className": ".main.l1",
                    "data": [{"y": 15, "x": "2014-11-19"},
                        {"y": 11, "x": "2014-11-20"}, {"y": 8, "x": "2014-11-21"},
                        {"y": 10, "x": "2014-11-22"}, {"y": 1, "x": "2014-11-23"},
                        {"y": 6, "x": "2014-11-24"}, {"y": 8, "x": "2014-11-25"}]
                },
                    {
                        "className": ".main.l2",
                        "data": [{"y": 29, "x": "2014-11-19"},
                            {"y": 33, "x": "2014-11-20"}, {"y": 13, "x": "2014-11-21"},
                            {"y": 16, "x": "2014-11-22"}, {"y": 7, "x": "2014-11-23"},
                            {"y": 18, "x": "2014-11-24"}, {"y": 8, "x": "2014-11-25"}]
                    },
                    {
                        "className": ".main.l3",
                        "data": [{"y": 10, "x": "2014-11-19"},
                            {"y": 12, "x": "2014-11-20"}, {"y": 13, "x": "2014-11-21"},
                            {"y": 14, "x": "2014-11-22"}, {"y": 15, "x": "2014-11-23"},
                            {"y": 16, "x": "2014-11-24"}, {"y": 28, "x": "2014-11-25"}]
                    },
                    {
                        "className": ".main.l4",
                        "data": [{"y": 6, "x": "2014-11-19"},
                            {"y": 10, "x": "2014-11-20"}, {"y": 10, "x": "2014-11-21"},
                            {"y": 10, "x": "2014-11-22"}, {"y": 10, "x": "2014-11-23"},
                            {"y": 10, "x": "2014-11-24"}, {"y": 10, "x": "2014-11-25"}]
                    }],
                "type": "line-dotted", "yScale": "linear"
            },
            {
                "xScale": "ordinal", "comp": [],
                "main": [{
                    "className": ".main.l1",
                    "data": [{"y": 15, "x": "2014-11-19"},
                        {"y": 11, "x": "2014-11-20"}, {"y": 8, "x": "2014-11-21"},
                        {"y": 10, "x": "2014-11-22"}, {"y": 1, "x": "2014-11-23"},
                        {"y": 6, "x": "2014-11-24"}, {"y": 8, "x": "2014-11-25"}]
                },
                    {
                        "className": ".main.l2",
                        "data": [{"y": 29, "x": "2014-11-19"},
                            {"y": 33, "x": "2014-11-20"}, {"y": 13, "x": "2014-11-21"},
                            {"y": 16, "x": "2014-11-22"}, {"y": 7, "x": "2014-11-23"},
                            {"y": 18, "x": "2014-11-24"}, {"y": 8, "x": "2014-11-25"}]
                    },
                    {
                        "className": ".main.l3",
                        "data": [{"y": 10, "x": "2014-11-19"},
                            {"y": 12, "x": "2014-11-20"}, {"y": 13, "x": "2014-11-21"},
                            {"y": 14, "x": "2014-11-22"}, {"y": 15, "x": "2014-11-23"},
                            {"y": 16, "x": "2014-11-24"}, {"y": 28, "x": "2014-11-25"}]
                    }],
                "type": "line-dotted", "yScale": "linear"
            },
        ];
        var chartAll = new xChart('line-dotted', data[0], '#chartAll', {
            axisPaddingTop: 5,
            /*dataFormatX: function (x) {
             return new Date(x);
             },*/
            /*tickFormatX: function (x) {
             return xFormat(x);
             },*/
            //timing: 1250
        });
        var chartNode = new xChart('line-dotted', data[1], '#chartNode', {
            axisPaddingTop: 5,
        });
        var chartMinion = new xChart('line-dotted', data[2], '#chartMinion', {
            axisPaddingTop: 5,
        })
        var chartMinion02 = new xChart('line-dotted', data[2], '#chartMinion02', {
            axisPaddingTop: 5,
        });

        var colors = ['#3880aa','#4da944','#f26522','#c6080d','#672d8b','#ce1797','#d9ce00','#754c24','#2eb9b4','#0e2e42'];
        for(var i=0; i< $(".graph-legend-series").length; i++){
            for(var j=0; j<10; j++){
                if($(".graph-legend-series")[i].getAttribute("data-series-index") == j){
                    var a = $(".graph-legend-series")[i].children[0].children[0];
                    a.setAttribute("style","color:"+colors[j])
                }
            }
        }



//        var i=0;
//        for(i ; i <= $document.getElementsByName('').){
//
//        }
//        if(data-series-index="0"){
//
//        }


//                rotateTimer,
//                toggles = d3.selectAll('.multi button'),
//                t = 3500;
//
//        function updateChart(i) {
//            var d = data[i];
//            chart.setData(d);
//            toggles.classed('toggled', function () {
//                return (d3.select(this).attr('data-type') === d.type);
//            });
//            return d;
//        }
//
//        toggles.on('click', function (d, i) {
//            clearTimeout(rotateTimer);
//            updateChart(i);
//        });
//
//        function rotateChart() {
//            i += 1;
//            i = (i >= order.length) ? 0 : i;
//            var d = updateChart(order[i]);
//            rotateTimer = setTimeout(rotateChart, t);
//        }
//        rotateTimer = setTimeout(rotateChart, t);
    }());

    /*var data = {
     labels : ["January","February","March","April","May","June","July"],
     datasets : [
     {
     fillColor : "rgba(220,220,220,0.5)",
     strokeColor : "rgba(220,220,220,1)",
     pointColor : "rgba(220,220,220,1)",
     pointStrokeColor : "#fff",
     data : [65,59,90,81,56,55,40]
     }
     {
     fillColor : "rgba(151,187,205,0.5)",
     strokeColor : "rgba(151,187,205,1)",
     pointColor : "rgba(151,187,205,1)",
     pointStrokeColor : "#fff",
     data : [28,48,40,19,96,27,100]
     }
     ]
     }

     var ctx = document.getElementById("a_canvas").getContext("2d");
     alert(data.datasets[0].fillColor)
     var myNewChart = new Chart(ctx).PolarArea(data);
     alert(myNewChart)*/
</script>

<%--<script type="text/javascript">--%>
<%--(function (){--%>

<%--window.addEventListener("load", function(){--%>

<%--var data = [80,92,104,110,68,50,45,90,74,68,98,103];--%>

<%--// 获取上下文--%>
<%--var a_canvas = document.getElementById('a_canvas');--%>
<%--var context = a_canvas.getContext("2d");--%>


<%--// 绘制背景--%>
<%--var gradient = context.createLinearGradient(0,0,0,300);--%>


<%--gradient.addColorStop(0,"#e0e0e0");--%>
<%--gradient.addColorStop(1,"#ffffff");--%>


<%--context.fillStyle = gradient;--%>

<%--context.fillRect(0,0,a_canvas.width,a_canvas.height);--%>


<%--// 描绘边框--%>
<%--var grid_cols = data.length + 1;--%>
<%--var grid_rows = 4;--%>
<%--var cell_height = a_canvas.height / grid_rows;--%>
<%--var cell_width = a_canvas.width / grid_cols;--%>
<%--context.lineWidth = 1;--%>
<%--context.strokeStyle = "#a0a0a0";--%>

<%--// 结束边框描绘--%>
<%--context.beginPath();--%>
<%--// 准备画横线--%>
<%--for (var col = 0; col <= grid_cols; col++) {--%>
<%--var x = col * cell_width;--%>
<%--context.moveTo(x,0);--%>
<%--context.lineTo(x,a_canvas.height);--%>
<%--}--%>
<%--// 准备画竖线--%>
<%--for(var row = 0; row <= grid_rows; row++){--%>
<%--var y = row * cell_height;--%>
<%--context.moveTo(0,y);--%>
<%--context.lineTo(a_canvas.width, y);--%>
<%--}--%>
<%--context.lineWidth = 1;--%>
<%--context.strokeStyle = "#c0c0c0";--%>
<%--context.stroke();--%>

<%--var max_v = 0;--%>
<%--for(var i = 0; i<data.length; i++){--%>
<%--if (data[i] > max_v) { max_v = data[i]};--%>
<%--}--%>

<%--max_v = max_v * 1.1;--%>
<%--// 将数据换算为坐标--%>
<%--var points = [];--%>
<%--for( var i=0; i < data.length; i++){--%>
<%--var v= data[i];--%>
<%--var px = cell_width *　(i +1);--%>
<%--var py = a_canvas.height - a_canvas.height*(v / max_v);--%>
<%--points.push({"x":px,"y":py});--%>
<%--}--%>
<%--// 绘制折现--%>
<%--context.beginPath();--%>
<%--context.moveTo(points[0].x, points[0].y);--%>
<%--for(var i= 1; i< points.length; i++){--%>
<%--context.lineTo(points[i].x,points[i].y);--%>
<%--}--%>


<%--context.lineWidth = 2;--%>
<%--context.strokeStyle = "#ee0000";--%>
<%--context.stroke();--%>

<%--//绘制坐标图形--%>
<%--for(var i in points){--%>
<%--var p = points[i];--%>
<%--context.beginPath();--%>
<%--context.arc(p.x,p.y,6,0,2*Math.PI);--%>
<%--context.fillStyle = "#ee0000";--%>
<%--context.fill();--%>
<%--}--%>
<%--},false);--%>
<%--})();--%>
<%--</script>--%>

</body>


</html>