$(document).ready(function(){

    $(".time-line-content").click(function(){

        if($(this).hasClass("lives")){
            $(this).children(".event-title").children(".time-line-message").css("display","none");
            $(this).children(".event-title").children(".time-line-time").children(".event-sign").children(".fa_caret").css("transform","rotate(0deg)");
            $(this).removeClass("lives");
        }else{
            $(this).children(".event-title").children(".time-line-message").css("display","block");
            $(this).children(".event-title").children(".time-line-time").children(".event-sign").children(".fa_caret").css("transform","rotate(90deg)");

            $(this).addClass("lives");
        }


    });

    $(".ci-tab").click(function(){

        $(".code-tabmain").children().addClass("hide");
        $(".code-tabmain").children().eq($(this).index()).removeClass("hide");
        $(".ci-tab").removeClass("active");
        $(this).addClass("active");

    });


});