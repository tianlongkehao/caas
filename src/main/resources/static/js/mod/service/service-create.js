$(document).ready(function(){
	loadImageList();
	
	$("#createButton").click(function(){
        
//            $("#buildService").ajaxSubmit({
//                type: "post",
//                success: function (data) {
//                    data = eval("(" + data + ")");
//                    if (data.status == "200") {
//                        $(".contentMain").load("/service");
//                    } else {
//                        layer.alert(data.msg);
//                    }
//                }
//            });
		alert("123");
		$("#buildService").submit();
    });




    $(".two_step").click(function(){

        $(this).addClass("hide");
        $(".createPadding").removeClass("hide");
        $(".radius_step").removeClass("action").eq(2).addClass("action");
        $(".step-inner").css("left","-200%");

    });

    $(".go_backs").click(function(){

        if($(".radius_step").eq(1).hasClass("action")){
            $(".createPadding").addClass("hide");
            $(".step-inner").css("left","0");
            $(".radius_step").removeClass("action").eq(0).addClass("action");
        }else if($(".radius_step").eq(2).hasClass("action")){
            $(".createPadding").removeClass("hide");
            $(".step-inner").css("left","-100%");
            $(".radius_step").removeClass("action").eq(1).addClass("action");
        }

    });

});

function loadImageList() {
    $.ajax({
        url: "/service/images",
        success: function (data) {
            data = eval("(" + data + ")");

            var html = "";
            if (data != null) {
                if (data['data'].length > 0) {
                    for (var i in data.data) {
                        var image = data.data[i];
                        html += "<li class='image-item'><span class='img_icon span2'>"+
						"<img src='images/tenxcloud_mysql.png'>"+
				"</span> <span class='span5 type' type='database'>"+
						"<div class='list-item-description'>"+
							"<div class='name h4'>"+
								""+ image.name +" <a title='点击查看镜像详情' target='_blank' href=''>"+
									"<i class='fa fa-external-link-square'></i>"+
								"</a>"+
							"</div>"+
							"<span class='span2'> <i class='cloud_download'></i> <span"+
								"class='number'>12214</span>"+
							"</span> <i class='fa fa-star-o'></i> <span class='star'>46</span>"+
						"</div>"+
				"</span> <span class='span3'>"+
						"<div class='list-item-description'>"+
							"<span class='id h5' title='latest,5.6' value='"+ image.version+"'>版本:"+
								""+ image.version +"</span> <span imageName='"+image.name+"' imageVersion='"+image.version+"' class='pull-deploy btn btn-primary'"+
								"data-attr='tenxcloud/mysql'> 部署 <i"+
								"class='fa fa-arrow-circle-o-right margin fa-lg'></i>"+
							"</span>"+
						"</div>"+
				"</span></li>";
                    	}
                    	$("#imageList").html(html);
                        $(".pull-deploy").click(function(){
                        	var imageName = $(this).attr("imageName");
                        	var imageVersion = $(this).attr("imageVersion");

                            deploy(imageName, imageVersion);
                        });
                	}
            }
        }
    })
}

function deploy(imageName, imageVersion){
    $("#imageName").val(imageName);
    $("#imgVersion").val(imageVersion);
    $(".step-inner").css("left","-100%");
    $(".createPadding").removeClass("hide");
    $(".radius_step").removeClass("action").eq(1).addClass("action");
    $(".two_step").removeClass("hide");
}