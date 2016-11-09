$(document).ready(function(){
    $(".ci-tab").click(function(){
        $(".code-tabmain").children().addClass("hide");
        $(".code-tabmain").children().eq($(this).index()).removeClass("hide");
        $(".ci-tab").removeClass("active");
        $(this).addClass("active");
    });
    changeBaseImageVersion();
    registerCiRecordEvent();
    registerDeployEvent();
    registerConstructCiEvent();
    //修改事件
    registerCiEditEvent();
    //删除事件
    registerCiDelEvent($("#id").val());
    //加载构建日志
    printLog();
    
    //导入模板
	$("#docImport-btn").click(function(){
		layer.open({
		 	type:1,
	        title: 'dockerfile模板',
	        content: $("#dockerfile-import"),
	        btn: ['导入', '取消'],
	        yes: function(index, layero){ 
	        	
	        	$("#dockerFile").val(dockerFile);
	        	layer.close(index);
	        }
	 })
	});
	
	//导入模板文件选项对勾
	var dockerFile = null;
	$("#Path-table-doc>tbody>tr").on("click", function () {
		$(this).parent().find("tr.focus").find("span.doc-tr").toggleClass("hide");
        $(this).parent().find("tr.focus").toggleClass("focus");//取消原先选中行
        //$("#Path-table>tbody>tr").parent().find("tr.focus").find("span.vals-path").removeClass("hide")
        $(this).toggleClass("focus");//设定当前行为选中行
        $(this).parent().find("tr.focus").find("span.doc-tr").toggleClass("hide");
        dockerFile = $(this).parent().find("tr.focus").find(".dockerFileTemplate").val();
    });

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
	$("#editCiUploadBtn").click(function(){
		var index = layer.load(0, {shade: [0.3, '#000']});
        $("#editCiUploadForm").ajaxSubmit({
            url: ctx+"/ci/modifyDockerFileCi.do",
            type: "post",
            success: function (data) {
            	layer.close(index);
                data = eval("(" + data + ")");
                if (data.status == "200") {
                	layer.alert("修改成功");
                    $("#projectNameSpan").text($("#projectName").val());
                } else {
                    layer.alert(data.msg);
                }
            },
            error: function (e) {
            	layer.close(index);
                layer.alert("请求出错");
            }
        });
    });
	
	$("#editCiUploadCodeBtn").click(function(){
		var index = layer.load(0, {shade: [0.3, '#000']});
        $("#editCiUploadCodeForm").ajaxSubmit({
        	url: ctx+"/ci/modifyCodeResourceCi.do",
        	type: "post",
        	success: function (data) {
        		layer.close(index);
        		data = eval("(" + data + ")");
        		if (data.status == "200") {
        			layer.alert("修改成功");
        			$("#projectNameSpan").text($("#projectName").val());
        		} else {
        			layer.alert(data.msg);
        		}
        	},
        	error: function (e) {
        		layer.close(index);
        		layer.alert("请求出错");
        	}
        });
        
    });
	
	$("#baseImageName").change(function(){
		changeBaseImageVersion();
	});
}

function changeBaseImageVersion () {
	var baseImageName = $("#baseImageName").val();
	var baseVersion = $("#ownBase").html();
	$.ajax({
		url:""+ctx+"/ci/findBaseImageVersion.do",
		type:"post",
		data:{"baseImageName":baseImageName}, 
		success: function (data) {
            data = eval("(" + data + ")");
            var html = "";
            if (data != null) {
            	if (data['data'].length > 0) {
            		for (var i in data.data) {
            			var image = data.data[i];
            			if (baseVersion == image.version) {
            				html = "" + html;
            			}else {
            				html += "<option type='text' value='"+image.id+"'>"+image.version+"</option>"
            			}
            		}
            	}
            }
            $("#baseImageId").append(html);    
		}
	})
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
	$(".event-sign").unbind("click").click(function(){
        if($(this).hasClass("lives")){
        	$(this).parent().parent().children(".time-line-message").css("display","none");
            $(this).children(".fa_caret").css("transform","rotate(0deg)");
            $(this).removeClass("lives");
        }else{
        	$(this).parent().parent().children(".time-line-message").css("display","block");
            $(this).children(".fa_caret").css("transform","rotate(90deg)");
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
		}, 1000);
	});
}
