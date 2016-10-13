<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/cluster.css"/>
    <script type="text/javascript" src="<%=path %>/js/cluster/cluster.js"></script>
    <script type="text/javascript" src="<%=path %>/plugins/echarts-2.2.7/build/dist/echarts.js"></script>
    <%-- <script type="text/javascript" src="<%=path %>/plugins/echarts-2.2.7/doc/asset/js/esl/esl.js"></script> --%>
    <%-- <script type="text/javascript" src="<%=path %>/plugins/echarts/src/echarts.js"></script> --%>
	<%-- <script type="text/javascript" src="<%=path %>/js/plugins/jtopo-0.4.8-min.js"></script> --%>

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
                                class="ic_left">集群拓扑</span></span></a>
                    </div>
                </aside>
                <div class="caption clearfix hide" style="padding-bottom: 0px">
                    <ul class="toolbox clearfix">
                        <li><a id="updateCluster" style="cursor:pointer"><i
                                class="fa fa-repeat"></i></a></li>
                    </ul>
                    <form id="search_form" class="form-inline" action="<%=path %>/user/searchByCondition" method="post">
                        <div class="searchFun" style="float: left; text-align: center; margin: 0px 10px; float: right"
                             align="right">
                            <label style="line-height: 35px">租户:</label>
                            <select name="search_time" id="search_time" onchange="searchTime()"
                                    style="height: 30px;display: inline; width: 140px; border-radius: 5px; ">
                                <option value="0">ALL</option>
                                <option value="1">北京</option>
                                
                            </select>
                        </div>

                    </form>
                </div>
                <div>
                    <div id="clusterResource_wrap" class="tab_wrap">
                        <div class="detail-info">
                        	<div class="info-list" id="resourceImg">
                        		<div id="clusterTopo" style="height:500px;"></div>
                        		<!-- <div id ="can" style="width:100%; border:1px dashed #cccccc">
                        			<canvas id="canvas" width="1248px" height="450px"></canvas>
                        		</div> -->		
                            </div>

                        </div>

                    </div>
                </div>
            </div>
        </div>
    </article>
</div>

<script type="text/javascript">
 var nodeDataTopo = new Array();
 var linksDataTopo = new Array();
  $(document).ready(function(){
	 
	 $.ajax({
		    async : false,
            url:ctx+"/cluster/topo/data.do",
            success:function(data){
                debugger
                data = eval("(" + data + ")");
                var services = data.services;
                var master = data.master;
                var num = 0;
                var obj0 = {
               		category: '0',
               		name: master,
               		value: 12,
               		label: 'master(主节点)'
                };
                nodeDataTopo.push(obj0);
                
                var nodeList = data.nodes;
                for (var node in nodeList) {
                	var objNode1 = {};
                	objNode1.category = 1;
                	objNode1.name = node;
                	objNode1.value = 10;
                	nodeDataTopo.push(objNode1);
                	
                	var podNumber = num;
                	var objLink1 ={};
                	objLink1.source = master;
                	objLink1.target = node;
                	objLink1.weight = 2;
                	linksDataTopo.push(objLink1);
                	
                	var podTopoList = nodeList[node];
                	var podLength = podTopoList.length;
                    for (var j = 0; j < podLength; j++) {
                    	
                    	var podName = podTopoList[j].podName;
                    	
                    	var objNode2 = {};
                    	objNode2.category = 2;
                    	objNode2.name = podName;
                    	objNode2.value = 8;
                        nodeDataTopo.push(objNode2);
                    	
                        var objLink2 ={};
                        objLink2.source = node;
                        objLink2.target = podName;
                        objLink2.weight = 3;
                        linksDataTopo.push(objLink2);
                        
                    }
                    num = podNumber+1;
                }
                
                for (var i = 0; i < services.length; i++) {
                	var serviceTopo = services[i];
                	var serviceName = serviceTopo.serviceName;
                	var podNames = serviceTopo.podName;
                	if (podNames != null && serviceName != null) {
                		for (var j = 0 ; j <podNames.length; j++) {
                			var podName = podNames[j];
                			
                			var objNode3 = {};
                			objNode3.category = 3;
                			objNode3.name = serviceName;
                			objNode3.value = 10;
                            nodeDataTopo.push(objNode3);
                            
                            var objLink3 ={};
                            objLink3.source = podName;
                            objLink3.target = serviceName;
                            objLink3.weight = 4;
                            linksDataTopo.push(objLink3);
                			//nodeDataTopo.push("{category:3, name: '"+ serviceName +"' ,value : 10}");
                			//linksDataTopo.push("{source : '"+ podName +"', target : '"+ serviceName +"', weight : 4}"); 
                		}
                	}
                	
                }
                require.config({
        	        paths: {
        	            echarts: '<%=path %>/plugins/echarts-2.2.7/build/dist'
        	        }
        	    });
        	    require(
        	        [
        	            'echarts',
        	            'echarts/chart/force'
        	        ],
        	        function (ec) {
        	            var clusterTopo = ec.init(document.getElementById('clusterTopo'));
        	            console.log(nodeDataTopo);
        	            var nodeData = "[" +  nodeDataTopo +"]";
        	            var linkData =  "[" + linksDataTopo + "]";
        	            var option = {
        	            	    title : {
        	            	        text: '集群拓扑关系图',
        	            	        x:'right',
        	            	        y:'bottom'
        	            	    },
        	            	    tooltip : {
        	            	        trigger: 'item',
        	            	        formatter: '{a} : {b}'
        	            	    },
        	            	    toolbox: {
        	            	        show : true,
        	            	        feature : {
        	            	            restore : {show: true},
        	            	        }
        	            	    },
        	            	    legend: {
        	            	        x: 'left',
        	            	        data:['master','node','pod','service']
        	            	    }, 
        	            	    series : [
        	            	        {
        	            	            type:'force',
        	            	            name : "集群拓扑关系",
        	            	            ribbonType: false,
        	            	            categories : [
        	            	                {
        	            	                    name: 'master'
        	            	                },
        	            	                {
        	            	                    name:'node'
        	            	                },
        	            	                {
        	            	                    name:'pod'
        	            	                },
        	            	                {
        	            	                    name:'service'
        	            	                }
        	            	            ],
        	            	            itemStyle: {
        	            	                normal: {
        	            	                    label: {
        	            	                        show: true,
        	            	                        textStyle: {
        	            	                            color: '#333'
        	            	                        }
        	            	                    },
        	            	                    nodeStyle : {
        	            	                        brushType : 'both',
        	            	                        borderColor : 'rgba(255,215,0,0.4)',
        	            	                        borderWidth : 1
        	            	                    },
        	            	                    linkStyle: {
        	            	                        type: 'curve'
        	            	                    }
        	            	                },
        	            	                emphasis: {
        	            	                    label: {
        	            	                        show: false
        	            	                    },
        	            	                    nodeStyle : {
        	            	                        //r: 30
        	            	                    },
        	            	                    linkStyle : {}
        	            	                }
        	            	            },
        	            	            useWorker: false,
        	            	            minRadius : 15,
        	            	            maxRadius : 25,
        	            	            gravity: 1.1,
        	            	            scaling: 1.1,
        	            	            roam: 'move',
        	            	            nodes: nodeDataTopo,
        	            	            links :linksDataTopo
        	            	        }
        	            	    ]
        	            	};
        	           /* 	option.series[0].nodes = "[" +  nodeDataTopo +"]";
        	           	option.series[0].links = "[" + linksDataTopo + "]"; */
        	            clusterTopo.setOption(option);
        	        }
        	    ); 
            }
     });
	 
	 
	                   
   
});

</script>

</body>


</html>
