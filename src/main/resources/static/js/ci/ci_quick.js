$(document).ready(function () {
	
	changeBaseImageVersion ();
	
 	$("#buildBtn").click(function(){
        if(checkCiAdd()) {
        	$("#buildForm").submit();
        	layer.load(0, {shade: [0.3, '#000']});
        }
        return false;
    });

    $(".btn-imageType .btns").each(function(){
        $(this).click(function(){
            $(".btn-imageType .btns").removeClass("active");
            $(this).addClass("active");
        });
    });



    $('#projectName').blur(function(event){
        var projectName = $('#projectName').val().trim();
        if(projectName.length === 0){
            layer.tips('项目名称不能为空', '#projectName', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
        }
    });

    $('#description').blur(function(event){
        var description = $('#description').val().trim();
        if(description.length === 0){
            layer.tips('项目简介不能为空', '#description', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
        }
    });

    $('#imgNameLast').blur(function(event){
        var imgNameLast = $('#imgNameLast').val().trim();
        if(imgNameLast.length === 0){
            layer.tips('镜像名称不能为空', '#imgNameLast', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
        }
    });
    
    $('#imgNameVersion').blur(function(event){
        var imgNameLast = $('#imgNameVersion').val().trim();
        if(imgNameLast.length === 0){
            layer.tips('镜像版本不能为空', '#imgNameVersion', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
        }
    });
    
    //是否为基础镜像提示信息
    $("#is-baseImage").click(function(){
    	layer.tips('默认为非基础镜像', '#is-baseImage', {
            tips: [2, '#0FA6D8'] //还可配置颜色
        });
    });
    
    //添加基础镜像的版本信息
    $("#baseImageName").change(function(){
    	changeBaseImageVersion ();
    });
    
    function changeBaseImageVersion () {
    	var baseImageName = $("#baseImageName").val();
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
	            			html += "<option type='text' value='"+image.id+"'>"+image.version+"</option>"
	            		}
	            	}
	            }
	            $("#baseImageId").html(html);    
    		}
    	})
    }
    
    function checkCiAdd(){
    	var imgNameFirst = $("#imgNameFirst").val().trim();
        var imgNameLast = $("#imgNameLast").val().trim();
        var imgNameVersion = $("#imgNameVersion").val().trim();
        var projectName = $('#projectName').val().trim();
        var description = $('#description').val().trim();
        // 验证仓库名称
        imgNameLast = imgNameLast.toLowerCase();
        $('#imgNameLast').val(imgNameLast);
        if(imgNameLast.length === 0){
            layer.tips('镜像名称不能为空', '#imgNameLast', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#imgNameLast').focus();
            return false;
        }
        if(imgNameLast.search(/^[a-z][a-z0-9-_]*$/) === -1){
            layer.tips('镜像名称只能由字母、数字、横线及下划线组成，且首字母不能为数字、横线及下划线。', '#imgNameLast', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#imgNameLast').focus();
            return false;
        }
        if(imgNameLast.length > 50 || imgNameLast.length < 3){
            layer.tips('镜像名称为3~50个字符', '#imgNameLast', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#imgNameLast').focus();
            return false;
        }
        
        //验证镜像版本imgNameVersion
        if(imgNameVersion.length === 0){
            layer.tips('镜像版本不能为空', '#imgNameVersion', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#imgNameVersion').focus();
            return false;
        }
        if(imgNameVersion.search(/^[A-Za-z0-9-_]*$/) === -1){
            layer.tips('镜像版本只能由字母、数字、横线和下划线组成', '#imgNameVersion', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#imgNameVersion').focus();
            return false;
        }
        if(imgNameVersion.length > 128 || imgNameVersion.length < 1){
            layer.tips('镜像版本为1~128个字符', '#imgNameVersion', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#imgNameVersion').focus();
            return false;
        }
        // 验证填写的镜像名称是否重复
        var flagName = false;
        $.ajax({
    		url : ctx + "/ci/validciinfo.do",
    		async:false,
    		type: "POST",
    		data:{
    				"imgNameFirst":imgNameFirst,
    				"imgNameLast":imgNameLast,
    				"imgNameVersion":imgNameVersion
    		},
    		success : function(data) {
    			data = eval("(" + data + ")");
    			if (data.status=="400") {
    	            layer.tips('镜像版本重复', '#imgNameVersion', {
    	                tips: [1, '#0FA6D8'] //还可配置颜色
    	            });
    	            $('#imgNameVersion').focus();
    	            flagName = true;
    			} 
    		}
    	});
        if (flagName) {
        	flagName = false;
        	return false;
        }
        
        // 验证简介
        if(description.length === 0){
            layer.tips('项目简介不能为空', '#description', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#description').focus();
            return false;
        }
        
        //如果有codeUrl 则 验证codeUrl
        if($('#codeUrl').length>0){
        	var codeUrl = $('#codeUrl').val().trim();
        	if ($('#codeUrl').attr('type') !== 'hidden') {
        		if(codeUrl.length === 0){
        			layer.tips('代码仓库地址不能为空', '#codeUrl', {
        				tips: [1, '#0FA6D8'] //还可配置颜色
        			});
        			$('#codeUrl').focus();
        			return false;
        		}
        	}
        }
        
        //如果有上传代码，则验证上传代码是否为空
        if ($('#sourceCode').length > 0) {
        	var sourceCode = $('#sourceCode').val().trim();
        	if(sourceCode.length === 0){
    			layer.tips('上传代码不能为空', '#sourceCode', {
    				tips: [1, '#0FA6D8'] //还可配置颜色
    			});
    			$('#sourceCode').focus();
    			return false;
    		}
        }
        
        // 验证项目名称
        if(projectName.length === 0){
            layer.tips('项目名称不能为空', '#projectName', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#projectName').focus();
            return false;
        }
        if(projectName.length > 20 || projectName.length < 3){
            layer.tips('项目名称为3~20个字符', '#projectName', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#projectName').focus();
            return false;
        }
        else {
        	$.ajax({
        		url : ctx + "/ci/judgeCodeCiName.do",
        		async:false,
        		type: "POST",
        		data:{"projectName":projectName},
        		success : function(data) {
        			data = eval("(" + data + ")");
        			if (data.status=="400") {
        	            layer.tips('项目名称重复', '#projectName', {
        	                tips: [1, '#0FA6D8'] 
        	            });
        	            $('#projectName').focus();
        	            flagName = true;
        			} 
        		}
        	});
        }
        if (flagName) {
        	flagName = false;
        	return false;
        }

        return true;
    }
 
});

