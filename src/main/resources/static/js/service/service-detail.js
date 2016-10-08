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
    	  dropdownLog();
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
});

function execCommand(){
		var podName = $(this).attr("podName");
		var namespace = $(this).attr("namespace");
		$("#execText").val("kubectl logs "+podName+" --tail 100 --namespace="+namespace);
}
$(function(){
	$(".podName").on("click",execCommand);
});

//下拉列表选中项对应的log
function dropdownLog(obj){
	if(obj != null){
		$('#serviceid').val($(obj).attr("serviceid"));
		$('#podName').val($(obj).attr("podName"));
	}
	var id = $('#serviceid').val();
	var podName = $('#podName').val();
	var date = $('#date_log1').val();
	$.ajax({
		url:ctx+"/service/detail/getPodlogs.do?id="+id+"&podName="+podName+"&date="+date,
		success:function(data){
			data = $.parseJSON(data);
			if(data.status == '200' && data.logList != ""){
				
				var containerlog = data.logStr;
				var html = '<pre class="serviceLogs" style="background: none repeat scroll 0 0 black; color: #37fc34; border: 0; font-size: 12px; overflow: hidden; float: left;">'
					+ containerlog
				+ '</pre>'
				
				$("#containerlogList").html("");
				$("#containerlogList").html(html);
			}else{
				html += '<pre id="serviceLogs" style="background: none repeat scroll 0 0 black; color: #37fc34; border: 0; font-size: 12px;">今天没有产生日志。</pre>'
				$("#containerlogList").html("");
				$("#containerlogList").html(html);	
			}
			
			}
	})
}


function getServiceLogs(){
	var id = $('#datePicker').attr('serviceid');
	var serviceName = $('#datePicker').attr('serviceName');
	var date = $('#date_log').val();
	$.ajax({
		url:ctx+"/service/detail/getLogsByService.do?id="+id+"&date="+date,
		success:function(data){
			data = $.parseJSON(data);
			var html = "";
			if(data.status == '200' && data.logList != ""){
				var logs = data.logList;
				console.log(logs)
				for(var i = 0; i < logs.length; i++){
					var num = i+1;
					html += '<div class="ci-body container" style="padding-top: 10px; padding-bottom: 30px; width:96%">'
						+ '<div class="">'
						+ '<div class="code-tabmain">'
						+ '<div class="log-details" id="">'
						+ '<div class="event-line " repotype="" status="success">'
						+ '<div class="event-status success">'
						+ '<i class="fa fa-check notes"></i>'
						+ '</div>'
						+ '<div class="time-line-content">'
						+ '<div class="time-line-reason event-title">'
						+ '<div class="title-name success">'
						+ '<span class="event-names">'+serviceName+num+' </span>'
						+'</div>'
						+ '<div class="time-line-time">'
						+ '<div class="event-sign " onclick="ServiceEvent(this)">'
						+ '<i class="fa fa-angle-right fa_caret" style="transform: rotate(0deg);"></i>'
						+ '</div>'
						+ '</div>'
						+ '<div class="containerLog time-line-message" style="min-height: 500px; margin-top: 50px">'
						+ '<div class="weblog logList">'
						+ '<pre class="serviceLogs" style="background: none repeat scroll 0 0 black; color: #37fc34; border: 0; font-size: 12px; overflow: hidden; float: left;">'
						+ logs[i]
						+ '</pre>'
						+ '</div></div></div></div></div></div></div></div></div><br>';
				}	
				$("#hisLogList").html("");
				$("#hisLogList").html(html);
				$(".event-sign").addClass("lives");
				$(".event-sign").parent().parent().children(".time-line-message").css("display","block");
				$(".event-sign").children(".fa_caret").css("transform", "rotate(90deg)");
			}else{
				html += '<pre class="serviceLogs" style="background: none repeat scroll 0 0 black; color: #37fc34; border: 0; font-size: 12px;">今天没有产生日志。</pre>'
				$("#hisLogList").html("");
				$("#hisLogList").html(html);
			}
		}	
	})
}


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
