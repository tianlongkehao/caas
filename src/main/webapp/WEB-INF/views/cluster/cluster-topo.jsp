<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/cluster.css"/>
    <script type="text/javascript" src="<%=path %>/js/cluster/cluster.js"></script>
    <script type="text/javascript" src="<%=path %>/plugins/echarts/src/echarts.js"></script>
	<script type="text/javascript" src="<%=path %>/js/plugins/jtopo-0.4.8-min.js"></script>

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
                <div class="caption clearfix" style="padding-bottom: 0px">
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
                                <option value="2">天津</option>
                                <option value="3">上海</option>
                                <option value="4">河北</option>
                                <option value="5">河南</option>
                                <option value="6">山西</option>
                                <option value="7">内蒙古</option>
                                <option value="8">辽宁</option>
                                <option value="9">吉林</option>
                                <option value="10">黑龙江</option>
                                <option value="11">江苏</option>
                                <option value="12">浙江</option>
                                <option value="13">安徽</option>
                                <option value="14">福建</option>
                                <option value="15">江西</option>
                                <option value="16">山东</option>
                                <option value="17">湖南</option>
                                <option value="18">湖北</option>
                                <option value="19">广东</option>
                                <option value="20">广西</option>
                                <option value="21">海南</option>
                                <option value="22">重庆</option>
                                <option value="23">四川</option>
                                <option value="24">贵州</option>
                                <option value="25">云南</option>
                                <option value="26">西藏</option>
                                <option value="27">陕西</option>
                                <option value="28">甘肃</option>
                                <option value="29">青海</option>
                                <option value="30">宁夏</option>
                                <option value="31">新疆</option>
                            </select>
                        </div>

                    </form>
                </div>
                <div>
                    <div id="clusterResource_wrap" class="tab_wrap">
                        <div class="detail-info">
                        	<div class="info-list" id="resourceImg">
                        		<div id ="can" style="width:100%; border:1px dashed #cccccc">
                        			<canvas id="canvas" width="1248px" height="450px"></canvas>
                        		</div>		
                            </div>

                        </div>

                    </div>
                </div>
                <input type="hidden" id="master" name="master" value="${master }"></input>
                <input type="hidden" id="nodes" name="nodes" value="${nodes }"></input>
                <input type="hidden" id="services" name="service" value="${services }"></input>
                <p id = "test">buttom</p>
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
	 
	 
	 $("#test").click(function () {
	        var master = $("#master").val();
	        var nodes = $("#nodes").val();
	        var services = $("#services").val();
	        
	        
	        alert(master);
	        alert(nodes);
	        alert(services);
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
/*	
$(document).ready(function(){
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
	var container = new JTopo.Container();
	container.textPosition = 'Middle_Center';
	container.fontColor = '100,255,0';
	container.font = '18pt 微软雅黑';
	//container.borderColor = '255,0,0';
	container.fillColor = '204,204,255';
	container.borderRadius = 2; // 圆角
	container.alpha = 1;
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
	newLink(s2, container, '', 3); 
	//隐藏container

	var container2 = "";
	var scene2 = "";
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
			
			scene2 = new JTopo.Scene(stage);
			//scene2.translateX = 100;
			//scene2.translateY = 100;
			//scene2.dragble = true;
			//stage.add(scene2);
			//scene.add(scene2);
			scene2.add(container2);
			for(var i=0; i<5; i++){
				var node2 = new JTopo.Node("pod-A_"+i);	
				node2.textPosition = "Middle_Center";
				node2.setLocation(50*i, 0);
				node2.fillColor = '60,255,108';
				scene.add(node2);
				container2.add(node2);
			}
			  for(var i=0; i<8; i++){
				var node = new JTopo.Node("pod-B_"+i);	
				node.textPosition = "Middle_Center";
				node.setLocation(50*i, 60);
				node.fillColor = '68,86,247';
				scene2.add(node);
				container2.add(node);
			} 
			stoc2 = newLink(s1, container2, '', 3);
			scene.doLayout(JTopo.layout.TreeLayout('right', 100, 120));
		}else{
			scene2.hide(container2);
			scene.remove(stoc2);
			container2 = "";
		}
	});
	
	scene.doLayout(JTopo.layout.TreeLayout('right', 100, 120));
	
   
});*/

</script>

</body>


</html>
