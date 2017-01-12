$(document).ready(function(){
	
	if ($("#userType").val() == "root") {
		document.getElementById("search_user").value="";
		document.getElementById("search_service").value="";
	} else {
		document.getElementById("search_service").value="";
	}
	
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
	            		label: 'master(主节点)',
	            		symbol: 'image://../images/master.png' ,
                        symbolSize: [30, 25],
                        draggable: true,
                        itemStyle: {
                            normal: {
                                label: {
                                    position: 'bottom',
                                    textStyle: {
                                        color: 'black'
                                    }
                                }
                            }
                        }
	             };
	             nodeDataTopo.push(obj0);
	             
	             var nodeList = data.nodes;
	             for (var node in nodeList) {
	             	var objNode1 = {};
	             	objNode1.category = 1;
	             	objNode1.name = node;
	             	objNode1.value = 10;
	             	objNode1.symbol = 'image://../images/node.png';
                 	objNode1.symbolSize = [18, 28];
                 	objNode1.draggable = true;
                 	objNode1.itemStyle = {
                            normal: {
                                label: {
                                    position: 'bottom',
                                    textStyle: {
                                        color: 'black'
                                    }
                                }
                            }};
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
	                 	objNode2.symbol = 'image://../images/pod.png';
	                 	objNode2.symbolSize = [25, 26];
	                 	objNode2.draggable = true;
	                 	objNode2.itemStyle = {
	                            normal: {
	                                label: {
	                                    position: 'bottom',
	                                    textStyle: {
	                                        color: 'black'
	                                    }
	                                }
	                            }};
	                     nodeDataTopo.push(objNode2);
	                 	
	                     var objLink2 ={};
	                     objLink2.source = node;
	                     objLink2.target = podName;
	                     objLink2.weight = 3;
	                     linksDataTopo.push(objLink2);
	                     
	                     var objLink3 ={};
                         objLink3.source = podName;
                         objLink3.target = podTopoList[j].serviceName;
                         objLink3.weight = 4;
                         linksDataTopo.push(objLink3);
	                     
	                 }
	                 num = podNumber+1;
	             }
	             
	             for (var i = 0; i < services.length; i++) {
	             	var serviceTopo = services[i];
	             	var serviceName = serviceTopo.serviceName;
	             	var podNames = serviceTopo.podName;
	             	var objNode3 = {};
	             	objNode3.category = 3;
	             	objNode3.name = serviceName;
	             	objNode3.value = 10;
	             	objNode3.symbol = 'image://../images/service.png';
                 	objNode3.symbolSize = [26, 18];
                 	objNode3.draggable = true;
                 	objNode3.itemStyle = {
                            normal: {
                                label: {
                                    position: 'bottom',
                                    textStyle: {
                                        color: 'black'
                                    }
                                }
                            }};
	             	nodeDataTopo.push(objNode3);
	             }
	             var legend =  {
            	        x: 'left',
            	        data:[{name:'master',
            	               icon :'image://../images/master.png'
                        },{name:'node',
            	               icon :'image://../images/node.png'
                        },{name:'pod',
            	               icon :'image://../images/pod.png'
                        },{name:'service',
            	               icon :'image://../images/service.png'
                        }],
            	        selected: {
            	            'pod' : false,
            	            'service' : false
            	        },
	             	};
	             
	             showTopo(nodeDataTopo,linksDataTopo,legend);
		    }
	 });
	 
});

//admin查看单个租户的集群拓扑图
function searchUser() {
	var nameSpace = $("#search_user").val();
	if (nameSpace == null || nameSpace == "") {
		return;
	}
	
	//查询所有租户的集群拓扑图
	if (nameSpace == "all") {
		 location.reload();
		 document.getElementById("search_user").value="all";
		 return;
	}
	
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
            		label: 'master(主节点)',
            		symbol: 'image://../images/master.png' ,
                    symbolSize: [30, 25],
                    draggable: true,
                    itemStyle: {
                        normal: {
                            label: {
                                position: 'bottom',
                                textStyle: {
                                    color: 'black'
                                }
                            }
                        }
                    }
             };
             nodeDataTopo.push(obj0);
             
             var nodeList = data.nodes;
             for (var node in nodeList) {
             	var objNode1 = {};
             	objNode1.category = 1;
             	objNode1.name = node;
             	objNode1.value = 10;
             	objNode1.symbol = 'image://../images/node.png';
             	objNode1.symbolSize = [18, 28];
             	objNode1.draggable = true;
             	objNode1.itemStyle = {
                        normal: {
                            label: {
                                position: 'bottom',
                                textStyle: {
                                    color: 'black'
                                }
                            }
                        }};
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
                 	objNode2.symbol = 'image://../images/pod.png';
                 	objNode2.symbolSize = [25, 26];
                 	objNode2.draggable = true;
                 	objNode2.itemStyle = {
                            normal: {
                                label: {
                                    position: 'bottom',
                                    textStyle: {
                                        color: 'black'
                                    }
                                }
                            }};
                    nodeDataTopo.push(objNode2);
                 	
                     var objLink2 ={};
                     objLink2.source = node;
                     objLink2.target = podName;
                     objLink2.weight = 3;
                     linksDataTopo.push(objLink2);
                     
                     var objLink3 ={};
                     objLink3.source = podName;
                     objLink3.target =podTopoList[j].serviceName;
                     objLink3.weight = 4;
                     linksDataTopo.push(objLink3);
                     
                 }
                 num = podNumber+1;
             }
             
             
             var html = "";
             if (services.length == 0) {
            	 html = "<option value=''>无服务</option>";
             }else {
            	 html = "<option value=''>-----请选择-----</option><option value='all'>All</option>";
             }
             for (var i = 0; i < services.length; i++) {
             	var serviceTopo = services[i];
             	var serviceName = serviceTopo.serviceName;
             	html += "<option value='"+serviceName+"'>"+serviceName+"</option>"
             	var objNode3 = {};
             	objNode3.category = 3;
             	objNode3.name = serviceName;
             	objNode3.value = 10;
             	objNode3.symbol = 'image://../images/service.png';
             	objNode3.symbolSize = [26, 18];
             	objNode3.draggable = true;
             	objNode3.itemStyle = {
                        normal: {
                            label: {
                                position: 'bottom',
                                textStyle: {
                                    color: 'black'
                                }
                            }
                        }};
             	nodeDataTopo.push(objNode3);
//             	var podNames = serviceTopo.podName;
//             	if (podNames != null && serviceName != null) {
//             		for (var j = 0 ; j <podNames.length; j++) {
//             			var podName = podNames[j];
//                         
//             		}
//             	}
             	
             }
             $("#search_service").html(html);
             var legend =  {
            	        x: 'left',
            	        data:[{name:'master',
		         	               icon :'image://../images/master.png'
		                     },{name:'node',
		         	               icon :'image://../images/node.png'
		                     },{name:'pod',
		         	               icon :'image://../images/pod.png'
		                     },{name:'service',
		         	               icon :'image://../images/service.png'
		                     }],
            	        selected: {
            	            'service' : false
            	        },
            	    };
             
             showTopo(nodeDataTopo,linksDataTopo,legend);
	    }
 });
}

