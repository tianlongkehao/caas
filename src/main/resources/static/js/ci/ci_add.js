$(document).ready(function () {
    //codeType
	$(".git-config").hide();
    $("#codeType").change(function(){
    	var codeType = $("#codeType").val();
    	if(codeType == 1){
    		$(".git-config").show();
    		$("#git-higher").show();
    		$(".git-higher").hide();
    	}else{
    		$(".git-config").hide();
    		$(".git-higher").hide();
    	}
    });
    //git-higher
    $("#git-higher").click(function(){
    	$(".git-higher").show();
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
    		$("#ant-higher").show();
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

