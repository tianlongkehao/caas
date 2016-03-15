$(document).ready(function(){
	loadImageList();
	
	$("#createButton").click(function(){
		var name = $('#serviceName').val();
		// check the name of container
	    if(!name || name.length < 1){
	      layer.tips('服务名称不能为空','#serviceName',{tips: [1, '#3595CC']});
	      $('#serviceName').focus();
	      return;
	    }
	    name = name.toLowerCase();
	    if(name.search(/^[a-z][a-z0-9-]*$/) === -1){
	      layer.tips('服务名称只能由字母、数字及横线组成，且首字母不能为数字及横线。','#serviceName',{tips: [1, '#3595CC'],time: 3000});
	      $('#serviceName').focus();
	      return;
	    }
	    if(name.length > 50 || name.length < 3){
	      layer.tips('服务名称为3~50个字符','#serviceName',{tips: [1, '#3595CC'],time: 3000});
	      $('#serviceName').focus();
	      return;
	    }
	    
	    var cpuNum = $('#cpuNum').val();
	    if(!cpuNum || cpuNum.length < 1){
		      layer.tips('cpu数量不能为空','#cpuNum',{tips: [1, '#3595CC']});
		      $('#cpuNum').focus();
		      return;
		    }
	    
	    var ram = $('#ram').val();
	    if(!ram || ram < 1){
		      layer.tips('内存不能为零','#ram',{tips: [1, '#3595CC']});
		      $('#ram').focus();
		      return;
		    }

		containerName();
    });
	
	$("#searchimage").click(function(){
		var imageName = $('#imageName').val();
		$.ajax({
	        url: ""+ctx+"/service/findimages.do?imageName="+imageName,
	        success: function (data) {
	            data = eval("(" + data + ")");

	            var html = "";
	            if (data != null) {

	                if (data['data'].length > 0) {
	                    for (var i in data.data) {
	                        var image = data.data[i];
	                        html += "<li class='image-item'><span class='img_icon span2'>"+
							"<img src='"+ctx+"/images/image-1.png'>"+
					"</span> <span class='span5 type' type='database'>"+
							"<div class='list-item-description'>"+
								"<div class='name h4'>"+
									""+ image.name +" <a title='点击查看镜像详情' target='_blank' href='"+ctx+"../registry/detail/"+image.id+"'>"+
										"<i class='fa fa-external-link-square'></i>"+
									"</a>"+
								"</div>"+
							"</div>"+
					"</span> <span class='span3'>"+
							"<div class='list-item-description'>"+
								"<span class='id h5' title='latest,5.6' value='"+ image.version+"'>版本:"+
									""+ image.version +"</span> <span imgID='"+image.id+"' imageName='"+image.name+"' imageVersion='"+image.version+"' class='pull-deploy btn btn-primary'"+
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
	                        	var imgID = $(this).attr("imgID");
	                        	//containerName();
	                            deploy(imgID,imageName, imageVersion);
	                        });
	                	}
	            }
	        }
	    })
	})


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

$(function(){
	var ramSlider = $("#ramSlider").slider({
		formatter: function(value) {
			return value;
		}
	});

	ramSlider.on("slide", function(slideEvt) {
		$("#ram").val(slideEvt.value);
	}).on("change", function(slideEvt){
		$("#ram").val(slideEvt.value.newValue);
	});

	$("#ram").on("change",function(){
		var ramVal = Number($(this).val());
		console.log(ramVal);
		ramSlider.slider('setValue', ramVal);
	});
});

function loadImageList() {
    $.ajax({
        url: ""+ctx+"/service/images",
        success: function (data) {
            data = eval("(" + data + ")");

            var html = "";
            if (data != null) {

                if (data['data'].length > 0) {
                    for (var i in data.data) {
                        var image = data.data[i];
                        html += "<li class='image-item'><span class='img_icon span2'>"+
						"<img src='"+ctx+"/images/image-1.png'>"+
				"</span> <span class='span5 type' type='database'>"+
						"<div class='list-item-description'>"+
							"<div class='name h4'>"+
								""+ image.name +" <a title='点击查看镜像详情' target='_blank' href='"+ctx+"../registry/detail/"+image.id+"'>"+
									"<i class='fa fa-external-link-square'></i>"+
								"</a>"+
							"</div>"+
						"</div>"+
				"</span> <span class='span3'>"+
						"<div class='list-item-description'>"+
							"<span class='id h5' title='latest,5.6' value='"+ image.version+"'>版本:"+
								""+ image.version +"</span> <span imgID='"+image.id+"' imageName='"+image.name+"' imageVersion='"+image.version+"' class='pull-deploy btn btn-primary'"+
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
                        	var imgID = $(this).attr("imgID");
                        	//containerName();
                            deploy(imgID,imageName, imageVersion);
                        });
                	}
            }
        }
    })
}

function deploy(imgID,imageName, imageVersion){

    $("#imgName").val(imageName);
    $("#imgVersion").val(imageVersion);
    $("#imgID").val(imgID);
    $(".step-inner").css("left","-100%");
    $(".createPadding").removeClass("hide");
    $(".radius_step").removeClass("action").eq(1).addClass("action");
    $(".two_step").removeClass("hide");
}

function containerName(){
	var serviceName = $("#serviceName").val();
			 $.ajax({
					url:""+ctx+"/service/serviceName.do?serviceName="+serviceName,
					success:function(data){
						data = eval("(" + data + ")");
						if(data.status=="400"){
							layer.alert("服务名称重复，请重新输入！");
							}else{
								$("#buildService").submit();
							}
					}
				
			 });
	 
}