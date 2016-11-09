$(document).ready(function () {
    //codeType
    $("#codeType").change(function(){
    	var codeType = $("#codeType").val();
    	if(codeType == 1){
    		$(".git-config").removeClass("hide");
    	}else{
    		$(".git-config").addClass("hide");
    	}
    });
    //git-higher
    $("#git-higher").click(function(){
    	$(".git-higher-config").removeClass("hide");
    	$("#git-higher").hide();
    });
    //maven
    $(".ant").hide();
    $(".maven").hide();
    $("#addciStep").change(function(){
    	var addciStep = $("#addciStep").val();
    	if(addciStep == 1){
    		$(".maven").show();
    		$(".ant").hide();
    	}else if(addciStep == 2){
    		$(".ant-config").show();
    		$(".maven").hide();
    	}else{
    		$(".maven").hide();
    		$(".ant").hide();
    	}
    	
    });
    //ant-higher
    $("#ant-higher").click(function(){
    	$("#ant-higher").hide();
    	$(".ant-higher").show();
    });
    
});

