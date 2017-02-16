$(document).ready(function () {
	$("#ciReloadBtn").click(function(){
		/*window.location.reload();*/
		window.location.href = ctx + "/registry/1";
	});
	$(".ci-code-content").hide();
	
	loadList();
	
    registerConstructCiEvent();
    
    //ci and ci-code 列表切换
    
    $(document).on('click','#ciTab',function(){
    	ciClick();
    });
    $(document).on('click','#ciCodeTab',function(){
    	cicodeClick();
    });
});

function loadList(){
	var locationUrl = window.location.href;
	var str1 = "code";
	if(locationUrl.indexOf(str1) != -1){
		cicodeClick();
	}else{
		ciClick();
	}
}
function ciClick(){
	loadCi();
	$(".ci-code-content").hide();
	$(".ci-content").show();
	$("#ciCodeTab").removeClass("active");
	$("#ciTab").addClass("active");
}
function cicodeClick(){
	
	$(".ci-code-content").show();
	$(".ci-content").hide();
	$("#ciCodeTab").addClass("active");
	$("#ciTab").removeClass("active");
	loadCiCode();
}


function addCiInfo(type) {
	$.ajax({
		url:ctx+"/ci/judgeUserImages.do",
		async:false,
		success:function(data){
			data = eval("(" + data + ")");
			if(data.overwhelm){
				layer.open({
			        title: '提示',
			        content: '镜像个数已用完，是否删除无用镜像',
			        btn: ['确定', '取消'],
			        yes: function(index, layero){ //或者使用btn1
			        	window.location.href = ctx + "/registry/1";
			        }
				});				
			}else{
				if (type == 0) {
					$.ajax({
						url:ctx + "/ci/judgeShera.do",
						async : false,
						success : function(data){
							data = eval("("+data+")");
							if (data.status == "200") {
								window.location.href = ctx + "/ci/add";
							}
							if (data.status == "400") {
								layer.open({
							        title: '提示',
							        content: '当前租户没有添加shera环境，请您联系管理员',
							        btn: ['确定', '取消']
								});		
							}
						}
					})
				}
				if (type == 1) {
					window.location.href = ctx + "/ci/addCodeSource";
				}
				if (type ==2) {
					window.location.href = ctx + "/ci/uploadImage";
				}
				if (type ==3) {
					window.location.href = ctx + "/ci/dockerfile";
				}
			}
		}
	});	
}

function registerConstructCiEvent(){
	//停止构建
	$(document).on('click','.stop-ci',function(){
		var $this = $(this);
		var id = $this.attr("ciId");
		$.ajax({
			url:ctx+"/ci/stopCi.do?id="+id,
			async:true,
			success:function(data){
				data = eval("(" + data + ")");
				if(data.status=="200"){
					if(data.ci.type == "1"){
   					 	window.location.href = ctx+"/ci?code";
	   				 }else{
	   					 window.location.href = ctx+"/ci";
	   				 }
				}else{
					layer.alert("停止构建失败");
				}
			}
		});
	})
	
	$(document).on('click','.bj-green',function(){
//	$(".bj-green").unbind("click").click(function(){
		if($(this).attr("constructionStatus")=="2"){
			return;
		}
		var $this = $(this);
		var id = $this.attr("ciId");
		var cStatusHtml = "<i class='fa_success'></i>"+
		        "构建中"+
		        "<img src='"+ctx+"/images/loading4.gif' alt=''/>";
		$this.parent().parent().find(".cStatusColumn").html(cStatusHtml);
		$this.css("cursor","no-drop");
		$this.find("i").removeClass("bj-no-drop");
		$this.next().removeClass("hide");
		$(this).unbind("click");
		$.ajax({	
			url:ctx+"/ci/constructCi.do?id="+id,
			async:true,
			success:function(data){
				data = eval("(" + data + ")");
				if(data.status=="200"){
					if(data.ci.type == "1"){
   					 	window.location.href = ctx+"/ci?code";
	   				 }else{
	   					 window.location.href = ctx+"/ci";
	   				 }
//					window.location.reload();
				}else{
					layer.alert("构建失败");
				}
				setTimeout('window.location.reload()',2000);
			}
		});
		/*layer.open({
	        title: '构建镜像',
	        content: '确定构建镜像？',
	        btn: ['确定', '取消'],
	        yes: function(index, layero){ //或者使用btn1
	        	
	        },
	        cancel: function(index){ //或者使用btn2
	        }
	    });*/
	});
}

