$(document).ready(function(){

    $(".images-panel").mouseover(function(){
        $(this).children(".create-item").css("opacity","1");
    });

    $(".images-panel").mouseout(function(){
        $(this).children(".create-item").css("opacity","0");
    });


    $(".list_info").click(function(){
        $(".table_list>.list_info").removeClass("active");
        $(".table_list").siblings("section").addClass("hide");
        $(this).addClass("active").parent().siblings("section").eq($(this).index()).removeClass("hide");
    });

});
