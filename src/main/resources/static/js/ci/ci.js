$(document).ready(function () {
	$("#ciReloadBtn").click(function(){
		window.location.reload();
	});

    registerConstructCiEvent();

});


function registerConstructCiEvent(){
	$(".bj-green").unbind("click").click(function(){
		if($(this).attr("constructionStatus")=="2"){
			return;
		}
		var $this = $(this);
		var id = $this.attr("ciId");
		layer.open({
	        title: '构建镜像',
	        content: '确定构建镜像？',
	        btn: ['确定', '取消'],
	        yes: function(index, layero){ //或者使用btn1
	        	var cStatusHtml = "<i class='fa_success'></i>"+
					                "构建中"+
					                "<img src='"+ctx+"/images/loading4.gif' alt=''/>";
	        	$this.parent().parent().find(".cStatusColumn").html(cStatusHtml);
	        	$this.css("cursor","no-drop");
	        	$(this).unbind("click");
	        	layer.close(index);
	        	$.ajax({
	        		url:ctx+"/ci/constructCi.do?id="+id,
	        		async:true,
	        		success:function(data){
	        			data = eval("(" + data + ")");
	       			 	if(data.status=="200"){
	       			 		//layer.alert("构建成功");
	       			 	}else{
	       			 		layer.alert(data.msg);
	       			 	}
	       			 	window.location.reload();
	        		},
	        		error:function(){
	        			layer.alert("系统错误，请联系管理员");
	        			window.location.reload();
	        		}
	        	});
	        },
	        cancel: function(index){ //或者使用btn2
	        }
	    });
	});
}
