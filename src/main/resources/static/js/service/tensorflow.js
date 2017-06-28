/**
 * Creator：daien
 * Time：2017年6月28日10:57:56
 */
$(function(){
	loadTensorflowList();
	$("#add").click(function(){
		layer.open({
			type:1,
			title: '创建TensorFlow',
			area: ['500px', '500px'],
			content: $("#tensorflowAdd"),
			btn: ['确定', '取消'],
			yes: function(index, layero){

			}

	     });
});
})


/**
 * 加载数据
 */
function loadTensorflowList(){
	var itemsHtml = '';
	itemsHtml += '<tr>'
	+'<td style="width: 5%; text-indent: 30px;">'
	+'<input type="checkbox" class="chkItem" name="chkItem"/>'
	+'</td>'
	+'<td style="width: 15%;">'
	+'<a href="javascript:detail(1)" title="查看详细信息">'+'demo'+'</a>'
	+'</td>'
	+' <td style="width: 15%;">' + 1 +' </td>'
	+' <td style="width: 15%;">' + 4 + '</td>'
	+' <td style="width: 15%;">' + 'dddd' + ' M</td>'
	+' <td style="width: 15%;">' + '2016-10-11' + '</td>'
	+' <td style="width: 20%;">'
	+    ' <a class="format formatStorage" href="javascript:start(1)" title="开始"><i class="fa fa-play self_a"></i></a>'
	+	' <a class="dilation dilatationStorage" href="javascript:stop(1)"  title="停止" ><i class="fa fa-power-off"></i></a>'
	+	' <a class="delete deleteStorage" href="javascript:remove(1)" title="删除" ><i class="fa fa-trash"></i></a>'
	+'</td>'
	+'</tr>';

    $("#tensorflowList").html(itemsHtml);
}

/**
 * 启动
 * @param obj
 */
function start(id){
   alert("启动"+id+"成功！");
}

/**
 * 停止
 * @param obj
 */
function stop(id){
	alert("停止"+id+"成功！");
}

/**
 * 删除
 * @param obj
 */
function remove(id){
	alert("删除"+id+"成功！");
}

function detail(id){

}