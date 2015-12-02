$(document).ready(function () {
 	$("#buildBtn").click(function(){
 		$("#buildForm").ajaxSubmit({
            type:"post",
            success: function(data) {
               data = eval("(" + data + ")");
               if(data.status=="200"){
               		$(".contentMain").load("/ci");
               }else{
               		alert(data.msg);
               }
            }
        });
        return false;
    });
});