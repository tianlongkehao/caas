
 function addNode(obj){
     var nodename = $(obj).attr('nodename');
     layer.open({
			title : '节点加入',
			content : '确定将节点加入集群吗？',
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
				layer.close(index);
				$.ajax({
					url : "" + ctx+ "/cluster/addnode?nodename="+ nodename,
					success : function(data) {
						data = eval("(" + data + ")");
						if (data.status == "200") {
							layer.msg("节点已加入集群",{icon: 6});
							setTimeout('window.location.reload()',1500);
						} else {
							layer.alert("节点加入失败，请检查服务器连接");
						}
					}
				});
			}
		});
 }

 function deleteNode(obj){
	 var nodename = $(obj).attr('nodename');
     layer.open({
			title : '节点删除',
			content : '确定将节点从集群中删除吗？',
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
				layer.close(index);
				$.ajax({
					url : "" + ctx+ "/cluster/deletenode?nodename="+ nodename,
					success : function(data) {
						data = eval("(" + data + ")");
						if (data.status == "200") {
							layer.msg("节点已被隔离",{icon: 6});
							setTimeout('window.location.reload()',1500);
						} else {
							layer.alert("节点隔离失败，请检查服务器连接");
						}
					}
				});
			}
		});
 }

 function nodedetail(obj){
   var nodename = $(obj).attr('nodename');
   $.ajax({
		url : ctx + "/cluster/nodedetail?nodename="+nodename,
		success: function(data) {
			var data = eval("("+data+")");

            $("#nodename").val(data.node.metadata.name);
            $("#ip").val(data.node.status.addresses[0].address);
            $("#status").val(data.status!=null?'隔离':'正常');
            $("#nodestatus").val(data.node.status.conditions[1].status=='True'?'Ready':'NotReady');
            $("#cpu").val(data.node.status.capacity.cpu);
            $("#memory").val(data.node.status.capacity.memory);
            $("#pod").val(data.node.status.capacity.pods);

            $('#podTable').empty();
            	for(var pod in data.pods){
                    var tr = '<tr><td>'+pod.metadata.name+'</td><td>'+pod.metadata.namespace+'</td><td>'+pod.status.conditions[0].status+'</td></tr>';
                    $('#podTable').append(tr);
            	};
            layer.open({
			 	type: 1,
		        title: '节点信息',
		        content: $("#nodedetail"),
		        area: ['600px'],
		        btn: false,
		    })

		}
	});

 }
