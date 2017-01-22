$(document).ready(function () {
	$("#delItemcfg").hide();
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
    
  
   
});/*ready*/

//折叠三角的方向
function filters(obj){
	$(obj).find("span.caret").toggleClass('caret-filter');
}

function findImages(){
	var index = $("#index").val();
	var userId = $("#userId").val();
	$('.dataTables-example').dataTable({
		 	"aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0 ,6] }],
		 	"autoWidth": false,
	        "processing": true,
	        "serverSide": true,
	        "ordering":false,
	        "stateSave":true,
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
	                    	data : 'createDate',
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
	                    						'<i class="fa fa-star-o star-style" style="color: #e8504f;margin-left:55px;"></i>'+
	                    					'</a>'
	                    			} else {
	                    				html += '<a class="no-drop a-oper forkquick" imageId="'+row.id+'">' + 
                									'<i class="fa fa-star star-style" style="color: #e8504f;margin-left:35px;"></i>'+
                								'</a>'
	                    			}
	                    		} else {
	                    			html += '<a class="no-drop" href="'+ctx+'/service/add?imageName='+row.name+'&imageVersion='+row.version+'&imgID='+row.id+'&resourceName='+row.resourceName+'"'+
										 		'imageversion="'+row.version+'" imagename="'+row.name+'" title="部署">'+
										 		'<i class="fa fa-wrench"></i>'+
										 	'</a>'+
										 	'<a class="no-drop a-oper downloadImage" imageversion="'+row.version+'" imagename="'+row.name+'" imgID="'+row.id+'" resourcename= "'+row.resourceName+'" title="导出">'+ 
										 		'<i class="fa fa-share-square-o"></i>'+
										 	'</a>' 
							 		if (row.currUserFavor == 0) {
	                    				html += '<a class="no-drop a-oper forkquick" imageId="'+row.id+'">' + 
	                    						'<i class="fa fa-star-o star-style" style="color: #e8504f;"></i>'+
	                    					'</a>'
	                    			} else {
	                    				html += '<a class="no-drop a-oper forkquick" imageId="'+row.id+'">' + 
                									'<i class="fa fa-star star-style" style="color: #e8504f;"></i>'+
                								'</a>'
	                    			}
	                    			
	                    			if (userId == row.createBy) {
	                    				html +=	'<a class="no-drop a-oper" href="javascript:void(0)" onclick="deleteImage(this)"'+
													'title="删除" imageversion="'+row.version+'" imagename="'+row.name+'" imageid="'+row.id+'">' +
													'<i class="fa fa-trash"></i>'+
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
		   						window.location.href="" + ctx + "/registry/1";
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
function checkbox(count){
	// 删除条件筛选中的   全选checkbox 
    var checkAllItem = '.checkAllItem'+count;
    var checkItem = '.checkItem'+count;
    $(checkAllItem).click(function(){
        $(checkItem).prop('checked',$(checkAllItem).is(":checked"));
    });
 
    // 删除条件筛选中的  选项
    $(document).on("click",checkItem, function(){
        if($(this).is(":checked")){
            if ($(checkItem+":checked").length == $(checkItem).length) {
                $(checkAllItem).prop("checked", "checked");
            }
        }else{
            $(checkAllItem).prop('checked', $(this).is(":checked"));
        }
    });
}
//批量删除镜像
function delImages(){
	$(".namefiltercon").empty();
	$(".timefiltercon").empty();
	var count = 1;
	$.ajax({
		url:""+ctx+"/registry/getImagesGroupByName.do",
		type:"post",
		success:function(data){
			data = eval("(" + data + ")");
			var dataLength = data.imagesGroupByName.length;
			for(var i = 0; i < dataLength; i++){
				var imageLength = data.imagesGroupByName[i].length;
				var imageName = data.imagesGroupByName[i][0].name;
				var imageHtml = '<div class="panel-group filter" id="accordion'+count+'">'+
				'<div class="panel panel-success">'+
				'<div class="panel-heading">'+
					'<h4 class="panel-title">'+
						'<input type="checkbox" class="checkAllItem'+count+'">'+
						'<a data-toggle="collapse" data-parent="#accordion'+count+'" href="#collapseOne'+count+'" onclick="filters(this)" class="imageName">'+
							'<span class="">'+imageName+'</span><span class="caret caret-filter"></span>'+
						'</a>'+
					'</h4>'+
				'</div>'+
				'<div id="collapseOne'+count+'" class="panel-collapse collapse in">'+
					'<div class="panel-body">'+
						'<ul class="image-version versionList'+count+'">'+
							
						'</ul>'+
					'</div></div></div></div>';
				$(".namefiltercon").append(imageHtml);
				var imageList = '';
				for(var j = 0; j < imageLength; j++){
					var imageListVersion = data.imagesGroupByName[i][j].version;
					var imageListId = data.imagesGroupByName[i][j].id;
					imageList += '<li><input type="checkbox" name="ids" value="'+imageListId+'" class="checkItem'+count+'"><span title="title"><i class="fa fa-tag"></i>&nbsp;'+imageListVersion+'</span></li>';
				}
				$('.versionList'+count+'').append(imageList);
				checkbox(count);
				count++;
			}
		}
	})
	$.ajax({
		url:""+ctx+"/registry/getImagesGroupByMonth.do",
		type:"post",
		success:function(data){
			data = eval("(" + data + ")");
			var allImages = data.allImages;
			for(var num=0; num<allImages.length; num++){
				var images = allImages[num];
				if(images.length != 0){
					var image0date = calendarFormat(images[0].createDate);
					var image0year = image0date.substring(0,4);
					var image0month = image0date.substring(5,7);
					var timeImages0Html = '<div class="panel-group filter" id="accordion'+count+'">'+
					'<div class="panel panel-info">'+
					'<div class="panel-heading">'+
						'<h4 class="panel-title">'+
							'<input type="checkbox" class="checkAllItem'+count+'">'+
							'<a data-toggle="collapse" data-parent="#accordion'+count+'" href="#collapseOne'+count+'" onclick="filters(this)" class="imageName">'+
								'<span class="">'+image0year+'年'+image0month+'月</span><span class="caret caret-filter"></span>'+
							'</a>'+
						'</h4>'+
					'</div>'+
					'<div id="collapseOne'+count+'" class="panel-collapse collapse in">'+
						'<div class="panel-body">'+
							'<ul class="image-version timeImageList'+count+'">'+
								
							'</ul>'+
						'</div></div></div></div>';
					$(".timefiltercon").append(timeImages0Html);
					var timeImageList = '';
					for(var m = 0; m < images.length; m++){
						var timeImagesListName = images[m].name;
						var timeImagesListVersion = images[m].version;
						var imageId = images[m].id;
						timeImageList += '<li><input type="checkbox" name="ids" value="'+imageId+'" class="checkItem'+count+'"><span title="'+timeImagesListName+'">'+timeImagesListName+'</span><br><span class="timeVersion"><i class="fa fa-tag"></i>&nbsp;'+timeImagesListVersion+'</span></li>';
					}
					$('.timeImageList'+count+'').append(timeImageList);
					checkbox(count);
					count++;
				}
			}
			
		}
	})
	
    layer.open({
    	type: 1,
        title: '批量删除条件',
        content: $("#delItemcfg"),
        area: ['880px','600px'],
        btn: ['确定删除', '取消'],
        yes: function(index, layero){ 
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
        	
			$.ajax({
				url:""+ctx+"/registry/delImages.do?imageIds="+imageIds,
				success:function(data){
					data = eval("(" + data + ")");
					if(data.status=="200"){
						layer.msg("镜像删除成功", {icon: 1 });
						setTimeout("window.location.reload(true)",1000);
					}else{
						layer.alert("镜像删除失败，请检查服务器连接");
					}

				}
			})
            layer.close(index);
        }
    })
}

$(function(){

	// 镜像备注编辑
	$("#desEdit").on('click', function(){
		$("#showContent").addClass("hide");
		$("#contentArea").removeClass("hide");
	});

	//取消保存
	$("#desEditCancel").on("click", function () {
		$("#showContent").removeClass("hide");
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
				$("#showContent").text(summary);
				$("#showContent").removeClass("hide");
				$("#contentArea").addClass("hide");
				$("#desEdit").removeClass("hide");
				layer.msg( "保存成功。", {
					icon: 1
				});
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
