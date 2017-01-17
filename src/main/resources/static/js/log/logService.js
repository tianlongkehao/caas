$(document).ready(function () {
	$('.dataTables-example').dataTable({
	 	"aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0 ,3] }],
        "processing": true,
        "serverSide": true,
        "ordering":false,
//        "bStateSave":true,
        "ajax": ctx+"/logService/pager.do",
        "columns": [
					{   
						data : null,
						render : function ( data, type, row ) {
							var html = '<td>&nbsp;&nbsp;&nbsp;</td>';
							return html;
						}
					},
                    {   
                    	data : "createUserName",
                		render : function ( data, type, row ) {
                    		if (data == null || data == "") {
                    			return "";
                    		}
                    		return data;
                    	}	
                    },
                    { 	
                    	data : "serviceName",
                		render : function ( data, type, row ) {
                    		if (data == null || data == "") {
                    			return "";
                    		}
                    		var html = '<td><a onmouseover="serviceExtraInfo(this)" onmouseout="ExtraInfo(this)" serviceExtraInfo="'+row.serviceExtraInfo+'">'+data+'</a></td>';
							return html;
                    		
                    	}	
                    },
                    { 
                    	data : "operationType" ,
                		render : function ( data, type, row ) {
                    		if (data == null || data == "") {
                    			return "";
                    		}else if(data==10) {
                    			return "更新";
                    		}else if(data==20){
                    			return "创建";
                    		}else if(data==30){
                    			return "启动";
                    		}else if(data==40){
                    			return "停止";
                    		}else if(data==50){
                    			return "调试";
                    		}else if(data==60){
                    			return "弹性伸缩";
                    		}else if(data==70){
                    			return "版本升级";
                    		}else if(data==80){
                    			return "更改配置";
                    		}else if(data==90){
                    			return "删除";
                    		}else if(data==100){
                    			return "取消升级";
                    		}
                    	}	
                    },
                    { 
                    	data : 'createDate',
                    	render : function ( data, type, row ) {
                    		var date = calendarFormat(data);
                    		return date;
                        }
                    }
                   ]
	});
})

function serviceExtraInfo(obj){
	var serviceExtraInfo = $(obj).attr("serviceExtraInfo");
	/*layer.open({
		  type: 1,
		  title: false,
		  closeBtn: 0, //不显示关闭按钮
		  shade: [0],
		  area: ['340px', '215px'],
		  offset: $(obj), //右下角弹出
		  time: 4000, //2秒后自动关闭
		  anim: 2,
		  content: serviceExtraInfo, //iframe的url，no代表不显示滚动条
		  end: function(){ //此处用于演示
		    
		  }
		});*/
	layer.tips(serviceExtraInfo, $(obj) ,{
		  tips: [2, 'rgba(237,114,114,0.75)'],area: ['640px', '135px'],time:60000}
	);
}
function ExtraInfo(obj){
	
	layer.closeAll('tips'); 
}