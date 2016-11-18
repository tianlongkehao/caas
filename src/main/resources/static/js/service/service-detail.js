$(document).ready(function(){
    $(".baseInfo>ul>li>a").click(function(){

        $(".baseInfo>ul>li>a").removeClass("btn-prim");
        $(this).addClass("btn-prim");
    });

    $(".BASE").click(function(){

        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".containerInfo").removeClass("hide");
    });


    $(".INSTANCES").click(function(){

        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".containerInstances").removeClass("hide");
    });


    $(".DOMAIN").click(function(){

        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".bindDomain").removeClass("hide");
    });

    $(".ENVS").click(function(){

        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".envMapping").removeClass("hide");
    });

    $(".PORTS").click(function(){

        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".portMapping").removeClass("hide");
    });


    $(".MONITOR").click(function(){

        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".monitorInfo").removeClass("hide");
    });

    $(".LOG").click(function(){
        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".containerLog").removeClass("hide");
    });
    
    $(".historyLOG").click(function(){
        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".historycontainerLog").removeClass("hide");
    });

    $(".execCommand").click(function(){
        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $("#containerexec").removeClass("hide");
        $("#execText").val("");
    });
    
    $(".EVENT").click(function(){

        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".containerEvent").removeClass("hide");
    });
    
    //setInterval("getServiceLogs()",10000);
    //getServiceLogs();
    
    $('#datePicker').click(function(event) {
        /* Act on the event */
        laydate({
          elem: '#date_log',
          // event: 'focus',
          issure: true, // 是否显示确认
          istime: true,
          format: 'YYYY-MM-DDThh:mm:ss',
//          min: $('#creationTime').val(),
//          max: laydate.now(+0),
          zIndex: 99999999, //css z-index
          choose: function(dates){ //选择好日期的回调
//            logPage = 1;
            getServiceLogs();
          }
        });
      });
    $('#datePicker1').click(function(event) {
        /* Act on the event */
        laydate({
          elem: '#date_log1',
          // event: 'focus',
          issure: true, // 是否显示确认
          istime: true,
          format: 'YYYY-MM-DDThh:mm:ss',
//          min: $('#creationTime').val(),
//          max: laydate.now(+0),
          zIndex: 99999999, //css z-index
          choose: function(dates){ //选择好日期的回调
//            logPage = 1;
        	  dropdownLog();
          }
        });
      });
    
      $('#refreshLog').click(function (event) {
    	  getServiceLogs();
      });
      $('#refreshLog1').click(function (event) {
    	  clearLog();
      });
       
      $('#fullScreen').click(function () {
        $('.containerLog').toggleClass('all');
        var title = $(this).attr('title');
        if(title == '满屏'){
          $(this).addClass('fa-compress').attr('title','退出满屏').removeClass('fa-exprend');
        }else{
          $(this).addClass('fa-exprend').attr('title','满屏').removeClass('fa-compress');
        }
      });
      
       $("#execcmd").click(function(){
    	   var cmd = $("#execText").val();
    	   if (cmd == "" || cmd == null) {
    		   layer.tips('命令不能为空！', '#execText', {
    	            tips: [2, '#0FA6D8'] //还可配置颜色
    	        });
    		   return;
    	   }
    	   load = layer.load(0, {shade: [0.3, '#000'],time:30000});
    	   $.ajax({
    		   async:false,
    		   url:ctx+"/service/detail/execcmd.do?cmd="+cmd,
    		   success:function(data){
    			   data = eval("(" + data + ")");
    			   if (data.status == "200") {
    				   	var containerlog = data.result;
    				   	containerlog = containerlog.replace(/</g,"&lt");
    				   	containerlog = containerlog.replace(/>/g,"&gt");
    					var html = '<pre class="serviceLogs" style="background: none repeat scroll 0 0 black; color: #37fc34; border: 0; font-size: 12px; overflow: hidden; float: left;">'
    						+ containerlog
    					+ '</pre>'
    					$("#containerlogList2").removeClass("hide");
    					$("#containerlogList2").html("");
    					$("#containerlogList2").html(html);
    					layer.close(load);
    			   }
    			   if (data.status == "400") {
    				    html += '<pre id="serviceLogs" style="background: none repeat scroll 0 0 black; color: #37fc34; border: 0; font-size: 12px;">执行失败！！！</pre>'
    				    $("#containerlogList").html("");
    				    $("#containerlogList").html(html);	
    				    layer.close(load);
    			   }
    		   }
    	   })
       });
       //可编辑的基本信息
       			//编辑
       $(".editBaseCon").hide();
       $("#restSerBtn").hide();
       $("#editSerBtn").click(function(){
    	   beforeEditDo();
    	   if($("#serStatus").val()==1 | $("#serStatus").val()==4){
    		   $(".editBaseCon").show();
        	 $(".oldBaseCon").hide();
    	   }else{
    		   $(".editBaseCon_Run").show();
          $(".oldBaseCon_Run").hide();
    	   			}
      	 $("#saveSerBtn").show();
      	 $("#restSerBtn").show();
       });
       			//保存
       $("#saveSerBtn").click(function(){
    	   $(".editBaseCon").hide();
    	   commBaseSerForm();
    	   $(".oldBaseCon").show();
       });
       			//取消
       $("#canclSerBtn").click(function(){
    	   $(".editBaseCon").hide();
    	   $(".oldBaseCon").show();
    	   $("#saveSerBtn").hide();
    	   $("#restSerBtn").hide();
       });
       	//reset
       $("#restSerBtn").click(function(){
    	   $("#BaseSerForm").resetForm();
       });
       function commBaseSerForm(){
    	   $("#BaseSerForm").ajaxSubmit(function(data) {
    		   var data = eval("(" + data + ")");
    		   location.reload();
    		}); 
       }
       function beforeEditDo(){
    	   var serType=$('#serType').val();
    	   var serMonPath=$('#serMonPath').val();
    	   var serVolName=$('#serVolName').val();
    	   var str='';
    	   $.ajax({
    		   url:ctx+"/service/storage/getVols.do",
    				success:function(data){
    					var data = eval("(" + data + ")");
    					for(var i=0; i<data.storages.length; i++){
    						storage=data.storages[i];
    					str+='<option value="'+storage.storageName+'">'
    					+storage.storageName+' '+storage.storageSize
    					+'M </option>';
    				}
    					$('#selSerType').append(str);
    				}
    	   			});
    	   
    	  
       }
       
       
       //可编辑的服务地址
       $(".editCon").hide();
       $("#editServiceAddrBtn").click(function(){
    	   getprex();
    	   $(".editCon").show();
    	   $(".oldCon").hide();
       });
       $("#saveEdit").click(function(){
   			if(false==checkSerAddr()){return ;};
    	   $(".editCon").hide();
    	   editSerAddr();
    	   $(".oldCon").show();
       });
       $("#canclEdit").click(function(){
    	   $(".editCon").hide();
    	   $(".oldCon").show();
       });
       //可以编辑的段口号
       $(".editPortConfig").hide();
       $("#editServiceAddrBtn").click(function(){
    	   $(".editPortConfig").show();
    	   $(".oldPortConfig").hide();
       } );
       $("#savePortEdit").click(function(){
 // 			if(false==checkSerAddr()){return ;};
   	   $(".editPortConfig").hide();
 //  	   editSerAddr();
   	   $(".oldCon").show();
      });
      $("#canclPortEdit").click(function(){
   	   $(".editPortConfig").hide();
   	   $("#BaseSerForm").resetForm();
   	   $(".oldPortConfig").show();
      });
});/*ready*/
  
