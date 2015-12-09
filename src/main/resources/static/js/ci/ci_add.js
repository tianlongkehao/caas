$(document).ready(function () {


 	$("#buildBtn").click(function(){
        //if(checkCiAdd()) {
            $("#buildForm").ajaxSubmit({
                type: "post",
                success: function (data) {
                    data = eval("(" + data + ")");
                    if (data.status == "200") {
                        $(".contentMain").load("/ci");
                    } else {
                        layer.alert(data.msg);
                    }
                }
            });
        //}
        //return false;
    });

    $('#projectName').blur(function(event){
        var projectName = $('#projectName').val().trim();
        if(projectName.length === 0){
            layer.tips('项目名称不能为空', '#projectName', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
        }
    });

    $('#ciSummary').blur(function(event){
        var ciSummary = $('#ciSummary').val().trim();
        if(ciSummary.length === 0){
            layer.tips('项目简介不能为空', '#ciSummary', {
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
        var codeUrl = $('#codeUrl').val().trim();
        var projectName = $('#projectName').val().trim();
        var ciSummary = $('#ciSummary').val().trim();

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
        // 验证cloneurl
        if ($('#codeUrl').attr('type') !== 'hidden') {
            if(codeUrl.length === 0){
                layer.tips('代码仓库地址不能为空', '#codeUrl', {
                    tips: [1, '#0FA6D8'] //还可配置颜色
                });
                $('#codeUrl').focus();
                return false;
            }
        }
        // 验证简介
        if(ciSummary.length === 0){
            layer.tips('项目简介不能为空', '#ciSummary', {
                tips: [1, '#0FA6D8'] //还可配置颜色
            });
            return false;
            $('#ciSummary').focus();
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