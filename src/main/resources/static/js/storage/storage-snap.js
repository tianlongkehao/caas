$(function(){
	
}) 

function storageRollBack(){
	layer.open({
		type:1,
			title: '回滚磁盘',
			area: ['500px'],
			content: $("#storageRollBack"),
			btn: ['确定', '取消'],
			yes: function(index, layero){
				
				}
	})
}
