$(document).ready(function () {

	findImages();
    $(".images-panel").mouseover(function () {
        $(this).children(".create-item").css("opacity", "1");
    });

    $(".images-panel").mouseout(function () {
        $(this).children(".create-item").css("opacity", "0");
    });
   
    $(".list_info").click(function () {
        $(".table_list>.list_info").removeClass("active");
        $(".table_list").siblings("section").addClass("hide");
        $(this).addClass("active").parent().siblings("section").eq($(this).index()).removeClass("hide");
    });
    
    $(".fork").click(function(){
    	var imageId = $("#imageId").val();
    	var _this = $(this);
    	$.ajax({
    		url:""+ctx+"/registry/detail/favor",
    		type:"post",
    		data:{"imageId":imageId},
    		success:function(data){
    			if(data == "success"){
    				$("#collectTxt").text("已收藏");
    				$(".star-style").removeClass("fa-star-o").addClass("fa-star");
    				_this.addClass('live');
    				layer.msg( "收藏成功。", {
							icon: 1
					});
    			}else{
    				$("#collectTxt").text("收藏");
    				$(".star-style").removeClass("fa-star").addClass("fa-star-o");
    				_this.removeClass('live');
    				layer.msg( "取消收藏。", {
							icon: 0.5
					});
    			}
    		}
    	});
    });
    
	// 快速收藏
    
    $(document).on("click",".forkquick",function(){
    
    	var imageId = $(this).attr('imageId');
    	var _this = $(this);
    	$.ajax({
    		async:false, 
    		url:""+ctx+"/registry/detail/favor",
    		type:"post",
    		data:{"imageId":imageId},
    		success:function(data){
    			if(data == "success"){
    				_this.children(".star-style").removeClass("fa-star-o").addClass("fa-star");
    				_this.addClass('live');
    				layer.msg( "收藏成功。", {
							icon: 1
					});
    			}else{
    				_this.children(".star-style").removeClass("fa-star").addClass("fa-star-o");
    				_this.removeClass('live');
    				layer.msg( "取消收藏。", {
							icon: 0.5
					});
    			}
    		}
    	});
    });
    
    // 导出镜像
    var downloadIndex;
    $(document).on("click",".downloadImage",function(){
    		var _this = $(this);
        	$.ajax({
    			async : false,
         		url:""+ctx+"/registry/judgeFileExist.do",
         		type:"post",
         		data:{
         			"imageName":$(this).attr("imagename"),
         			"imageVersion":$(this).attr("imageversion")
         		},
         		success:function(data){
         			data = eval("(" + data + ")");
         			if(data.status == "500"){ //判断有没有用户下载过该镜像，没有人下载过，页面加载一个遮罩层；
         				downloadIndex = layer.load(0, {shade: [0.3, '#000']});
         	        	$.ajax({
         	        		url:""+ctx+"/registry/downloadImage",
         	        		type:"get",
         	        		async:false, 
         	        		data:{
         	        			"imageName" : _this.attr("imagename"),
         	        			"imageVersion" : _this.attr("imageversion"),
         	        			"imgID" : _this.attr("imgID"),
         	        			"resourceName" :_this.attr("resourcename")
         	        		},
         	        		success:function(data){
         	        			layer.close(downloadIndex);
         	        			var data1 = eval("(" + data + ")");
         	        			if(data1.status == "200"){
         	        				window.location.href = ctx + "/registry/download?imageName="+_this.attr("imagename") +"&imageVersion="+_this.attr("imageversion");
         	        			} 
         	        			else {
           		   		     		layer.msg( "导出镜像失败", {
           		   						icon: 1
           		   					}); 
         	        			}
         	        		}
         	        	});
         			} else {
         				window.location.href = ctx +"/registry/download?imageName="+_this.attr("imagename") +"&imageVersion="+_this.attr("imageversion");
         			}
         		}
         	});
        });
});


