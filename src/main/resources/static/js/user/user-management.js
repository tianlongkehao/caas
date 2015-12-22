$(function(){

    // 全选
    $(".chkAll").click(function(){
        $(".chkItem").prop('checked',$(".chkAll").is(":checked"));
    });

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
    
    $("#userAdd").click(function(){
		$(".contentMain").load("/user/add");
	});


});