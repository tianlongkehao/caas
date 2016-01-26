$(document).ready(function(){
    $(".ci-tab").click(function(){
        $(".code-tabmain").children().addClass("hide");
        $(".code-tabmain").children().eq($(this).index()).removeClass("hide");
        $(".ci-tab").removeClass("active");
        $(this).addClass("active");
    });

    registerCiRecordEvent();
    registerDeployEvent();
    registerConstructCiEvent();
    //修改事件
    registerCiEditEvent();
    //删除事件
    registerCiDelEvent($("#id").val());
    //加载构建日志
    printLog();

});

function registerDeployEvent(){
	$("#deploy").unbind("click").click(function(){
		imgId = $(this).attr("imgId");
		if(imgId!=null&&imgId>0){
			window.open(ctx+"/registry/detail/"+imgId);
		}
	});
}
function registerConstructCiEvent(){
	$("#buildBtn").unbind("click").click(function(){
		if($(this).attr("constructionStatus")=="2"){
			return;
		}
		var $this = $(this);
		var id = $this.attr("ciId");
		layer.open({
	        title: '快速构建',
	        content: '确定构建镜像？',
	        btn: ['确定', '取消'],
	        yes: function(index, layero){
	        	$(this).unbind("click");
	        	layer.close(index);
	        	$.ajax({
	        		url:ctx+"/ci/constructCi.do?id="+id,
	        		success:function(data){
	        			data = eval("(" + data + ")");
	       			 	if(data.status=="200"){
	       			 		layer.alert("构建成功");
	       			 	}else{
	       			 		layer.alert(data.msg);
	       			 	}
	       			 	window.location.reload();
	        		},
	        		error:function(){
	        			layer.alert("系统错误，请联系管理员");
	        		}
	        	});
	        	window.location.reload();
	        },
	        cancel: function(index){
	        }
	    });
	});
}


function registerCiEditEvent(){
	$("#editCiBtn").click(function(){
        $("#editCiForm").ajaxSubmit({
            url: ctx+"/ci/modifyCi.do",
            type: "post",
            success: function (data) {
                data = eval("(" + data + ")");
                if (data.status == "200") {
                	layer.alert("修改成功");
                    $("#projectNameSpan").text($("#projectName").val());
                } else {
                    layer.alert(data.msg);
                }
            },
            error: function (e) {
                layer.alert("请求出错");
            }
        });
    });
}
function registerCiDelEvent(id){
	 $("#delCiBtn").click(function(){
		 layer.open({
	        title: '删除构建',
	        content: '确定删除构建？',
	        btn: ['确定', '取消'],
	        yes: function(index, layero){ //或者使用btn1
	        	$.ajax({
                    type:"post",
	        		url:ctx+"/ci/delCi.do",
                    data: {"id" : id},
	        		success:function(data){
	        			data = eval("(" + data + ")");
	        			 if(data.status=="200"){
                             window.location.href = ctx+"/ci";
	                     } else {
	                         layer.alert(data.msg);
	                     }
	        		},
                    error: function(e) {
                        layer.alert("请求出错");
                    }
	        	});
	        },
	        cancel: function(index){ //或者使用btn2
	        }
	    });
    });
}

function registerCiRecordEvent(){
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

function printLog(){
	$(".printLogSpan[status=3]").each(function(){
		var $this = $(this);
		var timer = setInterval(function(){
			$.ajax({
				url:ctx+"/ci/printCiRecordLog.do?id="+$this.attr("ciRecordId"),
				success:function(data){
					data = eval("(" + data + ")");
					 if(data.data.constructResult!="3"){
						 clearInterval(timer);
						 window.location.reload();
					 }
					 $this.html(data.data.logPrint);
					$this.parent(".logs").parent(".build-logs").scrollTop($this.parent().parent(".build-logs")[0].scrollHeight);

				}
			});
		}, 500);
	});
}
