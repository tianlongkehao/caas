$(document).ready(function () {
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
    
    //是否为基础镜像提示信息
    $("#is-baseImage").click(function(){
    	layer.tips('默认上传的为基础镜像', '#is-baseImage', {
            tips: [2, '#0FA6D8'] //还可配置颜色
        });
    });
    //是否和上传镜像名字和版本提示信息
/*    $("#image_name_version").click(function(){
    	layer.tips('请确认填写的镜像名称及版本号和上传镜像信息一致！！！', '#image_name_version', {
            tips: [2, '#FF0000'] //还可配置颜色
        });
    });*/

    $('#remark').blur(function(event){
        var projectName = $('#remark').val().trim();
        if(projectName.length === 0){
            layer.tips('项目名称不能为空', '#remark', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
        }
    });

/*    $('#description').blur(function(event){
        var description = $('#description').val().trim();
        if(description.length === 0){
            layer.tips('项目简介不能为空', '#description', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
        }
    });*/

    $('#name').blur(function(event){
        var imgNameLast = $('#name').val().trim();
        if(imgNameLast.length === 0){
            layer.tips('镜像名称不能为空', '#name', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
        }
    });
    
    $('#version').blur(function(event){
        var imgNameLast = $('#version').val().trim();
        if(imgNameLast.length === 0){
            layer.tips('镜像版本不能为空', '#version', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
        }
    });
  
    
    function checkCiAdd(){
    	var imgNameFirst = $("#imgNameFirst").val();
        var name = $("#name").val().trim();
        var version = $("#version").val().trim();
        var remark = $('#remark').val().trim();
        //var description = $('#description').val().trim();

        // 验证仓库名称
        name = name.toLowerCase();
        $('#name').val(name);
        if(name.length === 0){
            layer.tips('镜像名称不能为空', '#name', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#name').focus();
            return false;
        }
        if(name.search(/^[a-z][a-z0-9-_]*$/) === -1){
            layer.tips('镜像名称只能由字母、数字、横线及下划线组成，且首字母不能为数字、横线及下划线。', '#name', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#name').focus();
            return false;
        }
        if(name.length > 50 || name.length < 3){
            layer.tips('镜像名称为3~50个字符', '#name', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#name').focus();
            return false;
        }
        
        //验证镜像版本version
        if(version.length === 0){
            layer.tips('镜像版本不能为空', '#version', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#version').focus();
            return false;
        }
        if(version.search(/^[A-Za-z0-9-_]*$/) === -1){
            layer.tips('镜像版本只能由字母、数字、横线和下划线组成', '#version', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#version').focus();
            return false;
        }
        if(version.length > 128 || version.length < 1){
            layer.tips('镜像版本为1~128个字符', '#version', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#version').focus();
            return false;
        }
        
        // 验证填写的镜像名称是否重复
        var imageFlag = false;
        $.ajax({
    		url : ctx + "/ci/validimageinfo.do",
    		async:false,
    		type: "POST",
    		data:{
    				"imgNameFirst":imgNameFirst,
    				"name":name,
    				"version":version
    		},
    		success : function(data) {
    			data = eval("(" + data + ")");
    			if (data.status=="400") {
    	            layer.tips('镜像版本重复', '#version', {
    	                tips: [1, '#0FA6D8'] //还可配置颜色
    	            });
    	            $('#version').focus();
    				imageFlag = true;
    			} 
    		}
    	});
        if (imageFlag) {
        	imageFlag = false;
        	return false;
        }
        
        
        
        // 验证简介
/*        if(description.length === 0){
            layer.tips('项目简介不能为空', '#description', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            return false;
            $('#description').focus();
        }*/
        // 验证项目名称
        if(remark.length === 0){
            layer.tips('项目名称不能为空', '#remark', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#remark').focus();
            return false;
        }
        if(remark.length > 20 || remark.length < 3){
            layer.tips('项目名称为3~20个字符', '#remark', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            $('#remark').focus();
            return false;
        }

        return true;
    }
});