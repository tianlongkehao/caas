$(document).ready(function(){
    $(".app-list a[class=icon-view]").on("click",function(){
        $("body").load($(this).attr("action"));
    });
});
