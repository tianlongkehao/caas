$(document).ready(function(){

    $(".next2").click(function(){
        $(".radius_step").removeClass("action").eq(1).addClass("action");
        $(".step-inner").css("left","-100%");

    });
    $(".next3").click(function(){
        $(".radius_step").removeClass("action").eq(2).addClass("action");
        $(".step-inner").css("left","-200%");

    });
    $(".last_step").click(function(){
        if($(".radius_step").eq(1).hasClass("action")){
            $(".step-inner").css("left","0");
            $(".radius_step").removeClass("action").eq(0).addClass("action");
        }else if($(".radius_step").eq(2).hasClass("action")){
            $(".step-inner").css("left","-100%");
            $(".radius_step").removeClass("action").eq(1).addClass("action");
        }

    });


});

$(function(){
    var ramSlider = $("#ramSlider").slider({
        formatter: function(value) {
            return value;
        }
    });

    ramSlider.on("slide", function(slideEvt) {
        $("#ram").val(slideEvt.value);
    }).on("change", function(slideEvt){
        $("#ram").val(slideEvt.value.newValue);
    });

    $("#ram").on("change",function(){
        var ramVal = Number($(this).val());
        console.log(ramVal);
        ramSlider.slider('setValue', ramVal);
    });
});


$(function(){
    var volSlider = $("#volSlider").slider({
        formatter: function(value) {
            return value;
        }
    });

    volSlider.on("slide", function(slideEvt) {
        $("#vol").val(slideEvt.value);
    }).on("change", function(slideEvt){
        $("#vol").val(slideEvt.value.newValue);
    });

    $("#vol").on("change",function(){
        var volVal = Number($(this).val());
        console.log(volVal);
        volSlider.slider('setValue', volVal);
    });
});





