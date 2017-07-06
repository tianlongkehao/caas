$(document).ready(function() {
	/*高级配置*/
	$(".btn-higher").click(function() {
		cfgHighter();
	});
	$(".step2Btn").click(function() {
		cfgHighter();
	});
	$(".step1Btn").click(function() {
		cfgNoHighter();
	});
	$(".btn-nohigher").click(function() {
		cfgNoHighter();
	});

	$("#saveBtn").click(function() {
		//集群名称的判断
		var name = $("#name").val();
		if (name.length == 0) {
			layer.tips('集群名称不能为空', '#name', {
				tips : [1, '#3595CC']
			});
			$('#name').focus();
			return;
		}
		var check = /^[a-z][a-z\d]{3,19}$/;
		if (!check.test(name)) {
			layer.tips('集群名称必须是小写字母开头，只能是小写字母和数字的4-20个字符组成！', '#name', {
				tips : [1, '#3595CC']
			});
			$('#name').focus();
			return;
		}

		//使用者的判断
		var user = $("#user").val();
		if (user.length == 0) {
			layer.tips('使用者不能为空', '#user', {
				tips : [1, '#3595CC']
			});
			$('#user').focus();
			return;
		}

		//给节点数和内存大小赋值
		var nodeRamList = $("#nodeRam").val().split(",");
		//节点数
		$("#nodeNum").val(nodeRamList[1]);
		//内存
		$("#ram").val(nodeRamList[0]);

		var minRam = nodeRamList[0];
		var currentRam = $("#storage").val();
		if (parseInt(minRam) > parseInt(currentRam)) {
			layer.tips('存储卷大小应大于redis内存', '#user', {
				tips : [1, '#3595CC']
			});
			$('#storage').focus();
			return;
		}

		$.ajax({
			url : ctx + "/RedisController/modifyRedisService.do",
			type : "post",
			data : {
				"id" : $("#id").val(),
				"aof" : $("#aof").val() == "yes" ? 1 : 0,
				"aofSync" : $("#aofSync").val(),
				"clientTimeout" : $("#clientTimeout").val(),
				"cpu" : $("#cpu").val(),
				"daemon" : $("#daemon").val() == "yes" ? 1 : 0,
				"databaseDir" : $("#databaseDir").val(),
				"databaseFileName" : $("#databaseFileName").val(),
				"databaseNum" : $("#databaseNum").val(),
				"memoryPolicy" : $("#memoryPolicy").val(),
				"name" : $("#name").val(),
				"nodeConfigFile" : $("#nodeConfigFile").val(),
				"nodeNum" : $("#nodeNum").val(),
				"nodeTimeout" : $("#nodeTimeout").val(),
				"port" : $("#port").val(),
				"ram" : $("#ram").val(),
				"storage" : $("#storage").val(),
				"user" : $("#user").val(),
				"version" : $("#version").val()
			},
			success : function(data) {
				data = eval("(" + data + ")");
				if (data.status == "200") {
					layer.alert("保存成功！生效需要重启服务！", {
						time : 0,
						end : function() {
							window.location.reload();
						}
					});
				} else {
					layer.alert("保存失败：[" + data.message + "]");
				}

			}
		});
	});
	$("#buildBtn").click(function() {
		//集群名称的判断
		var name = $("#name").val();
		if (name.length == 0) {
			layer.tips('集群名称不能为空', '#name', {
				tips : [1, '#3595CC']
			});
			$('#name').focus();
			return;
		}
		var check = /^[a-z][a-z\d]{3,19}$/;
		if (!check.test(name)) {
			layer.tips('集群名称必须是小写字母开头，只能是小写字母和数字的4-20个字符组成！', '#name', {
				tips : [1, '#3595CC']
			});
			$('#name').focus();
			return;
		}

		//使用者的判断
		var user = $("#user").val();
		if (user.length == 0) {
			layer.tips('使用者不能为空', '#user', {
				tips : [1, '#3595CC']
			});
			$('#user').focus();
			return;
		}

		//给节点数和内存大小赋值
		var nodeRamList = $("#nodeRam").val().split(",");
		//节点数
		$("#nodeNum").val(nodeRamList[1]);
		//内存
		$("#ram").val(nodeRamList[0]);

		var minRam = nodeRamList[0];
		var currentRam = $("#storage").val();
		if (parseInt(minRam) > parseInt(currentRam)) {
			layer.tips('存储卷大小应大于redis内存', '#user', {
				tips : [1, '#3595CC']
			});
			$('#storage').focus();
			return;
		}

		$.ajax({
			url : ctx + "/RedisController/createRedisService.do",
			type : "post",
			data : {
				"aof" : $("#aof").val() == "yes" ? 1 : 0,
				"aofSync" : $("#aofSync").val(),
				"clientTimeout" : $("#clientTimeout").val(),
				"cpu" : $("#cpu").val(),
				"daemon" : $("#daemon").val() == "yes" ? 1 : 0,
				"databaseDir" : $("#databaseDir").val(),
				"databaseFileName" : $("#databaseFileName").val(),
				"databaseNum" : $("#databaseNum").val(),
				"memoryPolicy" : $("#memoryPolicy").val(),
				"name" : $("#name").val(),
				"nodeConfigFile" : $("#nodeConfigFile").val(),
				"nodeNum" : $("#nodeNum").val(),
				"nodeTimeout" : $("#nodeTimeout").val(),
				"port" : $("#port").val(),
				"ram" : $("#ram").val(),
				"storage" : $("#storage").val(),
				"user" : $("#user").val(),
				"version" : $("#version").val()
			},
			success : function(data) {
				data = eval("(" + data + ")");
				if (data.status == "200") {
					layer.alert("创建成功！", {
						time : 2000,
						end : function() {
							window.location.href = ctx + "/RedisController/redis";
						}
					});
				} else {
					layer.alert("创建失败：[" + data.message + "]");
				}

			}
		});
	});
});
function cfgHighter() {
	$(".createRedisStep1").hide();
	$(".createRedisStep2").show();
	$(".step1Btn").removeClass("action");
	$(".step2Btn").addClass("action");
}

