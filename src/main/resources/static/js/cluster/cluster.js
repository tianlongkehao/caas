$(function(){

    $(".Record").click(function(){

        $(".Record").removeClass("active");
        $(this).addClass("active");

        $(".tab_wrap").addClass("hide");
        $("#"+$(this).attr("id")+"_wrap").removeClass("hide");

    });



});

function resource_detail() {
    var ip = "";
    var count = 0;
    $(":checked[name='ips']").each(function(){
        ip = jQuery(this).val();
        count = count + 1;
    });
    if ("" == ip) {
        alert("请选择一个用户");
        return;
    }
    location.href = "cluster/detail/";
}

