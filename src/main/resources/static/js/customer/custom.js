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

    $(".nav-menu .nav-item-hover li").on("click",function(){
        $(".nav-menu>li").removeClass("item-click");
        $(this).parents("li").addClass("item-click");
        $("#nav1").html($(this).parents("li").find(".nav-title").html());
        $("#nav2").html($(this).find("a").html());
        $(".contentMain").load($(this).attr("action"));
    });

    if(flag=="ci"){
    	$("#menuCi").click();
    }else if(flag=="service"){
    	$("#menuService").click();
    }else if(flag=="registry"){
    	$("#menuRegistry").click();
    }

});


function getQueryString(name) {
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}