Date.prototype.Format = function(fmt){
	var o = {
			"M+":this.getUTCMonth()+1,
			"d+":this.getUTCDate(),
			"h+":this.getUTCHours(),
			"m+":this.getUTCMinutes(),
			"s+":this.getUTCSeconds(),
			"q+":Math.floor((this.getUTCMonth()+1)/3),
			"S":this.getUTCMilliseconds()
	};
	if(/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1,(this.getUTCFullYear()+"").substr(4-RegExp.$1.length));
	for(var k in o)
		if(new RegExp("("+k+")").test(fmt))
			fmt =fmt.replace(RegExp.$1,(RegExp.$1.length==1)?(o[k]):(("00"+o[k]).substr((""+o[k]).length)));
	return fmt;
}

function execCommand(){
	var podName = $(this).attr("podName");
	var namespace = $(this).attr("namespace");
	$("#execText").val("kubectl logs "+podName+" --tail 100 --namespace="+namespace);
}

$(function(){
	$(".podName").on("click",execCommand);
});
	
	
var sinceTime;
var interval;
function clearLog() {
	sinceTime = new Date().Format("yyyy-MM-ddThh:mm:ss.000000000Z")
	$("#containerlogList").html("");
	getCurrentPodlogs();
	clearInterval(interval);
	interval = setInterval("getCurrentPodlogs()",5000);
}
//下拉列表选中项对应的log
function dropdownLog(obj){
	$("#containerlogList").html("");
	clearInterval(interval);
	if(obj != null){
		$('#podName').val($(obj).attr("podName"));
		var a= ctx + '/service/detail/getPodlogFile?podName='+$(obj).attr("podName");
		$('#getPodlogFile').attr("href",a);
	}
	var podName = $('#podName').val();
//	var date = $('#date_log1').val();
	$.ajax({
		url:ctx+"/service/detail/getPodlogs.do?&podName="+podName,
		success:function(data){
			data = $.parseJSON(data);
			if(data.status == '200' && data.logStr != ""){
				
				var containerlog = data.logStr;
				var html = '<pre class="serviceLogs" style="background: none repeat scroll 0 0 black; color: #37fc34; border: 0; font-size: 12px; overflow: hidden; float: left;">'
					+ containerlog
				+ '</pre>'
				
				$("#containerlogList").html("");
				$("#containerlogList").html(html);
			}else{
				var html = '<pre id="serviceLogs" style="background: none repeat scroll 0 0 black; color: #37fc34; border: 0; font-size: 12px;"> 该实例没有产生日志。</pre>'
				$("#containerlogList").html("");
				$("#containerlogList").html(html);	
			}
			
			}
	})
}

