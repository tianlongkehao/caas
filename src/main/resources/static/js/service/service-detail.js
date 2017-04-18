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

    $(".SERVOL").click(function(){
        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".serVolMapping").removeClass("hide");
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
    $(".CMD").click(function(){
        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".containerCMD").removeClass("hide");
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

    var dateInput = $('#dateTimePicker');
    dateInput.datetimepicker({
    	changeMonth: true,
        changeYear: true,
        dateFormat: 'yy/mm/dd',
        monthNames: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
        monthNamesShort:["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
        dayNamesMin: ["日", "一", "二", "三", "四", "五", "六"],
        /*minDate: -20,
        maxDate: "+1M +10D"*/
    });
    dateInput.datetimepicker('setDate', (new Date()) );
    /*$('#datePicker').click(function(event) {
         Act on the event
        laydate({
          elem: '#date_log',
          // event: 'focus',
          issure: true, // 是否显示确认
          istime: true,
          format: 'YYYY-MM-DDThh:mm:ss',
          zIndex: 99999999, //css z-index
          choose: function(dates){ //选择好日期的回调
            getServiceLogs();
          }
        });
      });
    $('#datePicker1').click(function(event) {
         Act on the event
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
      });*/

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
    					+ '</pre>';
    					$("#containerlogList2").removeClass("hide");
    					$("#containerlogList2").html("");
    					$("#containerlogList2").html(html);
    					layer.close(load);
    			   }
    			   if (data.status == "400") {
    				    html += '<pre id="serviceLogs" style="background: none repeat scroll 0 0 black; color: #37fc34; border: 0; font-size: 12px;">执行失败！！！</pre>';
    				    $("#containerlogList").html("");
    				    $("#containerlogList").html(html);
    				    layer.close(load);
    			   }
    		   }
    	   });
       });




	//可编辑的基本信息
	//编辑
	$(".editBaseCon").hide();
	$("#restSerBtn").hide();
	$("#canclSerBtn").hide();
	$("#saveSerBtn").hide();
	$("#editSerBtn").click(function() {
		if ($("#serStatus").val() == 1 | $("#serStatus").val() == 4) {
			$(".editBaseCon").show();
			$(".oldBaseCon").hide();
		} else {
			$(".editBaseCon_Run").show();
			$(".oldBaseCon_Run").hide();
		}
		$("#saveSerBtn").show();
		$("#restSerBtn").show();
		$("#canclSerBtn").show();
	});
	//保存
	$("#saveSerBtn").click(function() {
		//提交之前验证
		//判断服务名称
		var name = $('#serviceName').val();
		if (!name || name.length < 1) {
			layer.tips('服务名称不能为空', '#serviceName', {
				tips : [1, '#3595CC']
			});
			$('#serviceName').focus();
			return;
		}
		if (name.search(/^[a-z][a-z0-9-]*$/) === -1) {
			layer.tips('服务名称只能由小写字母、数字及横线组成，且首字母不能为数字及横线。', '#serviceName', {
				tips : [1, '#3595CC'],
				time : 3000
			});
			$('#serviceName').focus();
			return;
		}
		if (name.length > 24 || name.length < 1) {
			layer.tips('服务名称为1~24个字符', '#serviceName', {
				tips : [1, '#3595CC'],
				time : 3000
			});
			$('#serviceName').focus();
			return;
		}

		//检查服务状态的判断
		var checkPath = $("#checkSerStatus_input").val();
		var initialDelay = $("#initialDelay").val();
		var timeoutDetction = $("#timeoutDetction").val();
		var periodDetction = $("#periodDetction").val();

		if (!checkPath || checkPath.length < 1) {
		} else if (checkPath.search(/^[a-zA-Z\/][a-zA-Z0-9-\/]*$/) === -1) {
			layer.tips('测试路径只能由字母、数字、斜线及横线组成，且首字母不能为数字及横线。', '#checkSerStatus_input', {
				tips : [1, '#3595CC'],
				time : 3000
			});
			$('#checkSerStatus_input').focus();
			return;
		} else if (checkPath.length > 64 || checkPath.length < 3) {
			layer.tips('测试路径为3~64个字符', '#checkSerStatus_input', {
				tips : [1, '#3595CC'],
				time : 3000
			});
			$('#checkSerStatus_input').focus();
			return;
		}

		//服务路径的判断
		var servicePath = $("#webPath").val();
		if (!servicePath || servicePath.length < 1) {
			layer.tips('服务路径不能为空', '#webPath', {
				tips : [1, '#3595CC']
			});
			$('#webPath').focus();
			return;
		}
		if (servicePath.search(/^[a-zA-Z\/][a-zA-Z0-9-\/]*$/) === -1) {
			layer.tips('服务路径只能由字母、数字、斜线及横线组成，且首字母不能为数字及横线。', '#webPath', {
				tips : [1, '#3595CC'],
				time : 3000
			});
			$('#webPath').focus();
			return;
		}
		//nginx代理路径的判断
		var proxyPath = $("#nginxPath").val();
		if (!proxyPath || proxyPath.length < 1) {
			layer.tips('nginx代理路径不能为空', '#nginxPath', {
				tips : [1, '#3595CC']
			});
			$('#nginxPath').focus();
			return;
		}
		if (proxyPath.search(/^[a-zA-Z\/][a-zA-Z0-9-\/]*$/) === -1) {
			layer.tips('nginx代理路径只能由字母、数字、斜线及横线组成，且首字母不能为数字及横线。', '#nginxPath', {
				tips : [1, '#3595CC'],
				time : 3000
			});
			$('#nginxPath').focus();
			return;
		}
		var commitFlag = true;
		//判断是否已存在该服务名
		if ($("#oldSerName").text() != name) {
			$.ajax({
				url : ctx + "/service/matchServiceName.do",
				type : "POST",
				async:false,
				data : {
					"serviceName" : name,
				},
				success : function(data) {
					data = eval("(" + data + ")");
					if (data.status != "200") {
						layer.tips('服务名称重复，请重新输入！', '#serviceName', {
							tips : [1, '#3595CC'],
							time : 3000
						});
						$('#oldSerName').focus();
						commitFlag=false;
						return;
					}
				}
			});
		}
		//判断是否已存在该代理路径
		if ($("#oldProxyPath").text() != proxyPath) {
			$.ajax({
				url : ctx + "/service/matchProxyPath.do",
				type : "POST",
				async:false,
				data : {
					"proxyPath" : proxyPath,
				},
				success : function(data) {
					data = eval("(" + data + ")");
					if (data.status != "200") {
						layer.tips('nginx路径名称重复，请重新输入！', '#nginxPath', {
							tips : [1, '#3595CC'],
							time : 3000
						});
						$('#nginxPath').focus();
						commitFlag=false;
						return;
					}
				}
			});
		}
		if(commitFlag){
			commBaseSerForm();
			$(".editBaseCon").hide();
			$(".oldBaseCon").show();
		}
	});

       			//取消
       $("#canclSerBtn").click(function(){
    	   $(".editBaseCon").hide();
    	   $(".oldBaseCon").show();
    	   $("#saveSerBtn").hide();
    	   $("#restSerBtn").hide();
    	   $("#canclSerBtn").hide();
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


	//可编辑的服务地址
	$(".editCon").hide();
	$("#editServiceAddrBtn").click(function() {
/*
		// $('#addrPrex').html("http://");
		// var addr = $('#editServiceAddrValue').val().substr(7);
		// $('#editServiceAddr').val(addr);*/

		$("#editServiceAddrBtn").hide();
		$(".editCon").show();
		$(".oldCon").hide();
	});
	$("#saveEdit").click(function() {
		if (false == checkSerAddr()) {
			return;
		};
		$("#editServiceAddrBtn").show();
		$(".editCon").hide();
		editSerAddr();
		$(".oldCon").show();
	});
	$("#canclEdit").click(function() {
		$("#editServiceAddrBtn").show();
		$(".editCon").hide();
		$(".oldCon").show();
	});

	//可编辑的责任人
	$(".editCon1").hide();
	$("#editResponseBtn").click(function() {

		$("#editResponseBtn").hide();
		$(".editCon1").show();
		$(".oldCon1").hide();
	});
	$("#saveEdit1").click(function() {
		if (false == checkResponse()) {
			return;
		};
		$("#editResponseBtn").show();
		$(".editCon1").hide();
		editResponse();
		$(".oldCon1").show();
	});
	$("#canclEdit1").click(function() {
		$("#editResponseBtn").show();
		$(".editCon1").hide();
		$(".oldCon1").show();
	});

	//可以编辑的端口号
	$(".editPortConfig").hide();
	//可编辑的环境变量
	$(".editEnv").hide();

});/*ready*/

