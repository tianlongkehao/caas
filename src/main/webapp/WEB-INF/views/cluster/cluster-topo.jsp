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
 var nodeDataTopo = "";
 var links = "";
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
                nodeDataTopo += "{category:"+ num++ +", name: '"+master+"', value : 12, label: 'master\n（主节点）'},";
                var nodeList = data.nodes;
                for (var node in nodeList) {
                	nodeDataTopo += "{category:"+ num++ +", name: '"+ node +"' ,value : 10},";
                	var podNumber = num;
                	links += "{source : '"+ master +"', target : '"+ node +"', weight : 2},";
                	var podTopoList = nodeList[node];
                	var podLength = podTopoList.length;
                    for (var j = 0; j < podLength; j++) {
                    	
                    	var podName = podTopoList[j].podName;
                    	nodeDataTopo += "{category:"+ podNumber +", name: '"+ podName +"' ,value : 8'},";
                        links += "{source : '"+ node +"', target : '"+ podName +"', weight : 3},";
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
                			nodeDataTopo += "{category:"+ num++ +", name: '"+ serviceName +"' ,value : 10},";
                            links += "{source : '"+ podName +"', target : '"+ serviceName +"', weight : 4},"; 
                		}
                	}
                	
                }
                alert(nodeDataTopo);
                alert(links);
            }
     });
	 
    var canvas = document.getElementById('canvas');
    var stage = new JTopo.Stage(canvas);
    //显示工具栏
    //showJTopoToobar(stage);
    var scene = new JTopo.Scene(stage);
    //scene.background = '/images/container_bg.png';
    //master和slave的节点
    function node(text, img){
		var node = new JTopo.Node(text);
		node.fontColor = '0, 0, 255';
		node.setImage('/images/' + img, true);	
		//node.setSize(150,150); //方法不生效
		node.textPosition = 'Bottom_Center';
		scene.add(node);
		return node;
	}
    
	//master与slave的连接线
	function linkNode(nodeA, nodeZ, f){
		var link;
		if(f){
			link = new JTopo.FoldLink(nodeA, nodeZ);
		}else{
			link = new JTopo.Link(nodeA, nodeZ);
		}
		link.direction = 'vertical';
		scene.add(link);
		return link;
	}
	
	//slave与pod的连接虚线
	function newLink(nodeA, nodeZ, text,dashedPattern){
		var link = new JTopo.Link(nodeA, nodeZ, text);		
		link.lineWidth = 3; // 线宽
		link.bundleOffset = 60; // 折线拐角处的长度
		link.bundleGap = 20; // 线条之间的间隔
		link.textOffsetY = 3; // 文本偏移量（向下3个像素）
		//link.strokeColor = JTopo.util.randomColor(); // 线条颜色随机
		link.dashedPattern = dashedPattern; 
		link.zIndex = -2;
		scene.add(link);
		return link;
	}

	var master = node('master 10.0.0.201', 'server.png');
	var transfer = node('',  'transfer.png');
	var s1 = node('slave 10.0.0.202', 'server.png');
	var s2 = node('slave 10.0.0.203', 'server.png');
	var s3 = node('slave 10.0.0.204', 'server.png');
	linkNode(master, transfer, true);
	linkNode(transfer, s1, true);
	linkNode(transfer, s2, true);
	linkNode(transfer, s3, true);
 
	//容器
	// 不指定布局的时候，容器的布局为自动(容器边界随元素变化）
	var container = "";
	var stoc ="";
	s2.click(function(event){
		if(container == ""){
			container = new JTopo.Container();
			container.textPosition = 'Middle_Center';
			container.fontColor = '100,255,0';
			container.font = '18pt 微软雅黑';
			container.fillColor = '204,204,255';
			container.borderRadius = 2; // 圆角			
			container.alpha = 1;//透明度
			scene.add(container);
			for(var i=0; i<5; i++){
				var node = new JTopo.Node("pod-A_"+i);	
				node.textPosition = "Middle_Center";
				node.setLocation(50*i, 0);
				node.fillColor = '60,255,108';
				scene.add(node);
				container.add(node);
			}
			for(var i=0; i<8; i++){
				var node = new JTopo.Node("pod-B_"+i);	
				node.textPosition = "Middle_Center";
				node.setLocation(50*i, 60);
				node.fillColor = '68,86,247';
				scene.add(node);
				container.add(node);
			}
			stoc = newLink(s2, container, '', 3);
			scene.doLayout(JTopo.layout.TreeLayout('right', 100, 120));
		}else{
			scene.remove(stoc);
			for(var i = 0; i < container.childs.length; i++){
				scene.remove(container.childs[i]);
			}
			scene.remove(container);
			container = "";
		}
	});
	//container2

	var container2 = "";
	
	var stoc2 ="";
	s1.click(function(event){
		if(container2 == ""){
			container2 = new JTopo.Container();
			container2.textPosition = 'Middle_Center';
			container2.fontColor = '100,255,0';
			container2.font = '18pt 微软雅黑';
			//container.borderColor = '255,0,0';
			container2.fillColor = '204,204,255';
			container2.borderRadius = 2; // 圆角			
			container2.alpha = 1;//透明度
			//scene2 = new JTopo.Scene(stage);
			//stage.add(scene2);
			scene.add(container2);
			for(var i=0; i<5; i++){
				var node = new JTopo.Node("pod-A_"+i);	
				node.textPosition = "Middle_Center";
				node.setLocation(50*i, 0);
				node.fillColor = '60,255,108';
				scene.add(node);
				container2.add(node);
			}
			for(var i=0; i<8; i++){
				var node = new JTopo.Node("pod-B_"+i);	
				node.textPosition = "Middle_Center";
				node.setLocation(50*i, 60);
				node.fillColor = '68,86,247';
				scene.add(node);
				container2.add(node);
			}
			stoc2 = newLink(s1, container2, '', 3);
			scene.doLayout(JTopo.layout.TreeLayout('right', 100, 120));
		}else{
			scene.remove(stoc2);
			for(var i = 0; i < container2.childs.length; i++){
				scene.remove(container2.childs[i]);
			}
			scene.remove(container2);
			container2 = "";
		}
	});
	
	scene.doLayout(JTopo.layout.TreeLayout('right', 100, 120));
	
   
}); 
	
