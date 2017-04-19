$(document).ready(function(){
	showEchart(optionDay);
	 
});
var optionDay = {
	    title : {
	        text: '集群资源使用情况(天)',
	    },
	    tooltip : {
	        trigger: 'axis'
	    },
	    legend: {
	        data:['CPU','内存']
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            //mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType : {show: true, type: ['line', 'bar']},
	            restore : {show: true},
	            //saveAsImage : {show: true}
	        }
	    },
	    
	    calculable : true,
	    xAxis : [
	        {
	            type : 'category',
	            data : ['2017/03/27','2017/03/28','2017/03/29','2017/03/30','2017/03/31','2017/04/01','2017/04/02','2017/04/03','2017/04/04','2017/04/05','2017/04/06','2017/04/07',
	                    '2017/04/08','2017/04/09','2017/04/10','2017/04/11','2017/04/12','2017/04/13','2017/04/14','2017/04/15','2017/04/16','2017/04/17','2017/04/18','2017/04/19']
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value'
	        }
	    ],
	    dataZoom : {
	        show : true,
	        realtime: true,
	        start: 75,
            end:100
	    },
	    
	    series : [
	        {
	            name:'CPU',
	            type:'bar',
	            data:[212.0, 214.0, 217.0, 223.0, 225.0, 236.0, 235.0, 222.0, 232.0, 220.0, 226.0, 223.0,
	                  222.0, 224.0, 237.0, 263.0, 265.0, 266.0, 265.0, 282.0, 292.0, 290.0, 326.0, 323.0],
	            markPoint : {
	                data : [
	                    {type : 'max', name: '最大值'},
	                    {type : 'min', name: '最小值'}
	                ]
	            },
	            markLine : {
	                data : [
	                    {type : 'average', name: '平均值'}
	                ]
	            }
	        },
	        {
	            name:'内存',
	            type:'bar',
	            data:[442.6, 445.9, 449.0, 456.4, 458.7, 470.7, 475.6, 452.2, 488.7, 448.8, 476.0, 472.3,
	                  444.8, 465.0, 489.0, 646.8, 698.7, 650.7, 675.6, 700.2, 720.7, 718.8, 760.0, 772.3],
	            markPoint : {
	                data : [
	                    {type : 'max', name: '最大值'},
	                    {type : 'min', name: '最小值'}
	                ]
	            },
	            markLine : {
	                data : [
	                    {type : 'average', name : '平均值'}
	                ]
	            }
	        }
	    ]
	};


var optionWeek = {
	    title : {
	        text: '集群资源使用情况（周）'
	    },
	    tooltip : {
	        trigger: 'axis'
	    },
	    legend: {
	        data:['CPU','内存']
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            //mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType : {show: true, type: ['line', 'bar']},
	            restore : {show: true},
	            //saveAsImage : {show: true}
	        }
	    },
	    calculable : true,
	    xAxis : [
	        {
	            type : 'category',
	            data : ['2015/11/28-2016/12/04','2016/12/05-2016/12/11','2016/12/12-2016/12/18','2016/12/19-2016/12/25','2016/12/26-2017/01/01','2017/01/02-2017/01/08','2017/01/09-2017/01/15','2017/01/16-2017/01/22','2017/01/23-2017/01/29','2017/01/30-2017/02/05',
	                    '2017/02/06-2017/02/12','2017/02/13-2017/02/19','2017/02/20-2017/02/26','2017/02/27-2017/03/05','2017/03/06-2017/03/12','2017/03/13-2017/03/19','2017/03/20-2017/03/26','2017/03/27-2017/04/02','2017/04/03-2017/04/09','2017/04/10-2017/04/16']
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value'
	        }
	    ],
	    dataZoom : {
	        show : true,
	        realtime: true,
	        start: 75,
            end:100
	    },
	    series : [
	        {
	            name:'CPU',
	            type:'bar',
	            data:[117.0, 123.0, 125.0, 136.0, 135.0, 122.0, 132.0, 120.0, 126.0, 123.0,
	                  137.0, 163.0, 165.0, 166.0, 165.0, 182.0, 192.0, 190.0, 226.0, 223.0],
	            markPoint : {
	                data : [
	                    {type : 'max', name: '最大值'},
	                    {type : 'min', name: '最小值'}
	                ]
	            },
	            markLine : {
	                data : [
	                    {type : 'average', name: '平均值'}
	                ]
	            }
	        },
	        {
	            name:'内存',
	            type:'bar',
	            data:[249.0, 256.4, 258.7, 270.7, 275.6, 252.2, 288.7, 248.8, 276.0, 272.3,
	                  289.0, 346.8, 398.7, 350.7, 375.6, 400.2, 420.7, 418.8, 460.0, 472.3],
	            markPoint : {
	                data : [
	                    {type : 'max', name: '最大值'},
	                    {type : 'min', name: '最小值'}
	                ]
	            },
	            markLine : {
	                data : [
	                    {type : 'average', name : '平均值'}
	                ]
	            }
	        }
	    ]
	};