function editPortComm(portConfigId,containerPort){
	   alert(portConfigId);
//	   $(this).parent().(".e")
}
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
};

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
	sinceTime = new Date().Format("yyyy-MM-ddThh:mm:ss.000000000Z");
	getCurrentPodlogs();
	clearInterval(interval);
	interval = setInterval("getCurrentPodlogs()",5000);
}
//下拉列表选中项对应的log
function dropdownLog(obj){
	$(".dropdown-pod").each(function(){
		$(".dropdown-pod").removeClass("bgcolor");
	});
	$(obj).addClass("bgcolor");
	clearInterval(interval);
	if(obj != null){
		$('#podName').val($(obj).attr("podName"));
		var a= ctx + '/service/detail/getPodlogFile?podName='+$(obj).attr("podName");
		$('#getPodlogFile').attr("href",a);
	}
	var podName = $('#podName').val();
	$.ajax({
		url:ctx+"/service/detail/getPodlogs.do?&podName="+podName,
		success:function(data){
			data = $.parseJSON(data);
			if(data.status == '200' && data.logStr != ""){

				var containerlog = data.logStr;
				$(".printLogSpan").html(containerlog);
			}else{
				$(".printLogSpan").html("该实例没有产生日志。");
			}
			   $(".printLogSpan").parent().parent().scrollTop($(".printLogSpan").parent().parent()[0].scrollHeight);
			}
	});
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
				$(".printLogSpan").html(containerlog);

			}else{
				$(".printLogSpan").html("该实例没有产生日志。");
			}
			   $(".printLogSpan").parent().parent().scrollTop($(".printLogSpan").parent().parent()[0].scrollHeight);
			}
	});
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

