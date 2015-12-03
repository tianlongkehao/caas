 $(document).ready(function () {
	$("#serviceReloadBtn").click(function(){
		$(".contentMain").load("/service");
	});
	$("#serviceCreateBtn").click(function(){
		$(".contentMain").load("/service/add");
	});
	$.ajax({
		url:"/service/listservice.do",
		success:function(data){
			data = eval("(" + data + ")");
			 if(data.status=="200"){
            	var html="";
            	if(data.data.length>0){
            		for(var i in data.data){
            			var ci = data.data[i];
            			html += "<tr class='clusterId'><td style='width:5%;text-indent: 30px;'>"+
                                 "<input type='checkbox' name='chkItem' value='' status='' imagename='' imagetag='' /></td>"+
                                  "<td style='width:20%;white-space:nowrap;'><b class='caret margin' style='transform: rotate(0deg);'></b>"+
                                   "<a href='#' class='cluster_mirrer_name'>mysql</a>"+
                                   "<span class='number-node'>1</span>"+
                                    "<span class='margin cursor console-code-modal' data-id='#console-code-modal'>"+
                                     "<i class='fa fa-desktop' onclick='_showConsole('mysql');'></i></span></td>"+
                                    "<td style='width:10%' id='mysqlstatus'>"+
                                    "<i class='fa_run'></i>"+
                                    "<span style='color: #65BC2C'>运行中 </span>"+
                                    "</td>"+
                                    "<td style='width:20%;'>"+
                                    "<img src='https://dn-tenxstore.qbox.me/tenxcloud_mysql.png?imageView2/2/h/20' style='max-height:20px;max-width:40px;'>"+
                                     "<span class='cluster_mirrer'>"+
                                     "<a title='点击查看镜像' target='_blank' href=''>tenxcloud/mysql</a>"+
                                     "</span></td>"+
                                     "<td style='width:34%' id='mysqlurl'>"+
                                     "<span class="url">"+
                                      "<a href='' target='_blank'>mysql-lynnxu.tenxapp.com:25314</a>"+
                                      "</span></td>"+
                                     "<td style='width:10%' class='tdTimeStrap'>"+
                                      "<input type='hidden' class='timeStrap' value='2015-11-30T02:23:28.000Z'>"+
                                      "<i class='fa_time'></i>"+
                                       "<span>1 天前</span></td></tr>"+
                                      "<tr style='border-left:1px solid #eee;'>"+
                                                        "<td colspan='8'><div class='align-center'>"+
                                                            "<table class='table'>"+
                                                                "<thead style='background: #FAFAFA;border-top:1px solid #EDECEC;'>"+
                                                                    "<tr class='tr-row'>"+
                                                                        "<td style='width:5%'>&nbsp;</td>"+
                                                                        "<td style='width:20%;'>"+
                                                                            "<a style='margin-left: 19px;' href='#'>mysql-s49b2</a>"+
                                                                        "</td>"+
                                                                        "<td colspan='2' style='width:30%'>"+
                                                                            "<i class='fa_run'></i>"+
                                                                            "<span style='color: #65BC2C'>运行中 </span>"+
                                                                        "</td>"+
                                                                        "<td style='width:34%'>mysql:3306&nbsp; （内网）</td>"+
                                                                        "<td style='width:10%'>"+
                                                                            "<i class='fa_time'></i>20 分钟前"+
                                                                        "</td>"+
                                                                    "</tr></thead></table></div></td></tr></tbody></table></div></td></tr>"
            		}
            	}
            	$("#serviceList").html(html);
            }else{
            	alert(data.msg);
            }
		}
	});
});