 $(document).ready(function () {
	 $(".fa-minus:last").hide();
	 $(document).on('click','.addSerBtn',function(){
		var serHtml = '<div class="nginx-label col-md-offset-1">'+
		'<span>server</span><input type="text" class="ip_hash" name="ip_hash" value="192.168.0.81:31586">;<i class="fa fa-plus addSerBtn"></i><i class="fa fa-minus delSerBtn"></i>'+
		'</div>';
		$("#nginx-sers").append(serHtml);
		$(".fa-plus:not(':last')").hide();
		$(".fa-minus:not(':last')").show();
		$(".fa-minus:last").hide();
	});
	var ListenPort = $("#ListenPort").val();
	$(".sameToListenPort").val(ListenPort);
	$("#ListenPort").blur(function(){
		ListenPort = $("#ListenPort").val();
		$(".sameToListenPort").val(ListenPort);
	});
	
	var appNameAndNamespace = $("#appNameAndNamespace").val();
	$("#proxy_pass").val('http://'+appNameAndNamespace);
	
	
	
 });/*reday*/
 