//查询某一个服务的pod和node；
function searchService(){
	var serviceName = $("#search_service").val();
	var nameSpace = $("#search_user").val();
	if (nameSpace == null ) {
		nameSpace = "";
	}
	//当前登录账户是租户时，查询所有服务的拓扑
	if (serviceName == 'all' && nameSpace == "") {
		location.reload();
		return;
	}
	//当前登录账户是admin，查询选中的租户的所有服务拓扑图；
	if (serviceName == 'all' && nameSpace != null) {
		searchUser();
		return;
	}
	$.ajax({
	    async : false,
	    url:ctx+"/cluster/topo/findPod.do?serviceName="+serviceName+"&nameSpace="+nameSpace,
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
             		label: 'master(主节点)',
             		symbol: 'image://../images/master.png' ,
                    symbolSize: [30, 25],
                    draggable: true,
                    itemStyle: {
                        normal: {
                            label: {
                                position: 'bottom',
                                textStyle: {
                                    color: 'black'
                                }
                            }
                        }
                    }
             };
             nodeDataTopo.push(obj0);
             //添加service节点
             var obj1 = {
            		category: '3',
              		name: serviceName,
              		value: 10,
              		label: serviceName,
              		symbol : 'image://../images/node.png',
          			symbolSize : [18, 28],
          			draggable : true,
          	        itemStyle : {
                     normal: {
                         label: {
                             position: 'bottom',
                             textStyle: {
                                 color: 'black'
                             }
                         }
                     }}
             }
             nodeDataTopo.push(obj1);
             //循环添加node节点和pod节点
             for (var i = 0 ; i < podTopoList.length; i++) {
            	 var objNode2 = {};
              	 objNode2.category = 2;
              	 objNode2.name = podTopoList[i].podName;
              	 objNode2.value = 8;
              	objNode2.symbol = 'image://../images/pod.png';
             	objNode2.symbolSize = [25, 26];
             	objNode2.draggable = true;
             	objNode2.itemStyle = {
                        normal: {
                            label: {
                                position: 'bottom',
                                textStyle: {
                                    color: 'black'
                                }
                            }
                        }};
                 nodeDataTopo.push(objNode2);
                 
                 var objNode3 = {};
                 objNode3.category = 1;
              	 objNode3.name = podTopoList[i].nodeName;
              	 objNode3.value = 10;
              	objNode3.symbol = 'image://../images/service.png';
             	objNode3.symbolSize = [26, 18];
             	objNode3.draggable = true;
             	objNode3.itemStyle = {
                        normal: {
                            label: {
                                position: 'bottom',
                                textStyle: {
                                    color: 'black'
                                }
                            }
                        }};
                 nodeDataTopo.push(objNode3);
                 
                 //匹配连线
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
             
             $("#clusterTopo").css("height","500px");
             var legend =  {
            	        x: 'left',
            	        data:[{name:'master',
	         	               icon :'image://../images/master.png'
	                     },{name:'node',
	         	               icon :'image://../images/node.png'
	                     },{name:'pod',
	         	               icon :'image://../images/pod.png'
	                     },{name:'service',
	         	               icon :'image://../images/service.png'
	                     }],
            	        selected: {
            	            'master' : false
            	        },
            	    };
             
             showTopo(nodeDataTopo,linksDataTopo,legend)
	    }
	});
}

//画出拓扑关系图；
function showTopo(nodeDataTopo,linksDataTopo,legend) {
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
	            	    /*toolbox: {
	            	        show : true,
	            	        feature : {
	            	            restore : {show: true},
	            	        }
	            	    },*/
	            	    legend: legend,
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
