$(document).ready(function(){

    $(".baseInfo>a").click(function(){

        $(".baseInfo>a").removeClass("btn-prim");
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


    $(".EVENT").click(function(){

        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".containerEvent").removeClass("hide");
    });
    
    setInterval("getServiceLogs()",10000);
    
    $('#datePicker').click(function(event) {
        /* Act on the event */
        laydate({
          elem: '#date_log',
          // event: 'focus',
          issure: false, // 是否显示确认
          min: $('#creationTime').val(),
          max: laydate.now(+0),
          zIndex: 99999999, //css z-index
          choose: function(dates){ //选择好日期的回调
            logPage = 1;
            getServiceLogs();
          }
        });
      });
      $('#refreshLog').click(function (event) {
    	  getServiceLogs();
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



    
    ServiceEvent();

});

function getServiceLogs(){
	var id = $('#datePicker').attr('serviceid');
	var date = $('#date_log').val();
	$.ajax({
		url:ctx+"/service/detail/getlogs.do?id="+id+"&date="+date,
		success:function(data){
			data = $.parseJSON(data);
			data.logStr = "";
			var html = "";
			if(data.status == '200' && data.logStr != ""){
					var logs = data.logStr;
					console.log(logs)
					html += '<pre id="serviceLogs" style="background: none repeat scroll 0 0 black; color: #37fc34; border: 0; font-size: 12px;">'+
						logs+'<br></pre>'+
						'<input id="serviceInstances" type="hidden" value="">'+
						'<input id="creationTime" type="hidden" value="'+date+'">';
					
					$("#logList").html(html);
			}
		}	
	})
}


function ServiceEvent(){
	$(".time-line-content").unbind("click").click(function(){
        if($(this).hasClass("lives")){
            $(this).children(".event-title").children(".time-line-message").css("display","none");
            $(this).children(".event-title").children(".time-line-time").children(".event-sign").children(".fa_caret").css("transform","rotate(0deg)");
            $(this).removeClass("lives");
        }else{
            $(this).children(".event-title").children(".time-line-message").css("display","block");
            $(this).children(".event-title").children(".time-line-time").children(".event-sign").children(".fa_caret").css("transform","rotate(90deg)");
            $(this).addClass("lives");
        }
    });
}
