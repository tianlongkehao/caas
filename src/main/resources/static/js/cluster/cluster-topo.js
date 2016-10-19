$(document).ready(function(){
	//加载所有租户的集群拓扑图；
	 $.ajax({
		    async : false,
		    url:ctx+"/cluster/topo/data.do",
		    success:function(data){
		    	 var nodeDataTopo = new Array();
		    	 var linksDataTopo = new Array();
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
	             		}
	             	}
	             	
	             }
	             showTopo(nodeDataTopo,linksDataTopo);
		    }
	 });
	 
});

//admin查看单个租户的集群拓扑图
function searchUser() {
	var nameSpace = $("#search_user").val();
	$.ajax({
	    async : false,
	    url:ctx+"/cluster/topo/data.do?nameSpace="+nameSpace,
	    success:function(data){
	    	 var nodeDataTopo = new Array();
	    	 var linksDataTopo = new Array();
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
             
             
             var html = "";
             if (services.length == 0) {
            	 html = "<option value=''>无服务</option>";
             }
             for (var i = 0; i < services.length; i++) {
             	var serviceTopo = services[i];
             	var serviceName = serviceTopo.serviceName;
             	html += "<option value='"+serviceName+"'>"+serviceName+"</option>"
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
             		}
             	}
             	
             }
             $("#search_service").html(html);
             showTopo(nodeDataTopo,linksDataTopo);
	    }
 });
}

function searchService(){
	var serviceName = $("#search_service").val();
	var nameSpace = $("#search_user").val();
	$.ajax({
	    async : false,
	    url:ctx+"/cluster/topo/findPod.do?serviceName="+serviceName+"&nameSpace="+name,
	    success:function(data){
	    	 var nodeDataTopo = new Array();
	    	 var linksDataTopo = new Array();
             data = eval("(" + data + ")");
             var master = data.master;
             var podTopoList = data.podTopoList;
             //添加master节点
             var obj0 = {
             		category: '0',
             		name: master,
             		value: 12,
             		label: 'master(主节点)'
             };
             nodeDataTopo.push(obj0);
             //添加service节点
             var obj1 = {
            		category: '3',
              		name: serviceName,
              		value: 10,
              		label: serviceName
             }
             nodeDataTopo.push(obj1);
             //循环添加node节点和pod节点
             for (var i = 0 ; i < podTopoList.length; i++) {
            	 var objNode2 = {};
              	 objNode2.category = 2;
              	 objNode2.name = podTopoList[i].podName;
              	 objNode2.value = 8;
                 nodeDataTopo.push(objNode2);
                 
                 var objNode3 = {};
                 objNode3.category = 1;
              	 objNode3.name = podTopoList[i].nodeName;
              	 objNode3.value = 10;
                 nodeDataTopo.push(objNode3);
                 
                 var objLink1 ={};
                 objLink1.source = master;
                 objLink1.target = podTopoList[i].nodeName;
                 objLink1.weight = 2;
                 linksDataTopo.push(objLink1);
                 var objLink2 ={};
                 objLink2.source = podTopoList[i].nodeName;
                 objLink2.target = podTopoList[i].podName;
                 objLink2.weight = 3;
                 linksDataTopo.push(objLink2);
                 var objLink3 ={};
                 objLink3.source = podTopoList[i].podName;
                 objLink3.target = serviceName;
                 objLink3.weight = 4;
                 linksDataTopo.push(objLink3);
             }
             showTopo(nodeDataTopo,linksDataTopo)
	    }
	});
}

//画出拓扑关系图；
function showTopo(nodeDataTopo,linksDataTopo) {
	require.config({
	        paths: {
	            echarts: ""+ctx+"/plugins/echarts-2.2.7/build/dist"
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
	            	        data:['master','node','pod','service'],
	            	        selected: {
	            	            'pod' : false,
	            	            'service' : false
	            	        },
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
	            clusterTopo.setOption(option);
	        }
	    ); 
}