function findImages(){
	var index = $("#nav2").val();
	var userId = $("#userId").val();
	$('.dataTables-example').dataTable({
		 	"aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0 ,6] }],
	        "processing": true,
	        "serverSide": true,
	        "ordering":false,
//	        "bStateSave":true,
	        "ajax": ctx+"/registry/pager/"+index,
	        "columns": [
	                    {   
	                    	data : null,
	                    	render : function ( data, type, row ) {
	                    		var html = '<input type="checkbox" class="chkItem" name="ids" style="margin-left:22px;"  value="'+row.id+'">'
	                    		return html;
	                    	}
	                    },
	                    { 	
	                    	data : null ,
	                    	render : function ( data, type, row ) {
	                    		var html = '<a href="'+ctx+'/registry/detail/'+row.id+'" title="查看详细信息" >'+row.name+'</a>'
	                    		return html;
	                    	}
	                    },
	                    { 
	                    	data : "version" ,
                    		render : function ( data, type, row ) {
	                    		if (data == null || data == "") {
	                    			return "";
	                    		}
	                    		return data;
	                    	}	
	                    },
	                    {	
	                    	data : "remark",
	                    	render : function ( data, type, row ) {
	                    		if (data == null || data == "") {
	                    			return "";
	                    		}
	                    		return data;
	                    	}
	                    	
	                    },
	                    { 
	                    	data : "creatorName" ,
                    		render : function ( data, type, row ) {
	                    		if (data == null || data == "") {
	                    			return "租户已注销";
	                    		}
	                    		return data;
	                    	}
	                    },
	                    { 
	                    	data : 'createTime',
	                    	render : function ( data, type, row ) {
	                    		var date = calendarFormat(data);
	                    		return date;
	                        }
	                    },
	                    { 	
	                    	data: null,
	                    	render: function ( data, type, row ) {
	                    		var html = "";
	                    		if (row.isDelete == 1 ) {
	                    			if (row.currUserFavor == 0) {
	                    				html += '<a class="no-drop a-oper forkquick" imageId="'+row.id+'">' + 
	                    						'<i class="fa fa-star-o star-style" style="color: #4280CB;margin-left:35px;"></i>'+
	                    					'</a>'
	                    			} else {
	                    				html += '<a class="no-drop a-oper forkquick" imageId="'+row.id+'">' + 
                									'<i class="fa fa-star star-style" style="color: #337ab7;margin-left:35px;"></i>'+
                								'</a>'
	                    			}
	                    		} else {
	                    			html += '<a class="no-drop" href="'+ctx+'/service/add?imageName='+row.name+'&imageVersion='+row.version+'&imgID='+row.id+'&resourceName='+row.resourceName+'"'+
										 		'imageversion="'+row.version+'" imagename="'+row.name+'" title="部署">'+
										 		'<i class="fa fa-wrench"></i>'+
										 	'</a>'+
										 	'<a class="no-drop a-oper downloadImage" imageversion="'+row.version+'" imagename="'+row.name+'" imgID="'+row.id+'" resourcename= "'+row.resourceName+'" title="导出">'+ 
										 		'<i class="fa fa-share-square-o" style="margin-left:10px;"></i>'+
										 	'</a>' 
							 		if (row.currUserFavor == 0) {
	                    				html += '<a class="no-drop a-oper forkquick" imageId="'+row.id+'">' + 
	                    						'<i class="fa fa-star-o star-style" style="color: #4280CB;margin-left:10px;"></i>'+
	                    					'</a>'
	                    			} else {
	                    				html += '<a class="no-drop a-oper forkquick" imageId="'+row.id+'">' + 
                									'<i class="fa fa-star star-style" style="color: #337ab7;margin-left:10px;"></i>'+
                								'</a>'
	                    			}
	                    			
	                    			if (userId == row.creator) {
	                    				html +=	'<a class="no-drop a-oper" href="javascript:void(0)" onclick="deleteImage(this)"'+
													'title="删除" imageversion="'+row.version+'" imagename="'+row.name+'" imageid="'+row.id+'">' +
													'<i class="fa fa-trash" style="margin-left:10px;"></i>'+
												'</a>'
	                    			}
	                    		}
	                    		
	                    		return html;
	                    	}
	                    }
	                ]
	  });
	
}

/*删除单个镜像*/
function deleteImage(obj){
	var imageId = $(obj).attr("imageid");
	layer.open({
	        title: '删除镜像',
	        content: '确定删除镜像？',
	        btn: ['确定', '取消'],
	        yes: function(index){ 
	        	layer.close(index);
	        	$.ajax({
   		     		url:""+ctx+"/registry/detail/deleteimage",
   		     		type:"post",
   		     		data:{"imageId":imageId},
   		     		success:function(data){
   		     			if(data == "ok"){
	   		     			layer.msg( "删除成功！", {
		   						icon: 1
		   					},function(){
		   						window.location.reload(true);
		   					});
   		     			} else {
   		   		     		layer.msg( "删除失败", {
   		   						icon: 1
   		   					});  		     				
   		     			}
   		     		},
   		     		error:function(){
	   		     		layer.msg( "删除失败", {
	   						icon: 1
	   					});
   		     		}
   		     	});
	        	refresh();
	        }
	 });
}

