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
                                class="ic_left">容器监控</span></span></a>
                    </div>
                </aside>

                <div class="caption clearfix" style="padding-bottom: 0px">
                    <ul class="toolbox clearfix">
                        <li><a href="javascript:window.location.reload(true);" id="userReloadBtn"><i class="fa fa-repeat"></i></a></li>
                    </ul>
                    <form id="search_form" class="form-inline" action="<%=path %>/user/searchByCondition" method="post">
                        <div class="searchFun" style="float: left; text-align: center; margin: 0px 10px" align="right">
                            <label style="line-height: 35px">服务:</label>
                            <select name="search_service" id="search_service" onchange="searchService()"
                                    style="height: 30px;display: inline; width: 140px; border-radius: 5px;">
                                <option name="search_service" ></option>
                                <option name="search_service" value="service01">service01</option>
                                <option name="search_service" value="service02">service02</option>
                            </select>
                        </div>
                        <div class="searchFun" style="float: left; text-align: center; margin: 0px 10px" align="right">
                            <label style="line-height: 35px">容器:</label>
                            <select name="search_container" id="search_container" onchange="searchContainer()"
                                    style="height: 30px;display: inline; width: 140px; border-radius: 5px;">
                                <option name="search_container" selected=""></option>
                                <option name="search_container" class="service01 container01" value="container01">container01</option>
                                <option name="search_container" class="service01 container02" value="container02">container02</option>
                                <option name="search_container" class="service02 container03" value="container03">container03</option>
                                <option name="search_container" class="service02 container04" value="container04">container04</option>
                            </select>
                        </div>
                        <div class="searchFun" style="float: left; text-align: center; margin: 0px 10px; float: right" align="right">
                            <label style="line-height: 35px">时间:</label>
                            <select name="search_province" id="search_time" style="height: 30px;display: inline; width: 140px; border-radius: 5px;" >
                                <option name="search_time" ></option>
                                <option name="search_time" value="1">最近6个小时</option>
                                <option name="search_time" value="2">最近一天</option>
                                <option name="search_time" value="2">最近三天</option>
                            </select>
                        </div>

                    </form>
                </div>


                <div>

                    <div id="clusterResource_wrap" class="tab_wrap">
                        <div class="detail-info">
                            <div class="info-list" id="resourceContainer">

                                <div class="table-lists service01 container01"  style="margin-top: 10px; float: left">
                                    <div style="width: 563px;height:260px;"></div>
                                </div>
                                <div  class="table-lists service01 container01"  style="margin-top: 10px; float: right">
                                    <div style="width: 563px;height:260px;"></div>
                                </div>
                                <div  class="table-lists service01 container02"  style="margin-top: 10px; float: left">
                                    <div style="width: 563px;height:260px;"></div>
                                </div>
                                <div  class="table-lists service01 container02" style="margin-top: 10px; float: right">
                                    <div style="width: 563px;height:260px;"></div>
                                </div>
                                <div class="table-lists service02 container03"  style="margin-top: 10px; float: left">
                                    <div style="width: 563px;height:260px;"></div>
                                </div>
                                <div  class="table-lists service02 container03"  style="margin-top: 10px; float: right">
                                    <div style="width: 563px;height:260px;"></div>
                                </div>
                                <div  class="table-lists service02 container04"  style="margin-top: 10px; float: left">
                                    <div style="width: 563px;height:260px;"></div>
                                </div>
                                <div  class="table-lists service02 container04" style="margin-top: 10px; float: right">
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

    var colorData = ['#c5e1d2','#abd4bd','#91c7a9'];
    var containerData =[{
        'xAxis':['2014-11-19','2014-11-20','2014-11-21','2014-11-22','2014-11-23','2014-11-24','2014-11-25','2014-11-26','2014-11-27'],
        'memoryVal':{'titleText':'container01 memory',
            'val':[{'legendName' : 'memory Limit Current',
            'yAxis':[220, 182, 191, 234, 290, 330, 310, 290, 330]},
            {'legendName' : 'memory Usage Current',
                'yAxis':[120, 132, 101, 134, 90, 230, 210, 101, 134]},
            {'legendName' : 'memory Working Set Current',
                'yAxis':[10, 11, 10, 12, 12, 12, 12, 12, 12]}]},


        'cpuVal':{'titleText':'container01 cpu',
            'val':[{'legendName' : 'cpu Limit Current',
            'yAxis':[320, 182, 391, 234, 390, 330, 310, 290, 330]},
            {'legendName' : 'cpu Usage Current',
                'yAxis':[120, 132, 101, 134, 90, 230, 210, 101, 134]},
            {'legendName' : 'cpu Working Set Current',
                'yAxis':[10, 11, 10, 12, 12, 12, 12, 12, 12]}]}

    },
        {
            'xAxis':['2014-11-19','2014-11-20','2014-11-21','2014-11-22','2014-11-23','2014-11-24','2014-11-25','2014-11-26','2014-11-27',
                '2014-11-28','2014-11-29','2014-11-30','2014-12-01','2014-12-02','2014-12-03','2014-12-04','2014-12-05','2014-12-06'],
            'memoryVal':{'titleText':'container02 memory',
                'val':[{'legendName' : 'Limit Current',
                'yAxis':[220, 182, 191, 234, 290, 330, 310, 290, 330, 220, 182, 191, 234, 290, 330, 310, 290, 330]},
                {'legendName' : 'Usage Current',
                    'yAxis':[120, 132, 101, 134, 90, 230, 210, 101, 134, 120, 132, 101, 134, 90, 230, 210, 101, 134]},
                {'legendName' : 'Working Set Current',
                    'yAxis':[98, 90, 96, 96, 93, 95, 86, 89, 85, 98, 90, 96, 96, 93, 95, 86, 89, 85]}]},
            'cpuVal':{'titleText':'container02 cpu',
                'val':[{'legendName' : 'cLimit Current',
                'yAxis':[220, 182, 191, 234, 290, 330, 310, 290, 330, 220, 182, 191, 234, 290, 330, 310, 290, 330]},
                {'legendName' : 'cUsage Current',
                    'yAxis':[120, 132, 101, 134, 90, 230, 210, 101, 134, 120, 132, 101, 134, 90, 230, 210, 101, 134]},
                {'legendName' : 'cWorking Set Current',
                    'yAxis':[98, 90, 96, 96, 93, 95, 86, 89, 85, 98, 90, 96, 96, 93, 95, 86, 89, 85]}]}
        },
        {
            'xAxis':['2014-11-19','2014-11-20','2014-11-21','2014-11-22','2014-11-23','2014-11-24','2014-11-25','2014-11-26','2014-11-27'],
            'memoryVal':{'titleText':'container03 memory',
                'val':[{'legendName' : 'memory Limit Current',
                    'yAxis':[220, 182, 191, 234, 290, 330, 310, 290, 330]},
                    {'legendName' : 'memory Usage Current',
                        'yAxis':[120, 132, 101, 134, 90, 230, 210, 101, 134]},
                    {'legendName' : 'memory Working Set Current',
                        'yAxis':[10, 11, 10, 12, 12, 12, 12, 12, 12]}]},


            'cpuVal':{'titleText':'container03 cpu',
                'val':[{'legendName' : 'cpu Limit Current',
                    'yAxis':[320, 182, 391, 234, 390, 330, 310, 290, 330]},
                    {'legendName' : 'cpu Usage Current',
                        'yAxis':[120, 132, 101, 134, 90, 230, 210, 101, 134]},
                    {'legendName' : 'cpu Working Set Current',
                        'yAxis':[10, 11, 10, 12, 12, 12, 12, 12, 12]}]}

        },{
            'xAxis':['2014-11-19','2014-11-20','2014-11-21','2014-11-22','2014-11-23','2014-11-24','2014-11-25','2014-11-26','2014-11-27'],
            'memoryVal':{'titleText':'container04 memory',
                'val':[{'legendName' : 'memory Limit Current',
                    'yAxis':[220, 182, 191, 234, 290, 330, 310, 290, 330]},
                    {'legendName' : 'memory Usage Current',
                        'yAxis':[120, 132, 101, 134, 90, 230, 210, 101, 134]},
                    {'legendName' : 'memory Working Set Current',
                        'yAxis':[10, 11, 10, 12, 12, 12, 12, 12, 12]}]},


            'cpuVal':{'titleText':'container04 cpu',
                'val':[{'legendName' : 'cpu Limit Current',
                    'yAxis':[320, 182, 391, 234, 390, 330, 310, 290, 330]},
                    {'legendName' : 'cpu Usage Current',
                        'yAxis':[120, 132, 101, 134, 90, 230, 210, 101, 134]},
                    {'legendName' : 'cpu Working Set Current',
                        'yAxis':[10, 11, 10, 12, 12, 12, 12, 12, 12]}]}

        }
    ];

    for(var j = 0; j< containerData.length; j++){
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
            xAxis : [],
            yAxis : [],
            series : []
        };

        var xAxis = {
            type : 'category',
            boundaryGap : false,
            data : containerData[j].xAxis
        };
        option.xAxis.push(xAxis);

        var c = {
            type : 'value',
            scale:true,
            axisLabel : {
                formatter: '{value} Gib'
            },
        };
        option.yAxis.push(c);

        for(var i = 0; i< containerData[j].memoryVal.val.length; i++){
            var a = {
                name: containerData[j].memoryVal.val[i].legendName,
                icon: 'roundRect'
            };
            var b = {
                name:containerData[j].memoryVal.val[i].legendName,
                type:'line',
                barWidth : 5,
                barHeight : 2,
                itemStyle: {
                    normal: {
                        color: colorData[i],
                    }
                },
                areaStyle: {normal: {},color: colorData[i]},
                data: containerData[j].memoryVal.val[i].yAxis
            };
            option.legend.data.push(a);
            option.series.push(b);

            var titleText = containerData[j].memoryVal.titleText;
            option.title.text= titleText;

        }
        var containers = echarts.init(document.getElementById('resourceContainer').children[j*2].children[0]);
        containers.setOption(option);
        option.legend.data = [];
        option.series = [];
        option.yAxis = [];


        var f = {
            type : 'value',
            scale:true,
            axisLabel : {
                formatter: '{value} ms'
            },
        };
        option.yAxis.push(f);

        for(var k = 0; k< containerData[j].cpuVal.val.length; k++){
            var d = {
                name: containerData[j].cpuVal.val[k].legendName,
                icon: 'roundRect'
            };
            var e = {
                name:containerData[j].cpuVal.val[k].legendName,
                type:'line',
                barWidth : 5,
                barHeight : 2,
                itemStyle: {
                    normal: {
                        color: colorData[k],
                    }
                },
                areaStyle: {normal: {},color: colorData[k]},
                data: containerData[j].cpuVal.val[k].yAxis
            };

            option.legend.data.push(d);
            option.series.push(e);

            var titleText01 = containerData[j].cpuVal.titleText;
            option.title.text= titleText01;

        }
        var containers = echarts.init(document.getElementById('resourceContainer').children[j*2+1].children[0]);
        containers.setOption(option);

    }




</script>

</body>


</html>