$(document).ready(function(){

    $(".baseInfo>a").click(function(){

        $(".baseInfo>a").removeClass("btn-prim");
        $(this).addClass("btn-prim");
    });

    $(".BASE").click(function(){

        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".containerInfo").removeClass("hide");
    });


    $(".INSTANCES").click(function(){

        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".containerInstances").removeClass("hide");
    });


    $(".DOMAIN").click(function(){

        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".bindDomain").removeClass("hide");
    });


    $(".PORTS").click(function(){

        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".portMapping").removeClass("hide");
    });


    $(".MONITOR").click(function(){

        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".monitorInfo").removeClass("hide");
    });


    $(".LOG").click(function(){

        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".containerLog").removeClass("hide");
    });


    $(".EVENT").click(function(){

        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".containerEvent").removeClass("hide");
    });


    $( "#datePicker" ).click(function(){});

});