//批量删除镜像
function delImages(){
	obj = document.getElementsByName("ids");
	var imageIds = [];
    for (k in obj) {
        if (obj[k].checked) {
        	imageIds.push(obj[k].value);
        }
    }
    if (imageIds.length <=0) {
    	layer.msg( "请选择需要删除的镜像", {icon: 2 });
    	return;
    }
    layer.open({
        title: '删除镜像',
        content: '确定删除镜像？',
        btn: ['确定', '取消'],
        yes: function(index, layero){ 
        	layer.close(index);
			$.ajax({
				url:""+ctx+"/registry/delImages.do?imageIds="+imageIds,
				success:function(data){
					data = eval("(" + data + ")");
					if(data.status=="200"){
						layer.alert("镜像删除成功");
						window.location.reload(true);
					}else{
						layer.alert("镜像删除失败，请检查服务器连接");
					}

				}
			})
        }
 })
    
}

$(function(){

	// 镜像备注编辑
	$("#desEdit").on('click', function(){
		$(".list-content").addClass("hide");
		$("#contentArea").removeClass("hide");
		$(this).addClass("hide");
	});

	$("#desEditCancel").on("click", function () {
		$(".list-content").removeClass("hide");
		$("#contentArea").addClass("hide");
		$("#desEdit").removeClass("hide");
	});

	// 保存
	$("#desEditSave").on("click", function(){
		var summary = $("#desTextarea").val();
		var imageId = $("#imageId").val();
		var url = ""+ctx+"/registry/detail/summary";
		$.post(url, {'summary':summary,'imageId':imageId}, function(data){
			if(data == 'success') {
				layer.msg( "保存成功。", {
					icon: 1
				});
				$(".list-content").text(summary);
				$(".list-content").removeClass("hide");
				$("#contentArea").addClass("hide");
				$("#desEdit").removeClass("hide");
			}
		});
	});

});

$(function(){

	// markdown editor
	var editor;
	editor = new Editor();

	$('#detailEditSave').click(function() {
		var element = $('.infoLog .detail-contents');
		element.hide();
		$('#detailEditSave').hide();
		$('#contentEditor').removeClass('hide');
		editor.render($('#editor')[0]);
	});

	$('#cancelEdit').click(function(event) {
		$('#contentEditor').addClass('hide');
		$('.infoLog .detail-contents').show();
		$('#detailEditSave').show();
	});

	$('#saveContent').click(function(event) {
		var imageObj = {};
		var details = editor.codemirror.getValue();
		var imageId = $("#imageId").val();
		var self = this;
		$(self).text('保存中');
		$(self).attr('disabled', 'disabled');
		imageObj.name = $('.type-info .title').attr('imageName');
		imageObj.detail = details;
		imageObj.id = imageId;
		_saveImageConfigInfo(imageObj, function(result) {
			$(self).text('保存');
			$(self).removeAttr('disabled');
			if (result) {
				layer.msg(imageObj.name + "保存成功。", {
					icon: 1
				});
				$('#contentEditor').addClass('hide');
				$('.infoLog .detail-contents').show();
				$('#detailEditSave').show();
				var mdContent = marked(details);
				$('.infoLog .detail-contents').html(mdContent);
				$('.infoLog .detail-contents a').each(function(index, el) {
					$(el).attr('target', '_blank');
				});
			} else {
				//
			}
		});
	});

	function _saveImageConfigInfo(imageObj, callback) {
		$.ajax({
			url: ""+ctx+"/registry/detail/remark",
			data: {"imageId":imageObj.id,"remark":imageObj.detail},
			type: 'POST'
		}).done(function(resp) {
			callback(resp);
			 layer.msg(imageObj.name + " 信息保存成功。", {icon: 1});
		}).fail(function(err) {
			layer.msg("服务器错误，请稍后重试。", {
				icon: 8
			});
		});
	}

});

//管理员添加，同步本地数据库和私有仓库的镜像信息
function syncImages(){
	$.ajax({
		url:""+ctx+"/registry/refresh.do",
		success:function(data){
			data = eval("(" + data + ")");
			if(data.status=="200"){
				layer.msg("镜像同步成功",{
					icon:1,
					time:0,
					btn: ['确定'],
					yes:function(index, layero){
						layer.close(index);
						window.location.reload(true);
					}
				});
			}else{
				layer.alert("镜像同步失败，请检查服务器连接");
			}

		}
	})
}
