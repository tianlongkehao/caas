$(document).ready(function () {
 	$("#buildBtn").click(function(){
        //if(checkCiAdd()) {
        	$("#buildForm").submit();
        	layer.load(0, {shade: [0.3, '#000']});
        //}
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
  
    
    function checkCiAdd(){
        var name = $("#name").val().trim();
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