function cfgNoHighter() {
	$(".createRedisStep1").show();
	$(".createRedisStep2").hide();
	$(".step1Btn").addClass("action");
	$(".step2Btn").removeClass("action");
}

//选择创建集群类型
function changeClusterType(obj) {
	var thisType = $(obj).val();
	var redisMemHtml = "";
	if (thisType == 1) {
		$("#clusterTypeExplain").text("集群版架构，扩展CPU及QPS能力，支持8节点及16节点的集群架构");
		$("#clusterEnabled").val("1");
		redisMemHtml = '<select class="form-control conCred" id="nodeRam" onchange="javascript:changeRamType(this)">' + '<option value="16,8">16G 集群版  8个节点</option>' + '<option value="32,8">32G 集群版  8个节点</option>' + '<option value="64,8">64G 集群版  8个节点</option>' + '<option value="128,16">128G 集群版  16个节点</option>' + '</select>';
		$("#nodeRam").val("16,8");
		$("#storage").val("16");

	} else {
		$("#clusterTypeExplain").text("单机版架构，CPU处理能力为单核");
		$("#clusterEnabled").val("2");
		redisMemHtml = '<select class="form-control conCred" id="nodeRam" onchange="javascript:changeRamType(this)">' + '<option value="1,1">1G 单机版</option>' + '<option value="2,1">2G 单机版</option>' + '<option value="4,1">4G 单机版</option>' + '<option value="8,1">8G 单机版</option>' + '<option value="16,1">16G 单机版</option>' + '<option value="32,1">32G 单机版</option>' + '</select>';
		$("#nodeRam").val("1,1");
		$("#storage").val("1");
	}
	$("#redisMemSec").empty().append(redisMemHtml);
}

//选择redis内存
function changeRamType(obj) {
	var minRam = $(obj).val().split(",")[0];
	$("#storage").val(minRam);
}

//修改集群配置 内存 cpu port 内存消除策略
function changeCfg(obj) {
	var clusterType = $(obj).attr("clusterType");
	var oldRedisVal = $(obj).attr("redisMem");
	var changeRedisMemHtml = "";
	if (clusterType == "1") {
		changeRedisMemHtml = '<select class="form-control conCred" id="oldRedisVal" name="" >' + '<option value="16">16G 集群版</option>' + '<option value="32">32G 集群版</option>' + '<option value="64">64G 集群版</option>' + '<option value="128">128G 集群版</option>' + '</select>';
	} else {
		changeRedisMemHtml = '<select class="form-control conCred" id="oldRedisVal" name="" >' + '<option value="1">1G 单机版</option>' + '<option value="2">2G 单机版</option>' + '<option value="4">4G 单机版</option>' + '<option value="8">8G 单机版</option>' + '<option value="16">16G 单机版</option>' + '<option value="32">32G 单机版</option>' + '</select>';
	}
	$("#changeRedisMemSec").empty().append(changeRedisMemHtml);
	$("#oldRedisVal").val(oldRedisVal);
	layer.open({
		type : 1,
		title : "修改集群配置",
		content : $(".changeCfgInfo"),
		area : ['700px', '340px'],
		btn : ['保存', '关闭'],
		yes : function(index, layero) {

		}
	});
}

