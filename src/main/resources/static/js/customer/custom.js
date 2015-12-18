$(function(){

    // 全选  全选checkbox class设为 chkAll
    $(".chkAll").click(function(){
        $(".chkItem").prop('checked',$(".chkAll").is(":checked"));
    });

    // 每条数据 checkbox class设为 chkItem
    $(".chkItem").each(function(){

        $(this).click(function(){

            if($(this).is(":checked")){
                if ($(".chkItem:checked").length == $(".chkItem").length) {
                    $(".chkAll").prop("checked", "checked");
                }
            }else{
                $(".chkAll").prop('checked', $(this).is(":checked"));
            }

        });
    });

});