//获取前缀
function getprex(){
	$.ajax({
		type: "GET",
   url: ctx + "/service/detail/getprex.do",
   success : function(data) {
	  data = eval("(" + data + ")");

	  }
	});
}
//修改服务地址
function editSerAddr(){

	var editServiceAddr = $('#editServiceAddr').val();
	var editProxyPath = $('#editProxyPath').val();
	var serId = $('#serId').val();
	editServiceAddr = editServiceAddr;
	$.ajax({
		type : "GET",
		url : ctx + "/service/detail/editSerAddr.do?serviceAddr=" + editServiceAddr + "&proxyPath=" + editProxyPath + "&serId=" + serId,
		success : function(data) {
			data = eval("(" + data + ")");
			if (data.status == "200") {
				$('#oldServiceAddr').html(editServiceAddr);
				$('#oldProxyPath').html(editProxyPath);
				layer.msg("修改成功", {
					icon : 1
				},function(){
					location.reload();
				});
			} else if (data.status == "500") {
				layer.alert("服务地址重复，请重新输入！");
			} else {
				layer.msg("修改失败，请检查连接", {
					icon : 1
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

function checkResponse() {
	var responsiblePerson = $('#editResponsiblePerson').val();
	if (responsiblePerson.length > 24 || responsiblePerson.length < 1) {
		layer.tips('责任人为1~24个字符', $('#editResponsiblePerson'), {
			tips : [1, '#EF6578']
		});
		$('#editResponsiblePerson').focus();
		return false;
	}
	reg = /^((\d{11})|((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})))$/;
	var responsiblePersonTelephone = $('#editResponsiblePersonTelephone').val();
	if (!reg.test(responsiblePersonTelephone)) {
		layer.tips('非法的电话格式', '#editResponsiblePersonTelephone', {
			tips : [1, '#EF6578'],
		});
		$('#editResponsiblePersonTelephone').focus();
		return false;
	}
}
//修改责任人信息
function editResponse(){
	var editResponsiblePerson = $('#editResponsiblePerson').val();
	var editResponsiblePersonTelephone = $('#editResponsiblePersonTelephone').val();
	var serId = $('#serId').val();
	$.ajax({
		type : "GET",
		url : ctx + "/service/detail/editResponse.do?responsiblePerson=" + editResponsiblePerson + "&responsiblePersonTelephone=" + editResponsiblePersonTelephone + "&serId=" + serId,
		success : function(data) {
			data = eval("(" + data + ")");
			if (data.status == "200") {
				$('.oldCon1').html('责任人：' + editResponsiblePerson + ' ' + editResponsiblePersonTelephone);
				layer.msg("修改成功", {
					icon : 1
				},function(){
					location.reload();
				});
			} else {
				layer.msg("修改失败，请检查连接", {
					icon : 1
				});
			}
		}
	});
}

//环境变量修改按钮
function editEnvBtn(obj){
	$(obj).hide();
	$(obj).next().show();
	$(obj).next().next().show();
	$(obj).parent().parent().find("span.oldEnv").hide();
	$(obj).parent().parent().find("span.editEnv").show();
}
//环境变量保存按钮
function saveEnvEdit(obj){
   var envKey=$(obj).parent().parent().find("input.envKey").val();
   var envValue=$(obj).parent().parent().find("input.envValue").val();
   var id =$(obj).parent().parent().find("input.envId").val();
   var serId =$("#serId").val();
   var serName =$("#serviceName").val();
   if(1==checkRepEnv($(obj).parent().parent().find("input.envKey"),id,envKey)){return;};
	//环境变量Key只能是字母数字下划线；
	reg=/^[A-Za-z_][A-Za-z0-9_]*$/;
	if(!reg.test(envKey)){
		layer.tips('环境变量key只能是字母数字下划线，不能以数字开头',$(obj).parent().parent().find("input.envKey"),{tips: [1, '#3595CC']});
		$('#newKey').focus();
		return;
	}
	//判断envKey长度
	if(envKey.length >= 4096){
    	layer.tips('key字符长度不能超过4096',$(obj).parent().parent().find("input.envKey"),{tips: [1, '#3595CC']});
		$('#newKey').focus();
		return;
    }
	//判断envValue长度
	if(envValue.length >= 4096){
    	layer.tips('value字符长度不能超过4096',$(obj).parent().parent().find("input.envValue"),{tips: [1, '#3595CC']});
		$('#newValue').focus();
		return;
    }

	$.ajax({
		url : ctx + "/service/detail/editEnv.do?envKey=" + envKey + "&envValue=" + envValue + "&envId=" + id + "&serviceId=" + serId + "&serviceName=" + serName,
		success : function(data) {
			var data = eval("(" + data + ")");
			if ("200" == data.status) {
				layer.msg("修改成功，重启服务后生效", {
					icon : 1,
					time : 2000
				},function(){
					$(obj).parent().find("i.editEnvBtn").show();
					$(obj).next().hide();
					$(obj).parent().parent().find("span.oldEnv").show();
					var id =$(obj).parent().parent().find("input.envId").val();
					document.getElementById("key"+id).innerHTML=envKey;
					document.getElementById("value"+id).innerHTML=envValue;
					$(obj).parent().parent().find("span.editEnv").hide();
					$(obj).hide();
				});
			}
		}
	});

}
//环境变量取消按钮
function canclEnvEdit(obj){
	$(obj).parent().find("i.editEnvBtn").show();
  $(obj).parent().find("i.saveEnv").hide();
  $(obj).parent().parent().find("span.oldEnv").show();
  $(obj).parent().parent().find("span.editEnv").hide();
  $(obj).hide();
//  $("#BaseSerForm").resetForm();
}

//端口修改按钮
function editPortAddrBtn(obj){
	   if($("#serStatus").val()==1 | $("#serStatus").val()==4){
 	   $(obj).hide();
 	   $(obj).next().show();
 	   $(obj).next().next().show();
 	   $(obj).parent().parent().find("span.oldPortConfig").hide();
 	   $(obj).parent().parent().find("span.editPortConfig").show();
 	   }
}
//端口保存按钮
function savePortEdit(obj){
	 var thiz= $(obj);
   var port=$(obj).parent().parent().find("input.containerPort").val();
   var id =$(obj).parent().parent().find("input.portId").val();
   var serId =$("#serId").val();
   var serName =$("#serviceName").val();

	if (1 == checkRepPortCfg(obj, id, port)) {
		return;
	}
	if (!port || port.length < 1) {
		layer.tips('容器端口不能为空', $(obj).parent().parent().find("input.containerPort"), {
			tips : [1, '#3595CC']
		});
		$(obj).parent().parent().find("input.containerPort").focus();
		flag = true;
		return;
	}
	if (port.search(/^[0-9]*$/) === -1) {
		layer.tips('容器端口只能是数字组成', $(obj).parent().parent().find("input.containerPort"), {
			tips : [1, '#3595CC'],
			time : 3000
		});
		$(obj).parent().parent().find("input.containerPort").focus();
		flag = true;
		return;
	}


   $(obj).parent().find("i.editPortAddrBtn").show();
   $(obj).next().hide();
   $(obj).parent().parent().find("span.oldPortConfig").show();
   $(obj).parent().parent().find("span.editPortConfig").hide();
   $(obj).hide();

	$.ajax({
		url:ctx+"/service/detail/editPortConfig.do?containerPort="+port+"&serviceName="+serName+"&portId="+id+"&serviceId="+serId,
		success:function(data){
			var data = eval("(" + data + ")");
			if("200"==data.status){
				layer.msg( "修改成功，重启服务后生效", {
					icon: 1,
					time: 2000
                });
			}
		}
	});
}
//端口新建按钮
function addPortCfgClick(obj){
	layer.open({
		type:1,
		content:$('#createCfg-template'),
		title:'新建端口信息',
		btn:['新建','取消'],
		yes: function(index, layero){
			var containerPort	= $("#containerPort").val();
			var protocol= $("#protocol").val();
			var mapPort =$("#mapPort").val();
			var serId =$("#serId").val();
			if (1 == checkRepPortCfg(obj, null, containerPort)) {
				layer.tips('容器端口不能重复', "#containerPort", {
					tips : [1, '#3595CC']
				});
				$("#containerPort").focus();
				return;
			}
			if (!containerPort || containerPort.length < 1) {
				layer.tips('容器端口不能为空', $("#containerPort"), {
					tips : [1, '#3595CC']
				});
				$("#containerPort").focus();
				flag = true;
				return;
			}
			if (containerPort.search(/^[0-9]*$/) === -1) {
				layer.tips('容器端口只能是数字组成', $("#containerPort"), {
					tips : [1, '#3595CC'],
					time : 3000
				});
				$("#containerPort").focus();
				flag = true;
				return;
			}
			$.ajax({
				url : ctx + "/service/detail/addPortCfg.do?containerPort="+containerPort+"&protocol="+protocol+"&mapPort="+mapPort+"&serviceId="+serId,
				type: "GET",
				success : function(data) {
					portConfig = eval("(" + data + ")");
					var portTr='';
					portTr +='<tr>'
						+'<td style="width:10%;text-indent: 15px;">'+portConfig.service.serviceName+'</td>'
						+'<td style="width:10%;" class="portConfig"><span class="oldPortConfig">'+portConfig.pCfg.containerPort+'</span>'
						+'<span class="editPortConfig"><input class="containerPort" type="text" value="'+portConfig.pCfg.containerPort+'" name="containerPort"/></span>'
						+'<input class="portId" hidden="true" value="'+portConfig.pCfg.portId+'"/>'
						+'</td>'
					  +'<td style="width:10%;">'+portConfig.pCfg.protocol+'</td>'
						+'<td style="width:10%;">'+portConfig.pCfg.mapPort+'</td>'
						+'<td style="width:50%;"><a href="'+portConfig.service.serviceAddr+'/'+portConfig.service.proxyPath+'" target="_blank">'
						+portConfig.service.serviceAddr+'/'+portConfig.service.proxyPath+'</a></td>'
						+'<td style="width:10%;" class="editBtn">'
						+'<i onclick="editPortAddrBtn(this)"  type="button" value="修改"  class="fa fa-edit oldPortConfig editPortAddrBtn"></i>'
						+'<i onclick="savePortEdit(this)" hidden=true type="button" value="提交"  class="fa fa-save editPortConfig savePortEdit"></i>'
						+'<i onclick="canclPortEdit(this)" hidden=true type="button" value="取消"  class="fa fa-times editPortConfig"></i>'
						+' <i onclick="delPortEdit(this)" type="button" value="删除"  class="fa fa-trash editPortBtn"></i>  '
						+'</td></tr>';
					$("#editPortCfgBody").append(portTr);
					$(".editPortConfig").hide();
					$(".editPortCfgBtn").show();

					layer.close(index);
				}
				});
		}
	});
}
//端口删除按钮
function delPortEdit(obj){
		layer.open({
			title:'删除端口',
			content:"确定删除？",
			btn:['确定','取消'],
			yes: function(index, layero){
				layer.close(index);
				var portId =$(obj).parent().parent().find("input.portId").val();
				$.ajax({
					url:ctx + "/service/detail/delPortCfg.do?portId="+portId,
					type: "POST",
					success : function(data) {
						var data = eval("(" + data + ")");
						if("200"==data.status){
							layer.msg( "删除成功，重启服务后生效", {
								icon: 1
			                });
						$(obj).parent().parent().remove();
					}else{
						alert("失败，出现了网络异常");
					}
					}
				});
			}
		});
}
//端口取消按钮
function canclPortEdit(obj){
	   $(obj).parent().find("i.editPortAddrBtn").show();
	   $(obj).parent().find("i.savePortEdit").hide();
	   $(obj).parent().parent().find("span.oldPortConfig").show();
	   $(obj).parent().parent().find("span.editPortConfig").hide();
	   $(obj).hide();
	   $("#BaseSerForm").resetForm();
};
//新增环境变量
function addEnvClick(obj){
	layer.open({
		type:1,
		content:$('#createEnv-templat'),
		title:'新建环境变量',
		btn:['新建','取消'],
		yes: function(index, layero){
			var envKey	= $("#newKey").val();
			var envValue= $("#newValue").val();
			var serId =$("#serId").val();
			if(1==checkRepEnv($("#newKey"),null,envKey)){
				return;
			};
			//环境变量Key只能是字母数字下划线；
			reg=/^[A-Za-z_][A-Za-z0-9_]*$/;
			if(!reg.test(envKey)){
				layer.tips('环境变量key只能是字母数字下划线，不能以数字开头',$("#newKey"),{tips: [1, '#3595CC']});
				$('#newKey').focus();
				return;
			}
			//判断envKey长度
			if(envKey.length >= 4096){
		    	layer.tips('key字符长度不能超过4096',$("#newKey"),{tips: [1, '#3595CC']});
				$('#newKey').focus();
				return;
		    }
			//判断envValue长度
			if(envValue.length >= 4096){
		    	layer.tips('value字符长度不能超过4096',$$("#newValue"),{tips: [1, '#3595CC']});
				$('#newValue').focus();
				return;
		    }

			$.ajax({
				url : ctx + "/service/detail/addEnv.do?envKey="+envKey+"&envValue="+envValue+"&serviceId="+serId,
				type: "GET",
				success : function(data) {
					envVariable = eval("(" + data + ")");
					var	newEnv='<tr><td style="width:40%;text-indent: 15px;">'
		            +'<span id="key'+envVariable.envId+'" class="oldEnv">'+envVariable.envKey+'</span>'
		            +'<span class="editEnv" hidden="true"><input class="envKey" type="text" name="envKey" value="'+envVariable.envKey+'"  /></span>'
		            +'<input class="envId" hidden="true" value="'+envVariable.envId+'" /></td>'
		            +'<td>'
		            +'<span id="value'+envVariable.envId+'" class="oldEnv">'+envVariable.envValue+'</span>'
		            +' <span class="editEnv" hidden="true"><input class="envValue" type="text" name="envValue" value="'+envVariable.envValue+'"  /></span></td>'
		               +'<td style="width:10%;" class="editBtn">'
		                   +'<i onclick="editEnvBtn(this)"  type="button" value="修改"  class="fa fa-edit oldEnvConfig editEnvBtn"></i>'
		                   +'<i onclick="saveEnvEdit(this)" hidden=true type="button" value="提交"  class="fa fa-save editEnv saveEnv"></i>'
		                   +'<i onclick="canclEnvEdit(this)" hidden=true type="button" value="取消"  class="fa fa-times editEnv"></i>'
		                   +' <i onclick="delEnvEdit(this)"  type="button" value="删除"  class="fa fa-trash editEnv editEnvBtn"></i>'
		                   +'</td>'
		        +'</tr>';
					$("#editEnvBody").append(newEnv);
					$(".editEnv").hide();
					$(".editEnvBtn").show();
					layer.close(index);
					layer.msg("修改成功，重启服务后生效", {
						icon : 1,
						time : 2000
					});
				}
				});
		}
	});
	};
//删除环境变量
	function delEnvEdit(obj){
		layer.open({

			title:'删除环境变量',
			content:"确定删除？",
			btn:['确定','取消'],
			yes: function(index, layero){
				layer.close(index);
				var envId =$(obj).parent().parent().find("input.envId").val();
				$.ajax({
					url:ctx + "/service/detail/delEnv.do?envId="+envId,
					type: "POST",
					success : function(data) {
						var data = eval("(" + data + ")");
						if("200"==data.status){
							layer.msg( "删除成功，重启服务后生效", {
								icon: 1
			                });
						$(obj).parent().parent().remove();
					}else{
						alert("失败，出现了网络异常");
					}
					}
				});
			}
		});
	}
	//环境变量验重
	function checkRepEnv(obj,id,envKey){
		var flag =0;
		if(envKey=="APM" || envKey == "namespace" || envKey == "service"){
			layer.tips('环境变量Key不能重复',$(obj),{tips: [1, '#3595CC']});
			$(obj).focus();
			flag=1;
			return flag;
		}
		$('#editEnvBody tr').each(function(index,domEle){
			var ek= $(domEle).find("input.envKey").val();
			var ei=$(domEle).find("input.envId").val();
			if(ei!=id && ek== envKey){
				layer.tips('环境变量Key不能重复',$(obj),{tips: [1, '#3595CC']});
				$(obj).focus();
				flag=1;
				return;
			}
		});
		return flag;
	}
	//端口信息验重
	function checkRepPortCfg(obj,id,port){
			 var flag =0;
			 $('#editPortCfgBody tr').each(function(index,domEle){
				   var pcp= $(domEle).find("input.containerPort").val();
				   var pi= $(domEle).find("input.portId").val();
				   if(null==id & port==pcp){flag=1; return false;}
				   if(id==pi){
					   return true;}
				   if(port==pcp){
					   layer.tips('容器端口不能重复',$(obj).parent().parent().find("input.containerPort"),{tips: [1, '#3595CC']});
					   $(obj).parent().parent().find("input.containerPort").focus();
					   flag=1;
					   return false;
				   }
			   });
			 return flag;
	}
