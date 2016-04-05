<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/cluster.css"/>
    <script type="text/javascript" src="<%=path %>/js/cluster/cluster.js"></script>
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
                        <li><a href="javascript:window.location.reload(true);" id="userReloadBtn"><i
                                class="fa fa-repeat"></i></a></li>
                    </ul>
                    <form id="search_form" class="form-inline" action="<%=path %>/user/searchByCondition" method="post">
                        <div class="searchFun" style="float: left; text-align: center; margin: 0px 10px" align="right">
                            <label style="line-height: 35px">服务:</label>
                            <select name="search_service" id="search_service" onchange="searchService()"
                                    style="height: 30px;display: inline; width: 140px; border-radius: 5px;">
                                <option name="search_service" value="0"></option>
                            </select>
                        </div>
                        <div class="searchFun" style="float: left; text-align: center; margin: 0px 10px" align="right">
                            <label style="line-height: 35px">容器:</label>
                            <select name="search_container" id="search_container" onchange="searchContainer()"
                                    style="height: 30px;display: inline; width: 140px; border-radius: 5px;">
                                <option name="search_container" selected="" value="0"></option>
                            </select>
                        </div>
                        <div class="searchFun" style="float: left; text-align: center; margin: 0px 10px; float: right"
                             align="right">
                            <label style="line-height: 35px">时间:</label>
                            <select name="search_time" id="search_time"
                                    style="height: 30px;display: inline; width: 140px; border-radius: 5px;">
                                <option name="search_time" value="5m">最近5分钟</option>
                                <option name="search_time" value="30m">最近30分钟</option>
                                <option name="search_time" value="1h">最近1小时</option>
                                <option name="search_time" value="6h">最近6个小时</option>
                                <option name="search_time" value="12h">最近12小时</option>
                                <option name="search_time" value="24h">最近24小时</option>
                                <option name="search_time" value="7d">最近7天</option>
                                <option name="search_time" value="30d">最近30天</option>
                            </select>
                        </div>

                    </form>
                </div>


                <div>

                    <div id="clusterResource_wrap" class="tab_wrap">
                        <div class="detail-info">
                            <div class="info-list" id="resourceContainer">

                            </div>

                        </div>

                    </div>
                </div>


            </div>
        </div>
    </article>
</div>