//获取当前Pod的实时日志
function getCurrentPodlogs(){
	var podName = $('#podName').val();
	$.ajax({
		url:ctx+"/service/detail/getCurrentPodlogs.do?&podName="+podName+"&sinceTime="+sinceTime,
		success:function(data){
			data = $.parseJSON(data);
			if(data.status == '200' && data.logStr != ""){
				
				var containerlog = data.logStr;
				var html = '<pre class="serviceLogs" style="background: none repeat scroll 0 0 black; color: #37fc34; border: 0; font-size: 12px; overflow: hidden; float: left;">'
					+ containerlog
				+ '</pre>'
				
				$("#containerlogList").html("");
				$("#containerlogList").html(html);
			}else{
				var html = '<pre id="serviceLogs" style="background: none repeat scroll 0 0 black; color: #37fc34; border: 0; font-size: 12px;">实时刷新中，当前无新日志产生。</pre>'
				$("#containerlogList").html("");
				$("#containerlogList").html(html);
			}
			
			}
	})
}


//function getServiceLogs(){
//	var id = $('#datePicker').attr('serviceid');
//	var serviceName = $('#datePicker').attr('serviceName');
//	var date = $('#date_log').val();
//	$.ajax({
//		url:ctx+"/service/detail/getLogsByService.do?id="+id+"&date="+date,
//		success:function(data){
//			data = $.parseJSON(data);
//			var html = "";
//			if(data.status == '200' && data.logList != ""){
//				var logs = data.logList;
//				console.log(logs)
//				for(var i = 0; i < logs.length; i++){
//					var num = i+1;
//					html += '<div class="ci-body container" style="padding-top: 10px; padding-bottom: 30px; width:96%">'
//						+ '<div class="">'
//						+ '<div class="code-tabmain">'
//						+ '<div class="log-details" id="">'
//						+ '<div class="event-line " repotype="" status="success">'
//						+ '<div class="event-status success">'
//						+ '<i class="fa fa-check notes"></i>'
//						+ '</div>'
//						+ '<div class="time-line-content">'
//						+ '<div class="time-line-reason event-title">'
//						+ '<div class="title-name success">'
//						+ '<span class="event-names">'+serviceName+num+' </span>'
//						+'</div>'
//						+ '<div class="time-line-time">'
//						+ '<div class="event-sign " onclick="ServiceEvent(this)">'
//						+ '<i class="fa fa-angle-right fa_caret" style="transform: rotate(0deg);"></i>'
//						+ '</div>'
//						+ '</div>'
//						+ '<div class="containerLog time-line-message" style="min-height: 500px; margin-top: 50px">'
//						+ '<div class="weblog logList">'
//						+ '<pre class="serviceLogs" style="background: none repeat scroll 0 0 black; color: #37fc34; border: 0; font-size: 12px; overflow: hidden; float: left;">'
//						+ logs[i]
//						+ '</pre>'
//						+ '</div></div></div></div></div></div></div></div></div><br>';
//				}	
//				$("#hisLogList").html("");
//				$("#hisLogList").html(html);
//				$(".event-sign").addClass("lives");
//				$(".event-sign").parent().parent().children(".time-line-message").css("display","block");
//				$(".event-sign").children(".fa_caret").css("transform", "rotate(90deg)");
//			}else{
//				html += '<pre class="serviceLogs" style="background: none repeat scroll 0 0 black; color: #37fc34; border: 0; font-size: 12px;">今天没有产生日志。</pre>'
//				$("#hisLogList").html("");
//				$("#hisLogList").html(html);
//			}
//		}	
//	})
//}


