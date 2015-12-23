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
    
    $(document).on("click", ".btn-pull-deploy", function () {
        var imageName = $(this).attr("imagename");
        var imageVersion = $(this).attr("imageversion");
        window.location.href = "/service/add?imageName="+imageName+"&imageVersion="+imageVersion;
    });
    
    $(".fork").click(function(){
    	
    	var imageId = $("#imageId").val();
    	$.ajax({
    		url:"/registry/detail/favor",
    		type:"post",
    		data:{"imageId":imageId},
    		success:function(data){
    			if(data == "success"){
    				$("#collectTxt").text("已收藏");
    				$(".star-style").removeClass("fa-star-o").addClass("fa-star");
    			}else{
    				$("#collectTxt").text("收藏");
    				$(".star-style").removeClass("fa-star").addClass("fa-star-o");
    			}
    		}
    		
    	});
    });
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