<script type="text/javascript">

    var colorData = ['#c5e1d2', '#abd4bd', '#91c7a9'];

    var containerData = {
        'xValue': ['2014-11-19', '2014-11-20', '2014-11-21', '2014-11-22', '2014-11-23', '2014-11-24', '2014-11-25', '2014-11-26', '2014-11-27'],
        'yValue': [{
            'name': 'service01', 'val': [{
                'titleText': 'container01',
                'val': [{
                    'title': 'memory',
                    'val': [{
                        'legendName': 'Limit Current',
                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
                    },
                        {
                            'legendName': 'Usage Current',
                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                        },
                        {
                            'legendName': 'Working Set Current',
                            'yAxis': [10, 11, 10, 12, 12, 12, 12, 12, 12]
                        }]
                },
                    {
                        'title': 'cpu',
                        'val': [{
                            'legendName': 'Limit Current',
                            'yAxis': [320, 182, 391, 234, 390, 330, 310, 290, 330]
                        },
                            {
                                'legendName': 'Usage Current',
                                'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                            }]
                    }]
            },
                {
                    'titleText': 'container02', 'val': [{

                    'title': 'memory',
                    'val': [{
                        'legendName': 'Limit Current',
                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
                    },
                        {
                            'legendName': 'Usage Current',
                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                        },
                        {
                            'legendName': 'Working Set Current',
                            'yAxis': [98, 90, 96, 96, 93, 95, 86, 89, 85]
                        }]},
                    {
                        'title': 'cpu',
                        'val': [{
                            'legendName': 'cLimit Current',
                            'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
                        },
                            {
                                'legendName': 'cUsage Current',
                                'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                            }]

                    }]
                }]
        },
            {
                'name': 'service02', 'val': [{
                'titleText': 'container03', 'val': [{

                        'title': 'memory',
                        'val': [{
                            'legendName': 'Limit Current',
                            'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
                        },
                            {
                                'legendName': 'Usage Current',
                                'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                            },
                            {
                                'legendName': 'Working Set Current',
                                'yAxis': [10, 11, 10, 12, 12, 12, 12, 12, 12]
                            }]}
                    ,

                    {'title': 'cpu',
                        'val': [{
                            'legendName': 'cpu Limit Current',
                            'yAxis': [320, 182, 391, 234, 390, 330, 310, 290, 330]
                        },
                            {
                                'legendName': 'cpu Usage Current',
                                'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                            }]


                }]
            }, {
                'titleText': 'container04', 'val': [{

                        'title': 'memory',
                        'val': [{
                            'legendName': 'Limit Current',
                            'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
                        },
                            {
                                'legendName': 'Usage Current',
                                'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                            },
                            {
                                'legendName': 'Working Set Current',
                                'yAxis': [10, 11, 10, 12, 12, 12, 12, 12, 12]
                            }]}
                    ,

                    {'title': 'cpu',
                        'val': [{
                            'legendName': 'Limit Current',
                            'yAxis': [320, 182, 391, 234, 390, 330, 310, 290, 330]
                        },
                            {
                                'legendName': 'Usage Current',
                                'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                            }]


                }]
            }]
            }
        ]
    };
    showContainerImg(containerData);

    function addContainerMemImg() {
        var memTxt = '<div class="table-lists"  style="margin-top: 10px; float: left;width: 563px;height:260px;">'
                + '</div>';
        $("#resourceContainer").append(memTxt);
    }
    function addContainerCpuImg() {
        var cpuTxt = '<div class="table-lists"  style="margin-top: 10px; float: right;width: 563px;height:260px;">'
                + '</div>';
        $("#resourceContainer").append(cpuTxt);
    }

    function addContainerSerOpt() {
        var serOpt = '<option name="search_service" value=""></option>'
        $("#search_service").append(serOpt);
    }
    function addContainerConOpt() {
        var conOpt = '<option name="search_container" value=""></option>'
        $("#search_container").append(conOpt);
    }

    function showContainerImg(containerData) {
        var count = 0;
        var containerNum = 0;
        for (var s = 0; s < containerData.yValue.length; s++) {
            var containerDataYval = containerData.yValue[s];
            //search_service_options
            addContainerSerOpt();
            var serviceOpt = document.getElementById('search_service').children[s + 1];
            serviceOpt.value = containerDataYval.name;
            serviceOpt.innerHTML = containerDataYval.name;


            for (var j = 0; j < containerDataYval.val.length; j++) {
                //search_container_options
                addContainerConOpt();
                containerNum++;
                var containerOpt = document.getElementById('search_container').children[containerNum];
                containerOpt.value = containerDataYval.val[j].titleText;
                containerOpt.innerHTML = containerDataYval.val[j].titleText;
                $(containerOpt).addClass(containerDataYval.name);
                $(containerOpt).addClass(containerDataYval.val[j].titleText);

                var option = {
                    title: {
                        text: ''
                    },
                    tooltip: {
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
                    xAxis: [],
                    yAxis: [],
                    series: []
                };

                var xAxis = {
                    type: 'category',
                    boundaryGap: false,
                    data: containerData.xValue
                };
                option.xAxis.push(xAxis);
                count = showContainerMemImg(count, containerDataYval, j, option);
                count = showContainerCpuImg(count, containerDataYval, j, option);
            }

        }

    }
    function showContainerMemImg(count, containerDataYval, j, option) {
        var c = {
            type: 'value',
            scale: true,
            axisLabel: {
                formatter: '{value} Gib'
            },
        };
        option.yAxis.push(c);
        var containerDataYmem = containerDataYval.val[j].val[0];

        for (var i = 0; i < containerDataYmem.val.length; i++) {
            var containerYval = containerDataYmem.val[i].yAxis;
            var a = {
                name: containerDataYmem.val[i].legendName,
                icon: 'roundRect'
            };
            var b = {
                name: containerDataYmem.val[i].legendName,
                type: 'line',
                barWidth: 5,
                barHeight: 2,
                itemStyle: {
                    normal: {
                        color: colorData[i]
                    }
                },
                areaStyle: {normal: {}, color: colorData[i]},
                data: containerYval
            };
            option.legend.data.push(a);
            option.series.push(b);

            var titleText = containerDataYval.name + " " + containerDataYval.val[j].titleText + " " + containerDataYmem.title;
            option.title.text = titleText;
        }
        addContainerMemImg();
        var containersMemImg = document.getElementById('resourceContainer').children[count];
        $(containersMemImg).addClass(containerDataYval.name);
        $(containersMemImg).addClass(containerDataYval.val[j].titleText);

        var containersMem = echarts.init(containersMemImg);
        containersMem.setOption(option);

        count++;
        option.legend.data = [];
        option.series = [];
        option.yAxis = [];
        return count;
    }

    function showContainerCpuImg(count, containerDataYval, j, option) {
        var f = {
            type: 'value',
            scale: true,
            axisLabel: {
                formatter: '{value} ms'
            },
        };
        option.yAxis.push(f);
        var containerDataYcpu = containerDataYval.val[j].val[0];

        for (var k = 0; k < containerDataYcpu.val.length; k++) {
            var containerYCpuval = containerDataYcpu.val[k].yAxis;
            var d = {
                name: containerDataYcpu.val[k].legendName,
                icon: 'roundRect'
            };
            var e = {
                name: containerDataYcpu.val[k].legendName,
                type: 'line',
                barWidth: 5,
                barHeight: 2,
                itemStyle: {
                    normal: {
                        color: colorData[k]
                    }
                },
                areaStyle: {normal: {}, color: colorData[k]},
                data: containerYCpuval
            };

            option.legend.data.push(d);
            option.series.push(e);

            var titleTextCpu = containerDataYval.name + " " + containerDataYval.val[j].titleText + " " + containerDataYcpu.title;
            option.title.text = titleTextCpu;

        }
        addContainerCpuImg();
        var containersCpuImg = document.getElementById('resourceContainer').children[count];
        $(containersCpuImg).addClass(containerDataYval.name);
        $(containersCpuImg).addClass(containerDataYval.val[j].titleText);
        var containersCpu = echarts.init(containersCpuImg);
        containersCpu.setOption(option);
        count++;
        return count;
    }

</script>
</body>
</html>