$(document).ready(function () {
	$("#ciReloadBtn").click(function(){
		window.location.reload();
	});
	loadCi();
    registerConstructCiEvent();
    
    //ci and ci-code 列表切换
    $(".ci-code-content").hide();
    $(document).on('click','#ciTab',function(){
    	loadCi();
    	$(".ci-code-content").hide();
    	$(".ci-content").show();
    	$("#ciCodeTab").removeClass("active");
    	$("#ciTab").addClass("active");
    });
    $(document).on('click','#ciCodeTab',function(){
    	loadCiCode();
    	$(".ci-code-content").show();
    	$(".ci-content").hide();
    	$("#ciCodeTab").addClass("active");
    	$("#ciTab").removeClass("active");
    });
});


function registerConstructCiEvent(){
	
	$(document).on('click','.bj-green',function(){
//	$(".bj-green").unbind("click").click(function(){
		if($(this).attr("constructionStatus")=="2"){
			return;
		}
		debugger;
		var $this = $(this);
		var id = $this.attr("ciId");
		var cStatusHtml = "<i class='fa_success'></i>"+
		        "构建中"+
		        "<img src='"+ctx+"/images/loading4.gif' alt=''/>";
		$this.parent().parent().find(".cStatusColumn").html(cStatusHtml);
		$this.css("cursor","no-drop");
		$(this).unbind("click");
		$.ajax({
			url:ctx+"/ci/constructCi.do?id="+id,
			async:true,
			success:function(data){
				data = eval("(" + data + ")");
				if(data.status=="200"){
					window.location.reload();
				}else{
					layer.alert(data.msg);
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
							if (row.constructionStatus == 2 ) {
								btnCursorClass = 'cursor-no-drop';
							}
							var html = '&nbsp;&nbsp;&nbsp;&nbsp;<span class="bj-green '+btnCursorClass+'" data-toggle="tooltip" data-placement="right" title="构建" '+
											'data-original-title="重新构建" constructionStatus="'+row.constructionStatus+'"  ciId="'+row.id+'">'+
											'<i class="fa fa-arrow-circle-right"></i>'+
										'</span>';
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
							var html = row.constructionFailDate;
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
							if (row.constructionStatus == 2 ) {
								btnCursorClass = 'cursor-no-drop';
							}
							var html = '&nbsp;&nbsp;&nbsp;&nbsp;<span class="bj-green '+btnCursorClass+'" data-toggle="tooltip" data-placement="right" title="构建" '+
											'data-original-title="重新构建" constructionStatus="'+row.constructionStatus+'"  ciId="'+row.id+'">'+
											'<i class="fa fa-arrow-circle-right"></i>'+
										'</span>';
							return html;
						}
					}
		]
	})					
}

