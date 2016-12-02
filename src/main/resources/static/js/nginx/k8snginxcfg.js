 $(document).ready(function () {
	 //添加多个service
	 $(".fa-minus:last").hide();
	 $(document).on('click','.addSerBtn',function(){
		var serHtml = '<div class="nginx-label col-md-offset-1">'+
		'<span>server</span><input type="text" class="ipAndUpstreamPort" name="ipAndUpstreamPort" value="">;<i class="fa fa-plus addSerBtn"></i><i class="fa fa-minus delSerBtn"></i>'+
		'</div>';
		$("#nginx-sers").append(serHtml);
		$(".fa-plus:not(':last')").hide();
		$(".fa-minus:not(':last')").show();
		$(".fa-minus:last").hide();
	});
	//与填写的端口号保持一致
	var ListenPort = $("#ListenPort").val();
	$(".sameToListenPort").val(ListenPort);
	$("#ListenPort").blur(function(){
		ListenPort = $("#ListenPort").val();
		$(".sameToListenPort").val(ListenPort);
	});
	//proxy_pass的内容是appName+Namespace拼成的
	var appNameAndNamespace = $("#appNameAndNamespace").val();
	$("#proxy_pass").val('http://'+appNameAndNamespace);
	
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
    
    
    
	
	
 });/*reday*/
 
