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
    var count = 0;
    function loadAnt(count){
    	var antHtml = '<div class="row addCiStepRow ant">'+
    	'<div class="col-sm-12">'+
    	'<div class="ibox float-e-margins">'+
    		'<div class="ibox-title">'+
    			'<h5>ant</h5>'+
    			'<div class="ibox-tools">'+
    				'<a class="collapse-link">'+
                        '<i class="fa fa-chevron-up"></i>'+
                    '</a>'+
    				'<a class="close-link">'+
                        '<i class="fa fa-times"></i>'+
                    '</a>'+
    			'</div>'+
    		'</div>'+
    		'<div class="ibox-content">'+
    			'<div class="row ant-config">'+
                	'<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">ant版本</label>'+
                        '<select id="antVersion-'+count+'" name="antVersion" class="form-control c-project-con" >'+
                        	'<option value="default">default</option>'+
                            '<option value="ant">ant</option>'+
                        '</select>'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                       '<label class="c-project-tit">目标</label>'+
                        '<input id="antTargets-'+count+'" name="antTargets" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                '</div>'+
                '<div class="row ant-config">'+
                	'<button class="ant-higherBtn" type="button" style="float:right!important">高级...</button>'+
                '</div>'+
                '<div class="row ant-higherCon hide">'+
                	'<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">构建文件</label>'+
                        '<input id="antBuildFileLocation-'+count+'" name="antBuildFileLocation" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">性能</label>'+
                        '<input id="antProperties-'+count+'" name="antProperties" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">java选项</label>'+
                        '<input id="antJavaOpts-'+count+'" name="antJavaOpts" type="text" class="form-control c-project-con" value="">'+
                    '</div></div></div></div></div></div>';
    	return antHtml;
    }
    
    function loadMaven(count){
    	var mavenHtml = '<div class="row addCiStepRow maven">'+
    	'<div class="col-sm-12">'+
    	'<div class="ibox float-e-margins">'+
    		'<div class="ibox-title">'+
    			'<h5>maven</h5>'+
    			'<div class="ibox-tools">'+
    				'<a class="collapse-link">'+
                        '<i class="fa fa-chevron-up"></i>'+
                    '</a>'+
    				'<a class="close-link">'+
                        '<i class="fa fa-times"></i>'+
                    '</a>'+
    			'</div>'+
    		'</div>'+
    		'<div class="ibox-content">'+
    			'<div class="row">'+
                	'<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">maven版本</label>'+
                        '<select id="mavenVersion-'+count+'" name="mavenVersion" class="form-control c-project-con" >'+
                        	'<option value="default">default</option>'+
                            '<option value="maven">maven</option>'+
                        '</select>'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">目标</label>'+
                        '<input id="mavenGoals-'+count+'" name="mavenGoals" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="row maven-config">'+
	                	'<button class="maven-higherBtn" type="button" style="float:right!important">高级...</button>'+
	                '</div>'+
	                
                    '<div class="maven-higherCon hide"><div class="form-group col-md-12">'+
                        '<label class="c-project-tit">POM</label>'+
                        '<input id="pomLocation-'+count+'" name="pomLocation" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">属性</label>'+
                        '<input id="mavenProperty-'+count+'" name="mavenProperty" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">JVM选项</label>'+
                        '<input id="mavenJVMOptions-'+count+'" name="mavenJVMOptions" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<div style="line-height:34px">'+
                            '<label class="c-project-tit" title="使用私人maven存储库">使用私人maven存储库</label>'+
                            '<input type="checkbox" id = "isUserPrivateRegistry" name = "isUserPrivateRegistry-'+count+'" value = "0">'+
                        '</div>'+
                    '</div>'+
                     '<div class="form-group col-md-12">'+
                        '<div style="line-height:34px">'+
                            '<label class="c-project-tit">注入建立变量</label>'+
                            '<input type="checkbox"  id="injectBuildVariables" name="injectBuildVariables-'+count+'" value="0">'+
                        '</div>'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">配置文件</label>'+
                        '<select id="mavenSetFile-'+count+'" name="mavenSetFile" class="form-control c-project-con" >'+
                        	'<option value="use default maven setting">use default maven setting</option>'+
                            '<option value="settings file in filesystem">settings file in filesystem</option>'+
                        '</select>'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">全局配置文件</label>'+
                        '<select id="mavenGlobalSetFile-'+count+'" name="mavenGlobalSetFile" class="form-control c-project-con" >'+
                        	'<option value="use default maven global setting">use default maven global setting</option>'+
                            '<option value="global settings file in filesystem">global settings file in filesystem</option>'+
                        '</select>'+
                    '</div>'+
                    '</div></div></div></div></div></div>';
    	return mavenHtml;
    }
    
    
    function loadShell(count){
    	shellHtml = '<div class="row addCiStepRow shell">'+
    	'<div class="col-sm-12">'+
    	'<div class="ibox float-e-margins">'+
    		'<div class="ibox-title">'+
    			'<h5>Execute shell</h5>'+
    			'<div class="ibox-tools">'+
    				'<a class="collapse-link">'+
                        '<i class="fa fa-chevron-up"></i>'+
                    '</a>'+
    				'<a class="close-link">'+
                        '<i class="fa fa-times"></i>'+
                    '</a>'+
    			'</div>'+
    		'</div>'+
    		'<div class="ibox-content">'+
    			'<div class="row">'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">命令</label>'+
                        '<textarea id="executeShell-'+count+'" name="executeShell" class="form-control c-project-con" type="text" row="5"></textarea>'+
                    '</div></div></div></div></div></div>';
    	return shellHtml;
    }
    
    //构建maven
    $("#maven").click(function(){
    	count++;
    	var maven = loadMaven(count);
    	$("#sortable").append(maven);
    });
    
    //maven高级按钮选项
    $(document).on('click','.maven-higherBtn',function(){
    	$(this).parent().hide();
    	$(this).parent().next('.maven-higherCon').removeClass("hide");
    });
    
    //构建ant
    $("#ant").click(function(){
    	count++;
    	var ant = loadAnt(count);
    	$("#sortable").append(ant);
    });
    
    //ant高级按钮选项
    $(document).on('click','.ant-higherBtn',function(){
    	$(this).parent().hide();
    	$(this).parent().next('.ant-higherCon').removeClass("hide");
    });
    
    //构建shell
    $("#shell").click(function(){
    	count++;
    	var shell = loadShell(count);
    	$("#sortable").append(shell);
    });
    
    //折叠ibox
    $(document).on('click','.collapse-link',function(){
        var ibox = $(this).closest('div.ibox');
        var button = $(this).find('i');
        var content = ibox.find('div.ibox-content');
        content.slideToggle(200);
        button.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
        ibox.toggleClass('').toggleClass('border-bottom');
        setTimeout(function () {
            ibox.resize();
            ibox.find('[id^=map-]').resize();
        }, 50);
        ibox.css("border-bottom","1px solid #dadada");
    });

    //关闭ibox
    $(document).on('click','.close-link',function(){
        var content = $(this).closest('div.ibox');
        content.remove();
    });
    
    //拖拽
    $( "#sortable" ).sortable({
        revert: true
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
//    	var mavenProperty = $("#mavenProperty").val();
//    	if(!mavenProperty || mavenProperty.length < 1){
//    		layer.tips('maven属性不能为空','#mavenProperty',{tips: [1, '#3595CC']});
//    		$('#mavenProperty').focus();
//    		return;
//	    }
    	//mvnJVM选项判断
//    	var mavenJVMOptions = $("#mavenJVMOptions").val();
//    	if(!mavenJVMOptions || mavenJVMOptions.length < 1){
//    		layer.tips('maven属性不能为空','#mavenJVMOptions',{tips: [1, '#3595CC']});
//    		$('#mavenJVMOptions').focus();
//    		return;
//	    }
    	
    	//maven存储库的判断
    	if($("#isUserPrivateRegistry").prop("checked")==true){
    		$("#isUserPrivateRegistry").val("1");
        }
    	//注入建立变量的判断
    	if($("#injectBuildVariables").prop("checked")==true){
    		$("#injectBuildVariables").val("1");
        }
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
    
  //doackerFile文件路径的判断
	var dockerFileLocation = $("#dockerFileLocation").val();
	if(!dockerFileLocation || dockerFileLocation.length < 1){
		layer.tips('dockerfile文件路径不能为空','#dockerFileLocation',{tips: [1, '#3595CC']});
		$('#dockerFileLocation').focus();
		return;
    }
    
    return true;
}

