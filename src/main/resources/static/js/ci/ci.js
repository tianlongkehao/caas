$(document).ready(function () {
	$("#ciReloadBtn").click(function(){
		loadCiList();
	});
	$("#ciAddBtn").click(function(){
		$(".contentMain").load("/ci/add");
	});
	loadCiList();

    $(document).on("click", ".bj-green", function(){

        layer.open({
            title: '快速构建',
            content: '里面可以写内容',
            btn: ['确定', '取消'],
            yes: function(index, layero){ //或者使用btn1
                //按钮【按钮一】的回调
            },
            cancel: function(index){ //或者使用btn2
                //按钮【按钮二】的回调
            }
        });

    });

});
function loadCiList(){
	$.ajax({
		url:"/ci/listCi.do",
		success:function(data){
			data = eval("(" + data + ")");
			 if(data.status=="200"){
            	var html="";
            	if(data.data.length>0){
            		for(var i in data.data){
            			var ci = data.data[i];
            			var constructionStatusHtml = "";
            			if(ci.constructionStatus==1){//构建中
            				constructionStatusHtml = "<i class='fa_success'></i>"+
										                "构建中"+
										                "<img src='images/loading4.gif' alt=''/>";
            			}else if(ci.constructionStatus==2){//完成
            				constructionStatusHtml = "<i class='fa_stop'></i>"+
 							"完成";
            			}else if(ci.constructionStatus==3){//失败
                            constructionStatusHtml = "<i class='fa_stop'></i>"+
			                							"失败";
            			}
            			var codeTypeHtml = "";
            			if(ci.codeType==1){//svn
            				codeTypeHtml = "<span class='bj-code-source'><i class='fa fa-git-square fa-lg'></i> svn</span>";
            			}else if(ci.codeType==2){//git
            				codeTypeHtml = "<span class='bj-code-source'><i class='fa fa-git-square fa-lg'></i> git</span>";
            			}
            			html += "<tr class='ci-listTr' style='cursor:auto'>"+
						            "<td style='width: 15%; text-indent:22px;'>"+
						                "<a href='viewCidetail("+ci.id+")' title='查看详细信息'>"+ci.projectName+"</a>"+
						            "</td>"+
						            "<td style='width: 12%;'>"+
						            	constructionStatusHtml+
						            "</td>"+
						            "<td style='width: 15%;'>"+
						                "<a data-toggle='tooltip' data-placement='left' title='' target='_blank' href='"+ci.codeUrl+"' data-original-title='查看源代码'>"+
						                	codeTypeHtml+
						                "</a>"+
						            "</td>"+
						            "<td style='width: 12%;'>"+ci.constructionDate+"</td>"+
						            "<td style='width: 10%;'>"+ci.constructionTime+"</td>"+
						            "<td style='width: 15%;'>"+
						                "<a target='_blank' title='' style='cursor:no-drop'>"+ci.imgNameLast+"</a>"+
						            "</td>"+
						            "<td style='width:18%'>"+
						                "<span class='bj-green' data-toggle='tooltip' data-placement='right' title='' data-original-title='重新构建'>构建&nbsp;&nbsp;<i class='fa fa-arrow-circle-right'></i></span>"+
						            "</td>"+
						        "</tr>"
            		}
            	}
            	$("#ciList").html(html);
            }else{
                 layer.alert(data.msg);
            }
		}
	});
}