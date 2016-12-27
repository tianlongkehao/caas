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
<jsp:include page="../frame/bcm-menu.jsp" flush="true">
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
                                class="ic_left">服务监控</span></span></a>
                    </div>
                </aside>

                <div>

                    <div id="clusterResource_wrap" class="tab_wrap">
                        <div class="detail-info">
                            <div class="info-list" id="resourceContainer">

                                <div class="table-lists" style="margin-top: 10px; float: left">
                                    <div id="container01" style="width: 563px;height:260px;"></div>
                                </div>
                                <div class="table-lists" style="margin-top: 10px; float: right">
                                    <div id="container02" style="width: 563px;height:260px;"></div>
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
    var containerData =[{'titleText':'Service01','val':[{'legendName' : 'Limit Current',
                        'xAxis':['2014-11-19','2014-11-20','2014-11-21','2014-11-22','2014-11-23','2014-11-24','2014-11-25','2014-11-26','2014-11-27'],
                        'yAxis':[220, 182, 191, 234, 290, 330, 310, 290, 330]},
        {'legendName' : 'Usage Current',
            'xAxis':['2014-11-19','2014-11-20','2014-11-21','2014-11-22','2014-11-23','2014-11-24','2014-11-25','2014-11-26','2014-11-27'],
            'yAxis':[120, 132, 101, 134, 90, 230, 210, 101, 134]},
        {'legendName' : 'Working Set Current',
            'xAxis':['2014-11-19','2014-11-20','2014-11-21','2014-11-22','2014-11-23','2014-11-24','2014-11-25','2014-11-26','2014-11-27'],
            'yAxis':[10, 11, 10, 12, 12, 12, 12, 12, 12]},

    ]},
    {'titleText':'Service02','val':[{'legendName' : 'Limit Current',
        'xAxis':['2014-11-19','2014-11-20','2014-11-21','2014-11-22','2014-11-23','2014-11-24','2014-11-25','2014-11-26','2014-11-27',
            '2014-11-28','2014-11-29','2014-11-30','2014-12-01','2014-12-02','2014-12-03','2014-12-04','2014-12-05','2014-12-06'],
        'yAxis':[220, 182, 191, 234, 290, 330, 310, 290, 330, 220, 182, 191, 234, 290, 330, 310, 290, 330]},
        {'legendName' : 'Usage Current',
            'xAxis':['2014-11-19','2014-11-20','2014-11-21','2014-11-22','2014-11-23','2014-11-24','2014-11-25','2014-11-26','2014-11-27',
                '2014-11-28','2014-11-29','2014-11-30','2014-12-01','2014-12-02','2014-12-03','2014-12-04','2014-12-05','2014-12-06'],
            'yAxis':[120, 132, 101, 134, 90, 230, 210, 101, 134, 120, 132, 101, 134, 90, 230, 210, 101, 134]},
        {'legendName' : 'Working Set Current',
            'xAxis':['2014-11-19','2014-11-20','2014-11-21','2014-11-22','2014-11-23','2014-11-24','2014-11-25','2014-11-26','2014-11-27',
                '2014-11-28','2014-11-29','2014-11-30','2014-12-01','2014-12-02','2014-12-03','2014-12-04','2014-12-05','2014-12-06'],
            'yAxis':[98, 90, 96, 96, 93, 95, 86, 89, 85, 98, 90, 96, 96, 93, 95, 86, 89, 85]}
    ]}
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

        var titleText = containerData[j].titleText;
        option.title.text= titleText;

        var xAxis = {
            type : 'category',
            boundaryGap : false,
            data : containerData[j].val[0].xAxis
        };
        option.xAxis.push(xAxis);

        for(var i = 0; i< containerData[j].val.length; i++){
            var a = {
                name: containerData[j].val[i].legendName,
                icon: 'roundRect'
            };
            var b = {
                name:containerData[j].val[i].legendName,
                type:'line',
                barWidth : 5,
                barHeight : 2,
                itemStyle: {
                    normal: {
                        color: colorData[i],
                    }
                },
                areaStyle: {normal: {},color: colorData[i]},
                data: containerData[j].val[i].yAxis
            };
            option.legend.data.push(a);
            option.series.push(b);
        }
        var containers = echarts.init(document.getElementById('resourceContainer').children[j].children[0]);
        containers.setOption(option);
    }







</script>

</body>


</html>