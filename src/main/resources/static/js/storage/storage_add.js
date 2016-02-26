$(function(){
    var storageSizeSlider = sliderFn('storageSizeSlider', 1024,0, 250);

    //var storageSizeSlider = $("#storageSizeSlider").slider({
    //    formatter: function(value) {
    //        return value;
    //    }
    //});
    //
    storageSizeSlider.on("slide", function(slideEvt) {
        $("#storageSize_input").val(slideEvt.value);
    }).on("change", function(slideEvt){
        $("#storageSize_input").val(slideEvt.value.newValue);
    });

    $("#storageSize_input").on("change",function(){
        var storageVal = Number($(this).val());
        storageSizeSlider.slider('setValue', storageVal);
    });
});

$(document).ready(function () {

    $("#storageName").blur(function(){
        var storageName = $("#storageName").val();
        if (storageName === '') {
            layer.tips('存储名称不能为空', $('#storageName'),{tips: [1, '#EF6578']});
            $('#storageName').focus();
            return;
        }
        var url = ""+ctx+"/service/storage/build/judge";
        $.post(url, {'storageName':storageName}, function(data){
            if(data == 'ok') {
                return;
            }else{
                layer.tips('存储名称重复', $('#storageName'),{tips: [1, '#EF6578']});
                $('#storageName').focus();
                return;
            }
        });
    });

    $("#storageSize").blur(function(){
        var storageSize = $("#storageSize").val();
        if(storageSize == ""){
            layer.tips('存储大小不能为空', $('#storageSize'),{tips: [1, '#EF6578']});
            $('#storageSize').focus();
            return;
        }
    });

    $("#buildStorage").click(function(){
        var storageName = $("#storageName").val();
        var storageSize = $("#storageSize").val();
        var options=$("#format option:selected");
        var format = options.text();

        if (storageName === '') {
            layer.tips('存储名称不能为空', $('#storageName'),{tips: [1, '#EF6578']});
            $('#storageName').focus();
            return;
        }
        if(storageSize == ""){
            layer.tips('存储大小不能为空', $('#storageSize'),{tips: [1, '#EF6578']});
            $('#storageSize').focus();
            return;
        }

        var url = ""+ctx+"/service/storage/build";
        $.post(url, {'storageName':storageName,'storageSize':storageSize,'format':format}, function(data){
            if(data == 'success') {
                layer.msg( "创建成功！", {
                    icon: 1
                },function(){
                    window.location.href = "/service/storage";
                });
            }
            else if(data == 'error'){
                layer.msg( "保存失败。名称相同，请重命名", {
                    icon: 0.5
                });
            }else{
                layer.msg( "保存失败。", {
                    icon: 0.5
                });
            }
        });
    });
});