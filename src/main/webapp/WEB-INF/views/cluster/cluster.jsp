<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/cluster.css"/>
    <script type="text/javascript" src="<%=path %>/js/cluster/cluster.js"></script>
    <script type="text/javascript" src="<%=path %>/plugins/bower-angular-master/angular.js"></script>
    <script type="text/javascript" src="<%=path %>/plugins/echarts/src/echarts.js"></script>


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
                                class="ic_left">集群监控</span></span></a>
                    </div>
                </aside>

                <div>
                    <div id="clusterResource_wrap" class="tab_wrap">
                        <div class="detail-info">
                            <div class="info-list" id="resourceImg">
                                <div class="table-lists" style="margin-top: 10px">
                                    <div style="width: 1115px;height:260px;"></div>
                                </div>

                                <div class="table-lists" style="margin-top: 10px">
                                    <div style="width: 1115px;height:260px;"></div>
                                </div>

                                <div class="table-lists" style="margin-top: 10px; float: left">
                                    <div style="width: 563px;height:260px;"></div>
                                </div>
                                <div class="table-lists" style="margin-top: 10px; float: right">
                                    <div style="width: 563px;height:260px;"></div>
                                </div>

                                <div class="table-lists" style="margin-top: 10px;float: left ">
                                    <div style="width: 1115px;height:260px;"></div>
                                </div>

                                <div class="table-lists" style="margin-top: 10px;float: left">
                                    <div style="width: 1115px;height:260px;"></div>
                                </div>

                                <div class="table-lists" style="margin-top: 10px; float: left">
                                    <div style="width: 563px;height:260px;"></div>
                                </div>
                                <div class="table-lists" style="margin-top: 10px; float: right">
                                    <div style="width: 563px;height:260px;"></div>
                                </div>
                            </div>

                        </div>

                    </div>
                </div>


            </div>
        </div>
    </article>
</div>

