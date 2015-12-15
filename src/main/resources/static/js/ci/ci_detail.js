$(document).ready(function(){
    $(".ci-tab").click(function(){
        $(".code-tabmain").children().addClass("hide");
        $(".code-tabmain").children().eq($(this).index()).removeClass("hide");
        $(".ci-tab").removeClass("active");
        $(this).addClass("active");
    });

    registerCiRecordEvent();

    //修改事件
    registerCiEditEvent();
    //删除事件
    registerCiDelEvent($("#id").val());

});
function registerCiEditEvent(){
	$("#editCiBtn").click(function(){
        $("#editCiForm").ajaxSubmit({
            url: "/ci/modifyCi.do",
            type: "post",
            success: function (data) {
                data = eval("(" + data + ")");
                if (data.status == "200") {
                	layer.alert("修改成功");
                    console.log($("#projectName").val());
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
	        		url:"/ci/delCi.do",
                    data: {"id" : id},
	        		success:function(data){
	        			data = eval("(" + data + ")");
	        			 if(data.status=="200"){
	        				 layer.alert("删除成功");
                             window.location.href = "/ci";
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
/*function loadCi(id){
	$.ajax({
		url:"/ci/findCi.do?id="+id,
		success:function(data){
			data = eval("(" + data + ")");
			 if(data.status=="200"){
				 var ci = data.data;
				 $("#id").val(id);
				 $("#projectNameSpan").html(ci.projectName);
				 $("#codeLocation").html(ci.imgNameLast);
				 $("#codeLocation").attr("href",ci.codeUrl);
				 $("#projectName").val(ci.projectName);
				 $("#description").val(ci.description);
				 $("#dockerFileLocation").val(ci.dockerFileLocation);
				 $("#codeBranch").val(ci.codeBranch);
			 }else{
				 layer.alert(data.msg);
			 }
		}
	});
}*/

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
/*
function loadCiRecordList(id){
	$.ajax({
		url:"/ci/listCiRecord.do?id="+id,
		success:function(data){
			data = eval("(" + data + ")");
			 if(data.status=="200"){
				 var ci = data.ci;
            	var html="";
            	if(data.data.length>0){
            		for(var i in data.data){
            			var ciRecord = data.data[i];
            			var statusHtml = "";
            			if(ciRecord.constructResult=="1"){
            				statusHtml =  "<i class='fa_run'></i>成功";
            			}else if(ciRecord.constructResult=="2"){
            				statusHtml =  "<i class='fa_stop'></i>失败";
            			}
            			html += "<div class='event-line' repotype='' status='success'>"+
                              "<div class='event-status success'>"+
                                  "<i class='fa fa-check notes'></i>"+
                              "</div>"+
                              "<div class='time-line-content lives'>"+
                                  "<div class='time-line-reason event-title'>"+
                                      "<div class='title-name'>"+
                                          "<span class='event-names'>"+
                                              ci.projectName+
                                              "<span class='btn-version'>"+ciRecord.ciVersion+"</span>"+
                                          "</span>"+
                                          "<span class='time-on-status'>"+
                                              statusHtml+
                                          "</span>"+
                                      "</div>"+
                                      "<div class='time-line-time'>"+
                                          "<div class='event-sign'><i class='fa fa-angle-right fa_caret' style='transform: rotate(90deg);'></i></div>"+
                                          "<div class='datetimes'><i class='fa fa-calendar margin'></i>"+ciRecord.constructDate+"</div>"+
                                          "<div class='time-on-timeout'><i class='fa fa-time'></i>"+ciRecord.constructTime+"</div>"+
                                      "</div>"+
                                      "<div class='time-line-message' style='display: block;'>"+
                                          "<div class='buildForm' buildid='' containerid='' buildername='builder2' status='fail'>"+
                                              "<div style='clear:both;'></div>"+
                                              "<div class='buildStatus' style='margin:0px 0px 10px 0px'></div>"+
                                              "<div class='build-logs' style='max-height: 400px; overflow: auto;margin-top:10px;background-color:black;color: #37fc34'>"+
                                                  "<pre class='logs' style='background-color:black;color: #37fc34;border:0'>"+
															"<span>" +
															"</span>"+
                                                        "</pre>"+
                                                    "</div>"+
                                                "</div>"+
                                            "</div>"+
                                        "</div>"+
                                    "</div>"+
                                 "</div>";
            		}
            	}
            	$("#ciRecordList").html(html);
            	registerCiRecordEvent();
            }
		}
	});
}*/
