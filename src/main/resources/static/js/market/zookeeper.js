/**
 * Creator：daien
 * Time：2017年7月6日15:18:37
 */
$(function(){
	$(".advanced").hide();

	$("#advanced").click(function(){
		$(".advanced").toggle();
	});

	$(".advanced2").hide();

	$("#advanced2").click(function(){
		$(".advanced2").toggle();
	});
})

/**
 * 启动
 * @param obj
 */
function start(id){
	$.ajax({
 		url : ctx + "/zookeeper/start.do",
 		type: "POST",
 		data: {"id":id},
 		success: function(data) {
 			var data = eval("("+data+")");
         	if (data.status == 200) {
         		layer.msg("启动成功！",{icon: 6});
         		window.location.reload();
         	}
         	else {
         		layer.alert("启动失败！");
         	}
 		}
 	});
}

/**
 * 停止
 * @param obj
 */
function stop(id){
	$.ajax({
 		url : ctx + "/zookeeper/stop.do",
 		type: "POST",
 		data: {"id":id},
 		success: function(data) {
 			var data = eval("("+data+")");
         	if (data.status == 200) {
         		layer.msg("停止成功！",{icon: 6});
         		window.location.reload();
         	}
         	else {
         		layer.alert("新增失败！请检查服务器连接");
         	}
 		}
 	});
}

/**
 * 删除
 * @param obj
 */
function remove(id){
	$.ajax({
 		url : ctx + "/zookeeper/delete.do",
 		type: "POST",
 		data: {"id":id},
 		success: function(data) {
 			var data = eval("("+data+")");
         	if (data.status == 200) {
         		layer.msg("删除成功！",{icon: 6});
         		window.location.reload();
         	}
         	else {
         		layer.alert("新增失败！请检查服务器连接");
         	}
 		}
 	});
}

//获取数据
function detail(id){
	$.ajax({
 		url : ctx + "/zookeeper/detail.do",
 		type: "POST",
 		data: {"id":id},
 		success: function(data) {
 			var data = eval("("+data+")");
         	if (data.status == 200) {
               var tensorflow = data.tensorflow;
               $("#name2").html(tensorflow.name);
               $("#cpu2").html(tensorflow.cpu);
               $("#memory2").html(tensorflow.memory);
               $("#image2").html(tensorflow.image);
               $("#storage2").html(tensorflow.rbd);
               $("#mark2").val(tensorflow.detail);

               layer.open({
            	   type: 1,
            	   area: ['500px', '400px'],
            	   shadeClose: true,
            	   content: $("#tensorflowDetail")
            	   });
         	}
         	else {
         		layer.alert("新增失败！请检查服务器连接");
         	}
 		}
 	});
}

/**
 * 新增tensorflow
 */
function add(){
	layer.open({
		type:1,
		title: '创建Zookeeper',
		area: ['600px', '500px'],
		content: $("#zookeeperAdd"),
		btn: ['确定', '取消'],
		yes: function(index, layero){
           if(verify()){
        	   var zookeeperName = $("#zookeeperName").val().trim();
        	   $.ajax({
        		   url : ctx + "/zookeeper/exist.do",
	         		type: "POST",
	         		data: {"name":zookeeperName},
	         		success: function(data) {
	         			var data = eval("("+data+")");
	                 	if (data.status == 200) {
                            if(data.exist == 0){
                            	layer.close(index);

                            	var imageId = $("#image").val();
                            	var image = $('#image option:selected').text();
                            	var detail = $("#mark").val();
                            	var cpumemeory =$("input[name='cpu×memeory']:checked").val();
                            	 var cpu;
                            	   var mem;
                            	   if(Number(cpumemeory)==1){
                            		   cpu = 1;
                            		   mem=2;
                            	   }else if(Number(cpumemeory)==2){
                            		   cpu = 2;
                            		   mem=4;
                            	   }else{
                            		   cpu = 2;
                            		   mem=8;
                            	   }
                            	   var storage =$("input[name='storage']:checked").val();
                            	   var timeout_end = $("#timeout_end").val();
                            	   var timeout_start = $("#timeout_start").val();
                            	   var timeout_syn = $("#timeout_syn").val();
                            	   var max_node = $("#max_node").val();
                            	   var max_request = $("#max_request").val();

                            	$.ajax({
                	         		url : ctx + "/zookeeper/add.do",
                	         		type: "POST",
                	         		data: {"name":zookeeperName,"imageId":imageId,"image":image,
                	         			"cpu":cpu,"memory":mem,"detail":detail,"storage":storage,
                	         			"timeoutdeadline":timeout_end,"starttimeout":timeout_start,
                	         			"syntimeout":timeout_syn,"maxnode":max_node,"maxrequest":max_request
                	         		    },
                	         		success: function(data) {
                	         			var data = eval("("+data+")");
                	                 	if (data.status == 200) {
                	                 		layer.msg("新增成功！",{icon: 6});
                	                 		window.location.reload();
                	                 	}
                	                 	else {
                	                 		layer.alert("新增失败！请检查服务器连接");
                	                 	}
                	         		}
                	         	});
                            }else{
                            	layer.alert("zookeeper名称已经存在，请更换名称!");
                            }
	                 	}
	                 	else {
	                 		layer.alert("请检查服务器连接!");
	                 	}
	         		}
        	   });

           }
		}

     });
}

