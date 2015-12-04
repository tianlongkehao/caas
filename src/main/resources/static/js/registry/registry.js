$(document).ready(function () {
	loadImageList();
});

function loadImageList(){
	$.ajax({
		url: "/registry/images",
		success: function(data){
			data = eval("(" + data + ")");
			var html = "";
			 if(data != null){
        		for(var i in data.data){
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
				                            '<a href="" class="btn-pull-deploy btn">部署</a>' +
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
            }else{
            	$("#imageList").html(html);
            }
		}
	});
}