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
	
	//codeType
	$(".git-config").hide();
    $("#codeType").change(function(){
    	var codeType = $("#codeType").val();
    	if(codeType == 1){
    		$(".git-config").show();
    		$("#git-higher").show();
    		$(".git-higher").hide();
    	}else{
    		$(".git-config").hide();
    		$(".git-higher").hide();
    	}
    });
    //git-higher
    $("#git-higher").click(function(){
    	$(".git-higher").show();
    	$("#git-higher").hide();
    });
    var count = 0;
    function loadAnt(count){
    	var antHtml = '<div class="row addCiStepRow ant">'+
    	'<div class="col-sm-12">'+
    	'<div class="ibox float-e-margins">'+
    		'<div class="ibox-title">'+
    			'<h5>ant</h5>'+
    			'<div class="ibox-tools">'+
    				'<a class="collapse-link">'+
                        '<i class="fa fa-chevron-up"></i>'+
                    '</a>'+
    				'<a class="close-link">'+
                        '<i class="fa fa-times"></i>'+
                    '</a>'+
    			'</div>'+
    		'</div>'+
    		'<div class="ibox-content">'+
    			'<div class="row ant-config">'+
                	'<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">ant版本</label>'+
                        '<select id="antVersion-'+count+'" name="antVersion" class="form-control c-project-con" >'+
                        	'<option value="default">default</option>'+
                            '<option value="ant">ant</option>'+
                        '</select>'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                       '<label class="c-project-tit">目标</label>'+
                        '<input id="antTargets-'+count+'" name="antTargets" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                '</div>'+
                '<div class="row ant-config">'+
                	'<button class="ant-higherBtn" type="button" style="float:right!important">高级...</button>'+
                '</div>'+
                '<div class="row ant-higherCon hide">'+
                	'<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">构建文件</label>'+
                        '<input id="antBuildFileLocation-'+count+'" name="antBuildFileLocation" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">性能</label>'+
                        '<input id="antProperties-'+count+'" name="antProperties" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">java选项</label>'+
                        '<input id="antJavaOpts-'+count+'" name="antJavaOpts" type="text" class="form-control c-project-con" value="">'+
                    '</div></div></div></div></div></div>';
    	return antHtml;
    }
    
    function loadMaven(count){
    	var mavenHtml = '<div class="row addCiStepRow maven">'+
    	'<div class="col-sm-12">'+
    	'<div class="ibox float-e-margins">'+
    		'<div class="ibox-title">'+
    			'<h5>maven</h5>'+
    			'<div class="ibox-tools">'+
    				'<a class="collapse-link">'+
                        '<i class="fa fa-chevron-up"></i>'+
                    '</a>'+
    				'<a class="close-link">'+
                        '<i class="fa fa-times"></i>'+
                    '</a>'+
    			'</div>'+
    		'</div>'+
    		'<div class="ibox-content">'+
    			'<div class="row">'+
                	'<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">maven版本</label>'+
                        '<select id="mavenVersion-'+count+'" name="mavenVersion" class="form-control c-project-con" >'+
                        	'<option value="default">default</option>'+
                            '<option value="maven">maven</option>'+
                        '</select>'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">目标</label>'+
                        '<input id="mavenGoals-'+count+'" name="mavenGoals" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="row maven-config">'+
	                	'<button class="maven-higherBtn" type="button" style="float:right!important">高级...</button>'+
	                '</div>'+
	                
                    '<div class="maven-higherCon hide"><div class="form-group col-md-12">'+
                        '<label class="c-project-tit">POM</label>'+
                        '<input id="pomLocation-'+count+'" name="pomLocation" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">属性</label>'+
                        '<input id="mavenProperty-'+count+'" name="mavenProperty" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">JVM选项</label>'+
                        '<input id="mavenJVMOptions-'+count+'" name="mavenJVMOptions" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<div style="line-height:34px">'+
                            '<label class="c-project-tit" title="使用私人maven存储库">使用私人maven存储库</label>'+
                            '<input type="checkbox" id = "isUserPrivateRegistry" name = "isUserPrivateRegistry-'+count+'" value = "0">'+
                        '</div>'+
                    '</div>'+
                     '<div class="form-group col-md-12">'+
                        '<div style="line-height:34px">'+
                            '<label class="c-project-tit">注入建立变量</label>'+
                            '<input type="checkbox"  id="injectBuildVariables" name="injectBuildVariables-'+count+'" value="0">'+
                        '</div>'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">配置文件</label>'+
                        '<select id="mavenSetFile-'+count+'" name="mavenSetFile" class="form-control c-project-con" >'+
                        	'<option value="use default maven setting">use default maven setting</option>'+
                            '<option value="settings file in filesystem">settings file in filesystem</option>'+
                        '</select>'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">全局配置文件</label>'+
                        '<select id="mavenGlobalSetFile-'+count+'" name="mavenGlobalSetFile" class="form-control c-project-con" >'+
                        	'<option value="use default maven global setting">use default maven global setting</option>'+
                            '<option value="global settings file in filesystem">global settings file in filesystem</option>'+
                        '</select>'+
                    '</div>'+
                    '</div></div></div></div></div></div>';
    	return mavenHtml;
    }
    
    
    function loadShell(count){
    	shellHtml = '<div class="row addCiStepRow shell">'+
    	'<div class="col-sm-12">'+
    	'<div class="ibox float-e-margins">'+
    		'<div class="ibox-title">'+
    			'<h5>Execute shell</h5>'+
    			'<div class="ibox-tools">'+
    				'<a class="collapse-link">'+
                        '<i class="fa fa-chevron-up"></i>'+
                    '</a>'+
    				'<a class="close-link">'+
                        '<i class="fa fa-times"></i>'+
                    '</a>'+
    			'</div>'+
    		'</div>'+
    		'<div class="ibox-content">'+
    			'<div class="row">'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">命令</label>'+
                        '<textarea id="executeShell-'+count+'" name="executeShell" class="form-control c-project-con" type="text" row="5"></textarea>'+
                    '</div></div></div></div></div></div>';
    	return shellHtml;
    }
    
    //构建maven
    $("#maven").click(function(){
    	count++;
    	var maven = loadMaven(count);
    	$("#sortable").append(maven);
    });
    
    //maven高级按钮选项
    $(document).on('click','.maven-higherBtn',function(){
    	$(this).parent().hide();
    	$(this).parent().next('.maven-higherCon').removeClass("hide");
    });
    
    //构建ant
    $("#ant").click(function(){
    	count++;
    	var ant = loadAnt(count);
    	$("#sortable").append(ant);
    });
    
    //ant高级按钮选项
    $(document).on('click','.ant-higherBtn',function(){
    	$(this).parent().hide();
    	$(this).parent().next('.ant-higherCon').removeClass("hide");
    });
    
    //构建shell
    $("#shell").click(function(){
    	count++;
    	var shell = loadShell(count);
    	$("#sortable").append(shell);
    });
    
    //折叠ibox
    $(document).on('click','.collapse-link',function(){
        var ibox = $(this).closest('div.ibox');
        var button = $(this).find('i');
        var content = ibox.find('div.ibox-content');
        content.slideToggle(200);
        button.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
        ibox.toggleClass('').toggleClass('border-bottom');
        setTimeout(function () {
            ibox.resize();
            ibox.find('[id^=map-]').resize();
        }, 50);
        ibox.css("border-bottom","1px solid #dadada");
    });

    //关闭ibox
    $(document).on('click','.close-link',function(){
        var content = $(this).closest('div.ibox');
        content.remove();
    });
    
    //拖拽
    $( "#sortable" ).sortable({
        revert: true
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