function verify(){
   var zookeeperName = $("#zookeeperName").val().trim();
   if(zookeeperName==''){
	   layer.tips('名称不能为空!','#zookeeperName',{tips: [1, '#3595CC']});
		$('#zookeeperName').focus();
		return false;
   }
   var reg=/^[a-z]([-a-z0-9]*[a-z0-9])?$/;
   if(!reg.test(zookeeperName)){
	    layer.tips('名称只能由小写字母、数字及横线组成，且首字母不能为数字及横线。','#zookeeperName',{tips: [1, '#3595CC'],time: 3000});
	    $('#zookeeperName').focus();
	    return false;
   }

   var image =$("#image").val();
   if(image == 0){
	   layer.tips('请选择镜像!','#image',{tips: [1, '#3595CC']});
		$('#image').focus();
		return false;
   }

   var cpumemeory =$("input[name='cpu×memeory']:checked").val();
   var restCpu = $("#restCpu").html();
   var restMem =  $("#restMem").html();
   var cpu;
   var mem;
   if(Number(cpumemeory)==1){
	   cpu = 1;
	   mem=2;
   }else if(Number(cpumemeory)==2){
	   cpu = 2;
	   mem=4;
   }else{
	   cpu = 2;
	   mem=8;
   }

   if(restCpu!=''&&cpu*3>restCpu){
	   layer.tips('剩余cpu不足，请申请更多资源!','#restCpu',{tips: [1, '#3595CC']});
	   return false;
   }

   if(restMem!=''&&mem*3>restMem){
	   layer.tips('剩余内存不足，请申请更多资源!','#restMem',{tips: [1, '#3595CC']});
	   return false;
   }

   var storage =$("input[name='storage']:checked").val();
   var restStorage = $("#restStorage").html();
   storage = Number(storage);
   if(restStorage!=''&&storage*3>restStorage){
	   layer.tips('剩余存储不足，请申请更多资源!','#restStorage',{tips: [1, '#3595CC']});
	   return false;
   }

   var timeout_end = $("#timeout_end").val().trim();
   if(timeout_end==''){
	   layer.tips('超时时间下限不能为空!','#timeout_end',{tips: [1, '#3595CC']});
		$('#timeout_end').focus();
		return false;
   }

   var timeout_start = $("#timeout_start").val().trim();
   if(timeout_start==''){
	   layer.tips('初始超时限制不能为空!','#timeout_start',{tips: [1, '#3595CC']});
		$('#timeout_start').focus();
		return false;
   }

   var timeout_syn = $("#timeout_syn").val().trim();
   if(timeout_syn==''){
	   layer.tips('同步超时不能为空!','#timeout_syn',{tips: [1, '#3595CC']});
		$('#timeout_syn').focus();
		return false;
   }

   var max_node = $("#max_node").val().trim();
   if(max_node==''){
	   layer.tips('节点最大并发数不能为空!','#max_node',{tips: [1, '#3595CC']});
		$('#max_node').focus();
		return false;
   }

   var max_request = $("#max_request").val().trim();
   if(max_request==''){
	   layer.tips('待处理请求最大值不能为空!','#max_request',{tips: [1, '#3595CC']});
		$('#max_request').focus();
		return false;
   }

   return true;
}

