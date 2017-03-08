$(document).ready(function () {
	$('.dataTables-example').dataTable({
	 	"aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0 ,3] }],
        "processing": true,
        "serverSide": true,
        "ordering":false,
//        "bStateSave":true,
        "ajax": ctx+"/logCommon/pager.do",
        "columns": [
					{   
						data : null,
						render : function ( data, type, row ) {
							var html = '<td>&nbsp;&nbsp;&nbsp;</td>';
							return html;
						}
					},
					{   
                    	data : "createUsername",
                		render : function ( data, type, row ) {
                    		if (data == null || data == "") {
                    			return "";
                    		}
                    		return data;
                    	}	
                    },
                    {   
                    	data : "catalogType",
                		render : function ( data, type, row ) {
                    		if (data == null || data == "") {
                    			return "";
                    		}else if(data==10) {
                    			return "dockerfile模板";
                    		}else if(data==20){
                    			return "环境变量模板";
                    		}else if(data==30){
                    			return "代码构建";
                    		}else if(data==40){
                    			return "快速构建";
                    		}else if(data==50){
                    			return "dockerfile构建";
                    		}else if(data==60){
                    			return "上传镜像";
                    		}else if(data==70){
                    			return "镜像中心";
                    		}else if(data==80){
                    			return "用户管理";
                    		}else if(data==90){
                    			return "秘钥管理";
                    		}else if(data==100){
                    			return "租户管理";
                    		}
                    		else if(data==110){
                    			return "shera管理 ";
                    		}
                    		else if(data==120){
                    			return "存储与备份 ";
                    		}
                    		else if(data==130){
                    			return "外部服务 ";
                    		}
                    		return data;
                    	}	
                    },
                    { 	
                    	data : "commonName",
                		render : function ( data, type, row ) {
                    		if (data == null || data == "") {
                    			return "";
                    		}
                    		var html = "<td><a class='serviceExtraInfo' onmouseover='comExtraInfo(this)' onmouseout='ExtraInfo(this)' comExtraInfo='"+row.extraInfo+"'>"+data+"</a></td>";
							return html;
                    	}	
                    },
                    { 
                    	data : "operationType" ,
                		render : function ( data, type, row ) {
                    		if (data == null || data == "") {
                    			return "";
                    		}else if(data==10) {
                    			return "创建";
                    		}else if(data==20){
                    			return "更新";
                    		}else if(data==30){
                    			return "删除";
                    		}else if(data==40){
                    			return "格式化";
                    		}else if(data==50){
                    			return "扩容";
                    		}else if(data==60){
                    			return "部署";
                    		}else if(data==70){
                    			return "导出";
                    		}else if(data==80){
                    			return "构建镜像";
                    		}else if(data==90){
                    			return "停止构建镜像";
                    		}else if(data==100){
                    			return "删除一个代码构建执行 ";
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
var tip_index = 0;
function comExtraInfo(obj){
	var comExtraInfo = $(obj).attr('comExtraInfo');
	tip_index=layer.tips(comExtraInfo, $(obj) ,{
		  tips: [3, 'rgba(237,114,114,0.75)'],area: ['550px', '150px'],time:0,tipsMore: true}
	);
}

function ExtraInfo(obj){
	layer.close(tip_index); 
}