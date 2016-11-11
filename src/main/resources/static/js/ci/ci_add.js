$(document).ready(function () {
    
	$("#buildBtn").click(function(){
        if(checkCodeCiAdd()) {
        	$("#buildForm").submit();
        	layer.load(0, {shade: [0.3, '#000']});
        }
        return false;
    });
	
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

function checkCodeCiAdd(){
	//项目名称的判断
	var projectName = $("#projectName").val();
	if(!projectName || projectName.length < 1){
      layer.tips('项目名称不能为空','#projectName',{tips: [1, '#3595CC']});
      $('#projectName').focus();
      return;
    }
    if(projectName.search(/^[a-z][a-z0-9-_]*$/) === -1){
      layer.tips('项目名称只能由小写字母、数字及横线下划线组成，且首字母只能为字母。','#projectName',{tips: [1, '#3595CC'],time: 3000});
      $('#projectName').focus();
      return;
    }
    
    //项目描述的判断
    var description = $("#description").val();
    if (!description || description.length < 1) {
    	layer.tips('项目简介不能为空','#description',{tips:[1,'#3595CC']});
    	$("#description").focus();
    	return;
    }
    
    //判断代码库类型
    var codeType = $("#codeType").val();
    if (codeType == 1) {
    	//判断代码仓库地址
    	var codeUrl = $("#codeUrl").val();
    	if(!codeUrl || codeUrl.length < 1){
    		layer.tips('代码仓库地址不能为空','#codeUrl',{tips: [1, '#3595CC']});
    		$('#codeUrl').focus();
    		return;
	    }
    	//判断代码分支
    	var codeBranch = $("#codeBranch").val();
    	if(!codeBranch || codeBranch.length < 1){
    		layer.tips('代码分支不能为空','#codeBranch',{tips: [1, '#3595CC']});
    		$('#codeBranch').focus();
    		return;
	    }
    }
    if (codeType == 0) {
    	$("#codeUrl").val("");
    	$("#codeBranch").val("");
    	$("#codeCredentials").val("");
    	$("#codeUsername").val("");
    	$("#codePassword").val("");
    }
    
    //判断构建
    var invokeType = $("#addciStep").val();
    //maven构建的判断
    if (invokeType == 1) {
    	//maven目标判断
    	var mavenGoals = $("#mavenGoals").val();
    	if(!mavenGoals || mavenGoals.length < 1){
    		layer.tips('目标不能为空','#mavenGoals',{tips: [1, '#3595CC']});
    		$('#mavenGoals').focus();
    		return;
	    }
    	//pom路径判断
    	var pomLocation = $("#pomLocation").val();
    	if(!pomLocation || pomLocation.length < 1){
    		layer.tips('pom路径不能为空','#pomLocation',{tips: [1, '#3595CC']});
    		$('#pomLocation').focus();
    		return;
	    }
    	//maven属性判断
    	var mavenProperty = $("#mavenProperty").val();
    	if(!mavenProperty || mavenProperty.length < 1){
    		layer.tips('maven属性不能为空','#mavenProperty',{tips: [1, '#3595CC']});
    		$('#mavenProperty').focus();
    		return;
	    }
    	//mvnJVM选项判断
    	var mavenJVMOptions = $("#mavenJVMOptions").val();
    	if(!mavenJVMOptions || mavenJVMOptions.length < 1){
    		layer.tips('maven属性不能为空','#mavenJVMOptions',{tips: [1, '#3595CC']});
    		$('#mavenJVMOptions').focus();
    		return;
	    }
    	//maven存储库的判断
    	if($("#isUserPrivateRegistry").prop("checked")==true){
    		$("#isUserPrivateRegistry").val("1");
        }
    	//注入建立变量的判断
    	if($("#injectBuildVariables").prop("checked")==true){
    		$("#injectBuildVariables").val("1");
        }
    	//daockerfile文件路径的判断
    	var dockerFileLocation = $("#mvnDockerFileLocation").val();
    	if(!dockerFileLocation || dockerFileLocation.length < 1){
    		layer.tips('dockerfile文件路径不能为空','#mvnDockerFileLocation',{tips: [1, '#3595CC']});
    		$('#mvnDockerFileLocation').focus();
    		return;
	    }
    	$("#dockerFileLocation").val(dockerFileLocation);
    	$("#antVersion").val("");
    	$("#antTargets").val("");
    	$("#antBuildFileLocation").val("");
    	$("#antProperties").val("");
    	$("#antJavaOpts").val("");
    }
    //ant构建
    if (invokeType == 2) {
    	//ant目标的判断
    	var antTargets = $("#antTargets").val();
    	if(!antTargets || antTargets.length < 1){
    		layer.tips('dockerfile文件路径不能为空','#antTargets',{tips: [1, '#3595CC']});
    		$('#antTargets').focus();
    		return;
	    }
    	//doackerFile文件路径的判断
    	var dockerFileLocation = $("#antDockerFileLocation").val();
    	if(!dockerFileLocation || dockerFileLocation.length < 1){
    		layer.tips('dockerfile文件路径不能为空','#antDockerFileLocation',{tips: [1, '#3595CC']});
    		$('#antDockerFileLocation').focus();
    		return;
	    }
    	$("#dockerFileLocation").val(dockerFileLocation);
    	$("#mavenVersion").val("");
    	$("#mavenGoals").val("");
    	$("#pomLocation").val("");
    	$("#mavenProperty").val("");
    	$("#mavenJVMOptions").val("");
    	$("#isUserPrivateRegistry").val("");
    	$("#injectBuildVariables").val("");
    	$("#mavenGlobalSetFile").val("");
    }
    //构建方式为none
    if (invokeType == 0) {
    	$("#mavenVersion").val("");
    	$("#mavenGoals").val("");
    	$("#pomLocation").val("");
    	$("#mavenProperty").val("");
    	$("#mavenJVMOptions").val("");
    	$("#isUserPrivateRegistry").val("");
    	$("#injectBuildVariables").val("");
    	$("#mavenGlobalSetFile").val("");
    	
    	$("#antVersion").val("");
    	$("#antTargets").val("");
    	$("#antBuildFileLocation").val("");
    	$("#antProperties").val("");
    	$("#antJavaOpts").val("");
    	
    	$("#dockerFileLocation").val("");
    }
    
    return true;
}

