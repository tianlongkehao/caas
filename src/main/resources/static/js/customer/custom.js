$(document).ready(function(){

    // 菜单效果
    $(".nav-menu>li").on("mouseenter",function(){
        $(".nav-menu>li").css("background-color","transparent");
        $(".nav-menu>li").children(".nav-item-hover").css("display","none");
        $(this).css("background-color","#3c81e0");
        $(this).children(".nav-item-hover").css("display","block");
    });

    $(".nav-menu>li").on("mouseleave",function(){
        $(this).css("background-color","transparent");
        $(this).children(".nav-item-hover").css("display","none");
        $(".item-click").css("background-color","#3c81e0");
    });

    $(".nav-menu>li").on("click",function(){
        $(".nav-menu>li").removeClass("item-click");
        $(this).addClass("item-click");
    });



});