function update(id){
	$.ajax({
 		url : ctx + "/zookeeper/detail.do",
 		type: "POST",
 		data: {"id":id},
 		success: function(data) {
 			var data = eval("("+data+")");
         	if (data.status == 200) {
               var zookeeper = data.zookeeper;
               $("#zookeeperName2").html(zookeeper.name);
               $("#image2").html(zookeeper.image);
               $("#storage2").html(zookeeper.storage);
               $("#mark2").val(zookeeper.detail);
               if(zookeeper.cpu == 1&&zookeeper.memory == 2){
            	   $("input[name='cpu×memeory2']:eq(0)").prop('checked',true);
               }else if(zookeeper.cpu == 2&&zookeeper.memory == 4){
            	   $("input[name='cpu×memeory2']:eq(1)").prop('checked',true);
               }else{
            	   $("input[name='cpu×memeory2']:eq(2)").prop('checked',true);
               }

               $("#lastCpu").val(zookeeper.cpu);
        	   $("#lastMem").val(zookeeper.memory);
        	   $("#zookeeperId2").val(zookeeper.id);


               $("#timeout_end2").val(zookeeper.timeoutdeadline);
               $("#timeout_start2").val(zookeeper.starttimeout);
               $("#timeout_syn2").val(zookeeper.syntimeout);
               $("#max_node2").val(zookeeper.maxnode);
               $("#max_request2").val(zookeeper.maxrequest);


               layer.open({
            	   type: 1,
            	   area: ['600px', '500px'],
            	   content: $("#zookeeperUpdate"),
            	   btn: ['确定', '取消'],
           		   yes: function(index, layero){

           			   if(verify2()){
           				layer.close(index);

           				var id =$("#zookeeperId2").val();
           				var detail = $("#mark2").val();
                    	var cpumemeory =$("input[name='cpu×memeory2']:checked").val();
                    	 var cpu;
                    	   var mem;
                    	   if(Number(cpumemeory)==1){
                    		   cpu = 1;
                    		   mem=2;
                    	   }else if(Number(cpumemeory)==2){
                    		   cpu = 2;
                    		   mem=4;
                    	   }else{
                    		   cpu = 2;
                    		   mem=8;
                    	   }
           				var timeout_end = $("#timeout_end2").val();
                 	   var timeout_start = $("#timeout_start2").val();
                 	   var timeout_syn = $("#timeout_syn2").val();
                 	   var max_node = $("#max_node2").val();
                 	   var max_request = $("#max_request2").val();

           				$.ajax({
        	         		url : ctx + "/zookeeper/update.do",
        	         		type: "POST",
        	         		data: {"id":id,
        	         			"cpu":cpu,"memory":mem,"detail":detail,
        	         			"timeoutdeadline":timeout_end,"starttimeout":timeout_start,
        	         			"syntimeout":timeout_syn,"maxnode":max_node,"maxrequest":max_request
        	         		    },
        	         		success: function(data) {
        	         			var data = eval("("+data+")");
        	                 	if (data.status == 200) {
        	                 		layer.msg("更新成功！",{icon: 6});
        	                 		window.location.reload();
        	                 	}
        	                 	else {
        	                 		layer.alert("更新失败！请检查服务器连接");
        	                 	}
        	         		}
        	         	});
           			   }

           		    }
            	   });
         	}
         	else {
         		layer.alert("请检查服务器连接!");
         	}
 		}
 	});
}

function verify2(){

	var timeout_end = $("#timeout_end2").val().trim();
	   if(timeout_end==''){
		   layer.tips('超时时间下限不能为空!','#timeout_end2',{tips: [1, '#3595CC']});
			$('#timeout_end2').focus();
			return false;
	   }

	   var timeout_start = $("#timeout_start2").val().trim();
	   if(timeout_start==''){
		   layer.tips('初始超时限制不能为空!','#timeout_start2',{tips: [1, '#3595CC']});
			$('#timeout_start2').focus();
			return false;
	   }

	   var timeout_syn = $("#timeout_syn2").val().trim();
	   if(timeout_syn==''){
		   layer.tips('同步超时不能为空!','#timeout_syn2',{tips: [1, '#3595CC']});
			$('#timeout_syn2').focus();
			return false;
	   }

	   var max_node = $("#max_node2").val().trim();
	   if(max_node==''){
		   layer.tips('节点最大并发数不能为空!','#max_node2',{tips: [1, '#3595CC']});
			$('#max_node2').focus();
			return false;
	   }

	   var max_request = $("#max_request2").val().trim();
	   if(max_request==''){
		   layer.tips('待处理请求最大值不能为空!','#max_request2',{tips: [1, '#3595CC']});
			$('#max_request2').focus();
			return false;
	   }

	   var cpumemeory =$("input[name='cpu×memeory2']:checked").val();
	   var restCpu = $("#restCpu2").html();
	   var restMem =  $("#restMem2").html();
	   var cpu;
	   var mem;
	   if(Number(cpumemeory)==1){
		   cpu = 1;
		   mem=2;
	   }else if(Number(cpumemeory)==2){
		   cpu = 2;
		   mem=4;
	   }else{
		   cpu = 2;
		   mem=8;
	   }

	   var lastCpu = $("#lastCpu").val();
	   var lastMem = $("#lastMem").val();

	   if(restCpu!=''&&(cpu - Number(lastCpu))*3>restCpu){
		   layer.tips('剩余cpu不足，请申请更多资源!','#restCpu2',{tips: [1, '#3595CC']});
		   return false;
	   }

	   if(restMem!=''&&(mem - Number(lastMem))*3>restMem){
		   layer.tips('剩余内存不足，请申请更多资源!','#restMem2',{tips: [1, '#3595CC']});
		   return false;
	   }

   return true;
}