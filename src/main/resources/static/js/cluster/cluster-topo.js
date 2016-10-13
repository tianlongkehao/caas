$(function () {
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
	                nodeDataTopo += "{category:0, name: '"+master+"', value : 12, label: 'master\n（主节点）'},";
	                var nodeList = data.nodes;
	                for (var node in nodeList) {
	                	nodeDataTopo += "{category:1, name: '"+ node +"' ,value : 10},";
	                	var podNumber = num;
	                	links += "{source : '"+ master +"', target : '"+ node +"', weight : 2},";
	                	var podTopoList = nodeList[node];
	                	var podLength = podTopoList.length;
	                    for (var j = 0; j < podLength; j++) {
	                    	
	                    	var podName = podTopoList[j].podName;
	                    	nodeDataTopo += "{category:2, name: '"+ podName +"' ,value : 8'},";
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
	                			nodeDataTopo += "{category:3, name: '"+ serviceName +"' ,value : 10},";
	                            links += "{source : '"+ podName +"', target : '"+ serviceName +"', weight : 4},"; 
	                		}
	                	}
	                	
	                }
	                //alert(nodeDataTopo);
	                //alert(links);
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
	        	            	            nodes:[],
	        	            	            links : []
	        	            	        }
	        	            	    ]
	        	            	};
	        	           	option.series[0].nodes.push(nodeDataTopo);
	        	           	option.series[0].links.push(links);
	        	            clusterTopo.setOption(option);
	        	        }
	        	    ); 
	            }
	     });         
	   
	});
    
});