function loadCi() {
	$('.dataTables-example1').dataTable({
	 	"aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0 ,5] }],
	 	"autoWidth": false,
        "processing": true,
        "serverSide": true,
        "stateSave":false,
        "ordering":false,
        "ajax": ctx+"/ci/page.do",
        "columns": [
					{   
						data : null,
						render : function ( data, type, row ) {
							var html = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="'+ctx+'/ci/detail/'+row.id+'" title="查看详细信息">'+row.projectName+'</a>';
							return html;
						}
					},
					
					{
						data : null,
						className : 'cStatusColumn',
						render : function (data,type,row) {
							var html = '';
							if (row.constructionStatus == 1) {
								html = '<i class="fa_stop"></i>' +
									'未构建 <img src="'+ctx+'/images/loading4.gif"'+
									'alt="" class="hide" />';
							}
							if (row.constructionStatus == 2) {
								html = '<i class="fa_success"></i>' +
									'构建中 <img src="'+ctx+'/images/loading4.gif"'+
									'alt="" class="" />';
							}
							if (row.constructionStatus == 3) {
								html = '<i class="fa_success"></i>' +
									'成功 <img src="'+ctx+'/images/loading4.gif"'+
									'alt="" class="hide" />';
							}
							if (row.constructionStatus == 4) {
								html = '<i class="fa_stop"></i>' +
									'失败<img src="'+ctx+'/images/loading4.gif"'+
									'alt="" class="hide" />';
							}
							return html;
						}
					
					},
					{
						data : null,
						render : function (data,type,row) {
							var html = row.constructionDate;
							return html;
						}
					},
					{
						data : null,
						render : function (data,type,row) {
							var html ="&nbsp;&nbsp;&nbsp;"+ Math.round(row.constructionTime/1000) + "s";
							return html;
						}
					},
					{
						data : null,
						render : function (data,type,row) {
							var html = '';
							if (row.imgId == null || row.imgId == 0) {
								html += '<a target="_blank" title="" class="cursor-no-drop">'+row.imgNameFirst+'/'+row.imgNameLast+':'+row.imgNameVersion+'</a>';
							}
							else {
								html += '<a target="_blank" title="" class="" href="'+ctx+'/registry/detail/'+row.imgId +'">'+row.imgNameFirst+'/'+row.imgNameLast+':'+row.imgNameVersion+'</a>';
							}
							return html;
						}
					},
					{
						data : null,
						render : function (data,type,row) {
							var btnCursorClass = '';
							var html = '';
							if (row.constructionStatus == 2 ) {
								btnCursorClass = 'cursor-no-drop';
								html = '&nbsp;&nbsp;&nbsp;&nbsp;<span class="bj-green '+btnCursorClass+'" data-toggle="tooltip" data-placement="right" title="构建" '+
												'data-original-title="重新构建" constructionStatus="'+row.constructionStatus+'"  ciId="'+row.id+'">'+
												'<i class="fa fa-arrow-circle-right bj-no-drop"></i>'+
											'</span>';
							}
							else {
								html = '&nbsp;&nbsp;&nbsp;&nbsp;<span class="bj-green '+btnCursorClass+'" data-toggle="tooltip" data-placement="right" title="构建" '+
											'data-original-title="重新构建" constructionStatus="'+row.constructionStatus+'"  ciId="'+row.id+'">'+
											'<i class="fa fa-arrow-circle-right bj-no-drop"></i>'+
										'</span>';
							}
							return html;
						}
					}
		]
	})					
}
function loadCiCode() {
	$('.dataTables-example').dataTable({
	 	"aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0 ,6] }],
	 	"autoWidth": false,
        "processing": true,
        "serverSide": true,
        "stateSave":false,
        "ordering":false,
        "ajax": ctx+"/ci/codepage.do",
        "columns": [
					{   
						data : null,
						render : function ( data, type, row ) {
							var html = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#"><span class="fa_level fa_level_e">E</span></a>';
							return html;
						}
					},
					{   
						data : null,
						render : function ( data, type, row ) {
							var html = '<a href="'+ctx+'/ci/detail/'+row.id+'" title="查看详细信息">'+row.projectName+'</a>';
							return html;
						}
					},
					
					{
						data : null,
						className : 'cStatusColumn',
						render : function (data,type,row) {
							var html = '';
							if (row.constructionStatus == 1) {
								html = '<i class="fa_stop"></i>' +
									'未构建 <img src="'+ctx+'/images/loading4.gif"'+
									'alt="" class="hide" />';
							}
							if (row.constructionStatus == 2) {
								html = '<i class="fa_success"></i>' +
									'构建中 <img src="'+ctx+'/images/loading4.gif"'+
									'alt="" class="" />';
							}
							if (row.constructionStatus == 3) {
								html = '<i class="fa_success"></i>' +
									'成功 <img src="'+ctx+'/images/loading4.gif"'+
									'alt="" class="hide" />';
							}
							if (row.constructionStatus == 4) {
								html = '<i class="fa_stop"></i>' +
									'失败<img src="'+ctx+'/images/loading4.gif"'+
									'alt="" class="hide" />';
							}
							return html;
						}
					
					},
					{
						data : null,
						render : function (data,type,row) {
							var html = row.constructionDate;
							if (html == null || html == "") {
								return "无";
							}
							return html;
						}
					},
					{
						data : null,
						render : function (data,type,row) {
							var html = row.constructionFailDate;
							if (html == null || html == "") {
								return "无";
							}
							return html;
						}
					},
					{
						data : null,
						render : function (data,type,row) {
							var html ="&nbsp;&nbsp;&nbsp;"+ Math.round(row.constructionTime/1000) + "s";
							return html;
						}
					},
					{
						data : null,
						render : function (data,type,row) {
							var btnCursorClass = '';
							var html = '';
							if (row.constructionStatus == 2 ) {
								btnCursorClass = 'cursor-no-drop';
								html = '&nbsp;&nbsp;&nbsp;&nbsp;<span class="bj-green '+btnCursorClass+'" data-toggle="tooltip" data-placement="right" title="构建" '+
												'data-original-title="重新构建" constructionStatus="'+row.constructionStatus+'"  ciId="'+row.id+'">'+
												'<i class="fa fa-arrow-circle-right "></i>'+
											'</span>'+
											'&nbsp;&nbsp;&nbsp;<span class="stop-ci" style="cursor:pointer" title="停止构建" ciId="'+row.id+'" ><i class="fa fa-power-off ci-powerOff"></i></span>';
							}else{
								html = '&nbsp;&nbsp;&nbsp;&nbsp;<span class="bj-green '+btnCursorClass+'" data-toggle="tooltip" data-placement="right" title="构建" '+
													'data-original-title="重新构建" constructionStatus="'+row.constructionStatus+'"  ciId="'+row.id+'">'+
													'<i class="fa fa-arrow-circle-right bj-no-drop"></i>'+
											'</span>'+
											'&nbsp;&nbsp;&nbsp;<span class="stop-ci hide" style="cursor:pointer" title="停止构建" ciId="'+row.id+'"><i class="fa fa-power-off ci-powerOff"></i></span>';
							}
							return html;
						}
					}
		]
	})					
}
function overImg(obj){
	var redImage = ctx +"/images/dockerfile-red.png";
	$(obj).attr("src",redImage);
}
function outImg(obj){
	var grayImage = ctx +"/images/dockerfile-gray.png";
	$(obj).attr("src",grayImage);
}

