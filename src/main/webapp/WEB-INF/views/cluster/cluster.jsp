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
                                class="ic_left">集群监控</span></span></a>
                    </div>
                </aside>

                <div>
                    <div id="clusterResource_wrap" class="tab_wrap">
                        <div class="detail-info">
                            <div class="info-list" id="resourceImg">

                            </div>

                        </div>

                    </div>
                </div>


            </div>
        </div>
    </article>
</div>

<script type="text/javascript">
    var colorData = ['#7EB26D', '#EAB839', '#6ED0E0','#61a0a8', '#d48265', '#749f83',  '#ca8622', '#bda29a','#2f4554','#00BFFF','#61a0a8','#61a0a8','#749f83' ,'#91c7ae','#6e7074'];

    var clusterData = {
        'xValue': ['2014-11-19', '2014-11-20', '2014-11-21', '2014-11-22', '2014-11-23', '2014-11-24', '2014-11-25', '2014-11-26', '2014-11-27'],
        'yValue': [
            {
                'name': 'cluster',
                'val': [
                    {
                        'titleText': 'memory', 'val': [
                        {
                            'title': 'OVERALL CLUSTER MEMORY USAGE', 'val': [{
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
                        }, {
                            'title': 'MEMORY USAGE GROUP BY NODE', 'val': []
                        }
                    ]
                    }, {
                        'titleText': 'CPU', 'val': [
                            {
                                'title': 'CPU USAGE GROUP BY NODE', 'val': []
                            }
                        ]
                    }, {
                        'titleText': 'DISK', 'val': [
                            {
                                'title': 'OVERALL CLUSTER DISK USAGE', 'val': [{
                                'legendName': 'Limit Current',
                                'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
                            },
                                {
                                    'legendName': 'Usage Current',
                                    'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                                }]
                            }, {
                                'title': 'DISK USAGE GROUP BY NODE', 'val': [{
                                    'legendName': 'Limit Current',
                                    'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
                                },
                                    {
                                        'legendName': 'Usage Current',
                                        'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                                    }]
                            }
                        ]
                    }, {
                        'titleText': 'NETWORK', 'val': [
                            {
                                'title': 'NETWORK USAGE GROUP BY NODE', 'val': [{
                                'legendName': 'Limit Current',
                                'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
                            },
                                {
                                    'legendName': 'Usage Current',
                                    'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                                }]
                            }
                        ]
                    }
                ]
            },
            {
                'name': 'minmon', 'val': [{
                'titleText': 'minion01', 'val': [{
                    'title': 'memory', 'val': [{
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
                }, {
                    'title': 'cpu', 'val': [{
                        'legendName': 'Limit Current',
                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
                    },
                        {
                            'legendName': 'Usage Current',
                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                        }]
                }, {
                    'title': 'disk', 'val': [{
                        'legendName': 'Limit Current',
                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
                    },
                        {
                            'legendName': 'Usage Current',
                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                        }]
                }, {
                    'title': 'network', 'val': [{
                        'legendName': 'Limit Current',
                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
                    },
                        {
                            'legendName': 'Usage Current',
                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                        }]
                }]
            }, {
                'titleText': 'minion02', 'val': [{
                    'title': 'memory', 'val': [{
                        'legendName': 'Limit Current',
                        'yAxis': [200, 162, 171, 214, 270, 310, 290, 270, 310]
                    },
                        {
                            'legendName': 'Usage Current',
                            'yAxis': [100, 112, 81, 114, 70, 210, 190, 81, 114]
                        },
                        {
                            'legendName': 'Working Set Current',
                            'yAxis': [10, 11, 10, 12, 12, 12, 12, 12, 12]
                        }]
                }, {
                    'title': 'cpu', 'val': [{
                        'legendName': 'Limit Current',
                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
                    },
                        {
                            'legendName': 'Usage Current',
                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                        }]
                }, {
                    'title': 'disk', 'val': [{
                        'legendName': 'Limit Current',
                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
                    },
                        {
                            'legendName': 'Usage Current',
                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                        }]
                }, {
                    'title': 'network', 'val': [{
                        'legendName': 'Limit Current',
                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
                    },
                        {
                            'legendName': 'Usage Current',
                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                        }]
                }]
            }, {
                'titleText': 'minion03', 'val': [{
                    'title': 'memory', 'val': [{
                        'legendName': 'Limit Current',
                        'yAxis': [180, 142, 151, 194, 250, 290, 270, 250, 290]
                    },
                        {
                            'legendName': 'Usage Current',
                            'yAxis': [80, 92, 61, 94, 50, 190, 170, 61, 94]
                        },
                        {
                            'legendName': 'Working Set Current',
                            'yAxis': [10, 11, 10, 12, 12, 12, 12, 12, 12]
                        }]
                }, {
                    'title': 'cpu', 'val': [{
                        'legendName': 'Limit Current',
                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
                    },
                        {
                            'legendName': 'Usage Current',
                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                        }]
                }, {
                    'title': 'disk', 'val': [{
                        'legendName': 'Limit Current',
                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
                    },
                        {
                            'legendName': 'Usage Current',
                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
                        }]
                }, {
                    'title': 'network', 'val': [{
                        'legendName': 'Limit Current',
                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
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
    function addClusterImg() {
        var clusterTxt = '<div class="table-lists"  style="margin-top: 10px;width: 1115px;height:260px; float: left">'
                + '</div>';
        $("#resourceImg").append(clusterTxt);
    }
    function addMinImgLeft() {
        var clusterTxt = '<div class="table-lists"  style="margin-top: 10px; width: 563px;height:260px;float: left">'
                + '</div>';
        $("#resourceImg").append(clusterTxt);
    }
    function addMinImgRight() {
        var clusterTxt = '<div class="table-lists"  style="margin-top: 10px; width: 563px;height:260px;float: right">'
                + '</div>';
        $("#resourceImg").append(clusterTxt);
    }

    var count = 0;
    for (var j = 0; j < 4; j++) {
        var clusterDataYval = clusterData.yValue[0].val[j];
        for (var i = 0; i < clusterDataYval.val.length; i++) {
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
                xAxis: [
                    {
                        type: 'category',
                        boundaryGap: false,
                        data: clusterData.xValue
                    }
                ],
                yAxis: [],
                series: []
            };


            var titleText = clusterDataYval.val[i].title;
            option.title.text = titleText;
            if (titleText.indexOf("CPU") != -1) {
                var a1 = {
                    type: 'value',
                    scale: true,
                    axisLabel: {
                        formatter: '{value} ms'
                    }
                };
                option.yAxis.push(a1);
            } else if (titleText.indexOf("NETWORK") != -1) {
                var b1 = {
                    type: 'value',
                    scale: true,
                    axisLabel: {
                        formatter: '{value} KBps'
                    }
                };
                option.yAxis.push(b1);
            } else {
                var c1 = {
                    type: 'value',
                    scale: true,
                    axisLabel: {
                        formatter: '{value} Gib'
                    }
                };
                option.yAxis.push(c1);
            }

            var memNodeNum = 0;
            var cpuNodeNum = 0;
            var diskNodeNum = 0;
            var netNodeNum = 0;
            for (var minToCluster = 0; minToCluster < clusterData.yValue[1].val.length; minToCluster++) {
                for (var minNum = 0; minNum < 2; minNum++) {
                    var titleText = clusterData.yValue[1].val[minToCluster].titleText;
                    var memoryNode = {
                        'legendName': clusterData.yValue[1].val[minToCluster].val[0].val[minNum].legendName + "{" + titleText + "}",
                        'yAxis': clusterData.yValue[1].val[minToCluster].val[0].val[minNum].yAxis
                    };
                    var cpuNode = {
                        'legendName': clusterData.yValue[1].val[minToCluster].val[1].val[minNum].legendName + "{" + titleText + "}",
                        'yAxis': clusterData.yValue[1].val[minToCluster].val[1].val[minNum].yAxis
                    };
                    var diskNode = {
                        'legendName': clusterData.yValue[1].val[minToCluster].val[2].val[minNum].legendName + "{" + titleText + "}",
                        'yAxis': clusterData.yValue[1].val[minToCluster].val[2].val[minNum].yAxis
                    };
                    var netNode = {
                        'legendName': clusterData.yValue[1].val[minToCluster].val[3].val[minNum].legendName + "{" + titleText + "}",
                        'yAxis': clusterData.yValue[1].val[minToCluster].val[3].val[minNum].yAxis
                    };
                    clusterData.yValue[0].val[0].val[1].val[memNodeNum] = memoryNode;
                    clusterData.yValue[0].val[1].val[0].val[cpuNodeNum] = cpuNode;
                    clusterData.yValue[0].val[2].val[0].val[cpuNodeNum] = diskNode;
                    clusterData.yValue[0].val[3].val[0].val[netNodeNum] = netNode;
                    memNodeNum++;
                    cpuNodeNum++;
                    diskNodeNum++;
                    netNodeNum++;
                }
            }

            for (var minToCluster = 0; minToCluster < clusterData.yValue[1].val.length; minToCluster++) {
                for (var memNum = 0; memNum < 2; memNum++) {


                    clusterData.yValue[0].val[0].val[1].val[cpuNode] = memoryNode;
                    cpuNode++;
                }
            }

            var clusterDataMinVal = clusterDataYval.val[i];
            for (var m = 0; m < clusterDataMinVal.val.length; m++) {

                var clusterYval = clusterDataMinVal.val[m].yAxis;

                var a = {
                    name: clusterDataMinVal.val[m].legendName,
                    icon: 'roundRect'
                };
                var b = {
                    name: clusterDataMinVal.val[m].legendName,
                    type: 'line',
                    barWidth: 5,
                    barHeight: 2,
                    itemStyle: {
                        normal: {
                            color: colorData[m],
                        }
                    },
                    stack: '总量',
                    areaStyle: {normal: {}, color: colorData[m]},
                    data: clusterYval
                };
                option.legend.data.push(a);
                option.series.push(b);
            }
            addClusterImg();
            var clustersImg = echarts.init(document.getElementById('resourceImg').children[count]);
            clustersImg.setOption(option);
            count++;
            option.legend.data = [];
            option.series = [];
        }
        var clusterDataYVal1 = clusterData.yValue[1];
        for (var min = 0; min < clusterDataYVal1.val.length; min++) {
            var titleTextMin = clusterDataYVal1.val[min].titleText + " " + clusterDataYVal1.val[min].val[j].title;
            option.title.text = titleTextMin;

//            for(var memNum = 0; memNum < 2; memNum++){
//                var memoryNode = {
//                    'legendName': clusterDataYVal1.val[min].val[0].val[memNum].legendName + "{" + clusterDataYVal1.val[min].titleText + "}",
//                    'yAxis': clusterDataYVal1.val[min].val[0].val[memNum].yAxis
//                };
//                for(var memNode = 0; memNode < clusterDataYVal1.val.length*2; memNode++){
//                    clusterData.yValue[0].val[0].val[1].val[memNode] = memoryNode;
//                }
//            }

            for (var minxx = 0; minxx < clusterDataYVal1.val[min].val[j].val.length; minxx++) {
                var clusterYVal = clusterDataYVal1.val[min].val[j].val[minxx].yAxis;
                var mina = {
                    name: clusterDataYVal1.val[min].val[j].val[minxx].legendName,
                    icon: 'roundRect'
                };
                var minb = {
                    name: clusterDataYVal1.val[min].val[j].val[minxx].legendName,
                    type: 'line',
                    barWidth: 5,
                    barHeight: 2,
                    itemStyle: {
                        normal: {
                            color: colorData[minxx],
                        }
                    },
                    stack: '总量',
                    areaStyle: {normal: {}, color: colorData[minxx]},
                    data: clusterYVal
                };
                option.legend.data.push(mina);
                option.series.push(minb);
            }


            if (min % 2 == 0) {
                addMinImgLeft();
            } else {
                addMinImgRight();
            }
            var minionImg = echarts.init(document.getElementById('resourceImg').children[count]);
            minionImg.setOption(option);
            count++;
            option.legend.data = [];
            option.series = [];
        }
    }


</script>

</body>


</html>