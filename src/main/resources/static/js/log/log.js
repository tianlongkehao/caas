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
                    		return data;
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