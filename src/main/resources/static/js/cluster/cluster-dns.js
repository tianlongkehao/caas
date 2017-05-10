$(document).ready(function () {
	
})

function timedTask(){
	layer.open({
		type: 1,
		title: "定时检查",
		content: $(".timedTaskInfo"),
		area: ["500px"],
		btn: ["确定","取消"],
		yes: function(index,layero){
			var checkItems = $("i.fa-check:visible");
			var domains = new Array();
			if(checkItems.length>5){
				layer.msg("最多可选5个服务域名", {
					icon : 2
				});
				return false;
			}else{
				for(var i=0; i<checkItems; i++){
					var checkItem = checkItems[i];
					domains.push($(checkItems).attr("domain"));
				}
				layer.close(index);
			}
			
		}
	});
}

function checkServerDomain(obj){
	$(obj).find("i.fa-check").toggle();
}


