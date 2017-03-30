$(function(){
	
}) 

function snapStrategyAdd(){
	layer.open({
		type:1,
		title: '创建自动快照策略',
		area: ['500px'],
		content: $("#snapStrategyInfo"),
		btn: ['确定', '取消'],
		yes: function(index, layero){
				
		}
	})
}

function snapStrategyEdit(){
	layer.open({
		type:1,
		title: '修改策略',
		area: ['500px'],
		content: $("#snapStrategyInfo"),
		btn: ['确定', '取消'],
		yes: function(index, layero){
				
		}
	})
}

function delOneStorage(){
	layer.open({
		title: '删除自动快照策略',
		content: "是否确定删除该自动快照策略？",
		btn: ['确定', '取消'],
		yes: function(index, layero){
				
		}
	})
}

function setStorage(){
	layer.open({
		type:1,
		title: '设置策略',
		area: ['900px'],
		content: $("#setStrategyInfo"),
		btn: ['执行快照策略', '取消'],
		yes: function(index, layero){
				
		}
	})
}






