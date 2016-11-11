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
                        '<select id="antVersion-'+count+'" name="antVersion-'+count+'" class="form-control c-project-con" >'+
                        	'<option value="default">default</option>'+
                            '<option value="ant">ant</option>'+
                        '</select>'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                       '<label class="c-project-tit">目标</label>'+
                        '<input id="antTargets-'+count+'" name="antTargets-'+count+'" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                '</div>'+
                '<div class="row ant-config">'+
                	'<button class="ant-higherBtn" type="button" style="float:right!important">高级...</button>'+
                '</div>'+
                '<div class="row ant-higherCon">'+
                	'<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">构建文件</label>'+
                        '<input id="antBuildFileLocation-'+count+'" name="antBuildFileLocation-'+count+'" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">性能</label>'+
                        '<input id="antProperties-'+count+'" name="antProperties-'+count+'" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">java选项</label>'+
                        '<input id="antJavaOpts-'+count+'" name="antJavaOpts-'+count+'" type="text" class="form-control c-project-con" value="">'+
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
                        '<select id="mavenVersion-'+count+'" name="mavenVersion-'+count+'" class="form-control c-project-con" >'+
                        	'<option value="default">default</option>'+
                            '<option value="maven">maven</option>'+
                        '</select>'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">目标</label>'+
                        '<input id="mavenGoals-'+count+'" name="mavenGoals-'+count+'" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">POM</label>'+
                        '<input id="pomLocation-'+count+'" name="pomLocation-'+count+'" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">属性</label>'+
                        '<input id="mavenProperty-'+count+'" name="mavenProperty-'+count+'" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">JVM选项</label>'+
                        '<input id="mavenJVMOptions-'+count+'" name="mavenJVMOptions-'+count+'" type="text" class="form-control c-project-con" value="">'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<div style="line-height:34px">'+
                            '<label class="c-project-tit" title="使用私人maven存储库">使用私人maven存储库</label>'+
                            '<input type="checkbox" id = "isUserPrivateRegistry-'+count+'" name = "isUserPrivateRegistry-'+count+'" value = "0">'+
                        '</div>'+
                    '</div>'+
                     '<div class="form-group col-md-12">'+
                        '<div style="line-height:34px">'+
                            '<label class="c-project-tit">注入建立变量</label>'+
                            '<input type="checkbox"  id="injectBuildVariables-'+count+'" name="injectBuildVariables-'+count+'" value="0">'+
                        '</div>'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">配置文件</label>'+
                        '<select id="mavenSetFile-'+count+'" name="mavenSetFile-'+count+'" class="form-control c-project-con" >'+
                        	'<option value="use default maven setting">use default maven setting</option>'+
                            '<option value="settings file in filesystem">settings file in filesystem</option>'+
                        '</select>'+
                    '</div>'+
                    '<div class="form-group col-md-12">'+
                        '<label class="c-project-tit">全局配置文件</label>'+
                        '<select id="mavenGlobalSetFile-'+count+'" name="mavenGlobalSetFile-'+count+'" class="form-control c-project-con" >'+
                        	'<option value="use default maven global setting">use default maven global setting</option>'+
                            '<option value="global settings file in filesystem">global settings file in filesystem</option>'+
                        '</select>'+
                    '</div>'+
                    '</div></div></div></div></div>';
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
                        '<textarea id="executeShell-'+count+'" name="executeShell-'+count+'" class="form-control c-project-con" type="text" row="5"></textarea>'+
                    '</div></div></div></div></div></div>';
    	return shellHtml;
    }
    
    
    $("#maven").click(function(){
    	count++;
    	var maven = loadMaven(count);
    	$("#sortable").append(maven);
    });
    $("#ant").click(function(){
    	count++;
    	var ant = loadAnt(count);
    	$("#sortable").append(ant);
    	$(".ant-higherBtn").show();
		$(".ant-higherCon").hide();
    });
    $("#shell").click(function(){
    	count++;
    	var shell = loadShell(count);
    	$("#sortable").append(shell);
    });
    //ant-higher
    $(document).on('click','.ant-higherBtn',function(){
    	$(".ant-higherBtn").hide();
    	$(".ant-higherCon").show();
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

