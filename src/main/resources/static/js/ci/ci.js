 $(document).ready(function () {
	$("#ciReloadBtn").click(function(){
		$(".contentMain").load("/ci");
	});
	$("#ciAddBtn").click(function(){
		$(".contentMain").load("/ci/add");
	});
	$.ajax({
		url:"/ci/listCi.do",
		success:function(data){
			data = eval("(" + data + ")");
			 if(data.status=="200"){
            	var html="";
            	if(data.data.length>0){
            		for(var i in data.data){
            			var ci = data.data[i];
            			html += "<tr class='ci-listTr' style='cursor:auto'>"+
						            "<td style='width: 15%; text-indent:22px;'>"+
						                "<a href='' title='查看详细信息'>"+ci.projectName+"</a>"+
						            "</td>"+
						            "<td style='width: 12%;'>"+
						                "<i class='fa_success'></i>"+
						                "构建中"+
						                "<img src='images/loading4.gif' alt=''/>"+
						            "</td>"+
						            "<td style='width: 15%;'>"+
						                "<a data-toggle='tooltip' data-placement='left' title='' target='_blank' href='' data-original-title='查看源代码'>"+
						                    "<span class='bj-code-source'><i class='fa fa-git-square fa-lg'></i> oschina</span>"+
						                "</a>"+
						            "</td>"+
						            "<td style='width: 12%;'>1 小时前</td>"+
						            "<td style='width: 10%;'>45分11秒</td>"+
						            "<td style='width: 15%;'>"+
						                "<a target='_blank' title='' style='cursor:no-drop'>node</a>"+
						            "</td>"+
						            "<td style='width:18%'>"+
						                "<span class='bj-green' data-toggle='tooltip' data-placement='right' title='' data-original-title='重新构建'>构建&nbsp;&nbsp;<i class='fa fa-arrow-circle-right'></i></span>"+
						            "</td>"+
						        "</tr>"
            		}
            	}
            	$("#ciList").html(html);
            }else{
            	alert(data.msg);
            }
		}
	});
});