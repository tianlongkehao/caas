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
                <div class="caption clearfix" style="padding-bottom: 0px">
                    <ul class="toolbox clearfix">
                        <li><a id="updateCluster" style="cursor:pointer"><i
                                class="fa fa-repeat"></i></a></li>
                    </ul>
                    <form id="search_form" class="form-inline" action="<%=path %>/user/searchByCondition" method="post">
                        <div class="searchFun" style="float: left; text-align: center; margin: 0px 10px; float: right"
                             align="right">
                            <label style="line-height: 35px">时间:</label>
                            <select name="search_time" id="search_time" onchange="searchTime()"
                                    style="height: 30px;display: inline; width: 140px; border-radius: 5px; ">
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

    var colorData = ['#7EB26D', '#EAB839', '#6ED0E0', '#61a0a8', '#749f83', '#ca8622', '#bda29a', '#2f4554', '#d48265', '#00bfff', '#61a0a8', '#61a0a8', '#749f83', '#91c7ae', '#6e7074'];

    var fourPart = [];
    var eachPart = [];
    //默认监控5分钟
    getClusterMonitor("5m");
    //获取监控数据
    function getClusterMonitor(timePeriod) {
        $.ajax({
            url: ctx + "/cluster/getClusterMonitor?timePeriod=" + timePeriod,
            success: function (data) {
                createChart($.parseJSON(data));
            }
        })
    }

    //添加集群中ALL&NODE画布
    function addClusterImg(abcHeight) {
        var clusterTxt = '<div class="table-lists node-lists"  style="margin-top: 10px;width: 100%;height:'+abcHeight+'; float: left;">'
                + '</div>';
        $("#resourceImg").append(clusterTxt);
    }
    //添加MINION节点画布（左侧）
    function addMinImgLeft() {
        var clusterTxt = '<div class="table-lists"  style="margin-top: 10px; width: 49.5%;height:260px;float: left;">'
                + '</div>';
        $("#resourceImg").append(clusterTxt);
    }
    //添加MINION节点画布（右侧）
    function addMinImgRight() {
        var clusterTxt = '<div class="table-lists"  style="margin-top: 10px; width: 49.5%;height:260px;float: right;">'
                + '</div>';
        $("#resourceImg").append(clusterTxt);
    }

    //时间筛选
    function searchTime() {
        removePod();
        var time0val = $("#search_time")[0].value;
        getClusterMonitor(time0val);
    }
    //删除页面所有画布
    function removePod() {
        var imgLst = document.getElementById("resourceImg");
        var count = imgLst.childNodes.length;
        for (var i = 0; i < count; i++) {
            imgLst.removeChild(imgLst.childNodes[0]);
        }
    }
    //根据容器个数计算集群监控画布高度
    function computerSize(clusterDataYval1){
    	//每个图例的宽度为240
    	var perwidth = 240;
    	//画布的宽度
    	var abcwidth = document.getElementById("resourceImg").offsetWidth;
    	//node下图例的个数
    	var abclength = clusterDataYval1.val.length * 2;
    	//画布的宽度除以每个图例的宽度，计算出每行显示几个图例
    	var perlineNum = parseInt(abcwidth/perwidth);
    	//计算显示几行图例
    	var lineNum = Math.ceil(abclength/perlineNum);
    	//根据图例的行数计算画布的高度
    	var abcHeight = 280 + 25*lineNum;
    	//计算图例占画布的百分比
    	var abcpercent = Math.ceil(25*lineNum/abcHeight*100)+"%";
    	var obj = {};
    	obj.abcHeight = abcHeight+"px";
    	obj.abcpercent = abcpercent;
    	return obj;
    }
    //node的seriesData
    function nodeSeriesData(clusterDataNodeVal){
    	   //红色预警
    	var seriesData = [];
    	//第i个node的第m条数据的数据线
    	for (var m = 0; m < clusterDataNodeVal.val.length; m++) {
    		var clusterDataNodeValY = clusterDataNodeVal.val[m].yAxis;
        	var nodeSeriesVal = {
                    name: clusterDataNodeVal.val[m].legendName,
                    type: 'line',
                    barWidth: 5,
                    barHeight: 2,
                    label: {},
                    itemStyle: {
                        normal: {
                            color: colorData[m]
                        }
                    },
                    areaStyle: {normal: {color: colorData[m], opacity: 0.3}},
                    data: clusterDataNodeValY
                };
        	if(m == 1 ){
        		for (var arrayCluster = 0; arrayCluster < clusterDataNodeValY.length; arrayCluster++) {
                    var usageVal = clusterDataNodeVal.val[1].yAxis[arrayCluster];
                    var limitVal = clusterDataNodeVal.val[0].yAxis[arrayCluster];
                    if (usageVal >= limitVal * 0.9 && clusterDataNodeVal.title.indexOf("NETWORK") == -1) {
                    	clusterDataNodeVal.title.indexOf("NETWORK");
                        var nodeSeriesLabel = {
				                normal: {
				                    show: true,
				                    position: 'top',
				                    textStyle: {
				                    	color: '#CC0000' 
				                    }
				                }
				            };
                        var nodeSeriesAreaStyle = {normal: {color: '#CC0000', opacity: 0.6}};
                        nodeSeriesVal.label = nodeSeriesLabel;
                        nodeSeriesVal.areaStyle = nodeSeriesAreaStyle;
                        break;
                    }
                }
        	}
        	seriesData.push(nodeSeriesVal);	
    	}
    	return seriesData;
    }
    //局部刷新
		function update(){
			$.ajax({
	            url: ctx + "/cluster/getClusterMonitor?timePeriod=" + "5m",
	            success: function (data) {
					var clusterData = $.parseJSON(data);
					//遍历mem,cpu,dist,net,生成memory的overall和node监控后，遍历各minion中的memory，生成监控图，以此循环出全部监控图。
					for (var j = 0; j < 4; j++) {
					    var thisPart =  fourPart[j]
			            //overall和node数据
			            var clusterDataYval = clusterData.yValue[0].val[j];
			            //minion数据
			            var clusterDataYval1 = clusterData.yValue[1]
			            //循环overall和node
			            var i = 0;
			            for (i = 0; i < clusterDataYval.val.length; i++) {
			            	var clustersImg = thisPart[i];
			                var clusterDataNodeVal = clusterDataYval.val[i];
			            	var seriesDataNode = nodeSeriesData(clusterDataNodeVal);
							var xAxis = {
		                            type: 'category',
		                            boundaryGap: false,
		                            data: clusterData.xValue
		                        };
							
							var abc =[];
							abc.push(xAxis); 
							
							clustersImg.setOption({
								xAxis: abc
						    });
							clustersImg.setOption({
						        series: seriesDataNode
						    });
						}
			            //over  setOption
						for (var m = 0; m < clusterDataNodeVal.val.length; m++) {
							var clustersImg = thisPart[i+m];
							var xAxis = {
		                            type: 'category',
		                            boundaryGap: false,
		                            data: clusterData.xValue
		                        };
							var abc =[];
							abc.push(xAxis); 
							
							clustersImg.setOption({
								xAxis: abc
						    });
							clustersImg.setOption({
						        series: seriesDataNode
						    });
						}
						
					}
				}
			})
		}
    //minion的series:[]
    function minSeriesData(titleTextMin,clusterYVal,clusterDataYVal1,min,j){
    	var seriesData = [];
    	var clusterYValNum = clusterDataYVal1.val[min].val[j];
    	for (var minxx = 0; minxx < clusterDataYVal1.val[min].val[j].val.length; minxx++) {
    		//红色预警
    		var clusterDataYVal1Min = clusterDataYVal1.val[min].val[j].val[minxx].yAxis
        	var minseriesVal = {
                        name: clusterYValNum.val[minxx].legendName,
                        type: 'line',
                        barWidth: 5,
                        barHeight: 2,
                        label:{},
                        itemStyle: {
                            normal: {
                                color: colorData[minxx]
                            }
                        },
                        areaStyle: {normal: {color: colorData[minxx], opacity: 0.3}},
                        data: clusterDataYVal1Min
                    };
             if(minxx == 1){
            	 for (var arrayMinNum = 0; arrayMinNum < clusterYVal.length; arrayMinNum++) {
                     var usageVal = clusterYValNum.val[1].yAxis[arrayMinNum];
                     var limitVal = clusterYValNum.val[0].yAxis[arrayMinNum];
                     if (usageVal >= limitVal * 0.9 && titleTextMin.indexOf("network") == -1) {
                         var minSeriesLabel = {
 				                normal: {
 				                    show: true,
 				                    position: 'top',
 				                    textStyle: {
 				                    	color: '#CC0000' 
 				                    }
 				                }
 				            };
                         var minSeriesAreaStyle = {normal: {color: '#CC0000', opacity: 0.6}};
                         minseriesVal.label = minSeriesLabel;
                         minseriesVal.areaStyle = minSeriesAreaStyle;
                         break;
                     }
                 }
             }
             seriesData.push(minseriesVal); 
    	}
    	return seriesData;
    }
    function showDynamic(){
		setInterval(function() {
			update();
		}, 1000);
	}
    //监控图
    function createChart(clusterData) {
        var count = 0;
        // mem cpu disk net 
        for (var j = 0; j < 4; j++) {
        	var eachPart = [];
            //遍历cluster,生成memory的overall和node监控后，遍历各minion中的memory，生成监控图，以此循环出全部监控图。
            var clusterDataYval = clusterData.yValue[0].val[j];
            //minion
            var clusterDataYval1 = clusterData.yValue[1]
            // over node 
            for (var i = 0; i < clusterDataYval.val.length; i++) {
            	//遍历overall和node
            	debugger
            	//gaodu
            	var obj = computerSize(clusterDataYval1); 
            	var abcHeight = obj.abcHeight;
            	var abcpercent = obj.abcpercent;
                
                var option = {
                    title: {
                        text: ''
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        bottom: '1%',
                        data: []
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: abcpercent,
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

                //根据titleText确定纵坐标的单位
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
                //cluster中node监控的数值是从各minion节点中获得的，把各节点的数据拼到node里
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
                        clusterData.yValue[0].val[2].val[1].val[diskNodeNum] = diskNode;
                        clusterData.yValue[0].val[3].val[0].val[netNodeNum] = netNode;
                        memNodeNum++;
                        cpuNodeNum++;
                        diskNodeNum++;
                        netNodeNum++;
                    }
                }
                //生成overall和node的监控，i为第几个node
                var clusterDataNodeVal = clusterDataYval.val[i];
                //node中的三条数据线clusterDataNodeVal.val.length=3
               
                for (var m = 0; m < clusterDataNodeVal.val.length; m++) {
                	//第i个node的第m条数据的数据线
                    var clusterDataNodeValY = clusterDataNodeVal.val[m].yAxis;
                    var a = {
                        name: clusterDataNodeVal.val[m].legendName,
                        icon: 'roundRect'
                    };
                    option.legend.data.push(a);

                }
                var seriesDataNode = nodeSeriesData(clusterDataNodeVal);
                option.series = seriesDataNode;
                
                addClusterImg(abcHeight);
                //OVER NODE
                var clustersImg = echarts.init(document.getElementById('resourceImg').children[count]);
                clustersImg.setOption(option);
                count++;
                option.legend.data = [];
                option.series = [];
                eachPart.push(clustersImg);
                //实时刷新OVER NODE
                showDynamic();
            }
            
            
          
            //cluster中的minion监控
            //嵌套，在生成overall和node后，遍历minion中相应数据，生成minion的监控项
            var clusterDataYVal1 = clusterData.yValue[1];

            for (var min = 0; min < clusterDataYVal1.val.length; min++) {
                //minion的个数
                var titleTextMin = clusterDataYVal1.val[min].titleText + " " + clusterDataYVal1.val[min].val[j].title;
                option.title.text = titleTextMin;

//                for(var memNum = 0; memNum < 2; memNum++){
//                    var memoryNode = {
//                        'legendName': clusterDataYVal1.val[min].val[0].val[memNum].legendName + "{" + clusterDataYVal1.val[min].titleText + "}",
//                        'yAxis': clusterDataYVal1.val[min].val[0].val[memNum].yAxis
//                    };
//                    for(var memNode = 0; memNode < clusterDataYVal1.val.length*2; memNode++){
//                        clusterData.yValue[0].val[0].val[1].val[memNode] = memoryNode;
//                    }
//                }
				//第min个minion中，第j个元素，j=1是memory j=2是cpu j=3是disk j=4是network
				//循环数据中的三条数据线clusterDataYVal1.val[min].val[j].val.length=3
                for (var minxx = 0; minxx < clusterDataYVal1.val[min].val[j].val.length; minxx++) {
                    var clusterYVal = clusterDataYVal1.val[min].val[j].val[minxx].yAxis;
                    var clusterYValNum = clusterDataYVal1.val[min].val[j];
                    var mina = {
                        name: clusterDataYVal1.val[min].val[j].val[minxx].legendName,
                        icon: 'roundRect'
                    };
                    option.legend.data.push(mina);
                }
                var seriesDataMin = minSeriesData(titleTextMin,clusterYVal,clusterDataYVal1,min,j);
                option.series = seriesDataMin;

                if (min % 2 == 0) {
                    addMinImgLeft();
                } else {
                    addMinImgRight();
                }
                //MINION
                var minionImg = echarts.init(document.getElementById('resourceImg').children[count]);
                minionImg.setOption(option);
                count++;
                option.legend.data = [];
                option.series = [];
                eachPart.push(clustersImg);
              //实时刷新MINION
                showDynamic();
            }
            fourPart.push(eachPart);
        }

        document.getElementById("updateCluster").addEventListener("click", function() {
        	update();
		}, false);
    }

    //    var clusterData = {
    //        'xValue': ['2014-11-19', '2014-11-20', '2014-11-21', '2014-11-22', '2014-11-23', '2014-11-24', '2014-11-25', '2014-11-26', '2014-11-27'],
    //        'yValue': [
    //            {
    //                'name': 'cluster',
    //                'val': [
    //                    {
    //                        'titleText': 'memory', 'val': [
    //                        {
    //                            'title': 'OVERALL CLUSTER MEMORY USAGE', 'val': [{
    //                            'legendName': 'Limit Current',
    //                            'yAxis': [300, 300, 300, 300, 300, 300, 300, 300, 300]
    //                        },
    //                            {
    //                                'legendName': 'Usage Current',
    //                                'yAxis': [240, 240, 240, 240, 240, 240, 240, 240, 240]
    //                            },
    //                            {
    //                                'legendName': 'Working Set Current',
    //                                'yAxis': [10, 11, 10, 12, 12, 12, 12, 12, 12]
    //                            }]
    //                        }, {
    //                            'title': 'MEMORY USAGE GROUP BY NODE', 'val': []
    //                        }
    //                    ]
    //                    }, {
    //                        'titleText': 'CPU', 'val': [
    //                            {
    //                                'title': 'CPU USAGE GROUP BY NODE', 'val': []
    //                            }
    //                        ]
    //                    }, {
    //                        'titleText': 'DISK', 'val': [
    //                            {
    //                                'title': 'OVERALL CLUSTER DISK USAGE', 'val': [{
    //                                'legendName': 'Limit Current',
    //                                'yAxis': [300, 300, 300, 300, 300, 300, 300, 300, 300]
    //                            },
    //                                {
    //                                    'legendName': 'Usage Current',
    //                                    'yAxis': [275, 300, 275, 300, 275, 330, 275, 275, 275]
    //                                }]
    //                            }, {
    //                                'title': 'DISK USAGE GROUP BY NODE', 'val': []
    //                            }
    //                        ]
    //                    }, {
    //                        'titleText': 'NETWORK', 'val': [
    //                            {
    //                                'title': 'NETWORK USAGE GROUP BY NODE', 'val': []
    //                            }
    //                        ]
    //                    }
    //                ]
    //            },
    //            {
    //                'name': 'minmon', 'val': [{
    //                'titleText': 'minion01', 'val': [{
    //                    'title': 'memory', 'val': [{
    //                        'legendName': 'Limit Current',
    //                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
    //                    },
    //                        {
    //                            'legendName': 'Usage Current',
    //                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
    //                        },
    //                        {
    //                            'legendName': 'Working Set Current',
    //                            'yAxis': [10, 11, 10, 12, 12, 12, 12, 12, 12]
    //                        }]
    //                }, {
    //                    'title': 'cpu', 'val': [{
    //                        'legendName': 'Limit Current',
    //                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
    //                    },
    //                        {
    //                            'legendName': 'Usage Current',
    //                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
    //                        }]
    //                }, {
    //                    'title': 'disk', 'val': [{
    //                        'legendName': 'Limit Current',
    //                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
    //                    },
    //                        {
    //                            'legendName': 'Usage Current',
    //                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
    //                        }]
    //                }, {
    //                    'title': 'network', 'val': [{
    //                        'legendName': 'Limit Current',
    //                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
    //                    },
    //                        {
    //                            'legendName': 'Usage Current',
    //                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
    //                        }]
    //                }]
    //            }, {
    //                'titleText': 'minion02', 'val': [{
    //                    'title': 'memory', 'val': [{
    //                        'legendName': 'Limit Current',
    //                        'yAxis': [200, 162, 171, 214, 270, 310, 290, 270, 310]
    //                    },
    //                        {
    //                            'legendName': 'Usage Current',
    //                            'yAxis': [100, 112, 81, 114, 70, 210, 190, 81, 114]
    //                        },
    //                        {
    //                            'legendName': 'Working Set Current',
    //                            'yAxis': [10, 11, 10, 12, 12, 12, 12, 12, 12]
    //                        }]
    //                }, {
    //                    'title': 'cpu', 'val': [{
    //                        'legendName': 'Limit Current',
    //                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
    //                    },
    //                        {
    //                            'legendName': 'Usage Current',
    //                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
    //                        }]
    //                }, {
    //                    'title': 'disk', 'val': [{
    //                        'legendName': 'Limit Current',
    //                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
    //                    },
    //                        {
    //                            'legendName': 'Usage Current',
    //                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
    //                        }]
    //                }, {
    //                    'title': 'network', 'val': [{
    //                        'legendName': 'Limit Current',
    //                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
    //                    },
    //                        {
    //                            'legendName': 'Usage Current',
    //                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
    //                        }]
    //                }]
    //            }, {
    //                'titleText': 'minion03', 'val': [{
    //                    'title': 'memory', 'val': [{
    //                        'legendName': 'Limit Current',
    //                        'yAxis': [180, 142, 151, 194, 250, 290, 270, 250, 290]
    //                    },
    //                        {
    //                            'legendName': 'Usage Current',
    //                            'yAxis': [80, 92, 61, 94, 50, 190, 170, 61, 94]
    //                        },
    //                        {
    //                            'legendName': 'Working Set Current',
    //                            'yAxis': [10, 11, 10, 12, 12, 12, 12, 12, 12]
    //                        }]
    //                }, {
    //                    'title': 'cpu', 'val': [{
    //                        'legendName': 'Limit Current',
    //                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
    //                    },
    //                        {
    //                            'legendName': 'Usage Current',
    //                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
    //                        }]
    //                }, {
    //                    'title': 'disk', 'val': [{
    //                        'legendName': 'Limit Current',
    //                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
    //                    },
    //                        {
    //                            'legendName': 'Usage Current',
    //                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
    //                        }]
    //                }, {
    //                    'title': 'network', 'val': [{
    //                        'legendName': 'Limit Current',
    //                        'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
    //                    },
    //                        {
    //                            'legendName': 'Usage Current',
    //                            'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
    //                        }]
    //                }]
    //            }]
    //            }
    //        ]
    //    };


</script>

</body>


</html>