var optionMonth = {
	    title : {
	        text: '集群资源使用情况（月）'
	    },
	    tooltip : {
	        trigger: 'axis'
	    },
	    legend: {
	        data:['CPU','内存']
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            //mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType : {show: true, type: ['line', 'bar']},
	            restore : {show: true},
	            //saveAsImage : {show: true}
	        }
	    },
	    calculable : true,
	    xAxis : [
	        {
	            type : 'category',
	            data : ['2015/05','2015/06','2015/07','2015/08','2015/09','2015/10','2015/11','2015/12','2016/01','2016/02','2016/03','2016/04',
	                    '2016/05','2016/06','2016/07','2016/08','2016/09','2016/10','2016/11','2016/12','2017/01','2017/02','2017/03','2017/04']
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value'
	        }
	    ],
	    dataZoom : {
	        show : true,
	        realtime: true,
	        start: 75,
            end:100
	    },
	    series : [
	        {
	            name:'CPU',
	            type:'bar',
	            data:[112.0, 114.0, 117.0, 123.0, 125.0, 136.0, 135.0, 122.0, 132.0, 120.0, 126.0, 123.0,
	                  122.0, 124.0, 137.0, 163.0, 165.0, 166.0, 165.0, 182.0, 192.0, 190.0, 226.0, 223.0],
	            markPoint : {
	                data : [
	                    {type : 'max', name: '最大值'},
	                    {type : 'min', name: '最小值'}
	                ]
	            },
	            markLine : {
	                data : [
	                    {type : 'average', name: '平均值'}
	                ]
	            }
	        },
	        {
	            name:'内存',
	            type:'bar',
	            data:[242.6, 245.9, 249.0, 256.4, 258.7, 270.7, 275.6, 252.2, 288.7, 248.8, 276.0, 272.3,
	                  244.8, 265.0, 289.0, 346.8, 398.7, 350.7, 375.6, 400.2, 420.7, 418.8, 460.0, 472.3],
	            markPoint : {
	                data : [
	                    {type : 'max', name: '最大值'},
	                    {type : 'min', name: '最小值'}
	                ]
	            },
	            markLine : {
	                data : [
	                    {type : 'average', name : '平均值'}
	                ]
	            }
	        }
	    ]
	};

function changeRateType(obj){
	
    var optionType = $("#rateType").val();
    var optionData = "";
    var myChartDay="";
    if(optionType == "day"){
    	optionData = optionDay;
    }else if(optionType == "week"){
    	optionData = optionWeek;
    }else if(optionType == "month"){
    	optionData = optionMonth;
    }
    showEchart(optionData);
}

function showEchart(optionData){
	require.config({
        paths: {
            echarts: ""+ctx+"/plugins/echarts-2.2.7/build/dist"
        }
    });
    require(
        [
            'echarts',
            'echarts/chart/bar',
            'echarts/chart/line'
        ],
        function (ec) {
            option = optionData;
            var myChartDay = ec.init(document.getElementById('mainDay'));
            
            myChartDay.setOption(option);
        }
    ); 
	
}

