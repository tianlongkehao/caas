$(document).ready(function(){

    $(".nextTwo").click(function(){
        $(".radius_step").removeClass("action").eq(1).addClass("action");
        $(".step-inner").css("left","-100%");

    });
    $(".checkBtn").click(function(){
        $(".radius_step").removeClass("action").eq(2).addClass("action");
        $(".step-inner").css("left","-200%");

    });
    $(".installBtn").click(function(){
        $(".radius_step").removeClass("action").eq(3).addClass("action");
        $(".step-inner").css("left","-300%");

    });
    $(".last_step").click(function(){
        if($(".radius_step").eq(1).hasClass("action")){
            $(".step-inner").css("left","0");
            $(".radius_step").removeClass("action").eq(0).addClass("action");
        }else if($(".radius_step").eq(2).hasClass("action")){
            $(".step-inner").css("left","-100%");
            $(".radius_step").removeClass("action").eq(1).addClass("action");
        }
        else if($(".radius_step").eq(3).hasClass("action")){
            $(".step-inner").css("left","-200%");
            $(".radius_step").removeClass("action").eq(2).addClass("action");
        }

    });


});





