$(document).ready(function () {

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
    
    $("#deleteImage").click(function(){
   		var imageId = $("#imageId").val();
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
				   						window.location.href = "/registry/0";
				   					});
		   		     			}
		   		     		},
		   		     		error:function(){
			   		     		layer.msg( "删除失败，镜像被收藏", {
			   						icon: 1
			   					});
		   		     		}
		   		     	});
   		        	refresh();
   		        }
   		 });
    });
});


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

/*
function loadImageList() {
    $.ajax({
        url: "/registry/images",
        success: function (data) {
            data = eval("(" + data + ")");

            var html = "";
            if (data != null) {
                if (data['data'].length > 0) {
                    for (var i in data.data) {
                        var image = data.data[i];
                        html += '<li class="images-panel">' +
                            '<div class="select-img">' +
                            '<i class="fa fa-star-o star-style" style="color:#efa421"></i>' +
                            '<div class="mir-img ">' +
                            '<img src="images/image-1.png">' +
                            '</div>' +
                            '</div>' +
                            '<div class="select-info">' +
                            '<div class="pull-right-text">' + image.name + '</div>' +
                            '<div>' +
                            '<div class="pull-right">' +
                            '<a href="javascript:void(0);" class="btn-pull-deploy btn" imageversion='+image.version+' imagename='+image.name+'>部署</a>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '<div class="create-item">' +
                            '<a href="docker-registry-detail.html">' +
                            '<span class="note-text" title="' + image.remark + '">' + image.remark + '</span>' +
                            '</a>' +
                            '</div>' +
                            '</li>';
                    }
                    $("#imageList").html(html);
                } else {

                }
            } else {
            }

        }
    });
}*/
