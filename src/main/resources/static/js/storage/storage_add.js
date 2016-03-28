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
        var url = ""+ctx+"/service/storage/build/validate";
        $.post(url, {'storageName':storageName}, function(data){
        	data = eval("(" + data + ")");
        	if(data.status=="200"){
                return;
            }else{
                layer.tips('存储名称重复', $('#storageName'),{tips: [1, '#EF6578']});
                $('#storageName').focus();
                return;
            }
        });
    });


    $("#buildStorage").click(function(){
        var storageName = $("#storageName").val();
        var storageSize = $("#storageSizeSlider").val();
        var options=$("#format option:selected");
        var format = options.text();
        
        if (storageName === '') {
            layer.tips('存储名称不能为空', $('#storageName'),{tips: [1, '#EF6578']});
            $('#storageName').focus();
            return;
        }
        if(storageSize == 0){
            layer.tips('存储大小只能为数字并且不能为0', $('#storageSize_input'),{tips: [1, '#EF6578']});
            $('#storageSize').focus();
            return;
        }

        var url = ""+ctx+"/service/storage/build";
        $.post(url, {'storageName':storageName,'storageSize':storageSize,'format':format}, function(data){
        	data = eval("(" + data + ")");
        	if(data.status=="200"){
                layer.msg( "创建成功！", {
                    icon: 1
                },function(){
                    window.location.href = ""+ctx+"/service/storage";
                });
            }else{
                layer.msg( "创建失败", {
                    icon: 0.5
                });
            }
        });
    });
});