$(document).ready(function(){
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
            	        data:['master','node','pod1','pod2','service1']
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
            	                    name:'pod1'
            	                },
            	                {
            	                    name:'pod2'
            	                },
            	                {
            	                    name:'service1'
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
            	            nodes:[
            	                {category:0, name: 'master', value : 12, label: 'master\n（主节点）'},
            	                {category:1, name: 'node1' ,value : 10},
            	                {category:1, name: 'node2' ,value : 10},
            	                {category:1, name: 'node3' ,value : 10},
            	                {category:1, name: 'node4' ,value : 10},
            	                {category:2, name: 'pod1-1' ,value : 8},
            	                {category:2, name: 'pod1-2' ,value : 8},
            	                {category:2, name: 'pod1-3' ,value : 8},
            	                {category:3, name: 'pod2-1',value : 8},
            	                {category:3, name: 'pod2-2',value : 8},
            	                {category:3, name: 'pod2-3',value : 8},
            	                {category:4, name: 'service1',value : 10,serviceName:'aaa'},
            	                {category:4, name: 'service2',value : 10},
            	            ],
            	            links : [
            	                {source : 'master', target : 'node1', weight : 2},
            	                {source : 'master', target : 'node2', weight : 2},
            	                {source : 'master', target : 'node3', weight : 2},
            	                {source : 'master', target : 'node4', weight : 2},
            	                {source : 'node1', target : 'pod1-1', weight : 3},
            	                {source : 'node1', target : 'pod1-2', weight : 3},
            	                {source : 'node1', target : 'pod1-3', weight : 3},
            	                {source : 'node2', target : 'pod2-1', weight : 3},
            	                {source : 'node2', target : 'pod2-2', weight : 3},
            	                {source : 'node2', target : 'pod2-3', weight : 3},
            	                {source : 'pod1-1', target : 'service1', weight : 4},
            	                {source : 'pod1-2', target : 'service1', weight : 4},
            	                {source : 'pod1-3', target : 'service2', weight : 4}
            	            ]
            	        }
            	    ]
            	};
           
            clusterTopo.setOption(option);
        }
    ); 
                   
})

</script>

</body>


</html>
