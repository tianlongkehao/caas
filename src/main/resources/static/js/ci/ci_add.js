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


    function checkCiAdd(){
        var imgNameLast = $("#imgNameLast").val().trim();
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
        // 验证简介
        if(description.length === 0){
            layer.tips('项目简介不能为空', '#description', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            return false;
            $('#description').focus();
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

        return true;
    }

});