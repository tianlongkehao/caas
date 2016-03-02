function loadSensitiveWordsList(){
	if (currentTemplateId == null) {
    	layer.alert("请先选择一个模板");
        return;
    }
	
	var searchWord = $('#searchWord').val();
	var url = ctx + '/admin/sensitivewords/searchSensitiveWordsList';
	var json = {templateId:currentTemplateId, sort:"createDate,desc"};
	var searchFlag = false;
	if(searchWord != ''){
		json.searchWord = searchWord;
		searchFlag = true;
	}

	jqList.query(url,json, function(data){
		if(data.status == 200) {
        	var itemsHtml = '';
        	var len = data.list.length;
        	if(len == 0){
        		if(searchFlag) {
        			itemsHtml = '<tr><td colspan="4">未找到匹配的数据...</td></tr>';
        		}else {
        			itemsHtml = '<tr><td colspan="4">无数据...</td></tr>';
        		}
        	}
        	for(var i=0; i<len; i++){
        		(function(){
        			var sensitiveWords = data.list[i];
        			var statusHtml = '启用';
        			if(sensitiveWords.status==-1)statusHtml='停用';
        			itemsHtml += '<tr id="'+sensitiveWords.id+'" word="'+sensitiveWords.word+'" type="'+sensitiveWords.type+'" status="'+sensitiveWords.status+'">'+
	                    '<td>'+(i+1)+'</td>'+
	                    '<td>'+sensitiveWords.word+'</td>'+
	                    '<td>'+statusHtml+'</td>'+
	                    '<td class="operation">'+
	                       '<a class="edit_sw" href="javascript:void(0);"><i class="fa fa-pencil-square-o fa-lg"></i></a>'+
	                       '<a class="remove-sw" href="javascript:void(0);"><i class="fa fa-trash-o fa-lg"></i></a>'+
	                    '</td>'+
	                '</tr>';
        		})(i);
        	}
        	$('.sensitivewords-table tbody').html(itemsHtml);
        }
	});
}
