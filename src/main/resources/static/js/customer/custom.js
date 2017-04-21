$(function(){

    // 全选  全选checkbox class设为 chkAll
    $(".chkAll").click(function(){
        $(".chkItem").prop('checked',$(".chkAll").is(":checked"));
    });
 
    // 每条数据 checkbox class设为 chkItem
    $(document).on("click",".chkItem", function(){
        if($(this).is(":checked")){
            if ($(".chkItem:checked").length == $(".chkItem").length) {
                $(".chkAll").prop("checked", "checked");
            }
        }else{
            $(".chkAll").prop('checked', $(this).is(":checked"));
        }
    });
//	// 主色调设置
//	var skinColor = $("#skinColor").attr("href");
//	if (skinColor == "") {
//		var redHref = ctx + '/css/core/color-red.css';
//		$("#skinColor").attr("href", redHref);
//	}
	// 主色调为红色
	$("#redColor").click(function() {
		setCookie('skinColor', 0);
		var redHref = ctx + '/css/core/color-red.css';
		$("#skinColor").attr("href", redHref);
	});
	// 主色调为蓝色
	$("#blueColor").click(function() {
		setCookie('skinColor', 1);
		var blueHref = ctx + '/css/core/color-blue.css';
		$("#skinColor").attr("href", blueHref);
	});
    
	checkCookie();
});

function sliderFn(sliderId, max, min, value){

    if(value == undefined){
        value = 10;
    }

    var sliderObj = $("#"+sliderId).slider({
        formatter: function(value) {
            return value;
        },
        max:max,
        min:min,
        value : value,
        tooltip:'hide'
    });

    sliderObj.on("slide", function(slideEvt) {
        $("#"+sliderId+'_input').val(slideEvt.value);
    }).on("change", function(slideEvt){
        $("#"+sliderId+'_input').val(slideEvt.value.newValue);
    });

    $("#"+sliderId+'_input').on("change",function(){
        var sliderVal = Number($(this).val());
        //sliderObj.setValue(sliderVal);
        sliderObj.slider('setValue', sliderVal);
    });

    return sliderObj;
}

function getCookie(c_name) {
	if (document.cookie.length > 0) {
		c_start = document.cookie.indexOf(c_name + "=")
		if (c_start != -1) {
			c_start = c_start + c_name.length + 1
			c_end = document.cookie.indexOf(";", c_start)
			if (c_end == -1)
				c_end = document.cookie.length
			return unescape(document.cookie.substring(c_start, c_end))
		}
	}
	return ""
}

function setCookie(c_name, value) {
	document.cookie = c_name + "=" + escape(value) + ";maxage=-1;path=/"
}

function checkCookie() {
	//判断主题
	skinColor = getCookie('skinColor')
	if (skinColor == null || skinColor == "") {
		
	}
	switch (skinColor) {
	case "0":
		var redHref = ctx + '/css/core/color-red.css';
		$("#skinColor").attr("href", redHref);
		break;
	case "1":
		var blueHref = ctx + '/css/core/color-blue.css';
		$("#skinColor").attr("href", blueHref);
		break;
	default:
		var redHref = ctx + '/css/core/color-red.css';
		$("#skinColor").attr("href", redHref);
	}
	//侧边栏
	label = getCookie('label')
	switch (label) {
	case "0":
		$(".navAll").addClass("hide");
		$(".page-container").css("margin-left","5px");
		$(".navbar-downSide-show").removeClass("hide");
		$(".sideBoxBtn").css("left","0px");
		break;
	case "1":
		$(".navAll").removeClass("hide");
		$(".page-container").css("margin-left","210px");
		$(".navbar-downSide-show").addClass("hide");
		$(".sideBoxBtn").css("left","185px")
		break;
	default:
		$(".navAll").removeClass("hide");
		$(".page-container").css("margin-left","210px");
		$(".navbar-downSide-show").addClass("hide");
		$(".sideBoxBtn").css("left","185px")
	}

}

function navHide(){
	$(".navAll").addClass("hide");
	$(".page-container").css("margin-left","5px");
	$(".navbar-downSide-show").removeClass("hide");
	$(".sideBoxBtn").css("left","0px");
	setCookie("label",0);
}
function navShow(){
	$(".navAll").removeClass("hide");
	$(".page-container").css("margin-left","210px");
	$(".navbar-downSide-show").addClass("hide");
	$(".sideBoxBtn").css("left","185px")
	setCookie("label",1);
}
//验证服务中文名称必须包含中文  只能是中文/^[\u4e00-\u9fa5]+$/
function isChinese(temp){
	 var re = /[\u4e00-\u9fa5]+/;  //必须包含中文
	 if(re.test(temp)){
		 return true;
	 } 
	 return false; 
}
