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
                    	data : "commonName",
                		render : function ( data, type, row ) {
                    		if (data == null || data == "") {
                    			return "";
                    		}
                    		return data;
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