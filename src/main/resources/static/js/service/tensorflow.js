/**
 * Creator：daien
 * Time：2017年6月28日10:57:56
 */
$(function(){
	//loadTensorflowList();
})


/**
 * 加载数据
 */
/*function loadTensorflowList(){
	var itemsHtml = '';
	itemsHtml += '<tr>'
	+'<td style="width: 5%; text-indent: 30px;">'
	+'<input type="checkbox" class="chkItem" name="chkItem"/>'
	+'</td>'
	+'<td style="width: 10%;">'
	+'<a href="javascript:detail(1)" title="查看详细信息">'+'demo'+'</a>'
	+'</td>'
	+' <td style="width: 10%;">' + 1 +' </td>'
	+' <td style="width: 10%;">' + 4 + '</td>'
	+' <td style="width: 15%;">' + 'dddd' + '</td>'
	+' <td style="width: 15%;">' + 'http://dfd.wer.ss' + '</td>'
	+' <td style="width: 15%;">' + '2016-10-11' + '</td>'
	+' <td style="width: 20%;">'
	+   ' <a class="a-live" href="javascript:start(1)" style="margin-left: 5px" title="开始"><i class="fa fa-play self_a"></i></a>'
	+	' <a class="no-drop" href="javascript:stop(1)" style="margin-left: 5px" title="停止" ><i class="fa fa-power-off"></i></a>'
	+	' <a href="javascript:remove(1)" style="margin-left: 5px" title="删除" ><i class="fa fa-trash"></i></a>'
	+'</td>'
	+'</tr>';

    $("#tensorflowList").empty().append(itemsHtml);

}*/

/**
 * 启动
 * @param obj
 */
function start(id){
	$.ajax({
 		url : ctx + "/tensorflow/start.do",
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
 		url : ctx + "/tensorflow/stop.do",
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
 		url : ctx + "/tensorflow/delete.do",
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
 		url : ctx + "/tensorflow/detail.do",
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
		title: '创建TensorFlow',
		area: ['600px', '500px'],
		content: $("#tensorflowAdd"),
		btn: ['确定', '取消'],
		yes: function(index, layero){
           if(verify()){
        	   var tensorflowname = $("#prefix").html() +$("#tensorflowName").val().trim();
        	   $.ajax({
        		   url : ctx + "/tensorflow/exist.do",
	         		type: "POST",
	         		data: {"name":tensorflowname},
	         		success: function(data) {
	         			var data = eval("("+data+")");
	                 	if (data.status == 200) {
                            if(data.exist == 0){
                            	layer.close(index);

                            	var cpu= $("input[name='cpu']:checked").val();;
                            	var mem = $("input[name='memory']:checked").val();;
                            	var imageId = $("#image").val();
                            	var image = $('#image option:selected').text();
                            	var rbdId = $("#storage").val();
                            	var rbd = (rbdId==0?'':$('#storage option:selected').text());
                            	var password = $("#password").val();
                            	var detail = $("#mark").val();
                            	var nginxstr = "";
                        	    $('input[name="nginxserv"]:checked').each(function(){
                            		var servname = $(this).val();
                            		var servid = $(this).attr('id');
                            		if ("" == nginxstr) {
                            			nginxstr = servid;
                            		} else {
                            			nginxstr += ","+servid;
                            		}
                            	});

                            	$.ajax({
                	         		url : ctx + "/tensorflow/add.do",
                	         		type: "POST",
                	         		data: {"name":tensorflowname,"imageId":imageId,"image":image,
                	         			"rbdId":rbdId,"rbd":rbd
                	         			,"password":password,"cpu":cpu
                	         			,"memory":mem,"detail":detail,"proxyZone":nginxstr},
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
                            	layer.alert("tensorflow名称已经存在，请更换名称!");
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
   var tensorflowname = $("#tensorflowName").val().trim();
   if(tensorflowname==''){
	   layer.tips('名称不能为空!','#tensorflowName',{tips: [1, '#3595CC']});
		$('#tensorflowName').focus();
		return false;
   }
   var reg=/^[a-z]([-a-z0-9]*[a-z0-9])?$/;
   if(!reg.test(tensorflowname)){
	    layer.tips('名称只能由小写字母、数字及横线组成，且首字母不能为数字及横线。','#tensorflowName',{tips: [1, '#3595CC'],time: 3000});
	    $('#tensorflowName').focus();
	    return false;
   }

   var image =$("#image").val();
   if(image == 0){
	   layer.tips('请选择镜像!','#image',{tips: [1, '#3595CC']});
		$('#image').focus();
		return false;
   }

   var password = $("#password").val();
   if(password==''){
	   layer.tips('请填写密码!','#password',{tips: [1, '#3595CC']});
		$('#password').focus();
		return false;
   }

   var password2 = $("#password2").val();
   if(password2==''){
	   layer.tips('请填写确认密码!','#password2',{tips: [1, '#3595CC']});
		$('#password2').focus();
		return false;
   }

   if(password!=password2){
	   layer.tips('密码确认失败!','#password2',{tips: [1, '#3595CC']});
	   return false;
   }

   var cpu =$("input[name='cpu']:checked").val();
   var restCpu = $("#restCpu").html();
   cpu = Number(cpu);
   if(restCpu!=''&&cpu>restCpu){
	   layer.tips('剩余cpu不足，请申请更多资源!','#restCpu',{tips: [1, '#3595CC']});
	   return false;
   }

   var mem =$("input[name='memory']:checked").val();
   var restMem =  $("#restMem").html();
   mem=Number(mem);
   if(restMem!=''&&mem>restMem){
	   layer.tips('剩余内存不足，请申请更多资源!','#restMem',{tips: [1, '#3595CC']});
	   return false;
   }

   return true;
}