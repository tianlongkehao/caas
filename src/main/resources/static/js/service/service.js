 $(document).ready(function () {
	$("#serviceReloadBtn").click(function(){
		loadService();
	});
	$("#serviceCreateBtn").click(function(){
		$(".contentMain").load("/service/add");
	});
	
	loadService();
	
	_refreshCreateTime(60000);
	
 });
 function loadService(){
	$.ajax({
		url:"service/listService.do",
		success:function(data){
			data = eval("(" + data + ")");
			if(data.status=="200"){
            	var html="";
            	if(data.data.length>0){
            		for(var i in data.data){
            			var service = data.data[i];
            			var StatusHtml = "";
            			var btnStyleHtml = "";
            			if(service.status = 1){
            				StatusHtml = "<i class='fa_run'></i><span style='color: #65BC2C'>运行中 </span>";
            			}else if(service.status = 2){
            				StatusHtml = "<i class='fa_createing'></i><span style='color: #FFA000'>等待中 </span>";
            			}else if(service.status = 3){
            				StatusHtml = "<i class='fa_success'></i><span style='color: #5E99E1'>创建中 <img class='margin' src='/images/loading4.gif'></span>";
            			}else if(service.status = 4){
            				StatusHtml = "<i class='fa_stop'></i><span style='color: #DF582C'>已停止 </span>";
            			}else if(service.status = 5){
            				StatusHtml = "<i class='fa_stop'></i><span style='color: #DF582C'>创建失败 </span>";
            			}else if(service.status = 6){
            				StatusHtml = "<i class='fa fa-warning'></i><span>服务异常 </span>";
            			}
            			html += "<tr class='clusterId'>"+
	"<td style='width: 5%; text-indent: 30px;'><input type='checkbox'"+
		"name='chkItem' status='' imagename='' imagetag='' value='"+service.id+"'/></td>"+
	"<td style='width: 20%; white-space: nowrap;'><b"+
		"class='caret margin' style='transform: rotate(0deg);'></b> <a"+
		"href='/containers/tenx_district2/cluster/mysql'"+
		"class='cluster_mirrer_name'>"+ service.serviceName +"</a> <span class='number-node'>"+ service.instanceNum +"</span>"+
		"<span class='margin cursor console-code-modal'"+
		"data-id='#console-code-modal'> <i class='fa fa-desktop'"+
			"onclick='_showConsole('mysql');'></i>"+
	"</span></td>"+
	"<td style='width: 10%' id='mysqlstatus'>"+ StatusHtml +
	"</td><td style='width: 20%;'><img"+
		"src='https://dn-tenxstore.qbox.me/tenxcloud_mysql.png?imageView2/2/h/20'"+
		"style='max-height: 20px; max-width: 40px;'> <span"+
		"class='cluster_mirrer'> <a title='点击查看镜像' target='_blank'"+
			"href='https://hub.tenxcloud.com/repos/tenxcloud/mysql'>"+ service.imgName +"</a>"+
	"</span></td>"+
	"<td style='width: 34%' id='mysqlurl'><span class='url'> <a"+
			"href='http://mysql-lynnxu.tenxapp.com:25314' target='_blank'>bonc</a>"+
	"</span></td>"+
	"<td style='width: 10%' class='tdTimeStrap'><input type='hidden'"+
		"class='timeStrap' value='"+ creationTimestamp +"'> <i"+
		"class='fa_time'></i> <span>"+ service.createDate + "</span></td>"+
"</tr>"+
"<tr style='border-left: 1px solid #eee;'>"+
	"<td colspan='8'><div class='align-center'>"+
			"<table class='table'>"+
				"<thead style='background: #FAFAFA; border-top: 1px solid #EDECEC;'>"+
					"<tr class='tr-row'>"+
						"<td style='width: 5%'>&nbsp;</td>"+
						"<td style='width: 20%;'><a style='margin-left: 19px;'"+
							"href='/containers/tenx_district2/instances/mysql-s49b2'>"+ service.serviceName +"-1</a>"+
						"</td>"+
						"<td colspan='2' style='width: 30%'>"+ StatusHtml +
						"</td><td style='width: 34%'> bonc（内网）</td>"+
						"<td style='width: 10%'><i class='fa_time'></i>" +service.createDate+ "</td>"+
					"</tr>"+
				"</thead>"+
			"</table>"+
		"</div></td>"+
"</tr>";
            		}
            	}
            	$("#serviceList").html(html);
            }else{
            	alert(data.msg);
            }
		}
	});
 }
 
 function createContainer(){
	 $('input[name="chkItem"]:checked').each(function(index, el){
		 var id = $(el).val();
			 //alert(id);
			 $.ajax({
					url:"service/createContainer.do?id="+id,
					success:function(data){
						data = eval("(" + data + ")");
						if(data.status=="200"){
							alert("容器启动成功");
							}else{
								alert("容器启动失败");
							}
					}
				
			 });
	 })
	 
	 
 }
 
 function stopContainer(){
	 $('input[name="chkItem"]:checked').each(function(index, el){
	        var id = $(el).val();
	        //alert(id);
	        $.ajax({
	        	url:"service/stopContainer.do?id="+id,
	        	success:function(data){
	        		data = eval("(" + data + ")");
	        		if(data.status=="200"){
	        			alert("容器已停止");
	        		}else{
	        			alert("容器停止失败，请检查服务器连接");
	        		}
	        		
	        	}
	        })
	 })
 }
 
 function delContainer(){
	 $('input[name="chkItem"]:checked').each(function(index, el){
		 var id = $(el).val();
		 $.ajax({
	        	url:"service/delContainer.do?id="+id,
	        	success:function(data){
	        		data = eval("(" + data + ")");
	        		if(data.status=="200"){
	        			alert("容器已删除");
	        		}else{
	        			alert("容器删除失败，请检查服务器连接");
	        		}
	        		
	        	}
	        })
	 })
	 
 }
 
 // Refresh create time
 var creationTimestamp;
 function _refreshCreateTime(interval){
   setInterval(function(){
     $('.tdTimeStrap').each(function(){
       var self = this;
       var timebefore = $(self).find('span').text();
       if (timebefore.indexOf('秒') > -1 || timebefore.indexOf('分钟') > -1) {
         moment.locale('zh-cn');
         creationTimestamp = $(self).find('.timeStrap').val();
         var timeFromNow = moment(creationTimestamp).fromNow();
         $(self).find('span').text(timeFromNow);
       }
     });
   }, interval);
 }
 