<script type="text/javascript">
    var colorData = ['#c5e1d2','#abd4bd','#91c7a9','#77ba95'];

    var  clusterData = [
        {
            'xAxis':['2014-11-19','2014-11-20','2014-11-21','2014-11-22','2014-11-23','2014-11-24','2014-11-25','2014-11-26','2014-11-27'],
            'memoryVal':{'titleTitle':'OverAll Cluster Memory USAGE',
                'val': [{'legendName':'Limit Current',
                    'yAxis':[220, 182, 191, 234, 290, 330, 310, 290, 330]},
                {'legendName':'Usage Current',
                    'yAxis':[120, 132, 101, 134, 90, 230, 210, 101, 134]},
                {'legendName':'Working Set Current',
                    'yAxis':[10, 11, 10, 12, 12, 12, 12, 12, 12]}]},

            'cpuVal':{'titleTitle':'OverAll Cluster Cpu USAGE',
                 'val':[{'legendName':'cLimit Current',
                'yAxis':[220, 182, 191, 234, 290, 330, 310, 290, 330]},
                {'legendName':'cUsage Current',
                    'yAxis':[120, 132, 101, 134, 90, 230, 210, 101, 134]},
                {'legendName':'cWorking Set Current',
                    'yAxis':[10, 11, 10, 12, 12, 12, 12, 12, 12]}]}
        },

        {
            'xAxis':['2014-11-19','2014-11-20','2014-11-21','2014-11-22','2014-11-23','2014-11-24','2014-11-25','2014-11-26','2014-11-27',
                '2014-11-28','2014-11-29','2014-11-30','2014-12-01','2014-12-02','2014-12-03','2014-12-04','2014-12-05','2014-12-06'],
            'memoryVal':{'titleTitle':'Memory Usage Group By Node',
                'val':[{'legendName':'111',
                    'yAxis':[220, 182, 191, 234, 290, 330, 310, 290, 330, 220, 182, 191, 234, 290, 330, 310, 290, 330]},
                    {'legendName':'222',
                        'yAxis':[120, 132, 101, 134, 101, 230, 210, 101, 134, 120, 132, 101, 134, 101, 230, 210, 101, 134]},
                    {'legendName':'333',
                        'yAxis':[98, 90, 96, 96, 93, 95, 86, 89, 85, 98, 90, 96, 96, 93, 95, 86, 89, 85]},
                    {'legendName':'444',
                        'yAxis':[10, 11, 10, 12, 12, 12, 12, 12, 12, 10, 11, 10, 12, 12, 12, 12, 12, 12]}]
            },
            'cpuVal':{
                'titleTitle':'cpu Usage Group By Node',
                'val':[{'legendName':'111',
                    'yAxis':[220, 182, 191, 234, 290, 330, 310, 290, 330, 220, 182, 191, 234, 290, 330, 310, 290, 330]},
                    {'legendName':'222',
                        'yAxis':[120, 132, 101, 134, 101, 230, 210, 101, 134, 120, 132, 101, 134, 101, 230, 210, 101, 134]},
                    {'legendName':'333',
                        'yAxis':[98, 90, 96, 96, 93, 95, 86, 89, 85, 98, 90, 96, 96, 93, 95, 86, 89, 85]},
                    {'legendName':'444',
                        'yAxis':[10, 11, 10, 12, 12, 12, 12, 12, 12, 10, 11, 10, 12, 12, 12, 12, 12, 12]}]}
        },
        {
            'xAxis':['2014-11-19','2014-11-20','2014-11-21','2014-11-22','2014-11-23','2014-11-24','2014-11-25','2014-11-26','2014-11-27'],
            'memoryVal':{
                'titleTitle':'memory Minion01',
                'val':[{'legendName':'Limit Current',
                    'yAxis':[220, 182, 191, 234, 290, 330, 310, 290, 330]},
                    {'legendName':'Usage Current',
                        'yAxis':[120, 132, 101, 134, 90, 230, 210, 101, 134]},
                    {'legendName':'Working Set Current',
                        'yAxis':[10, 11, 10, 12, 12, 12, 12, 12, 12]}]
            },
            'cpuVal':{
                'titleTitle':'cpu Minion01',
                'val':[{'legendName':'Limit Current',
                    'yAxis':[220, 182, 191, 234, 290, 330, 310, 290, 330]},
                    {'legendName':'Usage Current',
                        'yAxis':[120, 132, 101, 134, 90, 230, 210, 101, 134]},
                    {'legendName':'Working Set Current',
                        'yAxis':[10, 11, 10, 12, 12, 12, 12, 12, 12]}]}
        },
        {
            'xAxis':['2014-11-19','2014-11-20','2014-11-21','2014-11-22','2014-11-23','2014-11-24','2014-11-25','2014-11-26','2014-11-27'],
            'memoryVal':{
                'titleTitle':'memory Minion02',
                'val':[{'legendName':'Limit Current',
                    'yAxis':[220, 182, 191, 234, 290, 330, 310, 290, 330]},
                    {'legendName':'Usage Current',
                        'yAxis':[120, 132, 101, 134, 90, 230, 210, 101, 134]},
                    {'legendName':'Working Set Current',
                        'yAxis':[10, 11, 10, 12, 12, 12, 12, 12, 12]}]
            },
            'cpuVal':{
                'titleTitle':'cpu Minion02',
                'val':[{'legendName':'Limit Current',
                    'yAxis':[220, 182, 191, 234, 290, 330, 310, 290, 330]},
                    {'legendName':'Usage Current',
                        'yAxis':[120, 132, 101, 134, 90, 230, 210, 101, 134]},
                    {'legendName':'Working Set Current',
                        'yAxis':[10, 11, 10, 12, 12, 12, 12, 12, 12]}]}
        }
    ];


    for(var j = 0; j < clusterData.length; j++){
        var option = {
            title: {
                text: ''
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                bottom: '1%',
                data: [],
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '10%',
                containLabel: true
            },
            xAxis : [

            ],
            yAxis : [
                {
                    type : 'value',
                    scale:true,
                    axisLabel : {
                        formatter: '{value} Gib'
                    },
                }
            ],
            series : []
        };
        var xAxis = {
            type : 'category',
            boundaryGap : false,
            data : clusterData[j].xAxis
        };
        option.xAxis.push(xAxis);

        for(var i = 0; i < clusterData[j].memoryVal.val.length; i++){
            var a = {
                name: clusterData[j].memoryVal.val[i].legendName,
                icon: 'roundRect'
            };
            var b = {
                name:clusterData[j].memoryVal.val[i].legendName,
                type:'line',
                barWidth : 5,
                barHeight : 2,
                itemStyle: {
                    normal: {
                        color: colorData[i],
                    }
                },
                areaStyle: {normal: {},color: colorData[i]},
                data:clusterData[j].memoryVal.val[i].yAxis
            };

            var titleText = clusterData[j].memoryVal.titleTitle;
            option.title.text= titleText;

            option.legend.data.push(a);
            option.series.push(b);

        }
        var clusters = echarts.init(document.getElementById('resourceImg').children[j].children[0]);
        clusters.setOption(option);

        option.legend.data = [];
        option.series = [];

    }


    for(var j = 0; j < clusterData.length; j++){
        var option = {
            title: {
                text: ''
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                bottom: '1%',
                data: [],
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '10%',
                containLabel: true
            },
            xAxis : [

            ],
            yAxis : [
                {
                    type : 'value',
                    scale:true,
                    axisLabel : {
                        formatter: '{value} ms'
                    },
                }
            ],
            series : []
        };

        var xAxis = {
            type : 'category',
            boundaryGap : false,
            data : clusterData[j].xAxis
        };
        option.xAxis.push(xAxis);


        for(var k = 0; k < clusterData[j].cpuVal.val.length; k++){
         var a = {
         name: clusterData[j].cpuVal.val[k].legendName,
         icon: 'roundRect'
         };
         var b = {
         name:clusterData[j].cpuVal.val[k].legendName,
         type:'line',
         barWidth : 5,
         barHeight : 2,
         itemStyle: {
         normal: {
         color: colorData[k],
         }
         },
         areaStyle: {normal: {},color: colorData[k]},
         data:clusterData[j].cpuVal.val[k].yAxis
         };

         option.legend.data.push(a);
         option.series.push(b);

         var titleText = clusterData[j].cpuVal.titleTitle;
         option.title.text= titleText;

         }
         var clusters = echarts.init(document.getElementById('resourceImg').children[j+4].children[0]);
         clusters.setOption(option);
    }

</script>

</body>


</html>