function ServiceEvent(obj) {
	if ($(obj).hasClass("lives")) {
		$(obj).parent().parent().children(".time-line-message").css("display","none");
		$(obj).children(".fa_caret").css("transform", "rotate(0deg)");
		$(obj).removeClass("lives");
	} else {
		$(obj).parent().parent().children(".time-line-message").css("display","block");
		$(obj).children(".fa_caret").css("transform", "rotate(90deg)");
		$(obj).addClass("lives");
	}
}

//获取前缀
function getprex(){
	$.ajax({
		type: "GET",
   url: ctx + "/service/detail/getprex.do",
   success : function(data) {
	  data = eval("(" + data + ")");
	  $('#addrPrex').html(data.prex);
	  }
	});
}
//修改服务地址
function editSerAddr(){
		var editServiceAddr=$('#editServiceAddr').val();
		var editProxyPath=$('#editProxyPath').val();
		var serId=$('#serId').val();
//		var prex=	$('#addrPrex').html();
    editServiceAddr=editServiceAddr;
			$.ajax({
        		type: "GET",
           url: ctx + "/service/detail/editSerAddr.do?serviceAddr="+editServiceAddr+"&proxyPath="+editProxyPath+"&serId="+serId,
           success : function(data) {
        	  data = eval("(" + data + ")");
        	  if(data.status=="200"){
        		  $('#oldServiceAddr').html(editServiceAddr);
        		  $('#oldProxyPath').html(editProxyPath)
	     			layer.msg( "修改成功，重启服务后生效", {
   						icon: 1
	   					});
	         }else if(data.status=="500"){
	        	 layer.alert("服务名称重复，请重新输入！");
	         }else{
	        	 layer.msg( "修改失败，请检查连接", {
							icon: 1
	   					});
	        			}
           	}
        	});
}
function checkSerAddr(){
    if ($('#editServiceAddr').val() === '') {
        layer.tips('服务路径不能为空', $('#editServiceAddr'),{tips: [1, '#EF6578']});
        $('#editServiceAddr').focus();
        return false;
    }
    if ($('#editProxyPath').val() === '') {
        layer.tips('代理不能为空', $('#editProxyPath'),{tips: [1, '#EF6578']});
        $('#editProxyPath').focus();
        return false;
    }
}